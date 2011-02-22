package bok.labexercise4;
// Command wrapper class

import java.io.IOException;
import java.io.Serializable;

public class GetAllContactsCommand extends Command implements Serializable {
  public Object Execute (IPhonebook phonebook) throws IOException {
    return phonebook.GetAllContacts();
  }
}