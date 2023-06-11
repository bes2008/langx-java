package com.jn.langx.util.io.file;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.io.file.validator.FilepathValidators;
import com.jn.langx.util.net.Nets;

import java.io.File;
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
        return normalize(path);
    }

    private static String cleanPath0(String path) {
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


    /**
     * Normalizes a path, removing double and single dot path steps.
     * <p>
     * This method normalizes a path to a standard format.
     * The input may contain separators in either Unix or Windows format.
     * The output will contain separators in the format of the system.
     * <p>
     * A trailing slash will be retained.
     * A double slash will be merged to a single slash (but UNC names are handled).
     * A single dot path segment will be removed.
     * A double dot will cause that path segment and the one before to be removed.
     * If the double dot has no parent path segment to work with, {@code null}
     * is returned.
     * <p>
     * The output will be the same on both Unix and Windows except
     * for the separator character.
     * <pre>
     * /foo//               --&gt;   /foo/
     * /foo/./              --&gt;   /foo/
     * /foo/../bar          --&gt;   /bar
     * /foo/../bar/         --&gt;   /bar/
     * /foo/../bar/../baz   --&gt;   /baz
     * //foo//./bar         --&gt;   //foo/bar
     * /../                 --&gt;   null
     * ../foo               --&gt;   null
     * foo/bar/..           --&gt;   foo/
     * foo/../../bar        --&gt;   null
     * foo/../bar           --&gt;   bar
     * //server/foo/../bar  --&gt;   //server/bar
     * //server/../bar      --&gt;   null
     * C:\foo\..\bar        --&gt;   C:\bar
     * C:\..\bar            --&gt;   null
     * ~/foo/../bar/        --&gt;   ~/bar/
     * ~/../bar             --&gt;   null
     * </pre>
     * (Note the file separator returned will be correct for Windows/Unix)
     *
     * @param fileName the fileName to normalize, null returns null
     * @return the normalized fileName, or null if invalid
     * @throws IllegalArgumentException if the fileName contains the null character ({@code U+0000})
     */
    public static String normalize(final String fileName) {
        return doNormalize(fileName, File.separatorChar, true);
    }

    /**
     * Flips the Windows name separator to Linux and vice-versa.
     *
     * @param ch The Windows or Linux name separator.
     * @return The Windows or Linux name separator.
     */
    static char flipSeparator(final char ch) {
        if (ch == UNIX_NAME_SEPARATOR) {
            return WINDOWS_NAME_SEPARATOR;
        }
        if (ch == WINDOWS_NAME_SEPARATOR) {
            return UNIX_NAME_SEPARATOR;
        }
        throw new IllegalArgumentException(String.valueOf(ch));
    }

    /**
     * Internal method to perform the normalization.
     *
     * @param fileName      the fileName
     * @param separator     The separator character to use
     * @param keepSeparator true to keep the final separator
     * @return the normalized fileName
     * @throws IllegalArgumentException if the fileName contains the null character ({@code U+0000})
     */
    private static String doNormalize(final String fileName, final char separator, final boolean keepSeparator) {
        if (fileName == null) {
            return null;
        }

        requireNonNullChars(fileName);

        int size = fileName.length();
        if (size == 0) {
            return fileName;
        }
        final int prefix = getPrefixLength(fileName);
        if (prefix < 0) {
            return null;
        }

        final char[] array = new char[size + 2];  // +1 for possible extra slash, +2 for arraycopy
        fileName.getChars(0, fileName.length(), array, 0);

        // fix separators throughout
        final char otherSeparator = flipSeparator(separator);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == otherSeparator) {
                array[i] = separator;
            }
        }

        // add extra separator on the end to simplify code below
        boolean lastIsDirectory = true;
        if (array[size - 1] != separator) {
            array[size++] = separator;
            lastIsDirectory = false;
        }

        // adjoining slashes
        // If we get here, prefix can only be 0 or greater, size 1 or greater
        // If prefix is 0, set loop start to 1 to prevent index errors
        for (int i = prefix != 0 ? prefix : 1; i < size; i++) {
            if (array[i] == separator && array[i - 1] == separator) {
                System.arraycopy(array, i, array, i - 1, size - i);
                size--;
                i--;
            }
        }

        // dot slash
        for (int i = prefix + 1; i < size; i++) {
            if (array[i] == separator && array[i - 1] == '.' &&
                    (i == prefix + 1 || array[i - 2] == separator)) {
                if (i == size - 1) {
                    lastIsDirectory = true;
                }
                System.arraycopy(array, i + 1, array, i - 1, size - i);
                size -= 2;
                i--;
            }
        }

        // double dot slash
        outer:
        for (int i = prefix + 2; i < size; i++) {
            if (array[i] == separator && array[i - 1] == '.' && array[i - 2] == '.' &&
                    (i == prefix + 2 || array[i - 3] == separator)) {
                if (i == prefix + 2) {
                    return null;
                }
                if (i == size - 1) {
                    lastIsDirectory = true;
                }
                int j;
                for (j = i - 4; j >= prefix; j--) {
                    if (array[j] == separator) {
                        // remove b/../ from a/b/../c
                        System.arraycopy(array, i + 1, array, j + 1, size - i);
                        size -= i - j;
                        i = j + 1;
                        continue outer;
                    }
                }
                // remove a/../ from a/../c
                System.arraycopy(array, i + 1, array, prefix, size - i);
                size -= i + 1 - prefix;
                i = prefix + 1;
            }
        }

        if (size <= 0) {  // should never be less than 0
            return Emptys.EMPTY_STRING;
        }
        if (size <= prefix) {  // should never be less than prefix
            return new String(array, 0, size);
        }
        if (lastIsDirectory && keepSeparator) {
            return new String(array, 0, size);  // keep trailing separator
        }
        return new String(array, 0, size - 1);  // lose trailing separator
    }


    /**
     * Normalizes a path, removing double and single dot path steps.
     * <p>
     * This method normalizes a path to a standard format.
     * The input may contain separators in either Unix or Windows format.
     * The output will contain separators in the format specified.
     * <p>
     * A trailing slash will be retained.
     * A double slash will be merged to a single slash (but UNC names are handled).
     * A single dot path segment will be removed.
     * A double dot will cause that path segment and the one before to be removed.
     * If the double dot has no parent path segment to work with, {@code null}
     * is returned.
     * <p>
     * The output will be the same on both Unix and Windows except
     * for the separator character.
     * <pre>
     * /foo//               --&gt;   /foo/
     * /foo/./              --&gt;   /foo/
     * /foo/../bar          --&gt;   /bar
     * /foo/../bar/         --&gt;   /bar/
     * /foo/../bar/../baz   --&gt;   /baz
     * //foo//./bar         --&gt;   /foo/bar
     * /../                 --&gt;   null
     * ../foo               --&gt;   null
     * foo/bar/..           --&gt;   foo/
     * foo/../../bar        --&gt;   null
     * foo/../bar           --&gt;   bar
     * //server/foo/../bar  --&gt;   //server/bar
     * //server/../bar      --&gt;   null
     * C:\foo\..\bar        --&gt;   C:\bar
     * C:\..\bar            --&gt;   null
     * ~/foo/../bar/        --&gt;   ~/bar/
     * ~/../bar             --&gt;   null
     * </pre>
     * The output will be the same on both Unix and Windows including
     * the separator character.
     *
     * @param fileName      the fileName to normalize, null returns null
     * @param unixSeparator {@code true} if a Unix separator should
     *                      be used or {@code false} if a Windows separator should be used.
     * @return the normalized fileName, or null if invalid
     * @throws IllegalArgumentException if the fileName contains the null character ({@code U+0000})
     * @since 2.0
     */
    public static String normalize(final String fileName, final boolean unixSeparator) {
        return doNormalize(fileName, toSeparator(unixSeparator), true);
    }

    /**
     * Returns '/' if given true, '\\' otherwise.
     *
     * @param unixSeparator which separator to return.
     * @return '/' if given true, '\\' otherwise.
     */
    private static char toSeparator(final boolean unixSeparator) {
        return unixSeparator ? UNIX_NAME_SEPARATOR : WINDOWS_NAME_SEPARATOR;
    }

    /**
     * Gets the prefix from a full fileName, such as {@code C:/}
     * or {@code ~/}.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The prefix includes the first slash in the full fileName where applicable.
     * </p>
     * <pre>
     * Windows:
     * a\b\c.txt           --&gt; ""          --&gt; relative
     * \a\b\c.txt          --&gt; "\"         --&gt; current drive absolute
     * C:a\b\c.txt         --&gt; "C:"        --&gt; drive relative
     * C:\a\b\c.txt        --&gt; "C:\"       --&gt; absolute
     * \\server\a\b\c.txt  --&gt; "\\server\" --&gt; UNC
     *
     * Unix:
     * a/b/c.txt           --&gt; ""          --&gt; relative
     * /a/b/c.txt          --&gt; "/"         --&gt; absolute
     * ~/a/b/c.txt         --&gt; "~/"        --&gt; current user
     * ~                   --&gt; "~/"        --&gt; current user (slash added)
     * ~user/a/b/c.txt     --&gt; "~user/"    --&gt; named user
     * ~user               --&gt; "~user/"    --&gt; named user (slash added)
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * ie. both Unix and Windows prefixes are matched regardless.
     * </p>
     *
     * @param fileName the fileName to query, null returns null
     * @return the prefix of the file, null if invalid
     * @throws IllegalArgumentException if the result contains the null character ({@code U+0000})
     */
    public static String getPrefix(final String fileName) {
        if (fileName == null) {
            return null;
        }
        final int len = getPrefixLength(fileName);
        if (len < 0) {
            return null;
        }
        if (len > fileName.length()) {
            requireNonNullChars(fileName);
            return fileName + UNIX_NAME_SEPARATOR;
        }
        return requireNonNullChars(fileName.substring(0, len));
    }

    /**
     * Checks the input for null characters ({@code U+0000}), a sign of unsanitized data being passed to file level functions.
     * <p>
     * This may be used for poison byte attacks.
     *
     * @param path the path to check
     * @return The input
     * @throws IllegalArgumentException if path contains the null character ({@code U+0000})
     */
    private static String requireNonNullChars(final String path) {
        if (path.indexOf(0) >= 0) {
            throw new IllegalArgumentException(
                    "Null character present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
        }
        return path;
    }

    private static final int NOT_FOUND = -1;
    /**
     * The Unix separator character.
     */
    private static final char UNIX_NAME_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_NAME_SEPARATOR = '\\';

    /**
     * Checks if the character is a separator.
     *
     * @param ch the character to check
     * @return true if it is a separator character
     */
    private static boolean isSeparator(final char ch) {
        return ch == UNIX_NAME_SEPARATOR || ch == WINDOWS_NAME_SEPARATOR;
    }

    /**
     * Returns the length of the fileName prefix, such as {@code C:/} or {@code ~/}.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * </p>
     * <p>
     * The prefix length includes the first slash in the full fileName
     * if applicable. Thus, it is possible that the length returned is greater
     * than the length of the input string.
     * </p>
     * <pre>
     * Windows:
     * a\b\c.txt           --&gt; 0           --&gt; relative
     * \a\b\c.txt          --&gt; 1           --&gt; current drive absolute
     * C:a\b\c.txt         --&gt; 2           --&gt; drive relative
     * C:\a\b\c.txt        --&gt; 3           --&gt; absolute
     * \\server\a\b\c.txt  --&gt; 9           --&gt; UNC
     * \\\a\b\c.txt        --&gt; -1          --&gt; error
     *
     * Unix:
     * a/b/c.txt           --&gt; 0           --&gt; relative
     * /a/b/c.txt          --&gt; 1           --&gt; absolute
     * ~/a/b/c.txt         --&gt; 2           --&gt; current user
     * ~                   --&gt; 2           --&gt; current user (slash added)
     * ~user/a/b/c.txt     --&gt; 6           --&gt; named user
     * ~user               --&gt; 6           --&gt; named user (slash added)
     * //server/a/b/c.txt  --&gt; 9
     * ///a/b/c.txt        --&gt; -1          --&gt; error
     * C:                  --&gt; 0           --&gt; valid filename as only null character and / are reserved characters
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * ie. both Unix and Windows prefixes are matched regardless.
     * </p>
     * <p>
     * Note that a leading // (or \\) is used to indicate a UNC name on Windows.
     * These must be followed by a server name, so double-slashes are not collapsed
     * to a single slash at the start of the fileName.
     * </p>
     *
     * @param fileName the fileName to find the prefix in, null returns -1
     * @return the length of the prefix, -1 if invalid or null
     */
    public static int getPrefixLength(final String fileName) {
        if (fileName == null) {
            return NOT_FOUND;
        }
        final int len = fileName.length();
        if (len == 0) {
            return 0;
        }
        char ch0 = fileName.charAt(0);
        if (ch0 == ':') {
            return NOT_FOUND;
        }
        if (len == 1) {
            if (ch0 == '~') {
                return 2;  // return a length greater than the input
            }
            return isSeparator(ch0) ? 1 : 0;
        }
        if (ch0 == '~') {
            int posUnix = fileName.indexOf(UNIX_NAME_SEPARATOR, 1);
            int posWin = fileName.indexOf(WINDOWS_NAME_SEPARATOR, 1);
            if (posUnix == NOT_FOUND && posWin == NOT_FOUND) {
                return len + 1;  // return a length greater than the input
            }
            posUnix = posUnix == NOT_FOUND ? posWin : posUnix;
            posWin = posWin == NOT_FOUND ? posUnix : posWin;
            return Math.min(posUnix, posWin) + 1;
        }
        final char ch1 = fileName.charAt(1);
        if (ch1 == ':') {
            ch0 = Character.toUpperCase(ch0);
            if (ch0 >= 'A' && ch0 <= 'Z') {
                if (len == 2 && !FS.getCurrent().supportsDriveLetter()) {
                    return 0;
                }
                if (len == 2 || !isSeparator(fileName.charAt(2))) {
                    return 2;
                }
                return 3;
            }
            if (ch0 == UNIX_NAME_SEPARATOR) {
                return 1;
            }
            return NOT_FOUND;

        }
        if (!isSeparator(ch0) || !isSeparator(ch1)) {
            return isSeparator(ch0) ? 1 : 0;
        }
        int posUnix = fileName.indexOf(UNIX_NAME_SEPARATOR, 2);
        int posWin = fileName.indexOf(WINDOWS_NAME_SEPARATOR, 2);
        if (posUnix == NOT_FOUND && posWin == NOT_FOUND || posUnix == 2 || posWin == 2) {
            return NOT_FOUND;
        }
        posUnix = posUnix == NOT_FOUND ? posWin : posUnix;
        posWin = posWin == NOT_FOUND ? posUnix : posWin;
        final int pos = Math.min(posUnix, posWin) + 1;
        final String hostnamePart = fileName.substring(2, pos - 1);
        return Nets.isValidHostAddress(hostnamePart) ? pos : NOT_FOUND;
    }

    public static String asUnixFilePath(String filePath) {
        Preconditions.checkNotNull(filePath);
        return filePath.trim().replaceAll("\\\\", "/").replaceAll("/+", "/");
    }

    public static String getParentPath(String filepath) {
        String filename = extractFilename(filepath);
        return filepath.substring(0, filepath.length() - filename.length());
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
        if (index == -1) {
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

    private Filenames() {

    }
}
