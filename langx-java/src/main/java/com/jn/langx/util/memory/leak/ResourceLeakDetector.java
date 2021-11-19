package com.jn.langx.util.memory.leak;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.text.properties.PropertiesAccessor;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.random.ThreadLocalRandom;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import static com.jn.langx.util.SystemPropertys.NEWLINE;

public class ResourceLeakDetector<T> {
    private static String RESOURCE_LEAK_DETECTION_PREFIX = SystemPropertys.getAccessor().getString("RESOURCE_LEAK_DETECTION_PREFIX", "langx.leakDetection");
    private static final String PROP_LEVEL = RESOURCE_LEAK_DETECTION_PREFIX + ".level";
    private static final Level DEFAULT_LEVEL = Level.SIMPLE;

    private static final String PROP_TARGET_RECORDS = RESOURCE_LEAK_DETECTION_PREFIX + ".targetRecords";
    private static final int DEFAULT_TARGET_RECORDS = 4;

    private static final String PROP_SAMPLING_INTERVAL = RESOURCE_LEAK_DETECTION_PREFIX + ".samplingInterval";
    // There is a minor performance benefit in TLR if this is a power of 2.
    private static final int DEFAULT_SAMPLING_INTERVAL = 128;

    private static final int TARGET_RECORDS;
    static final int SAMPLING_INTERVAL;


    /**
     * Represents the level of resource leak detection.
     */
    public enum Level {
        /**
         * Disables resource leak detection.
         */
        DISABLED,
        /**
         * Enables simplistic sampling resource leak detection which reports there is a leak or not,
         * at the cost of small overhead (default).
         */
        SIMPLE,
        /**
         * Enables advanced sampling resource leak detection which reports where the leaked object was accessed
         * recently at the cost of high overhead.
         */
        ADVANCED,
        /**
         * Enables paranoid resource leak detection which reports where the leaked object was accessed recently,
         * at the cost of the highest possible overhead (for testing purposes only).
         */
        PARANOID;

        /**
         * Returns level based on string value. Accepts also string that represents ordinal number of enum.
         *
         * @param levelStr - level string : DISABLED, SIMPLE, ADVANCED, PARANOID. Ignores case.
         * @return corresponding level or SIMPLE level in case of no match.
         */
        static Level parseLevel(String levelStr) {
            String trimmedLevelStr = levelStr.trim();
            for (Level l : values()) {
                if (trimmedLevelStr.equalsIgnoreCase(l.name()) || trimmedLevelStr.equals(String.valueOf(l.ordinal()))) {
                    return l;
                }
            }
            return DEFAULT_LEVEL;
        }
    }

    private static Level level;


    static {
        final boolean disabled;
        PropertiesAccessor systemPropertiesAccessor = SystemPropertys.getAccessor();
        String enabledSwitch = RESOURCE_LEAK_DETECTION_PREFIX + ".enabled";
        if (systemPropertiesAccessor.get(enabledSwitch) != null) {
            disabled = systemPropertiesAccessor.getBoolean(enabledSwitch, false);
            Logger logger = Loggers.getLogger(ResourceLeakDetector.class);
            logger.debug("-{}: {}", enabledSwitch, disabled);
        } else {
            disabled = false;
        }

        Level defaultLevel = disabled ? Level.DISABLED : DEFAULT_LEVEL;

        String levelStr = systemPropertiesAccessor.getString(PROP_LEVEL, defaultLevel.name());
        Level level = Level.parseLevel(levelStr);

        TARGET_RECORDS = systemPropertiesAccessor.getInteger(PROP_TARGET_RECORDS, DEFAULT_TARGET_RECORDS);
        SAMPLING_INTERVAL = systemPropertiesAccessor.getInteger(PROP_SAMPLING_INTERVAL, DEFAULT_SAMPLING_INTERVAL);

        ResourceLeakDetector.level = level;
        Logger logger = Loggers.getLogger(ResourceLeakDetector.class);
        if (logger.isDebugEnabled()) {
            logger.debug("-D{}: {}", PROP_LEVEL, level.name().toLowerCase());
            logger.debug("-D{}: {}", PROP_TARGET_RECORDS, TARGET_RECORDS);
        }
    }


