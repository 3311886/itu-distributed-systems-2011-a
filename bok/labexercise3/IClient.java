package bok.labexercise3;

import java.io.IOException;

public interface IClient {

	/**
	 * Send value to server
	 * 
	 * @param value
	 * @throws IOException 
	 */
	Object send(Object methodid, Object[] args) throws IOException, ClassNotFoundException;	
	
	/**
	 * Receive value
	 * 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	Object receive() throws IOException, ClassNotFoundException;	
}
