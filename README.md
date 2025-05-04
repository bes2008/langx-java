[![License](https://img.shields.io/badge/license-Apache2.0-green.svg)](https://github.com/fangjinuo/langx-java/blob/master/LICENSE)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3d8c8c9680234698b04819059c9cd6c3)](https://www.codacy.com/manual/fs1194361820/langx-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=fangjinuo/langx-java&amp;utm_campaign=Badge_Grade)

[![JDK](https://img.shields.io/badge/JDK-1.6+-green.svg)](https://www.oracle.com/technetwork/java/javase/downloads/index.html)


[javadoc](https://apidoc.gitee.com/fangjinuo/langx-java)
[教程](https://fangjinuo.gitee.io/docs/)

[![maven](https://img.shields.io/badge/maven-v5.2.10.green.svg)](https://search.maven.org/search?q=g:com.github.fangjinuo.langx%20AND%20v:5.2.10)

## [GitHub地址](https://github.com/fangjinuo/langx-java)
## [Gitee地址](https://gitee.com/fangjinuo/langx-java)

# [OpenSource Licenses](https://opensource.org/licenses/)

# langx-java
Java lang extensions

## Features
+ Utils
  + Lang extensions, for examples: Bytes, Chars, Arrays, Numbers, Dates, Throwables, Filenames, Files, FileSystems ...
  + Beans, ModelMapper
  + Bits
  + Bloom Filter
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
      + Table
      + Forwarding API
      + MultiValueMap
      + Sequence
      + Pushback
      + Queue
      + Stack
      + Tree
      + Trie
      + Differ API
        + Collection Differ
        + Map Differ
  + Comparator
  + concurrent
    + async 
      + future listeners
      + Promise
    + executor
    + lock
    + thread local factory
    + CommonTask, CommonThreadFactory
    + Threads
  + converter
  + datetime
    + DateTimeParser
    + DateTimeFormatter
  + enums
    + CommonEnum
    + Enums
  + escape
  + function API
  + hash
    + CRC
    + FNV
    + Murmur
  + IdGenerator
    + AutoIncrementId
    + Base64Id
    + SimpleId
    + UUID
    + ShortUUID
    + SnowflakeId
    + NanoId
    + Sqids
    + ULID
    + VMID
  + io
    + Bytes
    + file
    + Charsets
    + Files
    + Channels
    + unicode
  + jar
    + Manifests
    + MultipleLevel jar
  + jni
  + logging
  + matchexp
  + math
  + money
  + net
    + CIDR
    + Hosts
    + http
    + UriBuilder
    + port
  + ValueGetter
+ Classpath API
+ Cache
    + Local Cache
      + LRUCache
      + FIFOCache
+ Accessor API

+ Reflect utilities
+ timing
    + HashedWheelTimer
    + Linux NTP
    + Cron
    + Scheduling
    + Clock
    + StopWatch
+ JavaScript in Java

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
      + bean :       `${fieldName1}`, `${fieldName2}`
      + map:         `${key1}`, `${key2}`
+ Null judge is unnecessary
+ Environment
    + SystemEnvironment
+ Event API  
+ Comparators
+ Parse Hosts File
+ Codec
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
+ Lifecycle API
+ ClassParser
+ IO Resource Load API
+ Common Configuration API

。。。


## Contact:
如果遇到问题，可以在Github上提出issue, 也可以在QQ群里询问。

QQ Group: 750929088   
![QQ Group](https://github.com/fangjinuo/sqlhelper/blob/master/_images/qq_group.png)

## 鸣谢
最后，感谢 Jetbrains 提供免费License，方便了开源项目的发展。

[![Jetbrains](https://github.com/fangjinuo/sqlhelper/blob/master/_images/jetbrains.png)](https://www.jetbrains.com/zh-cn/)