    /**
     * Returns {@code true} if resource leak detection is enabled.
     */
    public static boolean isEnabled() {
        return getLevel().ordinal() > Level.DISABLED.ordinal();
    }

    /**
     * Sets the resource leak detection level.
     */
    public static void setLevel(Level level) {
        Preconditions.checkNotNull(level);
        ResourceLeakDetector.level = level;
    }

    /**
     * Returns the current resource leak detection level.
     */
    public static Level getLevel() {
        return level;
    }

    /**
     * the collection of active resources
     */
    private final Set<DefaultResourceLeak> allLeaks = Collections.newSetFromMap(new ConcurrentHashMap<DefaultResourceLeak, Boolean>());

    private final ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
    private final ConcurrentMap<String, Boolean> reportedLeaks = new ConcurrentHashMap<String, Boolean>();

    private final String resourceType;
    private final int samplingInterval;

    /**
     * This should not be used directly by users of {@link ResourceLeakDetector}.
     * Please use {@link ResourceLeakDetectorFactory#newResourceLeakDetector(Class)}
     * or {@link ResourceLeakDetectorFactory#newResourceLeakDetector(Class, int)}
     */
    public ResourceLeakDetector(Class<?> resourceType, int samplingInterval) {
        this.resourceType = Reflects.getSimpleClassName(resourceType);
        this.samplingInterval = samplingInterval;
    }

    /**
     * Creates a new {@link ResourceLeakTracker} which is expected to be closed via
     * {@link ResourceLeakTracker#close(Object)} when the related resource is deallocated.
     *
     * @return the {@link ResourceLeakTracker} or {@code null}
     */
    @SuppressWarnings("unchecked")
    public final ResourceLeakTracker<T> track(T obj) {
        return track0(obj);
    }

    @SuppressWarnings("unchecked")
    private DefaultResourceLeak track0(T obj) {
        Level level = ResourceLeakDetector.level;
        if (level == Level.DISABLED) {
            return null;
        }

        if (level.ordinal() < Level.PARANOID.ordinal()) {
            if ((ThreadLocalRandom.current().nextInt(samplingInterval)) == 0) {
                reportLeak();
                return new DefaultResourceLeak(obj, refQueue, allLeaks);
            }
            return null;
        }
        reportLeak();
        return new DefaultResourceLeak(obj, refQueue, allLeaks);
    }

    private void clearRefQueue() {
        for (; ; ) {
            @SuppressWarnings("unchecked")
            DefaultResourceLeak ref = (DefaultResourceLeak) refQueue.poll();
            if (ref == null) {
                break;
            }
            ref.dispose();
        }
    }

    private void reportLeak() {
        Logger logger = Loggers.getLogger(getClass());
        if (!logger.isErrorEnabled()) {
            clearRefQueue();
            return;
        }
        // Detect and report previous leaks.
        for (; ; ) {
            DefaultResourceLeak ref = (DefaultResourceLeak) refQueue.poll();
            if (ref == null) {
                break;
            }

            if (!ref.dispose()) {
                continue;
            }

            String records = ref.toString();
            if (reportedLeaks.putIfAbsent(records, Boolean.TRUE) == null) {
                if (records.isEmpty()) {
                    reportUntracedLeak(resourceType);
                } else {
                    reportTracedLeak(resourceType, records);
                }
            }
        }
    }

    /**
     * This method is called when a traced leak is detected. It can be overridden for tracking how many times leaks
     * have been detected.
     */
    protected void reportTracedLeak(String resourceType, String records) {
        Logger logger = Loggers.getLogger(getClass());
        logger.error(
                "LEAK: {} was not (close | stop | release | dispose, or other similar method) before it's garbage-collected. " +
                        "See https://netty.io/wiki/reference-counted-objects.html for more information.{}",
                resourceType, records);
    }

