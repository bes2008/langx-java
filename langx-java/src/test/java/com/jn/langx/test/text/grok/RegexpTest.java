package com.jn.langx.test.text.grok;

import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

public class RegexpTest {

    public static void main(String[] args) {
        String pattern = "(?<timestamp>(?:[Jj]an(?:uary|uar)?|[Ff]eb(?:ruary|ruar)?|[Mm](?:a|ä)?r(?:ch|z)?|[Aa]pr(?:il)?|[Mm]a(?:y|i)?|[Jj]un(?:e|i)?|[Jj]ul(?:y|i)?|[Aa]ug(?:ust)?|[Ss]ep(?:tember)?|[Oo](?:c|k)?t(?:ober)?|[Nn]ov(?:ember)?|[Dd]e(?:c|z)(?:ember)?) (?:(?:0[1-9])|(?:[12][0-9])|(?:3[01])|[1-9]), (?>\\d\\d){1,2} (?:2[0123]|[01]?[0-9]):(?:[0-5][0-9]):(?:(?:[0-5]?[0-9]|60)(?:[:.,][0-9]+)?) (?:AM|PM)) (?<javalogoriginclassname>(?:[a-zA-Z$_][a-zA-Z$_0-9]*\\.)*[a-zA-Z$_][a-zA-Z$_0-9]*)(?: (?<logoriginfunction>(?:(<(?:cl)?init>)|[a-zA-Z$_][a-zA-Z$_0-9]*)))?\\s*(?:(?<loglevel>([Aa]lert|ALERT|[Tt]race|TRACE|[Dd]ebug|DEBUG|[Nn]otice|NOTICE|[Ii]nfo?(?:rmation)?|INFO?(?:RMATION)?|[Ww]arn?(?:ing)?|WARN?(?:ING)?|[Ee]rr?(?:or)?|ERR?(?:OR)?|[Cc]rit?(?:ical)?|CRIT?(?:ICAL)?|[Ff]atal|FATAL|[Ss]evere|SEVERE|EMERG(?:ENCY)?|[Ee]merg(?:ency)?)):)? (?<message>(?:.*)?)";
        String text = "Nov 01, 2020 5:26:57 PM org.apache.coyote.http11.AbstractHttp11Processor process\n" +
                "INFO: Error parsing HTTP request header Note: further occurrences of HTTP header parsing errors will be logged at DEBUG level.\n" +
                "java.lang.ArrayIndexOutOfBoundsException";

        System.out.println("all: "+ match(pattern, text));


        // 时间戳：
        String timestampPattern = "(?<timestamp>(?:[Jj]an(?:uary|uar)?|[Ff]eb(?:ruary|ruar)?|[Mm](?:a|ä)?r(?:ch|z)?|[Aa]pr(?:il)?|[Mm]a(?:y|i)?|[Jj]un(?:e|i)?|[Jj]ul(?:y|i)?|[Aa]ug(?:ust)?|[Ss]ep(?:tember)?|[Oo](?:c|k)?t(?:ober)?|[Nn]ov(?:ember)?|[Dd]e(?:c|z)(?:ember)?) (?:(?:0[1-9])|(?:[12][0-9])|(?:3[01])|[1-9]), (?>\\d\\d){1,2} (?:2[0123]|[01]?[0-9]):(?:[0-5][0-9]):(?:(?:[0-5]?[0-9]|60)(?:[:.,][0-9]+)?) (?:AM|PM))";
        String timestamp = "Nov 01, 2020 5:26:57 PM";
        System.out.println("timestamp: " + match(timestampPattern, timestamp));

        // classname, method name, level:
        String classname_methodName_level_pattern="(?<javalogoriginclassname>(?:[a-zA-Z$_][a-zA-Z$_0-9]*\\.)*[a-zA-Z$_][a-zA-Z$_0-9]*)(?: (?<logoriginfunction>(?:(<(?:cl)?init>)|[a-zA-Z$_][a-zA-Z$_0-9]*)))?\\s*(?:(?<loglevel>([Aa]lert|ALERT|[Tt]race|TRACE|[Dd]ebug|DEBUG|[Nn]otice|NOTICE|[Ii]nfo?(?:rmation)?|INFO?(?:RMATION)?|[Ww]arn?(?:ing)?|WARN?(?:ING)?|[Ee]rr?(?:or)?|ERR?(?:OR)?|[Cc]rit?(?:ical)?|CRIT?(?:ICAL)?|[Ff]atal|FATAL|[Ss]evere|SEVERE|EMERG(?:ENCY)?|[Ee]merg(?:ency)?)):)?";
        String classname_methodName_level = "org.apache.coyote.http11.AbstractHttp11Processor process\n" +
                "INFO:";
        System.out.println("classname_methodName_level: "+ match(classname_methodName_level_pattern, classname_methodName_level));


        // message
        String message_pattern = " (?<message>(?:.*)?)";
        String message = " Error parsing HTTP request header Note: further occurrences of HTTP header parsing errors will be logged at DEBUG level.\n" +
                "java.lang.ArrayIndexOutOfBoundsException";
        System.out.println("message: "+ match(message_pattern, message));

        Option option = new Option();
        option.setMultiple(true);
        Regexp regexp = Regexps.createRegexp(message_pattern, option);
        System.out.println("message2: "+ regexp.matcher(message).matches());

        String message_single_line_pattern = " (?<message>(?:.*)?)";
        String message_single_line = " Error parsing HTTP request header Note: further occurrences of HTTP header parsing errors will be logged at DEBUG level. java.lang.ArrayIndexOutOfBoundsException";

        System.out.println("message: "+ match(message_single_line_pattern, message_single_line));
    }

    private static boolean match(String pattern, String text) {
        return text.matches(pattern);
    }
}
