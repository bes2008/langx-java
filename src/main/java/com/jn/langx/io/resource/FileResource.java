package com.jn.langx.io.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileResource extends AbstractResource<File> {
    private File file;

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public boolean isReadable() {
        return file != null && file.canRead();
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
    public long contentLength() throws IOException {
        return exists() ? file.length() : -1;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
