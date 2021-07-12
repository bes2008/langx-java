package com.jn.langx.security.keyspec.pem;

import com.jn.langx.Nameable;
import com.jn.langx.security.keyspec.KeyEncoding;

public class PemKeyFormat implements Nameable {
    private String name;
    private String header;
    private String footer;
    private KeyEncoding encoding;

    public PemKeyFormat() {

    }

    public PemKeyFormat(String name, String header, String footer) {
        this(name, header, footer, KeyEncoding.BASE64);
    }

    public PemKeyFormat(String name, String header, String footer, KeyEncoding keyEncode) {
        setName(name);
        setHeader(header);
        setFooter(footer);
        setEncoding(keyEncode);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public KeyEncoding getEncoding() {
        return encoding;
    }

    public void setEncoding(KeyEncoding encoding) {
        this.encoding = encoding;
    }
}
