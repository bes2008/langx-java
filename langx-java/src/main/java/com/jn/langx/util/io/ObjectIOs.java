package com.jn.langx.util.io;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.io.stream.obj.SecureObjectInputStream;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.Reflects;

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
     */
    public static <T> byte[] serialize(T obj) throws IOException {
        if (obj == null) {
            return Emptys.EMPTY_BYTES;
        }
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            serialize(obj, bao);
            return bao.toByteArray();
        } finally {
            IOs.close(bao);
        }
    }

    /**
     * 序列化到指定的输出流
     */
    public static <T> void serialize(T obj, @NonNull OutputStream outputStream) throws IOException {
        if (obj == null) {
            return;
        }
        Preconditions.checkNotNull(outputStream, "the output stream is null");
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(outputStream);
            output.writeObject(obj);
            output.flush();
        } finally {
            IOs.close(output);
        }
    }

    public static <T> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        return deserialize(bytes, null);
    }

    public static <T> T deserialize(byte[] bytes, @Nullable Class<T> targetType) throws IOException, ClassNotFoundException {
        if (Emptys.isEmpty(bytes)) {
            return null;
        }
        ObjectInputStream input = null;
        try {
            ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
            input = new SecureObjectInputStream(bai);
            Object obj = input.readObject();
            if (obj == null) {
                return null;
            }
            if (targetType != null) {
                if (Reflects.isInstance(obj, targetType)) {
                    return (T) obj;
                }
                throw new ClassCastException(StringTemplates.formatWithPlaceholder("Class {} is not been cast to {}", Reflects.getFQNClassName(obj.getClass()), Reflects.getFQNClassName(targetType)));
            } else {
                return (T) obj;
            }
        } finally {
            IOs.close(input);
        }

    }
}
