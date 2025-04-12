package com.jn.langx.util.net.uri.component;

import java.nio.charset.Charset;

class DefaultUriTemplateEncoder implements UriComponentEncoder {

    private final Charset charset;

    private final StringBuilder currentLiteral = new StringBuilder();

    private final StringBuilder currentVariable = new StringBuilder();

    private final StringBuilder output = new StringBuilder();

    private boolean variableWithNameAndRegex;

    DefaultUriTemplateEncoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    public String apply(String source, UriComponentType type) {
        // URI variable only?
        if (isUriVariable(source)) {
            return source;
        }
        // Literal template only?
        if (source.indexOf('{') == -1) {
            return UriComponentUtils.encodeUriComponent(source, this.charset, type);
        }
        int level = 0;
        clear(this.currentLiteral);
        clear(this.currentVariable);
        clear(this.output);
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (c == ':' && level == 1) {
                this.variableWithNameAndRegex = true;
            }
            if (c == '{') {
                level++;
                if (level == 1) {
                    append(this.currentLiteral, true, type);
                }
            }
            if (c == '}' && level > 0) {
                level--;
                this.currentVariable.append('}');
                if (level == 0) {
                    boolean encode = !isUriVariable(this.currentVariable);
                    append(this.currentVariable, encode, type);
                } else if (!this.variableWithNameAndRegex) {
                    append(this.currentVariable, true, type);
                    level = 0;
                }
            } else if (level > 0) {
                this.currentVariable.append(c);
            } else {
                this.currentLiteral.append(c);
            }
        }
        if (level > 0) {
            this.currentLiteral.append(this.currentVariable);
        }
        append(this.currentLiteral, true, type);
        return this.output.toString();
    }

    /**
     * Whether the given String is a single URI variable that can be
     * expanded. It must have '{' and '}' surrounding non-empty text and no
     * nested placeholders unless it is a variable with regex syntax,
     * e.g. {@code "/{year:\d{1,4}}"}.
     */
    private boolean isUriVariable(CharSequence source) {
        if (source.length() < 2 || source.charAt(0) != '{' || source.charAt(source.length() - 1) != '}') {
            return false;
        }
        boolean hasText = false;
        for (int i = 1; i < source.length() - 1; i++) {
            char c = source.charAt(i);
            if (c == ':' && i > 1) {
                return true;
            }
            if (c == '{' || c == '}') {
                return false;
            }
            hasText = (hasText || !Character.isWhitespace(c));
        }
        return hasText;
    }

    private void append(StringBuilder sb, boolean encode, UriComponentType type) {
        this.output.append(encode ? UriComponentUtils.encodeUriComponent(sb.toString(), this.charset, type) : sb);
        clear(sb);
        this.variableWithNameAndRegex = false;
    }

    private void clear(StringBuilder sb) {
        sb.delete(0, sb.length());
    }
}