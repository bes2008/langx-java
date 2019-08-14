/**
 * MIT License
 * <p>
 * Copyright (c) 2019 fangjinuo
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.jn.langx.util;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

@SuppressWarnings({"unused"})
public class Strings {
    private Strings() {
    }

    /**
     * judge a string is null or ""
     *
     * @param str the specified string
     * @return whether is null or ""
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * judge a string has some whitespace at most
     *
     * @param str the specified string
     * @return the judge result
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * Get substring from 0 to a specified length
     * equals: string.substring(0, length)
     *
     * @param string a string will be truncated
     * @param length new string's length
     * @return the new string
     */
    public static String truncate(final String string, final int length) {
        Preconditions.checkTrue(length >= 0);
        if (string.length() <= length) {
            return string;
        }
        return string.substring(0, length);
    }

    /**
     * append all objects with the specified separator
     *
     * @param separator the specified separator
     * @param objects   the dbjects that will be append
     * @return the new string
     */
    public static String join(final String separator, final Iterator objects) {
        final StringBuilder buf = new StringBuilder();
        if (objects.hasNext()) {
            buf.append(objects.next());
        }
        while (objects.hasNext()) {
            buf.append(separator).append(objects.next());
        }
        return buf.toString();
    }

    /**
     * split a string, the returned array is not contains: "", null
     */
    public static String[] split(String string, String separator) {
        if (Emptys.isEmpty(string) || Emptys.isEmpty(separator)) {
            return new String[0];
        }

        StringTokenizer tokenizer = new StringTokenizer(string, separator, false);
        List<String> array = Collects.map(tokenizer, new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input.trim();
            }
        });
        array = Collects.filter(array, new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return Strings.isNotBlank(value);
            }
        });
        return Collects.toArray(array, String[].class);
    }

}