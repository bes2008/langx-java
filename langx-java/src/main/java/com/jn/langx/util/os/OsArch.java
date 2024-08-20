package com.jn.langx.util.os;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;
import com.jn.langx.util.function.Predicate;

import java.util.EnumSet;

/**
 *
 *
 *
 *
 * <pre>
 *     在民用领域得到广泛应用的国产CPU主要包括以下几个品牌：
 *
 * 龙芯 (Loongson)
 *     架构: 最初基于 MIPS 架构，后转向自主研发的 LoongArch 架构。
 *     应用: 龙芯CPU已经应用于桌面电脑、服务器等多个民用领域。例如，方正数码发布了基于龙芯3A3000 CPU的台式机与笔记本电脑。
 * 飞腾 (Phytium)
 *     架构: 基于 ARM 架构。
 *     应用: 飞腾CPU除了在服务器和工作站领域有广泛应用外，也开始进入桌面和移动计算市场。
 * 华为鲲鹏 (Kunpeng)
 *     架构: 基于 ARM 架构。
 *     应用: 华为鲲鹏系列处理器主要用于服务器和工作站，但也有部分产品进入了民用市场。
 * 兆芯 (Zhaoxin)
 *     架构: 基于 x86 架构。
 *     应用: 兆芯CPU主要应用于桌面级电脑，满足日常办公和家用需求。
 * 海光 (Hygon)
 *     架构: 基于 x86 架构。
 *     应用: 海光CPU虽然主要面向服务器市场，但其技术也可能间接影响到民用领域的计算机系统
 * </pre>
 */
public enum OsArch implements CommonEnum {
    IA64(1001, "ia64", "64 位 Intel Itanium 处理器", OsArchSeries.IA, 64),
    X86_64(2001, "x86_64", "64 位 x86 架构", OsArchSeries.X86, 64, "x86-64","x64"),
    X86(2002, "x86_32", "32 位 x86 架构", OsArchSeries.X86, 32, "x86-32","x32","x86"),
    I386(2005, "i386", "32 位 x86 架构", OsArchSeries.X86, 32),
    AMD64(2006, "amd64", "64 位 x86 架构", OsArchSeries.X86, 64),
    ARM64(3001, "arm64", "64 位 ARM 架构", OsArchSeries.ARM, 64),
    ARM(3002, "arm", "32 位 ARM 架构", OsArchSeries.ARM, 32),
    AARCH64(3003, "aarch64", "64 位 ARM 架构", OsArchSeries.ARM, 64), // ARM v8
    PPC64LE(4001, "ppc64le", "64 位 PowerPC 架构", OsArchSeries.POWER, 64),
    PPC64(4002, "ppc64", "64 位 PowerPC 架构", OsArchSeries.POWER, 64),
    POWERPC(4003, "powerpc", "32 位 PowerPC 架构", OsArchSeries.POWER, 32, "ppc", "power"),
    S390X(5001, "s390x", "64 位 IBM System z 架构", OsArchSeries.Z, 64),
    S390(5002, "s390", "32 位 IBM System z 架构", OsArchSeries.Z, 32),
    MIPS64(6001, "mips64", "64 位 MIPS 架构", OsArchSeries.MIPS, 64),
    MIPS(6002, "mips", "32 位 MIPS 架构", OsArchSeries.MIPS, 32),
    SPARC_V9(7001, "sparcv9", "64 位 SPARC 架构", OsArchSeries.SPARC, 64 ),
    SPARC(7001, "sparc", "32 位 SPARC 架构", OsArchSeries.SPARC, 32 ),
    PA_RISC(8001, "pa_risc", "32 位 PA-RISC 架构", OsArchSeries.PA_RISC, 32, "pa-risc","pa"),
    ALPHA(9001, "alpha", "64 位 Alpha 架构", OsArchSeries.ALPHA, 64),
    LOONGARCH64(10001, "loongarch64", "64 位龙芯架构", OsArchSeries.LOONGARCH, 64),
    LOONGARCH(10002, "loongarch", "32 位龙芯架构", OsArchSeries.LOONGARCH, 32),

    RISC_V_64(11001, "riscv64", "64 位RISC-V架构",OsArchSeries.RISC_V,64),

    VAX(98001,"vax","32 位 VAX 架构", OsArchSeries.VAX,32),
    SH(99001, "sh", "SuperH 架构", OsArchSeries.SH, 64),
    ;
    EnumDelegate delegate;
    private OsArchSeries series;
    private int bit;

    private String[] aliases;

    OsArch(int code, String name, String displayName, OsArchSeries series, int bit, String... aliases) {
        this.delegate = new EnumDelegate(code, name, displayName);
        this.series = series;
        this.bit = bit;
        this.aliases = aliases;
    }

    public int getBit() {
        return bit;
    }

    public boolean is64Bit(){
        return bit==64;
    }

    public static OsArch findByName(String osArch) {
        final String theName = Strings.lowerCase(osArch);
        return Pipeline.of(EnumSet.allOf(OsArch.class))
                .findFirst(new Predicate<OsArch>() {
                    @Override
                    public boolean test(OsArch arch) {
                        boolean c = Strings.contains(theName, arch.getName(), true);
                        if (!c && !Objs.isEmpty(arch.aliases)){
                            c = Pipeline.of(arch.aliases).anyMatch(new Predicate<String>() {
                                @Override
                                public boolean test(String alias) {
                                    return Strings.contains(theName, alias, true);
                                }
                            });
                        }
                        return c;
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
