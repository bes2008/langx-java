package com.jn.langx.text.stringtemplate;

import com.jn.langx.util.Objects;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.struct.Entry;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.struct.Pair;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class StringTemplateFormatterChain implements StringTemplateFormatter {
    private List<Pair<StringTemplateFormatter, Object[]>> chain = Collects.emptyArrayList();

    public StringTemplateFormatterChain addFormatterAndParameters(StringTemplateFormatter formatter, Object... args) {
        if (Objects.nonNull(formatter)) {
            chain.add(new Entry<StringTemplateFormatter, Object[]>(formatter, args));
        }
        return this;
    }

    public StringTemplateFormatterChain addFormatterAndParameters(Pattern pattern, Function2<String, Object[], String> valueGetter, Object... args) {
        return addFormatterAndParameters(new CustomPatternStringFormatter(pattern, valueGetter), args);
    }

    public StringTemplateFormatterChain addFormatterAndParameters(String variablePattern, Function2<String, Object[], String> valueGetter, Object... args) {
        return addFormatterAndParameters(Pattern.compile(variablePattern), valueGetter, args);
    }

    public StringTemplateFormatterChain addIndexedFormatterAndParameters(Object... args) {
        return addFormatterAndParameters(new IndexStringFormatter(), args);
    }

    public StringTemplateFormatterChain addPlaceHolderFormatterAndParameters(Object... args) {
        return addFormatterAndParameters(new PlaceholderStringFormatter(), args);
    }

    public <T>StringTemplateFormatterChain addBeanBasedFormatterAndParameters(T bean) {
        return addFormatterAndParameters(new BeanBasedStyleStringFormatter(), bean);
    }

    public StringTemplateFormatterChain addMapBasedFormatterAndParameters(Map<String, Object> map) {
        return addFormatterAndParameters(new MapBasedStringFormatter(), map);
    }

    @Override
    public String format(String template, final Object... args) {
        Preconditions.checkNotNull(template);
        final Holder<String> templateHolder = new Holder<String>(template);
        Collects.forEach(chain, new Consumer<Pair<StringTemplateFormatter, Object[]>>() {
            @Override
            public void accept(Pair<StringTemplateFormatter, Object[]> pair) {
                templateHolder.set(pair.getKey().format(templateHolder.get(), pair.getValue()));
            }
        });
        return templateHolder.get();
    }
}
