package distributedsystems.labexercise3;

import java.io.IOException;
import java.net.InetAddress;  

import org.junit.*;  

public class TcpServerCientTest implements Runnable 
{  
	
	private static int server_port = 4000;
	private static int client_port = 4000;
	private InetAddress server_address;
	
	/**
	 * Setup server
	 * - has to be in a separate thread since server makes a blocking call 
	 * 
	 * @throws Throwable
	 */
	@Before 
	public void setupServer() throws Throwable {
		server_address = InetAddress.getByName("localhost");
		new Thread(new TcpServerCientTest()).start();
	}

	@Override
	public void run() {
		try {
			TcpServer server = new TcpServer(server_port);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@Test 
	public void testClient2ServerMessageUppercase() throws Throwable { 
		
		TcpClient client = new TcpClient (client_port, server_address);
		String message = "Hello world";
		Assert.assertEquals(client.send(message, 1), message.toUpperCase());
		
	}

}
