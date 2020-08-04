package com.amt.crypto;

import com.amt.crypto.config.KeyPicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Use this file to read encryption/decryption key to read from properties file.
 */
@Component
public class SpringAESGCMCrypto {

    @Autowired
    private KeyPicker keyPicker;

    private AESGCMCrypto crypto = new AESGCMCrypto();

/*    public String encrypt(String textToEncrypt, String passKey) throws Exception {
        return Utility.getEncryptedString(textToEncrypt, passKey);
    }

    public String decrypt(String encryptedString, String passKey) throws Exception {
        return Utility.getDecryptedString(encryptedString, passKey);
    }*/

    public String encrypt(String textToEncrypt) throws Exception {
        String passKey = keyPicker.getKeyValue();
        return crypto.encrypt(textToEncrypt,passKey);
    }

    public String decrypt(String encryptedString) throws Exception {
        String passKey = keyPicker.getKeyValue();
        return crypto.decrypt(encryptedString, passKey);
    }

/*    public static void main(String[] args) {
        try {
            String textToEncrypt = args[0];
            AESGCMCrypto obj = new AESGCMCrypto();
            System.out.println(obj.encrypt(textToEncrypt, "EMPOWERHASH_ADCB"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
