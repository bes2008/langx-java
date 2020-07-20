package com.jn.langx.util.net;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.properties.PropertiesAccessor;
import com.jn.langx.util.*;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;


public class Nets {
    private static final Logger logger = LoggerFactory.getLogger(Nets.class);
    /**
     * The {@link Inet4Address} that represents the IPv4 loopback address '127.0.0.1'
     */
    public static final Inet4Address LOCALHOST4;

    /**
     * The {@link Inet6Address} that represents the IPv6 loopback address '::1'
     */
    public static final Inet6Address LOCALHOST6;

    /**
     * The {@link InetAddress} that represents the loopback address. If IPv6 stack is available, it will refer to
     * {@link #LOCALHOST6}.  Otherwise, {@link #LOCALHOST4}.
     */
    public static final InetAddress LOCALHOST;

    /**
     * The loopback {@link NetworkInterface} of the current machine
     */
    public static final NetworkInterface LOOPBACK_IF;

    /**
     * The SOMAXCONN value of the current machine.  If failed to get the value,  {@code 200}  is used as a
     * default value for Windows or {@code 128} for others.
     */
    public static final int SOMAXCONN;

    /**
     * This defines how many words (represented as ints) are needed to represent an IPv6 address
     */
    private static final int IPV6_WORD_COUNT = 8;

    /**
     * The maximum number of characters for an IPV6 string with no scope
     */
    private static final int IPV6_MAX_CHAR_COUNT = 39;

    /**
     * Number of bytes needed to represent and IPV6 value
     */
    private static final int IPV6_BYTE_COUNT = 16;

    /**
     * Maximum amount of value adding characters in between IPV6 separators
     */
    private static final int IPV6_MAX_CHAR_BETWEEN_SEPARATOR = 4;

    /**
     * Minimum number of separators that must be present in an IPv6 string
     */
    private static final int IPV6_MIN_SEPARATORS = 2;

    /**
     * Maximum number of separators that must be present in an IPv6 string
     */
    private static final int IPV6_MAX_SEPARATORS = 8;

    /**
     * Maximum amount of value adding characters in between IPV4 separators
     */
    private static final int IPV4_MAX_CHAR_BETWEEN_SEPARATOR = 3;

    /**
     * Number of separators that must be present in an IPv4 string
     */
    private static final int IPV4_SEPARATORS = 3;

    /**
     * {@code true} if IPv4 should be used even if the system supports both IPv4 and IPv6.
     */
    private static final boolean IPV4_PREFERRED;

    /**
     * {@code true} if an IPv6 address should be preferred when a host has both an IPv4 address and an IPv6 address.
     */
    private static final boolean IPV6_ADDRESSES_PREFERRED;

    static {
        final PropertiesAccessor systemPropertiesAccessor = SystemPropertys.getAccessor();
        IPV4_PREFERRED = systemPropertiesAccessor.getBoolean("java.net.preferIPv4Stack", false);
        IPV6_ADDRESSES_PREFERRED =
                systemPropertiesAccessor.getBoolean("java.net.preferIPv6Addresses", false);
        logger.debug("-Djava.net.preferIPv4Stack: {}", IPV4_PREFERRED);
        logger.debug("-Djava.net.preferIPv6Addresses: {}", IPV6_ADDRESSES_PREFERRED);

        byte[] LOCALHOST4_BYTES = {127, 0, 0, 1};
        byte[] LOCALHOST6_BYTES = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};

        // Create IPv4 loopback address.
        Inet4Address localhost4 = null;
        try {
            localhost4 = (Inet4Address) InetAddress.getByAddress("localhost", LOCALHOST4_BYTES);
        } catch (Exception e) {
            // We should not get here as long as the length of the address is correct.
            Throwables.throwAsRuntimeException(e);
        }
        LOCALHOST4 = localhost4;

        // Create IPv6 loopback address.
        Inet6Address localhost6 = null;
        try {
            localhost6 = (Inet6Address) InetAddress.getByAddress("localhost", LOCALHOST6_BYTES);
        } catch (Exception e) {
            // We should not get here as long as the length of the address is correct.
            Throwables.throwAsRuntimeException(e);
        }
        LOCALHOST6 = localhost6;

