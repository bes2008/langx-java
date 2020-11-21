package com.jn.langx.text;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.valuegetter.ValueGetter2;

import java.util.Map;
import java.util.regex.Pattern;

public class StringTemplates {

    /**
     * %s, %d
     *
     * @param template the template
     * @param args     the arguments
     * @return formatted string
     * @see {@link String#format(String, Object...)}
     */
    public static String formatWithCStyle(String template, Object... args) {
        return new CStyleStringFormatter().format(template, args);
    }

    public static Supplier<Object[], String> cStyleSupplier(final String template) {
        return new Supplier<Object[], String>() {
            @Override
            public String get(Object[] params) {
                return formatWithCStyle(template, params);
            }
        };
    }

    /**
     * format based placeholder: {}
     *
     * @param template the string template
     * @return formatted string
     */
    public static String formatWithPlaceholder(String template, Object... args) {
        return new PlaceholderStringFormatter().format(template, args);
    }

    public static Supplier<Object[], String> placeholderStyleSupplier(final String template) {
        return new Supplier<Object[], String>() {
            @Override
            public String get(Object[] params) {
                return formatWithPlaceholder(template, params);
            }
        };
    }


    /**
     * format based index: {0}, {1}, {2}
     * the index based on 0
     *
     * @param template the string template
     * @return formatted string
     * @see #format(String, Object...)
     */
    public static String formatWithIndex(String template, Object... args) {
        return format(template, args);
    }

    public static Supplier<Object[], String> indexStyleSupplier(final String template) {
        return new Supplier<Object[], String>() {
            @Override
            public String get(Object[] params) {
                return formatWithIndex(template, params);
            }
        };
    }

    /**
     * format based index: {0}, {1}, {2}
     *
     * @param template the string template
     * @param args     args
     * @return formatted string
     * @see #formatWithIndex(String, Object...)
     */
    public static String format(String template, Object[] args) {
        return new IndexStringFormatter().format(template, args);
    }

    public static Supplier<Object[], String> supplier(final String template) {
        return new Supplier<Object[], String>() {
            @Override
            public String get(Object[] params) {
                return format(template, params);
            }
        };
    }

    /**
     * format based on a bean, the variable: ${fieldName}
     *
     * @param template the string template
     * @param bean     the bean
     * @param <T>      the bean type
     * @return formatted string
     */
    public static <T> String formatWithBean(String template, T bean) {
        return new BeanBasedStyleStringFormatter().format(template, bean);
    }

    public static Supplier<Object[], String> beanStyleSupplier(final String template) {
        return new Supplier<Object[], String>() {
            @Override
            public String get(Object[] params) {
                return formatWithBean(template, params);
            }
        };
    }

    /**
     * format based on a map, the variable: ${key}
     *
     * @param template the string template
     * @return formatted string
     */
    public static String formatWithMap(String template, Map<String, ?> map) {
        return formatWithMap(template, MapBasedStringFormatter.PatternStyle.$, map);
    }

    public static Supplier<Object[], String> mapStyleSupplier(@NonNull final String template) {
        return mapStyleSupplier(template, null);
    }

    @SuppressWarnings({"unchecked"})
    public static Supplier<Object[], String> mapStyleSupplier(@NonNull final String template, @Nullable final MapBasedStringFormatter.PatternStyle patternStyle) {
        return new Supplier<Object[], String>() {
            @Override
            public String get(Object[] params) {
                return formatWithMap(template, patternStyle, Emptys.isNotEmpty(params) ? (Map) params[0] : null);
            }
        };
    }

    /**
     * format based on a map, the variable: ${key}
     *
     * @param template the string template
     * @return formatted string
     */
    public static String formatWithMap(String template, MapBasedStringFormatter.PatternStyle patternStyle, Map<String, ?> map) {
        return new MapBasedStringFormatter(patternStyle).format(template, map);
    }

    /**
     * custom formatter
     *
     * @param template        the string template
     * @param variablePattern variable pattern in template
     * @param valueGetter     variable's value getter, will get value from args
     * @param args            args
     * @return formatted string
     */
    public static String format(String template, String variablePattern, Function2<String, Object[], String> valueGetter, Object... args) {
        return new CustomPatternStringFormatter(variablePattern, valueGetter).format(template, args);
    }

