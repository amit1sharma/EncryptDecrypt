package com.amt.crypto;

import com.amt.crypto.util.Utility;


public class AESGCMCrypto {

    public AESGCMCrypto() {
    }

    public String encrypt(String textToEncrypt, String passkey)
            throws Exception {
        return Utility.getEncryptedString(textToEncrypt, passkey);
    }

    public String decrypt(String encryptedString, String passkey)
            throws Exception {
        return Utility.getDecryptedString(encryptedString, passkey);
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