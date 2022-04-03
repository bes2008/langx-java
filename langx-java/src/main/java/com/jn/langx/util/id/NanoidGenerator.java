package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;
import com.jn.langx.util.function.Function3;
import com.jn.langx.util.function.Supplier;

public class NanoidGenerator implements IdGenerator {
    private String alphabet = Nanoids.URL_ALPHABET;
    private int idLength = 21;
    private Supplier<Integer, byte[]> randomBytesSupplier = GlobalThreadLocalMap.pooledBytesRandom();
    private Function3<String, Integer, Supplier<Integer, byte[]>, String> idGenFun = Nanoids.SIMPLE_ID_FUN;


    @Override
    public String get(Object o) {
        return get();
    }

    @Override
    public String get() {
        return Nanoids.nanoid(alphabet, idLength, randomBytesSupplier, idGenFun);
    }


    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public int getIdLength() {
        return idLength;
    }

    public void setIdLength(int idLength) {
        this.idLength = idLength;
    }

    public Supplier<Integer, byte[]> getRandomBytesSupplier() {
        return randomBytesSupplier;
    }

    public void setRandomBytesSupplier(Supplier<Integer, byte[]> randomBytesSupplier) {
        this.randomBytesSupplier = randomBytesSupplier;
    }

    public Function3<String, Integer, Supplier<Integer, byte[]>, String> getIdGenFun() {
        return idGenFun;
    }

    public void setIdGenFun(Function3<String, Integer, Supplier<Integer, byte[]>, String> idGenFun) {
        this.idGenFun = idGenFun;
    }
}
