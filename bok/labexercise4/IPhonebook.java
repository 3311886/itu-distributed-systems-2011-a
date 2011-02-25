package bok.labexercise4;
// Phone book interface

import java.io.IOException;
import java.util.List;

public interface IPhonebook {
  AddResult AddContact (Contact contact) throws IOException;

  UpdateResult Update (String name, String newPhoneNo) throws IOException;
  String Lookup (String name) throws IOException;

  // Returns null if name is not in the phone
  // book

  List<Contact> GetAllContacts () throws IOException;

  boolean Remove (String name) throws IOException;
  
  // Returns true if name was found and
  // removed
  
  void Synchronize(List<Contact> contacts) ;
}

