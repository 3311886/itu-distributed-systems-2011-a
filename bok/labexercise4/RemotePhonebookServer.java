package bok.labexercise4;

import java.io.IOException;
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
//		client = listener.accept ();
//		try {
//			listener.close ();
//			InputStream is = client.getInputStream ();
//			ObjectInputStream ois = new ObjectInputStream (is);
//
//			return ois.readObject ();
//		} catch (ClassNotFoundException e) {
//			System.err.println (e.getMessage ());
//			System.exit (-1);
//			return null;
//		} finally {
//			if (client != null)
//				client.close ();
//		}
		return null;
		
	}

	public ServerResult addConnectionPoint(InetSocketAddress joiningserver) {
		// The server that wants to join
		Object result = addConnectionPoint(new JoinServerCommand(joiningserver, Server));
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
		// TODO Auto-generated method stub
		Object result = null;
		try {
			result = SendAndReceive(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (ServerResult) result;
	}

	Set<InetSocketAddress> getConnectionPoints() throws IOException {
		ServerSocket listener = new ServerSocket(0);
		InetSocketAddress isa =  (InetSocketAddress) listener.getLocalSocketAddress ();
		return getConnectionPoints(new GetConnectionPointsCommand( isa, Server)); 
	}
	public Set<InetSocketAddress> getConnectionPoints(
			GetConnectionPointsCommand command) throws IOException {
		return (Set<InetSocketAddress>) SendAndReceive(command);
	} 
}