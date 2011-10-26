package dk.itu.spct;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import dk.itu.spct.R;
import dk.itu.spct.R.id;
import dk.itu.spct.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;


public class TCPSendActivity extends Activity {

	
    private static final String TAG = "Fluidgallery - TCPSendActivity";
    private static final boolean D = true;
	
    private TextView serverStatus;

    // default ip
    //public static String SERVERIP = "10.25.254.241";
    // note: a default seems confusing right ?
    public static String DEVICEIP;

    //



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activitytcpclient);

        if(D) Log.e(TAG, "onCreate called");
        
        serverStatus = (TextView) findViewById(R.id.server_status);

        DEVICEIP = getLocalIpAddress();
        
        
        String messageAsString = "Hello from android";
        
        serverStatus.setText("Device IP: " + DEVICEIP + " Message sent: " + messageAsString);
        
        
        InetAddress receiversAddress;
		try {
			receiversAddress = InetAddress.getByName("10.25.254.241");

	        int receiversPort = 7656;
			
			// create a new socket
			Socket socket = new Socket( receiversAddress, receiversPort );
			
			OutputStream os = socket.getOutputStream();
			// could have gotten an InputStream as well
			
			DataOutputStream dos = new DataOutputStream( os );
			
			
			
			dos.writeUTF(messageAsString);
			
			dos.flush();
			
			
			// todo: clean me up 
			socket.close();

		
		} catch (UnknownHostException e) {
			serverStatus.setText("error");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			serverStatus.setText("error");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
//        Thread fst = new Thread(new ServerThread());
//        fst.start();
    }

    // gets the ip address of your phone's network
    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
                }
            }
        } catch (SocketException ex) {
            if(D) Log.e(TAG, ex.toString());
        }
        return null;
    }



}
