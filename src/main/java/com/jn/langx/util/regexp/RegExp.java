package com.jn.langx.util.regexp;

import com.jn.langx.script.ScriptLanguage;
import com.jn.langx.script.SimpleScriptEngineFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这是一个正则表达式验证。<br>
 * 1）包含了常用的表达式：{@link #URL}, {@link #EMAIL}, {@link #CHINESE}, {@link #TEL}等等。<br>
 * 2）可以进行忽略大小写的验证,通过{@link #setIgnoreCase(boolean)}，然后调用{@link #test(String)}即可。<br>
 * 3）提供了对于JavaScript中的RegExp的支持
 *
 * @author fjn
 */
public class RegExp {

    // URL
    public static final String URL = "";
    // Email
    public static final String EMAIL = "^[a-z\\d]+(\\.[a-z\\d]+)*@([\\da-z](-[\\da-z])?)+(\\.{1,2}[a-z]+)+$";
    // 中文
    public static final String CHINESE = "[\u4e00-\u9fa5]";

    public static final String JsTestMethod = "test";

    /**
     * 电话号码：
     * 1、手机号码验证  /^((86|\+86)?1)[358]\d{9}$/g ；
     * 2、座机号验证  /^(\d{3,4}-)?\d{7,8}(-\d{1,4})?$/g；
     * // 座机号,支持3-4位区号、7-8位直播号，1-4位分机号
     * 这个验证包括了手机号、座机号的验证。
     */
    public static final String TEL = "^((86|\\+86)?1)[358]\\d{9}$|^(\\d{3,4}-)?\\d{7,8}(-\\d{1,4})?$";

    /**
     * 提供了对于JDK中的Pattern与Matcher的调用入口
     *
     * @param pattern
     * @param src
     * @return
     */
    public static Matcher getMatcher(String pattern, String src) {
        return Pattern.compile(pattern).matcher(src);
    }

    private String pattern = "";
    private boolean isIgnoreCase = false;// 忽略大小写

    public RegExp(String pattern) {
        this.pattern = pattern;
    }

    public RegExp(String pattern, boolean ignore) {
        this.pattern = pattern;
        this.isIgnoreCase = ignore;
    }

    public void setIgnoreCase(boolean ignore) {
        isIgnoreCase = ignore;
    }

    /**
     * 判断字符串str是否匹配指定的模式
     *
     * @param str 字符串
     * @return boolean
     */
    public boolean test(String str) {
        if (isIgnoreCase) {
            try {

                ScriptEngine engine = SimpleScriptEngineFactory.getScriptEngine(ScriptLanguage.JavaScript);
                String script = "var reg=/" + this.pattern + "/gi;";
                engine.eval(script);
                Object regexp = engine.get("reg");
                Invocable inv = (Invocable) engine;
                Object returnValue = inv.invokeMethod(regexp, "test", new Object[]{str});
                Boolean match = (Boolean) returnValue;
                return match;
            } catch (Exception e) {
                throw new RegexpPatternException(this.pattern);
            }
        } else
            return str.matches(pattern);
    }

    /**
     * 这个方法可以多次调用Js中Reg的test方法
     *
     * @param str
     * @return
     */
    public Object test(String str, MutilTest mutilTest) {
        if (isIgnoreCase) {
            try {

                ScriptEngine engine = SimpleScriptEngineFactory.getScriptEngine(ScriptLanguage.JavaScript);
                String script = "var reg=/" + this.pattern + "/gi;";
                engine.eval(script);
                Object regexp = engine.get("reg");
                Invocable inv = (Invocable) engine;
				/*
				Object returnValue=inv.invokeMethod(regexp, "test", new Object[]{str});
				Boolean match=(Boolean)returnValue;
				return match;
				*/
                return mutilTest.invoke(inv, regexp, new Object[]{str});
            } catch (Exception e) {
                throw new RegexpPatternException(this.pattern);
            }
        } else
            return str.matches(pattern);
    }

    public static void main(String[] args) {
	/*
		RegExp regexp=new RegExp(".*"+RegExp.CHINESE+".*");
		regexp.setIgnoreCase(true);
		boolean dd=regexp.test("你好xx");
		System.out.println(dd);

		String pattern="^http:\\/\\/pic\\.cnitblog\\.com\\/face\\/\\d+\\/\\d+\\.png$";
		regexp=new RegExp(pattern);
		regexp.setIgnoreCase(true);
		dd=regexp.test("http://pic.cnitblog.com/face/569997/20131212230749.png");
		System.out.println(dd);

		pattern="<img\\s+src=[',\"]http:\\/\\/pic\\.cnitblog\\.com\\/face\\/\\d+\\/\\d+\\.png[',\"]\\s+\\/>";
		String src="<img src='http://pic.cnitblog.com/face/569997/20131212230749.png' />";
		System.out.println(src.matches(pattern));

	*/

        // 找出下面文本中有多少个"Java"，忽略大小写
        String src = "Java,javawoefsflewjoavejavaejsjavajavajavajavajavajavaeajavja";
        RegExp regexp = new RegExp("java", true);
        int count = (Integer) regexp.test(src, new MutilTest() {

            @Override
            public Object invoke(Invocable engine, Object jsRegexp, Object[] params) {
                int i = 0;
                try {
                    while ((Boolean) engine.invokeMethod(jsRegexp, JsTestMethod, params)) {
                        i++;
                    }
                } catch (ScriptException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return i;
            }

        });

        System.out.println(count);
    }


    interface MutilTest {
        public Object invoke(Invocable engine, Object obj, Object[] params);
    }
}