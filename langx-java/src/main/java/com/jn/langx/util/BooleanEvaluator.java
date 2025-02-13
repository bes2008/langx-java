package com.jn.langx.util;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jinuo.fang
 */
public class BooleanEvaluator {
    private final Set<Object> trueFactors = new HashSet<Object>();
    private final Set<Object> falseFactors = new HashSet<Object>();
    private boolean nullValue = false;
    private boolean stringIgnoreCase = true;

    public BooleanEvaluator(boolean nullValue, boolean stringIgnoreCase, Object[] trueFactors, Object[] falseFactors) {
        this(nullValue, stringIgnoreCase, Collects.asList(trueFactors), Collects.asList(falseFactors));
    }

    public BooleanEvaluator(boolean nullValue, boolean stringIgnoreCase, List<Object> trueFactors, List<Object> falseFactors) {
        setNullValue(nullValue);
        setStringIgnoreCase(stringIgnoreCase);
        addTrueFactors(trueFactors);
        addFalseFactors(falseFactors);
    }

    public void setStringIgnoreCase(boolean ignoreCase) {
        this.stringIgnoreCase = ignoreCase;
    }

    public void setNullValue(boolean nullValue) {
        this.nullValue = nullValue;
    }

    public void addFactor(Object trueFactor, Object falseFactor) {
        addTrueFactor(trueFactor);
        addFalseFactor(falseFactor);
    }

    public void addTrueFactors(List<Object> trueFactors) {
        if (Emptys.isNotEmpty(trueFactors)) {
            Collects.forEach(trueFactors, new Consumer<Object>() {
                @Override
                public void accept(Object object) {
                    addTrueFactor(object);
                }
            });
        }
    }

    public void addFalseFactors(List<Object> falseFactors) {
        if (Emptys.isNotEmpty(falseFactors)) {
            Collects.forEach(falseFactors, new Consumer<Object>() {
                @Override
                public void accept(Object object) {
                    addFalseFactor(object);
                }
            });
        }
    }

    public void addTrueFactor(Object trueFactor) {
        if (trueFactor != null) {
            if (trueFactor instanceof String) {
                trueFactors.add(stringIgnoreCase ? ((String) trueFactor).toLowerCase() : trueFactor);
            }
            trueFactors.add(trueFactor);
        }
    }

    public void addFalseFactor(Object falseFactor) {
        if (falseFactor != null) {
            if (falseFactor instanceof String) {
                falseFactors.add(stringIgnoreCase ? ((String) falseFactor).toLowerCase() : falseFactor);
                return;
            }
            falseFactors.add(falseFactor);
        }
    }

    public boolean evalTrue(Object object) {
        if (object == null) {
            return nullValue;
        }
        if (object instanceof String && stringIgnoreCase) {
            object = ((String) object).toLowerCase();
        }
        if (trueFactors.contains(object)) {
            return true;
        }
        if (falseFactors.contains(object)) {
            return false;
        }
        if(object instanceof String) {
            if(Strings.equalsIgnoreCase((String)object, "true")){
                return true;
            }
        }

        return !nullValue;
    }

    public boolean evalFalse(Object object) {
        if (object == null) {
            return nullValue;
        }
        if (object instanceof String && stringIgnoreCase) {
            object = ((String) object).toLowerCase();
        }
        if (falseFactors.contains(object)) {
            return true;
        }
        if(object instanceof String) {
            if(Strings.equalsIgnoreCase((String)object, "false")){
                return true;
            }
        }
        if (trueFactors.contains(object)) {
            return false;
        }
        return nullValue;
    }

    public static BooleanEvaluator createTrueEvaluator(Object... truthArray) {
        return new BooleanEvaluator(false, true, truthArray, new Object[]{"false",false});
    }

    public static BooleanEvaluator createTrueEvaluator(boolean nullValue, boolean stringIgnoreCase, Object[] truthArray) {
        return new BooleanEvaluator(nullValue, stringIgnoreCase, truthArray, null);
    }

    public static BooleanEvaluator createFalseEvaluator(Object... falseArray) {
        return new BooleanEvaluator(false, true, null, falseArray);
    }

    public static BooleanEvaluator createFalseEvaluator(boolean nullValue, boolean stringIgnoreCase, Object[] falseArray) {
        return new BooleanEvaluator(nullValue, stringIgnoreCase, null, falseArray);
    }

    public static final BooleanEvaluator simpleStringEvaluator = createTrueEvaluator("true");
}
