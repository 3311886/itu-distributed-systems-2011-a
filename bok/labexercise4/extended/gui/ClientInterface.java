package bok.labexercise4.extended.gui;
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
import java.util.HashMap;
import java.util.List;

import bok.labexercise4.extended.IDataCollection;
import bok.labexercise4.extended.IItem;
import bok.labexercise4.extended.commands.AddItemCommand;
import bok.labexercise4.extended.commands.GetAllCommand;
import bok.labexercise4.extended.commands.GetItemCommand;
import bok.labexercise4.extended.commands.ICommand;
import bok.labexercise4.extended.commands.RemoveItemCommand;
import bok.labexercise4.extended.commands.UpdateItemCommand;

public class ClientInterface implements IDataCollection<IItem> {
	private InetSocketAddress Server;

	public ClientInterface (InetSocketAddress server) throws UnknownHostException {
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

	public  Object AddItem(Class<?> c, HashMap<String, Object> values) throws IOException {
		
		AddItemCommand command =new AddItemCommand(c, values);
		command.setReceiver(Server);
		
		
		return SendAndReceive(command );
	}

	public boolean Update(Object key, HashMap<String, Object> values) throws IOException {
		
		SendAndReceive(new UpdateItemCommand(key, values));
		return true;
	}

	public IItem<?> Get(Object key) throws IOException {
		return (IItem<?>) SendAndReceive(new GetItemCommand(key)); 
	}

	public Object[] GetAll() throws IOException {
		return (Object[]) SendAndReceive(new GetAllCommand());
	}

	public boolean Remove(Object key) throws IOException {
		
		return (Boolean) SendAndReceive(new RemoveItemCommand(key));
	}

	public void Synchronize(List<?> list) {	
		throw new UnsupportedOperationException();
	}

	public boolean Update(IItem itemOld, IItem itemNew) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public List<IItem> GetAllTyped() throws IOException {
		throw new UnsupportedOperationException();
	}

	public Object AddItem(IItem item) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	}