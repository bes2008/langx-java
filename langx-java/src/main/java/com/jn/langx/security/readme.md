# 1. 算法说明

https://docs.oracle.com/en/java/javase/14/security/java-security-overview1.html
https://cryptobook.nakov.com/


## 1.1 Message Digest Algorithm（消息摘要算法）
```
  1. 可以对任意长度的数据进行操作
  2. 输出长度固定的hash value
  3. 不可逆，即不能根据输出内容反推输入内容。
```
  
例如：MD2, MD4, MD5, SHA, SHA2, SHA5
```
所有的消息摘要算法，都要事先 MessageDigestSpi接口，才能通过MessageDigest工具类来使用。
```

## 1.2 Encryption Algorithm (加密算法)
用途：保证接收方接收到的是 完整、无误 的数据。加密算法有两类：```对称加密、非对称加密```。

### 1.2.1 Symmetric encryption algorithm 对称加密算法
对称加密，即加密、解密时用的是同一个key，所以它是个 share key。

JDK中提供的对称加密算法有：
```
    1）DESede
    2）DES
    3）AES
    4）Blowfish
    5）3DES
    6）RC系列（RC2, RC4, RC5）
```

### 1.2.2 Asymmetric encryption algorithm 非对称加密
非对称加密，加密、解密用的是 不同的key。

所有的 拥有 private key, public key的算法，都是非对称加密算法。这两个合起来被称为 key pair。

例如：DSA, RSA, EC, DH

```
非对称加密算法，用作加密时：
    用的是接收方的key pair，即发送方使用 接收方的public key加密，接收方使用接收方的 private key解密。
非对称加密算法，用作签名时：
    用的是发送方的key pair，即发送方使用 发送方的private key加密，接收方使用发送方的 public key解密。


非对称加密算法，在PKCS系列规范中（定义私钥的），PKCS#1 是针对RSA的，PKCS #8是一种通用的规范，DSA、RSA、ECC都可使用。
1）在Java中，RSA、DSA、ECC 等都采用 PKCS #8 规范格式。
2）在Python中，对于RSA，默认采用 PKCS#1规范。

```    


## 1.3 Digit Signature Algorithm（数字签名算法）
用途：保证数据完整性、确保数据来源可信。

用的是发送方的密钥，即发送方使用发送方的私钥加密，接收方使用发送方的公钥解密
	
例如：DSA, RSA, ECDSA, DH		


# 2. Message Digest 算法使用说明


# 3. 加密算法的 Key 管理

加密算法必须有Key的参与

Key主要分为 ：
	Public Key: 公钥，公开的让大家可以用的key，属于非对称加密的key
	Private Key： 私钥，不公开的，只有自己可用的key，属于非对称加密的key
	Secret Key：对称加密 key


# 4. PKCS 标准

参考：https://www.cnblogs.com/lsgxeva/p/11701011.html

https://blog.csdn.net/qq_39385118/article/details/107510032


# 5. 国密算法
1. SM1
    对称加密算法，类似于 AES
```
    该算法是国家密码管理部门审批的 SM1 分组密码算法 , 分组长度和密钥长度都为 128 比
特，算法安全保密强度及相关软硬件实现性能与 AES 相当。
    该算法不公开，仅以 IP 核的形式存在于芯片中。采用该算法已经研制了系列芯片、智能 IC 卡、智能密码钥匙、加密卡、加 密
机等安全产品，广泛应用于电子政务、电子商务及国民经济的各个应用领域（包括国家政 务
通、警务通等重要领域）。
```
2. SM2
    基于EC进行改造的非对称加密算法.
    ```
   它是基于椭圆曲线密码的公钥密码算法标准，其秘钥长度256bit，包含数字签名、**交换和公钥加密，用于替换RSA/DH/ECDSA/ECDH等国际算法。可以满足电子认证服务系统等应用需求，由国家密码管理局于2010年12月17号发布。
   
   SM2采用的是ECC 256位的一种，其安全强度比RSA 2048位高，且运算速度快于RSA。
    ```
    
    算法标准下载地址：https://www.oscca.gov.cn/sca/xxgk/2010-12/17/content_1002386.shtml
    
3. SM3
```
用于替代MD5/SHA-1/SHA-2等国际算法，适用于数字签名和验证、消息认证码的生成与验证以及随机数的生成，
可以满足电子认证服务系统等应用需求，于2010年12月17日发布。

它是在SHA-256基础上改进实现的一种算法，采用Merkle-Damgard结构，消息分组长度为512bit，输出的摘要值长度为256bit。

```   
4. SM4
```
跟SM1类似，是我国自主设计的分组对称密码算法，用于替代DES/AES等国际算法。SM4算法与AES算法具有相同的**长度、分组长度，都是128bit。
于2012年3月21日发布，适用于密码应用中使用分组密码的需求。
```
5. SM7
```
该算法没有公开。SM7适用于非接IC卡应用包括身份识别类应用(门禁卡、工作证、参赛证)，票务类应用(大型赛事门票、展会门票)，
支付与通卡类应用(积分消费卡、校园一卡通、企业一卡通、公交一卡通)。
```

6. SM9
```
用椭圆曲线对实现的基于标识的数字签名算法、**交换协议、**封装机制和公钥加密与解密算法，
包括数字签名生成算法和验证算法，并给出了数字签名与验证算法及其相应的流程。并提供了相应的流程。可以替代基于数字证书的PKI/CA体系。

SM9主要用于用户的身份认证。据新华网公开报道，SM9的加密强度等同于3072位**的RSA加密算法，于2016年3月28日发布。
```


上述算法下载地址：
1）http://www.scctc.org.cn/templates/Download/index.aspx?nodeid=71
2）http://gmssl.org/



# 算法分析

加密算法的基本操作：移位、置换、编码
```text
移位：按照规则改变内容
置换：打乱字母的顺序


移位和置换都是可恢复的。

```










