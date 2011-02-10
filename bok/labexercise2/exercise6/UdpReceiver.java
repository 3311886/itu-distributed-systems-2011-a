package bok.labexercise2.exercise6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpReceiver implements Runnable{
	
	private DatagramSocket datagramSocket;
	private byte[] buffer;
	private byte[] sendData;
	
	public UdpReceiver(int port)  {
		
		try {
			datagramSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buffer = new byte[1024]; // max 65.536 bytes
		sendData = new byte[1024];
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
		DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
		try {
			datagramSocket.receive( datagramPacket );
			String message = new String( datagramPacket.getData());
			message = message.trim(); //removes white spaces
			//System.out.println(message);
			
			
			InetAddress IPAddress = datagramPacket.getAddress();
            int port = datagramPacket.getPort();
            String data = "true";
            sendData = data.getBytes();
            DatagramPacket sendPacket =
            new DatagramPacket(sendData, sendData.length, IPAddress, port);
            datagramSocket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		} 
	}
}


