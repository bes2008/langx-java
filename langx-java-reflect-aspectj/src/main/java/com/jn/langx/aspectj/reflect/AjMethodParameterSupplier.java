package com.jn.langx.aspectj.reflect;

import com.jn.langx.annotation.Name;
import com.jn.langx.aspectj.coderepository.Repositorys;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.Strings;
import com.jn.langx.util.reflect.Modifiers;
import com.jn.langx.util.reflect.ParameterServiceRegistry;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.parameter.AbstractMethodParameterSupplier;
import com.jn.langx.util.reflect.parameter.MethodParameter;
import com.jn.langx.util.reflect.parameter.MethodParameterSupplier;
import com.jn.langx.util.reflect.parameter.ParameterMeta;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.LocalVariable;
import org.aspectj.apache.bcel.classfile.LocalVariableTable;
import org.aspectj.apache.bcel.util.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@Name(AjReflectConstants.DEFAULT_PARAMETER_SUPPLIER_NAME)
public class AjMethodParameterSupplier extends AbstractMethodParameterSupplier {
    private static final Logger logger = LoggerFactory.getLogger(AjMethodParameterSupplier.class);
    private MethodParameterSupplier delegate;

    public AjMethodParameterSupplier() {

    }

    public AjMethodParameterSupplier(MethodParameterSupplier delegate) {
        this.delegate = delegate;
    }

    @Override
    public void init() throws InitializationException {
        if (!inited) {
            if (delegate == null) {
                delegate = ParameterServiceRegistry.getInstance().getDefaultMethodParameterSupplier();
            }
            inited = true;
        }
    }

    @Override
    public boolean usingJdkApi() {
        return false;
    }

    @Override
    public MethodParameter get(ParameterMeta meta) {
        init();
        String parameterName = findRealParameterName(meta);
        if (Strings.isNotEmpty(parameterName)) {
            meta.setName(parameterName);
        }
        // 获取参数，这里拿到的参数很可能跟之前获取的不一样
        MethodParameter delegate = this.delegate.get(meta);
        if (Strings.isEmpty(parameterName)) {
            parameterName = delegate.getName();
        }
        if (Strings.isEmpty(parameterName)) {
            parameterName = "arg" + meta.getIndex();
        }
        return new AjMethodParameter(parameterName, delegate);
    }

    private String findRealParameterName(ParameterMeta meta) {
        try {
            Method method = (Method) meta.getExecutable();
            Class declaringClass = method.getDeclaringClass();
            String classname = Reflects.getFQNClassName(declaringClass);

            Repository repository = Repositorys.getClassLoaderRepository(declaringClass);

            JavaClass classAj = Repositorys.loadJavaClass(repository, classname);
            if (classAj == null) {
                logger.warn("Can't find the BCEL JavaClass for the class {}", classname);
            } else {
                org.aspectj.apache.bcel.classfile.Method methodAj = classAj.getMethod(method);
                if (methodAj == null) {
                    logger.warn("Can't find the BCEL method for the method {} in the class {}", "", classname);
                } else {
                    LocalVariableTable lvt = methodAj.getLocalVariableTable();

                    // 如果一个jar 在编译时，设置 编译时不保留 vars , 这种情况下 lvt 将是 null
                    if (lvt != null) {
                        for (int i = 0; i < lvt.getTableLength(); i++) {
                            LocalVariable localVariable = lvt.getLocalVariable(i);

                            // 只有 start pc 为0 的，才会是 方法的参数
                            if (localVariable.getStartPC() == 0) {
                                if (Modifiers.isStatic(method)) {
                                    if (localVariable.getIndex() == meta.getIndex()) {
                                        return localVariable.getName();
                                    }
                                } else {
                                    // 实例方法的参数中， index 为0 的是 this 关键字
                                    if (localVariable.getIndex() - 1 == meta.getIndex()) {
                                        return localVariable.getName();
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }
}
