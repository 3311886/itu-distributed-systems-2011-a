package bok.labexercise4;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/***
 * An attempt to make a remoteserver interface similar to the userinterface
 * Not really working   
 * @author Andreas
 *
 */
public class RemotePhonebookServer {

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

	public ServerResult removeConnectionPoints(InetSocketAddress joiningserver) { 
		// The server that wants to join
		return removeConnectionPoints(new RemoveServerCommand(joiningserver, Server));
	}

	public ServerResult removeConnectionPoints(ServerCommand command) {
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
}