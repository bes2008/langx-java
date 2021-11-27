/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.jn.langx.util.os;

import com.jn.langx.util.Strings;

import java.util.Locale;

/**
 * Condition that tests the OS type.
 */
public final class OS {
    private static final String FAMILY_OS_400 = "os/400";

    private static final String FAMILY_Z_OS = "z/os";

    private static final String FAMILY_WIN9X = "win9x";

    private static final String FAMILY_OPENVMS = "openvms";

    private static final String FAMILY_UNIX = "unix";

    private static final String FAMILY_TANDEM = "tandem";

    private static final String FAMILY_MAC = "mac";

    private static final String FAMILY_DOS = "dos";

    private static final String FAMILY_NETWARE = "netware";

    private static final String FAMILY_OS_2 = "os/2";

    private static final String FAMILY_WINDOWS = "windows";

    private static final String FAMILY_LINUX = "linux";

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.US);

    private static final String OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.US);

    private static final String OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.US);

    private static final String PATH_SEP = System.getProperty("path.separator");

    /**
     * Default constructor
     */
    private OS() {
    }

    /**
     * Determines if the OS on which Ant is executing matches the given OS
     * family. * Possible values:<br />
     * <ul>
     * <li>dos</li>
     * <li>mac</li>
     * <li>netware</li>
     * <li>os/2</li>
     * <li>tandem</li>
     * <li>unix</li>
     * <li>windows</li>
     * <li>win9x</li>
     * <li>z/os</li>
     * <li>os/400</li>
     * </ul>
     *
     * @param family the family to check for
     * @return true if the OS matches
     */
    private static boolean isFamily(final String family) {
        return isOs(family, null, null, null);
    }


    public static boolean isFamilyAIX() {
        return isFamily("aix");
    }

    public static boolean isFamilySolaris() {
        return isFamily("solaris");
    }

    public static boolean isFamilySunOs() {
        return isFamily("sunos");
    }

    public static boolean isFamilyDOS() {
        return isFamily(FAMILY_DOS);
    }

    public static boolean isFamilyMac() {
        return isFamily(FAMILY_MAC);
    }

    public static boolean isMaxOSX() {
        return isFamilyMac() && isOs(null, "mac os x", null, null);
    }

    public static boolean isFamilyHP_UX() {
        return isFamily("hp-ux");
    }

    public static boolean isFamilyIrix() {
        return isFamily("irix");
    }

    public static boolean isFamilyNetware() {
        return isFamily(FAMILY_NETWARE);
    }

    public static boolean isFamilyOS2() {
        return isFamily(FAMILY_OS_2);
    }

    public static boolean isFamilyTandem() {
        return isFamily(FAMILY_TANDEM);
    }

    public static boolean isFamilyUnix() {
        return isFamily(FAMILY_UNIX);
    }

    public static boolean isFamilyWindows() {
        return isFamily(FAMILY_WINDOWS);
    }

    public static boolean isFamilyWin9x() {
        return isFamily(FAMILY_WIN9X);
    }

    public static boolean isWindows95() {
        return isOs("windows", "windows 9", null, "4.0");
    }

    public static boolean isWindows98() {
        return isOs("windows", "windows 9", null, "4.1");
    }

    public static boolean isWindowsME() {
        return isOs("windows", null, null, "4.9");
    }

    public static boolean isWindowsNT() {
        return isOs("windows", "windows nt", null, "4.9");
    }

    public static boolean isWindows2000() {
        return isOs("windows", null, null, "5.0");
    }

    public static boolean isWindowsXP() {
        return isOs("windows", null, null, "5.1");
    }

    public static boolean isWindowsVista() {
        return isOs("windows", null, null, "6.0");
    }

    public static boolean isWindows7() {
        return isOs("windows", null, null, "6.1");
    }

    public static boolean isFamilyZOS() {
        return isFamily(FAMILY_Z_OS);
    }

    public static boolean isFamilyOS400() {
        return isFamily(FAMILY_OS_400);
    }

    public static boolean isFamilyOpenVms() {
        return isFamily(FAMILY_OPENVMS);
    }

    /**
     * @since 4.1.0
     */
    public static boolean isFamilyLinux() {
        return isFamily(FAMILY_LINUX);
    }

    /**
     * @since 4.1.0
     */
    public static boolean isLinux(){
        return isFamilyLinux();
    }

    /**
     * Determines if the OS on which Ant is executing matches the given OS name.
     *
     * @param name the OS name to check for
     * @return true if the OS matches
     */
    public static boolean isName(final String name) {
        return isOs(null, name, null, null);
    }

    /**
     * Determines if the OS on which Ant is executing matches the given OS
     * architecture.
     *
     * @param arch the OS architecture to check for
     * @return true if the OS matches
     */
    public static boolean isArch(final String arch) {
        return isOs(null, null, arch, null);
    }

    /**
     * Determines if the OS on which Ant is executing matches the given OS
     * version.
     *
     * @param version the OS version to check for
     * @return true if the OS matches
     */
    public static boolean isVersion(final String version) {
        return isOs(null, null, null, version);
    }

    /**
     * Determines if the OS on which Ant is executing matches the given OS
     * family, name, architecture and version
     *
     * @param familyFlag  The OS family
     * @param nameFlag    The OS name
     * @param archFlag    The OS architecture
     * @param versionFlag The OS version
     * @return true if the OS matches
     */
    public static boolean isOs(final String familyFlag, final String nameFlag, final String archFlag, final String versionFlag) {
        boolean retValue = false;

        if (familyFlag != null || nameFlag != null || archFlag != null || versionFlag != null) {

            boolean isFamily = true;
            boolean isName = true;
            boolean isArch = true;
            boolean isVersion = true;

            if (familyFlag != null) {
                if (familyFlag.equals(FAMILY_WINDOWS)) {
                    isFamily = OS_NAME.contains(FAMILY_WINDOWS);
                } else if (familyFlag.equals(FAMILY_OS_2)) {
                    isFamily = OS_NAME.contains(FAMILY_OS_2);
                } else if (familyFlag.equals(FAMILY_NETWARE)) {
                    isFamily = OS_NAME.contains(FAMILY_NETWARE);
                } else if (familyFlag.equals(FAMILY_DOS)) {
                    isFamily = PATH_SEP.equals(";") && !isFamily(FAMILY_NETWARE);
                } else if (familyFlag.equals(FAMILY_MAC)) {
                    isFamily = OS_NAME.contains(FAMILY_MAC);
                } else if (familyFlag.equals(FAMILY_TANDEM)) {
                    isFamily = OS_NAME.contains("nonstop_kernel");
                } else if (familyFlag.equals(FAMILY_UNIX)) {
                    isFamily = PATH_SEP.equals(":") && !isFamily(FAMILY_OPENVMS) && (!isFamily(FAMILY_MAC) || OS_NAME.endsWith("x"));
                } else if (familyFlag.equals(FAMILY_WIN9X)) {
                    isFamily = isFamily(FAMILY_WINDOWS) && (OS_NAME.contains("95") || OS_NAME.contains("98") || OS_NAME.contains("me") || OS_NAME.contains("ce"));
                } else if (familyFlag.equals(FAMILY_Z_OS)) {
                    isFamily = OS_NAME.contains(FAMILY_Z_OS) || OS_NAME.contains("os/390");
                } else if (familyFlag.equals(FAMILY_OS_400)) {
                    isFamily = OS_NAME.contains(FAMILY_OS_400);
                } else if (familyFlag.equals(FAMILY_OPENVMS)) {
                    isFamily = OS_NAME.contains(FAMILY_OPENVMS);
                } else if (familyFlag.equals(FAMILY_LINUX)) {
                    isFamily = OS_NAME.contains("nux");
                } else {
                    isFamily = OS_NAME.contains(familyFlag);
                }
            }
            if (nameFlag != null) {
                isName = Strings.startsWith(OS_NAME, nameFlag);
            }
            if (archFlag != null) {
                isArch = archFlag.equals(OS_ARCH);
            }
            if (versionFlag != null) {
                isVersion = Strings.startsWith(OS_VERSION, versionFlag);
            }
            retValue = isFamily && isName && isArch && isVersion;
        }
        return retValue;
    }
}
