package com.jn.langx.util.memory.objectsize;

/**
 * HotSpot 17.0 and above use compressed OOPs below 30GB of RAM total
 * for all memory pools (yes, including code cache).
 */
public class Arch64CompressedMemoryLayoutSpecified implements MemoryLayoutSpecification {
    @Override
    public int getArrayHeaderSize() {
        return 16;
    }

    @Override
    public int getObjectHeaderSize() {
        return 12;
    }

    @Override
    public int getObjectPadding() {
        return 8;
    }

    @Override
    public int getReferenceSize() {
        return 4;
    }

    @Override
    public int getSuperclassFieldPadding() {
        return 4;
    }
}
