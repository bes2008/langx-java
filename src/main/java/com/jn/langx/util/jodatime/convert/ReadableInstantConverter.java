/*
 *  Copyright 2001-2009 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.jn.langx.util.jodatime.convert;

import com.jn.langx.util.jodatime.Chronology;
import com.jn.langx.util.jodatime.DateTimeUtils;
import com.jn.langx.util.jodatime.DateTimeZone;
import com.jn.langx.util.jodatime.ReadableInstant;
import com.jn.langx.util.jodatime.chrono.ISOChronology;
import com.jn.langx.util.jodatime.convert.AbstractConverter;
import com.jn.langx.util.jodatime.convert.InstantConverter;
import com.jn.langx.util.jodatime.convert.PartialConverter;

/**
 * ReadableInstantConverter extracts milliseconds and chronology from a ReadableInstant.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
class ReadableInstantConverter extends AbstractConverter
        implements InstantConverter, PartialConverter {

    /**
     * Singleton instance.
     */
    static final ReadableInstantConverter INSTANCE = new ReadableInstantConverter();

    /**
     * Restricted constructor.
     */
    protected ReadableInstantConverter() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the chronology, which is taken from the ReadableInstant.
     * If the chronology on the instant is null, the ISOChronology in the
     * specified time zone is used.
     * If the chronology on the instant is not in the specified zone, it is
     * adapted.
     * 
     * @param object  the ReadableInstant to convert, must not be null
     * @param zone  the specified zone to use, null means default zone
     * @return the chronology, never null
     */
    public Chronology getChronology(Object object, DateTimeZone zone) {
        Chronology chrono = ((ReadableInstant) object).getChronology();
        if (chrono == null) {
            return ISOChronology.getInstance(zone);
        }
        DateTimeZone chronoZone = chrono.getZone();
        if (chronoZone != zone) {
            chrono = chrono.withZone(zone);
            if (chrono == null) {
                return ISOChronology.getInstance(zone);
            }
        }
        return chrono;
    }

    /**
     * Gets the chronology, which is taken from the ReadableInstant.
     * <p>
     * If the passed in chronology is non-null, it is used.
     * Otherwise the chronology from the instant is used.
     * 
     * @param object  the ReadableInstant to convert, must not be null
     * @param chrono  the chronology to use, null means use that from object
     * @return the chronology, never null
     */
    public Chronology getChronology(Object object, Chronology chrono) {
        if (chrono == null) {
            chrono = ((ReadableInstant) object).getChronology();
            chrono = DateTimeUtils.getChronology(chrono);
        }
        return chrono;
    }

    /**
     * Extracts the millis from an object of this convertor's type.
     * 
     * @param object  the ReadableInstant to convert, must not be null
     * @param chrono  the non-null result of getChronology
     * @return the millisecond value
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the object is an invalid type
     */
    public long getInstantMillis(Object object, Chronology chrono) {
        return ((ReadableInstant) object).getMillis();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns ReadableInstant.class.
     * 
     * @return ReadableInstant.class
     */
    public Class<?> getSupportedType() {
        return ReadableInstant.class;
    }

}
