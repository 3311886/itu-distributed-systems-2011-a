package labexercise8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Cryptation
{
	private String password = "bdsp08-06";

	/***
	 * Takes a password for generating a key 
	 * @param password
	 */
	public Cryptation(String password) 
	{
		this.password = password;

	}

	/***
	 * Write a file to disk 
	 * @param message
	 * @param filename
	 * @throws IOException
	 */
	public void writeFile(byte[] message, String filename)
	throws IOException {
		byte[] output = message;
		FileOutputStream fos = new FileOutputStream(filename);
		fos.write(output);
		fos.flush();
		fos.close();
	}

	/***
	 * Read a file from disk
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public byte[] readFile(String filename) throws Exception {
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		byte[] input =  new byte[(int)file.length()];	
		fis.read(input);
		fis.close();
		return input;
	}

	/***
	 * Encrypt an object 
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public byte[] encrypt(Object o) throws Exception {
		// Create outputstream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CipherOutputStream cos = new CipherOutputStream(baos, getEncryptionKey());
		ObjectOutputStream oos = new ObjectOutputStream(cos);
		System.out.print("Encrypting... \n");
		// Write
		oos.writeObject(o);
		oos.flush();
		oos.close();
		System.out.println("The encrypted dog looks like this: " + baos.toString());
		System.out.println("The size of the encrypted Jeff is: " + baos.size()+ "\n");
		return baos.toByteArray();
	}

	/***
	 * Decrypt an object 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Object decrypt(Object object) throws Exception {
		
		System.out.println("The decrypted Jeff is received at server and is decrypted:");
		System.out.println("The received encrypted Jeff looks like this: " + object.toString() + "-and I do not get why it does not look the same as the encrypted dog sent from client????");
		System.out.println("Decrypting...");	
		byte[] objectForDecryption = (byte[]) object;
		System.out.println("Decrypted size: " + objectForDecryption.length + "\n\n\n");
		ByteArrayInputStream bais = new ByteArrayInputStream(objectForDecryption);
		CipherInputStream cis = new CipherInputStream(bais, getDecryptionKey());
		ObjectInputStream ois = new ObjectInputStream(cis);
		
		return ois.readObject();
	}


	/***
	 * Returns a Cipher based on the bytes key for decryption  
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Cipher getDecryptionKey(byte[] key) throws Exception{
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		// Create Cipher
		Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		desCipher.init(Cipher.DECRYPT_MODE, secretKey);
		return desCipher;
	}

	/***
	 * Returns a Cipher for decryption 
	 * @return
	 * @throws Exception
	 */
	public Cipher getDecryptionKey() throws Exception{
		DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		// Create Cipher
		Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		desCipher.init(Cipher.DECRYPT_MODE, secretKey);
		return desCipher;
	}

	/***
	 * Returns a Cipher based on the bytes of the key for encryption  
	 * @return
	 * @throws Exception
	 */
	public Cipher getEncryptionKey() throws Exception {
		DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		// Create Cipher
		Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return desCipher;
	}

	/***
	 * Returns a Cipher for encryption
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Cipher getEncryptionKey(byte[] key) throws Exception {
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		// Create Cipher
		Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return desCipher;
	}
}
