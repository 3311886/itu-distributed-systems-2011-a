package bok.labexercise4.extended;

import java.io.IOException;
import java.util.List;

public interface IDataCollection<T>{
	  Object AddItem (T item) throws IOException;

	  boolean Update (Object key, T item) throws IOException;
	  Object Get(Object key) throws IOException;

	  // Returns null if name is not in the phone
	  // book

	  List<T> GetAll() throws IOException;

	  boolean Remove (Object key) throws IOException;
	  
	  // Returns true if name was found and
	  // removed
	  
	  void Synchronize(List<?> list) ;	
}