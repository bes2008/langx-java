/*
 *
 *  Copyright 2018 FJN Corp.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Author                        Date                       Issue
 *  fs1194361820@163.com          2015-01-01                 Initial Version
 *
 */

package com.jn.langx.security;

import com.jn.langx.security.messagedigest.MessageDigests;

/**
 * @author fs1194361820@163.com
 * @see MessageDigests#md5(String)
 */
@Deprecated
public class MD5 {

    public static String getMD5(String srcMsg) {
        if (srcMsg == null) {
            throw new IllegalArgumentException("srcMsg is null.");
        }
        return MessageDigests.md5(srcMsg);
    }

}
