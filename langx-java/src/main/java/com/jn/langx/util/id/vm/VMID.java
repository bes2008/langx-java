package com.jn.langx.util.id.vm;


import com.jn.langx.util.collection.PrimitiveArrays;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.hash.HashCodeBuilder;

import java.io.Serializable;
import java.net.InetAddress;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;

/**
 * An object that uniquely identifies a virtual machine.
 *
 * <p>The identifier is composed of:
 * <ol>
 *    <li>The Internet address of the physical machine.</li>
 *    <li>The process identifier of the virtual machine.</li>
 *    <li>A UID to guarantee uniqness across multipule virtual
 *        machines on the same physical machine.</li>
 * </ol>
 *
 * <pre>
 *    [ address ] - [ process id ] - [ time ] - [ counter ]
 *                                   |------- UID --------|
 * </pre>
 *
 * <p>Numbers are converted to radix(Character.MAX_RADIX) when converting
 *    to strings.
 *
 */
public class VMID implements Serializable {
    /**
     * The serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * The address of the current virtual machine
     */
    protected final byte[] address;

    /**
     * The process identifier of the current virtual machine
     */
    protected final String pid;

    /**
     * A unique identifier to ensure uniqueness across the host machine
     */
    protected final UID uid;

    /**
     * The hash code of this VMID
     */
    protected final int hashCode;

    /**
     * Construct a new VMID.
     *
     * @param address The address of the current virtual machine.
     * @param pid     Process identifier.
     * @param uid     Unique identifier.
     * @see #getInstance()  For getting a VMID instance reference.
     */
    public VMID(final byte[] address, final String pid, final UID uid) {
        this.address = address;
        this.pid = pid;
        this.uid = uid;

        // generate a hashCode for this VMID
        int code = pid.hashCode();
        code ^= uid.hashCode();
        code ^= HashCodeBuilder.generate(address);
        hashCode = code;
    }

    /**
     * Copy a VMID.
     *
     * @param vmid VMID to copy.
     */
    public VMID(final VMID vmid) {
        this.address = vmid.address;
        this.pid = vmid.pid;
        this.uid = vmid.uid;
        this.hashCode = vmid.hashCode;
    }

    /**
     * Get the address portion of this VMID.
     *
     * @return The address portion of this VMID.
     */
    public final byte[] getAddress() {
        return PrimitiveArrays.copy(address);
    }

    /**
     * Get the process identifier portion of this VMID.
     *
     * @return The process identifier portion of this VMID.
     */
    public final String getProcessID() {
        return pid;
    }

    /**
     * Get the UID portion of this VMID.
     *
     * @return The UID portion of this VMID.
     */
    public final UID getUID() {
        return uid;
    }

    /**
     * Return a string representation of this VMID.
     *
     * @return A string representation of this VMID.
     */
    public String toString() {
        StringBuilder buff = new StringBuilder();

        for (int i = 0; i < address.length; i++) {
            int n = (address[i] & 0xFF);
            buff.append(Integer.toString(n, Character.MAX_RADIX));
        }

        buff.append("-").append(Integer.toString(Integer.parseInt(this.pid), Character.MAX_RADIX));
        buff.append("-").append(uid);

        return buff.toString();
    }

    /**
     * Return the hash code of this VMID.
     *
     * @return The hash code of this VMID.
     */
    public final int hashCode() {
        return hashCode;
    }

    /**
     * Check if the given object is equal to this VMID.
     *
     * <p>A VMID is equals to another VMID if the address,
     * process identifer and UID portions are equal.
     *
     * @param obj Object to test equality with.
     * @return True if object is equals to this VMID.
     */
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj != null && obj.getClass() == getClass()) {
            VMID vmid = (VMID) obj;
            return
                    Arrays.equals(vmid.address, address) &&
                            vmid.pid.equals(pid) &&
                            vmid.uid.equals(uid);
        }

        return false;
    }

    /**
     * Returns a VMID as a string.
     *
     * @return VMID as a string.
     */
    public static String asString() {
        return getInstance().toString();
    }


    /////////////////////////////////////////////////////////////////////////
    //                            Instance Access                          //
    /////////////////////////////////////////////////////////////////////////

    /**
     * The single instance of VMID for the running Virtual Machine
     */
    private static VMID instance = null;

    /**
     * Get the VMID for the current virtual machine.
     *
     * @return Virtual machine identifier.
     */
    public synchronized static VMID getInstance() {
        if (instance == null) {
            instance = create();
        }
        return instance;
    }

    /**
     * The address used when conventional methods fail to return the address
     * of the current machine.
     */
    static final byte[] UNKNOWN_HOST = {0, 0, 0, 0};

    /**
     * Return the current host internet address.
     */
    private static byte[] getHostAddress() {
        return (byte[]) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    return InetAddress.getLocalHost().getAddress();
                } catch (Exception e) {
                    return UNKNOWN_HOST;
                }
            }
        });
    }

    /**
     * Create the VMID for the current virtual mahcine.
     *
     * @return Virtual machine identifer.
     */
    private static VMID create() {
        // get the local internet address for the current host
        byte[] address = getHostAddress();

        return new VMID(address, Platform.processId, new UID());
    }
}

