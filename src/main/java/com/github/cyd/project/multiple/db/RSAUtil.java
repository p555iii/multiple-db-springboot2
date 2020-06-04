package com.github.cyd.project.multiple.db;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {
    final static String PUBLIC_KEY_STR="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAOGB1BRSBBBhWYJe6cZK+aa5ePl09XGw4ERkLKSavj9e67brDJ+Ug0nARxZyoR7rQJG3Zjy85IavIkIxT2dw1NMCAwEAAQ==";
    final static String PRIVATE_KEY_STR="MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEA4YHUFFIEEGFZgl7pxkr5prl4+XT1cbDgRGQspJq+P17rtusMn5SDScBHFnKhHutAkbdmPLzkhq8iQjFPZ3DU0wIDAQABAj8O+ZAbyVZ7v/sxrRvZDyGE8MwXxKyH4DcEU6KjS6NWkdckLNH8mUHu42eVx9U9xDBdqtSK7HNmGa/Q0WNpMYkCIQD7GaKmXHRAVxa1Px9C665dxz8HsECFoRKOqg6ymONVHQIhAOXoWAl5DYY7wpnFej/AzP7qwLW9XloKfStyatWYnV6vAiBKtIPJjvQfOEnPPqpkVI4Z5G+CMBPiLdvBiiK5S/dGmQIhALEwQXGDj03ODuvOS6qI4DTzHfSzbt08Sj3K39Wg2mBXAiAlrdVerK1M0CPpeecWCqhLlgE1MWLAin3EDqhUtJ9r4A==";
    //生成秘钥对
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    //获取公钥(Base64编码)
    public static String getPublicKey(KeyPair keyPair){
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return byte2Base64(bytes);
    }

    //获取私钥(Base64编码)
    public static String getPrivateKey(KeyPair keyPair){
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return byte2Base64(bytes);
    }

    //将Base64编码后的公钥转换成PublicKey对象
    public static PublicKey string2PublicKey(String pubStr) throws Exception{
        byte[] keyBytes = base642Byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    //将Base64编码后的私钥转换成PrivateKey对象
    public static PrivateKey string2PrivateKey(String priStr) throws Exception{
        byte[] keyBytes = base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    //公钥加密
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    //私钥解密
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    //字节数组转Base64编码
    public static String byte2Base64(byte[] bytes){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    //Base64编码转字节数组
    public static byte[] base642Byte(String base64Key) throws IOException{
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Key);
    }


    /**
     * rsa 加密
     * @param str
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    public static String encode(String str,String publicKeyStr) throws Exception{
        return RSAUtil.byte2Base64(RSAUtil.publicEncrypt(str.getBytes(), RSAUtil.string2PublicKey(publicKeyStr)));
    }
    /**
     * rsa 加密
     * @param str
     * @return
     * @throws Exception
     */
    public static String encode(String str) throws Exception{
        return encode(str,PUBLIC_KEY_STR);
    }

    /**
     * rsa 解密
     * @param rsaStr
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static String decode(String rsaStr,String privateKeyStr) throws Exception{
        return new String(RSAUtil.privateDecrypt(RSAUtil.base642Byte(rsaStr), RSAUtil.string2PrivateKey(privateKeyStr)));
    }
    /**
     * rsa 解密
     * @param rsaStr
     * @return
     * @throws Exception
     */
    public static String decode(String rsaStr) throws Exception{
        return decode(rsaStr,PRIVATE_KEY_STR);
    }

    public static void main(String[] args) throws Exception{
        String password="cyd123456";
        String rsaPassword = encode(password);
        rsaPassword = rsaPassword.replaceAll("(\\r\\n|\\r|\\n|\\n\\r)","");
        System.out.println("加密后密码:"+rsaPassword.trim());
        String decodePwd = decode("hNnydbZe3XvJx2E6/lVc2QKVGpJC0rsbs46mAqI1km9e5G32e/ojhrngVeT/cKypUUCkqIk2XIBs96z/0nAXYQ==");
        System.out.println("解密后密码:"+decodePwd);
    }

}
