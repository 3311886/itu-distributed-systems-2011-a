package bok.labexercise4.extended;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class DataItemFactory {
	private static List<Class<?>> classes; 
	
	public static List<Class<?>> getClasses(){
		classes = new ArrayList<Class<?>>();
		classes.add(Book.class);
		classes.add(Car.class);
		classes.add(Bike.class);
		
		return classes;
	}
	
	@SuppressWarnings("unchecked")
	public static <E> IItem<E> build(Class<E> c) {
		IItem<E> clazz = null;
		try {
			clazz = (IItem<E>) c.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clazz;
	}
	
	public static IItem<?> Create(IItem<?> item, HashMap<String, Object> values) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
		
		for (Entry<String, Object> e : values.entrySet()) {
			Field f = item.getClass().getField(e.getKey());
			f.set(item, DataFactoryHelper.castObject(f, e.getValue()));
		}
		return item;
	}
	
	
	
}
