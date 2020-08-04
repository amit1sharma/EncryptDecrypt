package com.amt.crypto.util;

import com.ibm.wsspi.security.auth.callback.WSMappingCallbackHandlerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

public class Utility {

    public static String getDecryptedString(String encryptedString, String passkey) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        ByteBuffer byteBuffer = (new BASE64Decoder()).decodeBufferToByteBuffer(encryptedString);
        byte[] key = passkey.getBytes();
        int ivLength = byteBuffer.getInt();
        if (ivLength < 12 || ivLength >= 16) {
            throw new IllegalArgumentException("invalid iv length");
        } else {
            byte[] iv = new byte[ivLength];
            byteBuffer.get(iv);
            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(2, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText);
        }
    }

    public static String getEncryptedString(String textToEncrypt, String passkey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = passkey.getBytes();
        javax.crypto.SecretKey secretKey = new SecretKeySpec(key, "AES");
        byte[] iv = new byte[12];
        secureRandom.nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(1, secretKey, parameterSpec);
        byte[] cipherText = cipher.doFinal(textToEncrypt.getBytes());
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherText.length);
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);
        byte[] cipherMessage = byteBuffer.array();
        return (new BASE64Encoder()).encode(cipherMessage);
    }

    private String getCredentials(String alias) throws Exception {
        HashMap map = new HashMap();
        map.put("com.ibm.mapping.authDataAlias", alias);
        try {
            CallbackHandler callbackHandler = WSMappingCallbackHandlerFactory.getInstance().getCallbackHandler(map, null);
            LoginContext lc;
            try {
                lc = new LoginContext("DefaultPrincipalMapping", callbackHandler);
                lc.login();
                javax.security.auth.Subject subject = lc.getSubject();
                java.util.Set creds = subject.getPrivateCredentials();
                PasswordCredential result = (javax.resource.spi.security.PasswordCredential) creds.toArray()[0];

                return new String(result.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            throw e1;
        }
    }
}
