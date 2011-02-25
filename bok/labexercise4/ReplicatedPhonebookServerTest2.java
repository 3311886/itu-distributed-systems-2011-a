package bok.labexercise4;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReplicatedPhonebookServerTest2 {
	static MessageDigest MD5;
	private PhonebookServer primary;
	private PhonebookServer secondary;
	private PhonebookServer tertiary;
	
	private IPhonebook phonebook1;
	private IPhonebook phonebook2;
	private IPhonebook phonebook3;
	
	static Guid GuidFromString (String s) {
		byte [] bytes = {};
		try {
			bytes = s.getBytes ("UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println (e.getMessage ());
			System.exit (-1);
		}
		bytes = MD5.digest (bytes);
		return new Guid (bytes);
	}
	
	@Before	
	public void BeforeTest() throws NoSuchAlgorithmException, IOException {
		MD5 = MessageDigest.getInstance ("MD5");
		
		int port = 10 + ((int) Math.random() * 10000); 
		primary = new PhonebookServer(port-1); 
		Thread serverThread = new Thread (primary);
		serverThread.start ();
	
		Guid primaryGUID = GuidFromString(primary.getIP().toString());
		String primaryHostName = primary.getIP().toString();
		phonebook1 = new RemotePhonebook(primary.getIP());
		
		System.out.println ("Primary hostname is " + primaryHostName);
		System.out.println ("Primary server GUID is " + primaryGUID);
		
		secondary = new PhonebookServer(port+1); 
		Thread serverThread2 = new Thread (secondary);
		serverThread2.start ();

		Guid secondaryGUID = GuidFromString(secondary.getIP().toString());
		String secondaryHostName = secondary.getIP().toString();
		phonebook2 = new RemotePhonebook(secondary.getIP());
		
		
		System.out.println ("secondary hostname is " + secondaryHostName);
		System.out.println ("secondary GUID is " + secondaryGUID);
		
		tertiary = new PhonebookServer(port+2); 
		Thread serverThread3 = new Thread (tertiary);
		serverThread3.start ();

		Guid tertiaryGUID = GuidFromString(tertiary.getIP().toString());
		String tertiaryHostName = tertiary.getIP().toString();
		phonebook3 = new RemotePhonebook(tertiary.getIP());
				
		System.out.println ("tertiary hostname is " + tertiaryHostName);
		System.out.println ("tertiary GUID is " + tertiaryGUID); 

	}
	
	@After 
	public void AfterTest() {
		primary.abort ();
		secondary.abort();
		tertiary.abort();
	}
	
	@Test
	/***
	 * Testing add server and remove server
	 */
	public void TestJoinRemoveServer() throws IOException, NoSuchAlgorithmException {
		
		RemotePhonebookServer primaryremote = new RemotePhonebookServer(primary.getIP());
		System.out.println("Test Join Command");
		
		ConnectionPoint cpSecondary = new ConnectionPoint(secondary.getIP());
		ConnectionPoint cpPrimary = new ConnectionPoint(primary.getIP());
		ConnectionPoint cpTertiary = new ConnectionPoint(tertiary.getIP());
		
		Assert.assertEquals("[]", primary.getConnectionPoints().toString());
		primary.ExecuteAndSend(new JoinServerCommand(cpSecondary, cpPrimary));
		
		Assert.assertEquals("[" +secondary.getIP().toString()  +"]",  primary.getConnectionPoints().toString());
		Assert.assertEquals("["+ primary.getIP().toString() + "]", secondary.getConnectionPoints().toString());
		
		Contact c =  new Contact("Andreas", "3958475");
		phonebook2.AddContact(c);
		
		Assert.assertEquals("3958475", phonebook2.Lookup("Andreas"));
		Assert.assertEquals("3958475", phonebook1.Lookup("Andreas"));
		
		Contact c2 =  new Contact("Pelle", "24334");
		phonebook1.AddContact(c2);
		Assert.assertEquals("24334", phonebook1.Lookup("Pelle"));
		Assert.assertEquals("24334", phonebook2.Lookup("Pelle"));
		
		primary.ExecuteAndSend(new JoinServerCommand(cpTertiary, cpPrimary));
		
		Contact c3 =  new Contact("Mette", "45234");
		
		phonebook3.AddContact(c3);
		Assert.assertEquals("45234", phonebook3.Lookup("Mette"));
		Assert.assertEquals("45234", phonebook3.Lookup("Mette"));
		
		primary.ExecuteAndSend(new RemoveServerCommand(cpSecondary, cpPrimary));
		// Handle removeserver  
		Assert.assertEquals(1,primary.getConnectionPoints().size());
		
		Contact c4 = new Contact("Peter", "1234");
		Contact c5 = new Contact("Karen", "12345");
		phonebook1.AddContact(c4);
		
		primary.ExecuteAndSend(new JoinServerCommand(cpSecondary, cpPrimary));
		phonebook1.AddContact(c5);
		System.out.println(phonebook2.Lookup("Peter"));
		
		System.out.println("Phonebook 1:" + phonebook1.GetAllContacts());
		System.out.println("Phonebook 2" + phonebook2.GetAllContacts());
		System.out.println("Phonebook 3" + phonebook3.GetAllContacts());
	} 
}