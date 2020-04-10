package com.jn.langx.test.expression;

import com.jn.langx.expression.operator.arithmetic.Add;
import com.jn.langx.expression.operator.compare.GE;
import com.jn.langx.expression.operator.compare.NI;
import com.jn.langx.expression.operator.logic.And;
import com.jn.langx.expression.value.NumberExpression;
import com.jn.langx.expression.value.StringExpression;
import com.jn.langx.expression.value.ValueExpression;
import com.jn.langx.util.collection.Collects;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTests {
    @Test
    public void testLogicOperator() {
        GE ge = new GE();
        ge.setLeft(new StringExpression("zhangsan"));
        ge.setRight(new StringExpression("zhangsa"));

        NI ni = new NI();
        ni.setLeft(new StringExpression("zhangsan2"));
        ni.setRight(new ValueExpression(Collects.asList("wangwu", "zhangsan", "lisi")));

        And and = new And();
        and.setLeft(ge);
        and.setRight(ni);

        System.out.println(and.toString());
        Assert.assertTrue(and.execute().execute());
    }

    @Test
    public void testAdd(){
        Add add = new Add();
        add.setLeft(new NumberExpression<Number>(3));
        add.setRight(new NumberExpression<Number>(7));
        Assert.assertEquals(10L, add.execute().execute());
    }
}
