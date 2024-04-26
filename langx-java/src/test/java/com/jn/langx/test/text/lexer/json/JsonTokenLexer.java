package com.jn.langx.test.text.lexer.json;

import com.jn.langx.text.lexer.AbstractLexer;
import com.jn.langx.text.lexer.Token;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.regexp.Regexps;

import java.util.Map;

public class JsonTokenLexer extends AbstractLexer {

    private static Map<String, JsonSymbol> jsonSymbolMap= Maps.newHashMap();
    static {
        Map<String, JsonSymbol> symbolMap= Maps.newHashMap();
        symbolMap.put("OBJECT_START", new JsonSymbol("OBJECT_START","{",1));
        symbolMap.put("OBJECT_END", new JsonSymbol("OBJECT_END","{",2));
        symbolMap.put("KEY_VALUE_SEPARATOR", new JsonSymbol("KEY_VALUE_SEPARATOR",":",3));
        symbolMap.put("ARRAY_START", new JsonSymbol("ARRAY_START","[",4));
        symbolMap.put("ARRAY_END", new JsonSymbol("ARRAY_END","[",5));
        symbolMap.put("ELEMENT_SEPARATOR", new JsonSymbol("ELEMENT_SEPARATOR",",",6));
        symbolMap.put("NUMBER", new JsonSymbol("NUMBER", null, 7, Regexps.createRegexp("\\d+([lL]|(\\.\\d+[dDfF]?))?" )));
        symbolMap.put("BOOL", new JsonSymbol("BOOL", null, 8, new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return Objs.equals("true",value) || Objs.equals("false",value);
            }
        }));
        symbolMap.put("NULL", new JsonSymbol("NULL","null",9));
        symbolMap.put("STRING", new JsonSymbol("STRING",null,10, Regexps.createRegexp("\".*\"")));

        jsonSymbolMap=symbolMap;
    }


    private int currentTokenStartOffset;
    private int currentOffset;
    private int endOffset;
    private CharSequence charSequence;



    /**
     * 在
     */
    private int state;

    @Override
    protected void startInternal(CharSequence buf, int startOffset, int endOffset, int initialState) {
        this.charSequence=buf;
        this.currentTokenStartOffset=startOffset;
        this.endOffset=endOffset;
        this.state=initialState;
    }

    @Override
    public void next() {

    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public Token getToken() {
        return null;
    }

    @Override
    public CharSequence getBufferSequence() {
        return null;
    }

    @Override
    public int getBufferEnd() {
        return 0;
    }

    @Override
    public int getTokenType() {
        return 0;
    }

    @Override
    public int getTokenStart() {
        return 0;
    }

    @Override
    public int getTokenEnd() {
        return 0;
    }
}
