package bok.labexercise4;
// Main program entry point

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    MD5 = MessageDigest.getInstance ("MD5");
    PhonebookServer server = new PhonebookServer ();

    IPhonebook phonebook = new RemotePhonebook (server.getIP());
    RemotePhonebookServer remoteserver = new RemotePhonebookServer(server.getIP());
    
    ServerInterface s = new ServerInterface(remoteserver);
    UserInterface ui = new UserInterface (phonebook, s);
    Thread serverThread = new Thread (server);
    serverThread.start ();
    
    if(args.length > 0) {
    String host = args[0];
    int portnum = Integer.valueOf(args[1]);
    InetSocketAddress joiner = new InetSocketAddress(host, portnum);
     
    server.addConnectionPoint(new JoinServerCommand(joiner, server.getIP()));
    System.out.println(server.getConnectionPoints().toString());
    } 
    
    PhonebookServer server2 = new PhonebookServer ();
    IPhonebook phonebook2 = new RemotePhonebook (server2.getIP());
    Thread serverThread2 = new Thread(server2);
    serverThread2.start();
    
    System.out.println ("I'm listening on");
    for (InetSocketAddress sa : server.LocalEndpoints) {
      System.out.println ("  " + sa);
    }
    System.out.println ("My GUID is "
        + GuidFromString (server.getIP().toString()));
    
    System.out.println ("I'm listening on");
    for (InetSocketAddress sa : server2.LocalEndpoints) {
      System.out.println ("  " + sa);
    }
    System.out.println ("My GUID is "
        + GuidFromString (server2.getIP().toString()));
    ui.Start ();
    ui.Start ();
    server.abort ();
    server2.abort();
  }
}