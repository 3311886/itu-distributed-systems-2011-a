package bok.labexercise4;
// Phonebook server accepts commands and executes them.

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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

public class PhonebookServer implements Runnable {
  private IPhonebook phonebook = new Phonebook ();

  private ServerSocket Listener;

  private InetAddress localip;

  public LinkedList<InetSocketAddress> LocalEndpoints = new LinkedList<InetSocketAddress> ();

  public PhonebookServer () throws IOException {
    Listener = new ServerSocket (0);
    Listener.setSoTimeout (2000);

    Enumeration ifs = NetworkInterface.getNetworkInterfaces ();
    for (; ifs.hasMoreElements ();) {
      NetworkInterface nif = (NetworkInterface) ifs.nextElement ();
      Enumeration addrs = nif.getInetAddresses ();
      for (; addrs.hasMoreElements ();) {
        InetAddress ip = (InetAddress) addrs.nextElement ();
        String hostname = ip.getCanonicalHostName ();
        if (!hostname.equals (ip.getHostAddress ()))
          LocalEndpoints.add (new InetSocketAddress (hostname, Listener
              .getLocalPort ()));
      }
    }
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

  void ExecuteAndSend (Command command) throws IOException {
    Object result = command.Execute (phonebook);
    Socket client = new Socket ();
    try {
      client.connect (command.ReturnTo);
      OutputStream os = client.getOutputStream ();
      ObjectOutputStream oos = new ObjectOutputStream (os);
      oos.writeObject (result);
    } finally {
      if (client != null)
        client.close ();
    }
  }

  void HandleConnection (Socket client) throws IOException,
      ClassNotFoundException {
    try {
      InputStream is = client.getInputStream ();
      ObjectInputStream ois = new ObjectInputStream (is);
      Command command = (Command) ois.readObject ();
      ExecuteAndSend (command);
    } finally {
      if (client != null)
        client.close ();
    }
  }
}
