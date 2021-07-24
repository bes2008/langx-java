package com.jn.langx.security.crypto.key.supplier.bytesbased;

import com.jn.langx.Parser;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.security.crypto.cipher.Asymmetrics;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.crypto.key.spec.der.DsaPrivateKeySpecParser;
import com.jn.langx.security.crypto.key.spec.der.EcPrivateKeySpecParser;
import com.jn.langx.security.crypto.key.spec.der.Pkcs8PrivateKeySpecParser;
import com.jn.langx.security.crypto.key.spec.der.RsaPkcs1PrivateKeySpecParser;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.multivalue.CommonMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.struct.Holder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.Provider;
import java.security.spec.KeySpec;
import java.util.Collection;

public class BytesBasedPrivateKeySupplier implements BytesBasedKeySupplier<PrivateKey> {
    private static final Logger logger = LoggerFactory.getLogger(BytesBasedPrivateKeySupplier.class);

    private final MultiValueMap<String, Parser<byte[], ? extends KeySpec>> keySpecParsers = new CommonMultiValueMap<String, Parser<byte[], ? extends KeySpec>>();

    public BytesBasedPrivateKeySupplier() {
        addKeySpecParser("EC", new EcPrivateKeySpecParser());
        addKeySpecParser("DSA", new DsaPrivateKeySpecParser());
        addKeySpecParser("RSA", new RsaPkcs1PrivateKeySpecParser());
    }

    public void addKeySpecParser(String algorithm, Parser<byte[], ? extends KeySpec> parser) {
        keySpecParsers.add(algorithm, parser);
    }

    @Override
    public PrivateKey get(@NonNull final byte[] bytes, @NonNull final String algorithm, @Nullable Provider provider) {
        String _algorithm = Asymmetrics.extractCipherAlgorithm(algorithm);
        Collection<Parser<byte[], ? extends KeySpec>> parsers = keySpecParsers.get(_algorithm);
        final Holder<KeySpec> keySpecHolder = new Holder<KeySpec>();
        Pipeline.of(parsers)
                .add(Pkcs8PrivateKeySpecParser.INSTANCE)
                .forEach(new Consumer<Parser<byte[], ? extends KeySpec>>() {
                    @Override
                    public void accept(Parser<byte[], ? extends KeySpec> parser) {
                        KeySpec keySpec = null;
                        try {
                            keySpec = parser.parse(bytes);
                            if (keySpec != null) {
                                keySpecHolder.set(keySpec);
                            }
                        } catch (Throwable ex) {
                            logger.warn(ex.getMessage(), ex);
                        }
                    }
                }, new Predicate<Parser<byte[], ? extends KeySpec>>() {
                    @Override
                    public boolean test(Parser<byte[], ? extends KeySpec> value) {
                        return !keySpecHolder.isNull();
                    }
                });
        if (!keySpecHolder.isNull()) {
            PrivateKey privateKey = PKIs.createPrivateKey(_algorithm, provider == null ? null : provider.getName(), keySpecHolder.get());
            return privateKey;
        }
        return null;
    }
}
