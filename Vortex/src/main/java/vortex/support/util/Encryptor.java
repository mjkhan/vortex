package vortex.support.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import vortex.support.AbstractComponent;

public class Encryptor extends AbstractComponent {
	public static String newKey(String algorithm) {
		try {
			SecretKey key = KeyGenerator.getInstance(algorithm).generateKey();
			byte[] bytes = key.getEncoded();
			return Base64.getEncoder().encodeToString(bytes);
		} catch (Exception e) {
			throw runtimeException(e);
		}
	}
	
	private String
		algorithm,
		charset;
	private SecretKey key;
	private Cipher
		ecipher,
		dcipher;
	
	private String algorithm() {
		return ifEmpty(algorithm, "AES");
	}
	
	public Encryptor setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
		return this;
	}
	
	private String charset() {
		return ifEmpty(charset, "UTF-8");
	}
	
	public Encryptor setCharset(String charset) {
		this.charset = charset;
		return this;
	}
	
	private SecretKey key() {
		if (key != null) return key;
		
		setKey(newKey(algorithm()));
		return key;
	}
	
	public Encryptor setKey(String str) {
		key = new SecretKeySpec(str.getBytes(), algorithm());
		return this;
	}
	
	private Cipher ecipher() throws Exception {
		if (ecipher != null) return ecipher;
		
		ecipher = Cipher.getInstance(algorithm());
		ecipher.init(Cipher.ENCRYPT_MODE, key());
		return ecipher;
	}
	
	private Cipher dcipher() throws Exception {
		if (dcipher != null) return dcipher;
		
		dcipher = Cipher.getInstance(algorithm());
		dcipher.init(Cipher.DECRYPT_MODE, key());
		return dcipher;
	}
	
	public String encrypt(String str) {
		if (isEmpty(str)) return str;
		
		try {
			byte[] bytes = str.getBytes(charset());
			bytes = ecipher().doFinal(bytes);
			bytes = Base64.getEncoder().encode(bytes);
			return new String(bytes);
		} catch (Exception e) {
			throw runtimeException(e);
		}
	}
	
	public String decrypt(String str) {
		if (isEmpty(str)) return str;
		
		try {
			byte[] bytes = str.getBytes();
			bytes = Base64.getDecoder().decode(bytes);
			bytes = dcipher().doFinal(bytes);
			return new String(bytes, charset());
		} catch (Exception e) {
			throw runtimeException(e);
		}
	}
	
	public static void main(String[] args) {
		String algorithm = "AES",
			   key = Encryptor.newKey(algorithm);
		System.out.println("key: " + key);
		Encryptor enc = new Encryptor().setAlgorithm(algorithm).setKey(key);
		String str = "가나다";
		String encrypted = enc.encrypt(str);
		System.out.println("encrypted: " + encrypted);
		System.out.println("decrypted: " + enc.decrypt(encrypted));
	}
}