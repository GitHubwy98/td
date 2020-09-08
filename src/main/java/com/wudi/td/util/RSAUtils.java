package com.wudi.td.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {
    /**
     * 密钥算法
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 密钥位数
     */
    private static int keySize = 1024;

    /**
     * 初始化密钥
     *
     * @return 密钥
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, Object> initKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(keySize);
        //生成密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put(PRIVATE_KEY, privateKey);
        keyMap.put(PUBLIC_KEY, publicKey);
        return keyMap;
    }

    public static void main(String[] args) {
        try {
            Map<String, Object> map = RSAUtils.initKey();
            String privateKeyStr = RSAUtils.getPrivateKeyStr(map);
            String publicKeyStr = RSAUtils.getPublicKeyStr(map);
            System.out.println(privateKeyStr);
            System.out.println("------------");
            System.out.println(publicKeyStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取公钥字符串
     *
     * @param keyMap 密钥对
     * @return 公钥字符串
     */
    public static String getPublicKeyStr(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 获取私钥字符串
     *
     * @param keyMap 密钥对
     * @return 私钥字符串
     */
    public static String getPrivateKeyStr(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * byte 数组 返回字符串
     *
     * @param key 公钥
     * @return 公钥字符串
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.encodeBase64String(key);
    }

    /**
     * 获取公钥
     *
     * @param publicKeyStr 公钥字符串
     * @return 公钥
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKeyStr) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKeyStr);
        //返回密钥的字节。
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //设置密钥算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(encodedKeySpec);
        return publicKey;
    }

    /**
     * 获取私钥
     *
     * @param privateKeyStr 私钥字符串
     * @return 私钥
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKeyStr) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * byte 数组 返回字符串
     *
     * @param key 私钥
     * @return 私钥字符串
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decodeBase64(key);
    }

    /**
     * 获取签名
     *
     * @param data          数据
     * @param privateKeyStr 私钥字符串
     * @return 签名byte数组
     * @throws Exception
     */
    public static byte[] sign(byte[] data, String privateKeyStr) throws Exception {
        PrivateKey priK = getPrivateKey(privateKeyStr);
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initSign(priK);
        sig.update(data);
        return sig.sign();
    }

    /**
     * 验证
     *
     * @param data         数据
     * @param sign         签名byte数组
     * @param publicKeyStr 公钥字符串
     * @return 验证是否通过
     * @throws Exception
     */
    public static boolean verify(byte[] data, byte[] sign, String publicKeyStr) throws Exception {
        PublicKey pubK = getPublicKey(publicKeyStr);
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initVerify(pubK);
        sig.update(data);
        return sig.verify(sign);
    }

    /**
     * 数据分段
     *
     * @param data
     * @param cipher
     * @return
     * @throws Exception
     */
    public static byte[] blockEncryption(byte[] data, Cipher cipher) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        byte[] bytes = blockEncryption(encryptedData, cipher);
        return bytes;
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        byte[] bytes = blockEncryption(encryptedData, cipher);
        return bytes;
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        byte[] bytes = blockEncryption(data, cipher);
        return bytes;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        byte[] bytes = blockEncryption(data, cipher);
        return bytes;
    }

    /**
     * java端公钥加密
     */
    public static String encryptedDatauPublicKeyOnJava(String data, String PUBLICKEY) throws Exception {
        data = Base64.encodeBase64String(encryptByPublicKey(data.getBytes(), PUBLICKEY));
        return data;
    }

    /**
     * java端私钥加密
     */
    public static String encryptedDataPrivateKeyOnJava(String data, String PRIVATEKEY) throws Exception {
        data = Base64.encodeBase64String(encryptByPrivateKey(data.getBytes(), PRIVATEKEY));
        return data;
    }

    /**
     * java端私钥解密
     */
    public static String decryptDataPrivateKeyOnJava(String data, String PRIVATEKEY) throws Exception {
        String temp = "";
        byte[] rs = Base64.decodeBase64(data);
        temp = new String(RSAUtils.decryptByPrivateKey(rs, PRIVATEKEY), "UTF-8");
        return temp;
    }

    /**
     * java端公钥解密
     */
    public static String decryptDataPublicKeyOnJava(String data, String PRIVATEKEY) throws Exception {
        String temp = "";
        byte[] rs = Base64.decodeBase64(data);
        temp = new String(RSAUtils.decryptByPublicKey(rs, PRIVATEKEY), "UTF-8");
        return temp;
    }
}
