package bok.labexercise4;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerInterface {
	private RemotePhonebookServer server;

	public ServerInterface(RemotePhonebookServer server) {
		this.server = server;
	}
	void JoinCommand (BufferedReader bisr) throws IOException {
		InetSocketAddress isa = GetIP(bisr, "joining");
		System.out.println (isa.toString());
		server.addConnectionPoint(isa);
	}
	
	void RemoveCommand (BufferedReader bisr) throws IOException {
		InetSocketAddress isa = GetIP(bisr, "joining");
		server.removeConnectionPoints(isa);
	}

	static InetSocketAddress GetIP (BufferedReader bisr, String args) throws IOException {
		System.out.printf("Input %s server hostname: ", args);
		String hostname = bisr.readLine (); 
		System.out.printf("Input %s server port: ", args);
		int port = bisr.read();
		return new InetSocketAddress(hostname, port);
	}
}	
