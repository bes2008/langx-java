package com.jn.langx.security.crypto.cipher;

import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.util.List;
import java.util.ServiceLoader;

public class CipherAlgorithmSuiteRegistry extends GenericRegistry<CipherAlgorithmSuite> {

    @Override
    protected void doInit() throws InitializationException {
        ServiceLoader<CipherAlgorithmSuiteSupplier> loader = ServiceLoader.load(CipherAlgorithmSuiteSupplier.class);
        Collects.forEach(loader.iterator(), new Consumer<CipherAlgorithmSuiteSupplier>() {
            @Override
            public void accept(CipherAlgorithmSuiteSupplier cipherAlgorithmSuiteSupplier) {
                List<CipherAlgorithmSuite> suites = cipherAlgorithmSuiteSupplier.get();
                Collects.forEach(suites, new Consumer<CipherAlgorithmSuite>() {
                    @Override
                    public void accept(CipherAlgorithmSuite suite) {
                        register(suite);
                    }
                });
            }
        });
    }

    public void add(String algorithm, String transformation) {
        this.add(algorithm, transformation, null);
    }

    public void add(@NotEmpty String algorithm, @NotEmpty String transformation, @Nullable AlgorithmParameterSupplier supplier) {
        CipherAlgorithmSuite suite = new CipherAlgorithmSuite(algorithm, transformation, supplier);
        register(suite);
    }

    public String getTransformation(String algorithm) {
        CipherAlgorithmSuite suite = get(algorithm);
        return suite == null ? null : suite.getTransformation();
    }

    public AlgorithmParameterSupplier getParmameterSupplier(String algorithm) {
        CipherAlgorithmSuite suite = get(algorithm);
        return suite == null ? null : suite.getParameterSupplier();
    }
}
