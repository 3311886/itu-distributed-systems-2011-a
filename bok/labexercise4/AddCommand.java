package bok.labexercise4;
// Command wrapper class

import java.io.*;

public class AddCommand extends Command implements Serializable {
  private Contact Contact;

  public AddCommand (Contact contact) {
    Contact = contact;
  }

  public Object Execute (IPhonebook phonebook) throws IOException {
    return phonebook.AddContact (Contact);
  }
}
