package com.jn.langx.util.os;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;
import com.jn.langx.util.function.Predicate;

import java.util.EnumSet;

public enum OsArch implements CommonEnum {
    X86_64(1001, "x86_64", "64 位 x86 架构", OsArchSeries.X86),
    X64(1002, "x64", "64 位 x86 架构", OsArchSeries.X86),
    X86(1003, "x86", "32 位 x86 架构", OsArchSeries.X86),
    X32(1004, "x32", "32 位 x86 架构", OsArchSeries.X86),
    AMD64(1005, "amd64", "64 位 x86 架构", OsArchSeries.X86),
    ARM64(2001, "arm64", "64 位 ARM 架构", OsArchSeries.ARM),
    ARM(2002, "arm", "32 位 ARM 架构", OsArchSeries.ARM),
    AARCH64(2003, "aarch64", "64 位 ARM 架构", OsArchSeries.ARM), // ARM v8
    PPC64LE(3001, "ppc64le", "64 位 PowerPC 架构", OsArchSeries.POWER),
    PPC64(3002, "ppc64", "64 位 PowerPC 架构", OsArchSeries.POWER),
    S390X(4001, "s390x", "64 位 IBM System z 架构", OsArchSeries.Z),
    S390(4002, "s390", "31 位 IBM System z 架构", OsArchSeries.Z),
    MIPS64(5001, "mips64", "64 位 MIPS 架构", OsArchSeries.MIPS),
    MIPS(5002, "mips", "32 位 MIPS 架构", OsArchSeries.MIPS),
    SH(6001, "sh", "SuperH 架构", OsArchSeries.SH),
    ;
    EnumDelegate delegate;
    private OsArchSeries series;

    OsArch(int code, String name, String displayName, OsArchSeries series) {
        this.delegate = new EnumDelegate(code, name, displayName);
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
