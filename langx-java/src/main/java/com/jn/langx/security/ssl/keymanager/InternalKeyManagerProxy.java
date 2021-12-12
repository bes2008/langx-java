package com.jn.langx.security.ssl.keymanager;

import com.jn.langx.security.ssl.PrivateKeyDetails;
import com.jn.langx.security.ssl.keymanager.PrivateKeyAliasChooseStrategy;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public final class InternalKeyManagerProxy extends X509ExtendedKeyManager {

    private final X509ExtendedKeyManager delegate;
    private final PrivateKeyAliasChooseStrategy aliasStrategy;

    InternalKeyManagerProxy(final X509ExtendedKeyManager delegate, final PrivateKeyAliasChooseStrategy aliasStrategy) {
        super();
        this.delegate = delegate;
        this.aliasStrategy = aliasStrategy;
    }

    @Override
    public String[] getServerAliases(final String keyType, final Principal[] issuers) {
        return this.delegate.getServerAliases(keyType, issuers);
    }
    @Override
    public String[] getClientAliases(final String keyType, final Principal[] issuers) {
        return this.delegate.getClientAliases(keyType, issuers);
    }

    @Override
    public X509Certificate[] getCertificateChain(final String alias) {
        return this.delegate.getCertificateChain(alias);
    }

    @Override
    public PrivateKey getPrivateKey(final String alias) {
        return this.delegate.getPrivateKey(alias);
    }

    private Map<String, PrivateKeyDetails> getClientAliasMap(final String[] keyTypes, final Principal[] issuers) {
        final Map<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
        for (final String keyType : keyTypes) {
            final String[] aliases = this.delegate.getClientAliases(keyType, issuers);
            if (aliases != null) {
                for (final String alias : aliases) {
                    validAliases.put(alias, new PrivateKeyDetails(keyType, this.delegate.getCertificateChain(alias)));
                }
            }
        }
        return validAliases;
    }

    private Map<String, PrivateKeyDetails> getServerAliasMap(final String keyType, final Principal[] issuers) {
        final Map<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
        final String[] aliases = this.delegate.getServerAliases(keyType, issuers);
        if (aliases != null) {
            for (final String alias : aliases) {
                validAliases.put(alias, new PrivateKeyDetails(keyType, this.delegate.getCertificateChain(alias)));
            }
        }
        return validAliases;
    }

    @Override
    public String chooseClientAlias(final String[] keyTypes, final Principal[] issuers, final Socket socket) {
        final Map<String, PrivateKeyDetails> validAliases = getClientAliasMap(keyTypes, issuers);
        return this.aliasStrategy.chooseAlias(validAliases, socket);
    }


    @Override
    public String chooseServerAlias(final String keyType, final Principal[] issuers, final Socket socket) {
        final Map<String, PrivateKeyDetails> validAliases = getServerAliasMap(keyType, issuers);
        return this.aliasStrategy.chooseAlias(validAliases, socket);
    }


    @Override
    public String chooseEngineClientAlias(final String[] keyTypes, final Principal[] issuers, final SSLEngine sslEngine) {
        final Map<String, PrivateKeyDetails> validAliases = getClientAliasMap(keyTypes, issuers);
        return this.aliasStrategy.chooseAlias(validAliases, null);
    }

    @Override
    public String chooseEngineServerAlias(final String keyType, final Principal[] issuers, final SSLEngine sslEngine) {
        final Map<String, PrivateKeyDetails> validAliases = getServerAliasMap(keyType, issuers);
        return this.aliasStrategy.chooseAlias(validAliases, null);
    }

}
