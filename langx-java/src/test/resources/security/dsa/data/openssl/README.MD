该目录下的数据，是使用 Linux openssl工具生成。

生成过程如下：

```shell script
# 1. 创建 DSA param 文件
openssl dsaparam -out dsa_param.pem 1024

# 2. 根据param文件创建 DSA private key
openssl gendsa -out dsa_private_key.pem dsa_param.pem

# 3. JAVA程序要用，所以需要将 生成的 mydsa.pri.pem 转为 PKCS8 格式
openssl pkcs8 -topk8 -inform PEM -in dsa_private_key.pem -outform PEM -nocrypt -out dsa_private_key_pkcs8.pem

# 4. 根据 pcks8 格式的 private key文件，生成 public key文件
openssl dsa -in dsa_private_key_pkcs8.pem -pubout -out dsa_public_key.pem

```
