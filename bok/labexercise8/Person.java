package bok.labexercise8;

import java.io.Serializable;

public class Person implements Serializable{
private String name;
private String address;
private int zip;
private String phone;


public Person(String name, String address, int zip, String phone) {
	super();
	this.name = name;
	this.address = address;
	this.zip = zip;
	this.phone = phone;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public int getZip() {
	return zip;
}
public void setZip(int zip) {
	this.zip = zip;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}

public String toString() {
	return String.format("Name: %s\nAddress: %s\nZip: %d\nPhone: %s", this.name, this.address, this.zip, this.phone); 
}

@Override
	public boolean equals(Object o) {
		if (!(o instanceof Person)) return false;
		Person p = (Person)o;
		return ((this.name.equals(p.name)) && (this.address.equals(p.address)) && (this.zip == p.zip) && this.phone.equals(p.phone));
	}

}
