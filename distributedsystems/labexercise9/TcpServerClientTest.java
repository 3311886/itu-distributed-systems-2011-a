package pellekrogholt.labexercise9;

import java.io.IOException; 
import java.net.InetAddress;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class TcpServerClientTest implements Runnable 
{  

	private static int run_call = 0;

	private static int authentication_server_flag = 0;

	private static int client_port = 4000;
	private static int server_port = 5000;

	//	private static int authentication_port = 4004;
	private static InetAddress server_address;

	private static long timeout = 10;


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

		Thread server_authentication_thread = new Thread(new TcpServerClientTest());
		server_authentication_thread.start();

		Thread server_thread = new Thread(new TcpServerClientTest());
		server_thread.start();

	}



	@Test 
	public void test_messages_from_A_to_S_to_A_to_B_to_A_to_B() throws Throwable { 

		// A will communicate with S (Authentication Server)
		TcpClient client = new TcpClient (5001, server_address);
		String message = "message1";

		client.sendMessage(message);

		// from S to A
		message = client.receiveMessage().toString();

		Assert.assertEquals(message, "message1_message2");		

		// from A to B (Another Server)
		client = new TcpClient (5002, server_address);		

		client.sendMessage(message+"_message3");

		// from B to A
		message = client.receiveMessage().toString();

		Assert.assertEquals(message, "message1_message2_message3_message4");

		// final message from A to B   
		client.sendMessage(message+"_message5");

		// quit server 
		client.sendMessage("quit");
	}


	@Test 
	public void testClient2AuthenticationServerMessage() throws Throwable { 

		// from A to S (Authentication Server)
		TcpClient client = new TcpClient (5001, server_address);
		String message = "message1";

		client.sendMessage(message);

		//		System.out.println(client.receiveMessage());

		Assert.assertEquals("message1_message2", client.receiveMessage());

		// quit server 
		client.sendMessage("quit");
	}

	@Test 
	public void testClient2ServerMessage() throws Throwable { 

		// from A to B (Server)
		TcpClient client = new TcpClient (5002, server_address);
		String message = "message3";

		client.sendMessage(message);

		Assert.assertEquals("message3_message4", client.receiveMessage());

		// quit server 
		client.sendMessage("quit");
	}


	@Test 
	public void testClient2Server2Messages() throws Throwable { 

		// from A to B (Server)
		TcpClient client = new TcpClient (5002, server_address);
		String message = "message3";

		client.sendMessage(message);

		Assert.assertEquals("message3_message4", client.receiveMessage());

		client.sendMessage("new_message");

		Assert.assertEquals("new_message_message4", client.receiveMessage());

		// quit server 
		client.sendMessage("quit");
	}


	@Override
	public void run() {
		try {


			/* TODO:
			 * 
			 * Figure out how to ensure authentication_server is created 
			 * before server.
			 * 
			 * Tried out from setupServer() to control the order of the threads with 
			 * various approaches sleep(timeout), wait()/notify()
			 * but didn't worked out in the junit framework.
			 * 
			 * For now it means that the test failes now and then when servers
			 * not create din the right order.
			 */


			// note: approach is to create one authentication server then a *normal* server 
			if (run_call == 1) {
				AuthenticationTcpServer authentication_server = new AuthenticationTcpServer(++server_port);
			} else {
				TcpServer server = new TcpServer(++server_port);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
