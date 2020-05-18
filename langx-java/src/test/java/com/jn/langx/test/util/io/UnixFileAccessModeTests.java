package com.jn.langx.test.util.io;

import com.jn.langx.util.io.file.UnixFileAccessMode;
import org.junit.Test;

public class UnixFileAccessModeTests {
    @Test
    public void test() {
        showMode(UnixFileAccessMode.create(509));
        showMode(UnixFileAccessMode.create(493));
    }

    private void showMode(UnixFileAccessMode accessMode){
        System.out.println(accessMode);
    }
}
