package com.jn.langx.util.bit;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;

import java.util.Collection;

public final class Masks {
    private Masks(){}

    public static <T> int createMask(Collection<T> operandHolders, Function<T, Integer> mapper) {
        Collection<Integer> operands = Pipeline.of(operandHolders).map(mapper).clearNulls().asList();
        return createMask(operands);
    }

    public static int createMask(int... operands) {
        return createMask(Collects.<Integer>asList(Collects.<Integer>asIterable(operands)));
    }

    /**
     * @param operands 操作数集合
     * @return 掩码
     */
    public static int createMask(Collection<Integer> operands) {
        Preconditions.checkNotNull(operands, "operand is required");
        Preconditions.checkArgument(operands.size() >= 1, "operands is required");
        int mask = 0;
        for (int operand : operands) {
            mask = addOperand(mask, operand);
        }
        return mask;
    }

    public static int addOperand(int mask, int operand) {
        return mask | operand;
    }

    public static int removeOperand(int mask, int operand) {
        return mask & ~operand;
    }


    /**
     * 判断 mask 是否包含 指定的操作数 operand
     *
     * @param mask    掩码
     * @param operand 操作数
     * @return 包含与否
     */
    public static boolean containsOperand(int mask, int operand) {
        return (mask & operand) == operand;
    }


}