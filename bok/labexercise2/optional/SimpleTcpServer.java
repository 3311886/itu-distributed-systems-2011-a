package bok.labexercise2.optional;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SimpleTcpServer{
	private static ArrayList<Object> list;
	

	int port;
	
	public SimpleTcpServer(int port) throws Exception {
		this.port = port;
		list = new ArrayList<Object>();

		ServerSocket server = new ServerSocket(port);	

		while (true) {			
			Socket socket = server.accept();	
			Thread t = new Thread(new Connection(socket));
			t.start();
		}
	}
	
	public static ArrayList<Object> getList() {
		return list;
	}  
	public static void removeFromList(int idx) {
		if (list.get(idx) != null)
			list.remove(idx);
	} 

} 

//class Connection extends Thread {
class Connection implements Runnable {

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private String stringMethod = "lower"; 

	public Connection(Socket socket) throws Exception{
		System.out.println("Waiting for client message...");
		this.socket = socket;
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());

	}
	public void run() {
		// TODO Auto-generated method stub
		Object o1 = null;
		Object o2 = null;
		try {
			o1 = ois.readObject();
			o2 = ois.readObject();
			handleAction(o1, o2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handleAction(Object m, Object o) {
		int i = Integer.parseInt(m.toString()); 
		if (i == 1) {
			SimpleTcpServer.getList().add(o);
			
			try {
				oos.writeObject(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			int j = Integer.parseInt(o.toString());
			Object p = SimpleTcpServer.getList().get(j);
			try {
				oos.writeObject(p);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	} 
}