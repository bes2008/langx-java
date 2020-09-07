## Java Secure Socket Extension (JSSE) 

https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html

SSLContext ： 用于创建 SSLSocket, SSLEngine。创建SSLContext时，需要提供KeyManager, TrustManager。


+ KeyManager :  提供证书、私钥。提供的证书将被发送到 peer。
+ TrustManager： 用于验证 peer 发来的证书是否可信。

 
如果是在客户端使用SSLContext，只认证服务端是否合法，则只需要创建TrustManager

https://segmentfault.com/a/1190000002963044