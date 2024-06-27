package com.jn.langx.security.crypto.pbe;

import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.UnsupportedCipherAlgorithmException;
import com.jn.langx.security.crypto.cipher.Ciphers;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.key.LangxSecretKeyFactory;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedSecretKeySupplier;
import com.jn.langx.security.crypto.pbe.pbkdf.DerivedPBEKey;
import com.jn.langx.security.crypto.pbe.pbkdf.OpenSSLEvpKDFKeyFactorySpi;
import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeyFactorySpi;
import com.jn.langx.security.ext.js.cryptojs.CryptoJS;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.util.struct.Holder;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import java.security.Provider;
import java.security.SecureRandom;
import java.util.Map;

/**
 * @since 5.3.9
 */
public class PBEs {
    public static String PBKDF2WithHmacSHA224="PBKDF2WithHmacSHA224";
    public static String PBKDF2WithHmacSHA256="PBKDF2WithHmacSHA256";
    public static String PBKDF2WithHmacSHA384="PBKDF2WithHmacSHA384";
    public static String PBKDF2WithHmacSHA512="PBKDF2WithHmacSHA512";

    /**
     * key: pbe algorithm regex
     * value: secret key factory
     */
    private static Map<String, Supplier<String, PBKDFKeyFactorySpi>> PBE_DEFAULT_KEY_FACTORY_REGISTRY;
    static {
        Map<String, Supplier<String, PBKDFKeyFactorySpi>> map = Maps.newLinkedHashMap();

        map.put("PBEWith.*And.*OPENSSL_EVP", new Supplier<String, PBKDFKeyFactorySpi>(){
            @Override
            public PBKDFKeyFactorySpi get(String pbeAlgorithm) {
                return new OpenSSLEvpKDFKeyFactorySpi(pbeAlgorithm);
            }
        });

        PBE_DEFAULT_KEY_FACTORY_REGISTRY = map;

    }
    public static SecretKeyFactory getLangxPBEKeyFactory(final String pbeAlgorithm){
        SecretKeyFactory secretKeyFactory = null;

        // 从 langx pbe包下获取
        String upperCaseAlgorithm= Strings.upperCase(pbeAlgorithm);
        if(!Strings.startsWith(upperCaseAlgorithm, "PBEWITH")){
            throw new UnsupportedCipherAlgorithmException("unsupported PBE cipher algorithm: "+pbeAlgorithm);
        }

        Supplier<String, PBKDFKeyFactorySpi> supplier = PBE_DEFAULT_KEY_FACTORY_REGISTRY.get(pbeAlgorithm);

        if(supplier==null) {
            Map.Entry supplierEntry = Collects.findFirst(PBE_DEFAULT_KEY_FACTORY_REGISTRY, new Predicate2<String, Supplier<String, PBKDFKeyFactorySpi>>() {
                @Override
                public boolean test(String key, Supplier<String, PBKDFKeyFactorySpi> supplier) {
                    return Regexps.match(key, pbeAlgorithm);
                }
            });

            if (supplierEntry != null) {
                supplier = (Supplier<String, PBKDFKeyFactorySpi>) supplierEntry.getValue();
            }
        }
        if(supplier==null){
            throw new UnsupportedCipherAlgorithmException("unsupported PBE cipher algorithm: "+pbeAlgorithm);
        }

        PBKDFKeyFactorySpi secretKeyFactorySpi=supplier.get(pbeAlgorithm);
        secretKeyFactory = new LangxSecretKeyFactory(secretKeyFactorySpi, Securitys.getLangxSecurityProvider(),pbeAlgorithm);
        return secretKeyFactory;
    }

    public static SecretKeyFactory getPBEKeyFactoryFromProvider(final String pbeAlgorithm, Provider provider){
        // 从 providers 中 获取 key factory
        SecretKeyFactory secretKeyFactory = null;
        try {
            secretKeyFactory = PKIs.getSecretKeyFactory(pbeAlgorithm, provider==null?null:provider.getName());
        }catch (Throwable e){
            // ignore it
        }
        return secretKeyFactory;
    }

    public static SecretKeyFactory getPBEKeyFactory(final String pbeAlgorithm, Provider provider){
        // 从 providers 中 获取 key factory
        SecretKeyFactory secretKeyFactory = getPBEKeyFactoryFromProvider(pbeAlgorithm,provider);
        if(secretKeyFactory==null) {
            secretKeyFactory = getLangxPBEKeyFactory(pbeAlgorithm);
        }
        return secretKeyFactory;
    }

