package bok.labexercise4;
// Command wrapper class

import java.io.IOException;
import java.io.Serializable;

public class RemoveCommand extends ReplicateCommand implements Serializable {
  private String Name;

  public RemoveCommand (String name) {
	  
    Name = name;
  }

  @Override
public Object Execute (IPhonebook phonebook) throws IOException {
    return phonebook.Remove (Name);
  }
}
