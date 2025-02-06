package com.jn.langx.text.i18n;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.util.Locale;

/**
 * I18nMessageStorage接口用于管理国际化消息的存储和检索
 * 它提供了一种机制来设置和获取当前的locale（地区信息），以及根据消息键和参数获取本地化消息
 */
public interface I18nMessageStorage {

    /**
     * 设置默认的locale
     *
     * @param locale 非空的Locale对象，表示要设置的地区信息
     */
    void setLocale(@NonNull Locale locale);

    /**
     * 获取当前设置的locale
     *
     * @return 当前的Locale对象
     */
    @NonNull
    Locale getLocale();

    /**
     * 根据消息键和参数获取本地化消息
     * 此方法使用默认的locale和类加载器来检索消息
     *
     * @param key 非空的消息键，用于标识消息
     * @param args 可选的消息参数，用于替换消息中的占位符
     * @return 根据键和参数获取的本地化消息字符串
     */
    String getMessage(@NonNull String key, @Nullable Object[] args);

    /**
     * 根据类加载器、消息键和参数获取本地化消息
     * 此方法允许指定一个类加载器，以支持从不同的类路径资源中加载消息
     *
     * @param classLoader 可选的类加载器，用于加载消息资源
     * @param key 非空的消息键，用于标识消息
     * @param args 可变的消息参数，用于替换消息中的占位符
     * @return 根据类加载器、键和参数获取的本地化消息字符串
     */
    String getMessage(@Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args);

    /**
     * 根据locale、消息键和参数获取本地化消息
     * 此方法允许指定一个特定的locale，以支持获取不同地区的消息
     *
     * @param locale 可选的Locale对象，表示要使用的地区信息
     * @param key 非空的消息键，用于标识消息
     * @param args 可变的消息参数，用于替换消息中的占位符
     * @return 根据locale、键和参数获取的本地化消息字符串
     */
    String getMessage(@Nullable Locale locale, @NonNull String key, @Nullable Object... args);

    /**
     * 根据locale、类加载器、消息键和参数获取本地化消息
     * 此方法结合了locale和类加载器的使用，提供了最大的灵活性来检索特定环境下的消息
     *
     * @param locale 可选的Locale对象，表示要使用的地区信息
     * @param classLoader 可选的类加载器，用于加载消息资源
     * @param key 非空的消息键，用于标识消息
     * @param args 可变的消息参数，用于替换消息中的占位符
     * @return 根据locale、类加载器、键和参数获取的本地化消息字符串
     */
    String getMessage(@Nullable Locale locale, @Nullable ClassLoader classLoader, @NonNull String key, @Nullable Object... args);


}

