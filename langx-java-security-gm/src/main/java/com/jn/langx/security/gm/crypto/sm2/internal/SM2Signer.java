package com.jn.langx.security.gm.crypto.sm2.internal;


import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SM2Signer implements DSA {
    private ECKeyParameters key;
    private SecureRandom secureRandom;
    private static final BigInteger ZERO;
    private static final BigInteger ONE;

    static {
        ZERO = BigInteger.valueOf(0L);
        ONE = BigInteger.valueOf(1L);
    }

    public SM2Signer() {
        this.key = null;
        this.secureRandom = null;
    }

    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (b) {
            if (cipherParameters instanceof ParametersWithRandom) {
                final ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
                this.secureRandom = parametersWithRandom.getRandom();
                this.key = (ECPrivateKeyParameters) parametersWithRandom.getParameters();
            } else {
                try {
                    this.secureRandom = SecureRandom.getInstance("SHA1PRNG");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                this.key = (ECPrivateKeyParameters) cipherParameters;
            }
        } else {
            this.key = (ECPublicKeyParameters) cipherParameters;
        }
    }

    @Override
    public BigInteger[] generateSignature(final byte[] array) {
        final BigInteger n = this.key.getParameters().getN();
        final BigInteger d = ((ECPrivateKeyParameters) this.key).getD();
        final ECPoint g = this.key.getParameters().getG();
        final BigInteger bigInteger = new BigInteger(1, array);
        BigInteger mod;
        BigInteger mod2;
        while (true) {
            final BigInteger gen = MyBigInteger.gen(n, this.secureRandom);
            final BigInteger bigInteger2 = g.multiply(gen).getX().toBigInteger();
            mod = bigInteger.add(bigInteger2).mod(n);
            if (!mod.equals(SM2Signer.ZERO) && !mod.add(gen).equals(n)) {
                mod2 = SM2Signer.ONE.add(d).modInverse(n).multiply(gen.subtract(mod.multiply(d))).mod(n);
                if (!mod2.equals(SM2Signer.ZERO)) {
                    break;
                }
                continue;
            }
        }
        return new BigInteger[]{mod, mod2};
    }

    @Override
    public boolean verifySignature(final byte[] array, final BigInteger bigInteger, final BigInteger bigInteger2) {
        final BigInteger n = this.key.getParameters().getN();
        final ECPoint g = this.key.getParameters().getG();
        final ECPoint q = ((ECPublicKeyParameters) this.key).getQ();
        if (bigInteger.compareTo(SM2Signer.ONE) < 0 || bigInteger.compareTo(n) >= 0) {
            return false;
        }
        if (bigInteger2.compareTo(SM2Signer.ONE) < 0 || bigInteger2.compareTo(n) >= 0) {
            return false;
        }
        final BigInteger bigInteger3 = new BigInteger(1, array);
        final BigInteger mod = bigInteger.add(bigInteger2).mod(n);
        if (mod.equals(SM2Signer.ZERO)) {
            return false;
        }
        final ECPoint multiply = g.multiply(bigInteger2);
        final ECPoint multiply2 = q.multiply(mod);
        final BigInteger bigInteger4 = multiply.getX().toBigInteger();
        final BigInteger bigInteger5 = multiply2.getX().toBigInteger();
        final BigInteger bigInteger6 = multiply.add(multiply2).getX().toBigInteger();
        final BigInteger mod2 = bigInteger3.add(bigInteger6).mod(n);
        return mod2.equals(bigInteger);
    }
}
