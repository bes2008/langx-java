package com.jn.langx.asm.reflect.accessor;


import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.Reflects;
import org.objectweb.asm.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class FieldAccessor {
    private String[] fieldNames;
    private Class[] fieldTypes;
    private Field[] fields;

    public int getIndex(String fieldName) {
        for (int i = 0, n = fieldNames.length; i < n; i++) {
            if (fieldNames[i].equals(fieldName)) return i;
        }
        throw new IllegalArgumentException("Unable to find non-private field: " + fieldName);
    }

    public int getIndex(Field field) {
        for (int i = 0, n = fields.length; i < n; i++) {
            if (fields[i].equals(field)) return i;
        }
        throw new IllegalArgumentException("Unable to find non-private field: " + field);
    }

    public void set(Object instance, String fieldName, Object value) {
        set(instance, getIndex(fieldName), value);
    }

    public Object get(Object instance, String fieldName) {
        return get(instance, getIndex(fieldName));
    }

    public List<String> getFieldNames() {
        return Collects.newArrayList(fieldNames);
    }

    public List<Class> getFieldTypes() {
        return Collects.newArrayList(fieldTypes);
    }

    public int getFieldCount() {
        return fieldTypes.length;
    }

    public List<Field> getFields() {
        return Collects.newArrayList(fields);
    }

    public void setFields(Field[] fields) {
        this.fields = Arrs.copy(fields);
    }

    public abstract void set(Object instance, int fieldIndex, Object value);

    public abstract void setBoolean(Object instance, int fieldIndex, boolean value);

    public abstract void setByte(Object instance, int fieldIndex, byte value);

    public abstract void setShort(Object instance, int fieldIndex, short value);

    public abstract void setInt(Object instance, int fieldIndex, int value);

    public abstract void setLong(Object instance, int fieldIndex, long value);

    public abstract void setDouble(Object instance, int fieldIndex, double value);

    public abstract void setFloat(Object instance, int fieldIndex, float value);

    public abstract void setChar(Object instance, int fieldIndex, char value);

    public abstract Object get(Object instance, int fieldIndex);

    public abstract String getString(Object instance, int fieldIndex);

    public abstract char getChar(Object instance, int fieldIndex);

    public abstract boolean getBoolean(Object instance, int fieldIndex);

    public abstract byte getByte(Object instance, int fieldIndex);

    public abstract short getShort(Object instance, int fieldIndex);

    public abstract int getInt(Object instance, int fieldIndex);

    public abstract long getLong(Object instance, int fieldIndex);

    public abstract double getDouble(Object instance, int fieldIndex);

    public abstract float getFloat(Object instance, int fieldIndex);

    /**
     * @param type Must not be the Object class, an interface, a primitive type, or void.
     */
    public static FieldAccessor get(Class type) {
        if (type.getSuperclass() == null)
            throw new IllegalArgumentException("The type must not be the Object class, an interface, a primitive type, or void.");

        ArrayList<Field> fields = new ArrayList<Field>();
        Class nextClass = type;
        while (nextClass != Object.class) {
            List<Field> declaredFields = Collects.asList(Reflects.getAllDeclaredFields( nextClass));
            for (int i = 0, n = declaredFields.size(); i < n; i++) {
                Field field = declaredFields.get(i);
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) continue;
                if (Modifier.isPrivate(modifiers)) continue;
                fields.add(field);
            }
            nextClass = nextClass.getSuperclass();
        }

        String[] fieldNames = new String[fields.size()];
        Class[] fieldTypes = new Class[fields.size()];
        for (int i = 0, n = fieldNames.length; i < n; i++) {
            fieldNames[i] = fields.get(i).getName();
            fieldTypes[i] = fields.get(i).getType();
        }

        String className = type.getName();
        String accessClassName = className + "FieldAccessor";
        if (accessClassName.startsWith("java.")) accessClassName = "langxreflectasm." + accessClassName;

        Class accessClass;
        AccessorClassLoader loader = AccessorClassLoader.get(type);
        synchronized (loader) {
            accessClass = loader.loadAccessClass(accessClassName);
            if (accessClass == null) {
                String accessClassNameInternal = accessClassName.replace('.', '/');
                String classNameInternal = className.replace('.', '/');

                ClassWriter cw = new ClassWriter(0);
                cw.visit(Opcodes.V1_1, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, accessClassNameInternal, null, Reflects.getFQNClassName(FieldAccessor.class).replace('.', '/'),
                        null);
                insertConstructor(cw);
                insertGetObject(cw, classNameInternal, fields);
                insertSetObject(cw, classNameInternal, fields);
                insertGetPrimitive(cw, classNameInternal, fields, Type.BOOLEAN_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.BOOLEAN_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.BYTE_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.BYTE_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.SHORT_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.SHORT_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.INT_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.INT_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.LONG_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.LONG_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.DOUBLE_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.DOUBLE_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.FLOAT_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.FLOAT_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.CHAR_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.CHAR_TYPE);
                insertGetString(cw, classNameInternal, fields);
                cw.visitEnd();
                accessClass = loader.defineAccessClass(accessClassName, cw.toByteArray());
            }
        }
        try {
            FieldAccessor access = (FieldAccessor) accessClass.newInstance();
            access.fieldNames = fieldNames;
            access.fieldTypes = fieldTypes;
            access.fields = fields.toArray(new Field[fields.size()]);
            return access;
        } catch (Throwable t) {
            throw new RuntimeException("Error constructing field access class: " + accessClassName, t);
        }
    }

    private static void insertConstructor(ClassWriter cw) {
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Reflects.getFQNClassName(FieldAccessor.class).replace('.', '/'), "<init>", "()V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private static void insertSetObject(ClassWriter cw, String classNameInternal, ArrayList<Field> fields) {
        int maxStack = 6;
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "set", "(Ljava/lang/Object;ILjava/lang/Object;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ILOAD, 2);

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            for (int i = 0, n = labels.length; i < n; i++)
                labels[i] = new Label();
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                Field field = fields.get(i);
                Type fieldType = Type.getType(field.getType());

                mv.visitLabel(labels[i]);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                mv.visitVarInsn(Opcodes.ALOAD, 3);

                switch (fieldType.getSort()) {
                    case Type.BOOLEAN:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Boolean");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
                        break;
                    case Type.BYTE:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Byte");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B");
                        break;
                    case Type.CHAR:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Character");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C");
                        break;
                    case Type.SHORT:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Short");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S");
                        break;
                    case Type.INT:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Integer");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
                        break;
                    case Type.FLOAT:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Float");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F");
                        break;
                    case Type.LONG:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Long");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J");
                        break;
                    case Type.DOUBLE:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Double");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
                        break;
                    case Type.ARRAY:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, fieldType.getDescriptor());
                        break;
                    case Type.OBJECT:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, fieldType.getInternalName());
                        break;
                    default:
                        break;
                }

                mv.visitFieldInsn(Opcodes.PUTFIELD, field.getDeclaringClass().getName().replace('.', '/'), field.getName(),
                        fieldType.getDescriptor());
                mv.visitInsn(Opcodes.RETURN);
            }

            mv.visitLabel(defaultLabel);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 4);
        mv.visitEnd();
    }

    private static void insertGetObject(ClassWriter cw, String classNameInternal, ArrayList<Field> fields) {
        int maxStack = 6;
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "get", "(Ljava/lang/Object;I)Ljava/lang/Object;", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ILOAD, 2);

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            for (int i = 0, n = labels.length; i < n; i++)
                labels[i] = new Label();
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                Field field = fields.get(i);

                mv.visitLabel(labels[i]);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                mv.visitFieldInsn(Opcodes.GETFIELD, field.getDeclaringClass().getName().replace('.', '/'), field.getName(),
                        Type.getDescriptor(field.getType()));

                Type fieldType = Type.getType(field.getType());
                switch (fieldType.getSort()) {
                    case Type.BOOLEAN:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
                        break;
                    case Type.BYTE:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
                        break;
                    case Type.CHAR:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
                        break;
                    case Type.SHORT:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
                        break;
                    case Type.INT:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                        break;
                    case Type.FLOAT:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
                        break;
                    case Type.LONG:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
                        break;
                    case Type.DOUBLE:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
                        break;
                    default:
                        break;
                }

                mv.visitInsn(Opcodes.ARETURN);
            }

            mv.visitLabel(defaultLabel);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    private static void insertGetString(ClassWriter cw, String classNameInternal, ArrayList<Field> fields) {
        int maxStack = 6;
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getString", "(Ljava/lang/Object;I)Ljava/lang/String;", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ILOAD, 2);

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            for (int i = 0, n = labels.length; i < n; i++) {
                if (fields.get(i).getType().equals(String.class))
                    labels[i] = new Label();
                else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                if (!labels[i].equals(labelForInvalidTypes)) {
                    Field field = fields.get(i);
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                    mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                    mv.visitFieldInsn(Opcodes.GETFIELD, field.getDeclaringClass().getName().replace('.', '/'), field.getName(),
                            "Ljava/lang/String;");
                    mv.visitInsn(Opcodes.ARETURN);
                }
            }
            // Rest of fields: different type
            if (hasAnyBadTypeLabel) {
                mv.visitLabel(labelForInvalidTypes);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                insertThrowExceptionForFieldType(mv, "String");
            }
            // Default: field not found
            mv.visitLabel(defaultLabel);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    private static void insertSetPrimitive(ClassWriter cw, String classNameInternal, ArrayList<Field> fields,
                                           Type primitiveType) {
        int maxStack = 6;
        int maxLocals = 4; // See correction below for LLOAD and DLOAD
        final String setterMethodName;
        final String typeNameInternal = primitiveType.getDescriptor();
        final int loadValueInstruction;
        switch (primitiveType.getSort()) {
            case Type.BOOLEAN:
                setterMethodName = "setBoolean";
                loadValueInstruction = Opcodes.ILOAD;
                break;
            case Type.BYTE:
                setterMethodName = "setByte";
                loadValueInstruction = Opcodes.ILOAD;
                break;
            case Type.CHAR:
                setterMethodName = "setChar";
                loadValueInstruction = Opcodes.ILOAD;
                break;
            case Type.SHORT:
                setterMethodName = "setShort";
                loadValueInstruction = Opcodes.ILOAD;
                break;
            case Type.INT:
                setterMethodName = "setInt";
                loadValueInstruction = Opcodes.ILOAD;
                break;
            case Type.FLOAT:
                setterMethodName = "setFloat";
                loadValueInstruction = Opcodes.FLOAD;
                break;
            case Type.LONG:
                setterMethodName = "setLong";
                loadValueInstruction = Opcodes.LLOAD;
                maxLocals++; // (LLOAD and DLOAD actually load two slots)
                break;
            case Type.DOUBLE:
                setterMethodName = "setDouble";
                loadValueInstruction = Opcodes.DLOAD;
                maxLocals++; // (LLOAD and DLOAD actually load two slots)
                break;
            default:
                setterMethodName = "set";
                loadValueInstruction = Opcodes.ALOAD;
                break;
        }
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, setterMethodName, "(Ljava/lang/Object;I" + typeNameInternal + ")V", null,
                null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ILOAD, 2);

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            for (int i = 0, n = labels.length; i < n; i++) {
                if (Type.getType(fields.get(i).getType()).equals(primitiveType))
                    labels[i] = new Label();
                else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                if (!labels[i].equals(labelForInvalidTypes)) {
                    Field field = fields.get(i);
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                    mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                    mv.visitVarInsn(loadValueInstruction, 3);
                    mv.visitFieldInsn(Opcodes.PUTFIELD, field.getDeclaringClass().getName().replace('.', '/'), field.getName(),
                            typeNameInternal);
                    mv.visitInsn(Opcodes.RETURN);
                }
            }
            // Rest of fields: different type
            if (hasAnyBadTypeLabel) {
                mv.visitLabel(labelForInvalidTypes);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                insertThrowExceptionForFieldType(mv, primitiveType.getClassName());
            }
            // Default: field not found
            mv.visitLabel(defaultLabel);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, maxLocals);
        mv.visitEnd();
    }

    private static void insertGetPrimitive(ClassWriter cw, String classNameInternal, ArrayList<Field> fields,
                                           Type primitiveType) {
        int maxStack = 6;
        final String getterMethodName;
        final String typeNameInternal = primitiveType.getDescriptor();
        final int returnValueInstruction;
        switch (primitiveType.getSort()) {
            case Type.BOOLEAN:
                getterMethodName = "getBoolean";
                returnValueInstruction = Opcodes.IRETURN;
                break;
            case Type.BYTE:
                getterMethodName = "getByte";
                returnValueInstruction = Opcodes.IRETURN;
                break;
            case Type.CHAR:
                getterMethodName = "getChar";
                returnValueInstruction = Opcodes.IRETURN;
                break;
            case Type.SHORT:
                getterMethodName = "getShort";
                returnValueInstruction = Opcodes.IRETURN;
                break;
            case Type.INT:
                getterMethodName = "getInt";
                returnValueInstruction = Opcodes.IRETURN;
                break;
            case Type.FLOAT:
                getterMethodName = "getFloat";
                returnValueInstruction = Opcodes.FRETURN;
                break;
            case Type.LONG:
                getterMethodName = "getLong";
                returnValueInstruction = Opcodes.LRETURN;
                break;
            case Type.DOUBLE:
                getterMethodName = "getDouble";
                returnValueInstruction = Opcodes.DRETURN;
                break;
            default:
                getterMethodName = "get";
                returnValueInstruction = Opcodes.ARETURN;
                break;
        }
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, getterMethodName, "(Ljava/lang/Object;I)" + typeNameInternal, null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ILOAD, 2);

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            for (int i = 0, n = labels.length; i < n; i++) {
                if (Type.getType(fields.get(i).getType()).equals(primitiveType))
                    labels[i] = new Label();
                else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                Field field = fields.get(i);
                if (!labels[i].equals(labelForInvalidTypes)) {
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                    mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                    mv.visitFieldInsn(Opcodes.GETFIELD, field.getDeclaringClass().getName().replace('.', '/'), field.getName(),
                            typeNameInternal);
                    mv.visitInsn(returnValueInstruction);
                }
            }
            // Rest of fields: different type
            if (hasAnyBadTypeLabel) {
                mv.visitLabel(labelForInvalidTypes);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                insertThrowExceptionForFieldType(mv, primitiveType.getClassName());
            }
            // Default: field not found
            mv.visitLabel(defaultLabel);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    private static MethodVisitor insertThrowExceptionForFieldNotFound(MethodVisitor mv) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(Opcodes.DUP);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn("Field not found: ");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.ATHROW);
        return mv;
    }

    private static MethodVisitor insertThrowExceptionForFieldType(MethodVisitor mv, String fieldType) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(Opcodes.DUP);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn("Field not declared as " + fieldType + ": ");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.ATHROW);
        return mv;
    }

}
