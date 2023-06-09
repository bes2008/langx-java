package com.jn.langx.io.stream.obj;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.List;

/**
 * @since 5.2.9
 */
public class SecureObjectInputStream extends ObjectInputStream {

    private Predicate<ObjectStreamClass> predicate;

    public SecureObjectInputStream(InputStream inputStream) throws IOException {
        this(inputStream, new SecureObjectClassPredicate[0]);
    }

    public SecureObjectInputStream(InputStream inputStream, SecureObjectClassPredicate... predicates) throws IOException {
        super(inputStream);
        initPredicate(predicates);
    }

    private void initPredicate(SecureObjectClassPredicate... predicates) {
        List<Predicate> ps = Pipeline.<Predicate>of(predicates)
                .clearNulls()
                .asList();
        if (ps.size() == 0) {
            predicate = new DefaultSecureObjectClassPredicate();
        } else {
            predicate = Functions.allPredicate(ps);
        }
    }

    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();
        if (!isSafeClass(desc)) {
            throw new SecurityException(StringTemplates.formatWithPlaceholder("Illegal class name: {}", name));
        }
        return super.resolveClass(desc);
    }

    private boolean isSafeClass(ObjectStreamClass desc) {
        return predicate.test(desc);
    }

    @Override
    protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
        return super.resolveProxyClass(interfaces);
    }


}
