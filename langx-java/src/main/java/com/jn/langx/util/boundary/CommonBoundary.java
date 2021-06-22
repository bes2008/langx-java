package com.jn.langx.util.boundary;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

public class CommonBoundary implements Boundary {
    private Predicate<String> low = Functions.truePredicate();
    private Predicate<String> high = Functions.truePredicate();
    private Predicate<String> exclusion = Functions.falsePredicate();

    @Override
    public boolean test(String value) {
        Preconditions.checkState(low != null && high != null && exclusion != null);

        if (low.test(value) && high.test(value)) {
            return !exclusion.test(value);
        } else {
            return false;
        }
    }

    public void setLow(Predicate<String> low) {
        if (low != null) {
            this.low = low;
        }
    }

    public void setHigh(Predicate<String> high) {
        if (high != null) {
            this.high = high;
        }
    }

    public void setExclusion(Predicate<String> exclusion) {
        if (exclusion != null) {
            this.exclusion = exclusion;
        }
    }

    public Predicate<String> getLow() {
        return low;
    }

    public Predicate<String> getHigh() {
        return high;
    }

    public Predicate<String> getExclusion() {
        return exclusion;
    }
}