        // Retrieve the list of available network interfaces.
        List<NetworkInterface> ifaces = new ArrayList<NetworkInterface>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    NetworkInterface iface = interfaces.nextElement();
                    // Use the interface with proper INET addresses only.
                    if (addressesFromNetworkInterface(iface).hasMoreElements()) {
                        ifaces.add(iface);
                    }
                }
            }
        } catch (SocketException e) {
            logger.warn("Failed to retrieve the list of available network interfaces", e);
        }

        // Find the first loopback interface available from its INET address (127.0.0.1 or ::1)
        // Note that we do not use NetworkInterface.isLoopback() in the first place because it takes long time
        // on a certain environment. (e.g. Windows with -Djava.net.preferIPv4Stack=true)
        NetworkInterface loopbackIface = null;
        InetAddress loopbackAddr = null;
        loop:
        for (NetworkInterface iface : ifaces) {
            for (Enumeration<InetAddress> i = addressesFromNetworkInterface(iface); i.hasMoreElements(); ) {
                InetAddress addr = i.nextElement();
                if (addr.isLoopbackAddress()) {
                    // Found
                    loopbackIface = iface;
                    loopbackAddr = addr;
                    break loop;
                }
            }
        }

        // If failed to find the loopback interface from its INET address, fall back to isLoopback().
        if (loopbackIface == null) {
            try {
                for (NetworkInterface iface : ifaces) {
                    if (iface.isLoopback()) {
                        Enumeration<InetAddress> i = addressesFromNetworkInterface(iface);
                        if (i.hasMoreElements()) {
                            // Found the one with INET address.
                            loopbackIface = iface;
                            loopbackAddr = i.nextElement();
                            break;
                        }
                    }
                }

                if (loopbackIface == null) {
                    logger.warn("Failed to find the loopback interface");
                }
            } catch (SocketException e) {
                logger.warn("Failed to find the loopback interface", e);
            }
        }

        if (loopbackIface != null) {
            // Found the loopback interface with an INET address.
            logger.debug(
                    "Loopback interface: {} ({}, {})",
                    loopbackIface.getName(), loopbackIface.getDisplayName(), loopbackAddr.getHostAddress());
        } else {
            // Could not find the loopback interface, but we can't leave LOCALHOST as null.
            // Use LOCALHOST6 or LOCALHOST4, preferably the IPv6 one.
            if (loopbackAddr == null) {
                try {
                    if (NetworkInterface.getByInetAddress(LOCALHOST6) != null) {
                        logger.debug("Using hard-coded IPv6 localhost address: {}", localhost6);
                        loopbackAddr = localhost6;
                    }
                } catch (Exception e) {
                    // Ignore
                } finally {
                    if (loopbackAddr == null) {
                        logger.debug("Using hard-coded IPv4 localhost address: {}", localhost4);
                        loopbackAddr = localhost4;
                    }
                }
            }
        }

        LOOPBACK_IF = loopbackIface;
        LOCALHOST = loopbackAddr;

        // As a SecurityManager may prevent reading the somaxconn file we wrap this in a privileged block.
        //
        // See https://github.com/netty/netty/issues/3680
        SOMAXCONN = AccessController.doPrivileged(new PrivilegedAction<Integer>() {
            @Override
            public Integer run() {
                // Determine the default somaxconn (server socket backlog) value of the platform.
                // The known defaults:
                // - Windows NT Server 4.0+: 200
                // - Linux and Mac OS X: 128
                int somaxconn = Platform.isWindows ? 200 : 128;
                File file = new File("/proc/sys/net/core/somaxconn");
                BufferedReader in = null;
                try {
                    // file.exists() may throw a SecurityException if a SecurityManager is used, so execute it in the
                    // try / catch block.
                    // See https://github.com/netty/netty/issues/4936
                    if (file.exists()) {
                        in = new BufferedReader(new FileReader(file));
                        somaxconn = Integer.parseInt(in.readLine());
                        if (logger.isDebugEnabled()) {
                            logger.debug("{}: {}", file, somaxconn);
                        }
                    } else {
                        // Try to get from sysctl
                        Integer tmp = null;
                        if (systemPropertiesAccessor.getBoolean("io.netty.net.somaxconn.trySysctl", false)) {
                            tmp = sysctlGetInt("kern.ipc.somaxconn");
                            if (tmp == null) {
                                tmp = sysctlGetInt("kern.ipc.soacceptqueue");
                                if (tmp != null) {
                                    somaxconn = tmp;
                                }
                            } else {
                                somaxconn = tmp;
                            }
                        }

                        if (tmp == null) {
                            logger.debug("Failed to get SOMAXCONN from sysctl and file {}. Default: {}", file,
                                    somaxconn);
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Failed to get SOMAXCONN from sysctl and file {}. Default: {}", file, somaxconn, e);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e) {
                            // Ignored.
                        }
                    }
                }
                return somaxconn;
            }
        });
    }

    /**
     * This will execute <a href ="https://www.freebsd.org/cgi/man.cgi?sysctl(8)">sysctl</a> with the {@code sysctlKey}
     * which is expected to return the numeric value for for {@code sysctlKey}.
     *
     * @param sysctlKey The key which the return value corresponds to.
     * @return The <a href ="https://www.freebsd.org/cgi/man.cgi?sysctl(8)">sysctl</a> value for {@code sysctlKey}.
     */
    private static Integer sysctlGetInt(String sysctlKey) throws IOException {
        Process process = new ProcessBuilder("sysctl", sysctlKey).start();
        try {
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            try {
                String line = br.readLine();
                if (line.startsWith(sysctlKey)) {
                    for (int i = line.length() - 1; i > sysctlKey.length(); --i) {
                        if (!Character.isDigit(line.charAt(i))) {
                            return Integer.valueOf(line.substring(i + 1));
                        }
                    }
                }
                return null;
            } finally {
                br.close();
            }
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * This is the maximum length of a domain name. But be aware that each label (parts separated by a dot) of the
     * domain name must be at most 63 characters long. This is verified by {@link IDN#toASCII(String)}.
     */
    private static final int MAX_DOMAIN_PART_LENGTH = 255;

    private static final String DOMAIN_CHARS_WITHOUT_DASH = "[a-z\u0080-\uFFFF0-9!#$%&'*+/=?^_`{|}~]";
    private static final String DOMAIN_LABEL = "(" + DOMAIN_CHARS_WITHOUT_DASH + "-*)*" + DOMAIN_CHARS_WITHOUT_DASH + "+";
    private static final String DOMAIN = DOMAIN_LABEL + "+(\\." + DOMAIN_LABEL + "+)*";

    private static final String IP_DOMAIN = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
    //IP v6 regex taken from http://stackoverflow.com/questions/53497/regular-expression-that-matches-valid-ipv6-addresses
    private static final String IP_V6_DOMAIN = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";

    /**
     * Regular expression for the domain part of an URL
     * <p>
     * A host string must be a domain string, an IPv4 address string, or "[", followed by an IPv6 address string,
     * followed by "]".
     */
    private static final Pattern DOMAIN_PATTERN = Pattern.compile(
            DOMAIN + "|\\[" + IP_V6_DOMAIN + "\\]", CASE_INSENSITIVE
    );

    /**
     * Regular expression for the domain part of an email address (everything after '@')
     */
    private static final Pattern EMAIL_DOMAIN_PATTERN = Pattern.compile(
            DOMAIN + "|\\[" + IP_DOMAIN + "\\]|" + "\\[IPv6:" + IP_V6_DOMAIN + "\\]", CASE_INSENSITIVE
    );

    /**
     * Checks the validity of the domain name used in an email. To be valid it should be either a valid host name, or an
     * IP address wrapped in [].
     *
     * @param domain domain to check for validity
     * @return {@code true} if the provided string is a valid domain, {@code false} otherwise
     */
    public static boolean isValidEmailDomainAddress(String domain) {
        return isValidDomainAddress(domain, EMAIL_DOMAIN_PATTERN);
    }

    /**
     * Checks validity of a domain name.
     *
     * @param domain the domain to check for validity
     * @return {@code true} if the provided string is a valid domain, {@code false} otherwise
     */
    public static boolean isValidDomainAddress(String domain) {
        return isValidDomainAddress(domain, DOMAIN_PATTERN);
    }

    private static boolean isValidDomainAddress(String domain, Pattern pattern) {
        // if we have a trailing dot the domain part we have an invalid email address.
        // the regular expression match would take care of this, but IDN.toASCII drops the trailing '.'
        if (domain.endsWith(".")) {
            return false;
        }

        Matcher matcher = pattern.matcher(domain);
        if (!matcher.matches()) {
            return false;
        }

        String asciiString;
        try {
            asciiString = IDN.toASCII(domain);
        } catch (IllegalArgumentException e) {
            return false;
        }

        if (asciiString.length() > MAX_DOMAIN_PART_LENGTH) {
            return false;
        }

        return true;
    }


    public static boolean isValidIpV6Address(String ip) {
        return isValidIpV6Address((CharSequence) ip);
    }

    public static boolean isValidIpV6Address(CharSequence ip) {
        int end = ip.length();
        if (end < 2) {
            return false;
        }

        // strip "[]"
        int start;
        char c = ip.charAt(0);
        if (c == '[') {
            end--;
            if (ip.charAt(end) != ']') {
                // must have a close ]
                return false;
            }
            start = 1;
            c = ip.charAt(1);
        } else {
            start = 0;
        }

        int colons;
        int compressBegin;
        if (c == ':') {
            // an IPv6 address can start with "::" or with a number
            if (ip.charAt(start + 1) != ':') {
                return false;
            }
            colons = 2;
            compressBegin = start;
            start += 2;
        } else {
            colons = 0;
            compressBegin = -1;
        }

        int wordLen = 0;
        loop:
        for (int i = start; i < end; i++) {
            c = ip.charAt(i);
            if (isValidHexChar(c)) {
                if (wordLen < 4) {
                    wordLen++;
                    continue;
                }
                return false;
            }

            switch (c) {
                case ':':
                    if (colons > 7) {
                        return false;
                    }
                    if (ip.charAt(i - 1) == ':') {
                        if (compressBegin >= 0) {
                            return false;
                        }
                        compressBegin = i - 1;
                    } else {
                        wordLen = 0;
                    }
                    colons++;
                    break;
                case '.':
                    // case for the last 32-bits represented as IPv4 x:x:x:x:x:x:d.d.d.d

                    // check a normal case (6 single colons)
                    if (compressBegin < 0 && colons != 6 ||
                            // a special case ::1:2:3:4:5:d.d.d.d allows 7 colons with an
                            // IPv4 ending, otherwise 7 :'s is bad
                            (colons == 7 && compressBegin >= start || colons > 7)) {
                        return false;
                    }

                    // Verify this address is of the correct structure to contain an IPv4 address.
                    // It must be IPv4-Mapped or IPv4-Compatible
                    // (see https://tools.ietf.org/html/rfc4291#section-2.5.5).
                    int ipv4Start = i - wordLen;
                    int j = ipv4Start - 2; // index of character before the previous ':'.
                    if (isValidIPv4MappedChar(ip.charAt(j))) {
                        if (!isValidIPv4MappedChar(ip.charAt(j - 1)) ||
                                !isValidIPv4MappedChar(ip.charAt(j - 2)) ||
                                !isValidIPv4MappedChar(ip.charAt(j - 3))) {
                            return false;
                        }
                        j -= 5;
                    }

                    for (; j >= start; --j) {
                        char tmpChar = ip.charAt(j);
                        if (tmpChar != '0' && tmpChar != ':') {
                            return false;
                        }
                    }

                    // 7 - is minimum IPv4 address length
                    int ipv4End = indexOf(ip, '%', ipv4Start + 7);
                    if (ipv4End < 0) {
                        ipv4End = end;
                    }
                    return isValidIpV4Address(ip, ipv4Start, ipv4End);
                case '%':
                    // strip the interface name/index after the percent sign
                    end = i;
                    break loop;
                default:
                    return false;
            }
        }

        // normal case without compression
        if (compressBegin < 0) {
            return colons == 7 && wordLen > 0;
        }

        return compressBegin + 2 == end ||
                // 8 colons is valid only if compression in start or end
                wordLen > 0 && (colons < 8 || compressBegin <= start);
    }

    private static boolean isValidIpV4Word(CharSequence word, int from, int toExclusive) {
        int len = toExclusive - from;
        char c0, c1, c2;
        if (len < 1 || len > 3 || (c0 = word.charAt(from)) < '0') {
            return false;
        }
        if (len == 3) {
            return (c1 = word.charAt(from + 1)) >= '0' &&
                    (c2 = word.charAt(from + 2)) >= '0' &&
                    (c0 <= '1' && c1 <= '9' && c2 <= '9' ||
                            c0 == '2' && c1 <= '5' && (c2 <= '5' || c1 < '5' && c2 <= '9'));
        }
        return c0 <= '9' && (len == 1 || isValidNumericChar(word.charAt(from + 1)));
    }

    private static boolean isValidHexChar(char c) {
        return c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f';
    }

    private static boolean isValidNumericChar(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isValidIPv4MappedChar(char c) {
        return c == 'f' || c == 'F';
    }

    private static boolean isValidIPv4MappedSeparators(byte b0, byte b1, boolean mustBeZero) {
        // We allow IPv4 Mapped (https://tools.ietf.org/html/rfc4291#section-2.5.5.1)
        // and IPv4 compatible (https://tools.ietf.org/html/rfc4291#section-2.5.5.1).
        // The IPv4 compatible is deprecated, but it allows parsing of plain IPv4 addressed into IPv6-Mapped addresses.
        return b0 == b1 && (b0 == 0 || !mustBeZero && b1 == -1);
    }

    private static boolean isValidIPv4Mapped(byte[] bytes, int currentIndex, int compressBegin, int compressLength) {
        final boolean mustBeZero = compressBegin + compressLength >= 14;
        return currentIndex <= 12 && currentIndex >= 2 && (!mustBeZero || compressBegin < 12) &&
                isValidIPv4MappedSeparators(bytes[currentIndex - 1], bytes[currentIndex - 2], mustBeZero) &&
                isZero(bytes, 0, currentIndex - 3);
    }

    private static boolean isZero(byte[] bytes, int startPos, int length) {
        if (length <= 0) {
            return true;
        }

        final int end = startPos + length;
        for (; startPos < end; ++startPos) {
            if (bytes[startPos] != 0) {
                return false;
            }
        }
        return true;

    }


    /**
     * Takes a {@link String} and parses it to see if it is a valid IPV4 address.
     *
     * @return true, if the string represents an IPV4 address in dotted
     * notation, false otherwise
     */
    public static boolean isValidIpV4Address(String ip) {
        return isValidIpV4Address(ip, 0, ip.length());
    }

    private static boolean isValidIpV4Address(CharSequence ip, int from, int toExcluded) {
        return isValidIpV4Address((String) ip, from, toExcluded);
    }

    @SuppressWarnings("DuplicateBooleanBranch")
    private static boolean isValidIpV4Address(String ip, int from, int toExcluded) {
        int len = toExcluded - from;
        int i;
        return len <= 15 && len >= 7 &&
                (i = ip.indexOf('.', from + 1)) > 0 && isValidIpV4Word(ip, from, i) &&
                (i = ip.indexOf('.', from = i + 2)) > 0 && isValidIpV4Word(ip, from - 1, i) &&
                (i = ip.indexOf('.', from = i + 2)) > 0 && isValidIpV4Word(ip, from - 1, i) &&
                isValidIpV4Word(ip, i + 1, toExcluded);
    }


    @SuppressWarnings("DuplicateBooleanBranch")
    private static boolean isValidIpV4Address0(CharSequence ip, int from, int toExcluded) {
        int len = toExcluded - from;
        int i;
        return len <= 15 && len >= 7 &&
                (i = indexOf(ip, '.', from + 1)) > 0 && isValidIpV4Word(ip, from, i) &&
                (i = indexOf(ip, '.', from = i + 2)) > 0 && isValidIpV4Word(ip, from - 1, i) &&
                (i = indexOf(ip, '.', from = i + 2)) > 0 && isValidIpV4Word(ip, from - 1, i) &&
                isValidIpV4Word(ip, i + 1, toExcluded);
    }


    public static boolean isValidPort(int port){
        if (port < 0 || port > 0xFFFF){
            return false;
        }
        return true;
    }

    /**
     * <p>Finds the first index in the {@code CharSequence} that matches the
     * specified character.</p>
     *
     * @param cs         the {@code CharSequence} to be processed, not null
     * @param searchChar the char to be searched for
     * @param start      the start index, negative starts at the string start
     * @return the index where the search char was found,
     * -1 if char {@code searchChar} is not found or {@code cs == null}
     */
    //-----------------------------------------------------------------------
    public static int indexOf(final CharSequence cs, final char searchChar, int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar, start);
        }
        if (cs == null) {
            return -1;
        }
        final int sz = cs.length();
        for (int i = start < 0 ? 0 : start; i < sz; i++) {
            if (cs.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the {@link Inet6Address} representation of a {@link CharSequence} IP address.
     * <p>
     * This method will treat all IPv4 type addresses as "IPv4 mapped" (see {@link #getByName(CharSequence, boolean)})
     *
     * @param ip {@link CharSequence} IP address to be converted to a {@link Inet6Address}
     * @return {@link Inet6Address} representation of the {@code ip} or {@code null} if not a valid IP address.
     */
    public static Inet6Address getByName(CharSequence ip) {
        return getByName(ip, true);
    }

    /**
     * Returns the {@link Inet6Address} representation of a {@link CharSequence} IP address.
     * <p>
     * The {@code ipv4Mapped} parameter specifies how IPv4 addresses should be treated.
     * "IPv4 mapped" format as
     * defined in <a href="http://tools.ietf.org/html/rfc4291#section-2.5.5">rfc 4291 section 2</a> is supported.
     *
     * @param ip         {@link CharSequence} IP address to be converted to a {@link Inet6Address}
     * @param ipv4Mapped <ul>
     *                   <li>{@code true} To allow IPv4 mapped inputs to be translated into {@link Inet6Address}</li>
     *                   <li>{@code false} Consider IPv4 mapped addresses as invalid.</li>
     *                   </ul>
     * @return {@link Inet6Address} representation of the {@code ip} or {@code null} if not a valid IP address.
     */
    public static Inet6Address getByName(CharSequence ip, boolean ipv4Mapped) {
        byte[] bytes = getIPv6ByName(ip, ipv4Mapped);
        if (bytes == null) {
            return null;
        }
        try {
            return Inet6Address.getByAddress(null, bytes, -1);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e); // Should never happen
        }
    }

    /**
     * Returns the byte array representation of a {@link CharSequence} IP address.
     * <p>
     * The {@code ipv4Mapped} parameter specifies how IPv4 addresses should be treated.
     * "IPv4 mapped" format as
     * defined in <a href="http://tools.ietf.org/html/rfc4291#section-2.5.5">rfc 4291 section 2</a> is supported.
     *
     * @param ip         {@link CharSequence} IP address to be converted to a {@link Inet6Address}
     * @param ipv4Mapped <ul>
     *                   <li>{@code true} To allow IPv4 mapped inputs to be translated into {@link Inet6Address}</li>
     *                   <li>{@code false} Consider IPv4 mapped addresses as invalid.</li>
     *                   </ul>
     * @return byte array representation of the {@code ip} or {@code null} if not a valid IP address.
     */
    private static byte[] getIPv6ByName(CharSequence ip, boolean ipv4Mapped) {
        final byte[] bytes = new byte[IPV6_BYTE_COUNT];
        final int ipLength = ip.length();
        int compressBegin = 0;
        int compressLength = 0;
        int currentIndex = 0;
        int value = 0;
        int begin = -1;
        int i = 0;
        int ipv6Separators = 0;
        int ipv4Separators = 0;
        int tmp;
        boolean needsShift = false;
        for (; i < ipLength; ++i) {
            final char c = ip.charAt(i);
            switch (c) {
                case ':':
                    ++ipv6Separators;
                    if (i - begin > IPV6_MAX_CHAR_BETWEEN_SEPARATOR ||
                            ipv4Separators > 0 || ipv6Separators > IPV6_MAX_SEPARATORS ||
                            currentIndex + 1 >= bytes.length) {
                        return null;
                    }
                    value <<= (IPV6_MAX_CHAR_BETWEEN_SEPARATOR - (i - begin)) << 2;

                    if (compressLength > 0) {
                        compressLength -= 2;
                    }

                    // The value integer holds at most 4 bytes from right (most significant) to left (least significant).
                    // The following bit shifting is used to extract and re-order the individual bytes to achieve a
                    // left (most significant) to right (least significant) ordering.
                    bytes[currentIndex++] = (byte) (((value & 0xf) << 4) | ((value >> 4) & 0xf));
                    bytes[currentIndex++] = (byte) ((((value >> 8) & 0xf) << 4) | ((value >> 12) & 0xf));
                    tmp = i + 1;
                    if (tmp < ipLength && ip.charAt(tmp) == ':') {
                        ++tmp;
                        if (compressBegin != 0 || (tmp < ipLength && ip.charAt(tmp) == ':')) {
                            return null;
                        }
                        ++ipv6Separators;
                        needsShift = ipv6Separators == 2 && value == 0;
                        compressBegin = currentIndex;
                        compressLength = bytes.length - compressBegin - 2;
                        ++i;
                    }
                    value = 0;
                    begin = -1;
                    break;
                case '.':
                    ++ipv4Separators;
                    tmp = i - begin; // tmp is the length of the current segment.
                    if (tmp > IPV4_MAX_CHAR_BETWEEN_SEPARATOR
                            || begin < 0
                            || ipv4Separators > IPV4_SEPARATORS
                            || (ipv6Separators > 0 && (currentIndex + compressLength < 12))
                            || i + 1 >= ipLength
                            || currentIndex >= bytes.length
                            || ipv4Separators == 1 &&
                            // We also parse pure IPv4 addresses as IPv4-Mapped for ease of use.
                            ((!ipv4Mapped || currentIndex != 0 && !isValidIPv4Mapped(bytes, currentIndex,
                                    compressBegin, compressLength)) ||
                                    (tmp == 3 && (!isValidNumericChar(ip.charAt(i - 1)) ||
                                            !isValidNumericChar(ip.charAt(i - 2)) ||
                                            !isValidNumericChar(ip.charAt(i - 3))) ||
                                            tmp == 2 && (!isValidNumericChar(ip.charAt(i - 1)) ||
                                                    !isValidNumericChar(ip.charAt(i - 2))) ||
                                            tmp == 1 && !isValidNumericChar(ip.charAt(i - 1))))) {
                        return null;
                    }
                    value <<= (IPV4_MAX_CHAR_BETWEEN_SEPARATOR - tmp) << 2;

                    // The value integer holds at most 3 bytes from right (most significant) to left (least significant).
                    // The following bit shifting is to restructure the bytes to be left (most significant) to
                    // right (least significant) while also accounting for each IPv4 digit is base 10.
                    begin = (value & 0xf) * 100 + ((value >> 4) & 0xf) * 10 + ((value >> 8) & 0xf);
                    if (begin < 0 || begin > 255) {
                        return null;
                    }
                    bytes[currentIndex++] = (byte) begin;
                    value = 0;
                    begin = -1;
                    break;
                default:
                    if (!isValidHexChar(c) || (ipv4Separators > 0 && !isValidNumericChar(c))) {
                        return null;
                    }
                    if (begin < 0) {
                        begin = i;
                    } else if (i - begin > IPV6_MAX_CHAR_BETWEEN_SEPARATOR) {
                        return null;
                    }
                    // The value is treated as a sort of array of numbers because we are dealing with
                    // at most 4 consecutive bytes we can use bit shifting to accomplish this.
                    // The most significant byte will be encountered first, and reside in the right most
                    // position of the following integer
                    value += Strings.decodeHexNibble(c) << ((i - begin) << 2);
                    break;
            }
        }

        final boolean isCompressed = compressBegin > 0;
        // Finish up last set of data that was accumulated in the loop (or before the loop)
        if (ipv4Separators > 0) {
            if (begin > 0 && i - begin > IPV4_MAX_CHAR_BETWEEN_SEPARATOR ||
                    ipv4Separators != IPV4_SEPARATORS ||
                    currentIndex >= bytes.length) {
                return null;
            }
            if (ipv6Separators == 0) {
                compressLength = 12;
            } else if (ipv6Separators >= IPV6_MIN_SEPARATORS &&
                    (!isCompressed && (ipv6Separators == 6 && ip.charAt(0) != ':') ||
                            isCompressed && (ipv6Separators < IPV6_MAX_SEPARATORS &&
                                    (ip.charAt(0) != ':' || compressBegin <= 2)))) {
                compressLength -= 2;
            } else {
                return null;
            }
            value <<= (IPV4_MAX_CHAR_BETWEEN_SEPARATOR - (i - begin)) << 2;

            // The value integer holds at most 3 bytes from right (most significant) to left (least significant).
            // The following bit shifting is to restructure the bytes to be left (most significant) to
            // right (least significant) while also accounting for each IPv4 digit is base 10.
            begin = (value & 0xf) * 100 + ((value >> 4) & 0xf) * 10 + ((value >> 8) & 0xf);
            if (begin < 0 || begin > 255) {
                return null;
            }
            bytes[currentIndex++] = (byte) begin;
        } else {
            tmp = ipLength - 1;
            if (begin > 0 && i - begin > IPV6_MAX_CHAR_BETWEEN_SEPARATOR ||
                    ipv6Separators < IPV6_MIN_SEPARATORS ||
                    !isCompressed && (ipv6Separators + 1 != IPV6_MAX_SEPARATORS ||
                            ip.charAt(0) == ':' || ip.charAt(tmp) == ':') ||
                    isCompressed && (ipv6Separators > IPV6_MAX_SEPARATORS ||
                            (ipv6Separators == IPV6_MAX_SEPARATORS &&
                                    (compressBegin <= 2 && ip.charAt(0) != ':' ||
                                            compressBegin >= 14 && ip.charAt(tmp) != ':'))) ||
                    currentIndex + 1 >= bytes.length ||
                    begin < 0 && ip.charAt(tmp - 1) != ':' ||
                    compressBegin > 2 && ip.charAt(0) == ':') {
                return null;
            }
            if (begin >= 0 && i - begin <= IPV6_MAX_CHAR_BETWEEN_SEPARATOR) {
                value <<= (IPV6_MAX_CHAR_BETWEEN_SEPARATOR - (i - begin)) << 2;
            }
            // The value integer holds at most 4 bytes from right (most significant) to left (least significant).
            // The following bit shifting is used to extract and re-order the individual bytes to achieve a
            // left (most significant) to right (least significant) ordering.
            bytes[currentIndex++] = (byte) (((value & 0xf) << 4) | ((value >> 4) & 0xf));
            bytes[currentIndex++] = (byte) ((((value >> 8) & 0xf) << 4) | ((value >> 12) & 0xf));
        }

        i = currentIndex + compressLength;
        if (needsShift || i >= bytes.length) {
            // Right shift array
            if (i >= bytes.length) {
                ++compressBegin;
            }
            for (i = currentIndex; i < bytes.length; ++i) {
                for (begin = bytes.length - 1; begin >= compressBegin; --begin) {
                    bytes[begin] = bytes[begin - 1];
                }
                bytes[begin] = 0;
                ++compressBegin;
            }
        } else {
            // Selectively move elements
            for (i = 0; i < compressLength; ++i) {
                begin = i + compressBegin;
                currentIndex = begin + compressLength;
                if (currentIndex < bytes.length) {
                    bytes[currentIndex] = bytes[begin];
                    bytes[begin] = 0;
                } else {
                    break;
                }
            }
        }

        if (ipv4Separators > 0) {
            // We only support IPv4-Mapped addresses [1] because IPv4-Compatible addresses are deprecated [2].
            // [1] https://tools.ietf.org/html/rfc4291#section-2.5.5.2
            // [2] https://tools.ietf.org/html/rfc4291#section-2.5.5.1
            bytes[10] = bytes[11] = (byte) 0xff;
        }

        return bytes;
    }

    /**
     * Returns the {@link String} representation of an {@link InetSocketAddress}.
     * <p>
     * The output does not include Scope ID.
     *
     * @param addr {@link InetSocketAddress} to be converted to an address string
     * @return {@code String} containing the text-formatted IP address
     */
    public static String toSocketAddressString(InetSocketAddress addr) {
        String port = String.valueOf(addr.getPort());
        final StringBuilder sb;

        if (addr.isUnresolved()) {
            String hostname = getHostname(addr);
            sb = newSocketAddressStringBuilder(hostname, port, !isValidIpV6Address(hostname));
        } else {
            InetAddress address = addr.getAddress();
            String hostString = toAddressString(address);
            sb = newSocketAddressStringBuilder(hostString, port, address instanceof Inet4Address);
        }
        return sb.append(':').append(port).toString();
    }

    /**
     * Returns the {@link String} representation of a host port combo.
     */
    public static String toSocketAddressString(String host, int port) {
        String portStr = String.valueOf(port);
        return newSocketAddressStringBuilder(
                host, portStr, !isValidIpV6Address(host)).append(':').append(portStr).toString();
    }

    private static StringBuilder newSocketAddressStringBuilder(String host, String port, boolean ipv4) {
        int hostLen = host.length();
        if (ipv4) {
            // Need to include enough space for hostString:port.
            return new StringBuilder(hostLen + 1 + port.length()).append(host);
        }
        // Need to include enough space for [hostString]:port.
        StringBuilder stringBuilder = new StringBuilder(hostLen + 3 + port.length());
        if (hostLen > 1 && host.charAt(0) == '[' && host.charAt(hostLen - 1) == ']') {
            return stringBuilder.append(host);
        }
        return stringBuilder.append('[').append(host).append(']');
    }

    /**
     * Returns the {@link String} representation of an {@link InetAddress}.
     * <ul>
     * <li>Inet4Address results are identical to {@link InetAddress#getHostAddress()}</li>
     * <li>Inet6Address results adhere to
     * <a href="http://tools.ietf.org/html/rfc5952#section-4">rfc 5952 section 4</a></li>
     * </ul>
     * <p>
     * The output does not include Scope ID.
     *
     * @param ip {@link InetAddress} to be converted to an address string
     * @return {@code String} containing the text-formatted IP address
     */
    public static String toAddressString(InetAddress ip) {
        return toAddressString(ip, false);
    }

    /**
     * Returns the {@link String} representation of an {@link InetAddress}.
     * <ul>
     * <li>Inet4Address results are identical to {@link InetAddress#getHostAddress()}</li>
     * <li>Inet6Address results adhere to
     * <a href="http://tools.ietf.org/html/rfc5952#section-4">rfc 5952 section 4</a> if
     * {@code ipv4Mapped} is false.  If {@code ipv4Mapped} is true then "IPv4 mapped" format
     * from <a href="http://tools.ietf.org/html/rfc4291#section-2.5.5">rfc 4291 section 2</a> will be supported.
     * The compressed result will always obey the compression rules defined in
     * <a href="http://tools.ietf.org/html/rfc5952#section-4">rfc 5952 section 4</a></li>
     * </ul>
     * <p>
     * The output does not include Scope ID.
     *
     * @param ip         {@link InetAddress} to be converted to an address string
     * @param ipv4Mapped <ul>
     *                                                                                                             <li>{@code true} to stray from strict rfc 5952 and support the "IPv4 mapped" format
     *                                                                                                             defined in <a href="http://tools.ietf.org/html/rfc4291#section-2.5.5">rfc 4291 section 2</a> while still
     *                                                                                                             following the updated guidelines in
     *                                                                                                             <a href="http://tools.ietf.org/html/rfc5952#section-4">rfc 5952 section 4</a></li>
     *                                                                                                             <li>{@code false} to strictly follow rfc 5952</li>
     *                                                                                                             </ul>
     * @return {@code String} containing the text-formatted IP address
     */
    public static String toAddressString(InetAddress ip, boolean ipv4Mapped) {
        if (ip instanceof Inet4Address) {
            return ip.getHostAddress();
        }
        if (!(ip instanceof Inet6Address)) {
            throw new IllegalArgumentException("Unhandled type: " + ip);
        }

        return toAddressString(ip.getAddress(), 0, ipv4Mapped);
    }

    private static String toAddressString(byte[] bytes, int offset, boolean ipv4Mapped) {
        final int[] words = new int[IPV6_WORD_COUNT];
        int i;
        final int end = offset + words.length;
        for (i = offset; i < end; ++i) {
            words[i] = ((bytes[i << 1] & 0xff) << 8) | (bytes[(i << 1) + 1] & 0xff);
        }

        // Find longest run of 0s, tie goes to first found instance
        int currentStart = -1;
        int currentLength;
        int shortestStart = -1;
        int shortestLength = 0;
        for (i = 0; i < words.length; ++i) {
            if (words[i] == 0) {
                if (currentStart < 0) {
                    currentStart = i;
                }
            } else if (currentStart >= 0) {
                currentLength = i - currentStart;
                if (currentLength > shortestLength) {
                    shortestStart = currentStart;
                    shortestLength = currentLength;
                }
                currentStart = -1;
            }
        }
        // If the array ends on a streak of zeros, make sure we account for it
        if (currentStart >= 0) {
            currentLength = i - currentStart;
            if (currentLength > shortestLength) {
                shortestStart = currentStart;
                shortestLength = currentLength;
            }
        }
        // Ignore the longest streak if it is only 1 long
        if (shortestLength == 1) {
            shortestLength = 0;
            shortestStart = -1;
        }

        // Translate to string taking into account longest consecutive 0s
        final int shortestEnd = shortestStart + shortestLength;
        final StringBuilder b = new StringBuilder(IPV6_MAX_CHAR_COUNT);
        if (shortestEnd < 0) { // Optimization when there is no compressing needed
            b.append(Integer.toHexString(words[0]));
            for (i = 1; i < words.length; ++i) {
                b.append(':');
                b.append(Integer.toHexString(words[i]));
            }
        } else { // General case that can handle compressing (and not compressing)
            // Loop unroll the first index (so we don't constantly check i==0 cases in loop)
            final boolean isIpv4Mapped;
            if (inRangeEndExclusive(0, shortestStart, shortestEnd)) {
                b.append("::");
                isIpv4Mapped = ipv4Mapped && (shortestEnd == 5 && words[5] == 0xffff);
            } else {
                b.append(Integer.toHexString(words[0]));
                isIpv4Mapped = false;
            }
            for (i = 1; i < words.length; ++i) {
                if (!inRangeEndExclusive(i, shortestStart, shortestEnd)) {
                    if (!inRangeEndExclusive(i - 1, shortestStart, shortestEnd)) {
                        // If the last index was not part of the shortened sequence
                        if (!isIpv4Mapped || i == 6) {
                            b.append(':');
                        } else {
                            b.append('.');
                        }
                    }
                    if (isIpv4Mapped && i > 5) {
                        b.append(words[i] >> 8);
                        b.append('.');
                        b.append(words[i] & 0xff);
                    } else {
                        b.append(Integer.toHexString(words[i]));
                    }
                } else if (!inRangeEndExclusive(i - 1, shortestStart, shortestEnd)) {
                    // If we are in the shortened sequence and the last index was not
                    b.append("::");
                }
            }
        }

        return b.toString();
    }

    /**
     * Returns InetSocketAddress#getHostString() if Java >= 7,
     * or {@link InetSocketAddress#getHostName()} otherwise.
     *
     * @param addr The address
     * @return the host string
     */
    public static String getHostname(InetSocketAddress addr) {

        if (Platform.JAVA_VERSION_INT < 7) {
            return addr.getHostName();
        } else {
            // addr.getHostString()
            try {
                return Reflects.invokePublicMethod(addr, "getHostString", null, null, true, true);
            } catch (Throwable ex) {
                return addr.getHostName();
            }
        }
    }

    /**
     * Does a range check on {@code value} if is within {@code start} (inclusive) and {@code end} (exclusive).
     *
     * @param value The value to checked if is within {@code start} (inclusive) and {@code end} (exclusive)
     * @param start The start of the range (inclusive)
     * @param end   The end of the range (exclusive)
     * @return <ul>
     * <li>{@code true} if {@code value} if is within {@code start} (inclusive) and {@code end} (exclusive)</li>
     * <li>{@code false} otherwise</li>
     * </ul>
     */
    private static boolean inRangeEndExclusive(int value, int start, int end) {
        return value >= start && value < end;
    }


    public static Enumeration<InetAddress> addressesFromNetworkInterface(final NetworkInterface intf) {
        return AccessController.doPrivileged(new PrivilegedAction<Enumeration<InetAddress>>() {
            @Override
            public Enumeration<InetAddress> run() {
                return intf.getInetAddresses();
            }
        });
    }

    /**
     * Creates an byte[] based on an ipAddressString. No error handling is performed here.
     */
    public static byte[] createByteArrayFromIpAddressString(String ipAddressString) {

        if (isValidIpV4Address(ipAddressString)) {
            return validIpV4ToBytes(ipAddressString);
        }

        if (isValidIpV6Address(ipAddressString)) {
            if (ipAddressString.charAt(0) == '[') {
                ipAddressString = ipAddressString.substring(1, ipAddressString.length() - 1);
            }

            int percentPos = ipAddressString.indexOf('%');
            if (percentPos >= 0) {
                ipAddressString = ipAddressString.substring(0, percentPos);
            }

            return getIPv6ByName(ipAddressString, true);
        }
        return null;
    }

    // visible for tests
    static byte[] validIpV4ToBytes(String ip) {
        int i;
        return new byte[]{
                ipv4WordToByte(ip, 0, i = ip.indexOf('.', 1)),
                ipv4WordToByte(ip, i + 1, i = ip.indexOf('.', i + 2)),
                ipv4WordToByte(ip, i + 1, i = ip.indexOf('.', i + 2)),
                ipv4WordToByte(ip, i + 1, ip.length())
        };
    }

    private static byte ipv4WordToByte(String ip, int from, int toExclusive) {
        int ret = decimalDigit(ip, from);
        from++;
        if (from == toExclusive) {
            return (byte) ret;
        }
        ret = ret * 10 + decimalDigit(ip, from);
        from++;
        if (from == toExclusive) {
            return (byte) ret;
        }
        return (byte) (ret * 10 + decimalDigit(ip, from));
    }

    private static int decimalDigit(String str, int pos) {
        return str.charAt(pos) - '0';
    }

    /**
     * Returns a {@link Map} of {@link InetAddress} per {@link NetworkInterface}.
     */
    public static Map<String, Set<InetAddress>> getNetworkInterfaceAddresses() {
        //JVM returns interfaces in a non-predictable order, so to make this more predictable
        //let's have them sort by interface name (by using a TreeMap).
        return getNetworkInterfaceAddresses(new Predicate<NetworkInterface>() {
            @Override
            public boolean test(NetworkInterface iface) {
                try {
                    return iface.isUp() && !iface.isLoopback() && !iface.isPointToPoint();
                } catch (SocketException ex) {
                    return false;
                }
            }
        }, new Predicate<InetAddress>() {
            @Override
            public boolean test(InetAddress ia) {
                return !ia.isLoopbackAddress() && !ia.getHostAddress().contains(":");
            }
        });
    }

    public static Map<String, Set<InetAddress>> getNetworkInterfaceAddresses(
            @Nullable Predicate<NetworkInterface> networkInterfacePredicate,
            @Nullable Predicate<InetAddress> inetAddressPredicate) {
        //JVM returns interfaces in a non-predictable order, so to make this more predictable
        //let's have them sort by interface name (by using a TreeMap).
        Map<String, Set<InetAddress>> interfaceAddressMap = new TreeMap<String, Set<InetAddress>>();
        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            networkInterfacePredicate = networkInterfacePredicate == null ? Functions.<NetworkInterface>truePredicate() : networkInterfacePredicate;
            inetAddressPredicate = inetAddressPredicate == null ? Functions.<InetAddress>truePredicate() : inetAddressPredicate;
            while (ifaces.hasMoreElements()) {
                NetworkInterface iface = ifaces.nextElement();
                //We only care about usable non-loopback interfaces.
                if (networkInterfacePredicate.test(iface)) {
                    String name = iface.getName();
                    Enumeration<InetAddress> ifaceAddresses = iface.getInetAddresses();
                    while (ifaceAddresses.hasMoreElements()) {
                        InetAddress ia = ifaceAddresses.nextElement();
                        //We want to filter out mac addresses
                        if (inetAddressPredicate.test(ia)) {
                            Set<InetAddress> addresses = interfaceAddressMap.get(name);
                            if (addresses == null) {
                                addresses = new LinkedHashSet<InetAddress>();
                            }
                            addresses.add(ia);
                            interfaceAddressMap.put(name, addresses);
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            //noop
        }
        return interfaceAddressMap;
    }

    public static List<NetworkInterface> getNetworkInterfaces() {
        return getNetworkInterfaces(null);
    }

    public static List<NetworkInterface> getNetworkInterfaces(Predicate<NetworkInterface> predicate) {
        predicate = predicate == null ? Functions.<NetworkInterface>truePredicate() : predicate;
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            return Pipeline.<NetworkInterface>of(enumeration).filter(predicate).asList();
        } catch (SocketException ex) {
            // ignore it
        }
        return Collects.emptyArrayList();
    }

    @Nullable
    public static String getMac(@NonNull NetworkInterface networkInterface) throws SocketException {
        byte[] macBytes = networkInterface.getHardwareAddress();
        if (Emptys.isNotEmpty(macBytes)) {
            return getMac(macBytes);
        }
        return null;
    }

    public static String getMac(@NonNull byte[] bytes) {
        Preconditions.checkArgument(Emptys.isNotEmpty(bytes), "invalid mac bytes");
        StringBuilder mac = new StringBuilder();
        byte currentByte;
        boolean first = false;
        for (byte b : bytes) {
            if (first) {
                mac.append("-");
            }
            currentByte = (byte) ((b & 240) >> 4);
            mac.append(Integer.toHexString(currentByte));
            currentByte = (byte) (b & 15);
            mac.append(Integer.toHexString(currentByte));
            first = true;
        }
        return mac.toString().toUpperCase();
    }

    /**
     * Returns a {@link Set} of {@link InetAddress} that are non-loopback or mac.
     */
    public static Set<InetAddress> getAddresses() {
        Set<InetAddress> allAddresses = new LinkedHashSet<InetAddress>();
        Map<String, Set<InetAddress>> interfaceAddressMap = getNetworkInterfaceAddresses();
        for (Map.Entry<String, Set<InetAddress>> entry : interfaceAddressMap.entrySet()) {
            Set<InetAddress> addresses = entry.getValue();
            if (!addresses.isEmpty()) {
                for (InetAddress address : addresses) {
                    allAddresses.add(address);
                }
            }
        }
        return allAddresses;
    }


    /**
     * Chooses one of the available {@link InetAddress} based on the specified preference.
     */
    private static InetAddress chooseAddress() throws UnknownHostException {
        Set<InetAddress> addresses = getAddresses();
        if (addresses.contains(InetAddress.getLocalHost())) {
            //Then if local host address is not bound to a loop-back interface, use it.
            return InetAddress.getLocalHost();
        } else if (addresses != null && !addresses.isEmpty()) {
            //else return the first available addrress
            return addresses.toArray(new InetAddress[addresses.size()])[0];
        } else {
            //else we are forcedt to use the localhost address.
            return InetAddress.getLocalHost();
        }
    }

    /**
     * Returns the local hostname. It loops through the network interfaces and returns the first non loopback hostname
     */
    public static String getLocalHostName() throws UnknownHostException {
        return chooseAddress().getHostName();
    }

    /**
     * Returns the local IP. It loops through the network interfaces and returns the first non loopback address
     */
    public static String getLocalIp() throws UnknownHostException {
        return chooseAddress().getHostAddress();
    }

}
