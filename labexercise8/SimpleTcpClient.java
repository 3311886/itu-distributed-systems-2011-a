package labexercise8;

import java.io.*;
import java.net.*;

public class SimpleTcpClient
{  

	int port;
	Socket socket;
	Cryptation crypt;
	String serveradress = "localhost";

	public SimpleTcpClient(String serveradress, int port, String password) throws Exception
	{
		this.serveradress=serveradress;
		this.port=port;
		crypt = new Cryptation(password);
	}

	public void send(Object firstObject, Object secondObject) throws Exception {
		InetAddress serverAddress = InetAddress.getByName(serveradress); //InetAddress.getByName("localhost");
		this.socket = new Socket(serverAddress, port); 
		ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		// Method 
		objectOutput.writeObject(firstObject);
		// Encrypt and write object
		System.out.println("Jeff is encrypted:");
		objectOutput.writeObject(crypt.encrypt(secondObject));
		System.out.println("... and Jeff is sent. \n");
		objectOutput.flush();
		readMessage();
	}

	public void readMessage() 
	{
		try 
		{
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			//Decrypt the object
			Object o = crypt.decrypt(ois.readObject());
			System.out.println(o.toString()); 
		}
		catch (Exception e){
			System.out.print(e.getMessage());
		}               
	}

	public Object receive() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}
