package bok.labexercise4;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Set;

public class ServerInterface {
	private RemotePhonebookServer server;

	public ServerInterface(RemotePhonebookServer server) {
		this.server = server;
	}
	void JoinCommand(BufferedReader bisr, boolean asJoiner) throws IOException {
		InetSocketAddress isa;
		if (asJoiner) {
			isa = GetIP(bisr, "joinee");
		} else {
			isa = GetIP(bisr, "joining"); 
		}
		System.out.println(server.addConnectionPoint(isa, asJoiner));
	}
	
	void RemoveCommand (BufferedReader bisr) throws IOException {
		InetSocketAddress isa = GetIP(bisr, "removing");
		server.removeConnectionPoint(isa);
	}
	
	void GetConnectionPointsCommand () throws IOException {
		Set<InetSocketAddress> list =  server.getConnectionPoints();
		System.out.printf("%s\n", "Connected server: ");
		for (InetSocketAddress isa : list ) {
			System.out.println(isa);
		}
		
	}

	static InetSocketAddress GetIP (BufferedReader bisr, String args) throws IOException {
		System.out.printf("Input %s server hostname: ", args);
		String hostname = bisr.readLine (); 
		System.out.printf("Input %s server port: ", args);
		String portStr = bisr.readLine();
		int port = Integer.valueOf(portStr);
		return new InetSocketAddress(hostname, port);
	}
	
	void ConnectToServer(BufferedReader bisr) throws IOException {
		InetSocketAddress isa = GetIP(bisr, "new server");
		server.ConnectToServer(isa);
		
		System.out.println("Now connected to: " + isa);
	}
}	
