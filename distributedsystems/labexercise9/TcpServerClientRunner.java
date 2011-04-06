package pellekrogholt.labexercise9;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Assert;

public class TcpServerClientRunner implements Runnable {
	
	private boolean server_authentication = false;
	private int server_port;
	private int client_port;
	
	public TcpServerClientRunner(int server_port) {
		this.server_port = server_port;
	}
	
	public TcpServerClientRunner(int server_port, boolean server_authentication) {
		this.server_port = server_port;
		this.server_authentication = server_authentication;
	}
	
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main (String args[]) throws Exception{ 

		
		InetAddress server_address = InetAddress.getByName("localhost");
		
		//start authentication server in a separate thread
		
		System.out.println("start authentication server (S) in separate thread");
		
		new Thread(new TcpServerClientRunner(5001, true)).start();
		
		System.out.println("start server (B) in separate thread");
		
		new Thread(new TcpServerClientRunner(5002)).start();		
		
		System.out.println("A send message to S");
		
		TcpClient client = new TcpClient (5001, server_address);
		String message = "message_1";
		client.sendMessage(message);

		message = client.receiveMessage().toString();
		System.out.println("A receives message from S: " + message);		

		
		System.out.println("A send message to B");
		
		TcpClient client2 = new TcpClient (5002, server_address);
		client2.sendMessage(message + "_message3");
		
		message = client2.receiveMessage().toString();
		System.out.println("A receives message from B: " + message);
		

		System.out.println("Final message from A to B");
		client2.sendMessage(message + "_message5");		
		
		
		System.out.println("---- close down server(s) ---------");
		client.sendMessage("quit");
		client2.sendMessage("quit");
		
		
	}

	/**
	 * approach is to create one authentication server then a *normal* server
	 */
	@Override
	public void run() {
		try {			
			if (server_authentication) {
				// nb! next line results in a blocking call
				AuthenticationTcpServer authentication_server = new AuthenticationTcpServer(server_port);
			} else {
				// nb! next line results in a blocking call
				TcpServer server = new TcpServer(server_port);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}    
}