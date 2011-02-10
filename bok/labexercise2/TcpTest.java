package bok.labexercise2;

public class TcpTest implements Runnable {
public static int port = 4568;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Exercise3();		
	}
	
	
	public static void Exercise3() throws Exception {
		System.out.println("Exercise 3: A round trip");
		String serverIP = "localhost";
		// TODO Auto-generated method stub
		
		new Thread(new TcpTest()).start();
		
		
		SimpleTcpClient stc = new SimpleTcpClient(serverIP, port);
		stc.send("Hello");
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