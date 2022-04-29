package com.jn.langx.annotation;


import com.unboundid.util.Extensible;

import java.lang.annotation.*;

/**
 * This annotation type is used to indicate that a non-final class or interface
 * is NOT intended to be extended or implemented by third-party code.  In order
 * to be completely safe, third-party code should only extend or implement code
 * marked with the {@code @Extensible} annotation type, but the
 * {@code @NotExtensible} annotation type can serve as a reminder for classes
 * or interfaces that are not intended to be extended or implemented by
 * third-party code.
 * <BR><BR>
 * This annotation type will appear in the generated Javadoc documentation for
 * classes and interfaces that include it.
 *
 * @see  Extensible
 */
@Documented()
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface NotExtensible
{
}