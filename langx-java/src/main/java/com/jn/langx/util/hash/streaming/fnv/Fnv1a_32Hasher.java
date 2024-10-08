/*
 * Copyright (C) 2014-2016 Markus Junginger, greenrobot (http://greenrobot.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.langx.util.hash.streaming.fnv;


import com.jn.langx.util.hash.AbstractHasher;
import com.jn.langx.util.hash.streaming.AbstractStreamingHasher;

/**
 * Hash function FNV-1a (http://www.isthe.com/chongo/tech/comp/fnv).
 *
 * @since 4.4.0
 */
public class Fnv1a_32Hasher extends AbstractStreamingHasher {
    private static final int INITIAL_VALUE = 0x811C9DC5;
    private static final int MULTIPLIER = 16777619;

    private int hash = INITIAL_VALUE;

    @Override
    public void update(byte b) {
        hash ^= 0xff & b;
        hash *= MULTIPLIER;
    }

    @Override
    public long getHash() {
        long h = hash & 0xffffffffL;
        reset();
        return h;
    }

    @Override
    public void reset() {
        hash = INITIAL_VALUE;
    }

    @Override
    protected AbstractHasher createInstance(Object initParam) {
        return new Fnv1a_32Hasher();
    }
}
