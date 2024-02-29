package com.jn.langx.util;

import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;

public class Validations {
    private Validations(){}
    /**
     * hostname 被分为两大部分标签与顶级域名:
     * <pre>label1.label2....lableN.top_domain</pre>
     *
     * 要求：
     * <pre>
     *     整个域名（包括顶级域.tld）最长255个字符。
     * </pre>
     *
     */
    public static boolean isValidRFC1123Hostname(String hostname){
        if(Strings.isEmpty(hostname) || Objs.length(hostname)>255){
            return false;
        }
        String[] labels=Strings.split(hostname,".",false,false);
        return Pipeline.of(labels).allMatch(new Predicate<String>() {
            @Override
            public boolean test(String label) {
                return isValidRFC1123Label(label);
            }
        });

    }

    /**
     * RFC1123 Label规则如下：
     * <pre>
     * 最多 63 个字符
     * 只能包含小写字母、数字，以及 '-'
     * 必须以字母数字开头
     * 必须以字母数字结尾
     * </pre>
     */
    private static boolean isValidRFC1123Label(String label){
        if(Strings.isEmpty(label)){
            return false;
        }
        if(label.length()>63){
            return false;
        }
        for (int i = 0; i < label.length(); i++) {
            char ch = label.charAt(i);
            // 判断是否为有效的字符
            if(ch!='-' && !Chars.isLowerCase(ch) && !Chars.isNumber(ch)){
                return false;
            }
            // 判断开头、结尾字符
            if(i==0 || (i == label.length()-1) ){
                if(!Chars.isLowerCase(ch) && !Chars.isNumber(ch)){
                    return false;
                }
            }
        }
        return true;
    }
}
