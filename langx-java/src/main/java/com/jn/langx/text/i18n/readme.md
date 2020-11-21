
# 国际化
## Locale

Locale 对象包含下面几个逻辑字段：

### 1）language

目前有两套Language Code：

+ ISO 639 alpha-2，由2个字母构成
+ ISO 639 alpha-3，由3个字母构成


当一个language既有 alpha-2 code，又有alpha-3code，必须用alpha-2的。

language code 在IANA Language Subtag Registry网站上，有专门的列表。

Locale中的language字段，是大小写不敏感的，但是默认都是使用小写的。



[清单列表](https://www.iana.org/assignments/language-subtag-registry/language-subtag-registry)

###  2）script

ISO 15924 alpha-4 提供了4个字母的script code规范.

Locale 中的script 字段是大小写不敏感的，但是总是会默认使用 首字母大写，剩余3个字母小写的格式。



### 3）country

Locale 中的country字段是大小写不敏感的，但是总是会全大写的格式。

  <dd>Well-formed country/region values have
  the form <code>[a-zA-Z]{2} | [0-9]{3}</code></dd>

### 4）variant

 一般用不到

### 5）extensions

一般用不到


## ResourceBundle

一个ResourceBundle对象，代表了一个国际化文件。
同一类的国际化文件，有一个共同的名字，称为 basename

在ResourceBundle这个类里本身又有全局的cache，用于存储系统中所有的ResourceBundle。
假设我们把一个软件系统，按照功能类别进行国际化的文件进行归类。通常大致会分为如下几类：

+ 错误消息
+ 需要国际化的名词、语句

Bundle里存放有消息，在运行时，我们会从Bundle里拉取消息来使用。对于Web应用来讲，肯定要给用户返回的消息是用户期望的语言的（浏览器里可以设置，有的网站在页面上也可以进行切换）。

ResourceBundle里提供了很多的static方法：
```text
public static final ResourceBundle getBundle(String baseName);
public static final ResourceBundle getBundle(String baseName, Control control);
public static final ResourceBundle getBundle(String baseName, Locale locale);
public static final ResourceBundle getBundle(String baseName, Locale targetLocale,Control control);
public static ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader);
public static ResourceBundle getBundle(String baseName, Locale targetLocale, ClassLoader loader, Control control);
```
由上述的方法参数可知，会有4个参数：
+ basename: 就理解为ResourceBundle的basename名称
+ targetLocale: 其要找的locale
+ classloader: 默认为System ClassLoader。如果你的
+ control: 控制bundle获取的顺序等等


在ResourceBundle内部，维护了一个static的 ResourceBundle Cache，cache 的key是由 basename, locale, classloader 三元组组成的。



# I18nMessageRegistry

提供了 Message Registry API，此外提供了基于JDK ResourceBundle的默认实现：JdkResourceBundleI18nRegistry

也可以自定义其他的I18nMessageRegistry





