package bok.labexercise4;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReplicatedPhonebookServerTestNaive {
	static MessageDigest MD5;
	private PhonebookServer primary;
	private PhonebookServer secondary;
	private PhonebookServer tertiary;

	private IPhonebook phonebook1;
	private IPhonebook phonebook2;
	private IPhonebook phonebook3;
	
	InetSocketAddress primaryISA;
	InetSocketAddress secondaryISA;
	InetSocketAddress tertiaryISA;		

	int port;

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

		System.out.println("Starting servers...");
		port = 10 + ((int) Math.random() * 10000); 
		primary = new PhonebookServer(0); 
		Thread serverThread = new Thread (primary);
		serverThread.start ();

		Guid primaryGUID = GuidFromString(primary.getIP().toString());
		String primaryHostName = primary.getIP().toString();
		phonebook1 = new RemotePhonebook(primary.getIP());

		System.out.println ("Primary hostname is " + primaryHostName);
		System.out.println ("Primary server GUID is " + primaryGUID);

		secondary = new PhonebookServer(0); 
		Thread serverThread2 = new Thread (secondary);
		serverThread2.start ();

		Guid secondaryGUID = GuidFromString(secondary.getIP().toString());
		String secondaryHostName = secondary.getIP().toString();
		phonebook2 = new RemotePhonebook(secondary.getIP());

		System.out.println ("secondary hostname is " + secondaryHostName);
		System.out.println ("secondary GUID is " + secondaryGUID);

		tertiary = new PhonebookServer(0); 
		Thread serverThread3 = new Thread (tertiary);
		serverThread3.start ();

		Guid tertiaryGUID = GuidFromString(tertiary.getIP().toString());
		String tertiaryHostName = tertiary.getIP().toString();
		phonebook3 = new RemotePhonebook(tertiary.getIP());

		System.out.println ("tertiary hostname is " + tertiaryHostName);
		System.out.println ("tertiary GUID is " + tertiaryGUID);
		
		primaryISA = primary.getIP();
		secondaryISA = secondary.getIP();
		tertiaryISA = tertiary.getIP();		

	}

	@After 
	public void AfterTest() {
		System.out.println("Exiting test...close servers");
		primary.abort();
		secondary.abort();
		tertiary.abort();
	}
	
	
	@Test
	public void TestAddSyncContactsBetween2Servers() throws IOException, NoSuchAlgorithmException, InterruptedException {

		// ensure no connection points are made
		Assert.assertEquals("[]", primary.getConnectionPoints().toString());
		
		RemotePhonebookServer remotePrimary = new  RemotePhonebookServer(primaryISA);
		remotePrimary.addConnectionPoint(new JoinServerCommand(secondaryISA, primaryISA));		
		
		Contact c =  new Contact("Andreas", "3958475");
		phonebook1.AddContact(c);
		Contact c2 =  new Contact("Pelle", "959656565");
		phonebook2.AddContact(c2);
		
		// ensure ip address's set up
		Assert.assertEquals("[" + secondary.getIP().toString()  +"]",  primary.getConnectionPoints().toString());
		Assert.assertEquals("["+ primary.getIP().toString() + "]", secondary.getConnectionPoints().toString());
		
		// contacts added should be visible at all servers
		Assert.assertEquals("3958475", phonebook2.Lookup("Andreas"));
		Assert.assertEquals("3958475", phonebook1.Lookup("Andreas"));
		Assert.assertEquals("959656565", phonebook2.Lookup("Pelle"));
		Assert.assertEquals("959656565", phonebook1.Lookup("Pelle"));
		
	}

	
	
	@Test
	public void TestAddSyncContactsBetween3Servers() throws IOException, NoSuchAlgorithmException, InterruptedException {

		// ensure no connection points are made
		Assert.assertEquals("[]", primary.getConnectionPoints().toString());

		RemotePhonebookServer remotePrimary = new  RemotePhonebookServer(primaryISA);
		remotePrimary.addConnectionPoint(new JoinServerCommand(secondaryISA, primaryISA));
		
		// lets have another remote phone book server
		RemotePhonebookServer remotePrimary2 = new  RemotePhonebookServer(secondaryISA);
		remotePrimary2.addConnectionPoint(new JoinServerCommand(tertiaryISA, secondaryISA));

		
		Contact c =  new Contact("Andreas", "3958475");
		phonebook1.AddContact(c);
		Contact c2 =  new Contact("Pelle", "959656565");
		phonebook2.AddContact(c2);
		Contact c3 =  new Contact("Stine", "767687606");
		phonebook3.AddContact(c3);
		
		
		// ensure ip address's set up
		Assert.assertEquals("[" + secondary.getIP().toString()  +"]",  primary.getConnectionPoints().toString());
		Assert.assertEquals("["+ primary.getIP().toString() + "]", secondary.getConnectionPoints().toString());
		Assert.assertEquals("["+ secondary.getIP().toString() + "]", tertiary.getConnectionPoints().toString());
		
		// contacts added should be visible at all servers
		Assert.assertEquals("3958475", phonebook1.Lookup("Andreas"));
		Assert.assertEquals("3958475", phonebook2.Lookup("Andreas"));
		Assert.assertEquals("3958475", phonebook3.Lookup("Andreas"));
		Assert.assertEquals("959656565", phonebook1.Lookup("Pelle"));
		Assert.assertEquals("959656565", phonebook2.Lookup("Pelle"));
		Assert.assertEquals("959656565", phonebook3.Lookup("Pelle"));
		Assert.assertEquals("767687606", phonebook1.Lookup("Stine"));
		Assert.assertEquals("767687606", phonebook2.Lookup("Stine"));
		Assert.assertEquals("767687606", phonebook3.Lookup("Stine"));
		
	}

	
	
}