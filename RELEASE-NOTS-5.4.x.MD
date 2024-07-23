## 5.4.2
* 整理 longadder, forkjoinpool, completion等 api到 langx-java8tojava6模块

## 5.4.1
+ 解除 JDK低版本中 crypto.policy 的限制 （美国加密算法出口限制）
+ 修复Security.getProvider()的Bug


## 5.4.0
+ 增强Crypto-JS, 支持PBKDF2
+ 支持Files.readAsText
+ 支持docker环境检测
+ 提供AnsiX923Padding,ISO97971Padding
+ 迁移 longadder, forkjoinpool, completion等与 unsafe相关API
