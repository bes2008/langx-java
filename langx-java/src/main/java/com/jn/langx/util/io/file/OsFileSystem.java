package com.jn.langx.util.io.file;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.PrimitiveArrays;
import com.jn.langx.util.os.Platform;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class OsFileSystem {
    private final int blockSize;
    private final boolean casePreserving;
    private final boolean caseSensitive;
    private final Character[] illegalFileNameChars;
    private final int maxFileNameLength;
    private final int maxPathLength;
    private final List<String> reservedFileNames;
    private final boolean reservedFileNamesExtensions;
    private final boolean supportsDriveLetter;
    private final char pathSeparator;
    private final char pathSeparatorOther;

    private final String[] pathSeparators;

    /**
     * Constructs a new instance.
     *
     * @param blockSize                   file allocation block size in bytes.
     * @param caseSensitive               Whether this file system is case-sensitive.
     * @param casePreserving              Whether this file system is case-preserving.
     * @param maxFileLength               The maximum length for file names. The file name does not include folders.
     * @param maxPathLength               The maximum length of the path to a file. This can include folders.
     * @param illegalFileNameChars        Illegal characters for this file system.
     * @param reservedFileNames           The reserved file names.
     * @param reservedFileNamesExtensions reserved file extensions
     * @param supportsDriveLetter         Whether this file system support driver letters.
     * @param pathSeparator               The name separator, '\\' on Windows, '/' on Linux.
     */
    OsFileSystem(final int blockSize,
                 final boolean caseSensitive,
                 final boolean casePreserving,
                 final int maxFileLength,
                 final int maxPathLength,
                 final Character[] illegalFileNameChars,
                 final String[] reservedFileNames,
                 final boolean reservedFileNamesExtensions,
                 final boolean supportsDriveLetter,
                 final char pathSeparator) {
        this.blockSize = blockSize;
        this.maxFileNameLength = maxFileLength;
        this.maxPathLength = maxPathLength;
        this.illegalFileNameChars = Objs.requireNonNull(illegalFileNameChars, "illegalFileNameChars");
        this.reservedFileNames = Lists.newArrayList(Objs.requireNonNull(reservedFileNames, "reservedFileNames"));
        this.reservedFileNamesExtensions = reservedFileNamesExtensions;
        this.caseSensitive = caseSensitive;
        this.casePreserving = casePreserving;
        this.supportsDriveLetter = supportsDriveLetter;
        this.pathSeparator = pathSeparator;
        this.pathSeparatorOther = Filenames.flipSeparator(pathSeparator);


        Set<String> pathSeparators = new HashSet<String>();
        pathSeparators.add(this.pathSeparator+"");
        pathSeparators.add("/");
        this.pathSeparators = Collects.toArray(pathSeparators, String[].class);
    }

    /**
     * Gets the file allocation block size in bytes.
     *
     * @return the file allocation block size in bytes.
     * @since 5.2.9
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * Gets a cloned copy of the illegal characters for this file system.
     *
     * @return the illegal characters for this file system.
     */
    public char[] getIllegalFileNameChars() {
        final char[] chars = new char[illegalFileNameChars.length];
        for (int i = 0; i < illegalFileNameChars.length; i++) {
            chars[i] = (char) illegalFileNameChars[i];
        }
        return chars;
    }

    /**
     * Gets a cloned copy of the illegal code points for this file system.
     *
     * @return the illegal code points for this file system.
     * @since 5.2.9
     */
    public char[] getIllegalFileNameCodes() {
        return PrimitiveArrays.unwrap(this.illegalFileNameChars, false);
    }

    /**
     * Gets the maximum length for file names. The file name does not include folders.
     *
     * @return the maximum length for file names.
     */
    public int getMaxFileNameLength() {
        return maxFileNameLength;
    }

    /**
     * Gets the maximum length of the path to a file. This can include folders.
     *
     * @return the maximum length of the path to a file.
     */
    public int getMaxPathLength() {
        return maxPathLength;
    }

    /**
     * Gets the name separator, '\\' on Windows, '/' on Linux.
     *
     * @return '\\' on Windows, '/' on Linux.
     * @since 5.2.9
     */
    public char getPathSeparator() {
        return pathSeparator;
    }

    public String[] getPathSeparatorStrings(){
        return this.pathSeparators;
    }

    /**
     * Gets a cloned copy of the reserved file names.
     *
     * @return the reserved file names.
     */
    public List<String> getReservedFileNames() {
        return Lists.newArrayList(reservedFileNames);
    }

    /**
     * Tests whether this file system preserves case.
     *
     * @return Whether this file system preserves case.
     */
    public boolean isCasePreserving() {
        return casePreserving;
    }

    /**
     * Tests whether this file system is case-sensitive.
     *
     * @return Whether this file system is case-sensitive.
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * Tests if a filename file name (without a path) such as {@code "filename.ext"} or {@code "filename"} is a
     * potentially legal file name. If the file name length exceeds {@link #getMaxFileNameLength()}, or if it contains
     * an illegal character then the check fails.
     *
     * @param filename a filename file name (without a path) like {@code "filename.ext"} or {@code "filename"}
     * @return {@code true} if the filename name is legal
     */
    public boolean isLegalFileName(final CharSequence filename) {
        int length = Objs.length(filename);
        if (length < 1 || length > maxFileNameLength) {
            return false;
        }

        if (Strings.startsWith(filename, " ") || Strings.startsWith(filename, "\t")) {
            return false;
        }

        if (isReservedFileName(filename)) {
            return false;
        }

        for (int i = 0; i < filename.length(); i++) {
            char c = filename.charAt(i);
            if (Collects.contains(illegalFileNameChars, c)) {
                return false;
            }
        }
        return true;

    }


    /**
     * Tests whether the given string is a reserved file name.
     *
     * @param candidate the string to test
     * @return {@code true} if the given string is a reserved file name.
     */
    public boolean isReservedFileName(final CharSequence candidate) {
        final CharSequence test = reservedFileNamesExtensions ? trimExtension(candidate) : candidate;
        return reservedFileNames.contains(test);
    }

    protected CharSequence trimExtension(final CharSequence cs) {
        final int index = Strings.lastIndexOf(cs, ".");
        return index < 0 ? cs : cs.subSequence(0, index);
    }

    /**
     * Tests whether this file system support driver letters.
     * <p>
     * Windows supports driver letters as do other operating systems. Whether these other OS's still support Java like
     * OS/2, is a different matter.
     * </p>
     *
     * @return whether this file system support driver letters.
     * @see <a href="https://en.wikipedia.org/wiki/Drive_letter_assignment">Operating systems that use drive letter
     * assignment</a>
     * @since 5.2.9
     */
    public boolean supportsDriveLetter() {
        return supportsDriveLetter;
    }

    /**
     * Converts all separators to the Windows separator of backslash.
     *
     * @param path the path to be changed, null ignored
     * @return the updated path
     * @since 5.2.9
     */
    public String normalizeSeparators(final String path) {
        return Strings.replaceChars(path, pathSeparatorOther, pathSeparator);
    }

    /**
     * Tests if the given character is illegal in a file name, {@code false} otherwise.
     *
     * @param c the character to test
     * @return {@code true} if the given character is illegal in a file name, {@code false} otherwise.
     */
    private boolean isIllegalFileNameChar(final char c) {
        return Pipeline.of(illegalFileNameChars).contains(c);
    }


    /**
     * Linux file system.
     */
    public static final OsFileSystem LINUX = new OsFileSystem(8192,
            true,
            true,
            255,
            4096,
            new Character[]{0, '/'},
            Emptys.EMPTY_STRINGS,
            false,
            false,
            '/');

    /**
     * MacOS file system.
     */
    public static final OsFileSystem MAC_OSX = new OsFileSystem(4096,
            true,
            true,
            255,
            1024,
            new Character[]{0, '/', ':'},
            Emptys.EMPTY_STRINGS,
            false,
            false,
            '/');

    /**
     * Windows file system.
     * <p>
     * The reserved characters are defined in the
     * <a href="https://docs.microsoft.com/en-us/windows/win32/fileio/naming-a-file">Naming Conventions
     * (microsoft.com)</a>.
     * </p>
     *
     * @see <a href="https://docs.microsoft.com/en-us/windows/win32/fileio/naming-a-file">Naming Conventions
     * (microsoft.com)</a>
     * @see <a href="https://docs.microsoft.com/en-us/windows/win32/api/fileapi/nf-fileapi-createfilea#consoles">
     * CreateFileA function - Consoles (microsoft.com)</a>
     */
    public static final OsFileSystem WINDOWS = new OsFileSystem(4096,
            false,
            true,
            255,
            32000,
            new Character[]{
                    0, 1, 2, 3, 4, 5, 6, 7,
                    8, 9, 10, 11, 12, 13, 14, 15,
                    16, 17, 18, 19, 20, 21, 22, 23,
                    24, 25, 26, 27, 28, 29, 30, 31,
                    '"', '*', '/', ':', '<', '>', '?', '\\', '|'
            },
            new String[]{"AUX", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "CON", "CONIN$", "CONOUT$", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9", "NUL", "PRN"},
            true,
            true,
            '\\'
    );
    private static final OsFileSystem current = current();

    private static OsFileSystem current() {
        if (Platform.isMaxOS) {
            return MAC_OSX;
        }
        if (Platform.isWindows) {
            return WINDOWS;
        }
        return LINUX;
    }

    public static OsFileSystem getCurrent() {
        return current;
    }
}
