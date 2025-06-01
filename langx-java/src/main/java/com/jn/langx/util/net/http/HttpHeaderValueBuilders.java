package com.jn.langx.util.net.http;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;

import java.util.List;
import java.util.Map;

public class HttpHeaderValueBuilders {

    public static String buildHeaderValueWithType(@NonNull String type, @NonNull String typeToParamsSeparator, @Nullable Map<String, String> paramMap, @NonNull String separator) {
        List<String> params = Pipeline.of(paramMap.entrySet())
                .map(new Function<Map.Entry<String, String>, String>() {
                    @Override
                    public String apply(Map.Entry<String, String> entry) {
                        return entry.getKey() + "=" + entry.getValue();
                    }
                }).asList();

        return buildHeaderValueWithType(type, typeToParamsSeparator, params, separator);
    }

    public static String buildHeaderValueWithType(@NonNull String type, @NonNull String typeToParamsSeparator, @Nullable List<String> params, @NonNull String paramsSeparator) {
        String paramsString = Strings.join(paramsSeparator, params);
        return buildHeaderValueWithType(type, typeToParamsSeparator, paramsString);
    }

    /**
     * build header value
     *
     * @param type                  值的类型，required
     * @param typeToParamsSeparator 值与directives之间的分隔符
     * @param paramsString          指令集字符串
     */
    public static String buildHeaderValueWithType(String type, String typeToParamsSeparator, String paramsString) {
        Preconditions.checkNotEmpty(type, "type is required");
        return Strings.trimToEmpty(type) + typeToParamsSeparator + Strings.trimToEmpty(paramsString);
    }

    public static String buildHeaderValueWithMultiValues(List<String> values, String separator) {
        return Strings.join(separator, values);
    }

}
