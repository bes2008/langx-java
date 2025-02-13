package com.jn.langx;

/**
 * The NamedCustomizer interface extends the Named and Customizer interfaces, requiring implementing classes to provide an name
 * and be able to customize a target object of type T.
 *
 * This interface aims to combine the naming functionality and the customization functionality, allowing implementers to not only
 * identify themselves through a name but also to customize the behavior or state of a target object.
 *
 * @param <T> The type parameter, representing the type of object that the implementer can customize.
 */
public interface NamedCustomizer<T> extends Named, Customizer<T> {
    /**
     * Gets the name of this customizer.
     *
     * @return The name of the customizer, used to uniquely identify it.
     */
    @Override
    String getName();

    /**
     * Customizes the behavior or state of the target object.
     *
     * This method allows the implementer to modify the target object in some way, with the specific behavior determined by the implementation.
     *
     * @param target The target object to be customized, of type T.
     */
    @Override
    void customize(T target);
}
