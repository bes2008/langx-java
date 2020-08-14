该目录下的数据，是使用 Linux openssl工具生成。

生成过程如下：

```shell script
# 1. 创建 DSA param 文件
openssl dsaparam -outform PEM -out mydsa_param.pem 1024

# 2. 根据param文件创建 DSA private key
openssl gendsa -out mydsa_pri.pem mydsa_param.pem 

# 3. JAVA程序要用，所以需要将 生成的 mydsa.pri.pem 转为 PKCS8 格式
openssl pkcs8 -topk8 -inform PEM -in mydsa_pri.pem -outform PEM -out mydsa_pri_pkcs8.pem -nocrypt

# 4. 根据 pcks8 格式的 private key文件，生成 public key文件
openssl dsa -inform PEM -in mydsa_pri_pkcs8.pem -outform PEM -out mydsa_pub.pem

```
