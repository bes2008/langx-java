package com.jn.langx.util.os;

import com.jn.langx.Delegatable;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

/**
 * <pre>
 *  1. JVM 是 JDK的核心，JVM实现有下列：
 *     HotSpot： 由Sun公司开发,伴随 SunJDK发布。后来由 Oracle 收购。
 *     BEA JRockit VM: BEA Systems开发的JVM，专注于高性能和实时Java应用，后来被Oracle收购并与hotspot整合，目前已基本退出市场。
 *     OpenJ9： 由IBM公司开发, 后来贡献给 Eclipse基金会。
 *     Zing VM: 由 Azul Systems 公司开发一个商业JVM，专为低延迟和高吞吐量设计
 *     Dalvik： 是早期的 Android设备上使用的 vm
 *     ART: 是当前的 Android 设备上使用的 vm
 *     Kaffe: 基本不用的 ，多用于嵌入式
 *
 *  2. JDK
 *  基于上面的JVM实现, 涌现了一大批的JDK：
 *  Open JDK: 支持 hotspot, open j9 两种 jvm 实现, 默认支持 hotspot
 *
 *  3. 下列JDK是基于OpenJDK 开发的：
 *    Eclipse: AdoptOpenJDK
 *    Alibaba: Dragonwell
 *    Amazon: Corretto
 *    Azul: Zulu, Zing
 *    BellSoft: Liberica
 *    Red Hat: Red Hat Builds of OpenJDK
 *    SAP: SapMachine
 *    GraalVM: Oracle开发的，一个高性能的JVM，支持多种语言运行时，包括Java、JavaScript、Python等
 *
 * </pre>
 */
public enum JVMImpl implements CommonEnum {
    HOTSPOT(10, "HotSpot", "hotspot vm"),
    OPEN_J9(20, "OpenJ9", "Open J9 vm"),
    ZING_VM(30, "Zing VM", "hotspot vm"),
    ART(40, "ART", "hotspot vm", true),

    // 下面是过时的vm
    JROCKIT(90, "JRockit", "JRockit vm"),
    DALVIK(91, "Dalvik", "Dalvik android vm", true),
    KAFFE(92, "kaffe", "kaffe jvm", true),
    ;
    private boolean runInAndroid;
    EnumDelegate delegate;



    JVMImpl(int code, String name, String displayText){
        this(code, name, displayText, false);
    }

    JVMImpl(int code, String name, String displayText, boolean runInAndroidDevice){
        this.delegate=new EnumDelegate(code,name,displayText);
        this.runInAndroid = runInAndroidDevice;
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public int getCode() {
        return this.delegate.getCode();
    }

    @Override
    public String getDisplayText() {
        return this.delegate.getDisplayText();
    }

    public boolean isRunInAndroid() {
        return runInAndroid;
    }
}
