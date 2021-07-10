package com.jn.langx.util.net;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Files;
import com.jn.langx.util.jar.multiplelevel.MultipleLevelURLStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class URLs {
    private static final Logger logger = LoggerFactory.getLogger(URLs.class);

    private static final Map<String,String> URL_PREFIX_MAPPING = new HashMap<String, String>();



    public static URL toURL(URI uri) {
        Preconditions.checkNotNull(uri);
        try {
            return uri.toURL();
        } catch (Throwable ex) {
            logger.warn("error url: {}", uri);
            return null;
        }
    }

    public static URL newURL(@NonNull String url) {
        Preconditions.checkNotNull(url);
        try {
            return new URL(url);
        } catch (Throwable ex) {
            logger.warn("error url: {}", url);
            return null;
        }
    }

    /**
     * Separator between JAR URL and file path within the JAR: "!/".
     */
    public static final String JAR_URL_SEPARATOR = "!/";


    public static final String URL_PREFIX_FILE = "file://";
    public static final String URL_PREFIX_JAR = "jar:";
    public static final String URL_PREFIX_JAR_FILE = URL_PREFIX_JAR + URL_PREFIX_FILE + "/";
    public static final String URL_PREFIX_FTP = "ftp://";
    public static final String URL_PREFIX_HTTP = "http://";
    public static final String URL_PREFIX_HTTPS = "https://";
    public static final String URL_PREFIX_MAIL = "mail:";
    public static final String URL_PREFIX_SMTP = "smtp://";


    public static final String URL_PROTOCOL_FILE = "file";


    public static final String URL_PROTOCOL_JAR = "jar";

    public static final String URL_PROTOCOL_WAR = "war";

    public static final String URL_PROTOCOL_ZIP = "zip";

    /**
     * URL protocol for an entry from a WebSphere jar file: "wsjar".
     */
    public static final String URL_PROTOCOL_WSJAR = "wsjar";

    /**
     * URL protocol for an entry from a JBoss jar file: "vfszip".
     */
    public static final String URL_PROTOCOL_VFSZIP = "vfszip";

    /**
     * URL protocol for a JBoss file system resource: "vfsfile".
     */
    public static final String URL_PROTOCOL_VFSFILE = "vfsfile";

    /**
     * URL protocol for a general JBoss VFS resource: "vfs".
     */
    public static final String URL_PROTOCOL_VFS = "vfs";

    /**
     * File extension for a regular jar file: ".jar".
     */
    public static final String JAR_FILE_SUFFIX = ".jar";

    /**
     * Special separator between WAR URL and jar part on Tomcat.
     */
    public static final String WAR_URL_SEPARATOR = "*/";

    static {
        URL_PREFIX_MAPPING.put("http", URL_PREFIX_HTTP);
        URL_PREFIX_MAPPING.put("https",URL_PREFIX_HTTPS);
        URL_PREFIX_MAPPING.put("ftp", URL_PREFIX_FTP);
        URL_PREFIX_MAPPING.put("jar", URL_PREFIX_JAR);
        URL_PREFIX_MAPPING.put("file", URL_PREFIX_FILE);
        URL_PREFIX_MAPPING.put("smtp", URL_PREFIX_SMTP);
        URL_PREFIX_MAPPING.put("mail", URL_PREFIX_MAIL);
    }

    public static String getUrlPrefix(String protocol){
        return URL_PREFIX_MAPPING.get(protocol);
    }

    public static boolean isFileURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_FILE.equals(protocol) || URL_PROTOCOL_VFSFILE.equals(protocol) ||
                URL_PROTOCOL_VFS.equals(protocol));
    }

    public static boolean isJarURL(URL url) {
        String protocol = url.getProtocol();
        return protocol.equals(URL_PROTOCOL_JAR) && url.getPath().contains(JAR_URL_SEPARATOR);
    }

    public static boolean isMultipleLevelJarURL(URL url) {
        if (isJarURL(url)) {
            String path = url.getPath();
            return path.indexOf(JAR_URL_SEPARATOR) != path.lastIndexOf(JAR_URL_SEPARATOR);
        }
        return false;
    }

    public static File getFile(URL url) {
        if (isFileURL(url)) {
            return new File(url.getFile());
        }
        return null;
    }

    public static boolean exists(URL url) {
        if (url == null) {
            return false;
        }
        try {
            if (isFileURL(url)) {
                return Files.exists(getFile(url));
            } else {
                if (isJarURL(url)) {
                    if (isMultipleLevelJarURL(url)) {
                        String urlString = url.toString();
                        url = new URL(null, urlString, new MultipleLevelURLStreamHandler());
                    }
                }

                // Try a URL connection content-length header
                URLConnection conn = url.openConnection();
                HttpURLConnection httpCon = (conn instanceof HttpURLConnection ? (HttpURLConnection) conn : null);
                if (httpCon != null) {
                    int code = httpCon.getResponseCode();
                    if (code == HttpURLConnection.HTTP_OK) {
                        return true;
                    } else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
                        return false;
                    }
                }
                if (URLConnections.getContentLengthLong(conn) > 0) {
                    return true;
                }
                if (URLConnections.getContentLength(conn) > 0) {
                    return true;
                }
                if (httpCon != null) {
                    // No HTTP OK status, and no content-length header: give up
                    httpCon.disconnect();
                    return false;
                } else {
                    return true;
                }
            }
        } catch (IOException ex) {
            return false;
        }
    }

    public static long getContentLength(URL url) {
        Preconditions.checkNotNull(url);
        try {
            if (URLs.isFileURL(url)) {
                return URLs.getFile(url).length();
            } else {
                if (isJarURL(url)) {
                    if (isMultipleLevelJarURL(url)) {
                        String urlString = url.toString();
                        url = new URL(null, urlString, new MultipleLevelURLStreamHandler());
                    }
                }
                URLConnection con = null;
                try {
                    con = url.openConnection();
                    long length = URLConnections.getContentLengthLong(con);
                    if (length < 0) {
                        length = URLConnections.getContentLength(con);
                    }
                    return length;
                } catch (IOException ex) {
                    // ignore it
                    return -1;
                } finally {
                    if (Emptys.isNotNull(con)) {
                        try {
                            IOs.close(con.getInputStream());
                        } catch (IOException ex) {
                            // ignore it
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            return -1;
        }
    }

    public static InputStream getInputStream(URL url) {
        try {
            URLConnection con = url.openConnection();
            return con.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }
}
