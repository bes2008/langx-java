package com.jn.langx.io.stream;

import com.jn.langx.util.collection.Collects;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class RewindableInputStream extends FilterInputStream implements Rewindable {
    private final List<ByteBuffer> buffers = Collects.emptyArrayList();

    public RewindableInputStream(InputStream in) {
        super((in instanceof BufferedInputStream) ? in : new BufferedInputStream(in));
    }

    @Override
    public void rewind() {

    }


}
