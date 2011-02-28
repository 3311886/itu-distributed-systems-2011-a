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
	private PhonebookServer primary;
	private PhonebookServer secondary;
	
	private IPhonebook phonebook1;
	private IPhonebook phonebook2;
	
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
        primary = new PhonebookServer(); 
        Thread serverThread = new Thread (primary);
        
        serverThread.start ();

        Guid primaryGUID = GuidFromString(primary.LocalEndpoints.get(1).toString());
        String primaryHostName = primary.LocalEndpoints.get(1).getHostName();
        phonebook1 = new RemotePhonebook(primary.LocalEndpoints.get(1));
        
        System.out.println ("Primary hostname is " + primaryHostName);
        System.out.println ("Primary server GUID is " + primaryGUID);

        secondary = new PhonebookServer(); 
        Thread serverThread2 = new Thread (secondary);
        serverThread2.start ();

        Guid secondaryGUID = GuidFromString(secondary.LocalEndpoints.get(0).toString());
        String secondaryHostName = secondary.LocalEndpoints.get(0).getHostName();
        phonebook2 =new RemotePhonebook(primary.LocalEndpoints.get(0));
        
        System.out.println ("Secondary hostname is " + secondaryHostName);
        System.out.println ("Secondary GUID is " + secondaryGUID);
        	}
	
	@Test
	public void TestJoinCommand() throws NoSuchAlgorithmException,
	IOException {
	
		System.out.println("TestJoinCommand");
		InetSocketAddress primaryISA = primary.getIP();
		InetSocketAddress secondaryISA = secondary.getIP();
		
		JoinServerCommand command = new JoinServerCommand(secondaryISA, primaryISA);
		
		InetSocketAddress[] expecteds = new InetSocketAddress[] {};
		
		ArrayList<InetSocketAddress> actuals = new ArrayList<InetSocketAddress>();		
		for(InetSocketAddress isa : primary.getConnectionPoints()) {
			actuals.add(isa);
		}
		Assert.assertArrayEquals(expecteds, actuals.toArray());
		
		// Execute directly on the main server 
		//primaryremote.addConnectionPoint(secondaryISA);
		primary.ExecuteAndSend(command);
		
		expecteds = new InetSocketAddress[] {secondaryISA};
		actuals = new ArrayList<InetSocketAddress>();		
		for(InetSocketAddress isa : primary.getConnectionPoints()) {
			actuals.add(isa);
		}
		Assert.assertArrayEquals(expecteds, actuals.toArray());
		
		//Assert.assertArrayEquals(expecteds, actuals.toArray());
		
		System.out.print("Testing adding new contact on replicated servers");
		Assert.assertEquals("OK", phonebook1.AddContact(new Contact("Contact 1", "123345")).toString());
		Assert.assertEquals("123345", phonebook2.Lookup("Contact 1"));
				
		System.out.print("Testing that the serverip that created the contact is persisted across replication");
		Assert.assertEquals(primaryISA, phonebook1.GetAllContacts().get(0).getConnectionPoint()); 
		Assert.assertEquals(primaryISA, phonebook2.GetAllContacts().get(0).getConnectionPoint());
		
		System.out.print("Testing updating contact on replicated servers");
		phonebook2.Update("Contact 1", "34234");
		Assert.assertEquals("34234", phonebook1.Lookup("Contact 1"));
				
		primary.removeConnectionPoint(new RemoveServerCommand(secondaryISA,primaryISA));
		Assert.assertEquals(0, primary.getConnectionPoints().size());
		
		System.out.print("Remove contact from a server");
		phonebook2.Remove("Contact 1");
		Assert.assertNull(phonebook1.Lookup("Contact 1"));	
	}
	
	// TODO: disabled because we can't run two tests cases within a test class so fare with the server approach we try to simulate
//	@Test
	public void TestAddRemove() throws IOException, NoSuchAlgorithmException {
		
		System.out.println("Add and GetAllContacts Command");
		
		Object[] expecteds = new Contact[] {};
		List<Contact> actuals =  phonebook1.GetAllContacts(); 
		Assert.assertArrayEquals(expecteds, actuals.toArray());
		
		Contact c1 = new Contact("Andreas", "60676776");
		Contact c2 = new Contact("Pelle", "12345678");
		phonebook1.AddContact(c1);
		phonebook1.AddContact(c2);
		
		c1.setConnectionPoint(primary.getIP());
		c2.setConnectionPoint(primary.getIP());
		
		expecteds = new Object[] {c2, c1};
		actuals =  phonebook1.GetAllContacts();
		 
		Assert.assertEquals(expecteds[0].toString(), actuals.get(0).toString());
		Assert.assertEquals(expecteds[1].toString(), actuals.get(1).toString());
		
		System.out.println("Update and Lookup Command");
		phonebook1.Update("Andreas", "87654321");
		
		String expected = "87654321";
		String actual = phonebook1.Lookup("Andreas"); 
		 
		Assert.assertEquals(expected, actual);
		
		System.out.println("Remove Command");
		Assert.assertTrue(phonebook1.Remove("Andreas"));
	}
	
	@After 
	public void AfterTest() {
		primary.abort ();
		secondary.abort();
	} 
}