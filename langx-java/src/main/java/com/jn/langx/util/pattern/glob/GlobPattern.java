package com.jn.langx.util.pattern.glob;


import com.jn.langx.exception.SyntaxException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;


/**
 * A class for POSIX glob pattern with brace expansions.
 */
public class GlobPattern {
    private static final char BACKSLASH = '\\';
    private Regexp regexp;
    private boolean hasWildcard = false;

    /**
     * Construct the glob pattern object with a glob pattern string
     *
     * @param globPattern the glob pattern string
     */
    public GlobPattern(String globPattern) {
        set(globPattern);
    }

    /**
     * @return the compiled pattern
     */
    public Regexp getRegexp() {
        return regexp;
    }

    /**
     * Compile glob pattern string
     *
     * @param globPattern the glob pattern
     * @return the pattern object
     */
    public static Regexp compile(String globPattern) {
        return new GlobPattern(globPattern).getRegexp();
    }

    /**
     * Match input against the compiled glob pattern
     *
     * @param s input chars
     * @return true for successful matches
     */
    public boolean matches(CharSequence s) {
        return regexp.matcher(s).matches();
    }

    /**
     * Set and compile a glob pattern
     *
     * @param glob the glob pattern string
     */
    public void set(String glob) {
        StringBuilder pattern = new StringBuilder();
        int setOpen = 0;
        int curlyOpen = 0;
        int len = glob.length();
        hasWildcard = false;

        for (int i = 0; i < len; i++) {
            char c = glob.charAt(i);

            switch (c) {
                case BACKSLASH:
                    if (++i >= len) {
                        error("Missing escaped character", glob, i);
                    }
                    pattern.append(c).append(glob.charAt(i));
                    continue;
                case '.':
                case '$':
                case '(':
                case ')':
                case '|':
                case '+':
                    // escape regex special chars that are not glob special chars
                    pattern.append(BACKSLASH);
                    break;
                case '*':
                    pattern.append('.');
                    hasWildcard = true;
                    break;
                case '?':
                    pattern.append('.');
                    hasWildcard = true;
                    continue;
                case '{': // start of a group
                    pattern.append("(?:"); // non-capturing
                    curlyOpen++;
                    hasWildcard = true;
                    continue;
                case ',':
                    pattern.append(curlyOpen > 0 ? '|' : c);
                    continue;
                case '}':
                    if (curlyOpen > 0) {
                        // end of a group
                        curlyOpen--;
                        pattern.append(")");
                        continue;
                    }
                    break;
                case '[':
                    if (setOpen > 0) {
                        error("Unclosed character class", glob, i);
                    }
                    setOpen++;
                    hasWildcard = true;
                    break;
                case '^': // ^ inside [...] can be unescaped
                    if (setOpen == 0) {
                        pattern.append(BACKSLASH);
                    }
                    break;
                case '!': // [! needs to be translated to [^
                    pattern.append(setOpen > 0 && '[' == glob.charAt(i - 1) ? '^' : '!');
                    continue;
                case ']':
                    // Many set errors like [][] could not be easily detected here,
                    // as []], []-] and [-] are all valid POSIX glob and java regex.
                    // We'll just let the regex compiler do the real work.
                    setOpen = 0;
                    break;
                default:
            }
            pattern.append(c);
        }

        if (setOpen > 0) {
            error("Unclosed character class", glob, len);
        }
        if (curlyOpen > 0) {
            error("Unclosed group", glob, len);
        }
        // 选项里，要不要加 DOTALL, 来启用匹配换行符呢？？
        regexp = Regexps.createRegexp(pattern.toString());
    }

    /**
     * @return true if this is a wildcard pattern (with special chars)
     */
    public boolean hasWildcard() {
        return hasWildcard;
    }

    private static void error(String message, String pattern, int pos) {
        String fullMessage = StringTemplates.formatWithPlaceholder("syntax error: {} at pos {} in: {}", message, pos, pattern);
        throw new SyntaxException(fullMessage);
    }
}
