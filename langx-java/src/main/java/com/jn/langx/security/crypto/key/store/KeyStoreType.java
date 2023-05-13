package com.jn.langx.security.crypto.key.store;

import com.jn.langx.util.Objs;

public class KeyStoreType {
    private String type;
    private String provider;

    public KeyStoreType(String type, String provider) {
        setType(type);
        setProvider(provider);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getType() {
        return type;
    }

    public String getProvider() {
        return provider;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KeyStoreType that = (KeyStoreType) o;

        if (!Objs.equals(this.type, that.type)) {
            return false;
        }
        return Objs.equals(this.provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objs.hash(this.type, this.provider);
    }
}
