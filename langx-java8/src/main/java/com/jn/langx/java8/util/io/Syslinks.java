package com.jn.langx.java8.util.io;

import com.jn.langx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class Syslinks {
    private Syslinks(){

    }
    /**
     * Reads the target of the symbolic link
     *
     * @param symlink A file that is a symlink
     * @return A file that is the target of the symlink
     */
    @NonNull
    public static File readSymbolicLink(@NonNull File symlink) throws IOException {
        final Path path = Files.readSymbolicLink(symlink.toPath());
        return path.toFile();
    }

    @NonNull
    public static File createSymbolicLink(@NonNull File symlink, File target) throws IOException {
        Path path = symlink.toPath();
        Path linkPath;
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            linkPath = Files.createSymbolicLink(path, target.toPath());
        }else{
            linkPath=path;
        }
        return linkPath.toFile();

    }
}

