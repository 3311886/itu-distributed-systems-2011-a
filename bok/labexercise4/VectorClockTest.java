package bok.labexercise4;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class VectorClockTest {

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		String isa1 = 
		new InetSocketAddress(InetAddress.getByName("localhost"), 1).toString();
		String isa2 = 
			new InetSocketAddress(InetAddress.getByName("localhost"), 2).toString();
		String isa3 = 
			new InetSocketAddress(InetAddress.getByName("localhost"), 3).toString();
		
		
		
		VectorClock vc1 = new VectorClock();
		
		vc1.incrementClock(isa1);
		vc1.incrementClock(isa2);
		vc1.incrementClock(isa3);
		vc1.incrementClock(isa3);
		vc1.incrementClock(isa1);
		vc1.incrementClock(isa3);
		
		System.out.println(vc1.toString());
		
		
		VectorClock vc2 = new VectorClock();
		vc2.incrementClock(isa2);
		vc2.incrementClock(isa1);
		vc2.incrementClock(isa3);
		vc2.incrementClock(isa1);
		vc2.incrementClock(isa2);
		
		System.out.println(vc2.toString());
		
		VectorClock vcmax =  VectorClock.max(vc1,vc2);
		System.out.print(vcmax.toString());
	}
}
