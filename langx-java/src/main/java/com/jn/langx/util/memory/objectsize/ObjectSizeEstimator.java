package com.jn.langx.util.memory.objectsize;


import com.jn.langx.util.*;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.collection.OpenHashSet;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;
import com.jn.langx.util.random.IRandom;
import com.jn.langx.util.reflect.Modifiers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.reference.ReferenceType;
import com.jn.langx.util.reflect.type.Primitives;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Stack;

/**
 * Estimates the sizes of Java objects (number of bytes of memory they occupy), for use in
 * memory-aware caches.
 * <p>
 * Based on the following JavaWorld article:
 * https://www.infoworld.com/article/2077408/sizeof-for-java.html
 */
class ObjectSizeEstimator {
    private ObjectSizeEstimator() {

    }

    // Alignment boundary for objects
    // Is this arch dependent ?
    private static final int ALIGN_SIZE = 8;

    // Estimate the size of arrays larger than ARRAY_SIZE_FOR_SAMPLING by sampling.
    private static final int ARRAY_SIZE_FOR_SAMPLING = 400;
    private static final int ARRAY_SAMPLE_SIZE = 100; // should be lower than ARRAY_SIZE_FOR_SAMPLING

    private static class ClassInfo {
        final long shellSize;
        final List<Field> pointerFields;

        public ClassInfo(final long shellSize, final List<Field> pointerFields) {
            this.shellSize = shellSize;
            this.pointerFields = pointerFields;
        }
    }

    static class SearchState {
        private final IdentityHashMap<Object, Object> visited;
        private final Stack<Object> stack;
        private long size;

        public IdentityHashMap<Object, Object> visited() {
            return this.visited;
        }

        public Stack<Object> stack() {
            return this.stack;
        }

        public long size() {
            return this.size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public void enqueue(final Object obj) {
            if (obj != null && !this.visited().containsKey(obj)) {
                this.visited().put(obj, (Object) null);
                this.stack().push(obj);
            }

        }

        public boolean isFinished() {
            return this.stack().isEmpty();
        }

        public Object dequeue() {
            if (!this.stack.isEmpty()) {
                return this.stack.pop();
            }
            return null;
        }

        public SearchState(final IdentityHashMap<Object, Object> visited) {
            this.visited = visited;
            this.stack = new Stack<Object>();
            this.size = 0L;
        }
    }

    private static final int[] fieldSizes = new int[]{8, 4, 2, 1};
    // A cache of ClassInfo objects for each class
    // We use weakKeys to allow GC of dynamically created classes
    private static final ConcurrentReferenceHashMap<Class, ClassInfo> classInfos = new ConcurrentReferenceHashMap(100, ReferenceType.WEAK, ReferenceType.SOFT);

    private static boolean isCompressedOops = false;
    private static int pointerSize = 4;

    // Minimum size of a java.lang.Object
    private static int objectSize = 8;

    /**
     * Estimate the number of bytes that the given object takes up on the JVM heap. The estimate
     * includes space taken up by objects referenced by the given object, their references, and so on
     * and so forth.
     * <p>
     * This is useful for determining the amount of heap space a broadcast variable will occupy on
     * each executor or the amount of space each object will take when caching objects in
     * deserialized form. This is not the same as the serialized size of the object, which will
     * typically be much smaller.
     */
    public static long estimate(final Object obj) {
        return estimate(obj, new IdentityHashMap());
    }


    // Sets object size, pointer size based on architecture and CompressedOops settings
    // from the JVM.
    private static void initialize() {
        MemoryLayoutSpecification memoryLayout = ObjectSizeCalculator.CurrentLayout.SPEC;
        objectSize = memoryLayout.getObjectHeaderSize();
        pointerSize = memoryLayout.getReferenceSize();
        classInfos.clear();
        classInfos.put(Object.class, new ClassInfo(objectSize, Collects.<Field>immutableList()));

    }

    // Size of an object reference
    // Based on https://wikis.oracle.com/display/HotSpotInternals/CompressedOops


    private static long estimate(Object obj, IdentityHashMap visited) {
        SearchState state = new SearchState(visited);
        state.enqueue(obj);
        while (!state.isFinished()) {
            visitSingleObject(state.dequeue(), state);
        }
        return state.size;
    }

