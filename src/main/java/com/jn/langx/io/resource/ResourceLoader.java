package com.jn.langx.io.resource;

import com.jn.langx.annotation.Nullable;

public interface ResourceLoader {

    /**
     * Return a Resource handle for the specified resource location.
     * <p>The handle should always be a reusable resource descriptor,
     * allowing for multiple {@link Resource#getInputStream()} calls.
     * <p><ul>
     * <li>Must support fully qualified URLs, e.g. "file:C:/test.dat".
     * <li>Must support classpath pseudo-URLs, e.g. "classpath:test.dat".
     * <li>Should support relative file paths, e.g. "WEB-INF/test.dat".
     * (This will be implementation-specific, typically provided by an
     * ApplicationContext implementation.)
     * </ul>
     * <p>Note that a Resource handle does not imply an existing resource;
     * you need to invoke {@link Resource#exists} to check for existence.
     * @param location the resource location
     * @return a corresponding Resource handle (never {@code null})
     * @see Resource#exists()
     * @see Resource#getInputStream()
     */
    <V>Resource<V> loadResource(String location);

    /**
     * Expose the ClassLoader used by this ResourceLoader.
     * <p>Clients which need to access the ClassLoader directly can do so
     * in a uniform manner with the ResourceLoader, rather than relying
     * on the thread context ClassLoader.
     * @return the ClassLoader
     * (only {@code null} if even the system ClassLoader isn't accessible)
     */
    @Nullable
    ClassLoader getClassLoader();
}
