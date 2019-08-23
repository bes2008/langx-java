package com.jn.langx.util.io.file;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

public class Filenames {
    /**
     * 文件名不能包含【\/:*?">|<】之一。
     * \\\\ 表示\
     * /	表示/
     * :	表示:
     * \\*	表示*
     * \\?	表示?
     * \"	表示"
     * >	表示>
     * <	表示<
     * \\|	表示|
     * <p>
     * 把这些字符排除就行了。
     */
    public static boolean checkFileName(String filename) {
        return filename.matches("^[^\\\\/:\\*\\?\">\\|<]+(\\.[^\\\\/:\\*\\?\">\\|<]+)?$");
    }

    public static String getSuffix(String filename) {
        Preconditions.checkTrue(Strings.isNotBlank(filename));
        Preconditions.checkTrue(!filename.endsWith("."));
        int index = filename.lastIndexOf(".");
        return filename.substring(index + 1);
    }

    public static String getLowCaseSuffix(String filename) {
        return getSuffix(filename).toLowerCase();
    }


}
