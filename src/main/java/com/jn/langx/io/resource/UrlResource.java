package com.jn.langx.io.resource;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.URLs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * ftp://ftp.baidu.com/resources/xx
 * file:///home/fjn/
 * file://c:\\a\\b
 * http://www.baidu.com/resources/xx
 */
public class UrlResource extends AbstractPathableResource<URL> {

    private URL url;

    public UrlResource(String url) {
        this(URLs.newURL(url));
    }

    public UrlResource(URI uri) {
        this(URLs.toURL(uri));
    }

    public UrlResource(URL url) {
        Preconditions.checkNotNull(url);
        this.url = url;
        setPath(getAbsolutePath());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return URLs.getInputStream(url);
    }

    @Override
    public boolean exists() {
        return URLs.exists(url);
    }

    @Override
    public String getAbsolutePath() {
        return URLs.isFileURL(url) ? URLs.getFile(url).getAbsolutePath() : url.getPath();
    }

    @Override
    public URL getRealResource() {
        return url;
    }

    @Override
    public long contentLength() {
        return URLs.getContentLength(url);
    }
}
