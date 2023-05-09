package com.jn.langx.util.net.cidr;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.net.Nets;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import static java.lang.Integer.signum;

/**
 * A Classless Inter-Domain Routing address.  This is the combination of an IP address and a netmask.
 * <p>
 * Migrate from wildfly-common.jar
 *
 * @since 4.1.0
 */
public final class CidrAddress implements Serializable, Comparable<CidrAddress> {
    private static final long serialVersionUID = -1L;

    /**
     * The CIDR address representing all IPv4 addresses.
     */
    public static final CidrAddress INET4_ANY_CIDR = new CidrAddress(Nets.INET4_ANY, 0);

    /**
     * The CIDR address representing all IPv6 addresses.
     */
    public static final CidrAddress INET6_ANY_CIDR = new CidrAddress(Nets.INET6_ANY, 0);

    private final InetAddress networkAddress;
    private final byte[] cachedBytes;
    private final int netmaskBits;
    private Inet4Address broadcast;
    private String toString;
    private int hashCode;

    private CidrAddress(final InetAddress networkAddress, final int netmaskBits) {
        this.networkAddress = networkAddress;
        cachedBytes = networkAddress.getAddress();
        this.netmaskBits = netmaskBits;
    }

    /**
     * Create a new CIDR address.
     *
     * @param networkAddress the network address (must not be {@code null})
     * @param netmaskBits    the netmask bits (0-32 for IPv4, or 0-128 for IPv6)
     * @return the CIDR address (not {@code null})
     */
    public static CidrAddress create(InetAddress networkAddress, int netmaskBits) {
        Preconditions.checkNotNullArgument(networkAddress, "networkAddress");
        Preconditions.checkArgument(netmaskBits >= 0, "the min value of netmaskBits is 0 ");
        int scopeId = Nets.getScopeId(networkAddress);
        if (networkAddress instanceof Inet4Address) {
            Preconditions.checkArgument(netmaskBits <= 32, "the max value of netmaskBits is 32 for IPV4");
            if (netmaskBits == 0) {
                return INET4_ANY_CIDR;
            }
        } else if (networkAddress instanceof Inet6Address) {
            Preconditions.checkArgument(netmaskBits <= 128, "the max value of netmaskBits is 128 for IPV6");
            if (netmaskBits == 0 && scopeId == 0) {
                return INET6_ANY_CIDR;
            }
        } else {
            throw new RuntimeException(StringTemplates.formatWithPlaceholder("illegal network address: {}", networkAddress));
        }
        final byte[] bytes = networkAddress.getAddress();
        maskBits0(bytes, netmaskBits);
        String name = Nets.toOptimalString(bytes);
        try {
            if (bytes.length == 4) {
                return new CidrAddress(InetAddress.getByAddress(name, bytes), netmaskBits);
            } else {
                return new CidrAddress(Inet6Address.getByAddress(name, bytes, scopeId), netmaskBits);
            }
        } catch (UnknownHostException e) {
            throw Throwables.wrapAsRuntimeException(e);
        }
    }

    /**
     * Create a new CIDR address.
     *
     * @param addressBytes the network address bytes (must not be {@code null}, must be 4 bytes for IPv4 or 16 bytes for IPv6)
     * @param netmaskBits  the netmask bits (0-32 for IPv4, or 0-128 for IPv6)
     * @return the CIDR address (not {@code null})
     */
    public static CidrAddress create(byte[] addressBytes, int netmaskBits) {
        return create(addressBytes, netmaskBits, true);
    }

    static CidrAddress create(byte[] addressBytes, int netmaskBits, boolean clone) {
        Preconditions.checkNotNullArgument(addressBytes, "networkAddress");
        Preconditions.checkArgument(netmaskBits >= 0, "the min value of netmaskBits is 0 ");
        final int length = addressBytes.length;
        if (length == 4) {
            Preconditions.checkArgument(netmaskBits <= 32, "the max value of netmaskBits is 32 for IPV4");
            if (netmaskBits == 0) {
                return INET4_ANY_CIDR;
            }
        } else if (length == 16) {
            Preconditions.checkArgument(netmaskBits <= 128, "the max value of netmaskBits is 128 for IPV6");
            if (netmaskBits == 0) {
                return INET6_ANY_CIDR;
            }
        } else {
            throw new RuntimeException(StringTemplates.formatWithPlaceholder("illegal network address length: {}", length));
        }
        final byte[] bytes = clone ? addressBytes.clone() : addressBytes;
        maskBits0(bytes, netmaskBits);
        String name = Nets.toOptimalString(bytes);
        try {
            return new CidrAddress(InetAddress.getByAddress(name, bytes), netmaskBits);
        } catch (UnknownHostException e) {
            throw Throwables.wrapAsRuntimeException(e);
        }
    }

