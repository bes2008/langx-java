package com.jn.langx.text.grok.pattern;


import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.configuration.Configuration;
import com.jn.langx.util.hash.HashCodeBuilder;

/**
 * @since 4.5.0
 */
public final class PatternDefinition implements Configuration {
    /**
     * 名称
     */
    @NotEmpty
    private String id;
    /**
     * 原始表达式
     */
    @NotEmpty
    private String expr;

    public PatternDefinition() {

    }

    public PatternDefinition(String name, String expr) {
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

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PatternDefinition that = (PatternDefinition) o;

        if (!id.equals(that.id)) {
            return false;
        }
        return expr.equals(that.expr);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().with(this.id).with(this.expr).build();
    }

}
