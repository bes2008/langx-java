package com.jn.langx.classpath;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * 代表一个资源路径，该路径可以是一个 目录, jar, zip 等形式。也可以是一个 class文件的全路径
 */
public class ClassPath implements Serializable {
    private static final String JRT_FS = "jrt-fs.jar";

    private static ClassPath SYSTEM_CLASS_PATH = null;

    private PathEntry[] paths;
    private String class_path;

    public static ClassPath getSystemClassPath() {
        if (SYSTEM_CLASS_PATH == null) {
            SYSTEM_CLASS_PATH = new ClassPath();
        }
        return SYSTEM_CLASS_PATH;
    }

    /**
     * Search for classes in given path.
     */
    public ClassPath(String class_path) {
        this.class_path = class_path;

        ArrayList<PathEntry> vec = new ArrayList<PathEntry>();

        for (StringTokenizer tok = new StringTokenizer(class_path, System.getProperty("path.separator")); tok
                .hasMoreTokens(); ) {
            String path = tok.nextToken();

            if (!path.equals("")) {
                File file = new File(path);

                try {
                    if (file.exists()) {
                        if (file.isDirectory()) {
                            vec.add(new Dir(path));
                        } else if (file.getName().endsWith("jrt-fs.jar")) { // TODO a bit crude...
                            //    vec.add(new JImage());
                        } else {
                            vec.add(new Zip(new ZipFile(file)));
                        }
                    }
                } catch (IOException e) {
                    System.err.println("CLASSPATH component " + file + ": " + e);
                }
            }
        }

        paths = new PathEntry[vec.size()];
        vec.toArray(paths);
    }

    /**
     * Search for classes in CLASSPATH.
     *
     * @deprecated Use SYSTEM_CLASS_PATH constant
     */
    @Deprecated
    public ClassPath() {
        this(getClassPath());
    }

    /**
     * @return used class path string
     */
    @Override
    public String toString() {
        return class_path;
    }

    @Override
    public int hashCode() {
        return class_path.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ClassPath) {
            return class_path.equals(((ClassPath) o).class_path);
        }

