package com.example.loginapp;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.widget.Toast;

import java.security.KeyStore;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class EncryptDecrypt {

    private String KEY_ALIAS;
    private Context context;
    private byte[] iv;

    public EncryptDecrypt() {
    }
    public EncryptDecrypt(String KEY_ALIAS, Context context, byte[] iv) {
        this.KEY_ALIAS = KEY_ALIAS;
        this.context = context;
        this.iv = iv;
    }

    public String getKEY_ALIAS() {
        return KEY_ALIAS;
    }

    public void setKEY_ALIAS(String KEY_ALIAS) {
        this.KEY_ALIAS = KEY_ALIAS;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public boolean generateKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setRandomizedEncryptionRequired(true)
                        .build();

                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                keyGenerator.init(keyGenParameterSpec);
                keyGenerator.generateKey();
                Toast.makeText(context, "LLave nueva", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(context, "La llave ya existe", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (Exception ex) {
        }
        return false;
    }

    public SecretKey searchKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
            return  secretKey;

        } catch (Exception e) {

        }
        return null;
    }

    byte[] encryptData(String dataToEncrypt, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        iv = cipher.getIV();
        return cipher.doFinal(dataToEncrypt.getBytes());
    }

    public String descryptData(byte[] encryptedData, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] decryptedData = cipher.doFinal(encryptedData);
        String decryptedText = new String(decryptedData);
        return  decryptedText;
    }

    public void deleteKeys() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                keyStore.deleteEntry(alias);
            }

            Toast.makeText(context, "Todas las claves se eliminaron correctamente", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(context, "Error al eliminar las claves: " + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
