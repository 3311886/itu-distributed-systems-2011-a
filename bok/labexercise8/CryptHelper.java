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

public class CryptHelper {

	/***
	 * Writes an array of bytes to a file
	 * @param message bytes to be written
	 * @param filename filename  
	 * @throws IOException
	 */
	public static void writeFile(byte[] message, String filename)
	throws IOException {
		byte[] output = message;
		FileOutputStream fos = new FileOutputStream(filename);
		fos.write(output);
		fos.flush();
		fos.close();

	}

/***
 * 
 * @param filename 
 * @return
 * @throws Exception 
 */
	public static byte[] readFile(String filename) throws Exception {
		
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		byte[] input =  new byte[(int)file.length()];	
		fis.read(input);
		return input;

	}

	/***
	 * Encrypts a String message using a key
	 * @param key
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] key, String message) throws Exception {
		// Create outputstream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CipherOutputStream cos = new CipherOutputStream(baos, getEncryptionKey(key));
		
		ObjectOutputStream oos = new ObjectOutputStream(cos);
		// Write
		oos.writeUTF(message);
		oos.flush();
		oos.close();
		return baos.toByteArray();
	}

	/***
	 * Descrypts a message using key 
	 * @param key
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(byte[] key,
		byte[] message) throws Exception {
		// Change cipher mode
		ByteArrayInputStream bais = new ByteArrayInputStream(message);
		CipherInputStream cis = new CipherInputStream(bais, getDecryptionKey(key));
		ObjectInputStream ois = new ObjectInputStream(cis);
		return ois.readUTF();
	}

	/***
	 * Returns a decryption key 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static Cipher getDecryptionKey(byte[] key) throws Exception{
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		// Create Cipher
		Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		desCipher.init(Cipher.DECRYPT_MODE, secretKey);
		return desCipher;
	}

	/***
	 * Returns an encryption key
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static Cipher getEncryptionKey(byte[] key) throws Exception {
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		// Create Cipher
		Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return desCipher;
	}
}