package com.jn.langx.security.ext.cryptojs;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.CipherAlgorithmPadding;
import com.jn.langx.security.crypto.cipher.Ciphers;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.pbe.PBEs;
import com.jn.langx.security.pbe.pbkdf.PBKDFKeySpec;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.io.Charsets;

public class CryptoJS {

    public static class SymmetricConfig{

        public int keyBitSize;
        public int ivBitSize;
        public int saltBitSize;
        public int iterations;

        public String cipherAlgorithm;
        public Symmetrics.MODE mode;
        public CipherAlgorithmPadding padding;

        public SymmetricConfig(int saltBitSize,
                               int keyBitSize,
                               int ivBitSize,
                               int iterations,
                               String cipherAlgorithm,
                               Symmetrics.MODE mode,
                               CipherAlgorithmPadding padding){
            this.saltBitSize= saltBitSize;
            this.keyBitSize=keyBitSize;
            this.ivBitSize=ivBitSize;
            this.iterations=iterations;

            this.cipherAlgorithm= cipherAlgorithm;
            this.mode=mode;
            this.padding=padding;

        }
    }
    public static class PBEConfig extends SymmetricConfig{
        public String hashAlgorithm;
        public String pbeAlgorithm;

        public PBEConfig(
                int saltBitSize,
                int keyBitSize,
                int ivBitSize,
                int iterations,
                String cipherAlgorithm,
                Symmetrics.MODE mode,
                CipherAlgorithmPadding padding,
                String hashAlgorithm,
                String pbeAlgorithm){
            super(saltBitSize,keyBitSize,ivBitSize,iterations, cipherAlgorithm, mode, padding);
            this.hashAlgorithm=hashAlgorithm;
            this.pbeAlgorithm=pbeAlgorithm;
        }
    }
    public static class AESConfig extends PBEConfig{
        public AESConfig(){
            this(64, 256, 128, 1,  JCAEStandardName.AES.getName(),  Symmetrics.MODE.CBC, CipherAlgorithmPadding.PKCS5Padding,  JCAEStandardName.MD5.getName(),"PBEWithMD5AndAES-OPENSSL_EVP");
        }
        public AESConfig(
                int saltBitSize,
                int keyBitSize,
                int ivBitSize,
                int iterations,
                String cipherAlgorithm,
                Symmetrics.MODE mode,
                CipherAlgorithmPadding padding,
                String hashAlgorithm,
                String pbeAlgorithm){
            super(saltBitSize, keyBitSize, ivBitSize, iterations, cipherAlgorithm, mode, padding, hashAlgorithm, pbeAlgorithm);
        }
    }

    public static class AES{
        private static String SALT_PREFIX="Salted__";
        public static String encrypt(String message, String passphrase, CryptoJS.AESConfig cfg){
            if (cfg==null){
                cfg= new CryptoJS.AESConfig();
            }

            byte[] salt =  Securitys.randomBytes(cfg.saltBitSize);
            PBKDFKeySpec pbeKeySpec = new PBKDFKeySpec(passphrase.toCharArray(), salt, cfg.keyBitSize,cfg.ivBitSize, cfg.iterations,cfg.hashAlgorithm );
            String transformation= Ciphers.createAlgorithmTransformation(cfg.cipherAlgorithm, cfg.mode.name(),cfg.padding);

            byte[] encryptedBytes= PBEs.encrypt(
                        Strings.getBytesUtf8(message),
                        cfg.pbeAlgorithm,
                        pbeKeySpec,
                        transformation,
                        null,null
                    );


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

            /*
            SymmetricPBKDFCipher cipher = new SymmetricPBKDFCipher(
                    passphrase,
                    cfg.saltBitSize,
                    cfg.keyBitSize,
                    cfg.ivBitSize,
                    cfg.hashAlgorithm,
                    cfg.iterations,
                    cfg.cryptAlgorithm,
                    cfg.mode,
                    cfg.padding);
            PBKDF kdf = Reflects.<PBKDF>newInstance(cfg.kdfClass);
            cipher.setKdf(kdf);
            cipher.setSalt(salt);

             */
            PBKDFKeySpec pbeKeySpec = new PBKDFKeySpec(passphrase.toCharArray(), salt, cfg.keyBitSize,cfg.ivBitSize, cfg.iterations,cfg.hashAlgorithm );
            String transformation= Ciphers.createAlgorithmTransformation(cfg.cipherAlgorithm, cfg.mode.name(),cfg.padding);

            byte[] rawBytes = PBEs.decrypt(
                    encryptedBytes,
                    cfg.pbeAlgorithm,
                    pbeKeySpec,
                    transformation,
                    null,null
            );

            String result = new String(rawBytes, Charsets.UTF_8) ;
            return result;
        }

    }
}
