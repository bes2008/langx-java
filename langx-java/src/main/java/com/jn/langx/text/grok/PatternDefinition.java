package com.jn.langx.text.grok;


import com.jn.langx.configuration.Configuration;

/**
 * name: 代表 pattern 定义的名称
 * value: 代表了原始的 pattern ，也就是具有嵌套其他模板能力的 pattern
 * pattern: 代表了 处理后的最真实的pattern，能够直接作为 正则表达式的pattern
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
