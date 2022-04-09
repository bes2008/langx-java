package com.jn.langx.text.grok;


import com.jn.langx.configuration.Configuration;

/**
 * @since 4.5.0
 */
public class PatternDefinition implements Configuration {
    /**
     * 名称
     */
    private String id;
    /**
     * 原始表达式
     */
    private String expr;

    public PatternDefinition(){

    }

    public PatternDefinition(String name, String expr){
        this.setId(name);
        this.setExpr(expr);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

}
