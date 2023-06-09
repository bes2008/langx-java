package com.jn.langx.text.xml;


import com.jn.langx.util.Emptys;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;

import java.util.List;

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
 * <p>
 * <p>
 * 避免注入的方案：
 * https://cheatsheetseries.owasp.org/cheatsheets/Injection_Prevention_in_Java_Cheat_Sheet.html
 *
 * @author jinuo.fang
 */
public class XPaths {
    private XPaths() {

    }

    // ---- 属性判断

    /**
     * 不包括指定属性
     *
     * @param attrName attribute name
     * @return expression
     */
    public static String notContainsAttr(String attrName) {
        XPathInjectionPreventionHandler handler = XPathInjectionPreventionHandler.getInstance();
        attrName = handler.apply(attrName);
        return "name(@" + attrName + ")=''";
    }

    public static String attrNotEquals(String attrName, String value) {
        XPathInjectionPreventionHandler handler = XPathInjectionPreventionHandler.getInstance();
        attrName = handler.apply(attrName);
        value = handler.apply(value);
        return "@" + attrName + "!=" + value;
    }

    // ---------------------------------逻辑表达式--------------------------------------//

    /**
     * ( a or b ) and c
     *
     * @return expression
     */
    public static String aorB_and_C(String expA, String expB, String expC) {
        XPathInjectionPreventionHandler handler = XPathInjectionPreventionHandler.getInstance();
        expA = handler.apply(expA);
        expB = handler.apply(expB);
        expC = handler.apply(expC);
        return "(" + expA + " or " + expB + ") and " + expC;
    }

    /**
     * a or ( b and c)
     *
     * @return expression
     */
    public static String a_or_BandC(String expA, String expB, String expC) {
        XPathInjectionPreventionHandler handler = XPathInjectionPreventionHandler.getInstance();
        expA = handler.apply(expA);
        expB = handler.apply(expB);
        expC = handler.apply(expC);
        return expA + " or (" + expB + " and " + expC + ")";
    }

    public static String wrapXpath(String xpath, boolean usingCustomNamespace, String namespacePrefix) {
        if (usingCustomNamespace && Emptys.isNotEmpty(xpath)) {
            return XPaths.wrapXpath(xpath, namespacePrefix);
        }
        return xpath;
    }

    public static String wrapXpath(String xpathExpr, final String namespacePrefix) {
        boolean startWithSlash = Strings.startsWith(xpathExpr, "/");
        String[] segments = Strings.split(xpathExpr, "/");
        final String prefix = namespacePrefix + ":";
        List<String> prefixedSegments = Pipeline.of(segments).clearNulls().map(new Function<String, String>() {
            @Override
            public String apply(String segment) {
                return Strings.startsWith(segment, prefix) ? segment : (prefix + segment);
            }
        }).asList();
        return (startWithSlash ? "/" : "") + Strings.join("/", prefixedSegments);
    }
}
