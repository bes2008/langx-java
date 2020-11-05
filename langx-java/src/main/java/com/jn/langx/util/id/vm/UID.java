package com.jn.langx.util.id.vm;


import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A unique identifier (uniqueness only guarantied inside of the virtual
 * machine in which it was created).
 *
 * <p>The identifier is composed of:
 * <ol>
 *    <li>A long generated from the current system time (in milliseconds).</li>
 *    <li>A long generated from a counter (which is the number of UID objects
 *        that have been created durring the life of the executing virtual
 *        machine).</li>
 * </ol>
 *
 * <pre>
 *    [ time ] - [ counter ]
 * </pre>
 *
 * <p>Numbers are converted to radix(Character.MAX_RADIX) when converting
 *    to strings.
 *
 * <p>This <i>should</i> provide adequate uniqueness for most purposes.
 *
 */
public class UID implements Serializable, Cloneable {
    private static final long serialVersionUID = -8093336932569424512L;

    /**
     * A counter for generating identity values
     */
    protected static final AtomicLong COUNTER = new AtomicLong(0);

    /**
     * The time portion of the UID
     */
    protected final long time;

    /**
     * The identity portion of the UID
     */
    protected final long id;

    /**
     * Construct a new UID.
     */
    public UID() {
        time = System.currentTimeMillis();
        id = COUNTER.incrementAndGet();
    }

    /**
     * Copy a UID.
     *
     * @param uid
     */
    protected UID(final UID uid) {
        time = uid.time;
        id = uid.id;
    }

    /**
     * Get the time portion of this UID.
     *
     * @return The time portion of this UID.
     */
    public final long getTime() {
        return time;
    }

    /**
     * Get the identity portion of this UID.
     *
     * @return The identity portion of this UID.
     */
    public final long getID() {
        return id;
    }

    /**
     * Return a string representation of this UID.
     *
     * @return A string representation of this UID.
     */
    public String toString() {
        return
                Long.toString(time, Character.MAX_RADIX) +
                        "-" +
                        Long.toString(id, Character.MAX_RADIX);
    }

    /**
     * Return the hash code of this UID.
     *
     * @return The hash code of this UID.
     */
    public int hashCode() {
        return (int) id;
    }

    /**
     * Checks if the given object is equal to this UID.
     *
     * @param obj Object to test equality with.
     * @return True if object is equal to this UID.
     */
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj != null && obj.getClass() == getClass()) {
            UID uid = (UID) obj;

            return
                    uid.time == time &&
                            uid.id == id;
        }

        return false;
    }

    /**
     * Returns a copy of this UID.
     *
     * @return A copy of this UID.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * Returns a UID as a string.
     *
     * @return UID as a string.
     */
    public static String asString() {
        return new UID().toString();
    }
}
