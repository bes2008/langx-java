package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.id.vm.UID;
import com.jn.langx.util.id.vm.VMID;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.net.Nets;

public class VMIdGenerator implements IdGenerator<byte[]> {
    private byte[] address;

    @Override
    public String get(byte[] address) {
        if (address == null) {
            address = this.address;
            if (address == null) {
                try {
                    String ip = Nets.getLocalIp();
                    this.address = ip.getBytes(Charsets.UTF_8);
                    address = this.address;
                } catch (Exception ex) {
                    throw Throwables.wrapAsRuntimeException(ex);
                }
            }
        }
        return new VMID(address, Platform.processId, new UID()).toString();
    }

    @Override
    public String get() {
        return get(null);
    }
}
