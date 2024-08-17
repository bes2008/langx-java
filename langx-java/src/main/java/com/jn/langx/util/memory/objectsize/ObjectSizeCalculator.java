package com.jn.langx.util.memory.objectsize;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.collection.IdentityHashSet;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.reference.ReferenceType;
import com.jn.langx.util.reflect.type.Primitives;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Contains utility methods for calculating the memory usage of objects. It
 * only works on the HotSpot JVM, and infers the actual memory layout (32 bit
 * vs. 64 bit word size, compressed object pointers vs. uncompressed) from
 * best available indicators. It can reliably detect a 32 bit vs. 64 bit JVM.
 * It can only make an educated guess at whether compressed OOPs are used,
 * though; specifically, it knows what the JVM's default choice of OOP
 * compression would be based on HotSpot version and maximum heap sizes, but if
 * the choice is explicitly overridden with the <tt>-XX:{+|-}UseCompressedOops</tt> command line
 * switch, it can not detect
 * this fact and will report incorrect sizes, as it will presume the default JVM
 * behavior.
 *
 */
public class ObjectSizeCalculator {


    private static class CurrentLayout {
        private static final MemoryLayoutSpecification SPEC = getEffectiveMemoryLayoutSpecification();
    }

    /**
     * Given an object, returns the total allocated size, in bytes, of the object
     * and all other objects reachable from it.  Attempts to to detect the current JVM memory layout,
     * but may fail with {@link UnsupportedOperationException};
     *
     * @param obj the object; can be null. Passing in a {@link java.lang.Class} object doesn't do
     *            anything special, it measures the size of all objects
     *            reachable through it (which will include its class loader, and by
     *            extension, all other Class objects loaded by
     *            the same loader, and all the parent class loaders). It doesn't provide the
     *            size of the static fields in the JVM class that the Class object
     *            represents.
     * @return the total allocated size of the object and all other objects it
     * retains.
     * @throws UnsupportedOperationException if the current vm memory layout cannot be detected.
     */
    public static long getObjectSize(Object obj) throws UnsupportedOperationException {
        return obj == null ? 0 : new ObjectSizeCalculator(CurrentLayout.SPEC).calculateObjectSize(obj);
    }

    // Fixed object header size for arrays.
    private final int arrayHeaderSize;
    // Fixed object header size for non-array objects.
    private final int objectHeaderSize;
    // Padding for the object size - if the object size is not an exact multiple
    // of this, it is padded to the next multiple.
    private final int objectPadding;
    // Size of reference (pointer) fields.
    private final int referenceSize;
    // Padding for the fields of superclass before fields of subclasses are
    // added.
    private final int superclassFieldPadding;

    private static final ConcurrentReferenceHashMap<Class<?>, ClassSizeInfo> classSizeInfos = new ConcurrentReferenceHashMap<Class<?>, ClassSizeInfo>(100, ReferenceType.WEAK, ReferenceType.STRONG);
            /*
            CacheBuilder.<Class<?>, ClassSizeInfo>newBuilder().loader(new AbstractCacheLoader<Class<?>, ClassSizeInfo>() {
                public ClassSizeInfo load(Class<?> clazz) {
                    return new ClassSizeInfo(clazz);
                }
            }).build();
*/

    private final Set<Object> alreadyVisited = new IdentityHashSet<Object>();
    private final Deque<Object> pending = new ArrayDeque<Object>(16 * 1024);
    private long size;

    /**
     * Creates an object size calculator that can calculate object sizes for a given
     * {@code memoryLayoutSpecification}.
     *
     * @param memoryLayoutSpecification a description of the JVM memory layout.
     */
    public ObjectSizeCalculator(MemoryLayoutSpecification memoryLayoutSpecification) {
        Preconditions.checkNotNull(memoryLayoutSpecification);
        arrayHeaderSize = memoryLayoutSpecification.getArrayHeaderSize();
        objectHeaderSize = memoryLayoutSpecification.getObjectHeaderSize();
        objectPadding = memoryLayoutSpecification.getObjectPadding();
        referenceSize = memoryLayoutSpecification.getReferenceSize();
        superclassFieldPadding = memoryLayoutSpecification.getSuperclassFieldPadding();
    }

    /**
     * Given an object, returns the total allocated size, in bytes, of the object
     * and all other objects reachable from it.
     *
     * @param obj the object; can be null. Passing in a {@link java.lang.Class} object doesn't do
     *            anything special, it measures the size of all objects
     *            reachable through it (which will include its class loader, and by
     *            extension, all other Class objects loaded by
     *            the same loader, and all the parent class loaders). It doesn't provide the
     *            size of the static fields in the JVM class that the Class object
     *            represents.
     * @return the total allocated size of the object and all other objects it
     * retains.
     */
    public synchronized long calculateObjectSize(Object obj) {
        // Breadth-first traversal instead of naive depth-first with recursive
        // implementation, so we don't blow the stack traversing long linked lists.
        try {
            for (; ; ) {
                visit(obj);
                if (pending.isEmpty()) {
                    return size;
                }
                obj = pending.removeFirst();
            }
        } finally {
            alreadyVisited.clear();
            pending.clear();
            size = 0;
        }
    }

    private void visit(Object obj) {
        if (alreadyVisited.contains(obj)) {
            return;
        }
        final Class<?> clazz = obj.getClass();
        if (clazz == ArrayElementsVisitor.class) {
            ((ArrayElementsVisitor) obj).visit(this);
        } else {
            alreadyVisited.add(obj);
            if (clazz.isArray()) {
                visitArray(obj);
            } else {
                Maps.putIfAbsent(classSizeInfos, clazz, new Supplier<Class<?>, ClassSizeInfo>() {
                    @Override
                    public ClassSizeInfo get(Class<?> clazz) {
                        return new ClassSizeInfo(clazz);
                    }
                }).visit(obj, this);
                // classSizeInfos.get(clazz).visit(obj, this);
            }
        }
    }

