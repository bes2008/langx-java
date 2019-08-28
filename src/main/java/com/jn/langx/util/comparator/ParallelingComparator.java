package com.jn.langx.util.comparator;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings({"unchecked"})
public class ParallelingComparator implements Comparator {
    private final List<Comparator> list = new ArrayList<Comparator>();

    @Override
    public int compare(Object o1, Object o2) {
        Preconditions.checkTrue(Emptys.isNotEmpty(list));
        int leftMoveUnit = 32 / list.size();
        int result = 0;
        for (int i = 0; i < list.size(); i++) {
            Comparator comparator = list.get(i);
            int delta = comparator.compare(o1, o2);
            int leftMove = (list.size() - 1) * leftMoveUnit;
            result = result + delta << leftMove;
        }
        return result;
    }

    public void addComparator(Comparator comparator) {
        Preconditions.checkNotNull(comparator);
        list.add(comparator);
    }
}