        return false;
    }

    private static final void getPathComponents(String path, ArrayList<String> list) {
        if (path != null) {
            StringTokenizer tok = new StringTokenizer(path, File.pathSeparator);

            while (tok.hasMoreTokens()) {
                String name = tok.nextToken();
                File file = new File(name);

                if (file.exists())
                    list.add(name);
            }
        }
    }

    /**
     * Checks for class path components in the following properties:
     * "java.class.path", "sun.boot.class.path", "java.ext.dirs"
     *
     * @return class path as used by default by BCEL
     */
    public static final String getClassPath() {
        String class_path = System.getProperty("java.class.path");
        String boot_path = System.getProperty("sun.boot.class.path");
        String ext_path = System.getProperty("java.ext.dirs");
        String vm_version = System.getProperty("java.version");

        ArrayList<String> list = new ArrayList<String>();

        getPathComponents(class_path, list);
        getPathComponents(boot_path, list);

        ArrayList<String> dirs = new ArrayList<String>();
        getPathComponents(ext_path, dirs);

        for (Iterator<String> e = dirs.iterator(); e.hasNext(); ) {
            File ext_dir = new File(e.next());
            String[] extensions = ext_dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    name = name.toLowerCase();
                    return name.endsWith(".zip") || name.endsWith(".jar");
                }
            });

            if (extensions != null)
                for (int i = 0; i < extensions.length; i++)
                    list.add(ext_dir.toString() + File.separatorChar + extensions[i]);
        }

        StringBuffer buf = new StringBuffer();

        for (Iterator<String> e = list.iterator(); e.hasNext(); ) {
            buf.append(e.next());

            if (e.hasNext())
                buf.append(File.pathSeparatorChar);
        }

        // On Java9 the sun.boot.class.path won't be set. System classes accessible through JRT filesystem
        if (vm_version.startsWith("9") || vm_version.startsWith("10")
                || vm_version.startsWith("11")
                || vm_version.startsWith("12")
                || vm_version.startsWith("13")) {
            buf.insert(0, File.pathSeparatorChar);
            buf.insert(0, System.getProperty("java.home") + File.separator + "lib" + File.separator + JRT_FS);
        }

        return buf.toString().intern();
    }

    /**
     * @param name fully qualified class name, e.g. java.lang.String
     * @return input stream for class
     */
    public InputStream getInputStream(String name) throws IOException {
        return getInputStream(name, ".class");
    }

    /**
     * Return stream for class or resource on CLASSPATH.
     *
     * @param name   fully qualified file name, e.g. java/lang/String
     * @param suffix file name ends with suff, e.g. .java
     * @return input stream for file on class path
     */
    public InputStream getInputStream(String name, String suffix) throws IOException {
        InputStream is = null;

        try {
            is = getClass().getClassLoader().getResourceAsStream(name + suffix);
        } catch (Exception e) {
        }

        if (is != null)
            return is;

        return getClassFile(name, suffix).getInputStream();
    }

    /**
     * @param name   fully qualified file name, e.g. java/lang/String
     * @param suffix file name ends with suff, e.g. .java
     * @return class file for the java class
     */
    public ClassFile getClassFile(String name, String suffix) throws IOException {
        for (int i = 0; i < paths.length; i++) {
            ClassFile cf;

            if ((cf = paths[i].getClassFile(name, suffix)) != null)
                return cf;
        }

        throw new IOException("Couldn't find: " + name + suffix);
    }

    /**
     * @param name fully qualified class name, e.g. java.lang.String
     * @return input stream for class
     */
    public ClassFile getClassFile(String name) throws IOException {
        return getClassFile(name, ".class");
    }

    /**
     * @param name   fully qualified file name, e.g. java/lang/String
     * @param suffix file name ends with suffix, e.g. .java
     * @return byte array for file on class path
     */
    public byte[] getBytes(String name, String suffix) throws IOException {
        InputStream is = getInputStream(name, suffix);

        if (is == null)
            throw new IOException("Couldn't find: " + name + suffix);

        DataInputStream dis = new DataInputStream(is);
        byte[] bytes = new byte[is.available()];
        dis.readFully(bytes);
        dis.close();
        is.close();

        return bytes;
    }

    /**
     * @return byte array for class
     */
    public byte[] getBytes(String name) throws IOException {
        return getBytes(name, ".class");
    }

    /**
     * @param name name of file to search for, e.g. java/lang/String.java
     * @return full (canonical) path for file
     */
    public String getPath(String name) throws IOException {
        int index = name.lastIndexOf('.');
        String suffix = "";

        if (index > 0) {
            suffix = name.substring(index);
            name = name.substring(0, index);
        }

        return getPath(name, suffix);
    }

    /**
     * @param name   name of file to search for, e.g. java/lang/String
     * @param suffix file name suffix, e.g. .java
     * @return full (canonical) path for file, if it exists
     */
    public String getPath(String name, String suffix) throws IOException {
        return getClassFile(name, suffix).getPath();
    }

    private static abstract class PathEntry implements Serializable {
        abstract ClassFile getClassFile(String name, String suffix) throws IOException;
    }

    /**
     * Contains information about file/ZIP entry of the Java class.
     */
    public interface ClassFile {
        /**
         * @return input stream for class file.
         */
        public abstract InputStream getInputStream() throws IOException;

        /**
         * @return canonical path to class file.
         */
        public abstract String getPath();

        /**
         * @return base path of found class, i.e. class is contained relative to
         * that path, which may either denote a directory, or zip file
         */
        public abstract String getBase();

        /**
         * @return modification time of class file.
         */
        public abstract long getTime();

        /**
         * @return size of class file.
         */
        public abstract long getSize();
    }

    private static class Dir extends PathEntry {
        private String dir;

        Dir(String d) {
            dir = d;
        }

        @Override
        ClassFile getClassFile(String name, String suffix) throws IOException {
            final File file = new File(dir + File.separatorChar + name.replace('.', File.separatorChar) + suffix);

            return file.exists() ? new ClassFile() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return new FileInputStream(file);
                }

                @Override
                public String getPath() {
                    try {
                        return file.getCanonicalPath();
                    } catch (IOException e) {
                        return null;
                    }

                }

                @Override
                public long getTime() {
                    return file.lastModified();
                }

                @Override
                public long getSize() {
                    return file.length();
                }

                @Override
                public String getBase() {
                    return dir;
                }

            } : null;
        }

        @Override
        public String toString() {
            return dir;
        }
    }


    private static class Zip extends PathEntry {
        private ZipFile zip;

        Zip(ZipFile z) {
            zip = z;
        }

        @Override
        ClassFile getClassFile(String name, String suffix) throws IOException {
            final ZipEntry entry = zip.getEntry(name.replace('.', '/') + suffix);

            return (entry != null) ? new ClassFile() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return zip.getInputStream(entry);
                }

                @Override
                public String getPath() {
                    return entry.toString();
                }

                @Override
                public long getTime() {
                    return entry.getTime();
                }

                @Override
                public long getSize() {
                    return entry.getSize();
                }

                @Override
                public String getBase() {
                    return zip.getName();
                }
            } : null;
        }
    }
}
