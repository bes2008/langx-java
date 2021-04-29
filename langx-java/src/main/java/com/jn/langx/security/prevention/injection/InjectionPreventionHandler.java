package com.jn.langx.security.prevention.injection;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.struct.Holder;

import java.util.List;

public class InjectionPreventionHandler implements Function<String, String> {
    private List<String> blacklist = null;

    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
    }

    public List<String> getBlacklist() {
        return this.blacklist;
    }

    @Override
    public String apply(String value) {
        final Holder<String> stringHolder = new Holder<String>(value);
        Collects.forEach(getBlacklist(), new Consumer<String>() {
            @Override
            public void accept(String str) {
                String v = stringHolder.get();
                v = Strings.remove(v, str);
                stringHolder.set(v);
            }
        }, new Predicate<String>() {
            @Override
            public boolean test(String str) {
                return stringHolder.isEmpty();
            }
        });
        return stringHolder.get();
    }
}
