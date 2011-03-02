package bok.labexercise4.extended;
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

import bok.labexercise4.extended.commands.AddItemCommand;
import bok.labexercise4.extended.commands.GetAllCommand;
import bok.labexercise4.extended.commands.GetItemCommand;
import bok.labexercise4.extended.commands.ICommand;
import bok.labexercise4.extended.commands.RemoveItemCommand;
import bok.labexercise4.extended.commands.UpdateItemCommand;

class RemoteServerInterface implements IDataCollection<IItem> {
	private InetSocketAddress Server;

	public RemoteServerInterface (InetSocketAddress server) throws UnknownHostException {
		Server = server;
	}
	
	Object SendAndReceive (ICommand command) throws IOException {
		ServerSocket listener = new ServerSocket (0);
		command.setReturnTo((InetSocketAddress) listener.getLocalSocketAddress ());
		
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

	
	public  Object AddItem(IItem item) throws IOException {
		return SendAndReceive(new AddItemCommand(item));
		
	}

	public boolean Update(Object key, IItem item) throws IOException {
		return (Boolean) SendAndReceive(new UpdateItemCommand(key, item));
		
	}

	public Object Get(Object key) throws IOException {
		return SendAndReceive(new GetItemCommand(key)); 
	}

	public List<IItem> GetAll() throws IOException {
		return (List<IItem>) SendAndReceive(new GetAllCommand());
	}

	public boolean Remove(Object key) throws IOException {
		
		return (Boolean) SendAndReceive(new RemoveItemCommand(key));
	}

	public void Synchronize(List<?> list) {	
		throw new UnsupportedOperationException();
	}
	}