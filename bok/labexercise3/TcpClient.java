package bok.labexercise3;

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
 
	// - we had some problems with the generics perhaps just switch to Object 

	/**
	 * Sends message to server. Wraps argument in an array 
	 * methodid, objectarg
	 * TODO: Reverse order so it is methodid, objectarg  
	 */ 
	public Object send(Object o, Object methodid) throws IOException, ClassNotFoundException {
		Object[] args = new Object[] {o};
		return send(methodid, args);
	}
	
	/**
	 * Send message
	 * Takes a methodid and an array of argument 
	 */
	public Object send(Object methodid, Object[] args) throws IOException, ClassNotFoundException {
		
		// create a new socket
		this.socket = new Socket( server_address, port );
		
		OutputStream os = socket.getOutputStream(); // could have gotten an InputStream as well used in receive() 
		ObjectOutputStream oos = new ObjectOutputStream( os );
		
		// Write the number of elements in the argumentarray. 
		// The +1 is not meant to be.  
		oos.writeObject(args.length + 1);
		oos.writeObject(methodid);
		for (Object o : args) {
			oos.writeObject(o);
		} 
		oos.flush();
		return receive();
		// don't place receive() / readMessage() here 
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