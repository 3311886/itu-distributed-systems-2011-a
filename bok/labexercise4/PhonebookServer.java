package bok.labexercise4;
// Phonebook server accepts commands and executes them.

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class PhonebookServer extends AbstractServer implements IPhonebookServer{

	// Set of InetSocketAddresses
	private Set<InetSocketAddress> cPoints;
	// Storage
	private IPhonebook phonebook;


	public PhonebookServer (int port) throws IOException {
		// Call superclass constructor
		super(port);
		cPoints = new  HashSet<InetSocketAddress>();
		phonebook = new ReplicatedPhonebook();
	}
	public PhonebookServer () throws IOException {
		super();
		cPoints = new  HashSet<InetSocketAddress>();
		phonebook = new ReplicatedPhonebook();
	}

	/***
	 * Synchronizes connection points and contacts 
	 * @param command SynchronizeCommand
	 * @return Status of synchronization
	 * @throws IOException
	 */
	public SynchronizeStatus synchronize(SynchronizeCommand command) throws IOException {
		// A synchronize action was initiated from addConnectionPoint()
		if (command.status.equals(SynchronizeStatus.Created)) {
			// Target server creates a syncmessage and sends its connectionpoints and contacts to 
			// the joining server 
			Set<InetSocketAddress> cpointsToSend = new HashSet<InetSocketAddress>();
			cpointsToSend.addAll(getConnectionPoints());
			command.cpoints = cpointsToSend;

			// Add target server
			command.cpoints.add(command.getTargetServer());
			command.contacts = this.phonebook.GetAllContacts();
			command.status = SynchronizeStatus.SendFromTarget;
			ExecuteAndSend(command);
		} else if (command.status.equals(SynchronizeStatus.SendFromTarget)){
			// Joining server receives sync message from server
			// Returns connectionpoints and contacts 

			Set<InetSocketAddress> cpointsReceived = command.cpoints;

			this.phonebook.Synchronize(command.contacts);
			Set<InetSocketAddress> cpointsToSend = new HashSet<InetSocketAddress>();
			cpointsToSend.addAll(getConnectionPoints());
			command.cpoints = cpointsToSend;

			getConnectionPoints().addAll(cpointsReceived);
			command.contacts = this.phonebook.GetAllContacts();
			command.status = SynchronizeStatus.SendFromJoining;
			ExecuteAndSend(command);

		} else if (command.status.equals(SynchronizeStatus.SendFromJoining)) {
			// Server receives sync response from joining server 
			getConnectionPoints().addAll(command.cpoints);
			this.phonebook.Synchronize(command.contacts);
			command = null;
			return SynchronizeStatus.Synchronized;
		}
		return command.status;
	}
	/* (non-Javadoc)
	 * @see bok.labexercise4.IPhonebookServer#addConnectionPoint(bok.labexercise4.ServerCommand)
	 */
	/***
	 * Adds a connectionpoint to a server
	 */
	public ServerResult addConnectionPoint(ServerCommand command) throws IOException {
		if (!this.getIP().equals(command.getTargetServer())) {
			return cPoints.add(command.getJoiningServer())  ? ServerResult.Added : ServerResult.AlreadyAdded;
		}  else {
			try { 
				// First broadcast then add
				ServerResult result = broadcast(command);
				//Synchronize from target server to joining server
				SynchronizeCommand syncCommand =  
					new SynchronizeCommand(command.getJoiningServer(),  
							getIP());
				syncCommand.status = SynchronizeStatus.Created;
				syncCommand.Execute(this);
				
				// Add the joining server to target server
				cPoints.add(command.getJoiningServer());
				return result;
			} catch (IOException e) {
				e.printStackTrace();
				return ServerResult.UnknownError;
			}		
		}
	}

	/* (non-Javadoc)
	 * @see bok.labexercise4.IPhonebookServer#removeConnectionPoints(bok.labexercise4.ServerCommand)
	 */
	/***
	 * Removes a connection from a server, must be initiated from the server not the joiner
	 */
	public Object removeConnectionPoints(ServerCommand command) {
		// The message was sent from server to joiner
		if(this.getIP().equals(command.getTargetServer())) {
			cPoints.remove(command.getJoiningServer());
			try {
				broadcast(command);
				return ServerResult.RemoveServerInitiatedFromServer;
			} catch (IOException e){
				e.printStackTrace();
				return ServerResult.Removed;
			}
		} else if (command.getJoiningServer().equals(getIP())) {
			cPoints.removeAll(cPoints);
			return ServerResult.JoiningServerRemoved;

		} else {
			System.out.println("Broadcast server remove");
			cPoints.remove(command.getJoiningServer());
			return ServerResult.Removed;
		} 
	} 
	/* (non-Javadoc)
	 * @see bok.labexercise4.IPhonebookServer#broadcast(bok.labexercise4.ICommand)
	 */
	/***
	 * Broadcasts a command to connected servers    
	 */
	public ServerResult broadcast(ICommand command) throws IOException 
	{
		Socket client = null;
		Set<InetSocketAddress> cpList=	getConnectionPoints();
		for(InetSocketAddress cp : cpList) {
			client = new Socket();
			client.connect(cp);
			OutputStream os = client.getOutputStream ();
			ObjectOutputStream oos = new ObjectOutputStream (os);
			oos.writeObject (command);
		}
		if (client != null) client.close ();

		return ServerResult.AddedAndBroadCast;
	}
	/***
	 * Handles execution of commands of type ServerCommand
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend (ServerCommand command) throws IOException {
		Object result = command.Execute(this);

		Socket client = new Socket();
		try {
			client.connect(command.getJoiningServer());
			OutputStream os = client.getOutputStream ();
			ObjectOutputStream oos = new ObjectOutputStream (os);
			oos.writeObject (result);
		} 
		finally {
			if (client != null)
				client.close ();
		} 	
	}

	/* (non-Javadoc)
	 * @see bok.labexercise4.IPhonebookServer#getConnectionPoints()
	 */
	/***
	 * Returns a set containing connectionpoints
	 */
	public Set<InetSocketAddress> getConnectionPoints() {
		return cPoints;
	}

	@Override
	/***
	 * Overridden method from AbstractServer to handle requests 
	 */
	void ExecuteAndSend(Object command) throws IOException {
		if (command instanceof ReplicateCommand) ExecuteAndSend((ReplicateCommand) command);
		// The command is sent from another server
		// The command is sent from a client
		if (command instanceof Command) ExecuteAndSend((Command) command);
		// The command is sent from another server
		else if (command instanceof ServerCommand) ExecuteAndSend((ServerCommand)command);
		// The command is a reply from another server
		else if (command instanceof ServerResult) { 
			ExecuteAndSend((ServerResult) command);
		}
	}

	/***
	 * Handles ServerResult responses 
	 * @param result
	 */
	void ExecuteAndSend(ServerResult result) {
		System.out.println(result);
		switch (result) {
		case RemoveServerInitiatedFromServer : 
			cPoints.removeAll(cPoints);
		}
	}

	/***
	 * Handles client commands that may be broadcast 
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend(ReplicateCommand command) throws IOException {
		Object result = null;
		if (command.ForwardTo != null) {
			result = command.Execute (phonebook);

			if (command.ForwardTo.equals(getIP())) 
			{
				broadcast(command); 
			}
		} else {
			command.ForwardTo = getIP();
			ExecuteAndSend(command);
		}

		// Do something 
		Socket client = new Socket ();
		try {
			client.connect (command.ReturnTo);
			OutputStream os = client.getOutputStream ();
			ObjectOutputStream oos = new ObjectOutputStream (os);
			oos.writeObject (result);
		} finally {
			if (client != null)
				client.close ();
		}
	}

	/**
	 * Default handler for commands sent to the server
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend (Command command) throws IOException {
		Object result = command.Execute (phonebook);

		Socket client = new Socket ();
		try {
			client.connect (command.ReturnTo);
			OutputStream os = client.getOutputStream ();
			ObjectOutputStream oos = new ObjectOutputStream (os);
			oos.writeObject (result);
		} finally {
			if (client != null)
				client.close ();
		}
	}

	/***
	 * Handles synchronization commands 
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend(SynchronizeCommand command) throws IOException {

		if (command.status.equals(SynchronizeStatus.SendFromTarget) && command.getTargetServer().equals(getIP())) {
			// Send to joining server
			Socket client = new Socket ();
			try {

				client.connect (command.getJoiningServer());
				OutputStream os = client.getOutputStream ();
				ObjectOutputStream oos = new ObjectOutputStream (os);				
				oos.writeObject (command);
			} finally {
				if (client != null)
					client.close ();
			}
		}

		if (command.status.equals(SynchronizeStatus.SendFromTarget) && command.getJoiningServer().equals(getIP())) { 
			command.Execute(this);
		}

		if (command.status.equals(SynchronizeStatus.SendFromJoining) && !command.getTargetServer().equals(getIP())) {
			// Send sync message from joining to
			Socket client = new Socket ();
			try {
				client.connect (command.getJoiningServer());
				OutputStream os = client.getOutputStream ();
				ObjectOutputStream oos = new ObjectOutputStream (os);				
				oos.writeObject (command);
			} finally {
				if (client != null)
					client.close ();
			}

		}
	} 
	
	/***
	 * Returns the server's phonebook 
	 * @return
	 */
	public IPhonebook getPhoneBook() {
		return this.phonebook;
	}
}