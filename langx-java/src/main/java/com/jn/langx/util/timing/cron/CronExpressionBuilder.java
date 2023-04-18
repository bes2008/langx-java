package com.jn.langx.util.timing.cron;

import com.jn.langx.Builder;
import com.jn.langx.text.StrTokenizer;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Functions;

import java.text.ParseException;

public class CronExpressionBuilder implements Builder<CronExpression> {
    private CronExpressionType type = CronExpressionType.QUARTZ;
    private String expression;

    public CronExpressionType type() {
        return this.type;
    }

    public CronExpressionBuilder type(CronExpressionType type) {
        if (type != null) {
            this.type = type;
        }
        return this;
    }

    public String expression() {
        return this.expression;
    }

    public CronExpressionBuilder expression(String expression) {
        this.expression = expression;
        return this;
    }

    @Override
    public CronExpression build() {
        Preconditions.checkNotEmpty(this.expression);
        String[] segments = Pipeline.<String>of(new StrTokenizer(this.expression, " ","\t")).filter(Functions.<String>notEmptyPredicate()).toArray(String[].class);
        // unix-like cron:    分 时 日 月 星期
        // quartz cron:    秒 分 时 日 月 星期 年  //年不是必须的

        if (type == CronExpressionType.UNIX_LIKE && (segments.length > 6 || segments.length < 5)) {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("Illegal cron expression: {}", this.expression));
        }
        if (type == CronExpressionType.QUARTZ && (segments.length > 7 || segments.length < 5)) {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("Illegal cron expression: {}", this.expression));
        }
        // 下面要进行 拼接或者截取。
        // 目的是转换成 Quartz 的CRON 表达式
        if (segments.length == 5) {
            this.expression = "0 " + this.expression;
        } else {
            if (segments.length == 6) {
                if (type == CronExpressionType.UNIX_LIKE) {
                    this.expression = "0 " + Strings.join(" ", segments, 1);
                }
            }
        }
        try {
            return new CronExpression(expression);
        } catch (ParseException ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }
}
