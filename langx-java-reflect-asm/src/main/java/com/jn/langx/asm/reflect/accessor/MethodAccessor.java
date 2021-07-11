package com.jn.langx.asm.reflect.accessor;


import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.Reflects;
import org.objectweb.asm.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("all")
public abstract class MethodAccessor {
    private String[] methodNames;
    private Class[][] parameterTypes;
    private Class[] returnTypes;

    abstract public Object invoke(Object object, int methodIndex, Object... args);

    /**
     * Invokes the method with the specified name and the specified param types.
     */
    public Object invoke(Object object, String methodName, Class[] paramTypes, Object... args) {
        return invoke(object, getIndex(methodName, paramTypes), args);
    }

    /**
     * Invokes the first method with the specified name and the specified number of arguments.
     */
    public Object invoke(Object object, String methodName, Object... args) {
        return invoke(object, getIndex(methodName, args == null ? 0 : args.length), args);
    }

    /**
     * Returns the index of the first method with the specified name.
     */
    public int getIndex(String methodName) {
        for (int i = 0, n = methodNames.length; i < n; i++) {
            if (methodNames[i].equals(methodName)){
                return i;
            }
        }
        throw new IllegalArgumentException("Unable to find non-private method: " + methodName);
    }

    /**
     * Returns the index of the first method with the specified name and param types.
     */
    public int getIndex(String methodName, Class... paramTypes) {
        for (int i = 0, n = methodNames.length; i < n; i++) {
            if (methodNames[i].equals(methodName) && Arrays.equals(paramTypes, parameterTypes[i])){
                return i;
            }
        }
        throw new IllegalArgumentException("Unable to find non-private method: " + methodName + " " + Arrays.toString(paramTypes));
    }

    /**
     * Returns the index of the first method with the specified name and the specified number of arguments.
     */
    public int getIndex(String methodName, int paramsCount) {
        for (int i = 0, n = methodNames.length; i < n; i++) {
            if (methodNames[i].equals(methodName) && parameterTypes[i].length == paramsCount){
                return i;
            }
        }
        throw new IllegalArgumentException(
                "Unable to find non-private method: " + methodName + " with " + paramsCount + " params.");
    }

    public List<String> getMethodNames() {
        return Collects.newArrayList(methodNames);
    }

    public List<Class[]> getParameterTypes() {
        return Collects.newArrayList(parameterTypes);
    }

    public List<Class> getReturnTypes() {
        return Collects.newArrayList(returnTypes);
    }