    private static byte[] doEncryptOrDecrypt(byte[] bytes, String pbeAlgorithm, PBEKeySpec keySpec, String algorithmTransformation, Provider provider, SecureRandom secureRandom, Holder<byte[]> ivHolder, boolean encrypt){
        try {
            boolean isLangxSecretKeyFactory = false;
            SecretKeyFactory secretKeyFactory = getPBEKeyFactoryFromProvider(pbeAlgorithm, provider);
            if (secretKeyFactory == null) {
                secretKeyFactory = getLangxPBEKeyFactory(pbeAlgorithm);
                isLangxSecretKeyFactory=true;
            }
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            PBEKey pbeKey = (PBEKey) secretKey;

            if (!isLangxSecretKeyFactory) {
                String cipherAlgorithm = Ciphers.extractAlgorithm(algorithmTransformation);
                if (Objs.isEmpty(cipherAlgorithm)) {
                    cipherAlgorithm = PBEs.extractCipherAlgorithm(pbeAlgorithm);
                }
                Symmetrics.MODE mode=Ciphers.extractSymmetricMode(algorithmTransformation);
                IvParameterSpec ivObj=Ciphers.createIvParameterSpec(Objs.isEmpty(ivHolder.get())?pbeKey.getEncoded():ivHolder.get());
                if(mode== Symmetrics.MODE.ECB){
                    ivObj=null;
                }
                return Ciphers.doEncryptOrDecrypt(bytes, pbeKey.getEncoded(), cipherAlgorithm, algorithmTransformation, provider, secureRandom, new BytesBasedSecretKeySupplier(), ivObj , encrypt);
            } else {
                DerivedPBEKey derivedKey = (DerivedPBEKey) pbeKey;
                String cipherAlgorithm = derivedKey.getCipherAlgorithm();

                if (Objs.isEmpty(cipherAlgorithm) && Objs.isNotEmpty(algorithmTransformation)) {
                    cipherAlgorithm = Ciphers.extractAlgorithm(algorithmTransformation);
                }
                if (Objs.isEmpty(cipherAlgorithm)) {
                    cipherAlgorithm = PBEs.extractCipherAlgorithm(pbeAlgorithm);
                }
                Symmetrics.MODE mode = Ciphers.extractSymmetricMode(algorithmTransformation);

                IvParameterSpec ivObj=(IvParameterSpec)derivedKey;
                if(ivObj.getIV().length==0){
                    // 如果派生算法没有生成iv，则使用自定义的iv
                    ivObj=new IvParameterSpec(ivHolder.get());
                }else{
                    // 反之则反馈生成的iv
                    ivHolder.set(ivObj.getIV());
                }
                if(mode== Symmetrics.MODE.ECB){
                    ivObj = null;
                }
                return Ciphers.doEncryptOrDecrypt(bytes, pbeKey.getEncoded(), cipherAlgorithm, algorithmTransformation, provider, secureRandom, new BytesBasedSecretKeySupplier(), ivObj, encrypt);
            }
        }catch (Throwable e){
            throw new SecurityException(e);
        }
    }

    public static byte[] encrypt(byte[] bytes, String pbeAlgorithm, PBEKeySpec keySpec, String algorithmTransformation, Holder<byte[]> iv, Provider provider, SecureRandom secureRandom){
        return doEncryptOrDecrypt(bytes,pbeAlgorithm,keySpec,algorithmTransformation,provider,secureRandom,iv,true);
    }

    public static byte[] decrypt(byte[] bytes, String pbeAlgorithm, PBEKeySpec keySpec, String algorithmTransformation,Holder<byte[]> iv, Provider provider, SecureRandom secureRandom){
        return doEncryptOrDecrypt(bytes,pbeAlgorithm,keySpec,algorithmTransformation,provider,secureRandom,iv,false);
    }

    private static final Regexp PBE_ALGORITHM_REGEXP=Regexps.createRegexp("PBEWith(?<HASH>:.*)And(?<CIPHER>.*)(\\-.*)*", Option.fromJavaScriptFlags("ig"));
    public static String extractHashAlgorithm(String pbeAlgorithm){
        Map<String,String> groups=Regexps.findNamedGroup(PBE_ALGORITHM_REGEXP, pbeAlgorithm);
        return groups.get("HASH");
    }

    public static String extractCipherAlgorithm(String pbeAlgorithm){
        Map<String,String> groups=Regexps.findNamedGroup(PBE_ALGORITHM_REGEXP, pbeAlgorithm);
        return groups.get("CIPHER");
    }

}
