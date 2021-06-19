package com.jn.langx.util;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.enums.Enums;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

import java.util.EnumSet;
import java.util.List;

public enum DataUnit {
    B(1, "b"),
    KB(1024 * B.bytes, "kb", "k"),
    MB(1024 * KB.bytes, "mb", "m"),
    GB(1024 * MB.bytes, "gb", "g"),
    TB(1024 * MB.bytes, "tb", "t"),
    PB(1024 * MB.bytes, "pb", "p");

    private List<String> symbols;
    private long bytes;

    private DataUnit(long bytes, String... symbols) {
        this.symbols = Collects.asList(symbols);
        this.bytes = bytes;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public String getStandardSymbol(){
        return getSymbols().get(0);
    }

    public String getLessUnitSymbol(){
        String symbol = getStandardSymbol();
        if(symbol.length()==1){
            return symbol;
        }
        else{
            if(symbol.endsWith("b")){
                return symbol.substring(0,symbol.length()-1);
            }else{
                return symbol;
            }
        }
    }


    public long getBytes() {
        return bytes;
    }

    public static DataUnit ofBytes(long bytes) {
        return Enums.ofField(DataUnit.class, "bytes", bytes);
    }

    public static DataUnit ofSymbol(final String symbol) {
        return Enums.ofField(DataUnit.class, "symbols", symbol, new Predicate<DataUnit>() {
            @Override
            public boolean test(DataUnit unit) {
                return unit.symbols.contains(symbol);
            }
        });
    }

    public static List<String> allSymbols() {
        return Pipeline.of(EnumSet.allOf(DataUnit.class)).map(new Function<DataUnit, List<String>>() {
            @Override
            public List<String> apply(DataUnit unit) {
                return unit.symbols;
            }
        }).flatMap(Functions.<String>noopMapper()).asList();
    }
}