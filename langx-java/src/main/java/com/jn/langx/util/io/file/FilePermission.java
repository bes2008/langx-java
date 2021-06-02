package com.jn.langx.util.io.file;


import com.jn.langx.util.Radixs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public enum FilePermission {

    /***
     *           user group other
     *            RWX   RWX   RWX
     *  二进制     110   100   100
     *
     * 以0开始的数字，代表了是 8进制的。
     * 以 0x 开始的数字，代表是 16进制的
     */

    /**
     * read permission, owner
     */
    USR_R(00400),
    /**
     * write permission, owner
     */
    USR_W(00200),
    /**
     * execute/search permission, owner
     */
    USR_X(00100),
    /**
     * read permission, group
     */
    GRP_R(00040),
    /**
     * write permission, group
     */
    GRP_W(00020),
    /**
     * execute/search permission, group
     */
    GRP_X(00010),
    /**
     * read permission, others
     */
    OTH_R(00004),
    /**
     * write permission, others
     */
    OTH_W(00002),
    /**
     * execute/search permission, group
     */
    OTH_X(00001),
    /**
     * set-user-ID on execution
     */
    SUID(04000),
    /**
     * set-group-ID on execution
     */
    SGID(02000),
    /**
     * on directories, restricted deletion flag
     */
    STICKY(01000),
    // Composite:
    /**
     * read, write, execute/search by user
     */
    USR_RWX(USR_R, USR_W, USR_X),
    /**
     * read, write, execute/search by group
     */
    GRP_RWX(GRP_R, GRP_W, GRP_X),
    /**
     * read, write, execute/search by other
     */
    OTH_RWX(OTH_R, OTH_W, OTH_X);

    private final int val;

    FilePermission(int val) {
        this.val = val;
    }

    FilePermission(FilePermission... perms) {
        int val = 0;
        for (FilePermission perm : perms) {
            val |= perm.val;
        }
        this.val = val;
    }

    public boolean isIn(int mask) {
        return (mask & val) == val;
    }

    /**
     * 根据 unix 权限码转换成响应的权限，参数是 10进制的
     *
     * @param mask
     * @return
     */
    public static Set<FilePermission> fromMask(int mask) {
        final List<FilePermission> perms = new LinkedList<FilePermission>();
        for (FilePermission p : FilePermission.values()) {
            if (p.isIn(mask)) {
                perms.add(p);
            }
        }
        return new HashSet<FilePermission>(perms);
    }

    /**
     * 转换为 unix 权限码，返回数据是 10进制的
     *
     * @param perms
     * @return
     */
    public static int toMask(Set<FilePermission> perms) {
        int mask = 0;
        for (FilePermission p : perms) {
            mask |= p.val;
        }
        return mask;
    }

    public static String toOctal(int permissions) {
        return toOctal(fromMask(permissions));
    }

    public static String toOctal(Set<FilePermission> permissions) {
        return Radixs.toOtc(toMask(permissions));
    }

    public static String toBinary(int permissions){
        return toBinary(fromMask(permissions));
    }

    public static String toBinary(Set<FilePermission> permissions) {
        return Radixs.toBinary(toMask(permissions));
    }

    public int getVal(){
        return val;
    }
}