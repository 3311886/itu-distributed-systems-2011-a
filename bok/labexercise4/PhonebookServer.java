package bok.labexercise4;
// Phonebook server accepts commands and executes them.

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PhonebookServer extends AbstractServer{
  private IPhonebook phonebook = new Phonebook ();

  public PhonebookServer () throws IOException {
	  super();
	  
  }
  
  @Override
  void ExecuteAndSend(Object command) throws IOException {
		ExecuteAndSend((Command) command);
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
}