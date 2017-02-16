package com.chas.security.rsa;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Chas Zeng
 * @email 894555763@qq.com
 * @date 2017/2/15
 */
public class RSA {

    //指定加密算法为RSA
    private static final String ALGORITHM = "RSA";

    //签名算法
    private static final String SIGN_ALGORITHMS = "MD5withRSA";

    //字符编码格式
    private static final String CODE_FORMAT = "UTF-8";

    //密钥长度，用来初始化
    private static final int KEY_SIZE = 512;

    /**
     * 获取密钥对
     *
     * @return 密钥和私钥（密钥键值：privateKey，公钥键值：publicKey）
     */
    public static Map<String, String> getKeyPair(){
        Map<String, String> keyPairMap = new Hashtable<String, String>();
        try {
            //1.初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            //2.设置密钥长度(必须为64的倍数)
            keyPairGenerator.initialize(KEY_SIZE);
            //3.生成密钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            //得到公钥
            RSAPublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
            //得到私钥
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();

            String publicKey = Base64.encode(rsaPublicKey.getEncoded());
            String privateKey = Base64.encode(rsaPrivateKey.getEncoded());

            keyPairMap.put("publicKey", publicKey);
            keyPairMap.put("privateKey", privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyPairMap;
    }

    /**
     * RSA私钥签名
     *
     * @param content 待签名数据
     * @param privateKeyStr 私钥
     * @return 私钥签名后的字符
     */
    public static String doSign(String content, String privateKeyStr){
        String result = null;
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(privateKey);
            signature.update(content.getBytes());
            byte[] sign = signature.sign();
            result = Base64.encode(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * RSA公钥验证签名
     *
     * @param content 签名数据
     * @param publicKeyStr 公钥
     * @param sign 签名后的数据
     * @return true：验证成功(密钥匹配)，false：验证失败(密钥不匹配)
     */
    public static Boolean checkSign(String content, String publicKeyStr, String sign){
        Boolean result = false;
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(publicKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(publicKey);
            signature.update(content.getBytes());
            result = signature.verify(Base64.decode(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 私钥加密
     *
     * @param privateKeyStr 私钥
     * @param content 需要加密的字符
     * @return 加密后的字符
     */
    public static String privateEncrypt(String privateKeyStr, String content){
        return getRSAStr(privateKeyStr,content,KeyEnum.PRIVATE, Type.ENCRYPT);
    }

    /**
     * 公钥解密
     *
     * @param publicKeyStr 公钥
     * @param encryptStr 私钥加密后的字符
     * @return 解密后的字符
     */
    public static String publicDecrypt(String publicKeyStr, String encryptStr){
        return getRSAStr(publicKeyStr,encryptStr,KeyEnum.PUBLIC,Type.DECRYPT);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyStr 公钥
     * @param content 需要加密的字符
     * @return 加密后的字符
     */
    public static String publicEncrypt(String publicKeyStr, String content){
        return getRSAStr(publicKeyStr,content,KeyEnum.PUBLIC,Type.ENCRYPT);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyStr 私钥
     * @param encryptStr 公钥加密后的字符
     * @return 解密后的字符
     */
    public static String privateDecrypt(String privateKeyStr, String encryptStr){
        return getRSAStr(privateKeyStr,encryptStr,KeyEnum.PRIVATE,Type.DECRYPT);
    }


    /**
     * 获取公钥(私钥)加密(解密) 或 私钥(公钥)加密(解密) 后的字符信息
     *
     * @param keyStr 公钥或私钥
     * @param str 待加密的信息或 加密后的信息
     * @param keyEnum public 或 private
     * @param type 加密或解密
     * @return
     */
    private static String getRSAStr(String keyStr, String str, KeyEnum keyEnum, Type type){
        String result = null;
        try {
            if (KeyEnum.PUBLIC == keyEnum) {//公钥
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(keyStr));
                PublicKey publicKey = KeyFactory.getInstance(ALGORITHM).generatePublic(x509EncodedKeySpec);
                result = getBytes(str,type,publicKey);
            } else {//私钥
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(keyStr));
                PrivateKey privateKey = KeyFactory.getInstance(ALGORITHM).generatePrivate(pkcs8EncodedKeySpec);
                result = getBytes(str,type,privateKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取加密或者解密后的信息
     *
     * @param str 待加密或加密后的自信
     * @param type 加密或解密枚举
     * @param key publicKey 或 privateKey
     * @return
     * @throws Exception
     */
    private static String getBytes(String str, Type type, Key key) throws Exception{
        String result;
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        if (Type.ENCRYPT == type) {//加密
            cipher.init(Cipher.ENCRYPT_MODE,key);
            result = Base64.encode(cipher.doFinal(str.getBytes()));
        } else {//解密
            cipher.init(Cipher.DECRYPT_MODE,key);
            result = new String(cipher.doFinal(Base64.decode(str)));
        }
        return result;
    }
}
