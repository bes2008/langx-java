package com.jn.langx.util.boundary;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;

import java.util.List;

public class CommonBoundary implements Boundary {
    private List<Predicate<String>> predicates = Collects.emptyArrayList();
    private boolean any = false;
    @Override
    public boolean test(final String value) {
        Preconditions.checkNotEmpty(predicates);

        return Collects.allMatch(predicates, new Predicate<Predicate<String>>() {
            @Override
            public boolean test(Predicate<String> predicate) {
                return predicate.test(value);
            }
        });
    }

    public void addPredicate(Predicate<String> predicate) {
        if (predicate != null) {
            predicates.add(predicate);
        }
    }

    protected List<Predicate<String>> getPredicates(){
        return this.predicates;
    }

}
