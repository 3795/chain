package com.cdqd.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Description: 加密工具
 * Created At 2020/2/9
 */
public class EncryptUtil {

    private final static Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    // RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;

    // RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * MD5加密
     *
     * @param value
     * @return
     */
    public static String md5(String value) {
        return DigestUtils.md5DigestAsHex(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 验证签名
     *
     * @param srcData   源数据
     * @param publicStr 公钥字符串
     * @param sign      签名
     * @return 验签是否通过
     * @throws Exception
     */
    public static boolean verify(String publicStr, String srcData, String sign) throws Exception {
        byte[] keyBytes = getPublicKey(publicStr).getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    /**
     * 对数据进行签名
     *
     * @param data       待签名的数据
     * @param privateStr 私钥字符串
     * @return 生成的数据签名
     * @throws Exception
     */
    public static String sign(String privateStr, String data) throws Exception {
        byte[] keyBytes = getPrivateKey(privateStr).getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 加密数据
     *
     * @param data      待加密数据
     * @param publicStr 公钥字符串
     * @return
     * @throws Exception
     */
    public static String encrypt(String publicStr, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicStr));
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 加密后的字符串
        return Base64.encodeBase64String(encryptedData);
    }


    /**
     * 对数据进行解密
     *
     * @param privateStr 私钥字符串
     * @param data       待解密数据
     * @return
     * @throws Exception
     */
    public static String decrypt(String privateStr, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateStr));
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * 获取公钥
     *
     * @param publicStr
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey(String publicStr) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicStr.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥
     *
     * @param privateStr
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(String privateStr) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeKey = Base64.decodeBase64(privateStr.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodeKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 生成秘钥对
     *
     * @return
     * @throws Exception
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }


    /**
     * 获取公钥
     *
     * @return
     */
    public static String getPublicKey(KeyPair keyPair) {
        return new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
    }

    /**
     * 获取私钥
     *
     * @return
     */
    public static String getPrivateKey(KeyPair keyPair) {
        return new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
    }
}
