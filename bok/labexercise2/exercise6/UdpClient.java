package bok.labexercise2.exercise6;
public class UdpClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = 1010;
		UdpReceiver r = new UdpReceiver(port);
		Thread t = new Thread(r);
		t.start();
		
		UdpSender s = new UdpSender("localhost", port);
		
		for (int i = 0; i < Math.pow(10, 5); i++) {
			s.send("Hello");
		}
			
		System.out.println(s.getSent());
		System.out.println(s.getReceived ());
		
		
		
		
		
	}
}
