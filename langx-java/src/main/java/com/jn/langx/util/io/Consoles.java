package com.jn.langx.util.io;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Strings;

public class Consoles {
    public static void format(String template, Object... args) {
        String message = StringTemplates.formatWithPlaceholder(template, args);
        System.out.println(message);
    }

    public static void log(Object... args) {
        System.out.println(Strings.join("\t", args));
    }
}
