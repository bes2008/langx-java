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

package com.jn.langx.management;

import com.jn.langx.util.struct.Entry;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public interface MBeanService {
    boolean isServiceMatch();

    List<Entry<String, Object>> getMBeanAttrs(final Hashtable<String, String> p0, final Collection<String> p1);
}
