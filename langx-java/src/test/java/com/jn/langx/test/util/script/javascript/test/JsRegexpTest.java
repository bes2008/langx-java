package com.jn.langx.test.util.script.javascript.test;

import com.jn.langx.util.script.javascript.regexp.JavaScriptRegExps;

import javax.script.Invocable;
import javax.script.ScriptException;

public class JsRegexpTest {

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
        JavaScriptRegExps regexp = new JavaScriptRegExps("java", true);
        int count = (Integer) regexp.test(src, new JavaScriptRegExps.MutilTest() {

            @Override
            public Object invoke(Invocable engine, Object jsRegexp, Object[] params) {
                int i = 0;
                try {
                    while ((Boolean) engine.invokeMethod(jsRegexp, JavaScriptRegExps.JsTestMethod, params)) {
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

}
