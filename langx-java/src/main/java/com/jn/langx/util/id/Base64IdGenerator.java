package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;
import com.jn.langx.codec.base64.Base64;

import java.security.SecureRandom;
import java.util.Random;

/**
 * https://github.com/elastic/elasticsearch/tree/master/server/src/main/java/org/elasticsearch/common
 */
@SuppressWarnings("ALL")
public class Base64IdGenerator implements IdGenerator {
    public static final SecureRandom INSTANCE = new SecureRandom();

    @Override
    public String get() {
        return get(null);
    }

    /**
     * Returns a Base64 encoded version of a Version 4.0 compatible UUID
     * as defined here: http://www.ietf.org/rfc/rfc4122.txt
     */
    @Override
    public String get(Object o) {
        return getBase64UUID(INSTANCE);
    }


    /**
     * Returns a Base64 encoded version of a Version 4.0 compatible UUID
     * randomly initialized by the given {@link java.util.Random} instance
     * as defined here: http://www.ietf.org/rfc/rfc4122.txt
     */
    public String getBase64UUID(Random random) {
        return Base64.encodeBase64URLSafeString(getUUIDBytes(random));
    }

    private byte[] getUUIDBytes(Random random) {
        final byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        /* Set the version to version 4 (see http://www.ietf.org/rfc/rfc4122.txt)
         * The randomly or pseudo-randomly generated version.
         * The version number is in the most significant 4 bits of the time
         * stamp (bits 4 through 7 of the time_hi_and_version field).*/
        randomBytes[6] &= 0x0f;  /* clear the 4 most significant bits for the version  */
        randomBytes[6] |= 0x40;  /* set the version to 0100 / 0x40 */

        /* Set the variant:
         * The high field of th clock sequence multiplexed with the variant.
         * We set only the MSB of the variant*/
        randomBytes[8] &= 0x3f;  /* clear the 2 most significant bits */
        randomBytes[8] |= 0x80;  /* set the variant (MSB is set)*/
        return randomBytes;
    }
}
