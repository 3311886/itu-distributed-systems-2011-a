package distributedsystems.labexercise3;

import java.io.IOException;

public interface IClient<V, C> {

	/**
	 * Send value to server
	 * 
	 * @param value
	 * @throws IOException 
	 */
	Object send(V value) throws IOException, ClassNotFoundException;
	
	
	/**
	 * Send value to server with a command
	 * 
	 * @param value
	 * @param operator
	 * @throws IOException 
	 */
	Object send(V value, int operator) throws IOException, ClassNotFoundException;	
	
	
	/**
	 * Receive value
	 * 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	Object receive() throws IOException, ClassNotFoundException;
	
}
