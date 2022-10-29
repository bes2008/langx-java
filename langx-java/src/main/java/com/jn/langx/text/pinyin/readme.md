## 拼音

虽然我们可能天天都在用汉字，但中文的发音，拼音的读写，其实没有我们想象中那么单一。

关于中文的拼音的读写，有多种形式。官网：http://www.pinyin.info/

https://unicode-table.com/en/


# 1. 罗马化系统 与 其他系统
    中文在进行现代化时，为了更方便的让西方人理解，西方人以及部分中国人，搞起了罗马化的系统。

+ 罗马化系统
    + Hanyu Pinyin
    + MPS2
    + Gwoyeu Romatzyh (Guoyu Luomazi)
    + Sin Wenz (新文字)
    + Tongyong Pinyin
    + Wade-Giles
    + Yale
+ 其他
    + zhuyin fuhao (注音符号)
    
这些拼音系统的比较：http://www.pinyin.info/romanization/compare/tongyong.html


有这么多的拼音系统，我们通常采用的是 Hanyu Pinyin 系统。


# 2. 声母 (initials )和韵母(finals)

http://www.pinyin.info/rules/initials_finals.html


# 3. Java版本的Pinyin库实现方案

## Pinyin4j (com.belerweb:pinyin4j)
    1) 支持6种罗马化系统：Hanyu Pinyin, MPS2, Gwoyeu Romatzyh, Tongyong, Wade-Giles, Yale
    2) License: BSD
    3) 实现方案：
        + 定义了每一个汉字的 Hanyu pinyin, 带声调的，声调不是在元音字母上面，而是放在整个拼音的后面 （支持 20903 个汉字）
        + 维护了Hanyu pinyin 的每一个拼音与其他的罗马化系统拼写的映射表
                     
    
## Houbb pinyin (com.github.houbb:pinyin)
    1) 只支持Hanyu Pinyin系统
    2) License： Apache 2.0
    3) 实现方案：
        + 定义了每一个汉字的 Hanyu pinyin, 带声调的 （支持 41451 个汉字）
        + 定义了一些常用词汇、成语 的发音 （可以避免多音字时的转换错误，支持 42987个）
        + 定义了 5个元音字母的声调

## duguying-pinyin (net.duguying.pinyin:pinyin)
    1) 只支持Hanyu Pinyin系统
    2) License： MIT
    3) 实现方案：
        + 定义了每一个汉字的 Hanyu pinyin, 带声调的 （支持 6763 个汉字）
        + 定义了一些常用词汇、成语 的发音 （可以避免多音字时的转换错误，支持 41119个）
        
## mynlp-pinyin (com.mayabot.mynlp:mynlp-pinyin)
    1) 只支持Hanyu Pinyin系统
    2) License： Apache 2.0
    3) 实现方案：
        + 定义了每一个汉字的 Hanyu pinyin, 带声调的 （支持 6763 个汉字）
        + 支持有声调，无声调结果输出
        + 定义了一些常用词汇、成语 的发音 （可以避免多音字时的转换错误，支持 41119个）      
    4) 该公司还提供了分词支持
    5) 支持 Kotlin   

## nlp-lang (org.nlpcn:nlp-lang)
    1) 只支持Hanyu Pinyin系统
    2) License: Apache 2.0
    3) 实现方案：
        + 定义了每一个汉字的 Hanyu pinyin, 带声调的 （支持 6763 个汉字）
        + 定义了一些常用词汇、成语 的发音 （可以避免多音字时的转换错误，支持 41119个）
    4) 支持简繁互转
    5) elasticsearch 的中文分词是基于它来做的        

## TinyPinyin (io.github.biezhi:TinyPinyin)
    1) 只支持 Hanyu Pinyin系统
    2) License： Apache2.0
    3) 实现方案：
        + 定义了常用词汇发音等 （支持 2594个）
        + 不支持声调
    4) 优点：
        + 快，内存占用少
        + 手机端应用可以用一下

## JPinyin (com.github.stuxuhai:jpinyin)
    1) 只支持 Hanyu Pinyin 系统
    2) License：GPL
    3) 实现方案：
        + 定义了每一个汉字的 Hanyu pinyin, 带声调的（支持 20903 个汉字）
        + 定义了一些多音字发音 （可以避免多音字时的转换错误，支持 828个）
    4) 支持简繁互转

## bopomofo4j (com.rnkrsoft.bopomofo4j:bopomofo4j)
    1) 只支持 Hanyu Pinyin 系统
    2) License：Apache 2.0
    3) 实现方案：
        + 定义了每一个汉字的 Hanyu pinyin, 带声调的（支持 20903 个汉字）
        + 定义了一些多音字发音 （可以避免多音字时的转换错误，支持 856个）
    4) 支持简繁互转 (2534 个字)
    
    
## 选择

Houbb pinyin, duguying-pinyin 他们的功能基本一样，duguying-pinyin支持的汉字、词汇等都比 houbbpinyin少，所以这两个中，可以优先选择 houbb 。

从功能上来看，目前的Pinyin库，都能满足简单的需求，
从字库上来看，其实根本不需要那么多的字库，原因很简单的：

+ 汉语中，有绝大部分的汉字都是只有一个发音的，多音字占少数
+ 多音字则是在不同的语境，不同的词汇组合上，发出不同的音而已

所以合理的实现是这样的：
+ 维护一个单音的map（字-拼音）
+ 维护一个多音的map(字-拼音 list)
+ 维护一个多音字的词汇表，用 trie 或者用 多key的hashmap (第一个key是多音字本身)。

但实际情况下，如果只有上述3个结构的话，在处理多音字时，效率会很低。需要引入分词算法，没有分词算法是不准确的。
所以，通常的做法是维护的是短语集合。短语集合要区分好常用，和领域知识。通过领域来进行语义切分


```text
    chinese_surname.dict 中国姓氏大全，单姓、复姓
    idiom.dict 成语词典
    chinese_punctuation_symbol.dict 中文标点符号
    multiple_yin_phrase.dict 多音字大全，容易读错的且是常用的要放在这里面
```


    