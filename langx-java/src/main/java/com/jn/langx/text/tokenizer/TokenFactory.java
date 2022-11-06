package com.jn.langx.text.tokenizer;

import com.jn.langx.util.function.Supplier2;

/**
 * 5.1.0
 *
 * @param <Token>
 */
public interface TokenFactory<Token> extends Supplier2<String, Boolean, Token> {
    public Token get(String tokenContent, Boolean isDelimiter);
}