    private void visitArray(Object array) {
        final Class<?> componentType = array.getClass().getComponentType();
        final int length = Array.getLength(array);
        if (componentType.isPrimitive()) {
            increaseByArraySize(length, getPrimitiveFieldSize(componentType));
        } else {
            increaseByArraySize(length, referenceSize);
            // If we didn't use an ArrayElementsVisitor, we would be enqueueing every
            // element of the array here instead. For large arrays, it would
            // tremendously enlarge the queue. In essence, we're compressing it into
            // a small command object instead. This is different than immediately
            // visiting the elements, as their visiting is scheduled for the end of
            // the current queue.
            switch (length) {
                case 0: {
                    break;
                }
                case 1: {
                    enqueue(Array.get(array, 0));
                    break;
                }
                default: {
                    enqueue(new ArrayElementsVisitor((Object[]) array));
                }
            }
        }
    }

    private void increaseByArraySize(int length, long elementSize) {
        increaseSize(roundTo(arrayHeaderSize + length * elementSize, objectPadding));
    }

    private static class ArrayElementsVisitor {
        private final Object[] array;

        ArrayElementsVisitor(Object[] array) {
            this.array = array;
        }

        public void visit(ObjectSizeCalculator calc) {
            for (Object elem : array) {
                if (elem != null) {
                    calc.visit(elem);
                }
            }
        }
    }

    void enqueue(Object obj) {
        if (obj != null) {
            pending.addLast(obj);
        }
    }

    void increaseSize(long objectSize) {
        size += objectSize;
    }

    static long roundTo(long x, int multiple) {
        return ((x + multiple - 1) / multiple) * multiple;
    }

    private class ClassSizeInfo {
        // Padded fields + header size
        private final long objectSize;
        // Only the fields size - used to calculate the subclasses' memory
        // footprint.
        private final long fieldsSize;
        private final Field[] referenceFields;

        public ClassSizeInfo(Class<?> clazz) {
            long fieldsSize = 0;
            final List<Field> referenceFields = new LinkedList<Field>();
            for (Field f : Reflects.getAllDeclaredFields(clazz)) {
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                final Class<?> type = f.getType();
                if (type.isPrimitive()) {
                    fieldsSize += getPrimitiveFieldSize(type);
                } else {
                    Reflects.makeAccessible(f);
                    referenceFields.add(f);
                    fieldsSize += referenceSize;
                }
            }
            final Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
               // final ClassSizeInfo superClassInfo = classSizeInfos.get(superClass);
                final ClassSizeInfo superClassInfo = Maps.putIfAbsent(classSizeInfos, superClass, new Supplier<Class<?>, ClassSizeInfo>() {
                    @Override
                    public ClassSizeInfo get(Class<?> clazz) {
                        return new ClassSizeInfo(clazz);
                    }
                });
                fieldsSize += roundTo(superClassInfo.fieldsSize, superclassFieldPadding);
                referenceFields.addAll(Arrays.asList(superClassInfo.referenceFields));
            }
            this.fieldsSize = fieldsSize;
            this.objectSize = roundTo(objectHeaderSize + fieldsSize, objectPadding);
            this.referenceFields = referenceFields.toArray(new Field[0]);
        }

        void visit(Object obj, ObjectSizeCalculator calc) {
            calc.increaseSize(objectSize);
            enqueueReferencedObjects(obj, calc);
        }

        public void enqueueReferencedObjects(Object obj, ObjectSizeCalculator calc) {
            for (Field f : referenceFields) {
                try {
                    calc.enqueue(f.get(obj));
                } catch (IllegalAccessException e) {
                    final AssertionError ae = new AssertionError("Unexpected denial of access to " + f);
                    throw ae;
                }
            }
        }
    }

    private static long getPrimitiveFieldSize(Class<?> type) {
        return Primitives.sizeOf(type);
    }

    static MemoryLayoutSpecification getEffectiveMemoryLayoutSpecification() {
        final String vmName = System.getProperty("java.vm.name");
        if (vmName == null || !(vmName.startsWith("Java HotSpot(TM) ") || vmName.startsWith("OpenJDK") || vmName.startsWith("TwitterJDK"))) {
            throw new UnsupportedOperationException("ObjectSizeCalculator only supported on HotSpot VM");
        }

        final String dataModel = System.getProperty("sun.arch.data.model");
        if ("32".equals(dataModel)) {
            // Running with 32-bit data model
            return new Arch32MemoryLayoutSpecification();
        } else if (!"64".equals(dataModel)) {
            throw new UnsupportedOperationException("Unrecognized value '" + dataModel + "' of sun.arch.data.model system property");
        }

        final String strVmVersion = System.getProperty("java.vm.version");
        final int vmVersion = Integer.parseInt(strVmVersion.substring(0, strVmVersion.indexOf('.')));
        if (vmVersion >= 17) {
            long maxMemory = 0;
            for (MemoryPoolMXBean mp : ManagementFactory.getMemoryPoolMXBeans()) {
                maxMemory += mp.getUsage().getMax();
            }
            if (maxMemory < 30L * 1024 * 1024 * 1024) {
                // HotSpot 17.0 and above use compressed OOPs below 30GB of RAM total
                // for all memory pools (yes, including code cache).
                return new Arch64CompressedMemoryLayoutSpecified();
            }
        }

        // In other cases, it's a 64-bit uncompressed OOPs object model
        return new Arch64UncompressedMemoryLayoutSpecification();
    }
}