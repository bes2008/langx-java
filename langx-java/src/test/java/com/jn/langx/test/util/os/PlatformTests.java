package com.jn.langx.test.util.os;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.os.OsArch;
import com.jn.langx.util.os.Platform;
import org.junit.Assert;
import org.junit.Test;

public class PlatformTests {
    @Test
    public void testCurrentOSArch(){
        OsArch osArch = Platform.osArch;
        System.out.println(osArch);
        System.out.println(StringTemplates.formatWithPlaceholder("OS is {} bit",osArch.getBit()));
        System.out.println(StringTemplates.formatWithPlaceholder("JVM is {} bit",Platform.JVM_BITs));
    }

    @Test
    public void testOsArchFind(){
        Assert.assertSame(OsArch.findByName("x86"), OsArch.X86_32);
        Assert.assertSame(OsArch.findByName("x32"), OsArch.X86_32);
        Assert.assertSame(OsArch.findByName("x86_64"), OsArch.X86_64);
        Assert.assertSame(OsArch.findByName("aarch64"), OsArch.AARCH_64);
        Assert.assertSame(OsArch.findByName("amd64"), OsArch.AMD_64);
        Assert.assertSame(OsArch.findByName("ARM32"), OsArch.ARM_32);
        Assert.assertSame(OsArch.findByName("LoongArch64"), OsArch.LOONGARCH_64);
        Assert.assertSame(OsArch.findByName("RISC-V"), OsArch.RISCV_32);
    }
}
