package distributedsystems.labexercise3;

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
public class TcpClient<V, C> implements IClient<V, C> {

	private Socket socket;
	private int port;
	private InetAddress server_address;
	
	/**
	 * Simple Tcp Client constructor
	 * 
	 * @param port
	 * @param server_address
	 * @throws IOException
	 */
	public TcpClient(int port, InetAddress server_address) throws IOException {
		this.port = port;
		this.server_address = server_address;		

		// note/ observation: 
		// create a new socket - not sure if this should be here in the constructor 
		// or in the send method - so fare do it in the send method - if done here 
		// only one send will go through per client.
		// this.socket = new Socket( server_address, port );
	}

	
	// TODO: 4 different send() is not optimal 
	// - we had some problems with the generics perhaps just switch to Object 
	
	
	/**
	 * Send message with command
	 */
	public Object send(V message, int operator) throws IOException, ClassNotFoundException {
		
		this.socket = new Socket( server_address, port );
		
		OutputStream os = socket.getOutputStream(); // could have gotten an InputStream as well used in receive()

		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(message);
		oos.writeObject(operator);
		oos.flush();
		
		return receive();
	}
	

	/**
	 * Send message
	 */
	public Object send(V message) throws IOException, ClassNotFoundException {
		
		// create a new socket
		this.socket = new Socket( server_address, port );
		
		OutputStream os = socket.getOutputStream(); // could have gotten an InputStream as well used in receive() 

		ObjectOutputStream oos = new ObjectOutputStream( os );
		oos.writeObject(message);
		oos.flush();
		
		return receive();
		// don't place receive() / readMessage() here 
	}

	/**
	 * Send a Person object with command
	 * 	
	 * @param p
	 * @param operator
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object send(Person p, int operator) throws IOException, ClassNotFoundException {
		
		this.socket = new Socket( server_address, port );
		
		OutputStream os = socket.getOutputStream(); // could have gotten an InputStream as well used in receive()

		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(p);
		oos.writeObject(operator);
		oos.flush();
		
		if (operator == 1)
			return null;

		return receive();
		// don't plase receive() / readMessage() here
	}
	
	
	/**
	 * Send a key and get Person object
	 * 
	 * @param key
	 * @param operator
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object send(int key, int operator) throws IOException, ClassNotFoundException {
		
		this.socket = new Socket( server_address, port );
		
		OutputStream os = socket.getOutputStream(); // could have gotten an InputStream as well used in receive()

		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(key);
		oos.writeObject(operator);
		oos.flush();
		
		
		return receive();
		// don't plase receive() / readMessage() here
	}
	
	/**
	 * 
	 */
	public Object receive() throws IOException, ClassNotFoundException {

		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		Object o = ois.readObject();
		return o;
	}

}