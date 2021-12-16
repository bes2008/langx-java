# JSSE 规范

## 核心组件

1. KeyManager, X509KeyManager 基于KeyStore的key manager, 用于获取keystore中的 private key, cert chain, 筛选 client, server端的  alias等等。 与此同时，也提供了 KeyManagerFactory, KeyManagerFactorySpi 接口。
2. TrustManager, X509TrustManager 基于 trust keystore 的 trust manager, 用于验证 对端提供的 cert chain 是否可以信任。与此同时，也提供了 TrustManagerFactory, TrustManagerFactorySpi 接口。
3. SSLSocket, SSLServerSocket, 提供了 阻塞式 SSL socket, 具有 认证、加解密数据等功能。它有一个handshaking过程，完成了握手后，则会创建出SSLSession。



## handshake 


### 握手发生的时机
1. 通过调用 SSLSocket 中的 startHandshake 显式发起握手
2. 想要基于 SSLSocket来 read、write 应用的数据时，会隐式发起握手
3. 调用 SSLSocket#getSession 方法时，若是此时还没有一个有效的 session时，将会自动发起 握手

一旦 handshake 失败， SSLSocket 将关闭


### cipher suites