    /**
     * Creates a new MethodAccess for the specified type.
     *
     * @param type Must not be a primitive type, or void.
     */
    public static MethodAccessor get(Class type) {
        boolean isInterface = type.isInterface();
        if (!isInterface && type.getSuperclass() == null && type != Object.class) {
            throw new IllegalArgumentException("The type must not be an interface, a primitive type, or void.");
        }

        ArrayList<Method> methods = new ArrayList<Method>();
        if (!isInterface) {
            Class nextClass = type;
            while (nextClass != Object.class) {
                addDeclaredMethodsToList(nextClass, methods);
                nextClass = nextClass.getSuperclass();
            }
        } else {
            recursiveAddInterfaceMethodsToList(type, methods);
        }
        int n = methods.size();
        String[] methodNames = new String[n];
        Class[][] parameterTypes = new Class[n][];
        Class[] returnTypes = new Class[n];
        for (int i = 0; i < n; i++) {
            Method method = methods.get(i);
            methodNames[i] = method.getName();
            parameterTypes[i] = method.getParameterTypes();
            returnTypes[i] = method.getReturnType();
        }

        String className = type.getName();
        String accessorClassName = className + "MethodAccessor";
        if (accessorClassName.startsWith("java.")) {
            accessorClassName = "langxreflectasm." + accessorClassName;
        }

        Class accessClass;
        AccessorClassLoader loader = AccessorClassLoader.get(type);
        synchronized (loader) {
            accessClass = loader.loadAccessClass(accessorClassName);
            if (accessClass == null) {
                String accessClassNameInternal = accessorClassName.replace('.', '/');
                String classNameInternal = className.replace('.', '/');

                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                MethodVisitor mv;
                cw.visit(Opcodes.V1_1, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, accessClassNameInternal, null, Reflects.getFQNClassName(MethodAccessor.class).replace('.', '/'),
                        null);
                {
                    mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
                    mv.visitCode();
                    mv.visitVarInsn(Opcodes.ALOAD, 0);
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Reflects.getFQNClassName(MethodAccessor.class).replace('.', '/'), "<init>", "()V");
                    mv.visitInsn(Opcodes.RETURN);
                    mv.visitMaxs(0, 0);
                    mv.visitEnd();
                }
                {
                    mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_VARARGS, "invoke",
                            "(Ljava/lang/Object;I[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
                    mv.visitCode();

                    if (!methods.isEmpty()) {
                        mv.visitVarInsn(Opcodes.ALOAD, 1);
                        mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                        mv.visitVarInsn(Opcodes.ASTORE, 4);

                        mv.visitVarInsn(Opcodes.ILOAD, 2);
                        Label[] labels = new Label[n];
                        for (int i = 0; i < n; i++)
                            labels[i] = new Label();
                        Label defaultLabel = new Label();
                        mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

                        StringBuilder buffer = new StringBuilder(128);
                        for (int i = 0; i < n; i++) {
                            mv.visitLabel(labels[i]);
                            if (i == 0)
                                mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{classNameInternal}, 0, null);
                            else
                                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                            mv.visitVarInsn(Opcodes.ALOAD, 4);

                            buffer.setLength(0);
                            buffer.append('(');

                            Class[] paramTypes = parameterTypes[i];
                            Class returnType = returnTypes[i];
                            for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
                                mv.visitVarInsn(Opcodes.ALOAD, 3);
                                mv.visitIntInsn(Opcodes.BIPUSH, paramIndex);
                                mv.visitInsn(Opcodes.AALOAD);
                                Type paramType = Type.getType(paramTypes[paramIndex]);
                                switch (paramType.getSort()) {
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
                                        mv.visitTypeInsn(Opcodes.CHECKCAST, paramType.getDescriptor());
                                        break;
                                    case Type.OBJECT:
                                        mv.visitTypeInsn(Opcodes.CHECKCAST, paramType.getInternalName());
                                        break;
                                    default:
                                        break;
                                }
                                buffer.append(paramType.getDescriptor());
                            }

                            buffer.append(')');
                            buffer.append(Type.getDescriptor(returnType));
                            int invoke;
                            if (isInterface)
                                invoke = Opcodes.INVOKEINTERFACE;
                            else if (Modifier.isStatic(methods.get(i).getModifiers()))
                                invoke = Opcodes.INVOKESTATIC;
                            else
                                invoke = Opcodes.INVOKEVIRTUAL;
                            mv.visitMethodInsn(invoke, classNameInternal, methodNames[i], buffer.toString());

                            switch (Type.getType(returnType).getSort()) {
                                case Type.VOID:
                                    mv.visitInsn(Opcodes.ACONST_NULL);
                                    break;
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
                            }

                            mv.visitInsn(Opcodes.ARETURN);
                        }

                        mv.visitLabel(defaultLabel);
                        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                    }
                    mv.visitTypeInsn(Opcodes.NEW, "java/lang/IllegalArgumentException");
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitLdcInsn("Method not found: ");
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
                    mv.visitVarInsn(Opcodes.ILOAD, 2);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
                    mv.visitInsn(Opcodes.ATHROW);
                    mv.visitMaxs(0, 0);
                    mv.visitEnd();
                }
                cw.visitEnd();
                byte[] data = cw.toByteArray();
                accessClass = loader.defineAccessClass(accessorClassName, data);
            }
        }
        try {
            MethodAccessor access = (MethodAccessor) accessClass.newInstance();
            access.methodNames = methodNames;
            access.parameterTypes = parameterTypes;
            access.returnTypes = returnTypes;
            return access;
        } catch (Throwable t) {
            throw new RuntimeException("Error constructing method access class: " + accessorClassName, t);
        }
    }

    private static void addDeclaredMethodsToList(Class type, ArrayList<Method> methods) {
        Method[] declaredMethods = type.getDeclaredMethods();
        for (int i = 0, n = declaredMethods.length; i < n; i++) {
            Method method = declaredMethods[i];
            int modifiers = method.getModifiers();
            // if (Modifier.isStatic(modifiers)) continue;
            if (Modifier.isPrivate(modifiers)) continue;
            methods.add(method);
        }
    }

    private static void recursiveAddInterfaceMethodsToList(Class interfaceType, ArrayList<Method> methods) {
        addDeclaredMethodsToList(interfaceType, methods);
        for (Class nextInterface : interfaceType.getInterfaces()) {
            recursiveAddInterfaceMethodsToList(nextInterface, methods);
        }
    }
}