    /**
     * This method is called when an untraced leak is detected. It can be overridden for tracking how many times leaks
     * have been detected.
     */
    protected void reportUntracedLeak(String resourceType) {
        Logger logger = Loggers.getLogger(getClass());
        logger.error("LEAK: {} was not (close | stop | release | dispose, or other similar method) before it's garbage-collected. " +
                        "Enable advanced leak reporting to find out where the leak occurred. " +
                        "To enable advanced leak reporting, " +
                        "specify the JVM option '-D{}={}' or call {}.setLevel() " +
                        "See https://netty.io/wiki/reference-counted-objects.html for more information.",
                resourceType, PROP_LEVEL, Level.ADVANCED.name().toLowerCase(), Reflects.getSimpleClassName(this));
    }


    @SuppressWarnings({"deprecation","unused"})
    private static final class DefaultResourceLeak<T> extends WeakReference<Object> implements ResourceLeakTracker<T> {

        /**
         * generics and updaters do not mix.
         */
        private static final AtomicReferenceFieldUpdater<DefaultResourceLeak, Record> headUpdater = AtomicReferenceFieldUpdater.newUpdater(DefaultResourceLeak.class, Record.class, "head");

        /**
         * generics and updaters do not mix.
         */
        private static final AtomicIntegerFieldUpdater<DefaultResourceLeak> droppedRecordsUpdater = AtomicIntegerFieldUpdater.newUpdater(DefaultResourceLeak.class, "droppedRecords");

        private volatile Record head;
        private volatile int droppedRecords;

        private final Set<DefaultResourceLeak> allLeaks;
        private final int trackedHash;

        DefaultResourceLeak(Object referent, ReferenceQueue<Object> refQueue, Set<DefaultResourceLeak> allLeaks) {
            super(referent, refQueue);

            assert referent != null;

            // Store the hash of the tracked object to later assert it in the close(...) method.
            // It's important that we not store a reference to the referent as this would disallow it from
            // be collected via the WeakReference.
            trackedHash = System.identityHashCode(referent);
            Preconditions.checkNotNull(allLeaks);
            allLeaks.add(this);
            // Create a new Record so we always have the creation stacktrace included.
            headUpdater.set(this, new ResourceLeakDetector.Record(ResourceLeakDetector.Record.BOTTOM));
            this.allLeaks = allLeaks;
        }

        @Override
        public void record() {
            record0(null);
        }

        @Override
        public void record(Object hint) {
            record0(hint);
        }

