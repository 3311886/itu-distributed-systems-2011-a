package distributedsystems.labexercise2;


import java.net.*;  
import java.io.*;
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
			Object o = null;
			
			try {
				
				// first object in current stream
				o = ois.readObject();
			
				// quick fix 
				// TODO: we tried to readObject() depending how many was send 
				// - lets say send('test', 1) was only send('test') but the fix din't really worked out
				// so for now we always have 2 arguments in send('test', 1)  
				boolean flag = false;
				String operator = null;
				try {
					// second object in current stream - this part breaks if no second object is sent ... hmmm
					operator = ois.readObject().toString();
					flag = true;
				} catch (IOException e) {
					
				}
				
//				if (operator.toString().isEmpty()) {
				if (!flag) {
					send(o);
				} else {
					
					send(o, Integer.parseInt(operator));
				}


				System.out.println("server received:" + o);
				if (o.toString().equals("quit")) 
				{
					
					System.out.println("T/RE�E&/R/");
					System.exit(-1);
				}			
				
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
			} catch (Exception e) {	
			}
		}

		private void send(Object o, int i) {
			
			
			// assignment part 3/4
			if (o instanceof String) {
				
				String message = "";
				if (i == 0)
					message = o.toString().toLowerCase();
				if (i == 1)
					message = o.toString().toUpperCase();
				try {
					oos.writeObject(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (o instanceof Person) {
				
				// assignment part optional 1

				// add person to storage
				if (i == 1)
					storage.add(o);
				
			} else {
				// assignment part optional 1
				
				// request person from storage
				if (i == 2) {
					try {
						oos.writeObject(storage.get(Integer.parseInt(o.toString())));

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
			
		}

		private void elif(boolean b) {
			// TODO Auto-generated method stub
			
		}
		
	} // end Connection

} // end MyTcpServer