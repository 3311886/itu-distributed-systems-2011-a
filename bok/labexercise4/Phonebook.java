package bok.labexercise4;
// Phonebook database implementation
// Made thread-safe by making all methods synchronized

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Phonebook implements IPhonebook {
  private Map<String, String> NameTable;

  public Phonebook () {
    NameTable = new HashMap<String, String> ();
  }

  public synchronized AddResult AddContact (Contact contact) {
    if (NameTable.containsKey (contact.Name)) {
      return AddResult.AlreadyThere;
    } else {
      NameTable.put (contact.Name, contact.PhoneNo);
      return AddResult.OK;
    }
  }

  public synchronized UpdateResult Update (String name, String newPhoneNo) {
    if (NameTable.containsKey (name)) {
      NameTable.put (name, newPhoneNo);
      return UpdateResult.OK;
    } else {
      return UpdateResult.NotFound;
    }
  }

  public synchronized String Lookup (String name) {
    if (NameTable.containsKey (name)) {
      return NameTable.get (name);
    } else {
      return null;
    }
  }

  public synchronized List<Contact> GetAllContacts () {
    List<Contact> result = new LinkedList<Contact> ();
    for (Map.Entry<String, String> entry : NameTable.entrySet ()) {
      result.add (new Contact (entry.getKey (), entry.getValue ()));
    }
    return result;
  }

  public synchronized boolean Remove (String name) {
    return (NameTable.remove (name) != null);
  }
}