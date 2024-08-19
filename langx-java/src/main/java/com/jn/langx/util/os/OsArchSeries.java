package com.jn.langx.util.os;

/**
 *  <pre>
 *  CPU指令集：
 *    RISC:
 *      ARM
 *      PowerPC
 *      RISC-V
 *      MIPS
 *      SPARC
 *      PA_RISC
 *      SuperH （SH）
 *      Alpha
 *    CISC:
 *      x86
 *      Z
 *      VAX
 *    EPIC:
 *      IA (Intel Itanium )
 *
 *  </pre>
 */
public enum OsArchSeries {

    // 属于RISC的：
    ARM,
    POWER,
    RISC_V,
    MIPS,
    SH, //SuperH
    SPARC,
    PA_RISC,
    ALPHA,
    // 属于 CISC的：
    X86,
    Z,
    VAX,
    // 属于 EPIC的：
    IA,
    ;
}
