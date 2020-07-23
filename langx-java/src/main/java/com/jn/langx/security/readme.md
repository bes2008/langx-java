# 1. 算法说明

https://docs.oracle.com/en/java/javase/14/security/java-security-overview1.html

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
```

### 1.2.2 Asymmetric encryption algorithm 非对称加密
非对称加密，加密、解密用的是 不同的key。

所有的 拥有 private key, public key的算法，都是非对称加密算法。这两个合起来被称为 key pair。

例如：DSA, RSA, ECC

```
非对称加密算法，用作加密时：
    用的是接收方的key pair，即发送方使用 接收方的public key加密，接收方使用接收方的 private key解密。
非对称加密算法，用作签名时：
    用的是发送方的key pair，即发送方使用 发送方的private key加密，接收方使用发送方的 public key解密。
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









