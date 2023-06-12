package com.jn.langx.security.masking;

import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.spi.CommonServiceProvider;

/**
 * @since 5.2.0
 */
public class Maskings {

    public static final class Strategy {
        public static final String PHONE = "phone";
        public static final String STAR_6 = "STAR_6";

        private Strategy(){

        }
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

    private Maskings() {

    }
}
