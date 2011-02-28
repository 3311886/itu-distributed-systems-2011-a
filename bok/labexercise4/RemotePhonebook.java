package bok.labexercise4;
// RemotePhonebook forward all methods to a server wrapped as Commands

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

class RemotePhonebook implements IPhonebook {
	private InetSocketAddress Server;

	public RemotePhonebook (InetSocketAddress server) throws UnknownHostException {
		Server = server;
	}

	Object SendAndReceive (Command command) throws IOException {
		ServerSocket listener = new ServerSocket (0);
		command.setSender((InetSocketAddress) listener.getLocalSocketAddress ());
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

	public AddResult AddContact (Contact contact) throws IOException {
		// Set the server
		contact.setConnectionPoint(Server);
		Command command = new AddCommand (contact);
		
		Object result = SendAndReceive (command);
		return (AddResult) result;
	}

	public UpdateResult Update (String name, String newPhoneNo)
	throws IOException {
		Command command = new UpdateCommand (name, newPhoneNo);
		Object result = SendAndReceive (command);
		return (UpdateResult) result;
	}

	public String Lookup (String name) throws IOException {
		Command command = new LookupCommand (name);
		Object result = SendAndReceive (command);
		return (String) result;
	}

	public List<Contact> GetAllContacts () throws IOException {
		Command command = new GetAllContactsCommand ();
		Object result = SendAndReceive (command);
		return (List<Contact>) result;
	}

	public boolean Remove (String name) throws IOException {
		Command command = new RemoveCommand (name);
		Object result = SendAndReceive (command);
		return ((Boolean) result).booleanValue ();
	}

	public void Synchronize(List<Contact> contacts) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}