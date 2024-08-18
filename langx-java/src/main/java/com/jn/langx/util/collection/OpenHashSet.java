package com.jn.langx.util.collection;


import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.iter.UnmodifiableIterator;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.hash.Hashs;
import com.jn.langx.util.io.bytes.Bytes;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Primitives;

import java.lang.reflect.Array;
import java.util.BitSet;
import java.util.Iterator;

public class OpenHashSet<T> {

    private static final  int MAX_CAPACITY = 1 << 30;
    private static final int INVALID_POS = -1;
    private static final int NONEXISTENCE_MASK = 1 << 31;
    private static final int POSITION_MASK = (1 << 31) - 1;

    private int initialCapacity;
    private float loadFactor;

    private int capacity;
    private int mask;
    private int size;
    private int growThreshold;

    private BitSet bitset;

    private Class elementClass;

    private Hasher<T> hasher;

    // 这是个数组
    private Object data;

    public OpenHashSet(int initialCapacity, float loadFactor, Class<T> elementClass){
        Preconditions.checkArgument(initialCapacity <= OpenHashSet.MAX_CAPACITY,"Can't make capacity bigger than {} elements", MAX_CAPACITY);
        Preconditions.checkArgument(initialCapacity >= 0, "Invalid initial capacity");
        Preconditions.checkArgument(loadFactor < 1.0, "Load factor must be less than 1.0");
        Preconditions.checkArgument(loadFactor > 0.0, "Load factor must be greater than 0.0");

        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;

        Hasher hasher=null;
        this.elementClass = elementClass;
        if(Primitives.isLong(elementClass)){
            hasher = new LongHasher();
        }
        else if(Primitives.isFloat(elementClass)){
            hasher = new FloatHasher();
        }
        else if(Primitives.isInteger(elementClass)){
            hasher = new IntHasher();
        }else if(Primitives.isDouble(elementClass)){
            hasher=new DoubleHasher();
        }
        else{
            throw new IllegalArgumentException("illegal generic type argument: "+ Reflects.getFQNClassName(this.elementClass));
        }
        this.hasher=hasher;

        this.capacity = nextPowerOf2(initialCapacity);
        this.mask = this.capacity-1;
        this.size=0;
        this.bitset = new BitSet(this.capacity);
        this.data = Array.newInstance(this.elementClass,this.capacity);
    }

