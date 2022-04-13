[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/fangjinuo/langx-java/blob/master/LICENSE)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3d8c8c9680234698b04819059c9cd6c3)](https://www.codacy.com/manual/fs1194361820/langx-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=fangjinuo/langx-java&amp;utm_campaign=Badge_Grade)

[![JDK](https://img.shields.io/badge/JDK-1.6+-green.svg)](https://www.oracle.com/technetwork/java/javase/downloads/index.html)


[javadoc](https://apidoc.gitee.com/fangjinuo/langx-java)
[教程](https://fangjinuo.gitee.io/docs/)

[![maven](https://img.shields.io/badge/maven-v4.5.0.green.svg)](https://search.maven.org/search?q=g:com.github.fangjinuo.langx%20AND%20v:4.5.0)

## [GitHub地址](https://github.com/fangjinuo/langx-java)
## [Gitee地址](https://gitee.com/fangjinuo/langx-java)


# langx-java
Java lang extensions

## Features
+ Lang extensions, for examples: Bytes, Chars, Arrays, Numbers, Dates, Throwables, Filenames, Files, FileSystems ...
+ Collection extensions:
    + Supports Stream API for Java 6+
    + Adapter any object to Iterable
    + Supports NonAbsentMap, using it, the map.get(key) will not return the null
    + Supports NonDistinctTreeSet, NonDistinctTreeMap, just sort 
    + Supports Tuple
    + Supports WheelQueue, CopyOnWriteHashMap
    + NamedInheritableThreadLocal
    + IdentityMap
    + IdentitySet
    + WeakIdentityHashMap
    + ConcurrentReferenceHashMap
+ Classpath API
    + DirectoryClasspath
    + JarFileClasspath
    + WarFileClasspath
    + JarDirectoryClasspath
    + ClassLoaderClasspath
    + ClassClasspath
    + ResourceLoaderClasspath
    + ByteArrayClasspath
    + InputStreamClasspath
+ Differ API
    + Collection Differ
    + Map Differ
+ Cache
    + Local Cache
      + LRUCache
      + FIFOCache
+ Accessor API
    + Tuple
    + Environment Accessor
    + Properties Accessor
    + Map Accessor
    + StringMap Accessor
    + Field Accessor
    + Array Accessor 
    + HTTP Query String Accessor
+ ValueGetter
    + ArrayValueGetter
    + CompositeValueGetter
    + IterableValueGetter
    + LiteralValueGetter
    + MapValueGetter
    + MemberValueGetter
    + PipelineValueGetter
    + StreamValueGetter
+ Reflect utilities
    + Modifiers
    + Reflects
    + Types
    + Primitives
+ timing
    + HashedWheelTimer
    + Linux NTP
    + Cron
    + Joda-time
    + Clock
    + StopWatch
+ JavaScript in Java
+ IdGenerator
    + AutoIncrementIdGenerator
    + Base64IdGenerator
    + SimpleIdGenerator
    + UuidGenerator
    + SnowflakeIdGenerator
+ Proxy
    + AOP
    + delegate
+ Base64 for Java 6+  
+ text
    + CSV
    + Pinyin
    + StringTemplate
      + placeholder: {}
      + index:       {0}, {1}
      + C style:     %d, %s ...
      + bean :      ${fieldName1}, ${fieldName2}
      + map:         ${key1}, ${key2}
+ Null judge is unnecessary
+ Environment
    + SystemEnvironment
+ Event API  
+ Comparators
+ Parse Hosts File
+ Codec
+ ClassPath Scanner
+ Local JNDI
+ Security checksum
    + message digest
        + MD2
        + MD4
        + MD5
        + SHA-1
        + SHA-224, SHA-256, SHA-384, SHA-512
        + SHA3-224, SHA3-256, SHA3-384, SHA3-512
        + Whirlpool
    + Asymmetrics
    + Symmetrics
    + Signatures
    + GM
        + SM2
        + SM3
        + SM4
+ Radix converter
+ JMX management API
+ File Filter
    + ExecutableFilter
    + ExistsFileFilter
    + FilenamePrefixFilter
    + FilenameSuffixFilter
    + IsDirectoryFileFilter
    + IsFileFilter
    + IsHiddenFileFilter
    + IsSymlinkFileFilter
    + ReadableFileFilter
    + ReadonlyFileFilter
    + ValidFilenameFilter
    + WriteableFileFilter
+ Lifecycle API
+ ClassParser
+ Converter Service
+ BloomFilter
+ IO Resource Load API
+ Common Configuration API
+ Multiple Level Jar URLStreamHandler
+ Escaper API
+ Delimiter Channel
。。。


## Contact:
如果遇到问题，可以在Github上提出issue, 也可以在QQ群里询问。

QQ Group: 750929088   
![QQ Group](https://github.com/fangjinuo/sqlhelper/blob/master/_images/qq_group.png)

## 鸣谢
最后，感谢 Jetbrains 提供免费License，方便了开源项目的发展。

[![Jetbrains](https://github.com/fangjinuo/sqlhelper/blob/master/_images/jetbrains.png)](https://www.jetbrains.com/zh-cn/)

