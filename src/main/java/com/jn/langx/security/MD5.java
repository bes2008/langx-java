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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author fs1194361820@163.com
 */
public class MD5 {
    private static MessageDigest md5MsgDigest;

    static {
        try {
            md5MsgDigest = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String byte2hex(byte[] b) {
        StringBuilder hex = new StringBuilder();
        for (int n = 0; n < b.length; n++) {
            String stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hex.append("0");
            }
            hex.append(stmp);
        }
        return hex.toString().toUpperCase();
    }

    public static String getMD5(String srcMsg) {
        if (srcMsg == null) {
            throw new IllegalArgumentException("srcMsg is null.");
        }
        md5MsgDigest.update(srcMsg.getBytes());
        byte[] md5Bytes = md5MsgDigest.digest();
        return byte2hex(md5Bytes);
    }

    public static void main(String[] args) {
        System.out.println(MD5.getMD5("hello"));
        System.out.println(MD5.getMD5("world"));
    }
}
