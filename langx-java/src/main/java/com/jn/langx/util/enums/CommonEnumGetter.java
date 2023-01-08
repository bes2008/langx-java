package com.jn.langx.util.enums;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;

import java.util.EnumSet;
import java.util.List;

public class CommonEnumGetter implements EnumGetter {
    @Override
    public List<Class> applyTo() {
        return Lists.<Class>newArrayList(CommonEnum.class);
    }

    @Override
    public Enum getByName(Class tClass, final String name) {
        Enum t = null;
        if (Reflects.isSubClass(CommonEnum.class, tClass)) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<Enum>() {
                @Override
                public boolean test(Enum e) {
                    CommonEnum y = (CommonEnum) e;
                    return Objs.equals(y.getName(),name);
                }
            });
        }
        return t;
    }

    @Override
    public Enum getByCode(Class tClass, final int code) {
        Enum t = null;
        if (Reflects.isSubClass(CommonEnum.class, tClass)) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<Enum>() {
                @Override
                public boolean test(Enum e) {
                    CommonEnum y = (CommonEnum) e;
                    return y.getCode() == code;
                }
            });
        }
        return t;
    }

    @Override
    public Enum getByDisplayText(Class tClass, final String displayText) {
        Enum t = null;
        if (Reflects.isSubClass(CommonEnum.class, tClass)) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<Enum>() {
                @Override
                public boolean test(Enum e) {
                    CommonEnum y = (CommonEnum) e;
                    return Objs.equals(y.getDisplayText(),displayText);
                }
            });
        }
        return t;
    }

    @Override
    public Enum getByToString(Class tClass, final String toString) {
        Enum t = null;
        if (Reflects.isSubClass(CommonEnum.class, tClass)) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<Enum>() {
                @Override
                public boolean test(Enum e) {
                    CommonEnum y = (CommonEnum) e;
                    return y.toString().equals(toString);
                }
            });
        }
        return t;
    }

    @Override
    public String getName(Enum e) {
        if(e instanceof CommonEnum){
            CommonEnum ce = (CommonEnum)e;
            return ce.getName();
        }
        return null;
    }

    @Override
    public String getDisplayText(Enum e) {
        if(e instanceof CommonEnum){
            CommonEnum ce = (CommonEnum)e;
            return ce.getDisplayText();
        }
        return null;
    }

    @Override
    public int getCode(Enum e) {
        if(e instanceof CommonEnum){
            CommonEnum ce = (CommonEnum)e;
            return ce.getCode();
        }
        return Integer.MIN_VALUE;
    }
}
