package bok.labexercise4;
// Command wrapper class

import java.io.*;

public class LookupCommand extends Command implements Serializable {
  private String Name;

  public LookupCommand (String name) {
    Name = name;
  }

  public Object Execute (IPhonebook phonebook) throws IOException {
    return phonebook.Lookup (Name);
  }
}
