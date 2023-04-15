package com.jn.langx.util.logging.masking;

import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.spi.CommonServiceProvider;

public class Maskings {
    public static class Strategy {
        public static final String PHONE = "phone";
        public static final String PASSWORD = "pswd";
    }

    private static final GenericRegistry<Masker> registry = new GenericRegistry<Masker>();

    static {
        Pipeline.<Masker>of(new CommonServiceProvider<Masker>().get(Masker.class)).forEach(new Consumer<Masker>() {
            @Override
            public void accept(Masker masker) {
                registerMasker(masker);
            }
        });
    }

    public static String masking(Masker masker, Object obj) {
        String ret = masker.doTransform(obj);
        return ret;
    }


    public static String masking(String strategy, Object obj) {
        Masker masker = registry.get(strategy);
        Preconditions.checkNotNull(masker, "the masker strategy {} not found", strategy);
        return masking(masker, obj);
    }

    public static void registerMasker(Masker masker) {
        registry.register(masker);
    }
}
