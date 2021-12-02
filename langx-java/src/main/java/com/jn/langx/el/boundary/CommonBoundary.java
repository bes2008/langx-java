package com.jn.langx.el.boundary;

import com.jn.langx.el.operator.LogicOperatorType;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

import java.util.List;

public class CommonBoundary implements Boundary {
    private List<Predicate<String>> predicates = Collects.emptyArrayList();
    private LogicOperatorType logicOperatorType = LogicOperatorType.AND;

    public CommonBoundary() {

    }

    public CommonBoundary(LogicOperatorType logicOperatorType) {
        setLogicOperatorType(logicOperatorType);
    }

    public void setLogicOperatorType(LogicOperatorType logicOperatorType) {
        this.logicOperatorType = logicOperatorType;
    }

    @Override
    public boolean test(final String value) {
        Preconditions.checkNotEmpty(predicates);
        Preconditions.checkNotNull(logicOperatorType);
        boolean ret = false;
        switch (logicOperatorType) {
            case AND:
                ret = Functions.allPredicate(predicates).test(value);
                break;
            case OR:
                ret = Functions.anyPredicate(predicates).test(value);
                break;
            case NOT:
            default:
                ret = Functions.nonePredicate(predicates).test(value);
                break;
        }
        return ret;
    }

    public void addPredicate(Predicate<String> predicate) {
        if (predicate != null) {
            predicates.add(predicate);
        }
    }

    protected List<Predicate<String>> getPredicates() {
        return this.predicates;
    }

}
