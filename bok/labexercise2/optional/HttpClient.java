package bok.labexercise2.optional;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class HttpClient {
	static final int PORT = 80;
	static final String REQUEST= "GET / HTTP/1.1\nHost: www.itu.dk\nUser-Agent: none\n\n";
	static final String HOST = "www.itu.dk";
	
	public static void main(String[] args) throws Exception {
		Socket s = new Socket(HOST, PORT);
		
		OutputStream os = s.getOutputStream();
		PrintWriter to = new PrintWriter( os );
		
		InputStream is = s.getInputStream();
		InputStreamReader sr = new InputStreamReader( is );
		
	        to.print( REQUEST ); //send http-request
	        to.flush();
		StringBuffer sb = new StringBuffer();	

		while (!sr.ready()) ; //wait for reply from server

		int x=1;
		while (sr.ready()) { //read http-response
			char c = (char) sr.read();
			sb.append( c );
		}
		System.out.println(sb.toString());		
	}
}