    public BitSet getBitset() {
        return bitset;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * Return true if this set contains the specified element.
     * */
    public boolean contains(T e){
        return getPos(e)!=INVALID_POS;
    }

    public int getPos(T k){
        int pos = hashcode(hasher.hash(k)) & this.mask;
        int delta= 1;
        while (true){
            if(!this.bitset.get(pos)){
                return INVALID_POS;
            }else if(keyExistsAtPos(k, pos)){
                return pos;
            }
            else{
                pos = (pos+delta) & mask;
                delta+=1;
            }
        }
    }



    private boolean keyExistsAtPos(T k, int pos){
        return Objs.equals(Array.get(this.data, pos), k);
    }

    private int hashcode(int h){
        return (int)Hashs.getHasher("murmur3_32",null).hash(Bytes.toBytes(h));
    }
    /**
     * Add an element to the set. If the set is over capacity after the insertion, grow the set
     * and rehash all elements.
     */
    public void add(T k) {
        addWithoutResize(k);
        rehashIfNeeded(k, Functions.<Integer>noopConsumer(), Functions.<Integer,Integer>noopConsumer2());
    }

    public OpenHashSet<T> union( OpenHashSet<T> other){
        Iterator<T> iter = other.iterator();
        while (iter.hasNext()) {
            add(iter.next());
        }
        return this;
    }

    public Iterator<T> iterator(){
        return new UnmodifiableIterator<T>() {
            int pos = nextPos(0);

            @Override
            public boolean hasNext() {
                return pos != INVALID_POS;
            }

            @Override
            public T next() {
                T tmp = getValue(pos);
                pos = nextPos(pos + 1);
                return tmp;
            }
        };

    }

    /**
     * Return the next position with an element stored, starting from the given position inclusively.
     */
    public int nextPos(int fromPos){
        return this.bitset.nextSetBit(fromPos);
    }


    /** Return the value at the specified position. */
    public T getValue(int pos){
        return (T)Array.get(this.data,pos);
    }

    /** Return the value at the specified position. */
    public T getValueSafe(int pos) {
        assert(this.bitset.get(pos));
        return (T)Array.get(this.data,pos);
    }

    private int nextPowerOf2(int n) {
        if (n == 0) {
            return 1;
        } else {
            int highBit = Integer.highestOneBit(n);
            if (highBit == n) {
                return n;
            } else {
                return highBit << 1;
            }
        }
    }


    /**
     * Add an element to the set. This one differs from add in that it doesn't trigger rehashing.
     * The caller is responsible for calling rehashIfNeeded.
     *
     * Use (retval & POSITION_MASK) to get the actual position, and
     * (retval & NONEXISTENCE_MASK) == 0 for prior existence.
     *
     * @return The position where the key is placed, plus the highest order bit is set if the key
     *         does not exist previously.
     */
    public int addWithoutResize(T k){
        int pos = hashcode(hasher.hash(k)) & mask;
        int delta = 1;
        while (true) {
            if (!this.bitset.get(pos)) {
                // This is a new key.
                Array.set(this.data,pos,k);
                this.bitset.set(pos);
                this.size+=1;
                return pos | NONEXISTENCE_MASK;
            } else if (keyExistsAtPos(k, pos)) {
                return pos;
            } else {
                // quadratic probing with values increase by 1, 2, 3, ...
                pos = (pos + delta) & mask;
                delta += 1;
            }
        }
    }

    /**
     * Rehash the set if it is overloaded.
     * @param k A parameter unused in the function, but to force the Scala compiler to specialize
     *          this method.
     * @param allocateFunc Callback invoked when we are allocating a new, larger array.
     * @param moveFunc Callback invoked when we move the key from one position (in the old data array)
     *                 to a new position (in the new data array).
     */
    public void rehashIfNeeded(T k, Consumer<Integer> allocateFunc, Consumer2<Integer,Integer> moveFunc){
        if (this.size > this.growThreshold) {
            rehash(k, allocateFunc, moveFunc);
        }
    }

    /**
     * Double the table's size and re-hash everything. We are not really using k, but it is declared
     * so Scala compiler can specialize this method (which leads to calling the specialized version
     * of putInto).
     *
     * @param k A parameter unused in the function, but to force the Scala compiler to specialize
     *          this method.
     * @param allocateFunc Callback invoked when we are allocating a new, larger array.
     * @param moveFunc Callback invoked when we move the key from one position (in the old data array)
     *                 to a new position (in the new data array).
     */
    private void rehash(T k, Consumer<Integer> allocateFunc, Consumer2<Integer,Integer> moveFunc){
        int newCapacity = this.capacity * 2;
        Preconditions.checkArgument(newCapacity > 0 && newCapacity <= OpenHashSet.MAX_CAPACITY, "Can't contain more than ${(loadFactor * OpenHashSet.MAX_CAPACITY).toInt} elements");
        allocateFunc.accept(newCapacity);
        BitSet newBitset = new BitSet(newCapacity);
        Object newData = Array.newInstance(this.elementClass,newCapacity);
        int newMask = newCapacity - 1;

        int oldPos = 0;
        while (oldPos < capacity) {
            if (this.bitset.get(oldPos)) {
                T key = this.getValue(oldPos);
                int newPos = hashcode(hasher.hash(key)) & newMask;
                int i = 1;
                boolean keepGoing = true;
                // No need to check for equality here when we insert so this has one less if branch than
                // the similar code path in addWithoutResize.
                while (keepGoing) {
                    if (!newBitset.get(newPos)) {
                        // Inserting the key at newPos
                        Array.set(newData,newPos,key);
                        newBitset.set(newPos);
                        moveFunc.accept(oldPos, newPos);
                        keepGoing = false;
                    } else {
                        int delta = i;
                        newPos = (newPos + delta) & newMask;
                        i += 1;
                    }
                }
            }
            oldPos += 1;
        }

        this.bitset = newBitset;
        this.data = newData;
        this.capacity = newCapacity;
        this.mask = newMask;
        this.growThreshold =(int) (loadFactor * newCapacity);
    }


    public OpenHashSet(int initialCapacity, Class<T> elementClass){
        this(initialCapacity,0.7f, elementClass);
    }

    public OpenHashSet(Class<T> elementClass){
        this(64, elementClass);
    }

    public static interface Hasher<T> {
        public int hash(T o);
    }

    public static class IntHasher implements Hasher<Integer> {
        @Override
        public int hash(Integer o) {
            return o;
        }
    }
    public static class FloatHasher implements Hasher<Float>{
        @Override
        public int hash(Float o) {
            return Float.floatToIntBits(o);
        }
    }

    public static class DoubleHasher implements Hasher<Double>{
        @Override
        public int hash(Double o) {
            long bits = Double.doubleToLongBits(o);
            return (int)(bits ^ (bits >>> 32));
        }
    }

    public static class LongHasher implements Hasher<Long>{
        @Override
        public int hash(Long o) {
            return (int)(o ^ (o >>> 32));
        }
    }

    public static class DefaultHasher<T> implements Hasher<T>{
        @Override
        public int hash(T o) {
            return o.hashCode();
        }
    }

}
