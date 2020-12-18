package com.jn.langx.util.io.file;

import com.jn.langx.codec.Hex;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.properties.Props;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.io.IOs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class FileTypes {
    private static final Logger logger = LoggerFactory.getLogger(FileTypes.class);
    private static final MultiValueMap fileTypesMap = new LinkedMultiValueMap<String, String>();

    static {
        init();
    }

    private static final void init() {
        try {
            Resource r = Resources.loadClassPathResource("filetypes.properties", FileTypes.class);
            Properties properties = Props.load(r);
            Collects.forEach(Collects.propertiesToStringMap(properties), new Consumer2<String, String>() {
                @Override
                public void accept(String magicCode, String typesString) {
                    String[] types = Strings.split(typesString, ",");
                    addFileTypes(magicCode, Collects.asList(types));
                }
            });
        } catch (IOException ex) {
            logger.warn("Error occur when load filetypes.properties");
        }
    }


    public static final String getType(final String fileHexHeader) {
        Map.Entry entry = Collects.findFirst(fileTypesMap, new Predicate2<String, List<String>>() {
            @Override
            public boolean test(String magicCode, List<String> types) {
                return Strings.startsWith(fileHexHeader, magicCode, true);
            }
        });
        if (entry != null) {
            List<String> types = (List<String>) entry.getValue();
            if (Emptys.isNotEmpty(types)) {
                types.get(0);
            }
        }
        return null;
    }

    public static synchronized void addFileTypes(String magicCode, List<String> types) {
        fileTypesMap.addAll(magicCode, types);
    }

    public static synchronized void addFileType(String magicCode, String type) {
        fileTypesMap.add(magicCode, type);
    }

    public static String readFileMagic(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[28];
            int length = IOs.read(in, bytes);
            if (length > 0) {
                byte[] bs = new byte[length];
                return Hex.encodeHexString(bs, true);
            }
            return null;
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        } finally {
            IOs.close(in);
        }
    }

    public static String getFileType(File file) {
        String fileHexHeader = readFileMagic(file);
        String type = getType(fileHexHeader);
        if (Emptys.isEmpty(type)) {
            return Files.getSuffix(file);
        }
        return type;
    }


}
