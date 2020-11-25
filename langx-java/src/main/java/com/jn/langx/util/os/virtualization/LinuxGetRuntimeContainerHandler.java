package com.jn.langx.util.os.virtualization;

import com.jn.langx.util.io.IOs;
import com.jn.langx.util.struct.Holder;

import java.io.IOException;

class LinuxGetRuntimeContainerHandler extends GetRuntimeContainerHandler {
    private final Holder<RuntimeContainer> containerHolder = new Holder<RuntimeContainer>();

    public RuntimeContainer getContainer() {
        return containerHolder.get();
    }

    @Override
    public void start() throws IOException {
        containerHolder.reset();
        String content = IOs.readAsString(this.subProcessOutputStream);
        content.trim();

        // 没有在容器下
        if ("/".equals(content)) {
            return;
        }

        // docker
        if (content.startsWith("/docker/")) {
            containerHolder.set(new RuntimeContainer("docker"));
        }
    }
}
