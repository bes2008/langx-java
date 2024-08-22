## 5.4.4
* OsArch 支持RISCV64

## 5.4.3
* 支持 JVMCore
* 增强 Platform
* 提供 ObjectSizeEstimator

## 5.4.2
* 整理 longadder, forkjoinpool, completion等 api到 langx-java8tojava6模块
* 优化CommonServiceProvider: 优先使用ThreadContextClassLoader,加载不到时再使用特定类ClassLoader
* Hasher 服务使用 CommonServiceProvider

## 5.4.1
+ 解除 JDK低版本中 crypto.policy 的限制 （美国加密算法出口限制）
+ 修复Security.getProvider()的Bug


## 5.4.0
+ 增强Crypto-JS, 支持PBKDF2
+ 支持Files.readAsText
+ 支持docker环境检测
+ 提供AnsiX923Padding,ISO97971Padding
+ 迁移 longadder, forkjoinpool, completion等与 unsafe相关API
