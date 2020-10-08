package com.jn.langx.util.io;

import com.jn.langx.util.Emptys;

import java.io.*;

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
    public static byte[] serialize(Object obj) throws IOException {
        if (obj == null) {
            return Emptys.EMPTY_BYTES;
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

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ObjectInputStream input = null;
        try {
            ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
            input = new ObjectInputStream(bai);
            return input.readObject();
        } finally {
            IOs.close(input);
        }
    }
}
