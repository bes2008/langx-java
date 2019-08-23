package com.jn.langx.util.io.file.comparator;

import com.jn.langx.util.io.file.Files;

import java.io.File;
import java.util.Comparator;

public class SizeFileComparator implements Comparator<File> {
    /**
     * Whether the sum of the directory's contents should be calculated.
     */
    private final boolean sumDirectoryContents;

    public SizeFileComparator() {
        this.sumDirectoryContents = false;
    }

    /**
     * Construct a file size comparator instance specifying whether the size of
     * the directory contents should be aggregated.
     * <p>
     * If the <code>sumDirectoryContents</code> is {@code true} The size of
     * directories is calculated using  {@link Files#sizeOfDirectory(File)}.
     *
     * @param sumDirectoryContents {@code true} if the sum of the directories' contents
     *                             should be calculated, otherwise {@code false} if directories should be treated
     *                             as size zero (see {@link Files#sizeOfDirectory(File)}).
     */
    public SizeFileComparator(final boolean sumDirectoryContents) {
        this.sumDirectoryContents = sumDirectoryContents;
    }

    /**
     * Compare the length of two files.
     *
     * @param file1 The first file to compare
     * @param file2 The second file to compare
     * @return a negative value if the first file's length
     * is less than the second, zero if the lengths are the
     * same and a positive value if the first files length
     * is greater than the second file.
     */
    @Override
    public int compare(final File file1, final File file2) {
        long size1 = 0;
        if (file1.isDirectory()) {
            size1 = sumDirectoryContents && file1.exists() ? Files.sizeOfDirectory(file1) : 0;
        } else {
            size1 = file1.length();
        }
        long size2 = 0;
        if (file2.isDirectory()) {
            size2 = sumDirectoryContents && file2.exists() ? Files.sizeOfDirectory(file2) : 0;
        } else {
            size2 = file2.length();
        }
        final long result = size1 - size2;
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
