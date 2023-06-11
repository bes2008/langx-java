package org.gmssl;


import com.jn.langx.util.Strings;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Files;
import com.jn.langx.util.jni.NativeLibraryUtil;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.net.URLs;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GmsslNativeLibs {

    private static void saveLibrary(JarEntry jarEntry) {
        File tmpFile = new File("./.gmssl");
        String entityName = jarEntry.getName();
        String fileName = entityName.substring(entityName.lastIndexOf("/") + 1);
        File jarFile = Files.newFile(tmpFile, entityName);
        if (!jarFile.getParentFile().exists()) {
            jarFile.getParentFile().mkdirs();
        }
        InputStream in = null;
        BufferedInputStream reader;
        FileOutputStream writer = null;
        try {
            in = GmSSL.class.getResourceAsStream(entityName);
            if (in == null) {
                in = GmSSL.class.getResourceAsStream("/" + entityName);
                if (null == in) {
                    return;
                }
            }
            GmsslNativeLibs.class.getResource(fileName);
            reader = new BufferedInputStream(in);
            writer = new FileOutputStream(jarFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, len);
            }
            addLibraryDir(Files.getCanonicalPath(jarFile.getParentFile()));
        } catch (IOException e) {
            Logger logger = Loggers.getLogger(GmsslNativeLibs.class);
            logger.error(e.getMessage(), e);
        } finally {
            IOs.close(in);
            IOs.close(writer);
        }

    }

    public static void addGmSSLDir(String path) throws IOException, ClassNotFoundException {
        Enumeration<URL> dir = Thread.currentThread().getContextClassLoader().getResources(path);
        String ext = NativeLibraryUtil.libExtension();
        while (dir.hasMoreElements()) {
            URL url = dir.nextElement();
            String protocol = url.getProtocol();
            if ("jar".equals(protocol)) {
                JarURLConnection jarURLConnection = URLs.openURL(url);
                JarFile jarFile = jarURLConnection.getJarFile();
                // 遍历Jar包
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String entityName = jarEntry.getName();
                    if (jarEntry.isDirectory() || !entityName.startsWith(path)) {
                        continue;
                    }
                    if (entityName.endsWith(ext)) {
                        saveLibrary(jarEntry);
                    }
                }
            } else if ("file".equals(protocol)) {
                File file = URLs.getFile(url);
                ;

                addLibraryDir(Files.getCanonicalPath(file));
            }
        }
    }

    public static void addLibraryDir(String libraryPath) throws IOException {
        if (Strings.isNotEmpty(libraryPath)) {
            try {
                Field field = Reflects.getDeclaredField(ClassLoader.class, "usr_paths");
                if (field == null) {
                    throw new IOException("Failedto get field handle to set library path");
                }
                field.setAccessible(true);
                String[] paths = (String[]) field.get(null);
                for (String path : paths) {
                    if (libraryPath.equals(path)) {
                        return;
                    }
                }

                String[] tmp = new String[paths.length + 1];
                System.arraycopy(paths, 0, tmp, 0, paths.length);
                tmp[paths.length] = libraryPath;
                field.set(null, tmp);
            } catch (IllegalAccessException e) {
                throw new IOException("Failedto get permissions to set library path");
            }
        }
    }

    private GmsslNativeLibs() {

    }
}