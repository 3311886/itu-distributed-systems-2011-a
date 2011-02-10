package bok.labexercise2.exercise4;

public class TcpTest implements Runnable {
public static int port = 4567;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Exercise4();		
	}
	
	public static void Exercise4() throws Exception{
		System.out.println("Exercise 4: Supporting different functionality");
		String serverIP = "localhost";
		// TODO Auto-generated method stub
		
		new Thread(new TcpTest()).start();
		
		SimpleTcpClient stc = new SimpleTcpClient(serverIP, port);
		stc.send(1, "Hello");
		stc.send(0, "Hello");
		
		
	}

	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Thread called and server created");
		try {
			new SimpleTcpServer(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
