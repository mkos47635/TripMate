package com.trip.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

@Component
public class EncryptionUtil {
	
	@Value("${share.link.key}")
    private String shareSecretKey; // 16바이트 고정 키

    public String encrypt(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(shareSecretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getUrlEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8")));
    }

    public String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(shareSecretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getUrlDecoder().decode(encryptedData)), "UTF-8");
    }
    public String decode(String data) throws Exception{
    	return URLDecoder.decode(data, "UTF-8");
    }
    public String encode(String data) throws Exception{
    	return URLEncoder.encode(data, "UTF-8");
    }
}
