package com.jn.langx.security.ssl;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SSLs {
    private SSLs() {
    }

    public static final String TLS = SSLProtocolVersion.TLSv12.getName();

    public static X509TrustManager noopX509TrustManager() {
        return new NoopTrustManager();
    }

    /**
     * Creates default factory based on the standard JSSE trust material
     * ({@code cacerts} file in the security properties directory). System properties
     * are not taken into consideration.
     *
     * @return the default SSL socket factory
     */
    public static SSLContext defaultSSLContext() throws SSLInitializationException {
        try {
            final SSLContext sslcontext = SSLContext.getInstance(TLS);
            sslcontext.init(null, null, null);
            return sslcontext;
        } catch (final NoSuchAlgorithmException ex) {
            throw new SSLInitializationException(ex.getMessage(), ex);
        } catch (final KeyManagementException ex) {
            throw new SSLInitializationException(ex.getMessage(), ex);
        }
    }

    /**
     * Creates default SSL context based on system properties. This method obtains
     * default SSL context by calling {@code SSLContext.getInstance("Default")}.
     * Please note that {@code Default} algorithm is supported as of Java 6.
     * This method will fall back onto {@link #defaultSSLContext()} when
     * {@code Default} algorithm is not available.
     *
     * @return default system SSL context
     */
    public static SSLContext systemDefaultSSLContext() throws SSLInitializationException {
        try {
            return SSLContext.getDefault();
        } catch (final NoSuchAlgorithmException ex) {
            return defaultSSLContext();
        }
    }

    /**
     * Creates custom SSL context.
     *
     * @return default system SSL context
     */
    public static SSLContextBuilder customSSLContext() {
        return SSLContextBuilder.create();
    }

    /**
     *
     * @param cipherSuiteName java 中 cipher-suite name
     * @return cipher suite object
     */
    public static CipherSuite inferCipherSuite(String cipherSuiteName){
        String suiteName=cipherSuiteName;

        String alias=null;
        if((Strings.startsWith(cipherSuiteName, "TLS_") || Strings.startsWith(cipherSuiteName, "SSL_") ) && Objs.length(cipherSuiteName) > 4){
            cipherSuiteName=Strings.substring(cipherSuiteName, 4);
            if(suiteName.startsWith("SSL_")){
                alias= "TLS_"+cipherSuiteName;
            }
        }else{
            throw new IllegalArgumentException("illegal cipher suite name:" + cipherSuiteName);
        }

        if(!Strings.contains(cipherSuiteName,"_WITH_")){
            cipherSuiteName="NULL_WITH_"+cipherSuiteName;
        }
        String[] parts=Strings.split(cipherSuiteName, "_WITH_");
        String ke_authc_part=parts[0];
        String enc_mac_part=parts[1];


        String keyExchangeAlgorithm;
        String authcAlgorithm;
        String encryptAlgorithm;
        String macAlgorithm;

        boolean export=false;
        boolean anonymous=false;


        // 处理 ke_auth_part 部分
        if(ke_authc_part.endsWith("_EXPORT")){
            ke_authc_part=Strings.substring(ke_authc_part,0, ke_authc_part.length()-"_EXPORT".length());
            export=true;
        }
        int ke_authc_split_index= Strings.indexOf(ke_authc_part,"_");
        if(ke_authc_split_index<0){
            keyExchangeAlgorithm=ke_authc_part;
            authcAlgorithm=ke_authc_part;
        }
        else{
            keyExchangeAlgorithm=Strings.substring(ke_authc_part,0,ke_authc_split_index);
            authcAlgorithm=Strings.substring(ke_authc_part,ke_authc_split_index+1);
        }



        // 处理enc_mac部分
        int enc_mac_split_index=Strings.lastIndexOf(enc_mac_part, "_");
        if(enc_mac_split_index<0){
            throw new IllegalArgumentException("illegal cipher suite name:" + cipherSuiteName);
        }
        encryptAlgorithm=Strings.substring(enc_mac_part, 0, enc_mac_split_index);
        macAlgorithm=Strings.substring(enc_mac_part, enc_mac_split_index+1);

        // 处理特例：
        if(suiteName.equals("TLS_EMPTY_RENEGOTIATION_INFO_SCSV")){
            anonymous=true;
            keyExchangeAlgorithm="SCSV";
            authcAlgorithm="anon";
            encryptAlgorithm=null;
            macAlgorithm=null;
        }
        // 处理匿名
        authcAlgorithm=Objs.useValueIfEquals(authcAlgorithm,"ANON", "anon");


        CipherSuite cipherSuite = new CipherSuite();
        cipherSuite.setName(suiteName);
        cipherSuite.setAlias(alias);
        cipherSuite.setExport(export);
        cipherSuite.setKeyExchangeAlgorithm(Objs.useValueIfEquals(keyExchangeAlgorithm, "NULL",null));
        cipherSuite.setAuthcAlgorithm(Objs.useValueIfEquals(authcAlgorithm, "NULL",null));
        cipherSuite.setAnonymous(anonymous);
        cipherSuite.setEncryptAlgorithm(Objs.useValueIfEquals(encryptAlgorithm,"NULL",null));
        cipherSuite.setMacAlgorithm(Objs.useValueIfEquals(macAlgorithm,"NULL",null));
        return cipherSuite;
    }

}
