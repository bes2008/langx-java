package com.jn.langx.security.crypto.cipher;

import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.comparator.OrderedComparator;
import com.jn.langx.util.comparator.ReverseComparator;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.spi.CommonServiceProvider;

import java.util.Iterator;
import java.util.List;

public class CipherAlgorithmSuiteRegistry extends GenericRegistry<CipherAlgorithmSuite> {

    @Override
    protected void doInit() throws InitializationException {
        CommonServiceProvider<CipherAlgorithmSuiteSupplier> serviceProvider = new CommonServiceProvider<CipherAlgorithmSuiteSupplier>();
        // 优先级 改为 从低到高， 因为要放到 registry中，可能 存在 复写 （这样可以保证，在复写时，高优先级的 复写低优先级的）
        serviceProvider.setComparator(new ReverseComparator<CipherAlgorithmSuiteSupplier>(new OrderedComparator<CipherAlgorithmSuiteSupplier>()));
        Iterator<CipherAlgorithmSuiteSupplier> iterator = serviceProvider.get(CipherAlgorithmSuiteSupplier.class);
        Pipeline.<CipherAlgorithmSuiteSupplier>of(iterator)
                .map(new Function<CipherAlgorithmSuiteSupplier, List<CipherAlgorithmSuite>>() {
                    @Override
                    public List<CipherAlgorithmSuite> apply(CipherAlgorithmSuiteSupplier supplier) {
                        return supplier.get();
                    }
                }).<CipherAlgorithmSuite>flat()
                .forEach(new Consumer<CipherAlgorithmSuite>() {
                    @Override
                    public void accept(CipherAlgorithmSuite suite) {
                        register(suite);
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
