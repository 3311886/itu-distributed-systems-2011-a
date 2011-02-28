package bok.labexercise4;

 

import java.io.IOException;
import java.util.Collections;
import java.util.List;



public class TestingHashingSet 
{ 
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
			IPhonebook p1 = new ReplicatedPhonebook();
			IPhonebook p2 = new ReplicatedPhonebook();
			IPhonebook p3 = new ReplicatedPhonebook();
			
			Contact c1 = new Contact("Andreas", "60565656");
			Contact c2 = new Contact("Peter", "324");
			Contact c3 = new Contact("Stine", "3645");
			p1.AddContact(c3);
			p1.AddContact(c2);
			p1.AddContact(c1);
			
			p2.AddContact(c1);
			p2.AddContact(c2);
			p2.AddContact(c3);
			
			p3.AddContact(c2);
			p3.AddContact(c1);
			p3.AddContact(c3);
			
			List<Contact> l1 = p1.GetAllContacts();
			List<Contact> l2 = p2.GetAllContacts();
			List<Contact> l3 = p3.GetAllContacts();
			Collections.sort(l1);
			Collections.sort(l2);
			Collections.sort(l3);
			
			System.out.println("Test implementation of Comparable interface in Contact");
			System.out.println("Phonebook 1:" + l1);
			System.out.println("Phonebook 2" + l2);
			System.out.println("Phonebook 3" + l3);
			
	}
}

