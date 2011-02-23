package bok.labexercise4;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class RemotePhonebookServer {

	private InetSocketAddress Server;
	public RemotePhonebookServer(InetSocketAddress server) {
		Server = server;
	}
	
	void SendAndReceive (ServerCommand command) throws IOException {
		ServerSocket listener = new ServerSocket (0);
		Socket client = new Socket ();
		try {
			client.connect (Server);
			OutputStream os = client.getOutputStream ();
			ObjectOutputStream oos = new ObjectOutputStream (os);
			oos.writeObject (command);
		} finally {
			if (client != null)
				client.close ();
		}
	}

	public void addConnectionPoint(InetSocketAddress server) {
		// The server that wants to join
		ConnectionPoint cp1 = new ConnectionPoint(server);
		// The server to join
		ConnectionPoint cp2 = new ConnectionPoint(Server);
		addConnectionPoint(new JoinServerCommand(cp1, cp2));
	}

	public void addConnectionPoint(ServerCommand command) {	
		try {
			SendAndReceive(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeConnectionPoints(InetSocketAddress server) { 
		// The server that wants to join
		ConnectionPoint cp1 = new ConnectionPoint(server);
		// The server to join
		ConnectionPoint cp2 = new ConnectionPoint(Server);
		addConnectionPoint(new RemoveServerCommand(cp1, cp2));
	}

	public void removeConnectionPoints(ServerCommand command) {
		// TODO Auto-generated method stub
		
		try {
			SendAndReceive(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
