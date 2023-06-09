package com.jn.langx.util.io.file;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.file.validator.FilepathValidators;

import java.util.LinkedList;

public class Filenames {
    public static boolean checkFileSegment(String filename) {
        return FilepathValidators.validateName(filename);
    }

    public static boolean checkFilePath(String filePath) {
        return FilepathValidators.validatePath(filePath);
    }

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';

    private static final String PARENT_PATH = "..";

    /**
     * Normalize the path by suppressing sequences like "path/.." and
     * inner simple dots.
     * <p>The result is convenient for path comparison. For other uses,
     * notice that Windows separators ("\") are replaced by simple slashes.
     *
     * @param path the original path
     * @return the normalized path
     */
    public static String cleanPath(String path) {
        if (Strings.isEmpty(path)) {
            return path;
        }
        String pathToUse = Strings.replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

        // Strip prefix from path to analyze, to not treat it as part of the
        // first path element. This is necessary to correctly parse paths like
        // "file:core/../core/io/Resource.class", where the ".." should just
        // strip the first "core" directory while keeping the "file:" prefix.
        int prefixIndex = pathToUse.indexOf(':');
        String prefix = "";
        if (prefixIndex != -1) {
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (prefix.contains(FOLDER_SEPARATOR)) {
                prefix = "";
            } else {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            }
        }
        if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
            prefix = prefix + FOLDER_SEPARATOR;
            pathToUse = pathToUse.substring(1);
        }

        String[] pathArray = Strings.split(pathToUse, FOLDER_SEPARATOR);
        LinkedList<String> pathElements = new LinkedList<String>();
        int tops = 0;

        for (int i = pathArray.length - 1; i >= 0; i--) {
            String element = pathArray[i];
            if (CURRENT_PATH.equals(element)) {
                // Points to current directory - drop it.
            } else if (PARENT_PATH.equals(element)) {
                // Registering top path found.
                tops++;
            } else {
                if (tops > 0) {
                    // Merging path element with element corresponding to top path.
                    tops--;
                } else {
                    // Normal path element found.
                    pathElements.add(0, element);
                }
            }
        }

        // Remaining top paths need to be retained.
        for (int i = 0; i < tops; i++) {
            pathElements.add(0, PARENT_PATH);
        }
        // If nothing else left, at least explicitly point to current path.
        if (pathElements.size() == 1 && "".equals(pathElements.getLast()) && !prefix.endsWith(FOLDER_SEPARATOR)) {
            pathElements.add(0, CURRENT_PATH);
        }

        return prefix + Strings.join(FOLDER_SEPARATOR, pathElements);
    }

    public static String asUnixFilePath(String filePath) {
        Preconditions.checkNotNull(filePath);
        return filePath.trim().replaceAll("\\\\", "/").replaceAll("/+", "/");
    }

    public static String getParentPath(String filepath){
        String filename = extractFilename(filepath);
        return filepath.substring(0,filepath.length() - filename.length());
    }

    public static String extractFilename(String filePath) {
        return extractFilename(filePath, true);
    }

    public static String extractFilename(String filePath, boolean containsSuffix) {
        Preconditions.checkTrue(Strings.isNotBlank(filePath));
        int lastFileSeparatorIndex = asUnixFilePath(filePath).lastIndexOf("/");
        String fileName = filePath.substring(lastFileSeparatorIndex + 1);
        if (containsSuffix) {
            return fileName;
        }
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex > 0 && suffixIndex != (fileName.length() - 1)) {
            return fileName.substring(0, suffixIndex);
        }
        return fileName;
    }

    public static String getSuffix(String filePath) {
        String filename = extractFilename(filePath);
        int index = filename.lastIndexOf(".");
        if(index==-1){
            return "";
        }
        return filename.substring(index + 1);
    }

    public static String getSuffixAsLowCase(String filename) {
        return getSuffix(filename).toLowerCase();
    }

    public static String getSuffixAsUpperCase(String filename) {
        return getSuffix(filename).toUpperCase();
    }
    private Filenames(){

    }
}
