参考：国密实验室地址：
https://www.gmssl.cn/gmssl/index.jsp?go=gmsdk

# 要通过 JCA, JCE 的 API来使用该包 ，不建议直接使用该包下的类


# 国密算法实现

该包用于提供国密算法。

1) 目前提供了基于 BC 来实现
2) 提供了 SM2, SM3, SM4 基于 JCA, JCE 的直接实现，也就是说，使用是，只需要引入该包即可。
   基本不需要直接使用该包下的类。

   推荐项目中直接使用API： Ciphers, PKIs, Signatures, MessageDigests

3) 如果只使用SM3，不需要引入该包。因为在 langx-java 中已加入 SM3的基本实现
4) src/test/java 目录下，有针对 SM2, SM3, SM4的测试用例
5) SM2 加密解密时，使用的KeyPair 的算法名称为 SM2 （其本质也是一种EC） 。
6) SM2 加密解密时，使用的Cipher默认为 SM2WithSM3
7) 



# 常见问题解决

## 1. 出现 java.security.InvalidKeyException: Illegal key size 解决方案：
1）检查使用API 时，传递的 key size 是否合理
2）检查 ${JAVA_HOME}/jre/lib/security/java.security 文件中配置项：crypto.policy
```
若存在该配置项，则将该配置项设置为 unlimited
```
3) 若2)中的配置项不存在，则需要下载响应的 unlimited 包
```
    JDK 5: https://www.oracle.com/java/technologies/java-archive-downloads-java-plat-downloads.html#jce_policy-1.5.0-oth-JPR
    JDK 6: https://www.oracle.com/java/technologies/java-archive-downloads-java-plat-downloads.html#jce_policy-6-oth-JPR
    JDK 7: https://www.oracle.com/java/technologies/javase-jce7-downloads.html
    JDK 8: https://www.oracle.com/java/technologies/javase-jce8-downloads.html
```

# 参考
1) 国密实验室地址：

https://www.gmssl.cn/gmssl/index.jsp?go=gmsdk

https://datatracker.ietf.org/doc/html/draft-shen-sm2-ecdsa-02