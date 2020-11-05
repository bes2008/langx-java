package com.jn.langx.util.id.vm;


import java.io.Serializable;

/**
 * A globally unique identifier (globally across a cluster of virtual
 * machines).
 *
 * <p>The identifier is composed of:
 * <ol>
 *    <li>The VMID for the virtual machine.</li>
 *    <li>A UID to provide uniqueness over a VMID.</li>
 * </ol>
 *
 * <pre>
 *    [ address ] - [ process id ] - [ time ] - [ counter ] - [ time ] - [ counter ]
 *                                   |------- UID --------|   |------- UID --------|
 *    |---------------------- VMID -----------------------|
 * </pre>
 *
 * @see VMID
 * @see UID
 */
public class GUID implements Serializable, Cloneable, Comparable {
    /**
     * The serial version id, @since 1.6
     */
    static final long serialVersionUID = 3289509836244263718L;
    /**
     * The virtual machine identifier
     */
    protected final VMID vmid;

    /**
     * The unique identifier
     */
    protected final UID uid;

    /**
     * The hash code of this GUID
     */
    protected final int hashCode;

    private transient String toString;

    /**
     * Construct a new GUID.
     */
    public GUID() {
        this.vmid = VMID.getInstance();
        this.uid = new UID();

        // generate a hash code for this GUID
        int code = vmid.hashCode();
        code ^= uid.hashCode();
        hashCode = code;
    }

    /**
     * Copy a GUID.
     *
     * @param guid GUID to copy.
     */
    protected GUID(final GUID guid) {
        this.vmid = guid.vmid;
        this.uid = guid.uid;
        this.hashCode = guid.hashCode;
    }

    /**
     * Get the VMID portion of this GUID.
     *
     * @return The VMID portion of this GUID.
     */
    public final VMID getVMID() {
        return vmid;
    }

    /**
     * Get the UID portion of this GUID.
     *
     * @return The UID portion of this GUID.
     */
    public final UID getUID() {
        return uid;
    }

    /**
     * Return a string representation of this GUID.
     *
     * @return A string representation of this GUID.
     */
    public String toString() {
        // JBCOMMON-88. Cache this as it's expensive to create
        if (toString == null) {
            toString = vmid.toString() + "-" + uid.toString();
        }
        return toString;
    }

    /**
     * Return the hash code of this GUID.
     *
     * @return The hash code of this GUID.
     */
    public int hashCode() {
        return hashCode;
    }

    /**
     * Check if the given object is equal to this GUID.
     *
     * <p>A GUID is equal to another GUID if the VMID and UID portions are
     * equal.
     *
     * @param obj Object to test equality with.
     * @return True if object is equal to this GUID.
     */
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj != null && obj.getClass() == getClass()) {
            GUID guid = (GUID) obj;

            return
                    guid.vmid.equals(vmid) &&
                            guid.uid.equals(uid);
        }

        return false;
    }

    /**
     * Returns a copy of this GUID.
     *
     * @return A copy of this GUID.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * Returns a GUID as a string.
     *
     * @return GUID as a string.
     */
    public static String asString() {
        return new GUID().toString();
    }

    public int compareTo(Object o) {
        GUID guid = (GUID) o;
        return this.toString().compareTo(guid.toString());
    }

}
