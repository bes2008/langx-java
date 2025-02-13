/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.langx.configuration;

import java.io.InputStream;

/**
 * InputStreamConfigurationParser接口用于定义解析输入流中配置信息的标准方法
 * 它扩展了ConfigurationParser接口，专门针对InputStream类型的输入进行配置解析
 *
 * @param <T> 表示解析得到的配置类型，必须是Configuration接口的实现类
 */
public interface InputStreamConfigurationParser<T extends Configuration> extends ConfigurationParser<InputStream, T> {
    /**
     * 设置解析输入流时使用的编码方式
     * 这对于正确解析文本配置信息至关重要，因为不同的编码方式可能会得到不同的文本内容
     *
     * @param encoding 字符编码方式，例如"UTF-8"、"ISO-8859-1"等
     */
    void setEncoding(String encoding);

    /**
     * 重写parse方法，专门处理InputStream类型的输入
     * 将输入流解析为指定类型的配置对象
     *
     * @param input 输入流，包含待解析的配置信息
     * @return 解析得到的配置对象，类型为T
     */
    @Override
    T parse(InputStream input);
}
