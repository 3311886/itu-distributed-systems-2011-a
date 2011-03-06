package bok.labexercise4.extended;

import java.lang.reflect.Field;



public class DataFactoryHelper {
	@SuppressWarnings("unchecked")
	public static <T> T castObject(Field f, Object o) {
		T t = null; 
		
		
		if (f.getType().equals(java.lang.Integer.class)) {
			t = (T) Integer.valueOf(o.toString());
		 }else 
		if (f.getType().equals(java.lang.String.class)) {
			t = (T) o.toString();
		}else if (f.getType().equals(java.lang.Double.class)) {
			t = (T) Double.valueOf(o.toString());
		}
		return t;
		
	}
} 	
	
