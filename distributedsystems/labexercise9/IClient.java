package pellekrogholt.labexercise9;

import java.io.IOException;

public interface IClient {

	/**
	 * Send value to server
	 * 
	 * @param value
	 * @return 
	 * @throws IOException 
	 */
	void sendMessage(Object message) throws IOException, ClassNotFoundException;	
	
	/**
	 * Receive value
	 * 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	Object receiveMessage() throws IOException, ClassNotFoundException;	
}
