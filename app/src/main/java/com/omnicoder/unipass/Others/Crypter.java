package com.omnicoder.unipass.Others;

import android.os.Build;
import android.util.Log;

import com.omnicoder.unipass.BuildConfig;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypter {
    private static final String ENCRYPT_ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 256;
    private static final int IV_LENGTH_BYTE = 12;
    private static final int AES_KEY_BIT = 256;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;


    public  static byte[] getRandomNumber(int numBytes){
        byte[] randomNumber= new byte[numBytes];
        new SecureRandom().nextBytes(randomNumber);
        return randomNumber;
    }

    public static SecretKey getAESKeyFromPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 2000, 256);

        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

    }
    public static String encrypt(String password,String pText) throws Exception {

        byte[] salt= getRandomNumber(16);
        byte[] iv= getRandomNumber(16);
        SecretKey secretKey= getAESKeyFromPassword(password,salt);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        byte[] cipherText = cipher.doFinal(pText.getBytes());
        byte[] cipherTextWithIvSalt= ByteBuffer.allocate(salt.length+iv.length+cipherText.length).put(iv).put(salt).put(cipherText).array();
        if (Build.VERSION.SDK_INT <Build.VERSION_CODES.O) {
            return Arrays.toString(cipherTextWithIvSalt);

        }
        else {
            return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);

        }

    }
    public static String decrypt(String cText, String password) throws Exception{
        byte[] decode;
        if (Build.VERSION.SDK_INT <Build.VERSION_CODES.O) {
            decode=cText.getBytes();

        }
        else {
            decode= Base64.getDecoder().decode(cText.getBytes(UTF_8));

        }

//        decode= Base64.getDecoder().decode(cText.getBytes(UTF_8));
        ByteBuffer bb= ByteBuffer.wrap(decode);
        byte[] iv= new byte[16];
        bb.get(iv);
        byte[] salt= new byte[16];
        bb.get(salt);
        byte[] cipherText= new byte[bb.remaining()];
        bb.get(cipherText);
        SecretKey secretKey= getAESKeyFromPassword(password,salt);
        Cipher cipher= Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        byte[] plainText= cipher.doFinal(cipherText);
        return new String(plainText,UTF_8);

    }
//    private static String Decrypt(String cText, String password) throws InvalidKeyException,Exception{
//
//        byte[] decode = Base64.getDecoder().decode(cText.getBytes(UTF_8));
//
//        // get back the iv and salt from the cipher text
//        ByteBuffer bb = ByteBuffer.wrap(decode);
//
//        byte[] iv = new byte[16];
//        bb.get(iv);
//
//        byte[] salt = new byte[16];
//        bb.get(salt);
//
//        byte[] cipherText = new byte[bb.remaining()];
//        bb.get(cipherText);
//
//        // get back the aes key from the same password and salt
//        SecretKey aesKeyFromPassword = getAESKeyFromPassword(password, salt);
//
//        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
//
//        cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
//
//        byte[] plainText = cipher.doFinal(cipherText);
//
//        return new String(plainText, UTF_8);
//
//    }

    public static byte[] encryptFile(byte[] pText, String password) throws Exception {
        byte[] salt= getRandomNumber(16);
        byte[] iv= getRandomNumber(16);
        SecretKey secretKey= getAESKeyFromPassword(password,salt);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        byte[] cipherText = cipher.doFinal(pText);
        byte[] cipherTextWithIvSalt;

        cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array();

        return cipherTextWithIvSalt;

    }


    public static byte[] decryptFile(byte[] cText, String password) throws Exception {

        ByteBuffer bb= ByteBuffer.wrap(cText);
        byte[] iv= new byte[16];
        bb.get(iv);
        byte[] salt= new byte[16];
        bb.get(salt);
        byte[] cipherText= new byte[bb.remaining()];
        bb.get(cipherText);
        SecretKey secretKey= getAESKeyFromPassword(password,salt);
        Cipher cipher= Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        byte[] plainText;
        plainText= cipher.doFinal(cipherText);
        return plainText;
    }







}
