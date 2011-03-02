package bok.labexercise4.extended;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

import bok.labexercise4.ServerResult;
import bok.labexercise4.extended.commands.GetConnectionPointsCommand;
import bok.labexercise4.extended.commands.ICommand;
import bok.labexercise4.extended.commands.JoinServerCommand;
import bok.labexercise4.extended.commands.RemoveServerCommand;

/***
 * An attempt to make a remoteserver interface similar to the userinterface
 * Not really working   
 * @author Andreas
 *
 */
public class RemoteServerUI{

	private InetSocketAddress Server;
	public RemoteServerUI(InetSocketAddress server) {
		Server = server;
	}

	Object SendAndReceive (ICommand<?> command) throws IOException {
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


	public ServerResult addConnectionPoint(ICommand<JoinServerCommand> command) {
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
		RemoveServerCommand command = new RemoveServerCommand();
		command.setSender(joiningserver);
		command.setReceiver(Server);
		return removeConnectionPoint(command);
	}

	public ServerResult removeConnectionPoint(ICommand<?> command) {
		Object result = null;
		try {
			result = SendAndReceive(command);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return (ServerResult) result;
	}

	public Set<InetSocketAddress> getConnectionPoints() throws IOException {
		GetConnectionPointsCommand command = new GetConnectionPointsCommand();
		command.setSender(null);
		command.setReceiver(Server);
		return getConnectionPoints(command); 
	}
	@SuppressWarnings("unchecked")
	public Set<InetSocketAddress> getConnectionPoints(
			GetConnectionPointsCommand command) throws IOException {
		return (Set<InetSocketAddress>) SendAndReceive(command);
	}

	public boolean Ping(InetSocketAddress isa) {
		try {
			return isa.getAddress().isReachable(5000);
			
		} catch (IOException e) {
			return false;
		}
	}

	public boolean ConnectToServer(InetSocketAddress isa) throws IOException {
		if (Ping(isa)) { 
			Server = isa;
			return true;
		} 
		else 
			return false;
	}
}