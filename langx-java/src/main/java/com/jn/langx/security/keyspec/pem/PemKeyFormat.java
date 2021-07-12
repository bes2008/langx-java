package com.jn.langx.security.keyspec.pem;

import com.jn.langx.Nameable;
import com.jn.langx.security.keyspec.KeyEncode;

public class PemKeyFormat implements Nameable {
    private String name;
    private String header;
    private String footer;
    private KeyEncode encode;

    public PemKeyFormat() {

    }

    public PemKeyFormat(String name, String header, String footer) {
        this(name, header, footer, KeyEncode.BASE64);
    }

    public PemKeyFormat(String name, String header, String footer, KeyEncode keyEncode) {
        setHeader(header);
        setFooter(footer);
        setEncode(keyEncode);
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

    public KeyEncode getEncode() {
        return encode;
    }

    public void setEncode(KeyEncode encode) {
        this.encode = encode;
    }
}
