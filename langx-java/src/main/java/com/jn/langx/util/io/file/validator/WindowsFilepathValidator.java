package com.jn.langx.util.io.file.validator;

import com.jn.langx.text.StrTokenizer;
import com.jn.langx.util.Strings;
import com.jn.langx.util.io.file.OsFileSystem;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;


public class WindowsFilepathValidator extends AbstractFilepathValidator {


    private static final Regexp PARTITION = Regexps.compile("[A-Za-z]((\\w|\\$)+)?");


    @Override
    public boolean isLegalFilename(String name) {
        return OsFileSystem.WINDOWS.isLegalFileName(name);
    }

    @Override
    public boolean isLegalFilepath(String path) {
        if (Strings.isEmpty(path)) {
            return false;
        }
        String[] tmp = Strings.split(path, ":", false, false);
        if (tmp.length > 2 || tmp.length < 1) {
            return false;
        }
        String partition = null;
        String pathPart = null;
        if (tmp.length == 2) {
            partition = tmp[0];
            pathPart = tmp[1];
        } else {
            pathPart = tmp[0];
        }

        // 检验盘符
        if (partition != null) {
            /*
            在Windows操作系统中，分区名称（或称为卷标）有以下要求：

1. 分区名称的长度不能超过32个字符。

2. 分区名称不能包含以下字符：\ / : * ? " < > |。

3. 分区名称不能与操作系统保留的关键字相同，例如：CON","PRN","AUX","NUL","COM1","COM2","COM3","COM4","COM5","COM6","COM7","COM8","COM9","LPT1","LPT2","LPT3","LPT4","LPT5","LPT6","LPT7","LPT8","LPT9。

总之，在Windows中，分区名称必须符合上述要求，否则会导致无法创建分区或者无法访问分区。
             */
            while (Strings.startsWith(partition, "/")) {
                partition = Strings.substring(partition, 1);
            }

            if (partition.length() > 32) {
                return false;
            }

            if (!isLegalFilename(partition)) {
                return false;
            }
            if (OsFileSystem.WINDOWS.getReservedFileNames().contains(Strings.upperCase(partition))) {
                return false;
            }

            if (!Regexps.match(PARTITION, partition)) {
                return false;
            }
        }

        // 验证 pathPart
        if (Strings.isEmpty(pathPart)) {
            return false;
        }
        StrTokenizer tokenizer = new StrTokenizer(pathPart, false, OsFileSystem.WINDOWS.getPathSeparatorStrings());
        while (tokenizer.hasNext()) {
            String segment = tokenizer.next();
            if (!isLegalFilename(segment)) {
                return false;
            }
        }
        return true;
    }

    static final WindowsFilepathValidator INSTANCE = new WindowsFilepathValidator();
}