        /**
         * This method works by exponentially backing off as more records are present in the stack. Each record has a
         * 1 / 2^n chance of dropping the top most record and replacing it with itself. This has a number of convenient
         * properties:
         * <p>
         * <ol>
         * <li>  The current record is always recorded. This is due to the compare and swap dropping the top most
         * record, rather than the to-be-pushed record.
         * <li>  The very last access will always be recorded. This comes as a property of 1.
         * <li>  It is possible to retain more records than the target, based upon the probability distribution.
         * <li>  It is easy to keep a precise record of the number of elements in the stack, since each element has to
         * know how tall the stack is.
         * </ol>
         * <p>
         * In this particular implementation, there are also some advantages. A thread local random is used to decide
         * if something should be recorded. This means that if there is a deterministic access pattern, it is now
         * possible to see what other accesses occur, rather than always dropping them. Second, after
         * {@link #TARGET_RECORDS} accesses, backoff occurs. This matches typical access patterns,
         * where there are either a high number of accesses (i.e. a cached buffer), or low (an ephemeral buffer), but
         * not many in between.
         * <p>
         * The use of atomics avoids serializing a high number of accesses, when most of the records will be thrown
         * away. High contention only happens when there are very few existing records, which is only likely when the
         * object isn't shared! If this is a problem, the loop can be aborted and the record dropped, because another
         * thread won the race.
         */
        private void record0(Object hint) {
            // Check TARGET_RECORDS > 0 here to avoid similar check before remove from and add to lastRecords
            if (TARGET_RECORDS > 0) {
                ResourceLeakDetector.Record oldHead;
                ResourceLeakDetector.Record prevHead;
                ResourceLeakDetector.Record newHead;
                boolean dropped;
                do {
                    if ((prevHead = oldHead = headUpdater.get(this)) == null) {
                        // already closed.
                        return;
                    }
                    final int numElements = oldHead.pos + 1;
                    if (numElements >= TARGET_RECORDS) {
                        final int backOffFactor = Math.min(numElements - TARGET_RECORDS, 30);
                        if (dropped = ThreadLocalRandom.current().nextInt(1 << backOffFactor) != 0) {
                            prevHead = oldHead.next;
                        }
                    } else {
                        dropped = false;
                    }
                    newHead = hint != null ? new ResourceLeakDetector.Record(prevHead, hint) : new ResourceLeakDetector.Record(prevHead);
                } while (!headUpdater.compareAndSet(this, oldHead, newHead));
                if (dropped) {
                    droppedRecordsUpdater.incrementAndGet(this);
                }
            }
        }

        boolean dispose() {
            clear();
            return allLeaks.remove(this);
        }

        public boolean close() {
            if (allLeaks.remove(this)) {
                // Call clear so the reference is not even enqueued.
                clear();
                headUpdater.set(this, null);
                return true;
            }
            return false;
        }

        @Override
        public boolean close(T trackedObject) {
            // Ensure that the object that was tracked is the same as the one that was passed to close(...).
            assert trackedHash == System.identityHashCode(trackedObject);

            try {
                return close();
            } finally {
                // This method will do `synchronized(trackedObject)` and we should be sure this will not cause deadlock.
                // It should not, because somewhere up the callstack should be a (successful) `trackedObject.release`,
                // therefore it is unreasonable that anyone else, anywhere, is holding a lock on the trackedObject.
                // (Unreasonable but possible, unfortunately.)
                reachabilityFence0(trackedObject);
            }
        }

        /**
         * Ensures that the object referenced by the given reference remains
         * <a href="package-summary.html#reachability"><em>strongly reachable</em></a>,
         * regardless of any prior actions of the program that might otherwise cause
         * the object to become unreachable; thus, the referenced object is not
         * reclaimable by garbage collection at least until after the invocation of
         * this method.
         * <p>
         * <p> Recent versions of the JDK have a nasty habit of prematurely deciding objects are unreachable.
         * see: https://stackoverflow.com/questions/26642153/finalize-called-on-strongly-reachable-object-in-java-8
         * The Java 9 method Reference.reachabilityFence offers a solution to this problem.
         * <p>
         * <p> This method is always implemented as a synchronization on {@code ref}, not as
         * {@code Reference.reachabilityFence} for consistency across platforms and to allow building on JDK 6-8.
         * <b>It is the caller's responsibility to ensure that this synchronization will not cause deadlock.</b>
         *
         * @param ref the reference. If {@code null}, this method has no effect.
         */
        private static void reachabilityFence0(Object ref) {
            if (ref != null) {
                //synchronized (ref) {
                // Empty synchronized is ok: https://stackoverflow.com/a/31933260/1151521
                //}
            }
        }

