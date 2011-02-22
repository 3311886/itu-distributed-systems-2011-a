package distributedsystems.labexercise3;

import java.io.IOException;
import java.net.InetAddress;  

import org.junit.*;  

public class TcpServerClientTest implements Runnable 
{  
	
	private static int server_port = 4000;
	private static int client_port = 4000;
	private static InetAddress server_address;
	
	/**
	 * Setup server
	 * - has to be in a separate thread since server makes a blocking call 
	 * 
	 * Important: don't use @Before here since that will call method before each method.
	 * 
	 * @throws Throwable
	 */
	@BeforeClass 
	public static void setupServer() throws Throwable {
		server_address = InetAddress.getByName("localhost");
		new Thread(new TcpServerClientTest()).start();
	}

	public static void createPeronObjectsAndSend(TcpClient client, int arg) throws Throwable {
		Person persons[] = {
			new Person("Andreas", "Storegade 2", 1020, "60565656"),
			new Person("Bettina", "Prinsegade 2", 2030, "60565656"),
			new Person("Thor", "Allergade 4", 4030, "60565656")
		};
		for (Person p : persons) {
			client.send(p, 3);
		}
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
	
	
	@Test 
	public void testRequestPersonObjectFromServer() throws Throwable { 
		
		TcpClient client = new TcpClient (client_port, server_address);
		createPeronObjectsAndSend(client, 3);
		Assert.assertNotNull(client.send(0, 2));
		
	}

	
	@Test 
	public void testClientQuitServer() throws Throwable { 
		
		TcpClient client = new TcpClient (client_port, server_address);
		Assert.assertNull(client.send("quit", -1));
		// code after client.send("quit", -1) is not executed so not best practice done here

	}	
	
	
}
