package pellekrogholt.labexercise9;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;




//TODO: had hopped for an approach with  
//public class AuthenticationTcpServer extends TcpServer
//but had problems when called the constructor that actually 
//called up the constructor of TcpServer  


public class AuthenticationTcpServer implements IServer {

	public AuthenticationTcpServer(int port) throws IOException {
//		super(port); // why is the needed as first line in the constructor 
		
		
		System.out.println("authentication server constructor called added to port: " + port);
		
		ServerSocket server_socket = new ServerSocket( port );

		// this part handles multiple connections / users concurrently
		while(true) {
			Socket client_socket = server_socket.accept(); // blocking call code bellow not executed before request from client
			Connection c = new Connection(client_socket);
			new Thread(c).start(); 
		}  

	}
	/**
	 * Connection(s)
	 * 
	 * It implements threads with use of the Runnable and not the Thread
	 * 
	 */
	protected class Connection implements Runnable {

		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		
		Connection (Socket socket) throws IOException {

			oos = new ObjectOutputStream( socket.getOutputStream());
			ois = new ObjectInputStream( socket.getInputStream());
		}

		public void run() {

			try {	

				/*
				 * Note/ TODO:
				 * 
				 * 
				 mads suggested the while(keep_running) approach 
				 so it keeps running listening for communication on one socket
				 when trying to move socket creation away from send on the client.

				 */


				//				Boolean keep_running = true;
				//				while(keep_running) {


//				System.out.println("keep_running results in multiple calls on each message");
				Object o = ois.readObject(); // blocking call

				if (o.toString().equalsIgnoreCase("quit")) 
				{
					destroy();
					//					keep_running = false;
				}

				send(o);

				//				}				

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} // end run()

		private void send(Object o) {
			try {				
				oos.writeObject(o.toString() + "_message2");
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