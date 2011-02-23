package bok.labexercise4;
// Base-class for the wrapping of the IPhonebook methods
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;

public abstract class Command implements Serializable, ICommand  {
  public InetSocketAddress ReturnTo;
  
 
  abstract public Object Execute (IPhonebook phonebook) throws IOException;
}
