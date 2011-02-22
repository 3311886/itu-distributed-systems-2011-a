package bok.labexercise4;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReplicatedPhonebookServerTest {
	static MessageDigest MD5;
	private ReplicatedPhoneBookServer primary;
	private ReplicatedPhoneBookServer secondary;

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
		primary = new ReplicatedPhoneBookServer(); 
		Thread serverThread = new Thread (primary);
		serverThread.start ();
	
		Guid primaryGUID = GuidFromString(primary.LocalEndpoints.get(1).toString());
		String primaryHostName = primary.LocalEndpoints.get(1).getHostName();
		
		System.out.println ("Primary hostname is " + primaryHostName);
		System.out.println ("Primary server GUID is " + primaryGUID);

		secondary = new ReplicatedPhoneBookServer(); 
		Thread serverThread2 = new Thread (secondary);
		serverThread2.start ();

		Guid secondaryGUID = GuidFromString(secondary.LocalEndpoints.get(0).toString());
		String secondaryHostName = secondary.LocalEndpoints.get(0).getHostName();
		
		System.out.println ("Primary hostname is " + secondaryHostName);
		System.out.println ("Secondary GUID is " + secondaryGUID);
	}

	//@Test
	public void TestJoinCommand() throws NoSuchAlgorithmException,
	IOException {
		System.out.println("TestJoinCommand");
		InetSocketAddress primaryISA = primary.LocalEndpoints.get(1);
		InetSocketAddress secondaryISA = secondary.LocalEndpoints.get(0);
		
		ConnectionPoint cp1 = new ConnectionPoint(primaryISA);
		ConnectionPoint cp2 = new ConnectionPoint(secondaryISA);
		JoinServerCommand command = new JoinServerCommand(cp2, cp1);
		
		ConnectionPoint[] expecteds = new ConnectionPoint[] {};
		
		ArrayList<ConnectionPoint> actuals = new ArrayList<ConnectionPoint>();		
		for(ConnectionPoint cp : primary.getConnectionPoints()) {
			actuals.add(cp);
		}
		Assert.assertArrayEquals(expecteds, actuals.toArray());
		
		primary.ExecuteAndSend(command);
		
		expecteds = new ConnectionPoint[] {cp2};
		actuals = new ArrayList<ConnectionPoint>();		
		for(ConnectionPoint cp : primary.getConnectionPoints()) {
			actuals.add(cp);
		}
		Assert.assertArrayEquals(expecteds, actuals.toArray());	
	}
	
	@Test
	public void TestAddRemove() throws IOException {
		System.out.println("Add and GetAllContacts Command");
		
		IPhonebook phonebook = new RemotePhonebook (primary.LocalEndpoints
		        .getFirst());
		
		Object[] expecteds = new Contact[] {};
		List<Contact> actuals =  phonebook.GetAllContacts(); 
		Assert.assertArrayEquals(expecteds, actuals.toArray());
		
		Contact c1 = new Contact("Andreas", "60676776");
		Contact c2 = new Contact("Pelle", "12345678");
		phonebook.AddContact(c1);
		phonebook.AddContact(c2);
		
		c1.setConnectionPoint(primary.getIP());
		c2.setConnectionPoint(primary.getIP());
		expecteds = new Object[] {c2, c1};
		actuals =  phonebook.GetAllContacts();
		 
		Assert.assertEquals(expecteds[0].toString(), actuals.get(0).toString());
		Assert.assertEquals(expecteds[1].toString(), actuals.get(1).toString());
		
		System.out.println("Update and Lookup Command");
		phonebook.Update("Andreas", "87654321");
		
		String expected = "87654321";
		String actual = phonebook.Lookup("Andreas"); 
		 
		Assert.assertEquals(expected, actual);
		
		System.out.println("Remove Command");
		Assert.assertTrue(phonebook.Remove("Andreas"));
		
		
	}
	
	@After 
	public void AfterTest() {
		primary.abort ();
		secondary.abort();
	}
}