package bok.labexercise8;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

public class SymmetricCryptTest {
	private static final String password = "bdsp08-06";
	private static final String plaintext = "The quick brown fox jumps over the lazy dog.";
	private SymmetricCrypt crypt = new SymmetricCrypt(password);
	

	@Test
	/***
	 * Static implementation of cryptation and encryption methods 
	 */
	public void TestCryptHelper() throws Exception {
		// Create Key
		byte key[] = password.getBytes();
		CryptHelper.writeFile( CryptHelper.encrypt(key, plaintext), "output.txt");
		CryptHelper.writeFile(plaintext.getBytes(), "unsecure.txt");

		String decrypted = CryptHelper.decrypt(key, CryptHelper.readFile("output.txt"));
		// Compare
		
		Assert.assertEquals(plaintext, decrypted);
		
		Integer i = 5;
		
	}
	
	@Test
	public void TestSymmetricCrypt() throws Exception {
		Person p1 = new Person("Andreas", "Nygade", 2300, "60676776");
		Object enc = crypt.encrypt(p1);
		Person p2  = (Person) crypt.decrypt(enc);
		Assert.assertEquals(p1, p2);
		
		ArrayList<String> myList = new ArrayList<String>();
		myList.add("Pelle is cool");
		myList.add("Stine is funny");
		myList.add("Andreas is laughing");
		
		Object enc2 =  crypt.encrypt(myList);
		ArrayList<String> dec2 =  (ArrayList<String>) crypt.decrypt(enc2);
		Assert.assertEquals(myList, dec2);
	}
}
