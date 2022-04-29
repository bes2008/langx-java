package com.jn.langx.annotation;



import java.lang.annotation.*;

/**
 * This annotation type is used to indicate that instances of the associated
 * class may not be altered after they have been created.  Note that this may or
 * may not indicate strict immutability, as some classes marked with this
 * annotation type may have their internal state altered in some way that is not
 * externally visible.  In addition, the following caveats must be observed for
 * classes containing this annotation type, and for all other classes in the
 * LDAP SDK:
 * <UL>
 *   <LI>
 *     If an array is provided as an argument to a constructor or a method, then
 *     that array must not be referenced or altered by the caller at any time
 *     after that point unless it is clearly noted that it is acceptable to do
 *     so.
 *     <BR><BR>
 *   </LI>
 *
 *   <LI>
 *     If an array is returned by a method, then the contents of that array must
 *     not be altered unless it is clearly noted that it is acceptable to do so.
 *     <BR><BR>
 *   </LI>
 * </UL>
 * <BR><BR>
 * It will only be used for classes which are primarily used as data structures
 * and will not be included in classes whose primary purpose is something other
 * than as a data type.  It will also not be used for interfaces, abstract
 * classes, or enums.
 * <BR><BR>
 * This annotation type will appear in the generated Javadoc documentation for
 * classes and interfaces that include it.
 *
 */
@Documented()
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface NotMutable
{
}
