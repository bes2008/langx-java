package com.jn.langx.util.io.file.validator;

import com.jn.langx.util.Strings;
import com.jn.langx.util.os.Platform;

public class FilepathValidators {

    public static boolean validateName(String path) {
        return validateName(path, Platform.isWindows);
    }

    public static boolean validateName(String path, String osFamily) {
        return validateName(path, Strings.contains(osFamily, "win"));
    }

    private static boolean validateName(String path, boolean windows) {
        if (windows) {
            return WindowsFilepathValidator.INSTANCE.isLegalFilename(path);
        } else {
            return UnixFilepathValidator.INSTANCE.isLegalFilename(path);
        }
    }


    public static boolean validatePath(String path) {
        return validatePath(path, Platform.isWindows);
    }

    public static boolean validatePath(String path, String osFamily) {
        return validatePath(path, Strings.contains(osFamily, "win"));
    }

    private static boolean validatePath(String path, boolean windows) {
        if (windows) {
            return WindowsFilepathValidator.INSTANCE.isLegalFilepath(path);
        } else {
            return UnixFilepathValidator.INSTANCE.isLegalFilepath(path);
        }
    }

    private FilepathValidators(){}
}
