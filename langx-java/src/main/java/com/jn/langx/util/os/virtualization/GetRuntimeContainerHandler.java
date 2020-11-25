package com.jn.langx.util.os.virtualization;

import com.jn.langx.commandline.AbstractExecuteStreamHandler;

public abstract class GetRuntimeContainerHandler extends AbstractExecuteStreamHandler {
    /**
     * 判断是否运行在容器中，不限定docker容器
     *
     * @return
     */
    public abstract boolean isRunningInContainer();

    /**
     * 获取当前所在的容器名称
     *
     * @return
     */
    public abstract String getContainerName();

}
