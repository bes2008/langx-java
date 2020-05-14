package com.jn.langx.util.valuegetter;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class MemberValueGetter<M extends Member, T, V> implements ValueGetter<T, V> {
    Member member;

    public MemberValueGetter(Member member) {
        this.member = member;
    }

    @Override
    public V get(T entity) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(member);
        if (member instanceof Method) {
            return Reflects.invoke((Method) member, entity, null, true, true);
        } else if (member instanceof Field) {
            return Reflects.getFieldValue((Field) member, entity, true, true);
        }
        return null;
    }
}
