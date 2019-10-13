package com.jn.langx.text.html;

public class Htmls {
    /**
     * This method is used to insert HTML block dynamically
     *
     * @param source       the HTML code to be processes
     * @param replaceNl    if true '\n' will be replaced by &lt;br>
     * @param replaceTag   if true '<' will be replaced by &lt; and '>' will be replaced
     *                     by &gt;
     * @param replaceQuote if true '\"' will be replaced by &quot;
     * @return the formated html block
     */
    public static String formatHtml(String source, boolean replaceNl, boolean replaceTag,
                                    boolean replaceQuote) {
        StringBuilder buf = new StringBuilder();
        int len = source.length();

        for (int ii = 0; ii < len; ii++) {
            char ch = source.charAt(ii);

            switch (ch) {
                case '\"':
                    if (replaceQuote) {
                        buf.append("&quot;");
                    } else {
                        buf.append(ch);
                    }
                    break;

                case '<':
                    if (replaceTag) {
                        buf.append("&lt;");
                    } else {
                        buf.append(ch);
                    }
                    break;

                case '>':
                    if (replaceTag) {
                        buf.append("&gt;");
                    } else {
                        buf.append(ch);
                    }
                    break;

                case '\n':
                    if (replaceNl) {
                        if (replaceTag) {
                            buf.append("&lt;br&gt;");
                        } else {
                            buf.append("<br>");
                        }
                    } else {
                        buf.append(ch);
                    }
                    break;

                case '\r':
                    break;

                case '&':
                    buf.append("&amp;");
                    break;

                default:
                    buf.append(ch);
                    break;
            }
        }

        return buf.toString();
    }
}
