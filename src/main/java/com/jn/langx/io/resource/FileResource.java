package com.jn.langx.io.resource;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * file:/home/fjn/xx/yy
 */
public class FileResource extends AbstractPathableResource<File> {
    private File file;
    public static final String PATTERN = "file:";
    public static final String FILE_URL_PATTERN = "file://";

    public FileResource(@NonNull String path) {
        Preconditions.checkTrue(path.startsWith(PATTERN) && !path.startsWith(FILE_URL_PATTERN));
        setPath(path);
    }

    @Override
    public void setPath(String path) {
        super.setPath(path);
        file = new File(path.substring(PATTERN.length()));
    }

    @Override
    public String getPath() {
        return exists() ? file.getAbsolutePath() : null;
    }

    @Override
    public String getAbsolutePath() {
        return exists() ? file.getAbsolutePath() : null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public boolean isReadable() {
        return exists() && file.canRead();
    }


    @Override
    public boolean exists() {
        return file != null && file.exists();
    }

    @Override
    public File getRealResource() {
        return file;
    }

    @Override
    public long contentLength() {
        return exists() ? file.length() : -1;
    }
}
