package labexercise8;

import java.io.*;
import java.net.*;

public class SimpleTcpServer
{
	int port;
	public String password;

	public SimpleTcpServer(int port, String password) throws Exception
	{
		this.password=password;

		ServerSocket server = new ServerSocket(port);

		while(true)
		{
			// When a new connection is made it is automatically starting 
			// a new thread because Connection implements Runnable.
			Socket socket= server.accept();
			System.out.println("Server receives message");
			Thread thread = new Thread(new Connection(socket, this.password));
			thread.start();
		}
	}
}

class Connection implements Runnable
{
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;
	private Cryptation crypt;

	public Connection(Socket socket, String password) throws Exception{
		System.out.println("Waiting for client message... \n");
		crypt = new Cryptation(password);
		objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectInput = new ObjectInputStream(socket.getInputStream());
	}

	public void run() {
		Object object = null;
		try {
			// read method runs while waiting for the object input from client.
			String method = objectInput.readObject().toString();
			System.out.println("gudaw");
			// Decrypt 
			object = crypt.decrypt(objectInput.readObject());
			System.out.println("Run object:" + object.toString()) ;
			receivedMessage(method, object);
		} catch (IOException exception) {
			exception.printStackTrace();
		} catch (ClassNotFoundException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}

	public void sendMessage() {
		try {
			objectOutput.writeObject("Message received");
		} catch (Exception e) { 
		}
	}

	public void receivedMessage(Object firstObject, Object secondObject) {
		int i = Integer.parseInt(firstObject.toString());
		Object message = "";
		if (i == 0)
			message = secondObject.toString().toLowerCase();
		if (i == 1)
			message = secondObject.toString().toUpperCase();
		if (i == 2)
			message = secondObject;

		try {
			// Encrypt the message 
			objectOutput.writeObject(crypt.encrypt(message));
		} catch (IOException exception) {

			exception.printStackTrace();
		} catch (Exception exception) {

			exception.printStackTrace();
		}       
	}       
}

