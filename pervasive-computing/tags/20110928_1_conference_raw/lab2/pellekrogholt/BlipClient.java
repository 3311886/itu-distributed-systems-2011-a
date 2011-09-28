package lab2.pellekrogholt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * 
 * Blip Client 1.1
 *
 */
public class BlipClient {

	URL pitlab;
	
	public BlipClient(String url, String point) throws IOException {
		
		pitlab = new URL(url + point);

	}
		
	public BufferedReader device() throws IOException {

		URLConnection uc = pitlab.openConnection();
		BufferedReader in = new BufferedReader(
			new InputStreamReader(uc.getInputStream())
		);		
		return in;
	}
	
	

}