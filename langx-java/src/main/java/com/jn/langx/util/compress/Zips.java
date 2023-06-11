package com.jn.langx.util.compress;


import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Files;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author jinuo.fang
 */
public class Zips {
    private Zips(){

    }

    /**
     * Taille du buffer pour les lectures/écritures.
     */
    private static final int BUFFER_SIZE = 8 * 1024;

    /**
     * Uncompress zipped stream in targetDir.
     *
     * @param stream    the zip source stream, stream is closed before return
     * @param targetDir the destination directory
     * @throws IOException if any problem while unzip
     */
    public static void uncompress(InputStream stream, File targetDir) throws IOException {
        uncompressAndRename(stream, targetDir, null, null);
    }


    /**
     * Uncompress zipped stream in targetDir, and rename uncompressed file if
     * necessary. If renameFrom or renameTo is null no renaming is done
     * <p>
     * file in zip use / to separate directory and not begin with /
     * each directory ended with /
     *
     * @param stream     the zip source stream, stream is closed before return
     * @param targetDir  the destination directory
     * @param renameFrom pattern to permit rename file before uncompress it
     * @param renameTo   new name for file if renameFrom is applicable to it
     *                   you can use $1, $2, ... if you have '(' ')' in renameFrom
     * @throws IOException if any problem while uncompressing
     */
    public static void uncompressAndRename(InputStream stream,
                                           File targetDir,
                                           String renameFrom,
                                           String renameTo) throws IOException {
        ZipInputStream in = null;
        try {
            in = new ZipInputStream(new BufferedInputStream(stream));
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                String name = entry.getName();
                if (renameFrom != null && renameTo != null) {
                    name = name.replaceAll(renameFrom, renameTo);
                }
                File target = Files.newFile(targetDir, name);
                if (entry.isDirectory()) {
                    Files.makeDirs(target);
                } else {
                    Files.makeDirs(target.getParentFile());
                    OutputStream out = null;
                    try {
                        out = new BufferedOutputStream(new FileOutputStream(target));
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int len;
                        while ((len = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                            out.write(buffer, 0, len);
                        }
                    } finally {
                        IOs.close(out);
                    }
                }
            }
        } finally {
            IOs.close(in);
        }
    }

    /**
     * Unzip compressed archive and keep non excluded patterns.
     *
     * @param file      archive file
     * @param targetDir destination file
     * @param excludes  excludes pattern (pattern must match complete entry name including root folder)
     */
    public static void uncompressFiltered(File file, File targetDir, String... excludes) throws IOException {

        ZipFile zipFile = new ZipFile(file);
        try {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                String name = entry.getName();
                // add continue to break loop
                boolean excludeEntry = false;
                if (excludes != null) {
                    for (String exclude : excludes) {
                        if (name.matches(exclude)) {
                            excludeEntry = true;
                        }
                    }
                }

                if (!excludeEntry) {
                    File target = Files.newFile(targetDir, name);
                    if (entry.isDirectory()) {
                        Files.makeDirs(target);
                    } else {
                        // get inputstream only here
                        Files.makeDirs(target.getParentFile());
                        InputStream in = null;
                        try {
                            in = zipFile.getInputStream(entry);
                            OutputStream out = null;
                            try {
                                out = new BufferedOutputStream(new FileOutputStream(target));
                                byte[] buffer = new byte[8 * 1024];
                                int len;

                                while ((len = in.read(buffer, 0, 8 * 1024)) != -1) {
                                    out.write(buffer, 0, len);
                                }

                            } finally {
                                IOs.close(out);
                            }
                        } finally {
                            IOs.close(in);
                        }
                    }
                }
            }
        }finally {
            IOs.close(zipFile);
        }
    }


    /**
     * Tests if the given file is a zip file.
     *
     * @param file the file to test
     * @return {@code true} if the file is a valid zip file,
     * {@code false} otherwise.
     */
    public static boolean isZipFile(File file) {
        boolean result = false;
        try {
            ZipFile zipFile = new ZipFile(file);
            zipFile.close();
            result = true;
        } catch (IOException e) {
            // silent test
        }
        return result;
    }
}
