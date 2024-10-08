package com.jn.langx.aspectj.reflect;

import com.jn.langx.annotation.Name;
import com.jn.langx.aspectj.coderepository.Repositorys;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.Strings;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.ParameterServiceRegistry;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.parameter.AbstractConstructorParameterSupplier;
import com.jn.langx.util.reflect.parameter.ConstructorParameter;
import com.jn.langx.util.reflect.parameter.ConstructorParameterSupplier;
import com.jn.langx.util.reflect.parameter.ParameterMeta;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.LocalVariable;
import org.aspectj.apache.bcel.classfile.LocalVariableTable;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.apache.bcel.util.Repository;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;

@SuppressWarnings("rawtypes")
@Name(AjReflectConstants.DEFAULT_PARAMETER_SUPPLIER_NAME)
public class AjConstructorParameterSupplier extends AbstractConstructorParameterSupplier {
    private ConstructorParameterSupplier delegate;

    public AjConstructorParameterSupplier() {
    }

    public AjConstructorParameterSupplier(ConstructorParameterSupplier delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean usingJdkApi() {
        return false;
    }

    @Override
    protected void doInit() throws InitializationException {
        if (delegate == null) {
            delegate = ParameterServiceRegistry.getInstance().getDefaultConstructorParameterSupplier();
        }
    }

    @Override
    public ConstructorParameter get(ParameterMeta meta) {
        init();
        String parameterName = findRealParameterName(meta);
        if (Strings.isNotEmpty(parameterName)) {
            meta.setName(parameterName);
        }
        ConstructorParameter delegate = this.delegate.get(meta);
        if (Strings.isEmpty(parameterName)) {
            parameterName = delegate.getName();
        }
        if (Strings.isEmpty(parameterName)) {
            parameterName = "arg" + meta.getIndex();
        }
        return new AjConstructorParameter(parameterName, delegate);
    }

    private String findRealParameterName(ParameterMeta meta) {
        String classname = null;
        Logger logger = Loggers.getLogger(AjConstructorParameterSupplier.class);
        try {
            //noinspection rawtypes
            @SuppressWarnings("rawtypes")
            Constructor constructor = (Constructor) meta.getExecutable();
            Class declaringClass = constructor.getDeclaringClass();
            classname = Reflects.getFQNClassName(declaringClass);

            Repository repository = Repositorys.getClassLoaderRepository(declaringClass);

            JavaClass classAj = Repositorys.loadJavaClass(repository, classname);
            if (classAj == null) {
                logger.warn("Can't find the BCEL JavaClass for the class {}", classname);
            } else {

                // 构造器的本质 是 <init> 方法
                Method methodAj = classAj.getMethod(constructor);
                if (methodAj == null) {
                    logger.warn("Can't find the BCEL constructor for the constructor {}", "");
                } else {
                    LocalVariableTable lvt = methodAj.getLocalVariableTable();

                    // 如果一个jar 在编译时，设置 编译时不保留 vars , 这种情况下 lvt 将是 null
                    if (lvt != null) {
                        for (int i = 0; i < lvt.getTableLength(); i++) {
                            LocalVariable localVariable = lvt.getLocalVariable(i);

                            // 只有 start pc 为0 的，才会是 方法的参数
                            if (localVariable.getStartPC() == 0) {
                                // 构造器参数中， index 为0 的是 this 关键字
                                if (localVariable.getIndex() - 1 == meta.getIndex()) {
                                    return localVariable.getName();
                                }
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            logger.error("Can't find the class {} when use BCEL load it", classname, ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }
}
