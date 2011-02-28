package bok.labexercise4;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

/***
 * An attempt to make a remoteserver interface similar to the userinterface
 * Not really working   
 * @author Andreas
 *
 */
public class RemotePhonebookServer implements IPhonebookServer {

	private InetSocketAddress Server;
	public RemotePhonebookServer(InetSocketAddress server) {
		Server = server;
	}

	Object SendAndReceive (ServerCommand command) throws IOException {
		ServerSocket listener = new ServerSocket(0);
		command.setReturnTo((InetSocketAddress) listener.getLocalSocketAddress ());
		Socket client = new Socket ();
		try {
			client.connect (Server);
			OutputStream os = client.getOutputStream ();

			ObjectOutputStream oos = new ObjectOutputStream (os);
			oos.writeObject(command);

		} finally {
			if (client != null)
				client.close ();
		}
		client = listener.accept ();
		try {
			listener.close ();
			InputStream is = client.getInputStream ();
			ObjectInputStream ois = new ObjectInputStream (is);
			return ois.readObject ();
		} catch (ClassNotFoundException e) {
			System.err.println (e.getMessage ());
			System.exit (-1);
			return null;
		} finally {
			if (client != null)
				client.close ();
		}
		
		
	}
	
/***
 * 
 * @param joiner The joinning server
 * @param joinee The server to join
 * @return
 */
	public ServerResult addConnectionPoint(InetSocketAddress server, boolean asJoiner) {
		
		JoinServerCommand command;  
		if (asJoiner) {
			command = new JoinServerCommand(Server, server);
		}else {
			command = new JoinServerCommand(server, Server);
		}
		Object result = addConnectionPoint(command);
		return (ServerResult) result;
	} 
	

	public ServerResult addConnectionPoint(ServerCommand command) {
		Object result = null;
		try {
			result = SendAndReceive(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (ServerResult) result;
	}

	public ServerResult removeConnectionPoint(InetSocketAddress joiningserver) { 
		// The server that wants to join
		return removeConnectionPoint(new RemoveServerCommand(joiningserver, Server));
	}

	public ServerResult removeConnectionPoint(ServerCommand command) {
		Object result = null;
		try {
			result = SendAndReceive(command);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return (ServerResult) result;
	}

	Set<InetSocketAddress> getConnectionPoints() throws IOException {
		GetConnectionPointsCommand command = new GetConnectionPointsCommand(null, Server); 
		return getConnectionPoints(command); 
	}
	public Set<InetSocketAddress> getConnectionPoints(
			GetConnectionPointsCommand command) throws IOException {
		return (Set<InetSocketAddress>) SendAndReceive(command);
	}
	
	public void ConnectToServer(InetSocketAddress isa) {
		Server = isa;
	}
}