    /**
     * custom formatter
     *
     * @param template        the string template
     * @param variablePattern variable pattern in template
     * @param valueGetter     variable's value getter, will get value from args
     * @param args            args
     * @return formatted string
     */
    public static String format(String template, Pattern variablePattern, Function2<String, Object[], String> valueGetter, Object... args) {
        // 需要自己剔除变量的前后标记
        return new CustomPatternStringFormatter(variablePattern, valueGetter).format(template, args);
    }

    /**
     * 模板变量替换
     * @param template 模板
     * @param variableStartFlag 变量的regexp pattern的前缀标识，例如 { 或 ${
     * @param variableEndFlag 变量的regexp pattern的后缀标识，例如 { 或 ${
     * @return 替换后排的字符串
     * @since 2.10.1
     */
    public static String format(String template, final String variableStartFlag, final String variableEndFlag, final Function2<String, Object[], String> valueGetter, final Object... args) {
        String startFlagPattern = variableStartFlag.replace("$", "\\$")
                .replace("[", "\\[")
                .replace("{", "\\{")
                .replace("(", "\\)");

        String endFlagPattern = variableEndFlag.replace("$", "\\$")
                .replace("]", "\\]")
                .replace("}", "\\}")
                .replace(")", "\\)");

        return format(template, startFlagPattern + "[\\w\\-]+(\\.[\\w\\-]+)*" + endFlagPattern, new Function2<String, Object[], String>() {
            @Override
            public String apply(String variable, Object[] arguments) {
                // 需要自己剔除变量的前后标记
                if (variable.startsWith(variableStartFlag)) {
                    variable = variable.substring(variableStartFlag.length());
                }
                if (variable.endsWith(variableEndFlag)) {
                    variable = variable.substring(0, variable.length() - variableEndFlag.length());
                }
                return valueGetter.apply(variable, args);
            }
        });
    }

    /**
     * 模板变量替换
     * @param template 模板
     * @param variablePattern 变量的regexp pattern
     * @param variableValueProvider 用于提供变量值
     * @return 替换后排的字符串
     * @since 2.10.1
     */
    public static String format(String template, Pattern variablePattern, final PlaceholderParser variableValueProvider) {
        return format(template, variablePattern, new Function2<String, Object[], String>() {
            @Override
            public String apply(String variable, Object[] arguments) {
                // 需要自己剔除变量的前后标记
                return variableValueProvider.parse(variable);
            }
        });
    }

    /**
     * 模板替换
     * @return 2.10.1
     */
    public static String format(String template, final String variableStartFlag, final String variableEndFlag, final PlaceholderParser variableValueProvider) {
        return format(template, variableStartFlag, variableEndFlag, new Function2<String, Object[], String>() {
            @Override
            public String apply(String variable, Object[] arguments) {
                // 需要自己剔除变量的前后标记
                return variableValueProvider.parse(variable);
            }
        });
    }

    /**
     * 模板替换
     * @return 2.10.1
     */
    public static String format(String template, final String variableStartFlag, final String variableEndFlag, final ValueGetter2<String> valueGetter) {
        return format(template, variableStartFlag, variableEndFlag, new Function2<String, Object[], String>() {
            @Override
            public String apply(String variable, Object[] arguments) {
                // 需要自己剔除变量的前后标记
                return valueGetter.getString(variable);
            }
        });
    }

    public static TemplateFluenter fluenter(String template) {
        return new TemplateFluenter(template);
    }

    public static final class TemplateFluenter {
        private String template;

        private TemplateFluenter(String template) {
            this.template = template;
        }

        public TemplateFluenter format(StringTemplateFormatter formatter, Object... args) {
            template = formatter.format(template, args);
            return this;
        }

        public TemplateFluenter format(String variablePattern, Function2<String, Object[], String> valueGetter, Object... args) {
            return format(new CustomPatternStringFormatter(variablePattern, valueGetter), args);
        }

        public TemplateFluenter format(Pattern variablePattern, Function2<String, Object[], String> valueGetter, Object... args) {
            return format(new CustomPatternStringFormatter(variablePattern, valueGetter), args);
        }

        public TemplateFluenter formatWithIndex(Object... args) {
            return format(new IndexStringFormatter(), args);
        }

        public TemplateFluenter formatWithPlaceHolder(Object... args) {
            return format(new PlaceholderStringFormatter(), args);
        }

        public <T> TemplateFluenter formatWithBean(T bean) {
            return format(new BeanBasedStyleStringFormatter(), bean);
        }

        public <T> TemplateFluenter formatWithMap(Map<String, Object> map) {
            return format(new MapBasedStringFormatter(), map);
        }

        public String get() {
            return template;
        }
    }
}
