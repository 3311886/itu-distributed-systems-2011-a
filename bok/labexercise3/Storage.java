package bok.labexercise3;

import java.util.Vector;

/***
 * 
 * @author Andreas
 * A singleton class emulating server storage   
 */
public class Storage {
	private static Vector<Object> storage;
	
	public Storage() {
		this.storage = new Vector<Object>();
	}
	
	public static Object getObject(int index ) {
		
		return getStorage().get(index);
	}
	
	public static void putObject(Object o) {
		getStorage().add(o);
	}
	
	public static Object removeObject(int index) {
		return getStorage().remove(index);
	}
	
	public static Object removeObject(Object o) {
		return getStorage().remove(o);
	}
	
	public static Vector<Object>  getStorage() {
		if (storage != null) return storage;
		else { 
			storage = new Vector<Object>();
			return getStorage();
		}
	}
}
