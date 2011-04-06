package pellekrogholt.labexercise9;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


/**
 * 
 * Simple Tcp Client
 *
 */
public class TcpClient implements IClient {

	private Socket socket;
	private int server_port;
	private InetAddress server_address;
	
	/**
	 * Simple Tcp Client constructor
	 * 
	 * @param port
	 * @param server_address
	 * @throws IOException
	 */
	public TcpClient(int server_port, InetAddress server_address) throws IOException {
		this.server_port = server_port;
		this.server_address = server_address;		

		
//		TODO: figure this out whats the right approach
//		// create a new socket - apparently don't move to constructor
//		this.socket = new Socket( server_address, server_port );
		
	
	}
  
	public void sendMessage(Object o) throws IOException, ClassNotFoundException {

		
//		TODO: figure this out whats the right approach
		// create a new socket - apparently don't move to constructor
		this.socket = new Socket( server_address, server_port );
		
		OutputStream os = socket.getOutputStream(); // could have gotten an InputStream as well used in receive() 
		ObjectOutputStream oos = new ObjectOutputStream( os );
		oos.writeObject(o);
		
	}

		
	/**
	 * 
	 */
	public Object receiveMessage() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		Object o = ois.readObject();
		return o;
	}
}