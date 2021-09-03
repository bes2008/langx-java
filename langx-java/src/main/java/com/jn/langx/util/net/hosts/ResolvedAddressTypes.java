package com.jn.langx.util.net.hosts;

/**
 * Defined resolved address types.
 */
public enum ResolvedAddressTypes {
    /**
     * Only resolve IPv4 addresses
     */
    IPV4_ONLY,
    /**
     * Only resolve IPv6 addresses
     */
    IPV6_ONLY,
    /**
     * Prefer IPv4 addresses over IPv6 ones
     */
    IPV4_PREFERRED,
    /**
     * Prefer IPv6 addresses over IPv4 ones
     */
    IPV6_PREFERRED
}
