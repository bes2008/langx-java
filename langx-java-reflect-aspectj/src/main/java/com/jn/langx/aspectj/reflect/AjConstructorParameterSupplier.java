package com.jn.langx.aspectj.reflect;

import com.jn.langx.annotation.Name;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.reflect.ParameterServiceRegistry;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.parameter.AbstractConstructorParameterSupplier;
import com.jn.langx.util.reflect.parameter.ConstructorParameter;
import com.jn.langx.util.reflect.parameter.ConstructorParameterSupplier;
import com.jn.langx.util.reflect.parameter.ParameterMeta;
import org.aspectj.apache.bcel.Repository;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.LocalVariable;
import org.aspectj.apache.bcel.classfile.LocalVariableTable;
import org.aspectj.apache.bcel.classfile.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

@Name(AjReflectConstants.DEFAULT_PARAMETER_SUPPLIER_NAME)
public class AjConstructorParameterSupplier extends AbstractConstructorParameterSupplier {
    private static final Logger logger = LoggerFactory.getLogger(AjConstructorParameterSupplier.class);
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
    public void init() throws InitializationException {
        if (!inited) {
            if (delegate == null) {
                delegate = ParameterServiceRegistry.getInstance().getDefaultConstructorParameterSupplier();
            }
            inited = true;
        }
    }

    @Override
    public ConstructorParameter get(ParameterMeta meta) {
        init();
        String parameterName = findRealParameterName(meta);
        meta.setName(parameterName);
        ConstructorParameter delegate = this.delegate.get(meta);
        return new AjConstructorParameter(parameterName, delegate);
    }

    private String findRealParameterName(ParameterMeta meta) {
        try {
            Constructor constructor = (Constructor) meta.getExecutable();
            Class declaringClass = constructor.getDeclaringClass();
            JavaClass classAj = Repository.lookupClass(Reflects.getFQNClassName(declaringClass));
            Method methodAj = classAj.getMethod(constructor);
            LocalVariableTable lvt = methodAj.getLocalVariableTable();

            if (lvt != null) {
                for (int i = 0; i < lvt.getTableLength(); i++) {
                    LocalVariable localVariable = lvt.getLocalVariable(i);

                    if (localVariable.getStartPC() == 0) {
                        // 构造器参数中， index 为0 的是 this 关键字
                        if (localVariable.getIndex() - 1 == meta.getIndex()) {
                            return localVariable.getName();
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "arg" + meta.getIndex();
    }
}
