package bok.labexercise8;

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

public class SymmetricCrypt {
	private String password = "bdsp08-06";

	/***
	 * Takes a password for generating a key 
	 * @param password
	 */
	public SymmetricCrypt(String password) {
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
		//System.out.print("encrypting...");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CipherOutputStream cos = new CipherOutputStream(baos, getEncryptionKey());
		ObjectOutputStream oos = new ObjectOutputStream(cos);

		// Write
		oos.writeObject(o);
		oos.flush();
		oos.close();
		//System.out.println("Encrypted size: " +  baos.size());
		return baos.toByteArray();
	}

	/***
	 * Decrypt an object 
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public Object decrypt(Object o) throws Exception {
		//System.out.println("Decrypting");	
		byte[] b = (byte[]) o;
		
		System.out.println("Decrypted size: " + b.length);
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
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