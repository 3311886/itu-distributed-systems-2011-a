package bok.labexercise3;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class TcpServer implements IServer {

	private int port;
	private Vector<Object> storage = new Vector<Object>();

	/* constructor */
	public TcpServer(int port) throws IOException {
		this.port = port; 
		ServerSocket server_socket = new ServerSocket( port );

		// this part handles multiple connections / users
		while(true) {
			Socket client_socket = server_socket.accept(); // blocking call code bellow not executed before request from client
			Connection c = new Connection(client_socket);
			new Thread(c).start(); 
		}  
	}

	//class Connection extends Thread {
	/**
	 * Connection(s)
	 * 
	 * Class that can handle multiple connections - its based on xxx (add reference)
	 * but its made private and for that reason moved into / under the server class
	 * because it should be accessible by that class.
	 * 
	 * It implements threads with use of the Runnable and not the Thread 
	 * 
	 */
	private class Connection implements Runnable {

		private ObjectInputStream ois; // previously in
		private ObjectOutputStream oos; // previously out
		private Socket socket;


		// TODO: what's best practice to throw exception or try/catch clauses ?
		private Connection (Socket socket) throws IOException {

			this.socket = socket;
			oos = new ObjectOutputStream( socket.getOutputStream());
			ois = new ObjectInputStream( socket.getInputStream());
		}

		public void run() {

			// TODO Auto-generated method stub

			Object[] args; 
			try {	
				// first object in current stream
				// This object tells us how many elements we have to read
				Object o = ois.readObject();
			
				int argCount = Integer.valueOf(o.toString());
				args = new Object[argCount];
				for (int i = 0; i < argCount; i++) {
					args[i] = ois.readObject();
					// Check if the server is requested to halt
					if (args[i].toString().equalsIgnoreCase("quit")) 
						{
						destroy();
						}
				}

				send(ServerHelper.UnmarshallRequest(args));				

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // end run()

		private void send(Object o) {
			try {				
				oos.writeObject(o);
			} 
			catch (Exception e) {	
			}

			// end Connection	

		} // end MyTcpServer
		
		private void destroy() {
			System.out.println("Server is closing down...");
			System.exit(-1);
		}
	}	 
}