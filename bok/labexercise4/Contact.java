package bok.labexercise4;
// Data structure for holding data for a contact

import java.io.Serializable;
import java.net.InetSocketAddress;

public class Contact extends Person<Contact> implements Serializable {
	public String Name;
	public String PhoneNo;
	private InetSocketAddress cp;

	public Contact(String name, String phoneNo) {
		Name = name;
		PhoneNo = phoneNo;
	}
	
	public void setConnectionPoint(InetSocketAddress isa) {
		cp = isa;
	}
	public InetSocketAddress getConnectionPoint() {
		return cp;
	}

	@Override
	public String toString () {
		return "{ Name: " + Name + "; PhoneNo: " + PhoneNo + " }";
	}

	public int compareTo(Contact o) {
		
		return this.Name.compareTo(o.Name);
	}

	public int compare(Contact o1, Contact o2) {
		// TODO Auto-generated method stub
		return o1.Name.compareTo(o2.Name);
	}	
}