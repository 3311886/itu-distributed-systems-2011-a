package bok.labexercise4;
// Command wrapper class

import java.io.IOException;
import java.io.Serializable;

public class UpdateCommand extends Command implements Serializable {
  private String Name, NewPhoneNo;

  public UpdateCommand (String name, String newPhoneNo) {
    Name = name;
    NewPhoneNo = newPhoneNo;
  }

  public Object Execute (IPhonebook phonebook) throws IOException {
    return phonebook.Update (Name, NewPhoneNo);
  }
}