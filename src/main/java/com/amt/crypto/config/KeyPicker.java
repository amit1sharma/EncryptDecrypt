package com.amt.crypto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author yamraaj
 */
@Component
public class KeyPicker {

    @Value("${key.picker.type:prop}")
    private String keyPickerType;

    @Value("${encryption.key.name:encryption.key}")
    private String encryptionKeyName;

    @Autowired
    private Environment environment;

/*    public String getKeyPickerType() {
        return keyPickerType;
    }

    public void setKeyPickerType(String keyPickerType) {
        this.keyPickerType = keyPickerType;
    }*/

    public String getKeyValue() throws Exception {
        if("prop".equalsIgnoreCase(keyPickerType)){
            return environment.getProperty(encryptionKeyName);
        } else if("wss".equalsIgnoreCase(keyPickerType)){
            return null;
        } else {
            throw new Exception("Please set key.picker.type value in property file.");
        }
    }
}
