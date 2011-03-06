package bok.labexercise4.extended.test;

import java.io.IOException;

import bok.labexercise4.extended.Book;
import bok.labexercise4.extended.DataItemFactory;

public class Test2 {

	/**
	 * @param args
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, IOException {
		Book b = (Book) DataItemFactory.build(Book.class);
		
		Book.build(b, "12123", "234", 2001,"dfsdf");
		Book b2 = (Book) DataItemFactory.build(Book.class);

//		BufferedReader bisr = new BufferedReader (new InputStreamReader (System.in));
//		for (Field f : Book.class.getDeclaredFields()) {
//			System.out.printf("%s: %s", f.getName(), f.get(b));
//		}
		
		

//		for (Field f : b2.getClass().getFields()) {
//			if (f.getAnnotations().length>0) {
//				// a = 
//				//Annotation a = f.getAnnotations()[0];
//				FieldAnnotation a =  (FieldAnnotation) f.getAnnotations()[0];
//				
//				System.out.println(a.value() + ": " + f.getType());
//				String input = bisr.readLine (); 
//				while(input.length()  ==0 ) {
//					input = bisr.readLine();
//				}		
//			
//				Book.class.getField(f.getName()).set(b2, f.getType().cast(input));
//			}
//		}
	}
}
