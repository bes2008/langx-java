package com.jn.langx.util.io.file;

import com.jn.langx.util.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
}