        @Override
        public String toString() {
            Record oldHead = headUpdater.getAndSet(this, null);
            if (oldHead == null) {
                // Already closed
                return "";
            }

            final int dropped = droppedRecordsUpdater.get(this);
            int duped = 0;

            int present = oldHead.pos + 1;
            // Guess about 2 kilobytes per stack trace
            StringBuilder buf = new StringBuilder(present * 2048).append(NEWLINE);
            buf.append("Recent access records: ").append(NEWLINE);

            int i = 1;
            Set<String> seen = new HashSet<String>(present);
            for (; oldHead != Record.BOTTOM; oldHead = oldHead.next) {
                String s = oldHead.toString();
                if (seen.add(s)) {
                    if (oldHead.next == Record.BOTTOM) {
                        buf.append("Created at:").append(NEWLINE).append(s);
                    } else {
                        buf.append('#').append(i++).append(':').append(NEWLINE).append(s);
                    }
                } else {
                    duped++;
                }
            }

            if (duped > 0) {
                buf.append(": ")
                        .append(duped)
                        .append(" leak records were discarded because they were duplicates")
                        .append(NEWLINE);
            }

            if (dropped > 0) {
                buf.append(": ")
                        .append(dropped)
                        .append(" leak records were discarded because the leak record count is targeted to ")
                        .append(TARGET_RECORDS)
                        .append(". Use system property ")
                        .append(PROP_TARGET_RECORDS)
                        .append(" to increase the limit.")
                        .append(NEWLINE);
            }

            buf.setLength(buf.length() - NEWLINE.length());
            return buf.toString();
        }
    }

    private static final AtomicReference<String[]> excludedMethods =
            new AtomicReference<String[]>(Emptys.EMPTY_STRINGS);

    public static void addExclusions(Class clz, String... methodNames) {
        Set<String> nameSet = new HashSet<String>(Arrays.asList(methodNames));
        // Use loop rather than lookup. This avoids knowing the parameters, and doesn't have to handle
        // NoSuchMethodException.
        for (Method method : clz.getDeclaredMethods()) {
            if (nameSet.remove(method.getName()) && nameSet.isEmpty()) {
                break;
            }
        }
        if (!nameSet.isEmpty()) {
            throw new IllegalArgumentException("Can't find '" + nameSet + "' in " + clz.getName());
        }
        String[] oldMethods;
        String[] newMethods;
        do {
            oldMethods = excludedMethods.get();
            newMethods = Arrays.copyOf(oldMethods, oldMethods.length + 2 * methodNames.length);
            for (int i = 0; i < methodNames.length; i++) {
                newMethods[oldMethods.length + i * 2] = clz.getName();
                newMethods[oldMethods.length + i * 2 + 1] = methodNames[i];
            }
        } while (!excludedMethods.compareAndSet(oldMethods, newMethods));
    }

    private static final class Record extends Throwable {
        private static final long serialVersionUID = 6065153674892850720L;

        private static final ResourceLeakDetector.Record BOTTOM = new ResourceLeakDetector.Record();

        private final String hintString;
        private final ResourceLeakDetector.Record next;
        private final int pos;

        Record(Record next, Object hint) {
            // This needs to be generated even if toString() is never called as it may change later on.
            hintString = hint instanceof ResourceLeakHint ? ((ResourceLeakHint) hint).toHintString() : hint.toString();
            this.next = next;
            this.pos = next.pos + 1;
        }

        Record(Record next) {
            hintString = null;
            this.next = next;
            this.pos = next.pos + 1;
        }

        // Used to terminate the stack
        private Record() {
            hintString = null;
            next = null;
            pos = -1;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder(2048);
            if (hintString != null) {
                buf.append("\tHint: ").append(hintString).append(NEWLINE);
            }

            // Append the stack trace.
            StackTraceElement[] array = getStackTrace();
            // Skip the first three elements.
            out:
            for (int i = 3; i < array.length; i++) {
                StackTraceElement element = array[i];
                // Strip the noisy stack trace elements.
                String[] exclusions = excludedMethods.get();
                for (int k = 0; k < exclusions.length; k += 2) {
                    if (exclusions[k].equals(element.getClassName())
                            && exclusions[k + 1].equals(element.getMethodName())) {
                        continue out;
                    }
                }

                buf.append('\t');
                buf.append(element.toString());
                buf.append(NEWLINE);
            }
            return buf.toString();
        }
    }
}
