package com.jn.langx.util.io.file;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;

public class Filenames {
    /**
     * 文件名不能包含【\/:*?">|<】之一。
     * \\\\ 表示\
     * /	表示/
     * :	表示:
     * \\*	表示*
     * \\?	表示?
     * \"	表示"
     * >	表示>
     * <	表示<
     * \\|	表示|
     * <p>
     * 把这些字符排除就行了。
     */
    public static boolean checkFileSegment(String filename) {
        return filename.matches("^[^\\\\/:\\*\\?\">\\|<]+(\\.[^\\\\/:\\*\\?\">\\|<]+)?$");
    }

    public static boolean checkFilePath(String filePath) {
        filePath = asUnixFilePath(filePath);
        int partitionSeparatorIndex = filePath.indexOf(":");
        String partition = filePath.substring(0, partitionSeparatorIndex);
        if (Strings.isNotBlank(partition)) {
            if (!checkFileSegment(partition)) {
                return false;
            }
        }
        filePath = filePath.substring(partitionSeparatorIndex + 1);
        filePath = filePath.replaceAll("\\\\+", "/");
        String[] pathSegments = Strings.split(filePath, "/");
        return Collects.<String>allMatch(Collects.asList(pathSegments), new Predicate<String>() {
            @Override
            public boolean test(String pathSegment) {
                return checkFileSegment(pathSegment);
            }
        });
    }

    public static String asUnixFilePath(String filePath) {
        Preconditions.checkNotNull(filePath);
        return filePath.trim().replaceAll("\\\\", "/").replaceAll("/+", "/");
    }

    public static String extractFilename(String filePath) {
        Preconditions.checkTrue(Strings.isNotBlank(filePath));
        int lastFileSeparatorIndex = asUnixFilePath(filePath).lastIndexOf("/");
        return filePath.substring(lastFileSeparatorIndex + 1);
    }

    public static String getSuffix(String filePath) {
        String filename = extractFilename(filePath);
        int index = filename.lastIndexOf(".");
        return filename.substring(index + 1);
    }

    public static String getSuffixAsLowCase(String filename) {
        return getSuffix(filename).toLowerCase();
    }
    public static String getSuffixAsUpperCase(String filename) {
        return getSuffix(filename).toUpperCase();
    }

}
