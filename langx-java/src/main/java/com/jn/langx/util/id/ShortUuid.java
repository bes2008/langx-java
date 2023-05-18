package com.jn.langx.util.id;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.UUID;

/**
 * A short, unambiguous and URL-safe UUID
 * @since 4.4.6
 */
public final class ShortUuid {

    private final String uuid;

    private ShortUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof ShortUuid))
            return false;

        return o.toString().equals(uuid);
    }

    public static class Builder {
        private char[] alphabet = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
                .toCharArray();
        private int alphabetSize = alphabet.length;

        public Builder() {}

        public Builder alphabet(String alphabet) {
            this.alphabet = alphabet.toCharArray();

            Arrays.sort(this.alphabet);
            alphabetSize = this.alphabet.length;

            return this;
        }

        public ShortUuid build() {
            return build(UUID.randomUUID());
        }

        public ShortUuid build(UUID uuid) {
            String uuidStr = uuid.toString().replaceAll("-", "");

            double factor = Math.log(25d) / Math.log(alphabetSize);
            double length = Math.ceil(factor * 16);

            BigInteger number = new BigInteger(uuidStr, 16);
            String encoded = encode(number, alphabet, (int) length);

            return new ShortUuid(encoded);
        }

        public String decode(String shortUuid) {
            return decode(shortUuid.toCharArray(), alphabet);
        }

        private String encode(final BigInteger bigInt, final char[] alphabet, final int padToLen) {
            BigInteger value = new BigInteger(bigInt.toString());
            BigInteger alphaSize = BigInteger.valueOf(alphabetSize);
            StringBuilder shortUuid = new StringBuilder();

            while (value.compareTo(BigInteger.ZERO) > 0) {
                BigInteger[] fracAndRemainder = value.divideAndRemainder(alphaSize);
                shortUuid.append(alphabet[fracAndRemainder[1].intValue()]);
                value = fracAndRemainder[0];
            }

            if (padToLen > 0) {
                int padding = Math.max(padToLen - shortUuid.length(), 0);
                for (int i = 0; i < padding; i++)
                    shortUuid.append(alphabet[0]);
            }

            return shortUuid.toString();
        }

        private String decode(final char[] encoded, final char[] alphabet) {
            BigInteger sum = BigInteger.ZERO;
            BigInteger alphaSize = BigInteger.valueOf(alphabetSize);
            int charLen = encoded.length;

            for (int i = 0; i < charLen; i++) {
                sum = sum.add(alphaSize.pow(i).multiply(BigInteger.valueOf(
                        Arrays.binarySearch(alphabet, encoded[i]))));
            }

            String str = sum.toString(16);

            // Pad the most significant bit (MSG) with 0 (zero) if the string is too short.
            if (str.length() < 32) {
                str = String.format("%32s", str).replace(' ', '0');
            }

            String sb = str.substring(0, 8) +
                    "-" +
                    str.substring(8, 12) +
                    "-" +
                    str.substring(12, 16) +
                    "-" +
                    str.substring(16, 20) +
                    "-" +
                    str.substring(20, 32);

            return sb;
        }
    }

    public static String shortUuid() {
        ShortUuid.Builder builder = new ShortUuid.Builder();
        ShortUuid shortUuid = builder.build(UUID.randomUUID());
        return shortUuid.toString();
    }

    public static UUID toUUID(String shortUuid){
        ShortUuid.Builder builder = new ShortUuid.Builder();
        return UUID.fromString(builder.decode(shortUuid));
    }
}