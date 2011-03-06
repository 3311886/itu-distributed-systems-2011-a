package bok.labexercise4.extended;

import java.io.Serializable;


public abstract class IItem<T> implements Comparable<T>, Serializable{
	abstract public void Update(T itemNew);
	
	abstract public  String toString();
	abstract public int compareTo(T o);
	abstract public boolean getByKey(Object key) ;
	
}
