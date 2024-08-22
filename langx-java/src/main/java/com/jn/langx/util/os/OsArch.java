package com.jn.langx.util.os;

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
    X86_64(2001, "x86_64", "64 位 x86 架构", OsArchSeries.X86, 64, "x8664","x86-64","x64","ia32e","em64t"),
    X86_32(2002, "x86_32", "32 位 x86 架构", OsArchSeries.X86, 32, "x86-32","x86_32","x32","x86"),
    I686(2005, "i686", "32 位 x86 架构", OsArchSeries.X86, 32),
    I386(2005, "i386", "32 位 x86 架构", OsArchSeries.X86, 32),
    AMD_64(2006, "amd_64", "64 位 x86 架构", OsArchSeries.X86, 64, "amd-64","amd64"),
    IA_64(1001, "itanium_64", "64 位 Intel Itanium 处理器", OsArchSeries.IA, 64, "ia64","ia-64","ia_64", "itanium64", "itanium-64", "itanium_64"),
    IA_32(1001, "itanium_32", "32 位 Intel Itanium 处理器", OsArchSeries.IA, 32, "itanium32", "itanium-32", "itanium_32", "itanium","ia64n"),
    ARM_64(3001, "arm_64", "64 位 ARM 架构", OsArchSeries.ARM, 64, "arm64","arm_64","arm-64"),
    ARM_32(3002, "arm_32", "32 位 ARM 架构", OsArchSeries.ARM, 32, "arm32", "arm-32","arm"),
    AARCH_64(3003, "aarch_64", "64 位 ARM 架构", OsArchSeries.ARM, 64,"aarch64","aarch-64"), // ARM v8
    PPCLE_64(4001, "ppcle_64", "64 位 PowerPC 架构", OsArchSeries.POWER, 64, "ppcle64","ppc64le","ppcle-64"),
    PPCLE_32(4002, "ppcle_32", "64 位 PowerPC 架构", OsArchSeries.POWER, 64, "ppcle32","ppc32le","ppcle-32","ppcle"),
    PPC_64(4003, "ppc_64", "64 位 PowerPC 架构", OsArchSeries.POWER, 64,"ppc64","ppc-64","ppc_64"),
    PPC32(4004, "ppc_32", "32 位 PowerPC 架构", OsArchSeries.POWER, 32, "powerpc","ppc32", "power","ppc"),
    S390_64(5001, "s390_64", "64 位 IBM System z 架构", OsArchSeries.Z, 64,"s390x","s390-64"),
    S390_32(5002, "s390", "32 位 IBM System z 架构", OsArchSeries.Z, 32),
    MIPSEL_64(6001, "mipsel_64", "64 位 MIPS 架构", OsArchSeries.MIPS, 64,"mipsel-64","mips64el","mipsel64"),
    MIPS_64(6002, "mips_64", "64 位 MIPS 架构", OsArchSeries.MIPS, 64,"mips64","mips-64"),
    MIPSEL_32(6003, "mipsel_32", "32 位 MIPS 架构", OsArchSeries.MIPS, 32,"mipsel-32","mips32el","mipsel32","mipsel"),
    MIPS_32(6004, "mips_32", "32 位 MIPS 架构", OsArchSeries.MIPS, 32,"mips"),
    SPARC_64(7001, "sparc_64", "64 位 SPARC 架构", OsArchSeries.SPARC, 64,"sparcv9_64","sparcv9_64","sparcv9","sparc64","sparc-64"),
    SPARC_32(7001, "sparc_32", "32 位 SPARC 架构", OsArchSeries.SPARC, 32 ,"sparc"),
    PA_RISC_32(8001, "pa_risc", "32 位 PA-RISC 架构", OsArchSeries.PA_RISC, 32, "pa-risc","pa"),
    ALPHA_64(9001, "alpha_64", "64 位 Alpha 架构", OsArchSeries.ALPHA, 64,"alpha64","alpha-64", "alpha"),
    LOONGARCH_64(10001, "loongarch_64", "64 位龙芯架构", OsArchSeries.LOONGARCH, 64, "loongarch64", "loongarch-64"),
    LOONGARCH_32(10002, "loongarch_32", "32 位龙芯架构", OsArchSeries.LOONGARCH, 32,"loongarch"),

    RISCV_64(11001, "riscv_64", "64 位RISC-V架构",OsArchSeries.RISC_V,64, "riscv-64","riscv64","risc-v-64","risc-v_64","risc_v-64","risc_v_64"),
    RISCV_128(11002, "riscv_128", "128 位RISC-V架构",OsArchSeries.RISC_V,128, "riscv-128","riscv128","risc-v-128","risc-v_128","risc_v-128","risc_v_128"),
    RISCV_32(11003, "riscv_32", "32 位RISC-V架构",OsArchSeries.RISC_V,32, "riscv-32","riscv32","riscv","risc-v-32","risc-v_32","risc-v","risc_v-32","risc_v_32","risc_v"),

    SW_64(12001, "sw_64","64位神威架构",OsArchSeries.SW, 64, "sw64","sw-64"),

    VAX_32(98001,"vax_32","32 位 VAX 架构", OsArchSeries.VAX,32,"vax"),
    SH_64(99001, "sh_64", "SuperH 架构", OsArchSeries.SH, 64,  "sh-64","sh"),
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
                        boolean c = Pipeline.of(arch.aliases).add(arch.getName()).anyMatch(new Predicate<String>() {
                            @Override
                            public boolean test(String alias) {
                                return Strings.contains(theName, alias, true);
                            }
                        });
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
