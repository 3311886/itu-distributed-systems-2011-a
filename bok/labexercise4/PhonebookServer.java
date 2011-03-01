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
		if (this.getIP().equals(command.getSender())) {
			// Receives from client a command to join another server
			command.setReturnTo(null);
			Send(command, command.getReceiver());
			// Return to client 
			return ServerResult.JoiningServer;
		} else 	
			if (this.getIP().equals(command.getReceiver())) {
				try {
					// First broadcast then add
					ServerResult result = null;
					// Broadcast join command
					result = broadcast(command);
					//Synchronize from target server to joining server
					SynchronizeCommand syncCommand =  
						new SynchronizeCommand(getIP(),  
								command.getSender());
					syncCommand.status = SynchronizeStatus.Created;
					syncCommand.Execute(this);

					// Add the joining server to target server
					cPoints.add(command.getSender());
					return result;
				} catch (IOException e) {
					e.printStackTrace();
					return ServerResult.UnknownError;
				}		
			} else {
				// A broadcast server has received a joincommand 
				cPoints.add(command.getSender());
				return ServerResult.Added;
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
			if (command.getSender() != cp) Send(command, cp);
		}
		return ServerResult.BroadCast;
	}
	/***
	 * Handles execution of commands of type ServerCommand
	 * RemoveServerCommand, JoinServerCommand
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend (ServerCommand command) throws IOException {
		Trace("Executing SERVERCOMMAND");
		//Execute sets returnto to null --> No ServerResults are returned to sender
		 InetSocketAddress returnTo = command.getReturnTo();
		
		// May broadcast sync messages
		Object result = command.Execute(this);
		
		Send(result, returnTo);
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
		if (command instanceof ICommand) {
			ExecuteAndSend((ICommand) command);
		}
		else if (command instanceof ServerResult) {
			// The command is a reply from another server
			ExecuteAndSend((ServerResult) command);
		}
	}

	void ExecuteAndSend(ICommand command) throws IOException {
		// For tracing 
		StringBuilder tracer = new StringBuilder();
		String sender = command.getReturnTo() != null ? 
				command.getReturnTo().toString() : command.getSender().toString(); 
				Trace(String.format("%s: ", getIP()));
				if (command instanceof ReplicateCommand) {
					Trace(String.format("Receiving from: %s ", sender));
					Trace(tracer.toString());
					ExecuteAndSend((ReplicateCommand) command);
				} else
					// The command is sent from a client
					if (command instanceof Command) {
						Trace(String.format("Receiving from: %s", sender));
						ExecuteAndSend((Command) command);

					} else if (command instanceof SynchronizeCommand ) {
						Trace(String.format("Receiving from: %s", sender));
						
						ExecuteAndSend((SynchronizeCommand) command);
					}
				// The command is sent from another server
					else if (command instanceof ServerCommand) {
						Trace(String.format("Receiving from: %s", sender));
						Trace(tracer.toString());
						ExecuteAndSend((ServerCommand)command);
					}
	}

	/***
	 * Handles ServerResult responses 
	 * @param result
	 */
	void ExecuteAndSend(ServerResult result) {
		try {
			Trace("Executing SERVERRESULT: " + result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		switch (result) {
		case RemoveServerInitiatedFromServer : 
			cPoints.removeAll(cPoints);
		}
	}

	/***
	 * Handles client commands that may be broadcast
	 * UpdateCommand, RemoveCommand and AddContact 
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend(ReplicateCommand command) throws IOException {
		Trace("Executing REPLICATECOMMAND:");
		Object result = null;
		if (command.getReceiver() != null) {
			// adds contact to phonebook
			result = command.Execute (phonebook);
			Send(result, command.getReturnTo());
			if (command.getReceiver().equals(getIP())) 
			{
				// Broadcast but clear returnto address
				command.setReturnTo(null);
				command.setSender(getIP());
				broadcast(command);
			}
		} else {
			command.setReceiver(getIP());
			// Calls the method again
			ExecuteAndSend(command);
		}
	}

	/**
	 * Default handler for commands sent to the server
	 * GetAllContactsCommand & Lookup
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend (Command command) throws IOException {
		Object result = command.Execute (phonebook);
		Send(result, command.getReturnTo());
	}

	/***
	 * Handles synchronization commands 
	 * @param command
	 * @throws IOException
	 */
	void ExecuteAndSend(SynchronizeCommand command) throws IOException {
		Object result = command.Execute(this);
		Send(result, null);
	} 

	public void Send(Object o, InetSocketAddress receiver) throws IOException {
		if (receiver != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Sending from %s: \n", getIP()));
			sb.append(String.format("Command: %s \n", o.getClass()));
			sb.append(String.format("Sending to: %s \n", receiver));
			Trace(sb.toString());
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
	} 
	
	/***
	 * Returns the server's phonebook 
	 * @return
	 */
	public IPhonebook getPhoneBook() {
		return this.phonebook;
	}
	
	public Set<InetSocketAddress> getConnectionPoints(
			GetConnectionPointsCommand command) {
		return cPoints;
	} 
}