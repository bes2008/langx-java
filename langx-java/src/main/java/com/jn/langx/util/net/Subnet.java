package com.jn.langx.util.net;

import java.net.InetAddress;

/**
 * IP subnet consisting of a network address and a network mask. Individual ip
 * addresses are represented as all ones subnets. Zero subnets are also
 * allowed.
 *
 * @since 5.2.0
 */
public class Subnet {
    private final InetAddress ipAddress;
    // >=0
    private final int mask;

    private Subnet(InetAddress ipAddress, int mask) {
        this.ipAddress = ipAddress;
        this.mask = mask;
    }

    /**
     * Creates a subnet of the given pattern.
     * <p>
     * Patterns can be IPv4/IPv6 addresses or subnets in both asterisk and CIDR
     * notation. Examples of valid patterns:
     * <pre>
     * 192.168.1.10
     * ::10
     * 192.168.1.*
     * 192.168.5.128/26
     * 0:0:0:7b::/64
     * </pre>
     *
     * @param pattern pattern to create a Subnet of
     * @return Subnet based on the given pattern
     */
    public static Subnet forPattern(String pattern) {
        final InetAddress ipAddress;
        final int mask;
        if (pattern.matches("(\\d+\\.)*\\*(\\.\\*)*")) // Asterisk subnet notation
        {
            String[] parts = pattern.split("\\.");
            int asteriskCount = 0;
            for (int i = parts.length - 1; i >= 0 && parts[i].equals("*"); --i) {
                ++asteriskCount;
            }
            ipAddress = Nets.forString(pattern.replace("*", "0"));
            mask = 32 - 8 * asteriskCount;
        } else if (pattern.contains("/")) // CIDR subnet notation
        {
            final String[] addressComponents = pattern.split("/", 2);
            ipAddress = Nets.forString(addressComponents[0]);
            mask = Integer.parseInt(addressComponents[1]);
        } else // Plain IP address (Treated as all ones subnet for matching purposes)
        {
            ipAddress = Nets.forString(pattern);
            mask = 8 * ipAddress.getAddress().length;
        }
        return new Subnet(ipAddress, mask);
    }

    /**
     * Creates an all ones subnet of the given ip address.
     *
     * @param ipAddress ipAddress to create a subnet of
     * @return all ones subnet based on the given ip address
     */
    public static Subnet forAddress(InetAddress ipAddress) {
        return new Subnet(ipAddress, ipAddress.getAddress().length * 8);
    }

    /**
     * Returns true if the given pattern is a valid subnet pattern.
     *
     * @param pattern pattern to validate
     * @return true if the given pattern is a valid subnet pattern
     */
    public static boolean isValidPattern(String pattern) {
        try {
            forPattern(pattern);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Returns the network address of this subnet.
     *
     * @return network address of this subnet
     */
    public byte[] getAddress() {
        return ipAddress.getAddress();
    }

    /**
     * Returns the network mask of this subnet.
     *
     * @return network mask of this subnet
     */
    public int getMask() {
        return mask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subnet subnet = (Subnet) o;

        if (mask != subnet.mask) return false;
        if (!ipAddress.equals(subnet.ipAddress)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ipAddress.hashCode();
        result = 31 * result + mask;
        return result;
    }
}
