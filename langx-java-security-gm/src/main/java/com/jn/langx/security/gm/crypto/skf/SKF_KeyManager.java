package com.jn.langx.security.gm.crypto.skf;


import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.security.auth.x500.X500Principal;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.*;

public class SKF_KeyManager extends X509ExtendedKeyManager
{
    private static final String[] STRING0;
    private Map<String, X509Credentials> credentialsMap;
    private X509Certificate[] sigCert;
    private X509Certificate[] encCert;
    private PrivateKey sigPri;
    private PrivateKey encPri;

    static {
        STRING0 = new String[0];
    }

    public SKF_KeyManager(final ICryptoProvider cryptoProvider, final X509Certificate[] array) {
        this.credentialsMap = new HashMap<String, X509Credentials>();
        this.sigCert = null;
        this.encCert = null;
        this.sigPri = null;
        this.encPri = null;
        try {
            final X509Certificate cert = cryptoProvider.getCert(1);
            final X509Certificate cert2 = cryptoProvider.getCert(0);
            if (array != null && array.length > 0) {
                (this.sigCert = new X509Certificate[1 + array.length])[0] = cert;
                for (int i = 0; i < array.length; ++i) {
                    this.sigCert[1 + i] = array[i];
                }
            }
            else {
                this.sigCert = new X509Certificate[] { cert };
            }
            this.encCert = new X509Certificate[] { cert2 };
            this.sigPri = cryptoProvider.getPrivateKey(1);
            this.encPri = cryptoProvider.getPrivateKey(0);
            this.credentialsMap.put("SKF_Sig", new X509Credentials(this.sigPri, this.sigCert));
            this.credentialsMap.put("SKF_Enc", new X509Credentials(this.encPri, this.encCert));
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String[] getClientAliases(final String s, final Principal[] array) {
        return this.getAliases(s, array);
    }

    @Override
    public String chooseClientAlias(final String[] array, final Principal[] array2, final Socket socket) {
        if (array == null) {
            return null;
        }
        for (int i = 0; i < array.length; ++i) {
            final String[] clientAliases = this.getClientAliases(array[i], array2);
            if (clientAliases != null && clientAliases.length > 0) {
                if (array[i].equals("EC") || array[i].equals("EC_EC")) {
                    if (clientAliases.length == 1) {
                        return clientAliases[0];
                    }
                    if (clientAliases.length > 1) {
                        return String.valueOf(clientAliases[0]) + ":" + clientAliases[1];
                    }
                }
                return clientAliases[0];
            }
        }
        return null;
    }

    @Override
    public String chooseEngineClientAlias(final String[] array, final Principal[] array2, final SSLEngine sslEngine) {
        if (array == null) {
            return null;
        }
        for (int i = 0; i < array.length; ++i) {
            final String[] clientAliases = this.getClientAliases(array[i], array2);
            if (clientAliases != null && clientAliases.length > 0) {
                return clientAliases[0];
            }
        }
        return null;
    }

    @Override
    public String[] getServerAliases(final String s, final Principal[] array) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String chooseServerAlias(final String s, final Principal[] array, final Socket socket) {
        throw new UnsupportedOperationException();
    }

    @Override
    public X509Certificate[] getCertificateChain(final String s) {
        if (s.equals("SKF_Sig")) {
            return this.sigCert;
        }
        if (s.equals("SKF_Enc")) {
            return this.encCert;
        }
        return null;
    }

    @Override
    public PrivateKey getPrivateKey(final String s) {
        if (s.equals("SKF_Sig")) {
            return this.sigPri;
        }
        if (s.equals("SKF_Enc")) {
            return this.encPri;
        }
        return null;
    }

    private String[] getAliases(String substring, Principal[] convertPrincipals) {
        if (substring == null) {
            return null;
        }
        if (convertPrincipals == null) {
            convertPrincipals = new X500Principal[0];
        }
        if (!(convertPrincipals instanceof X500Principal[])) {
            convertPrincipals = convertPrincipals(convertPrincipals);
        }
        String substring2;
        if (substring.contains("_")) {
            final int index = substring.indexOf("_");
            substring2 = substring.substring(index + 1);
            substring = substring.substring(0, index);
        }
        else {
            substring2 = null;
        }
        final X500Principal[] array = (X500Principal[])convertPrincipals;
        final ArrayList<String> list = new ArrayList<String>();
        for (final Map.Entry<String, X509Credentials> entry : this.credentialsMap.entrySet()) {
            final String s = entry.getKey();
            final X509Credentials x509Credentials = entry.getValue();
            final X509Certificate[] certificates = x509Credentials.certificates;
            if (!substring.equals(certificates[0].getPublicKey().getAlgorithm())) {
                continue;
            }
            if (substring2 != null) {
                if (certificates.length > 1) {
                    if (!substring2.equals(certificates[1].getPublicKey().getAlgorithm())) {
                        continue;
                    }
                }
                else {
                    final String upperCase = certificates[0].getSigAlgName().toUpperCase(Locale.ENGLISH);
                    final String string = "WITH" + substring2.toUpperCase(Locale.ENGLISH);
                    if (!upperCase.contains(string)) {
                        continue;
                    }
                }
            }
            if (convertPrincipals.length == 0) {
                list.add(s);
            }
            else {
                final Set<X500Principal> issuerX500Principals = x509Credentials.getIssuerX500Principals();
                for (int i = 0; i < array.length; ++i) {
                    if (issuerX500Principals.contains(convertPrincipals[i])) {
                        list.add(s);
                        break;
                    }
                }
            }
        }
        final String[] array2 = list.toArray(SKF_KeyManager.STRING0);
        return (String[])((array2.length == 0) ? null : array2);
    }

    private static X500Principal[] convertPrincipals(final Principal[] array) {
        final ArrayList<X500Principal> list = new ArrayList<X500Principal>(array.length);
        for (int i = 0; i < array.length; ++i) {
            final Principal principal = array[i];
            if (principal instanceof X500Principal) {
                list.add((X500Principal)principal);
            }
            else {
                try {
                    list.add(new X500Principal(principal.getName()));
                }
                catch (IllegalArgumentException ex) {}
            }
        }
        return list.toArray(new X500Principal[list.size()]);
    }

    private static class X509Credentials
    {
        PrivateKey privateKey;
        X509Certificate[] certificates;
        private Set<X500Principal> issuerX500Principals;

        X509Credentials(final PrivateKey privateKey, final X509Certificate[] certificates) {
            this.privateKey = privateKey;
            this.certificates = certificates;
        }

        synchronized Set<X500Principal> getIssuerX500Principals() {
            if (this.issuerX500Principals == null) {
                this.issuerX500Principals = new HashSet<X500Principal>();
                for (int i = 0; i < this.certificates.length; ++i) {
                    this.issuerX500Principals.add(this.certificates[i].getIssuerX500Principal());
                }
            }
            return this.issuerX500Principals;
        }
    }
}