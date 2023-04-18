package com.jn.langx.util.bit;


import java.util.Iterator;
import java.util.LinkedHashSet;
/**
 * @since 4.0.6
 */
public class MaskSet extends LinkedHashSet<Integer> {
    private int masks = 0;

    @Override
    public boolean add(Integer mask) {
        if (mask == null) {
            return false;
        }
        if (super.add(mask)) {
            this.masks = Masks.addOperand(masks, mask);
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        int mask = ((Number) o).intValue();
        if (super.remove(o)) {
            this.masks = Masks.removeOperand(this.masks, mask);
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        int mask = ((Number) o).intValue();
        return Masks.containsOperand(this.masks, mask);
    }
}
