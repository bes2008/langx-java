package com.jn.langx.security.ext.cryptojs;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.CipherAlgorithmPadding;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.cipher.pb.SymmetricPBCipher;
import com.jn.langx.security.crypto.key.pb.EvpKDF;
import com.jn.langx.security.crypto.key.pb.KDF;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.reflect.Reflects;

public class CryptoJS {

    public static class SymmetricConfig{

        public int keyBitSize;
        public int ivBitSize;
        public int saltBitSize;
        public String hashAlgorithm;
        public int iterations;
        public Class kdfClass;


        public String cryptAlgorithm;
        public Symmetrics.MODE mode;
        public CipherAlgorithmPadding padding;

        public SymmetricConfig(int saltBitSize,
                               int keyBitSize,
                               int ivBitSize,
                               String hashAlgorithm,
                               int iterations,
                               Class<? extends KDF> kdfClass,
                               String cryptoAlgorithm,
                               Symmetrics.MODE mode,
                               CipherAlgorithmPadding padding){
            this.saltBitSize= saltBitSize;
            this.keyBitSize=keyBitSize;
            this.ivBitSize=ivBitSize;
            this.hashAlgorithm=hashAlgorithm;
            this.iterations=iterations;
            this.kdfClass=kdfClass;

            this.cryptAlgorithm= cryptoAlgorithm;
            this.mode=mode;
            this.padding=padding;

        }
    }

    public static class AESConfig extends SymmetricConfig{
        public AESConfig(){
            this(64, 256, 128, JCAEStandardName.MD5.getName(), 1, EvpKDF.class, Symmetrics.MODE.CBC, CipherAlgorithmPadding.PKCS5Padding);
        }
        public AESConfig(int saltBitSize,
                         int keyBitSize,
                         int ivBitSize,
                         String hashAlgorithm,
                         int iterations,
                         Class<? extends KDF> kdfClass,
                         Symmetrics.MODE mode,
                         CipherAlgorithmPadding padding){
            super(saltBitSize,keyBitSize,ivBitSize,hashAlgorithm,iterations,kdfClass, JCAEStandardName.AES.getName(), mode, padding);
        }
    }

    public static class AES{
        private static String SALT_PREFIX="Salted__";
        public static String encrypt(String message, String passphrase, CryptoJS.AESConfig cfg){
            if (cfg==null){
                cfg= new CryptoJS.AESConfig();
            }
            SymmetricPBCipher cipher = new SymmetricPBCipher(
                    passphrase,
                    cfg.saltBitSize,
                    cfg.keyBitSize,
                    cfg.ivBitSize,
                    cfg.hashAlgorithm,
                    cfg.iterations,
                    cfg.cryptAlgorithm,
                    cfg.mode,
                    cfg.padding);
            KDF kdf = Reflects.<KDF>newInstance(cfg.kdfClass);
            cipher.setKdf(kdf);
            byte[] encryptedBytes= cipher.encrypt(Strings.getBytesUtf8(message));
            byte[] salt = cipher.getSalt();

            byte[] resultBytes;
            if(Objs.isNull(salt)){
                resultBytes=encryptedBytes;
            }else{
                byte[] saltPrefix=SALT_PREFIX.getBytes(Charsets.UTF_8);
                resultBytes =new byte[saltPrefix.length + salt.length+ encryptedBytes.length];

                System.arraycopy(saltPrefix, 0, resultBytes, 0, saltPrefix.length);
                System.arraycopy(salt, 0, resultBytes, saltPrefix.length, salt.length);
                System.arraycopy(encryptedBytes, 0, resultBytes, saltPrefix.length+salt.length, encryptedBytes.length);
            }
            String result = Base64.encodeBase64String(resultBytes);
            return result;
        }

        public static String decrypt(String encryptedText, String passphrase, CryptoJS.AESConfig cfg){
            if (cfg==null){
                cfg= new CryptoJS.AESConfig();
            }

            boolean hasSalt=Base64.decodeBase64ToString(encryptedText).startsWith(SALT_PREFIX);
            if(!hasSalt){
                cfg.saltBitSize=0;
            }

            byte[] saltAndEncryptedBytes= Base64.decodeBase64(encryptedText);

            byte[] salt;
            byte[] encryptedBytes;
            if(!hasSalt){
                salt = Emptys.EMPTY_BYTES;
                encryptedBytes = saltAndEncryptedBytes;
            }else{
                int saltBytesLength=Securitys.getBytesLength(cfg.saltBitSize);
                salt = new byte[saltBytesLength];
                System.arraycopy(saltAndEncryptedBytes, SALT_PREFIX.getBytes(Charsets.UTF_8).length, salt, 0, saltBytesLength);

                int startOffsetOfEncryptedBytes=SALT_PREFIX.getBytes(Charsets.UTF_8).length+ saltBytesLength;
                int encryptedBytesLength=saltAndEncryptedBytes.length-startOffsetOfEncryptedBytes;
                encryptedBytes= new byte[encryptedBytesLength];
                System.arraycopy(saltAndEncryptedBytes, startOffsetOfEncryptedBytes, encryptedBytes, 0, encryptedBytesLength);
            }

            SymmetricPBCipher cipher = new SymmetricPBCipher(
                    passphrase,
                    cfg.saltBitSize,
                    cfg.keyBitSize,
                    cfg.ivBitSize,
                    cfg.hashAlgorithm,
                    cfg.iterations,
                    cfg.cryptAlgorithm,
                    cfg.mode,
                    cfg.padding);
            KDF kdf = Reflects.<KDF>newInstance(cfg.kdfClass);
            cipher.setKdf(kdf);
            cipher.setSalt(salt);

            String result = new String(cipher.decrypt(encryptedBytes), Charsets.UTF_8) ;
            return result;
        }

    }
}
