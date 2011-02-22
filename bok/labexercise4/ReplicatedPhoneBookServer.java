package bok.labexercise4;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ReplicatedPhoneBookServer extends PhonebookServer { 
	private Set<ConnectionPoint> cPoints;
	private IPhonebook phonebook ;

	public ReplicatedPhoneBookServer() throws IOException {
		super();
		cPoints = new  HashSet<ConnectionPoint>();
		phonebook = new ReplicatedPhonebook(getIP());
	} 
	

	public ServerResult addConnectionPoint(ServerCommand command) { 
		if (!this.LocalEndpoints.getFirst().equals(command.getTargetServer().getISA())) {
			return cPoints.add(command.getJoiningServer()) ? ServerResult.Added : ServerResult.AlreadyAdded;
		}  else {
			cPoints.add(command.getJoiningServer());
			try {
				return broadcast(command);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return ServerResult.UnknownError;
			}
		}
	}

	public ServerResult removeConnectionPoints(ServerCommand command) {
		if(this.LocalEndpoints.getFirst().equals(command.getTargetServer())) {
			cPoints.remove(command.getJoiningServer());
			return ServerResult.Removed;
		} else {
			cPoints.remove(command.getJoiningServer());
			try {
				broadcast(command);
				return ServerResult.RemovedAndBroadCast;
			} catch (IOException e){
				return ServerResult.Removed;
			}	
		} 
	} 

	public ServerResult broadcast(ServerCommand command) throws IOException 
	{
		Socket client = new Socket();
		for (InetSocketAddress isa : this.LocalEndpoints) {
			client.connect(isa);
			OutputStream os = client.getOutputStream ();
			ObjectOutputStream oos = new ObjectOutputStream (os);
			oos.writeObject (command);
		}
		if (client != null) client.close ();
		return ServerResult.AddedAndBroadCast;
	} 

	void ExecuteAndSend (ServerCommand command) throws IOException {
		Object result = command.Execute(this);
		Socket client = new Socket();
		try {
			client.connect(command.getJoiningServer().getISA());
			OutputStream os = client.getOutputStream ();
			ObjectOutputStream oos = new ObjectOutputStream (os);
			oos.writeObject (result);
		} finally {
			if (client != null)
				client.close ();
		}
	}
		
	public Set<ConnectionPoint> getConnectionPoints() {
		return cPoints;
	}
	
	@Override
	  void ExecuteAndSend(Object command) throws IOException {
		  	// The command is sent from a client
			if (command instanceof Command) ExecuteAndSend((Command) command);
			// The command is sent from another server
			else if (command instanceof Command) ExecuteAndSend((ServerCommand)command);
			// The command is a reply from another server
			else if (command instanceof ServerResult) System.out.println(command.toString());
	  }
}