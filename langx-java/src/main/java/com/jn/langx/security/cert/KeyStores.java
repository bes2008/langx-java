package com.jn.langx.security.cert;

import java.io.File;
import java.security.KeyStore;
import java.util.Locale;

public class KeyStores {
    /**
     * Make a best guess about the "type" (see {@link KeyStore#getType()}) of the keystore file located at the given {@code Path}.
     * This method only references the <em>file name</em> of the keystore, it does not look at its contents.
     */
    public static String inferKeyStoreType(File path) {
        String name = path == null ? "" : path.toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".p12") || name.endsWith(".pfx") || name.endsWith(".pkcs12")) {
            return "PKCS12";
        } else {
            return "jks";
        }
    }

}
