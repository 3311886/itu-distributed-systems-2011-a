package bok.labexercise4.extended.test;
// Main program entry point

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import bok.labexercise4.Guid;
import bok.labexercise4.extended.AbstractServer;
import bok.labexercise4.extended.BokServer;
import bok.labexercise4.extended.gui.ClientInterface;
import bok.labexercise4.extended.gui.RemoteServerUI;
import bok.labexercise4.extended.gui.ServerInterface;
import bok.labexercise4.extended.gui.UserInterface;

class MainClass {
	static MessageDigest MD5;

	static Guid GuidFromString (String s) {
		byte [] bytes = {};
		try {
			bytes = s.getBytes ("UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println (e.getMessage ());
			System.exit (-1);
		}
		bytes = MD5.digest (bytes);
		return new Guid (bytes);
	}

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static void main (String [] args) throws NoSuchAlgorithmException,
	IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, NoSuchFieldException 
	{
		
		BufferedReader bisr = new BufferedReader (new InputStreamReader (System.in));
		System.out.println("Input the number of servers you want to start");
		String input = bisr.readLine();
		while(input.length() == 0 ) {
			input = bisr.readLine();
		}
			int serverCount = Integer.valueOf(input);

			MD5 = MessageDigest.getInstance ("MD5");

			ArrayList<BokServer> servers  = new ArrayList<BokServer>(); 
			for (int i = 0; i< serverCount; i++) {
				BokServer server = new BokServer();

				Thread serverThread = new Thread (server);
				serverThread.start ();

				servers.add(server);
				String s = i == 0 ? " your server" : " another server"; 
				System.out.printf("I'm%s listening on\n", s);
				for (InetSocketAddress sa : server.LocalEndpoints) {
					System.out.println ("  " + sa);
				}
				System.out.println ("My GUID is "
						+ GuidFromString (server.getIP().toString()));
			}		

		
		if (servers.size()> 0) {
						ServerInterface si = new ServerInterface(servers.get(0).getIP());
						RemoteServerUI remoteserver = new RemoteServerUI(si);
						
						ClientInterface ci  = new ClientInterface (servers.get(0).getIP());
						UserInterface ui = new UserInterface (ci, remoteserver);
						ui.Start();
		}else {
			System.out.println("Exiting main...missing specification of server count");
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (AbstractServer s: servers) {
			s.abort();
		}			
	}
}
