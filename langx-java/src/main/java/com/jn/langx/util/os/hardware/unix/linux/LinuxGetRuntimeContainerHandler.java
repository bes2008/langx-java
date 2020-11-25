package com.jn.langx.util.os.hardware.unix.linux;

import com.jn.langx.util.os.virtualization.GetRuntimeContainerHandler;

import java.io.IOException;

public class LinuxGetRuntimeContainerHandler extends GetRuntimeContainerHandler {
    @Override
    public boolean isRunningInContainer() {
        return false;
    }

    @Override
    public String getContainerName() {
        return null;
    }

    @Override
    public void start() throws IOException {

    }
}
