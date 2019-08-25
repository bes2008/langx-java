package com.jn.langx.util.io.file.comparator;


import java.io.File;
import java.util.Comparator;

public class LastModifiedFileComparator implements Comparator<File> {


    /**
     * Compare the last the last modified date/time of two files.
     *
     * @param file1 The first file to compare
     * @param file2 The second file to compare
     * @return a negative value if the first file's lastmodified date/time
     * is less than the second, zero if the lastmodified date/time are the
     * same and a positive value if the first files lastmodified date/time
     * is greater than the second file.
     */
    @Override
    public int compare(final File file1, final File file2) {
        long delta = file1.lastModified() - file2.lastModified();
        if (delta > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (delta < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return Integer.parseInt(delta + "");
    }
}