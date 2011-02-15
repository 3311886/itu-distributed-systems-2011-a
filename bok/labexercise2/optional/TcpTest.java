package bok.labexercise2.optional;


public class TcpTest implements Runnable {
public static int port = 4568;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Exercise optional 1: A round trip");
		String serverIP = "localhost";
		// TODO Auto-generated method stub
		
		new Thread(new TcpTest()).start();
		
		SimpleTcpClient stc = new SimpleTcpClient(serverIP, port);
		Person p1 = new Person("Andreas", "Storegade 2", 1020, "60565656");
		Person p2 = new Person("Bettina", "Prinsegade 2", 2030, "60565656");
		Person p3 = new Person("Thor", "Allergade 4", 4030, "60565656");
		
		stc.send(1, p1);
		System.out.println(stc.getSize());
		stc.send(1, p2);
		System.out.println(stc.getSize());
		stc.send(1, p3);
		System.out.println(stc.getSize());
		
		stc.send(2, 0);
		System.out.println(stc.getSize());
		
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
