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

	// Set of InetSocketAddresses - ensures distinct elements 
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
		Set<InetSocketAddress> cpointsToSend;
		SynchronizeStatus result = SynchronizeStatus.Default;
		switch (command.status) {
		case Created:
			// Target server creates a syncmessage and sends its connectionpoints and contacts to 
			// the joining server 
			// broadcasting of joinserver has already been done in addconnectionpoint
			cpointsToSend = new HashSet<InetSocketAddress>();
			cpointsToSend.addAll(getConnectionPoints());
			command.cpoints = cpointsToSend;

			// Add target server
			command.cpoints.add(command.getSender());
			command.contacts = this.phonebook.GetAllContacts();
			command.status = SynchronizeStatus.SendFromTarget;
			result = command.status;
			// ExecuteAndSend(command);
			Send(command, command.getReceiver());
			break;
		case SendFromTarget:
			// Joining server receives sync message from server
			// Returns connectionpoints and contacts 
			Set<InetSocketAddress> cpointsReceived = command.cpoints;
			this.phonebook.Synchronize(command.contacts);
			cpointsToSend = new HashSet<InetSocketAddress>();
			cpointsToSend.addAll(getConnectionPoints());
			command.cpoints = cpointsToSend;

			getConnectionPoints().addAll(cpointsReceived);
			command.contacts = this.phonebook.GetAllContacts();
			command.status = SynchronizeStatus.SendFromJoining;
			
			//ExecuteAndSend(command);
			InetSocketAddress tmp = command.getSender();
			command.setSender(command.getReceiver());
			command.setReceiver(tmp);
			result = command.status;
			Send(command, tmp);
			break;
		case SendFromJoining:
			// Server receives sync response from joining server
			getConnectionPoints().addAll(command.cpoints);
			this.phonebook.Synchronize(command.contacts);
			// Broadcast to servers
			command.status = SynchronizeStatus.BroadCast;
			broadcast(command);
			result =  SynchronizeStatus.Synchronized;
		case BroadCast:
			getConnectionPoints().addAll(command.cpoints);
			this.phonebook.Synchronize(command.contacts);
			result = SynchronizeStatus.Synchronized;
		default : 
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see bok.labexercise4.IPhonebookServer#addConnectionPoint(bok.labexercise4.ServerCommand)
	 */
	/***
	 * Adds a connectionpoint to a server
	 */
	public ServerResult addConnectionPoint(ServerCommand command) throws IOException {
		if (!this.getIP().equals(command.getReceiver())) {
			return cPoints.add(command.getSender())  ? ServerResult.Added : ServerResult.AlreadyAdded;
		}  else {
			try {
				// First broadcast then add
				ServerResult result =null;
				if (!cPoints.contains(command.getReceiver())) { 
					result = broadcast(command);
				
				
				//Synchronize from target server to joining server
				SynchronizeCommand syncCommand =  
					new SynchronizeCommand(getIP(),  
							command.getSender());
				syncCommand.status = SynchronizeStatus.Created;
				syncCommand.Execute(this);
				cPoints.add(command.getSender());
				} 
				// Add the joining server to target server
				
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
	public Object removeConnectionPoint(ServerCommand command) {
		// The message was sent from server to joiner
		if(this.getIP().equals(command.getReceiver())) {
			cPoints.remove(command.getSender());
			try {
				broadcast(command);
				return ServerResult.RemoveServerInitiatedFromServer;
			} catch (IOException e){
				e.printStackTrace();
				return ServerResult.Removed;
			}
		} else if (command.getSender().equals(getIP())) {
			cPoints.removeAll(cPoints);
			return ServerResult.JoiningServerRemoved;

		} else {
			System.out.println("Broadcast server remove");
			cPoints.remove(command.getSender());
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
		Set<InetSocketAddress> cpList=	getConnectionPoints();
		for(InetSocketAddress cp : cpList) {
			if (command.getSender()!= cp) Send(command, cp);
		}
		return ServerResult.BroadCast;
	}
	/***
	 * Handles execution of commands of type ServerCommand
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend (ServerCommand command) throws IOException {
		Trace("Executing SERVERCOMMAND");
		Object result = command.Execute(this);
		//
		Send(result, command.getSender());
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
		StringBuilder tracer = new StringBuilder();
		tracer.append(String.format("%s: \nCommand: %s\n", getIP(), command.getClass())); 
		if (command instanceof ReplicateCommand) {
			tracer.append(String.format("Receiving from: %s \n", ((ReplicateCommand) command).getSender()));
			Trace(tracer.toString());
			ExecuteAndSend((ReplicateCommand) command);
		} else
			// The command is sent from a client
			if (command instanceof Command) {
				tracer.append(String.format("Receiving from: %s \n", ((Command) command).getSender()));
				Trace(tracer.toString());
				ExecuteAndSend((Command) command);

			} else if (command instanceof SynchronizeCommand ) {
				tracer.append(String.format("Receiving from: %s \n", ((SynchronizeCommand) command).getSender()));
				Trace(tracer.toString());
				ExecuteAndSend((SynchronizeCommand) command);
			}
		// The command is sent from another server
			else if (command instanceof ServerCommand) {
				tracer.append(String.format("Receiving from: %s \n", ((ServerCommand) command).getSender()));
				Trace(tracer.toString());
				ExecuteAndSend((ServerCommand)command);
			}
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
		Trace("Executing SERVERRESULT: " + result);
		
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
		Trace("Executing REPLICATECOMMAND: ");
		Object result = null;
		if (command.getReceiver() != null) {
			result = command.Execute (phonebook);
			Send(result, command.getSender());
			if (command.getReceiver().equals(getIP())) 
			{
				broadcast(command); 
			}
		} else {
			command.setReceiver(getIP());
			ExecuteAndSend(command);
		}
	}

	/**
	 * Default handler for commands sent to the server
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend (Command command) throws IOException {
		Object result = command.Execute (phonebook);
		Send(result, command.getSender());
	}

	/***
	 * Handles synchronization commands 
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend(SynchronizeCommand command) throws IOException {
		Object result = command.Execute(this);
		Send(result, command.getSender());
	} 

	public void Send(Object o, InetSocketAddress receiver) throws IOException {
		System.out.printf("\nTRACE for %s: \n", getIP()); 
		System.out.printf("TRACE: Command: %s \n", o.getClass());
		System.out.printf("TRACE: Sending to: %s \n", receiver);
		Socket client = new Socket ();
		try {
			client.connect (receiver);
			OutputStream os = client.getOutputStream ();
			ObjectOutputStream oos = new ObjectOutputStream (os);				
			oos.writeObject (o);
		} finally {
			if (client != null)
				client.close ();
		}
	} 

	/***
	 * Returns the server's phonebook 
	 * @return
	 */
	public IPhonebook getPhoneBook() {
		return this.phonebook;
	}

	private void Trace(String s) {
		if (printServerResults()) {
			System.out.printf("\nTRACE: %s\n", s);
		}
	}
	public Set<InetSocketAddress> getConnectionPoints(
			GetConnectionPointsCommand command) {
		// TODO Auto-generated method stub
		return null;
	} 
}