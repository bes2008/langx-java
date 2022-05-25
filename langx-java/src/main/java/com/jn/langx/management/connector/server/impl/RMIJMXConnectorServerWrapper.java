package com.jn.langx.management.connector.server.impl;

import com.jn.langx.management.connector.server.JMXConnectorServerWrapper;
import com.jn.langx.util.collection.Arrs;

import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.HashMap;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;


public class RMIJMXConnectorServerWrapper implements JMXConnectorServerWrapper {
    private static final String serverNameDefault = "jmxrmi";
    protected boolean startRmiRegistry = false;
    protected int rmiRegistryPort = -1;
    protected int rmiServerPort = -1;
    protected boolean rmiSSL = true;
    protected String ciphers[] = null;
    protected String protocols[] = null;
    protected boolean clientAuth = true;
    protected boolean authenticate = true;
    protected String passwordFile = null;
    protected String loginModuleName = null;
    protected String accessFile = null;
    protected boolean useLocalPorts = false;
    protected String host = "localhost";

    private String serviceName;
    protected JMXConnectorServer connectorServer = null;

    public RMIJMXConnectorServerWrapper() {
        // Configure using standard jmx system properties
        init();
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        destroyServer(connectorServer);
    }

    /**
     * {@inheritDoc}
     */
    public void startup() {

        // Prevent an attacker guessing the RMI object ID
        System.setProperty("java.rmi.server.randomIDs", "true");

        // Create the environment
        HashMap<String, Object> env = new HashMap<String, Object>();

        RMIClientSocketFactory csf = null;
        RMIServerSocketFactory ssf = null;

        // Configure SSL for RMI connection if required
        if (rmiSSL) {
            csf = new SslRMIClientSocketFactory();
            ssf = new SslRMIServerSocketFactory(ciphers, protocols, clientAuth);
        }

        // Force the use of local ports if required
        if (useLocalPorts) {
            csf = new RmiClientLocalhostSocketFactory(csf);
        }

        // Populate the env properties used to create the server
        if (csf != null) {
            env.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, csf);
        }
        if (ssf != null) {
            env.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, ssf);
        }

        // Configure authentication
        if (authenticate) {
            env.put("jmx.remote.x.password.file", passwordFile);
            env.put("jmx.remote.x.access.file", accessFile);
            env.put("jmx.remote.x.login.config", loginModuleName);
        }

        // Create the Platform server
        connectorServer = createServer(env,
                ManagementFactory.getPlatformMBeanServer());

    }

    private void init() {
        // Get all the other parameters required from the standard system
        // properties. Only need to get the parameters that affect the creation
        // of the server port.
        String rmiSSLValue = System.getProperty(
                "com.sun.management.jmxremote.ssl", "true");
        rmiSSL = Boolean.parseBoolean(rmiSSLValue);

        String protocolsValue = System
                .getProperty("com.sun.management.jmxremote.ssl.enabled.protocols");
        if (protocolsValue != null) {
            protocols = protocolsValue.split(",");
        }

        String ciphersValue = System
                .getProperty("com.sun.management.jmxremote.ssl.enabled.cipher.suites");
        if (ciphersValue != null) {
            ciphers = ciphersValue.split(",");
        }

        String clientAuthValue = System.getProperty(
                "com.sun.management.jmxremote.ssl.need.client.auth", "true");
        clientAuth = Boolean.parseBoolean(clientAuthValue);

        String authenticateValue = System.getProperty(
                "com.sun.management.jmxremote.authenticate", "true");
        authenticate = Boolean.parseBoolean(authenticateValue);

        passwordFile = System.getProperty(
                "com.sun.management.jmxremote.password.file",
                "jmxremote.password");

        accessFile = System.getProperty(
                "com.sun.management.jmxremote.access.file", "jmxremote.access");

        loginModuleName = System
                .getProperty("com.sun.management.jmxremote.login.config");
        serviceName = serverNameDefault;
    }

    private JMXConnectorServer createServer(HashMap<String, Object> theEnv,
                                            MBeanServer theMBeanServer) {

        // Create the RMI registry
        if (rmiRegistryPort == -1) {
            if (!startRmiRegistry) {
                rmiRegistryPort = rmiServerPort;
            }
        }
        try {
            LocateRegistry.createRegistry(rmiRegistryPort);
        } catch (RemoteException e) {
            return null;
        }

        // Build the connection string with fixed ports
        StringBuilder url = new StringBuilder();
        if (startRmiRegistry) {
            url.append("service:jmx:rmi://").append(host).append(":");
            url.append(rmiServerPort);
            url.append("/jndi/rmi://").append(host).append(":");
            url.append(rmiRegistryPort);
            url.append("/").append(serviceName);
        } else {
            url.append("service:jmx:rmi:///jndi/rmi//").append(host)
                    .append(":").append(rmiServerPort).append("/")
                    .append(serviceName);
        }

        JMXServiceURL serviceUrl;
        try {
            serviceUrl = new JMXServiceURL(url.toString());
        } catch (MalformedURLException e) {
            return null;
        }

        // Start the JMX server with the connection string
        JMXConnectorServer cs = null;
        try {
            cs = JMXConnectorServerFactory.newJMXConnectorServer(serviceUrl,
                    theEnv, theMBeanServer);
            cs.start();
        } catch (IOException e) {
        }
        return cs;
    }

    private void destroyServer(JMXConnectorServer theConnectorServer) {
        if (theConnectorServer != null) {
            try {
                theConnectorServer.stop();
            } catch (IOException e) {
            }
        }
    }

    public int getRmiRegistryPort() {
        return rmiRegistryPort;
    }

    public void setRmiRegistryPort(int rmiRegistryPort) {
        this.rmiRegistryPort = rmiRegistryPort;
    }

    public int getRmiServerPort() {
        return rmiServerPort;
    }

    public void setRmiServerPort(int rmiServerPort) {
        this.rmiServerPort = rmiServerPort;
    }

    public boolean isRmiSSL() {
        return rmiSSL;
    }

    public void setRmiSSL(boolean rmiSSL) {
        this.rmiSSL = rmiSSL;
    }

    public String[] getCiphers() {
        return Arrs.copy(ciphers);
    }

    public void setCiphers(String[] ciphers) {
        this.ciphers = ciphers;
    }

    public String[] getProtocols() {
        return Arrs.copy(protocols);
    }

    public void setProtocols(String[] protocols) {
        this.protocols = Arrs.copy(protocols);
    }

    public boolean isClientAuth() {
        return clientAuth;
    }

    public void setClientAuth(boolean clientAuth) {
        this.clientAuth = clientAuth;
    }

    public boolean isAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(boolean authenticate) {
        this.authenticate = authenticate;
    }

    public String getPasswordFile() {
        return passwordFile;
    }

    public void setPasswordFile(String passwordFile) {
        this.passwordFile = passwordFile;
    }

    public String getLoginModuleName() {
        return loginModuleName;
    }

    public void setLoginModuleName(String loginModuleName) {
        this.loginModuleName = loginModuleName;
    }

    public String getAccessFile() {
        return accessFile;
    }

    public void setAccessFile(String accessFile) {
        this.accessFile = accessFile;
    }

    public boolean isUseLocalPorts() {
        return useLocalPorts;
    }

    public void setUseLocalPorts(boolean useLocalPorts) {
        this.useLocalPorts = useLocalPorts;
    }

    public String getServiceName() {
        return serviceName;
    }

    public final void setServiceName(String serviceName) {
        if (serviceName == null) {
            serviceName = serverNameDefault;
        } else {
            while (serviceName.startsWith("/")) {
                serviceName = serviceName.substring(1);
            }
        }
        if (serviceName.isEmpty()) {
            serviceName = serverNameDefault;
        }
        this.serviceName = serviceName;
    }

    @SuppressWarnings("serial")
    public static class RmiClientLocalhostSocketFactory implements
            RMIClientSocketFactory, Serializable {
        private static final String FORCED_HOST = "localhost";

        private RMIClientSocketFactory factory = null;

        public RmiClientLocalhostSocketFactory(RMIClientSocketFactory theFactory) {
            factory = theFactory;
        }

        public Socket createSocket(String host, int port) throws IOException {
            if (factory == null) {
                return new Socket(FORCED_HOST, port);
            } else {
                return factory.createSocket(FORCED_HOST, port);
            }
        }
    }

}
