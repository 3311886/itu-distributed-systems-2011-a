package bok.labexercise4;
// Simple-minded console-based user interface
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInterface {
  private IPhonebook phonebook;
  private ServerInterface serverinterface;

  public UserInterface (IPhonebook pb, RemotePhonebookServer server) {
    phonebook = pb;
    serverinterface = new ServerInterface(server);
  }

  void Start () {
    BufferedReader bisr = new BufferedReader (new InputStreamReader (System.in));
    while (true) {
      PrintOptions ();
      try {
        String command = bisr.readLine ().toUpperCase () + "Q";
        System.out.println ();
        switch (command.charAt (0)) {
        case 'A':
          AddCommand (bisr);
          break;
        case 'D':
          DeleteCommand (bisr);
          break;
        case 'F':
          FindCommand (bisr);
          break;
        case 'J':
			serverinterface.JoinCommand (bisr);
			break;
        case 'L':
          ListCommand ();
          break;
        case 'U':
          UpdateCommand (bisr);
          break;
        case 'Q':
          return;
        case 'R':
			serverinterface.RemoveCommand(bisr);
			break;
        default:
          System.out.println ("Unknown command: " + command.charAt (0)
              + ", try again");
          break;
        }
      } catch (IOException e) {
        System.err.println (e.getLocalizedMessage ());
        System.exit (-1);
      }
    }
  }

  static void PrintOptions () {
    System.out.println ();
    System.out.println ("(A) Add new contact");
    System.out.println ("(D) Delete contact");
    System.out.println ("(F) Find contact");
    System.out.println ("(J) Add new server");
    System.out.println ("(L) List all contacts");
    System.out.println ("(R) Remove server");
    System.out.println ("(U) Update contact");
    System.out.println ("(Q) Quit");
  }

  void AddCommand (BufferedReader bisr) throws IOException {
    String name = GetName (bisr);
    String phoneNo = GetPhoneNo (bisr);
    Contact contact = new Contact (name, phoneNo);
    AddResult result = phonebook.AddContact (contact);
    switch (result) {
    case OK:
      System.out.println ("OK");
      break;
    case AlreadyThere:
      System.out.println (name + " is already in the phone book");
      break;
    }
  }

  void DeleteCommand (BufferedReader bisr) throws IOException {
    String name = GetName (bisr);
    if (phonebook.Remove (name)) {
      System.out.println ("Contact deleted");
    } else {
      System.out.println ("Contact not found");
    }
  }

  void FindCommand (BufferedReader bisr) throws IOException {
    String name = GetName (bisr);
    String result = phonebook.Lookup (name);
    if (result != null) {
      System.out.println (name + " has phone no: " + result);
    } else {
      System.out.println ("Contact not found");
    }
  }

  void ListCommand () throws IOException {
    System.out.println ();
    for (Contact contact : phonebook.GetAllContacts ()) {
      System.out.println (contact);
    }
    System.out.println ();
  }

  void UpdateCommand (BufferedReader bisr) throws IOException {
    String name = GetName (bisr);
    String phoneNo = GetPhoneNo (bisr);
    UpdateResult result = phonebook.Update (name, phoneNo);
    switch (result) {
    case OK:
      System.out.println ("Updated");
      break;
    case NotFound:
      System.out.println (name + " is not in the phone book");
      break;
    }
  }

  static String GetName (BufferedReader bisr) throws IOException {
    System.out.print ("Name: ");
    return bisr.readLine ();
  }

  static String GetPhoneNo (BufferedReader bisr) throws IOException {
    System.out.print ("Phone no: ");
    return bisr.readLine ();
  }
}
