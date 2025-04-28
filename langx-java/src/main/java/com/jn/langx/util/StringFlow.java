package com.jn.langx.util;

import com.jn.langx.Transformer;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.transformer.DelegateConditionTransformer;

public class StringFlow {
    private String str;

    private StringFlow(String str) {
        this.str = str;
    }

    public static StringFlow empty() {
        return of("");
    }

    public static StringFlow of(char[] chars) {
        return of(new String(chars));
    }

    public static StringFlow of(String str) {
        return new StringFlow(str);
    }


    public StringFlow trim() {
        this.str = Strings.trim(this.str);
        return this;
    }

    public StringFlow trimToEmpty() {
        this.str = Strings.trimToEmpty(this.str);
        return this;
    }

    public StringFlow strip() {
        this.str = Strings.strip(this.str);
        return this;
    }

    public StringFlow strip(String chars) {
        this.str = Strings.strip(this.str, chars);
        return this;
    }

    public StringFlow stripStart(String chars) {
        this.str = Strings.stripStart(this.str, chars);
        return this;
    }

    public StringFlow stripEnd(String chars) {
        this.str = Strings.stripEnd(this.str, chars);
        return this;
    }


    public StringFlow substring(int beginIndex, int endIndex) {
        this.str = Strings.substring(this.str, beginIndex, endIndex);
        return this;
    }

    public StringFlow substring(int beginIndex) {
        this.str = Strings.substring(this.str, beginIndex);
        return this;
    }

    public StringFlow replace(String search, String replacement) {
        this.str = Strings.replace(this.str, search, replacement);
        return this;
    }

    public StringFlow replace(String search, String replacement, int max) {
        this.str = Strings.replace(this.str, search, replacement, max);
        return this;
    }

    public StringFlow remove(String search) {
        this.str = Strings.remove(this.str, search);
        return this;
    }

    public StringFlow removePattern(String pattern) {
        this.str = Strings.removePattern(this.str, pattern);
        return this;
    }

    public StringFlow truncate(int maxLength) {
        this.str = Strings.truncate(this.str, maxLength);
        return this;
    }


    public StringFlow padRight(String padding, int paddingToSize) {
        this.str = Strings.rightPad(this.str, paddingToSize, padding);
        return this;
    }

    public StringFlow padLeft(String padding, int paddingToSize) {
        this.str = Strings.leftPad(this.str, paddingToSize, padding);
        return this;
    }

    public StringFlow insert(String str, int offset) {
        this.str = Strings.insert(this.str, str, offset);
        return this;
    }

    public StringFlow append(String str) {
        this.str = getEmptyIfNull().concat(str);
        return this;
    }

    public StringFlow transform(Predicate<String> condition, final Transformer<String, String> transformer) {
        return transform(new DelegateConditionTransformer<String, String>(condition, transformer));
    }

    public StringFlow transform(Transformer<String, String> transformer) {
        this.str = transformer.transform(this.str);
        return this;
    }

    public StringFlow toUpperCase() {
        this.str = Strings.upperCase(this.str);
        return this;
    }

    public StringFlow toLowerCase() {
        this.str = Strings.lowerCase(this.str);
        return this;
    }

    public StringFlow toCamelCase(String... delimiters) {
        this.str = Strings.toCamelCase(this.str, true, delimiters);
        return this;
    }

    public StringFlow toCamelCase(boolean firstLetterUpperCase, String... delimiters) {
        this.str = Strings.toCamelCase(this.str, firstLetterUpperCase, delimiters);
        return this;
    }

    public StringFlow toSnakeCase(String... delimiters) {
        this.str = Strings.toSnakeCase(this.str, delimiters);
        return this;
    }

    public StringFlow toKebabCase(String... delimiters) {
        this.str = Strings.toKebabCase(this.str, delimiters);
        return this;
    }

    public StringFlow toPascalCase(String... delimiters) {
        this.str = Strings.toPascalCase(this.str, delimiters);
        return this;
    }

    public StringFlow toTrainCase(String... delimiters) {
        this.str = Strings.toTrainCase(this.str, delimiters);
        return this;
    }


    public StringFlow upperCaseFirstChar() {
        this.str = Strings.upperCaseFirstChar(this.str);
        return this;
    }

    public StringFlow lowerCaseFirstChar() {
        this.str = Strings.lowerCaseFirstChar(this.str);
        return this;
    }

    public StringFlow replaceFirstChar(String replacement) {
        this.str = Strings.replaceFirstChar(this.str, replacement);
        return this;
    }

    public StringFlow removeFirstChar() {
        this.str = Strings.removeFirstChar(this.str);
        return this;
    }

    public StringFlow transformFirstChar(Transformer<String, String> transformer) {
        this.str = Strings.transformFirstChar(this.str, transformer);
        return this;
    }

    public Pipeline<String> split(String separator, boolean separatorIsRegexp) {
        return Pipeline.of(Strings.split(this.str, separatorIsRegexp, separator, true, true));
    }

    public Pipeline<String> split(String separator, boolean trim, boolean ignoreEmptyTokens) {
        return Pipeline.of(Strings.split(this.str, separator, trim, ignoreEmptyTokens));
    }

    public Pipeline<String> splitRegexp(String separator, boolean trim, boolean ignoreEmptyTokens) {
        return Pipeline.of(Strings.splitRegexp(this.str, separator, trim, ignoreEmptyTokens));
    }

    public boolean isEmpty() {
        return Strings.isEmpty(this.str);
    }

    public boolean isBlank() {
        return Strings.isBlank(this.str);
    }


    public String get() {
        return this.str;
    }


    public String getIfEmpty(String defaultValue) {
        return Objs.useValueIfEmpty(get(), defaultValue);
    }

    public String getIfBlank(String defaultValue) {
        return Strings.useValueIfBlank(get(), defaultValue);
    }

    public String getNullIfBlank() {
        return Strings.getNullIfBlank(get());
    }

    public String getNullIfEmpty() {
        return Strings.getNullIfEmpty(get());
    }

    public String getEmptyIfNull() {
        return Strings.getEmptyIfNull(get());
    }

    public byte[] getUTF8Bytes() {
        return Strings.getBytesUtf8(get());
    }

    public char[] getChars() {
        return get().toCharArray();
    }
}
