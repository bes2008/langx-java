package com.jn.langx.text.tokenizer;

import com.jn.langx.util.function.Supplier2;

/**
 * 5.1.0
 *
 * 找到一个token 后，构建出 Token对象
 *
 * @param <Token>
 */
public interface TokenFactory<Token> extends Supplier2<String, Boolean, Token> {
    Token get(String tokenContent, Boolean isDelimiter);
}