    /**
     * Determine if this CIDR address matches the given address.
     *
     * @param address the address to test
     * @return {@code true} if the address matches, {@code false} otherwise
     */
    public boolean matches(InetAddress address) {
        Preconditions.checkNotNullArgument(address, "address");
        if (address instanceof Inet4Address) {
            return matches((Inet4Address) address);
        } else if (address instanceof Inet6Address) {
            return matches((Inet6Address) address);
        } else {
            throw new RuntimeException(StringTemplates.formatWithPlaceholder("illegal network address: {}", networkAddress));
        }
    }

    /**
     * Determine if this CIDR address matches the given address bytes.
     *
     * @param bytes the address bytes to test
     * @return {@code true} if the address bytes match, {@code false} otherwise
     */
    public boolean matches(byte[] bytes) {
        return matches(bytes, 0);
    }

    /**
     * Determine if this CIDR address matches the given address bytes.
     *
     * @param bytes   the address bytes to test
     * @param scopeId the scope ID, or 0 to match no scope
     * @return {@code true} if the address bytes match, {@code false} otherwise
     */
    public boolean matches(byte[] bytes, int scopeId) {
        Preconditions.checkNotNullArgument(bytes, "bytes");
        return cachedBytes.length == bytes.length && (getScopeId() == 0 || getScopeId() == scopeId) && bitsMatch(cachedBytes, bytes, netmaskBits);
    }

    /**
     * Determine if this CIDR address matches the given address.
     *
     * @param address the address to test
     * @return {@code true} if the address matches, {@code false} otherwise
     */
    public boolean matches(Inet4Address address) {
        Preconditions.checkNotNullArgument(address, "address");
        return networkAddress instanceof Inet4Address && bitsMatch(cachedBytes, address.getAddress(), netmaskBits);
    }

    /**
     * Determine if this CIDR address matches the given address.
     *
     * @param address the address to test
     * @return {@code true} if the address matches, {@code false} otherwise
     */
    public boolean matches(Inet6Address address) {
        Preconditions.checkNotNullArgument(address, "address");
        return networkAddress instanceof Inet6Address && bitsMatch(cachedBytes, address.getAddress(), netmaskBits)
                && (getScopeId() == 0 || getScopeId() == address.getScopeId());
    }

    /**
     * Determine if this CIDR address matches the given CIDR address.  This will be true only when the given CIDR
     * block is wholly enclosed by this one.
     *
     * @param address the address to test
     * @return {@code true} if the given block is enclosed by this one, {@code false} otherwise
     */
    public boolean matches(CidrAddress address) {
        Preconditions.checkNotNullArgument(address, "address");
        return netmaskBits <= address.netmaskBits && matches(address.cachedBytes)
                && (getScopeId() == 0 || getScopeId() == address.getScopeId());
    }

    /**
     * Get the network address.  The returned address has a resolved name consisting of the most compact valid string
     * representation of the network of this CIDR address.
     *
     * @return the network address (not {@code null})
     */
    public InetAddress getNetworkAddress() {
        return networkAddress;
    }

    /**
     * Get the broadcast address for this CIDR block.  If the block has no broadcast address (either because it is IPv6
     * or it is too small) then {@code null} is returned.
     *
     * @return the broadcast address for this CIDR block, or {@code null} if there is none
     */
    public Inet4Address getBroadcastAddress() {
        final Inet4Address broadcast = this.broadcast;
        if (broadcast == null) {
            final int netmaskBits = this.netmaskBits;
            if (netmaskBits >= 31) {
                // definitely IPv6 or too small
                return null;
            }
            // still maybe IPv6
            final byte[] cachedBytes = this.cachedBytes;
            if (cachedBytes.length == 4) {
                // calculate
                if (netmaskBits == 0) {
                    this.broadcast = Nets.INET4_BROADCAST;
                    return this.broadcast;
                } else {
                    final byte[] bytes = maskBits1(cachedBytes.clone(), netmaskBits);
                    try {
                        this.broadcast = (Inet4Address) InetAddress.getByAddress(Nets.toOptimalString(bytes), bytes);
                        return this.broadcast;
                    } catch (UnknownHostException e) {
                        throw Throwables.wrapAsRuntimeException(e);
                    }
                }
            }
            return null;
        }
        return broadcast;
    }

    /**
     * Get the netmask bits.  This will be in the range 0-32 for IPv4 addresses, and 0-128 for IPv6 addresses.
     *
     * @return the netmask bits
     */
    public int getNetmaskBits() {
        return netmaskBits;
    }

    /**
     * Get the match address scope ID (if it is an IPv6 address).
     *
     * @return the scope ID, or 0 if there is none or the address is an IPv4 address
     */
    public int getScopeId() {
        return Nets.getScopeId(getNetworkAddress());
    }

