package com.cdd.util;

import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import android.util.Log;

public class CryptAESNew {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private CryptAESNew aes = null;

	private SecretKeySpec sKey;

	private final static String key = "yaoyaole123123";

	private byte[] iv = { 0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC,
			0xD, 91 };

	private Cipher eCipher, dCipher;

	public CryptAESNew() {
		String secretkey = key;
		byte[] bytes = new byte[128 / 8];
		byte[] keys = null;

		try {

			keys = secretkey.getBytes("UTF-8");

			for (int i = 0; i < secretkey.length(); i++) {
				if (i >= bytes.length)
					break;
				bytes[i] = keys[i];
			}
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			sKey = new SecretKeySpec(bytes, "AES");
			eCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			eCipher.init(Cipher.ENCRYPT_MODE, sKey, ivSpec);
			dCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			dCipher.init(Cipher.DECRYPT_MODE, sKey, ivSpec);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

/*	public static CryptAESNew getInstance() {
		if (aes == null) {
			aes = new CryptAESNew(key);
		}
		return aes;
	}*/

	private byte[] encrypt(byte[] data) throws IllegalBlockSizeException,
			BadPaddingException {
		return eCipher.doFinal(data);
	}

	private byte[] decrypt(byte[] encData) throws IllegalBlockSizeException,
			BadPaddingException {
		return dCipher.doFinal(encData);

	}

	public String onEncrypt(String strData) {
		try {
			byte[] bytes = encrypt(strData.getBytes("UTF-8"));
			return Base64Encoder.encode(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String onDecrypt(String strData) {
		try {
			byte[] bytes = Base64Decoder.decodeToBytes(strData);
			String data = new String(decrypt(bytes));
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void main(String[] args) {
		CryptAESNew hahahah123 = new CryptAESNew();
		try {
			String msg = "{\"re\":[[\"1\",\"摇摇\"]}";
			String base64_ciphertext = hahahah123.onEncrypt(msg);
			String mm = hahahah123.onDecrypt(base64_ciphertext);
			if (CddConfig.IS_DEBUG) {
				Log.i("CDD", base64_ciphertext);
				Log.i("CDD", mm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
