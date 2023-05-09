package com.jn.langx.test.util.io;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.io.file.FilePermission;
import com.jn.langx.util.io.file.UnixFileAccessMode;
import org.junit.Test;


public class UnixFileAccessModeTests {
    @Test
    public void test() {
        showMode(UnixFileAccessMode.create(509));
        showMode(UnixFileAccessMode.create(493));
        showMode(UnixFileAccessMode.create(0755));

        System.out.println(StringTemplates.formatWithPlaceholder("binaryMode:{}, octalMode:{}", FilePermission.toBinary(0755),FilePermission.toOctal(0755)));
    }

    private void showMode(UnixFileAccessMode accessMode){
        System.out.println(accessMode);
    }
}
