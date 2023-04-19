package com.jn.langx.text.lexer;


import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;

import java.util.Arrays;

class MutableRandomAccessQueue<T> {
    private Object[] myArray;

    private int myFirst;

    private int myLast;

    private boolean isWrapped;

    MutableRandomAccessQueue(int initialCapacity) {
        this.myArray = (initialCapacity > 0) ? new Object[initialCapacity] : Emptys.EMPTY_OBJECTS;
    }

    void addLast(T object) {
        int currentSize = size();
        if (currentSize == this.myArray.length) {
            this.myArray = normalize(Math.max(currentSize * 3 / 2, 10));
            this.myFirst = 0;
            this.myLast = currentSize;
            this.isWrapped = false;
        }
        this.myArray[this.myLast] = object;
        this.myLast++;
        if (this.myLast == this.myArray.length) {
            this.isWrapped = !this.isWrapped;
            this.myLast = 0;
        }
    }

    void removeLast() {
        if (this.myLast == 0) {
            this.isWrapped = !this.isWrapped;
            this.myLast = this.myArray.length;
        }
        this.myLast--;
        this.myArray[this.myLast] = null;
    }

    private T getRaw(int last) {
        return (T) this.myArray[last];
    }

    boolean isEmpty() {
        return (size() == 0);
    }

    int size() {
        return this.isWrapped ? (this.myArray.length - this.myFirst + this.myLast) : (this.myLast - this.myFirst);
    }

    T pullFirst() {
        T result = peekFirst();
        this.myArray[this.myFirst] = null;
        this.myFirst++;
        if (this.myFirst == this.myArray.length) {
            this.myFirst = 0;
            this.isWrapped = !this.isWrapped;
        }
        return result;
    }

    T peekFirst() {
        if (isEmpty())
            throw new IndexOutOfBoundsException("queue is empty");
        return getRaw(this.myFirst);
    }

    private int copyFromTo(int first, int last, Object[] result, int destinationPos) {
        int length = last - first;
        System.arraycopy(this.myArray, first, result, destinationPos, length);
        return length;
    }

    private T[] normalize(int capacity) {
        T[] result = (T[]) new Object[capacity];
        return normalize(result);
    }

    private T[] normalize(T[] result) {
        Preconditions.checkNotNullArgument(result, "result");
        if (this.isWrapped) {
            int tailLength = copyFromTo(this.myFirst, this.myArray.length, result, 0);
            copyFromTo(0, this.myLast, result, tailLength);
        } else {
            copyFromTo(this.myFirst, this.myLast, result, 0);
        }
        return result;
    }

    void clear() {
        Arrays.fill(this.myArray, null);
        this.myFirst = this.myLast = 0;
        this.isWrapped = false;
    }

    T set(int index, T value) {
        int arrayIndex = this.myFirst + index;
        if (this.isWrapped && arrayIndex >= this.myArray.length) {
            arrayIndex -= this.myArray.length;
        }
        T old = getRaw(arrayIndex);
        this.myArray[arrayIndex] = value;
        return old;
    }

    T get(int index) {
        int arrayIndex = this.myFirst + index;
        if (this.isWrapped && arrayIndex >= this.myArray.length) {
            arrayIndex -= this.myArray.length;
        }
        return getRaw(arrayIndex);
    }
}