package bok.labexercise4;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract Server class that implements runnable 
 *
 */
public abstract class AbstractServer implements Runnable{
	private ServerSocket Listener;
	private InetSocketAddress localisa;
	private boolean printServerResults = true; 

	public LinkedList<InetSocketAddress> LocalEndpoints = new LinkedList<InetSocketAddress> ();

	/***
	 * @param port, specific port to start server on
	 * @throws IOException
	 */
	public AbstractServer(int port) throws IOException{
		initServer(port);
	}

	public AbstractServer() throws IOException {
		// 0 creates a server on the first available port 
		initServer(0);
	}
	
	/***
	 * Initializes server 
	 * @param port
	 * @throws IOException
	 */
	private void initServer(int port) throws IOException{
		Listener = new ServerSocket(port);
		Listener.setSoTimeout (2000);

		Enumeration ifs = NetworkInterface.getNetworkInterfaces ();
		for (; ifs.hasMoreElements ();) {
			NetworkInterface nif = (NetworkInterface) ifs.nextElement ();
			Enumeration addrs = nif.getInetAddresses ();
			for (; addrs.hasMoreElements ();) {
				InetAddress ip = (InetAddress) addrs.nextElement ();
				String hostname = ip.getCanonicalHostName ();
				if (!hostname.equals (ip.getHostAddress ()) )

					LocalEndpoints.add (new InetSocketAddress (hostname, Listener
							.getLocalPort()));
			}
		}
		// Get the servers own ip  
		localisa= new InetSocketAddress(InetAddress.getLocalHost(), Listener.getLocalPort());
	}

	boolean abort = false;

	static ExecutorService exeservice = Executors.newCachedThreadPool();

	public void run () {
		try {
			while (!abort) {
				try {
					final Socket client = Listener.accept();
					exeservice.execute (new Runnable () {
						public void run () {
							try {
								HandleConnection (client);
							} catch (Exception e) {
								System.err.println (e.getMessage ());
								System.exit (-1);
							}
						}
					});
				} catch (SocketTimeoutException e) {
				}
			}
		} catch (Exception e) {
			System.err.println (e.getMessage ());
			System.exit (-1);
		} finally {
			try {
				Listener.close ();
			} catch (IOException e) {
			}
			exeservice.shutdown ();
		}
	}

	public synchronized void abort () {
		abort = true;
	}

	abstract void ExecuteAndSend(Object command) throws IOException;

	/**
	 * Handles incoming requests 
	 * @param client
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	void HandleConnection (Socket client) throws IOException,
	ClassNotFoundException {
		try {
			InputStream is = client.getInputStream ();
			ObjectInputStream ois = new ObjectInputStream (is);
			Object command = ois.readObject ();
			ExecuteAndSend(command);
		} finally {
			if (client != null)
				client.close ();
		}
	}

	/***
	 * Returns InetSocketAddress for the server
	 * @return
	 */
	public InetSocketAddress getIP() {
		return this.localisa;
	}

	public void printServerResults(boolean flag) {
		printServerResults = flag;
	}

	public boolean printServerResults() {
		return printServerResults;
	}
}