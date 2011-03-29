package labexercise8;


public class SecurityRunner implements Runnable
{
	public static int port1 = 1799;
	public static String password = "bdsp08-06";

	public static void main(String[] args) throws Exception 
	{
		System.out.println("Hand-in exercise for week 8");
		System.out.println("==============================");
		System.out.println("The goal of this assignment is to \n"+ 
							"encrypt an object and send it from \n" + 
							"a client to a server where it will be decrypted");
		System.out.println("================================================");

		String serverIP = "localhost";

		Dog jeff = new Dog(5, "Jeff");
		System.out.println(jeff + "\n");


		new Thread(new SecurityRunner()).start();
		System.out.println("The client is instantiated in a new thread. \n");
		SimpleTcpClient connection = new SimpleTcpClient(serverIP, port1, password);
		connection.send(2, jeff);
	}	

	@Override
	public void run() {

		System.out.println("Thread called and server created");
		try 
		{
			new SimpleTcpServer(port1, password);

		} 
		catch (Exception exception) 
		{
			exception.printStackTrace();
		}	
	}
}
