package bok.labexercise4;
// Our implementation of the IPhoneBook
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class ReplicatedPhonebook implements IPhonebook {

	// Set of contact objects - ensures distinct objects 
	private Set<Contact> contacts;

	public ReplicatedPhonebook() {
		contacts = new HashSet<Contact>();
	}

	public synchronized AddResult AddContact(Contact contact) throws IOException {
		
		if (findByName(contact.Name) != null) {
			return AddResult.AlreadyThere;
		} else {
			contacts.add(contact);
			return AddResult.OK;
		}
	}

	public synchronized UpdateResult Update(String name, String newPhoneNo)
	throws IOException {
		Contact c = findByName(name); 
		if (c != null) {
			c.PhoneNo = newPhoneNo; 
			return UpdateResult.OK;
		} else {
			return UpdateResult.NotFound;
		}
	}

	/***
	 * 
	 */
	public synchronized String Lookup(String name) throws IOException {
		Contact c = findByName(name);
		return c!= null ? c.PhoneNo : null;
	}

	/***
	 * TODO: Strip server ip from address
	 */
	public synchronized LinkedList<Contact> GetAllContacts() throws IOException {
		return new LinkedList<Contact>(contacts);
	}

	public synchronized boolean Remove(String name) throws IOException {
		// TODO Auto-generated method stub
		Contact c = findByName(name); 
		return contacts.remove(c);
	}
	
	private synchronized Contact findByName(String name ) {
		for(Contact c : contacts ) {
			if (c.Name.equals(name)) return c;
		}
		return null;
	}
	
	 public synchronized void Synchronize(List<Contact> list){
		 
		  for (Contact c : list ) {
			  if (findByName(c.Name) == null) {
				  try {
					AddContact(c);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
		  }
	  }
}