    private static void visitSingleObject(Object obj, SearchState state) throws RuntimeException {
        Class cls = obj.getClass();
        if (cls.isArray()) {
            visitArray(obj, cls, state);
        } else if (cls.getName().startsWith("scala.reflect")) {
            // Many objects in the scala.reflect package reference global reflection objects which, in
            // turn, reference many other large global objects. Do nothing in this case.
        } else if ((obj instanceof ClassLoader) || (obj instanceof Class)) {
            // Hadoop JobConfs created in the interpreter have a ClassLoader, which greatly confuses
            // the size estimator since it references the whole REPL. Do nothing in this case. In
            // general all ClassLoaders and Classes will be shared between objects anyway.
        } else {
            ClassInfo classInfo = getClassInfo(cls);
            state.size += alignSize(classInfo.shellSize);
            for (Field field : classInfo.pointerFields) {
                try {
                    state.enqueue(Reflects.getFieldValue(field, obj,true,true));
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Get or compute the ClassInfo for a given class.
     */
    private static ClassInfo getClassInfo(Class cls) {
        // Check whether we've already cached a ClassInfo for this class
        ClassInfo info = classInfos.get(cls);
        if (info != null) {
            return info;
        }

        ClassInfo parent = getClassInfo(cls.getSuperclass());
        long shellSize = parent.shellSize;
        List<Field> pointerFields = Lists.newArrayList(parent.pointerFields);
        int[] sizeCount = new int[Maths.max(fieldSizes) + 1];

        // iterate through the fields of this class and gather information.
        for (Field field : cls.getDeclaredFields()) {
            if (!Modifiers.isStatic(field)) {
                Class fieldClass = field.getType();
                if (fieldClass.isPrimitive()) {
                    int size = Primitives.sizeOf(fieldClass);
                    sizeCount[size]= sizeCount[size] + 1;
                } else {
                    ObjectSizeCalculator.addReferenceFiled(ObjectSizeEstimator.class, pointerFields, field);
                    sizeCount[pointerSize]=sizeCount[pointerSize]+ 1;
                }
            }
        }
        // Based on the simulated field layout code in Aleksey Shipilev's report:
        // http://cr.openjdk.java.net/~shade/papers/2013-shipilev-fieldlayout-latest.pdf
        // The code is in Figure 9.
        // The simplified idea of field layout consists of 4 parts (see more details in the report):
        //
        // 1. field alignment: HotSpot lays out the fields aligned by their size.
        // 2. object alignment: HotSpot rounds instance size up to 8 bytes
        // 3. consistent fields layouts throughout the hierarchy: This means we should layout
        // superclass first. And we can use superclass's shellSize as a starting point to layout the
        // other fields in this class.
        // 4. class alignment: HotSpot rounds field blocks up to HeapOopSize not 4 bytes, confirmed
        // with Aleksey. see https://bugs.openjdk.java.net/browse/CODETOOLS-7901322
        //
        // The real world field layout is much more complicated. There are three kinds of fields
        // order in Java 8. And we don't consider the @contended annotation introduced by Java 8.
        // see the HotSpot classloader code, layout_fields method for more details.
        // hg.openjdk.java.net/jdk8/jdk8/hotspot/file/tip/src/share/vm/classfile/classFileParser.cpp
        long alignedSize = shellSize;
        for (int size : fieldSizes) {
            if (sizeCount[size]> 0) {
                long count = sizeCount[size];
                alignedSize = Maths.maxLong(alignedSize, alignSizeUp(shellSize, size) + size * count);
                shellSize += size * count;
            }
        }

        // Should choose a larger size to be new shellSize and clearly alignedSize >= shellSize, and
        // round up the instance filed blocks
        shellSize = alignSizeUp(alignedSize, pointerSize);

        // Create and cache a new ClassInfo
        ClassInfo newInfo = new ClassInfo(shellSize, pointerFields);
        classInfos.put(cls, newInfo);
        return newInfo;
    }

    private static long alignSize(long size) {
        return alignSizeUp(size, ALIGN_SIZE);
    }

    /**
     * Compute aligned size. The alignSize must be 2^n, otherwise the result will be wrong.
     * When alignSize = 2^n, alignSize - 1 = 2^n - 1. The binary representation of (alignSize - 1)
     * will only have n trailing 1s(0b00...001..1). ~(alignSize - 1) will be 0b11..110..0. Hence,
     * (size + alignSize - 1) & ~(alignSize - 1) will set the last n bits to zeros, which leads to
     * multiple of alignSize.
     */
    private static long alignSizeUp(long size, int alignSize) {
        return (size + alignSize - 1) & ~(alignSize - 1);
    }


    private static void visitArray(Object array, Class arrayClass, SearchState state) {
        int length = Array.getLength(array);
        Class elementClass = arrayClass.getComponentType();

        // Arrays have object header and length field which is an integer
        long arrSize = alignSize(objectSize + Primitives.sizeOf(Integer.TYPE));

        if (elementClass.isPrimitive()) {
            arrSize += alignSize((long) length * Primitives.sizeOf(elementClass));
            state.size += arrSize;
        } else {
            arrSize += alignSize((long) length * pointerSize);
            state.size += arrSize;

            if (length <= ARRAY_SIZE_FOR_SAMPLING) {
                int arrayIndex = 0;
                while (arrayIndex < length) {
                    state.enqueue(getArrayElementByIndex(array, arrayIndex));
                    arrayIndex += 1;
                }
            } else {
                // Estimate the size of a large array by sampling elements without replacement.
                // To exclude the shared objects that the array elements may link, sample twice
                // and use the min one to calculate array size.
                IRandom rand = GlobalThreadLocalMap.getRandom();
                OpenHashSet<Integer> drawn = new OpenHashSet<Integer>(2 * ARRAY_SAMPLE_SIZE,Integer.class);
                long s1 = sampleArray(array, state, rand, drawn, length);
                long s2 = sampleArray(array, state, rand, drawn, length);
                long size = Math.min(s1, s2);
                state.size += Math.max(s1, s2) + (size * ((length - ARRAY_SAMPLE_SIZE) / ARRAY_SAMPLE_SIZE));
            }
        }
    }

    private static long sampleArray(
            Object array,
            SearchState state,
            IRandom rand,
            OpenHashSet<Integer> drawn,
            int length) {
        long size = 0L;
        for (int i = 0; i < ARRAY_SAMPLE_SIZE; i++) {
            int index = 0;
            do {
                index = rand.nextInt(length);
            } while (drawn.contains(index));
            drawn.add(index);
            Object obj = getArrayElementByIndex(array, index);
            if (obj != null) {
                size += ObjectSizeEstimator.estimate(obj, state.visited);
            }
        }
        return size;
    }

    private static Object getArrayElementByIndex(Object xs, int idx) {
        if (xs == null) {
            throw new NullPointerException();
        }
        if (!xs.getClass().isArray()) {
            throw new IllegalArgumentException("not a array");
        }
        return Array.get(xs, idx);
    }


    static {
        initialize();
    }

}