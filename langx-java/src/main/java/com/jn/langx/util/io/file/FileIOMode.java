package com.jn.langx.util.io.file;

import com.jn.langx.Delegatable;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

public enum FileIOMode implements CommonEnum, Delegatable<EnumDelegate> {
    READ_ONLY("r", 1, "readonly", "只读模式"),
    READ_WRITE("rw", 2, "readwrite", "读写模式"),
    READ_WRITE_SYNC("rws", 4, "readwrite_sync", "读写模式，同步文件内容、元数据的更新到底层存储设备"),
    READ_WRITE_DSYNC("rwd", 8, "readwrite_dsync", "读写模式，同步文件的任何更新到底层存储设备");

    /**
     * copy form Random Access File
     */
    public static final int O_RDONLY = 1;
    public static final int O_RDWR = 2;
    public static final int O_SYNC = 4;
    public static final int O_DSYNC = 8;


    private EnumDelegate delegate;
    private String identifier;

    FileIOMode(String identifier, int code, String name, String displayText) {
        this.identifier = identifier;
        this.delegate = new EnumDelegate(code, name, displayText);
    }

    @Override
    public EnumDelegate getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(EnumDelegate delegate) {
    }

    @Override
    public int getCode() {
        return delegate.getCode();
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public String getDisplayText() {
        return this.delegate.getDisplayText();
    }

}
