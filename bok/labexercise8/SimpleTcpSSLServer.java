package bok.labexercise8;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleTcpSSLServer{
	int port;
	private String password;
	
	public SimpleTcpSSLServer(int port, String password) throws Exception {
		
		this.password = password;
		// 
		ServerSocket server = new ServerSocket(port);
		
		while (true) {
			Socket socket = server.accept();	
			Thread t = new Thread(new Connection(socket, this.password));
			t.start();
		}
	}

} 

//class Connection extends Runnable {
class Connection implements Runnable {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private SymmetricCrypt crypt;

	public Connection(Socket socket, String password) throws Exception{
		System.out.println("Waiting for client message...");
		this.socket = socket;
		crypt = new SymmetricCrypt(password);
		
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
	public void run() {
		Object o = null;
		try {
			// read method
			String method = ois.readObject().toString();
			// Decrypt 
			o = crypt.decrypt(ois.readObject());
			System.out.println(o.toString()) ;
			sendMessage(method, o);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage() {
		try {
			oos.writeObject("Message received");
		} catch (Exception e) {	
		}
	}

	public void sendMessage(Object o1, Object o2) {
		int i = Integer.parseInt(o1.toString());
		Object message = "";
		if (i == 0)
			message = o2.toString().toLowerCase();
		if (i == 1)
			message = o2.toString().toUpperCase();
		if (i == 2)
		try {
			// Encrypt the message 
			oos.writeObject(crypt.encrypt(message));
		} catch (IOException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}	
	} 	
}