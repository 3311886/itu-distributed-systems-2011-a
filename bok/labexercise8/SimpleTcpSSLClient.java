package bok.labexercise8;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SimpleTcpSSLClient {
	Socket socket;
	String serverAddr = "localhost";
	SymmetricCrypt crypt ;
	int port;

	public SimpleTcpSSLClient(String serverAddr, int port, String password) throws Exception{
		crypt = new SymmetricCrypt(password);
		this.serverAddr = serverAddr;
		this.port = port;

	}

	public void send(Object o, Object p) throws Exception {
		InetAddress serverAddress = InetAddress.getByName(serverAddr); //InetAddress.getByName("localhost");
		this.socket = new Socket( serverAddress, port); //
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		// Method 
		oos.writeObject(o);
		// Encrypt and write object
		oos.writeObject(crypt.encrypt(p));
		oos.flush();
		readMessage();
	}

	public void readMessage() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			// Decrypt the object
			Object o = crypt.decrypt(ois.readObject());
			System.out.println(o.toString()); 
		} catch (Exception e){

		}		
	}
}