    public int compareTo(final CidrAddress other) {
        Preconditions.checkNotNullArgument(other, "other");
        if (this == other) return 0;
        return compareAddressBytesTo(other.cachedBytes, other.netmaskBits, other.getScopeId());
    }

    public int compareAddressBytesTo(final byte[] otherBytes, final int otherNetmaskBits, final int scopeId) {
        Preconditions.checkNotNullArgument(otherBytes, "bytes");
        final int otherLength = otherBytes.length;
        if (otherLength != 4 && otherLength != 16) {
            throw new RuntimeException(StringTemplates.formatWithPlaceholder("illegal network address length: {}", otherLength));
        }
        // IPv4 before IPv6
        final byte[] cachedBytes = this.cachedBytes;
        int res = signum(cachedBytes.length - otherLength);
        if (res != 0) return res;
        res = signum(scopeId - getScopeId());
        if (res != 0) return res;
        // sorted numerically with long matches coming later
        final int netmaskBits = this.netmaskBits;
        int commonPrefix = Maths.min(netmaskBits, otherNetmaskBits);
        // compare byte-wise as far as we can
        int i = 0;
        while (commonPrefix >= 8) {
            res = signum((cachedBytes[i] & 0xff) - (otherBytes[i] & 0xff));
            if (res != 0) return res;
            i++;
            commonPrefix -= 8;
        }
        while (commonPrefix > 0) {
            final int bit = 1 << commonPrefix;
            res = signum((cachedBytes[i] & bit) - (otherBytes[i] & bit));
            if (res != 0) return res;
            commonPrefix--;
        }
        // common prefix is a match; now the shortest mask wins
        return signum(netmaskBits - otherNetmaskBits);
    }

    public boolean equals(final Object obj) {
        return obj instanceof CidrAddress && equals((CidrAddress) obj);
    }

    public boolean equals(final CidrAddress obj) {
        return obj == this || obj != null && netmaskBits == obj.netmaskBits && Arrays.equals(cachedBytes, obj.cachedBytes);
    }

    public int hashCode() {
        int hashCode = this.hashCode;
        if (hashCode == 0) {
            hashCode = Maths.HashMaths.multiHashOrdered(netmaskBits, Arrays.hashCode(cachedBytes));
            if (hashCode == 0) {
                hashCode = 1;
            }
            this.hashCode = hashCode;
        }
        return hashCode;
    }

    public String toString() {
        final String toString = this.toString;
        if (toString == null) {
            final int scopeId = getScopeId();
            if (scopeId == 0) {
                this.toString = String.format("%s/%d", Nets.toOptimalString(cachedBytes), netmaskBits);
                return this.toString;
            } else {
                this.toString = String.format("%s%%%d/%d", Nets.toOptimalString(cachedBytes), scopeId, netmaskBits);
                return this.toString;
            }
        }
        return toString;
    }

    Object writeReplace() {
        return new Ser(cachedBytes, netmaskBits);
    }

    static final class Ser implements Serializable {
        private static final long serialVersionUID = 6367919693596329038L;

        final byte[] b;
        final int m;

        Ser(final byte[] b, final int m) {
            this.b = b;
            this.m = m;
        }

        Object readResolve() {
            return create(b, m, false);
        }
    }


    private static boolean bitsMatch(byte[] address, byte[] test, int bits) {
        final int length = address.length;
        assert length == test.length;
        // bytes are in big-endian form.
        int i = 0;
        while (bits >= 8 && i < length) {
            if (address[i] != test[i]) {
                return false;
            }
            i++;
            bits -= 8;
        }
        if (bits > 0) {
            assert bits < 8;
            int mask = 0xff << 8 - bits;
            if ((address[i] & 0xff & mask) != (test[i] & 0xff & mask)) {
                return false;
            }
        }
        return true;
    }

    private static byte[] maskBits0(byte[] address, int bits) {
        final int length = address.length;
        // bytes are in big-endian form.
        int i = 0;
        while (bits >= 8 && i < length) {
            i++;
            bits -= 8;
        }
        if (bits > 0) {
            assert bits < 8;
            int mask = 0xff << 8 - bits;
            address[i++] &= mask;
        }
        while (i < length) {
            address[i++] = 0;
        }
        return address;
    }

    private static byte[] maskBits1(byte[] address, int bits) {
        final int length = address.length;
        // bytes are in big-endian form.
        int i = 0;
        while (bits >= 8 && i < length) {
            i++;
            bits -= 8;
        }
        if (bits > 0) {
            assert bits < 8;
            int mask = 0xff >>> 8 - bits;
            address[i++] |= mask;
        }
        while (i < length) {
            address[i++] = (byte) 0xff;
        }
        return address;
    }
}
