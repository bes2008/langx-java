package com.jn.langx.net.hosts;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A container of hosts file entries
 */
public final class HostsFileEntries {

    /**
     * Empty entries
     */
    static final HostsFileEntries EMPTY =
            new HostsFileEntries(
                    Collections.<String, Inet4Address>emptyMap(),
                    Collections.<String, Inet6Address>emptyMap());

    private final Map<String, Inet4Address> inet4Entries;
    private final Map<String, Inet6Address> inet6Entries;

    public HostsFileEntries(Map<String, Inet4Address> inet4Entries, Map<String, Inet6Address> inet6Entries) {
        this.inet4Entries = Collections.unmodifiableMap(new HashMap<String, Inet4Address>(inet4Entries));
        this.inet6Entries = Collections.unmodifiableMap(new HashMap<String, Inet6Address>(inet6Entries));
    }

    /**
     * The IPv4 entries
     *
     * @return the IPv4 entries
     */
    public Map<String, Inet4Address> inet4Entries() {
        return inet4Entries;
    }

    /**
     * The IPv6 entries
     *
     * @return the IPv6 entries
     */
    public Map<String, Inet6Address> inet6Entries() {
        return inet6Entries;
    }
}
