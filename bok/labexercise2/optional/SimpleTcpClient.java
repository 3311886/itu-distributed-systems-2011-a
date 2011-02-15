package bok.labexercise2.optional;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class SimpleTcpClient {
	Socket socket;
	String serverAddr = "localhost";
	int port;


	public SimpleTcpClient(String serverAddr, int port) throws Exception{
		this.serverAddr = serverAddr;
		this.port = port;

	}

	public void send(Object o, Object p) throws Exception {
		InetAddress serverAddress = InetAddress.getByName(serverAddr); //InetAddress.getByName("localhost");
		this.socket = new Socket( serverAddress, port); //

		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(o);
		oos.writeObject(p);
		oos.flush();
		 
		readMessage();
	}

	public void readMessage() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Object o = ois.readObject();
			System.out.println(o.toString()); 
		} catch (Exception e){

		}		
	}
	
	public ArrayList<Object> getObjects() {
		
			return SimpleTcpServer.getList();		
	}
	
	public int getSize() {
		
		return SimpleTcpServer.getList().size();		
}
}