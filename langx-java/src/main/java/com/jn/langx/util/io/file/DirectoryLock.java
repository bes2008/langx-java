/*
 * Copyright (c) 2008-2021, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jn.langx.util.io.file;

import com.jn.langx.exception.RuntimeIOException;
import com.jn.langx.util.io.IOs;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;


/**
 * A DirectoryLock represents a lock on a specific directory.
 * <p>
 * DirectoryLock is acquired by calling {@link #lockForDirectory(File, Logger)}.
 */
public final class DirectoryLock {

    public static final String FILE_NAME = "lock";

    private final File dir;
    private final FileChannel channel;
    private final FileLock lock;
    private final Logger logger;

    private DirectoryLock(File dir, FileChannel channel, FileLock lock, Logger logger) {
        this.dir = dir;
        this.channel = channel;
        this.lock = lock;
        this.logger = logger;
    }

    /**
     * Returns the locked directory.
     */
    public File getDir() {
        return dir;
    }

    /**
     * Returns the actual {@link FileLock}.
     */
    FileLock getLock() {
        return lock;
    }

    /**
     * Releases the lock on directory.
     */
    public void release() {
        if (logger.isInfoEnabled()) {
            logger.info("Releasing lock on {}", Files.getCanonicalPath(lockFile()));
        }
        try {
            lock.release();
        } catch (ClosedChannelException e) {
            // ignore it
        } catch (IOException e) {
            logger.error("Problem while releasing the lock on {}" , lockFile(), e);
        }
        try {
            channel.close();
        } catch (IOException e) {
            logger.error("Problem while closing the channel " + lockFile(), e);
        }
    }

    private File lockFile() {
        return new File(dir, FILE_NAME);
    }

    /**
     * Acquires a lock for given directory. A special file, named <strong>lock</strong>,
     * is created inside the directory and that file is locked.
     *
     * @param dir    the directory
     * @param logger logger
     * @throws RuntimeIOException If lock file cannot be created or it's already locked
     */
    public static DirectoryLock lockForDirectory(File dir, Logger logger) {
        File lockFile = new File(dir, FILE_NAME);
        FileChannel channel = openChannel(lockFile);
        FileLock lock = acquireLock(lockFile, channel);
        if (logger.isInfoEnabled()) {
            logger.info("Acquired lock on " + Files.getCanonicalPath(lockFile));
        }
        return new DirectoryLock(dir, channel, lock, logger);
    }

    private static FileChannel openChannel(File lockFile) {
        try {
            return new RandomAccessFile(lockFile, "rw").getChannel();
        } catch (IOException e) {
            throw new RuntimeIOException("Cannot create lock file " + Files.getCanonicalPath(lockFile), e);
        }
    }

    private static FileLock acquireLock(File lockFile, FileChannel channel) {
        FileLock fileLock = null;
        try {
            fileLock = channel.tryLock();
            if (fileLock == null) {
                throw new RuntimeIOException("Cannot acquire lock on " + Files.getCanonicalPath(lockFile)
                        + ". Directory is already being used by another member.");
            }
            return fileLock;
        } catch (OverlappingFileLockException e) {
            throw new RuntimeException("Cannot acquire lock on " + Files.getCanonicalPath(lockFile)
                    + ". Directory is already being used by another member.", e);
        } catch (IOException e) {
            throw new RuntimeIOException("Unknown failure while acquiring lock on " + Files.getCanonicalPath(lockFile), e);
        } finally {
            if (fileLock == null) {
                IOs.close(channel);
            }
        }
    }
}
