## 5.3.13 （此版本废弃，全部并入 5.4.0）
+ 支持Files.readAsText
+ 支持docker环境检测
+ 提供AnsiX923Padding,ISO97971Padding
+ 移除 longadder, forkjoinpool, completion等与 unsafe相关API

## 5.3.12
+ 整理 xml 相关的 customizer接口
+ 优化Xmls工具，避免相关安全漏洞

## 5.3.11
+ 移除ans1支持
+ 整理 xml 相关的 customizer接口
+ 移除空类 IPFilter
+ 移除 cvs 支持

## 5.3.10 （此版本废弃）
+ 移除ans1支持
+ 整理 xml 相关的 customizer接口
+ 移除空类 IPFilter
+ 移除 cvs 支持


## 5.3.9
+ 提供 StringCodec 接口，并提供 PunycodeCodec
+ 提供 IPv4 子网掩码合法性验证
+ 提供 bytes -> string中的转换工具 Stringifys
+ Retry API 提供 WaitStrategy 接口
+ 将 unsafe相关包迁移到 io.github.bes2008.solution.junsafe:junsafe:1.0.0
+ 安全相关：
  + 支持 argon2  
  + 支持 scrypt
  + 支持凯撒密码
  + 支持莫尔斯电码
  + 提供 PBKDF API
  + 提供对 Crypto-JS 中的AES算法的支持
  + 提供对 Django中的CSRF算法的支持
  + 增强 MessageDigestHasher,支持自定义 SaltGenerator
  

## 5.3.8
+ 增强 PropertySet 接口

## 5.3.7
+ BugFix: 支持 clean path时，一并转换为unix风格

## 5.3.6
+ 提供 PropertySet 接口
+ 增强 SM2,SM4 的支持

## 5.3.5
+ 提供 常用检验工具
