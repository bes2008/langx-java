package com.jn.langx.test.util.os.hardware.cpu;

import com.jn.langx.commandline.streamhandler.OutputExtractExecuteStreamHandler;

abstract class GetCpuIdStreamHandler extends OutputExtractExecuteStreamHandler<String> {
    public abstract String getCpuId();
}
