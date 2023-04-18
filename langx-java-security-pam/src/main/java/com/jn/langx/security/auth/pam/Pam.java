package com.jn.langx.security.auth.pam;


import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

public class Pam {
    private static final Logger LOG;
    private String serviceName;
    public static final String DEFAULT_SERVICE_NAME = "langx-java-security-pam";

    public Pam() {
        this(DEFAULT_SERVICE_NAME);
    }

    public Pam(String serviceName) throws NullPointerException, IllegalArgumentException {
        if (serviceName == null) {
            throw new NullPointerException("Service name is null");
        } else if (serviceName.length() == 0) {
            throw new IllegalArgumentException("Service name is empty");
        } else {
            this.serviceName = serviceName;
        }
    }

    native boolean isSharedLibraryWorking();


    public boolean authenticateSuccessful(String username, String credentials) {
        PamAuthcResult success = PamAuthcResult.PAM_SUCCESS;
        PamAuthcResult actual = this.authenticate(username, credentials);
        return actual.equals(success);
    }

    public PamAuthcResult authenticate(String username, String credentials) throws NullPointerException {
        boolean debug = LOG.isDebugEnabled();
        LOG.debug("Debug mode active.");
        if (this.serviceName == null) {
            throw new NullPointerException("Service name is null");
        } else if (username == null) {
            throw new NullPointerException("User name is null");
        } else if (credentials == null) {
            throw new NullPointerException("Credentials are null");
        } else {
            synchronized(Pam.class) {
                PamAuthcResult pamReturnValue = PamAuthcResult.fromId(this.authenticate(this.serviceName, username, credentials, debug));
                return pamReturnValue;
            }
        }
    }

    private native int authenticate(String var1, String var2, String var3, boolean var4);

    public static String getLibraryName() {
        return System.mapLibraryName("jpam");
    }

    public String getServiceName() {
        return this.serviceName;
    }

    static {
        LOG = Loggers.getLogger(Pam.class);
        System.loadLibrary("jpam");
    }
}
