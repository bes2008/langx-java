package com.jn.langx.util.hash.streaming.crc;

import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.util.List;

public class CRCs {
    private static final GenericRegistry<CrcAlgoMetadata> ALGO_METADATA_REGISTRY;

    static {
        ALGO_METADATA_REGISTRY = new GenericRegistry<CrcAlgoMetadata>();
        ALGO_METADATA_REGISTRY.init();
        List<CrcAlgoMetadata> metadatas = Collects.asList(
                new CrcAlgoMetadata("CRC-8", 8, 0x7, 0x0, false, false, 0x0, 0xF4),
                new CrcAlgoMetadata("CRC-8/CDMA2000", 8, 0x9B, 0xFF, false, false, 0x0, 0xDA),
                new CrcAlgoMetadata("CRC-8/DARC", 8, 0x39, 0x0, true, true, 0x0, 0x15),
                new CrcAlgoMetadata("CRC-8/DVB-S2", 8, 0xD5, 0x0, false, false, 0x0, 0xBC),
                new CrcAlgoMetadata("CRC-8/EBU", 8, 0x1D, 0xFF, true, true, 0x0, 0x97),
                new CrcAlgoMetadata("CRC-8/I-CODE", 8, 0x1D, 0xFD, false, false, 0x0, 0x7E),
                new CrcAlgoMetadata("CRC-8/ITU", 8, 0x7, 0x0, false, false, 0x55, 0xA1),
                new CrcAlgoMetadata("CRC-8/MAXIM", 8, 0x31, 0x0, true, true, 0x0, 0xA1),
                new CrcAlgoMetadata("CRC-8/ROHC", 8, 0x7, 0xFF, true, true, 0x0, 0xD0),
                new CrcAlgoMetadata("CRC-8/WCDMA", 8, 0x9B, 0x0, true, true, 0x0, 0x25),
                new CrcAlgoMetadata("CRC-16/CCITT-FALSE", 16, 0x1021, 0xFFFF, false, false, 0x0, 0x29B1),
                new CrcAlgoMetadata("CRC-16/ARC", 16, 0x8005, 0x0, true, true, 0x0, 0xBB3D),
                new CrcAlgoMetadata("CRC-16/AUG-CCITT", 16, 0x1021, 0x1D0F, false, false, 0x0, 0xE5CC),
                new CrcAlgoMetadata("CRC-16/BUYPASS", 16, 0x8005, 0x0, false, false, 0x0, 0xFEE8),
                new CrcAlgoMetadata("CRC-16/CDMA2000", 16, 0xC867, 0xFFFF, false, false, 0x0, 0x4C06),
                new CrcAlgoMetadata("CRC-16/DDS-110", 16, 0x8005, 0x800D, false, false, 0x0, 0x9ECF),
                new CrcAlgoMetadata("CRC-16/DECT-R", 16, 0x589, 0x0, false, false, 0x1, 0x7E),
                new CrcAlgoMetadata("CRC-16/DECT-X", 16, 0x589, 0x0, false, false, 0x0, 0x7F),
                new CrcAlgoMetadata("CRC-16/DNP", 16, 0x3D65, 0x0, true, true, 0xFFFF, 0xEA82),
                new CrcAlgoMetadata("CRC-16/EN-13757", 16, 0x3D65, 0x0, false, false, 0xFFFF, 0xC2B7),
                new CrcAlgoMetadata("CRC-16/GENIBUS", 16, 0x1021, 0xFFFF, false, false, 0xFFFF, 0xD64E),
                new CrcAlgoMetadata("CRC-16/MAXIM", 16, 0x8005, 0x0, true, true, 0xFFFF, 0x44C2),
                new CrcAlgoMetadata("CRC-16/MCRF4XX", 16, 0x1021, 0xFFFF, true, true, 0x0, 0x6F91),
                new CrcAlgoMetadata("CRC-16/RIELLO", 16, 0x1021, 0xB2AA, true, true, 0x0, 0x63D0),
                new CrcAlgoMetadata("CRC-16/T10-DIF", 16, 0x8BB7, 0x0, false, false, 0x0, 0xD0DB),
                new CrcAlgoMetadata("CRC-16/TELEDISK", 16, 0xA097, 0x0, false, false, 0x0, 0xFB3),
                new CrcAlgoMetadata("CRC-16/TMS37157", 16, 0x1021, 0x89EC, true, true, 0x0, 0x26B1),
                new CrcAlgoMetadata("CRC-16/USB", 16, 0x8005, 0xFFFF, true, true, 0xFFFF, 0xB4C8),
                new CrcAlgoMetadata("CRC-A", 16, 0x1021, 0xc6c6, true, true, 0x0, 0xBF05),
                new CrcAlgoMetadata("CRC-16/KERMIT", 16, 0x1021, 0x0, true, true, 0x0, 0x2189),
                new CrcAlgoMetadata("CRC-16/MODBUS", 16, 0x8005, 0xFFFF, true, true, 0x0, 0x4B37),
                new CrcAlgoMetadata("CRC-16/X-25", 16, 0x1021, 0xFFFF, true, true, 0xFFFF, 0x906E),
                new CrcAlgoMetadata("CRC-16/XMODEM", 16, 0x1021, 0x0, false, false, 0x0, 0x31C3),
                new CrcAlgoMetadata("CRC-32", 32, 0x04C11DB7L, 0xFFFFFFFFL, true, true, 0xFFFFFFFFL, 0xCBF43926L),
                new CrcAlgoMetadata("CRC-32/BZIP2", 32, 0x04C11DB7L, 0xFFFFFFFFL, false, false, 0xFFFFFFFFL, 0xFC891918L),
                new CrcAlgoMetadata("CRC-32C", 32, 0x1EDC6F41L, 0xFFFFFFFFL, true, true, 0xFFFFFFFFL, 0xE3069283L),
                new CrcAlgoMetadata("CRC-32D", 32, 0xA833982BL, 0xFFFFFFFFL, true, true, 0xFFFFFFFFL, 0x87315576L),
                new CrcAlgoMetadata("CRC-32/JAMCRC", 32, 0x04C11DB7L, 0xFFFFFFFFL, true, true, 0x00000000L, 0x340BC6D9L),
                new CrcAlgoMetadata("CRC-32/MPEG-2", 32, 0x04C11DB7L, 0xFFFFFFFFL, false, false, 0x00000000L, 0x0376E6E7L),
                new CrcAlgoMetadata("CRC-32/POSIX", 32, 0x04C11DB7L, 0x00000000L, false, false, 0xFFFFFFFFL, 0x765E7680L),
                new CrcAlgoMetadata("CRC-32Q", 32, 0x814141ABL, 0x00000000L, false, false, 0x00000000L, 0x3010BF7FL),
                new CrcAlgoMetadata("CRC-32/XFER", 32, 0x000000AFL, 0x00000000L, false, false, 0x00000000L, 0xBD0BE338L),
                new CrcAlgoMetadata("CRC-64", 64, 0x42F0E1EBA9EA3693L, 0x00000000L, false, false, 0x00000000L, 0x6C40DF5F0B497347L),
                new CrcAlgoMetadata("CRC-64/WE", 64, 0x42F0E1EBA9EA3693L, 0xFFFFFFFFFFFFFFFFL, false, false, 0xFFFFFFFFFFFFFFFFL, 0x62EC59E3F1A4F00AL),
                new CrcAlgoMetadata("CRC-64/XZ", 64, 0x42F0E1EBA9EA3693L, 0xFFFFFFFFFFFFFFFFL, true, true, 0xFFFFFFFFFFFFFFFFL, 0x995DC9BBDF1939FAL)
        );

        Collects.forEach(metadatas, new Consumer<CrcAlgoMetadata>() {
            @Override
            public void accept(CrcAlgoMetadata crcAlgoMetadata) {
                ALGO_METADATA_REGISTRY.register(crcAlgoMetadata);
            }
        });

    }
}
