package com.jn.langx.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Interface for discovering parameter names of methods and constructors.
 * This interface is used to obtain the names of parameters for methods and constructors,
 * which can be helpful for debugging, code analysis, or generating documentation.
 */
public interface ParameterNameDiscoverer {

    /**
     * Return parameter names for this method,
     * or {@code null} if they cannot be determined.
     * <p>
     * This method attempts to discover the names of the parameters of a given method.
     * If the parameter names cannot be determined, it returns {@code null}.
     *
     * @param method method to find parameter names for
     * @return an array of parameter names if the names can be resolved,
     * or {@code null} if they cannot
     */
    String[] getParameterNames(Method method);

    /**
     * Return parameter names for this constructor,
     * or {@code null} if they cannot be determined.
     * <p>
     * This method attempts to discover the names of the parameters of a given constructor.
     * If the parameter names cannot be determined, it returns {@code null}.
     *
     * @param ctor constructor to find parameter names for
     * @return an array of parameter names if the names can be resolved,
     * or {@code null} if they cannot
     */
    String[] getParameterNames(Constructor<?> ctor);

}
