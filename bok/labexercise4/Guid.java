package bok.labexercise4;
// GUID (globally unique ID)

public class Guid {
  byte [] id;

  public Guid (byte [] guid) {
    id = guid;
  }

  @Override
public String toString () {
    return String.format ("%02X%02X%02X%02X%02X%02X%02X%02X", id [7], id [6],
        id [5], id [4], id [3], id [2], id [1], id [0]);
  }

  public int compareTo (Guid guid) {
    for (int i = 7; i >= 0; i--) {
      if (guid.id [i] < id [i])
        return -1;
      else if (guid.id [i] > id [i])
        return 1;
    }
    return 0;
  }

}
