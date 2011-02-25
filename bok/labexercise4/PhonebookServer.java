package bok.labexercise4;
// Phonebook server accepts commands and executes them.

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class PhonebookServer extends AbstractServer implements IPhonebookServer{
	private Set<ConnectionPoint> cPoints;
	private IPhonebook phonebook;

	public PhonebookServer (int port) throws IOException {
		super(port);
		cPoints = new  HashSet<ConnectionPoint>();
		phonebook = new ReplicatedPhonebook();
	}
	public PhonebookServer () throws IOException {
		super();
		cPoints = new  HashSet<ConnectionPoint>();
		phonebook = new ReplicatedPhonebook();
	}

	public void ReqestAllContacts(RequestAllContactsCommand command) throws IOException {
		this.getPhoneBook().Synchronize(command.contacts);
		SynchronizeCommand scommand = new SynchronizeCommand(command.getJoiningServer(), command.getTargetServer());
		ExecuteAndSend(scommand);		  
	}

	public SynchronizeStatus synchronize(SynchronizeCommand command) throws IOException {
		if (command.status.equals(SynchronizeStatus.Created)) {
			// Target server creates a syncmessage and sends it to 
			// the joining server
			Set<ConnectionPoint> cpointsToSend = new HashSet<ConnectionPoint>();
			cpointsToSend.addAll(getConnectionPoints());
			command.cpoints = cpointsToSend;
			// Add target server
			command.cpoints.add(command.getTargetServer());
			System.out.println("points to send from server: " + command.cpoints);
			command.status = SynchronizeStatus.SendFromTarget;
			ExecuteAndSend(command);
		} else if (command.status.equals(SynchronizeStatus.SendFromTarget)){
			// Joining server receives
			System.out.println("points to receive: " + command.cpoints);
			Set<ConnectionPoint> cpointsReceived = command.cpoints;
			Set<ConnectionPoint> cpointsToSend = new HashSet<ConnectionPoint>();
			cpointsToSend.addAll(getConnectionPoints());
			command.cpoints = cpointsToSend;
			System.out.println("points to to send from joining: " + command.cpoints);
			
			
			getConnectionPoints().addAll(cpointsReceived);
			System.out.println("Joining server cpoints: " + getConnectionPoints());
			
			command.status = SynchronizeStatus.SendFromJoining;
			ExecuteAndSend(command);

		} else if (command.status.equals(SynchronizeStatus.SendFromJoining)) {
			getConnectionPoints().addAll(command.cpoints);
			command = null;
			return SynchronizeStatus.Synchronized;
		}
		
		return command.status;
	}
	/* (non-Javadoc)
	 * @see bok.labexercise4.IPhonebookServer#addConnectionPoint(bok.labexercise4.ServerCommand)
	 */
	public ServerResult addConnectionPoint(ServerCommand command) throws IOException {
		if (!this.getIP().equals(command.getTargetServer().getISA())) {
			return cPoints.add(command.getJoiningServer()) ? ServerResult.Added : ServerResult.AlreadyAdded;
		}  else {
			try { 
				// First broadcast then add
				ServerResult result = broadcast(command);
				//Synchronize from target server to joining server
				SynchronizeCommand syncCommand =  
					new SynchronizeCommand(command.getJoiningServer(),  
							new ConnectionPoint(getIP()));
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
	public ServerResult removeConnectionPoints(ServerCommand command) {
		if(!this.getIP().equals(command.getTargetServer())) {
			cPoints.remove(command.getJoiningServer());
			return ServerResult.Removed;
		} else {
			cPoints.remove(command.getJoiningServer());
			try {
				broadcast(command);
				return ServerResult.RemovedAndBroadCast;
			} catch (IOException e){
				e.printStackTrace();
				return ServerResult.Removed;
			}	
		} 
	} 
	/* (non-Javadoc)
	 * @see bok.labexercise4.IPhonebookServer#broadcast(bok.labexercise4.ICommand)
	 */
	public ServerResult broadcast(ICommand command) throws IOException 
	{
		Socket client = null;

		Set<ConnectionPoint> cpList = getConnectionPoints();
		for (ConnectionPoint cp : cpList) {
			client = new Socket();
			client.connect(cp.getISA());
			OutputStream os = client.getOutputStream ();
			ObjectOutputStream oos = new ObjectOutputStream (os);
			oos.writeObject (command);
		}
		if (client != null) client.close ();

		return ServerResult.AddedAndBroadCast;
	} 


	/***
	 * 
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend (ServerCommand command) throws IOException {
		Object result = command.Execute(this);

		Socket client = new Socket();
		try {
			client.connect(command.getJoiningServer().getISA());
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
	public Set<ConnectionPoint> getConnectionPoints() {
		return cPoints;
	}

	@Override
	void ExecuteAndSend(Object command) throws IOException {
		if (command instanceof ReplicateCommand) ExecuteAndSend((ReplicateCommand) command);
		// The command is sent from another server
		// The command is sent from a client
		if (command instanceof Command) ExecuteAndSend((Command) command);
		// The command is sent from another server
		else if (command instanceof ServerCommand) ExecuteAndSend((ServerCommand)command);
		// The command is a reply from another server
		else if (command instanceof ServerResult && printServerResults()) { 
			System.out.println(command.toString());
		}
	}

	void ExecuteAndSend(ServerResult result) {

	}

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

	void ExecuteAndSend(SynchronizeCommand command) throws IOException {
		System.out.println( getIP() + ":"  + command.status);
		if (command.status.equals(SynchronizeStatus.SendFromTarget) && command.getTargetServer().getISA().equals(getIP())) {
			// Send to joining server
			Socket client = new Socket ();
			try {
				System.out.println("Server send:" +  command.cpoints);
				System.out.println("Joining server:" +  command.getJoiningServer());
				client.connect (command.getJoiningServer().getISA());
				OutputStream os = client.getOutputStream ();
				ObjectOutputStream oos = new ObjectOutputStream (os);				
				oos.writeObject (command);
			} finally {
				if (client != null)
					client.close ();
			}
		}
		
		if (command.status.equals(SynchronizeStatus.SendFromTarget) && command.getJoiningServer().getISA().equals(getIP())) { 
			System.out.println(synchronize(command));
		}
				
		if (command.status.equals(SynchronizeStatus.SendFromJoining) && !command.getTargetServer().getISA().equals(getIP())) {
			// Send sync message from joining to
			Socket client = new Socket ();
			try {
				System.out.println("server receive:" +  command.cpoints);
				client.connect (command.getJoiningServer().getISA());
				OutputStream os = client.getOutputStream ();
				ObjectOutputStream oos = new ObjectOutputStream (os);				
				oos.writeObject (command);
			} finally {
				if (client != null)
					client.close ();
			}

		}
	} 

	public IPhonebook getPhoneBook() {
		return this.phonebook;
	}
}