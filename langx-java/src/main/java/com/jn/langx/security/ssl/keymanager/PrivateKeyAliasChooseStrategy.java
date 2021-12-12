package com.jn.langx.security.ssl.keymanager;


import com.jn.langx.security.ssl.PrivateKeyDetails;

import java.net.Socket;
import java.util.Map;

/**
 * A strategy allowing for a choice of an alias during SSL authentication.
 *
 */
public interface PrivateKeyAliasChooseStrategy {

    /**
     * Determines what key material to use for SSL authentication.
     *
     * @param aliases available private key material
     * @param socket socket used for the connection. Please note this parameter can be {@code null}
     * if key material is applicable to any socket.
     */
    String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket);

}
