package bok.labexercise2.exercise6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpSender {
	private int sent;
	private int received;
	private DatagramSocket datagramSocket ;
	private InetAddress receiversAddress;
	int port;
	public UdpSender(String address, int port) {
		try {
			receiversAddress = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			datagramSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.port= port;
		sent = 0;
		received = 0;

	}
	
	

	public void send(String m){
		String messageAsString = m;
		byte [ ] message = messageAsString.getBytes();
		int messageLength = messageAsString.length();
		DatagramPacket datagramPacket = new DatagramPacket(message, messageLength, receiversAddress, port);
		try {
			datagramSocket.send(datagramPacket);
			sent++;
			
			receive();
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void receive() {
		
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			datagramSocket.receive(receivePacket);
			String s = new String(receivePacket.getData());
			if (s.length() > 0)
				received++;
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public int getSent() {
		return this.sent;
	}
	public int getReceived() {
		return this.received;
	}
	
	
}
