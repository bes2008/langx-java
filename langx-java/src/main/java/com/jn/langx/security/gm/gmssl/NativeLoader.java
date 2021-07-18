package com.jn.langx.security.gm.gmssl;

import java.io.*;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class NativeLoader {
  private static void saveLibrary(JarEntry jarEntry) throws IOException, ClassNotFoundException {
    File tmpFile = new File("./.gmssl");
    String rootOutputPath = tmpFile.getAbsoluteFile() + File.separator;
    String entityName = jarEntry.getName();
    String fileName = entityName.substring(entityName.lastIndexOf("/") + 1);
    File jarFile = new File(rootOutputPath + File.separator + entityName);
    if (!jarFile.getParentFile().exists()) {
      jarFile.getParentFile().mkdirs();
    }
    InputStream in = null;
    BufferedInputStream reader = null;
    FileOutputStream writer = null;
    try {
      in = GmSSL.class.getResourceAsStream(entityName);
      if (in == null) {
        in = GmSSL.class.getResourceAsStream("/" + entityName);
        if (null == in) {
          return;
        }
      }
      NativeLoader.class.getResource(fileName);
      reader = new BufferedInputStream(in);
      writer = new FileOutputStream(jarFile);
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = reader.read(buffer)) != -1) {
        writer.write(buffer, 0, len);
      }
      addLibraryDir(jarFile.getParentFile().getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      if (in != null) {
        in.close();
      }
      if (writer != null) {
        writer.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean win() {
    String systemType = System.getProperty("os.name");
    return (systemType.toLowerCase().indexOf("win") != -1);
  }

  public static String libExt() {
    return win() ? ".dll" : ".so";
  }

  public static void addGmSSLDir(String path) throws IOException, ClassNotFoundException {
    Enumeration<URL> dir = Thread.currentThread().getContextClassLoader().getResources(path);
    String ext = libExt();
    while (dir.hasMoreElements()) {
      URL url = dir.nextElement();
      String protocol = url.getProtocol();
      if ("jar".equals(protocol)) {
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
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
        File file = new File(url.getPath());
        addLibraryDir(file.getAbsolutePath());
      }
    }
  }

  public static void addLibraryDir(String libraryPath) throws IOException {
    try {
      System.out.println("gmssl addLibraryDir:" + libraryPath);
      Field field = ClassLoader.class.getDeclaredField("usr_paths");
      field.setAccessible(true);
      String[] paths = (String[]) field.get(null);
      for (int i = 0; i < paths.length; i++) {
        if (libraryPath.equals(paths[i])) {
          return;
        }
      }

      String[] tmp = new String[paths.length + 1];
      System.arraycopy(paths, 0, tmp, 0, paths.length);
      tmp[paths.length] = libraryPath;
      field.set(null, tmp);
    } catch (IllegalAccessException e) {
      throw new IOException(
              "Failedto get permissions to set library path");
    } catch (NoSuchFieldException e) {
      throw new IOException(
              "Failedto get field handle to set library path");
    }
  }
}
