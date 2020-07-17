package com.jn.langx.text.xml;

/**
 *  一个xml document中，会使用多个namespace下定义的元素。
 *  所以在document 的根元素中，通常会包含所有的会用到的namespace.
 *
 *  <pre>
 *      <project xmlns="http://maven.apache.org/POM/4.0.0"
 *          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
 *                         http://maven.apache.org/maven-v4_0_0.xsd">
 *           <groupId>com.github.fangjinuo.langx</groupId>
 *           <artifactId>langx-java</artifactId>
 *           <version>1.0.0</version>
 *      </project>
 *  </pre>
 *
 *  上面的例子中，引用了两个namespace：
 *      1）http://www.w3.org/2001/XMLSchema-instance 它的前缀是 xsi
 *      2）http://maven.apache.org/POM/4.0.0 它的前缀是 ""，也可以认为是xmlns
 *
 */
public class Namespace {
    private String prefix; // 也被称为 prefix
    private String uri;

    public Namespace(String name, String uri){
        setPrefix(name);
        setUri(uri);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
