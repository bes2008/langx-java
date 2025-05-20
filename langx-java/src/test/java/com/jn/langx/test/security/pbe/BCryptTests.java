package com.jn.langx.test.security.pbe;

import com.jn.langx.security.crypto.pbe.pswdenc.bcrypt.BCrypt;

public class BCryptTests {
    public static void main(String[] args) {
        String password = "123456";
        String salt = BCrypt.gensalt();
        String hashpw = BCrypt.hashpw(password, salt);
        System.out.println(hashpw);
        System.out.println(BCrypt.checkpw(password, hashpw));
    }
}
