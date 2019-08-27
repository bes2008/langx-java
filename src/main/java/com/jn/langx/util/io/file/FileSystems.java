package com.jn.langx.util.io.file;

import com.jn.langx.util.Platform;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileSystems {
    private static final Logger logger = LoggerFactory.getLogger(FileSystems.class);

    /**
     * Checks if the request number of bytes of free disk space are available under the given
     * path.
     *
     * @param path Directory to check
     * @param size Bytes of free space requested
     * @return True iff the free space in the specified path meets or exceeds the requested space
     */
    public static boolean hasFreeSpace(final File path, final long size) {
        final long freeSpace = path.getFreeSpace();

        if (freeSpace == 0L && Platform.isWindows) {
            // On Windows, SUBST'ed drives report 0L from getFreeSpace().
            // The API doc says "The number of unallocated bytes on the partition or 0L if the abstract pathname does not name a partition."
            // There is no straightforward fix for this and it seems a fix is included in Java 9.
            // One alternative is to launch and parse a DIR command and look at the reported free space.
            // This is a temporary fix to get the CI tests going which relies on SUBST'ed drives to manage long paths.
            logger.warn("Cannot retrieve free space on " + path.toString() + ". This is probably a SUBST'ed drive.");
            return true;
        }

        return freeSpace >= size;
    }

    public static boolean isHidden(File file){
        String name = file.getName();
        return name.startsWith(".");
    }

    /**
     * Determines whether the specified file is a Symbolic Link rather than an actual file.
     * <p>
     * Will not return true if there is a Symbolic Link anywhere in the path,
     * only if the specific file is.
     * <p>
     * When using jdk1.7, this method delegates to {@code boolean java.nio.file.Files.isSymbolicLink(Path path)}
     * <p>
     * <b>Note:</b> the current implementation always returns {@code false} if running on jdk1.6
     * <p>
     * For code that runs on Java 1.7 or later, use the following method instead:
     * <br>
     * {@code boolean java.nio.file.Files.isSymbolicLink(Path path)}
     *
     * @param file the file to check
     * @return true if the file is a Symbolic Link
     * @throws IOException if an IO error occurs while checking the file
     */
    public static boolean isNotSymlink(final File file) throws IOException {
        return !isSymlink(file);
    }

    public static boolean isSymlink(final File file) throws IOException {
        Preconditions.checkNotNull(file);
        if (Platform.JAVA_VERSION_INT < 7) {
            return false;
        }
        try {
            Class pathClass = Class.forName("java.nio.file.Path");
            Object filePathObject = Reflects.invokePublicMethod(file, "toPath", null, null, true, false);
            return Reflects.<Boolean>invokeAnyStaticMethod("java.nio.file.Files", "isSymbolicLink", new Class[]{pathClass}, new Object[]{filePathObject}, true, false);
        } catch (Throwable ex) {
            return false;
        }
    }
}
