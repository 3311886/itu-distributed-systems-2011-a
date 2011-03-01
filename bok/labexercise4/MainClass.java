package bok.labexercise4;
// Main program entry point

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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

	public static void main (String [] args) throws NoSuchAlgorithmException,
	IOException {
		BufferedReader bisr = new BufferedReader (new InputStreamReader (System.in));
		System.out.println("Input the number of servers you want to start");
		String input = bisr.readLine();
		while(input.length() == 0 ) {
			input = bisr.readLine();
		}
		int serverCount = Integer.valueOf(input);

		MD5 = MessageDigest.getInstance ("MD5");

		ArrayList<PhonebookServer> servers  = new ArrayList<PhonebookServer>(); 
		for (int i = 0; i< serverCount; i++) {
			PhonebookServer server = new PhonebookServer ();

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
			
			RemotePhonebookServer remoteserver = new RemotePhonebookServer(servers.get(0).getIP());
			ServerInterface s = new ServerInterface(remoteserver);
			IPhonebook phonebook = new RemotePhonebook (servers.get(0).getIP());
			UserInterface ui = new UserInterface (phonebook, s);
			ui.Start ();
		}else {
			System.out.println("Exiting main...missing specification of server count");
		}

		for (PhonebookServer server : servers ) {
			server.abort();
		}
	}
}