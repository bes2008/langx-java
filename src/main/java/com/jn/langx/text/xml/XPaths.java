package com.jn.langx.text.xml;


/**
 * 根节点：/ <br>
 * 当前节点：.<br>
 * 父节点：..<br>
 * 父子关系: /<br>
 * 元素属性：@<br>
 * 元素节点：元素名<br>
 * 所有子元素节点：*<br>
 * 所有属性：@*<br>
 * 并列关系：|<br>
 * 约束关系：[]<br>
 *
 * @author jinuo.fang
 */
public class XPaths {


    // ---- 属性判断

    /**
     * 不包括指定属性
     *
     * @param attrName attribute name
     * @return expression
     */
    public static String notContainsAttr(String attrName) {
        return "name(@" + attrName + ")=''";
    }

    public static String attrNotEquals(String attrName, String value) {
        return "@" + attrName + "!=" + value;
    }

    // ---------------------------------逻辑表达式--------------------------------------//

    /**
     * ( a or b ) and c
     *
     * @param expA
     * @param expB
     * @param expC
     * @return expression
     */
    public static String aorB_and_C(String expA, String expB, String expC) {
        return "(" + expA + " or " + expB + ") and " + expC;
    }

    /**
     * a or ( b and c)
     *
     * @param expA
     * @param expB
     * @param expC
     * @return expression
     */
    public static String a_or_BandC(String expA, String expB, String expC) {
        return expA + " or (" + expB + " and " + expC + ")";
    }
}
