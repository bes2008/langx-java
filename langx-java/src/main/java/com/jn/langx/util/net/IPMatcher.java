package com.jn.langx.util.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A Flexible, immutable, IP address matcher.
 * <p>
 * Matches IP addresses against an internal database of IP addresses and IP
 * subnets.
 * <p>
 * Performs DNS lookups only when adding hosts names using {@link IPMatcher.Builder#addPatternOrHost(String)}.
 * If using host names, instances of this class should be regularly discarded
 * as any changes to DNS will not be picked up after the instance has been
 * created.
 * <p>
 * Example usage:
 * <pre>{@code
 *   final IPMatcher ipMatcher = IPMatcher.builder()
 *           .addPatternOrHost("example.org")
 *           .addPattern("192.168.1.0/24")
 *           .build();
 *   ipMatcher.match("192.168.1.13");}</pre>
 *
 *
 * @since 5.2.0
 */
public class IPMatcher {
    private final Set<Subnet> subnets;

    private IPMatcher(Set<Subnet> subnets) {
        this.subnets = new HashSet<Subnet>(subnets);
    }

    /**
     * Returns true if the given ip address matches one of the stored patterns.
     *
     * @param ipAddress ip address to match
     * @return true if the given ip address matches
     * @throws IllegalArgumentException if the argument is not a valid IP string literal
     */
    public boolean matches(String ipAddress) {
        return matches(Nets.forString(ipAddress));
    }

    /**
     * Returns true if the given ip address matches one of the stored patterns.
     *
     * @param ipAddress ip address to match
     * @return true if the given ip address matches
     */
    public boolean matches(InetAddress ipAddress) {
        for (Subnet subnet : subnets) {
            if (matches(subnet, ipAddress)) {
                return true;
            }
        }

        return false;
    }

    private boolean matches(Subnet subnet, InetAddress ipAddress) {
        // Code based on:
        // https://fisheye.springsource.org/browse/~raw,r=4de8b84b0d685c8adab5b3fb93c08bcf518708a9/spring-security/web/src/main/java/org/springframework/security/web/util/IpAddressMatcher.java

        int nMaskBits = subnet.getMask();
        int oddBits = nMaskBits % 8;
        int nMaskBytes = nMaskBits / 8 + (oddBits == 0 ? 0 : 1);
        byte[] mask = new byte[nMaskBytes];

        byte[] allowedIpAddress = subnet.getAddress();
        byte[] requestIpAddress = ipAddress.getAddress();

        // If IPs are not both IPv4 or IPv6, we can't compare
        if (allowedIpAddress.length != requestIpAddress.length) {
            return false;
        }

        Arrays.fill(mask, 0, oddBits == 0 ? mask.length : mask.length - 1, (byte) 0xFF);

        if (oddBits != 0) {
            int finalByte = (1 << oddBits) - 1;
            finalByte <<= 8 - oddBits;
            mask[mask.length - 1] = (byte) finalByte;
        }

        for (int i = 0; i < mask.length; i++) {
            if ((allowedIpAddress[i] & mask[i]) != (requestIpAddress[i] & mask[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidPatternOrHost(String patternOrHost) {
        if (patternOrHost == null || patternOrHost.trim().isEmpty()) {
            return false;
        }
        return !builder().addPatternOrHost(patternOrHost).subnets.isEmpty();
    }

    /**
     * Returns a new builder.
     *
     * @return new builder
     */
    public static IPMatcher.Builder builder() {
        return new IPMatcher.Builder();
    }

    /**
     * A builder for creating immutable IPMatcher instances.
     * <p>
     * Builder instances can be reused; it is safe to call {@link #build()}
     * multiple times.
     */
    public static class Builder {
        private final Set<Subnet> subnets = new HashSet<Subnet>();

        private Builder() {
        }

        /**
         * Adds a new pattern or host to be matched against. DNS lookup
         * is performed for hosts in order to get related IP addresses.
         * <p>
         * Patterns can be IPv4/IPv6 addresses, subnets in both asterisk and
         * CIDR notation, or hostnames. The following patterns are valid ip
         * addresses or subnets:
         * <pre>
         * 192.168.1.10
         * ::10
         * 192.168.1.*
         * 192.168.5.128/26
         * 0:0:0:7b::/64
         * </pre>
         * <p>
         * If a pattern is not a valid ip address or subnet, it will be treated as a hostname.
         *
         * @param patternOrHost pattern to be matched against
         * @throws IllegalArgumentException if the pattern is <tt>null</tt> or empty
         */
        public IPMatcher.Builder addPatternOrHost(String patternOrHost) {
            if (patternOrHost == null || patternOrHost.length() == 0) {
                throw new IllegalArgumentException("You cannot add an empty pattern or host");
            }

            try {
                subnets.add(Subnet.forPattern(patternOrHost));
            } catch (IllegalArgumentException e) {

                try {
                    for (InetAddress address : getAllByName(patternOrHost)) {
                        subnets.add(Subnet.forAddress(address));
                    }
                } catch (UnknownHostException e1) {
                    // Could not find any ip addresses for the hostname.
                }
            }

            return this;
        }

        /**
         * Adds a new pattern to be matched against.
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
         * @param pattern pattern to be matched against
         * @throws IllegalArgumentException if the pattern is <tt>null</tt> or empty
         */
        public IPMatcher.Builder addPattern(String pattern) {
            if (pattern == null || pattern.length() == 0) {
                throw new IllegalArgumentException("You cannot add an empty pattern");
            }

            subnets.add(Subnet.forPattern(pattern));

            return this;
        }

        /**
         * Adds a new subnet to be matched against.
         *
         * @param subnet non-null subnet to be matched against
         */
        public IPMatcher.Builder addSubnet(Subnet subnet) {
            if (subnet == null) {
                throw new NullPointerException("Subnet is null");
            }

            subnets.add(subnet);

            return this;
        }

        /**
         * Return a newly-created <tt>IPMatcher</tt> based on the contents of
         * the <tt>Builder</tt>.
         *
         * @return
         */
        public IPMatcher build() {
            return new IPMatcher(subnets);
        }

        // Abstracted out to allow mocking
        InetAddress[] getAllByName(String host) throws UnknownHostException {
            return InetAddress.getAllByName(host);
        }
    }
}
