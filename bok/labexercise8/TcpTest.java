package bok.labexercise8;

public class TcpTest implements Runnable {
	public static int port = 1213;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Exercise optional 8: Secure");
		String serverIP = "localhost";

		new Thread(new TcpTest()).start();

		SimpleTcpSSLClient stc = new SimpleTcpSSLClient(serverIP,port, "bdsp08-06");
		// We try to call to the server
		stc.send(1, "Hello");
		stc.send(0, "Hello");
	}

	public void run() {
		System.out.println("Thread called and server created");
		try {
			new SimpleTcpSSLServer(port, "bdsp08-06");

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}