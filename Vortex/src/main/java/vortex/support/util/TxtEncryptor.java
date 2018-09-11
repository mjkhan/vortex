package vortex.support.util;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import vortex.support.AbstractObject;

public class TxtEncryptor extends AbstractObject {
	private static TxtEncryptor obj;
	
	public static TxtEncryptor get() {
		return ifEmpty(obj, () -> obj = new TxtEncryptor());
	}
	
	private String
		password,
		salt;
	
	private String password() {
		return ifEmpty(password, "password");
	}
	
	public TxtEncryptor setPassword(String password) {
		this.password = notEmpty(password, "password");
		return this;
	}
	
	private String salt() {
		return ifEmpty(salt, "5c0744940b5c369b");
	}
	
	public TxtEncryptor setSalt(String salt) {
		this.salt = notEmpty(salt, "salt");
		return this;
	}
	private TextEncryptor txtEnc;
	
	private TextEncryptor encryptor() {
		return ifEmpty(txtEnc, () -> txtEnc = Encryptors.queryableText(password(), salt()));
	}

	public String encrypt(String str) {
		return isEmpty(str) ? str : encryptor().encrypt(str);
	}
	
	public String decrypt(String str) {
		return isEmpty(str) ? str : encryptor().decrypt(str);
	}
/*
	public static void main(String[] args) {
		String str = "가나다";
		Encryptor enc = new Encryptor().setPassword("smartPform");
		String encoded = enc.encrypt(str);
		System.out.println(encoded);
		String decoded = enc.decrypt(encoded);
		System.out.println(decoded);
	}
*/
}