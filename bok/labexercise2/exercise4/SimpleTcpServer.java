package bok.labexercise2.exercise4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleTcpServer{

	int port; 

	public SimpleTcpServer(int port) throws Exception {


		this.port = port;
		ServerSocket server = new ServerSocket(port);

		while (true) {
			Socket socket = server.accept();	
			Thread t = new Thread(new Connection(socket));
			t.start();

		}
	}

} 

//class Connection extends Runnable {
class Connection implements Runnable {

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;

	public Connection(Socket socket) throws Exception{
		System.out.println("Waiting for client message...");
		this.socket = socket;
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());

	}
	public void run() {
		// TODO Auto-generated method stub
		Object o = null;
		try {
			String method = ois.readObject().toString();
			o = ois.readObject();
			System.out.println(o.toString()) ;
			sendMessage(method, o);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
		String message = "";
		if (i == 0)
			message = o2.toString().toLowerCase();
		if (i == 1)
			message = o2.toString().toUpperCase();
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	} 
}