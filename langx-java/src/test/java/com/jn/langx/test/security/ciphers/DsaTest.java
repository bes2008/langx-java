package com.jn.langx.test.security.ciphers;


import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.crypto.signature.DSAs;
import org.junit.Assert;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.security.KeyPair;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SampleTime)
@State(Scope.Benchmark)
@Threads(4)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class DsaTest {

    private byte[] pubKey;
    private byte[] privKey;

    @Setup
    public void init() {
        KeyPair keyPair = PKIs.createKeyPair("DSA", null, 1024, null);
        this.privKey = keyPair.getPrivate().getEncoded();
        this.pubKey = keyPair.getPublic().getEncoded();
    }

    @Benchmark
    @Warmup(iterations = 1, batchSize = 20)
    @Measurement(iterations = 300, batchSize = 100)
    public void testDSA() {
        byte[] text = "hello, dsa".getBytes();
        byte[] s = DSAs.sign(this.privKey, text);
        boolean ok = DSAs.verify(this.pubKey, text, s);
        Assert.assertTrue(ok);
    }

    @TearDown
    public void destroy() {
        this.pubKey = null;
        this.privKey = null;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(DsaTest.class.getSimpleName()).build();
        new Runner(options).run();
    }
}
