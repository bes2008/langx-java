package com.jn.langx.util.os;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;
import com.jn.langx.util.function.Predicate;

import java.util.EnumSet;

public enum OsArch implements CommonEnum {
    IA64(1001, "ia64", "64 位 Intel Itanium 处理器", OsArchSeries.IA, 64),
    X86_64(2001, "x86_64", "64 位 x86 架构", OsArchSeries.X86, 64),
    X64(2002, "x64", "64 位 x86 架构", OsArchSeries.X86, 64),
    X86(2003, "x86", "32 位 x86 架构", OsArchSeries.X86, 32),
    X32(2004, "x32", "32 位 x86 架构", OsArchSeries.X86, 32),
    I386(2005, "i386", "32 位 x86 架构", OsArchSeries.X86, 32),
    AMD64(3005, "amd64", "64 位 x86 架构", OsArchSeries.X86, 64),
    ARM64(3001, "arm64", "64 位 ARM 架构", OsArchSeries.ARM, 64),
    ARM(3002, "arm", "32 位 ARM 架构", OsArchSeries.ARM, 32),
    AARCH64(3003, "aarch64", "64 位 ARM 架构", OsArchSeries.ARM, 64), // ARM v8
    PPC64LE(4001, "ppc64le", "64 位 PowerPC 架构", OsArchSeries.POWER, 64),
    PPC64(4002, "ppc64", "64 位 PowerPC 架构", OsArchSeries.POWER, 64),
    POWERPC(4003, "powerpc", "32 位 PowerPC 架构", OsArchSeries.POWER, 32),
    S390X(5001, "s390x", "64 位 IBM System z 架构", OsArchSeries.Z, 64),
    S390(5002, "s390", "31 位 IBM System z 架构", OsArchSeries.Z, 31),
    MIPS64(6001, "mips64", "64 位 MIPS 架构", OsArchSeries.MIPS, 64),
    MIPS(6002, "mips", "32 位 MIPS 架构", OsArchSeries.MIPS, 32),
    SH(7001, "sh", "SuperH 架构", OsArchSeries.SH, 64);
    EnumDelegate delegate;
    private OsArchSeries series;
    private int bit;

    OsArch(int code, String name, String displayName, OsArchSeries series, int bit) {
        this.delegate = new EnumDelegate(code, name, displayName);
        this.series = series;
        this.bit = bit;
    }

    public int getBit() {
        return bit;
    }

    public static OsArch findByName(String osArch) {
        final String theName = Strings.lowerCase(osArch);
        return Pipeline.of(EnumSet.allOf(OsArch.class))
                .findFirst(new Predicate<OsArch>() {
                    @Override
                    public boolean test(OsArch arch) {
                        return arch.getName().contains(theName);
                    }
                });
    }

    public static boolean isArchCompatible(OsArchSeries osArchSeries, String osArch) {
        OsArch arch = findByName(osArch);
        if (arch != null && arch.series == osArchSeries) {
            return true;
        }
        return false;
    }



    public static boolean isX86Compatible(String osArch) {
        return isArchCompatible(OsArchSeries.X86, osArch);
    }

    public static boolean isARMCompatible(String osArch) {
        return isArchCompatible(OsArchSeries.ARM, osArch);
    }

    public static boolean isPowerCompatible(String osArch) {
        return isArchCompatible(OsArchSeries.POWER, osArch);
    }

    @Override
    public int getCode() {
        return this.delegate.getCode();
    }

    @Override
    public String getDisplayText() {
        return this.delegate.getDisplayText();
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }
}
