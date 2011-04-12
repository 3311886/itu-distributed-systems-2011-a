package pellekrogholt.labexercise10_phonebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;  
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService(name="phoneBookService", targetNamespace = "http://hello.samples.smds2011.itu.dk/", serviceName="phoneBookService")
public class PhoneBookService 
{
	
	// for now just store person in memory - simply in a hash map. 
	private Map<String, Person> map = new HashMap<String, Person>();
	
	/* note: a simpler approach could have been
	   public String addPerson(Sting name, String street, int phone ...) { */
	
	@WebMethod()
	public String addPerson(Person person) {
		
		map.put(person.getName(), person);

		return person.getName() + " added to the phone book service";
	}

	@WebMethod()
	public String getPerson(String name) {

		// for now just store person in memory
		Person person = map.get(name);
		
		return person.toString() + " picked from the the phone book service";
	}
	
	
	

	/**
	 * Publish the service end point.
	 * @param args not used.
	 */
	public static void main(String[] args) 
	{
		Endpoint.publish("http://localhost:8085/labexercises/person", new PhoneBookService());
	}
}