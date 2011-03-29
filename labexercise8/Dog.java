package labexercise8;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Dog implements Serializable
{
	int legs;
	String name;

	public Dog(int legs, String name)
	{
		this.legs=legs;
		this.name=name;
	}
	
	public String toString()
	{
		return "The instantiated object is a dog with " + Integer.toString(legs) + " legs and its name is " + name;	
	}
	
	public void print()
	{
		System.out.println("Your new dogs name is " + this.name + ".");
		System.out.println(this.name + " has " + this.legs + " legs!");
		
	}
	
}
