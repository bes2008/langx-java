package com.jn.langx.text.transform;

import com.jn.langx.Transformer;
import com.jn.langx.util.collection.Collects;

/**
 * TextCaseTransformer接口继承自Transformer接口，专门用于字符串到字符串的转换操作
 * 它定义了一个转换字符串的方法，允许实现者提供自定义的字符串转换逻辑
 */
public abstract class AbstractTextCaseTransformer implements Transformer<String, String> {

    static final String[] default_delimiters = new String[]{"\t"," ","\r","\n","-","_"};

    /**
     * 转换输入的字符串
     *
     * @param text 待转换的原始字符串
     * @return 转换后的字符串
     */
    @Override
    public String transform(String text){
        // 前置处理
        StringBuilder newText = new StringBuilder();
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ((Character.isDigit(c)&& count>0) || Character.isLetter(c)) {
                newText.append(c);
                count++;
            }
            else if(count>0 && Collects.contains(default_delimiters,c+"")){
                newText.append(" ");
                count++;
            }
        }
        return transformInternal(newText.toString());
    }

    protected abstract String transformInternal(String text);
}
