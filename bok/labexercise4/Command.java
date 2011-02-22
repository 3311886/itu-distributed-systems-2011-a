package bok.labexercise4;
// Base-class for the wrapping of the IPhonebook methods
import java.io.*;
import java.net.*;

public abstract class Command implements Serializable {
  public InetSocketAddress ReturnTo;

  abstract public Object Execute (IPhonebook phonebook) throws IOException;
}
