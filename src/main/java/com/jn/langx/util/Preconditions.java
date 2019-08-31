/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

public class Preconditions {
    private Preconditions() {
        throw new UnsupportedOperationException();
    }

    public static <T> T checkNotNull(@NonNull T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static <T> T checkNotNull(@NonNull T obj, @Nullable String errorMessage) {
        if (obj == null) {
            if (errorMessage == null) {
                throw new NullPointerException();
            }
            throw new NullPointerException(errorMessage);
        }
        return obj;
    }

    public static <T> T checkNotEmpty(@Nullable T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static <T> T checkNotEmpty(@NonNull T obj, @Nullable String errorMessage) {
        if (Emptys.isEmpty(obj)) {
            if (errorMessage == null) {
                throw new NullPointerException();
            }
            throw new NullPointerException(errorMessage);
        }
        return obj;
    }

    public static void checkTrue(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }


    public static void checkTrue(boolean expression, String errorMessage) {
        if (!expression) {
            if (Emptys.isEmpty(errorMessage)) {
                throw new IllegalArgumentException();
            } else {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

}
