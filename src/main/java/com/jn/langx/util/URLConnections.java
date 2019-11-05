package com.jn.langx.util;

import java.net.URLConnection;
import java.util.Date;

public class URLConnections {
    public static long getHeaderFieldLong(URLConnection conn, String name, long defaultValue) {
        try {
            String value = conn.getHeaderField(name);
            return Long.parseLong(value);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public static int getHeaderFieldInt(URLConnection conn, String name, int defaultValue) {

        try {
            String value = conn.getHeaderField(name);
            return Integer.parseInt(value);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public static long getHeaderFieldDate(URLConnection conn, String name, long defaultValue) {
        try {
            String value = conn.getHeaderField(name);
            return Date.parse(value);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * Returns the value of the {@code content-length} header field.
     * <p>
     * <B>Note</B>: {@link #getContentLengthLong(URLConnection) getContentLengthLong()}
     * should be preferred over this method, since it returns a {@code long}
     * instead and is therefore more portable.</P>
     *
     * @return the content length of the resource that this connection's URL
     * references, {@code -1} if the content length is not known,
     * or if the content length is greater than Integer.MAX_VALUE.
     */
    public static int getContentLength(URLConnection conn) {
        long l = getContentLengthLong(conn);
        if (l > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) l;
    }

    /**
     * Returns the value of the {@code content-length} header field as a
     * long.
     *
     * @return the content length of the resource that this connection's URL
     * references, or {@code -1} if the content length is
     * not known.
     */
    public static long getContentLengthLong(URLConnection conn) {
        return getHeaderFieldLong(conn, "content-length", -1);
    }

    /**
     * Returns the value of the {@code content-type} header field.
     *
     * @return the content type of the resource that the URL references,
     * or {@code null} if not known.
     * @see java.net.URLConnection#getHeaderField(java.lang.String)
     */
    public static String getContentType(URLConnection conn) {
        return getHeaderField(conn, "content-type");
    }

    /**
     * Returns the value of the {@code content-encoding} header field.
     *
     * @return the content encoding of the resource that the URL references,
     * or {@code null} if not known.
     * @see java.net.URLConnection#getHeaderField(java.lang.String)
     */
    public static String getContentEncoding(URLConnection conn) {
        return getHeaderField(conn, "content-encoding");
    }

    /**
     * Returns the value of the {@code expires} header field.
     *
     * @return the expiration date of the resource that this URL references,
     * or 0 if not known. The value is the number of milliseconds since
     * January 1, 1970 GMT.
     * @see java.net.URLConnection#getHeaderField(java.lang.String)
     */
    public static long getExpiration(URLConnection conn) {
        return getHeaderFieldDate(conn, "expires", 0);
    }

    /**
     * Returns the value of the {@code date} header field.
     *
     * @return the sending date of the resource that the URL references,
     * or {@code 0} if not known. The value returned is the
     * number of milliseconds since January 1, 1970 GMT.
     * @see java.net.URLConnection#getHeaderField(java.lang.String)
     */
    public static long getDate(URLConnection conn) {
        return getHeaderFieldDate(conn, "date", 0);
    }

    /**
     * Returns the value of the {@code last-modified} header field.
     * The result is the number of milliseconds since January 1, 1970 GMT.
     *
     * @return the date the resource referenced by this
     * {@code URLConnection} was last modified, or 0 if not known.
     * @see java.net.URLConnection#getHeaderField(java.lang.String)
     */
    public static long getLastModified(URLConnection conn) {
        return getHeaderFieldDate(conn, "last-modified", 0);
    }

    public static String getHeaderField(URLConnection conn, String name) {
        return conn.getHeaderField(name);
    }

}
