package com.jn.langx.security.crypto.digest.spi.sha2;

import com.jn.langx.security.crypto.key.spi.BaseKeyGeneratorSpi;
import com.jn.langx.security.crypto.mac.HmacCoreSpi;

public class HMacSHA2Spis {
    private HMacSHA2Spis(){

    }
    public static class HMacSHA224Spi extends HmacCoreSpi {
        public HMacSHA224Spi() {
            super("SHA-224");
        }
    }

    public static class HMacSHA224KeyGeneratorSpi extends BaseKeyGeneratorSpi{
        public HMacSHA224KeyGeneratorSpi(){
            super("HMACSHA224",224);
        }
    }

    public static class HMacSHA256Spi extends HmacCoreSpi {
        public HMacSHA256Spi() {
            super("SHA-256");
        }
    }

    public static class HMacSHA256KeyGeneratorSpi extends BaseKeyGeneratorSpi{
        public HMacSHA256KeyGeneratorSpi(){
            super("HMACSHA256",256);
        }
    }

    public static class HMacSHA384Spi extends HmacCoreSpi {
        public HMacSHA384Spi() {
            super("SHA-384");
        }
    }
    public static class HMacSHA384KeyGeneratorSpi extends BaseKeyGeneratorSpi{
        public HMacSHA384KeyGeneratorSpi(){
            super("HMACSHA384",384);
        }
    }

    public static class HMacSHA512Spi extends HmacCoreSpi {
        public HMacSHA512Spi() {
            super("SHA-512");
        }
    }
    public static class HMacSHA512KeyGeneratorSpi extends BaseKeyGeneratorSpi{
        public HMacSHA512KeyGeneratorSpi(){
            super("HMACSHA512",512);
        }
    }

    public static class HMacSHA512T224Spi extends HmacCoreSpi{
        public HMacSHA512T224Spi(){
            super("SHA-512/224");
        }
    }
    public static class HMacSHA512T224KeyGeneratorSpi extends BaseKeyGeneratorSpi{
        public HMacSHA512T224KeyGeneratorSpi(){
            super("HMACSHA512/224",224);
        }
    }

    public static class HMacSHA512T256Spi extends HmacCoreSpi{
        public HMacSHA512T256Spi(){
            super("SHA-512/256");
        }
    }
    public static class HMacSHA512T256KeyGeneratorSpi extends BaseKeyGeneratorSpi{
        public HMacSHA512T256KeyGeneratorSpi(){
            super("HMACSHA512/256",256);
        }
    }
}
