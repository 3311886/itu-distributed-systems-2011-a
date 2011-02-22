package bok.labexercise4;
// Data structure for holding data for a contact

import java.io.Serializable;

public class Contact implements Serializable {
  public String Name;

  public String PhoneNo;

  public Contact (String name, String phoneNo) {
    Name = name;
    PhoneNo = phoneNo;
  }

  public String toString () {
    return "{ Name: " + Name + "; PhoneNo: " + PhoneNo + " }";
  }
}