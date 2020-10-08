package com.jn.langx.util.io;

import java.io.*;

/**
 * @since 2.10.2
 */
public class ObjectIOs {
    private ObjectIOs() {
    }

    /**
     * 使用 JDK 的 output stream 写对象。
     * 在序列化时，不会对 static 字段， transient 字段序列化
     *
     * @param obj 必须实现 Serializable 接口
     * @return
     * @throws IOException
     */
    public static <T> byte[] serialize(T obj) throws IOException {
        if (obj == null) {
            return null;
        }
        ObjectOutputStream output = null;
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            output = new ObjectOutputStream(bao);
            output.writeObject(obj);
            return bao.toByteArray();
        } finally {
            IOs.close(output);
        }
    }

    public static <T> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ObjectInputStream input = null;
        try {
            ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
            input = new ObjectInputStream(bai);
            return (T) input.readObject();
        } finally {
            IOs.close(input);
        }
    }
}
