package com.jn.langx.security.ssl;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SSLs {
    private SSLs(){}

    /**
     * Creates default factory based on the standard JSSE trust material
     * ({@code cacerts} file in the security properties directory). System properties
     * are not taken into consideration.
     *
     * @return the default SSL socket factory
     */
    public static SSLContext createDefault() throws SSLInitializationException {
        try {
            final SSLContext sslcontext = SSLContext.getInstance(SSLContextBuilder.TLS);
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
     * This method will fall back onto {@link #createDefault()} when
     * {@code Default} algorithm is not available.
     *
     * @return default system SSL context
     */
    public static SSLContext createSystemDefault() throws SSLInitializationException {
        try {
            return SSLContext.getDefault();
        } catch (final NoSuchAlgorithmException ex) {
            return createDefault();
        }
    }

    /**
     * Creates custom SSL context.
     *
     * @return default system SSL context
     */
    public static SSLContextBuilder custom() {
        return SSLContextBuilder.create();
    }

}
