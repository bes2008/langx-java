package com.jn.langx.test.util.os;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.os.OsArch;
import com.jn.langx.util.os.Platform;
import org.junit.Test;

public class PlatformTests {
    @Test
    public void testOSArch(){
        OsArch osArch = Platform.osArch;
        System.out.println(osArch);
        System.out.println(StringTemplates.formatWithPlaceholder("OS is {} bit",osArch.getBit()));
        System.out.println(StringTemplates.formatWithPlaceholder("JVM is {} bit",Platform.jvmBit));
    }
}
