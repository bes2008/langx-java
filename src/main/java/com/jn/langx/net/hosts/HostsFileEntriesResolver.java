package com.jn.langx.net.hosts;


import java.net.InetAddress;

/**
 * Resolves a hostname against the hosts file entries.
 */
public interface HostsFileEntriesResolver {

    /**
     * Default instance: a {@link DefaultHostsFileEntriesResolver}.
     */
    HostsFileEntriesResolver DEFAULT = new DefaultHostsFileEntriesResolver();

    /**
     * Resolve the address of a hostname against the entries in a hosts file, depending on some address types.
     * @param inetHost the hostname to resolve
     * @param resolvedAddressTypes the address types to resolve
     * @return the first matching address
     */
    InetAddress address(String inetHost, ResolvedAddressTypes resolvedAddressTypes);
}
