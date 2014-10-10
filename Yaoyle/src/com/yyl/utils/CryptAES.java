package com.yyl.utils;

import java.nio.charset.Charset;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


import android.util.Log;

public class CryptAES {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static CryptAES aes = null;

	private static SecretKeySpec sKey;

	private final static String key = "ydsw123321";

	private byte[] iv = { 0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC,
			0xD, 91 };

	private Cipher eCipher, dCipher;

	private CryptAES(String secretkey) {

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

	public static CryptAES getInstance() {
		if (aes == null) {
			aes = new CryptAES(key);
		}
		return aes;
	}

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
			String data = new String(decrypt(bytes), Charset.forName("UTF-8"));
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void main(String[] args) {
		CryptAES hahahah123 = CryptAES.getInstance();
		try {
			String msg = "{\"re\":[[\"1\",\"摇摇\"]}";
			String base64_ciphertext = hahahah123.onEncrypt(msg);
			String mm = hahahah123.onDecrypt(base64_ciphertext);
			if (YylConfig.IS_DEBUG) {
				Log.i("YYL", base64_ciphertext);
				Log.i("YYL", mm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
