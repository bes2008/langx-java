
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




