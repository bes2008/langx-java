package com.jn.langx.security.ssl;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

class InternalKeyManagerProxy extends X509ExtendedKeyManager {

    private final X509ExtendedKeyManager keyManager;
    private final PrivateKeyAliasChooseStrategy aliasStrategy;

    InternalKeyManagerProxy(final X509ExtendedKeyManager keyManager, final PrivateKeyAliasChooseStrategy aliasStrategy) {
        super();
        this.keyManager = keyManager;
        this.aliasStrategy = aliasStrategy;
    }

    @Override
    public String[] getClientAliases(
            final String keyType, final Principal[] issuers) {
        return this.keyManager.getClientAliases(keyType, issuers);
    }

    public Map<String, PrivateKeyDetails> getClientAliasMap(
            final String[] keyTypes, final Principal[] issuers) {
        final Map<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
        for (final String keyType : keyTypes) {
            final String[] aliases = this.keyManager.getClientAliases(keyType, issuers);
            if (aliases != null) {
                for (final String alias : aliases) {
                    validAliases.put(alias,
                            new PrivateKeyDetails(keyType, this.keyManager.getCertificateChain(alias)));
                }
            }
        }
        return validAliases;
    }

    public Map<String, PrivateKeyDetails> getServerAliasMap(
            final String keyType, final Principal[] issuers) {
        final Map<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
        final String[] aliases = this.keyManager.getServerAliases(keyType, issuers);
        if (aliases != null) {
            for (final String alias : aliases) {
                validAliases.put(alias,
                        new PrivateKeyDetails(keyType, this.keyManager.getCertificateChain(alias)));
            }
        }
        return validAliases;
    }

    @Override
    public String chooseClientAlias(
            final String[] keyTypes, final Principal[] issuers, final Socket socket) {
        final Map<String, PrivateKeyDetails> validAliases = getClientAliasMap(keyTypes, issuers);
        return this.aliasStrategy.chooseAlias(validAliases, socket);
    }

    @Override
    public String[] getServerAliases(
            final String keyType, final Principal[] issuers) {
        return this.keyManager.getServerAliases(keyType, issuers);
    }

    @Override
    public String chooseServerAlias(
            final String keyType, final Principal[] issuers, final Socket socket) {
        final Map<String, PrivateKeyDetails> validAliases = getServerAliasMap(keyType, issuers);
        return this.aliasStrategy.chooseAlias(validAliases, socket);
    }

    @Override
    public X509Certificate[] getCertificateChain(final String alias) {
        return this.keyManager.getCertificateChain(alias);
    }

    @Override
    public PrivateKey getPrivateKey(final String alias) {
        return this.keyManager.getPrivateKey(alias);
    }

    @Override
    public String chooseEngineClientAlias(
            final String[] keyTypes, final Principal[] issuers, final SSLEngine sslEngine) {
        final Map<String, PrivateKeyDetails> validAliases = getClientAliasMap(keyTypes, issuers);
        return this.aliasStrategy.chooseAlias(validAliases, null);
    }

    @Override
    public String chooseEngineServerAlias(
            final String keyType, final Principal[] issuers, final SSLEngine sslEngine) {
        final Map<String, PrivateKeyDetails> validAliases = getServerAliasMap(keyType, issuers);
        return this.aliasStrategy.chooseAlias(validAliases, null);
    }

}
