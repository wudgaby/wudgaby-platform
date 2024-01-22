## license授权机制的原理
1. 生成密钥对，包含私钥和公钥。
2. 授权者保留私钥，使用私钥对授权信息诸如使用截止日期，mac 地址等内容生成 license 签名证书。
3. 公钥给使用者，放在代码中使用，用于验证 license 签名证书是否符合使用条件。


```
truelicense. 密码要求6位字母+数字. 并且keysize=1024
#生成密钥对（私钥）提供给生成证书使用
keytool -genkeypair -alias hw -keypass "priwd123" -storepass "pubwd123" ^
        -validity 3650 -keystore "pri.keystore" ^
        -keysize 1024 ^
        -dname "CN=localhost, OU=localhost, O=localhost, L=GD, ST=SZ, C=CN"
        
#-storetype PKCS12 -keyalg EC -keysize 521 -sigalg SHA256withECDSA 

#导出证书       
keytool -exportcert -keystore "pri.keystore" -file "hw.cer" -alias "hw" -storepass "pubwd123"

#生成公钥库（公钥）提供给证书认证使用
keytool -import -alias "pubCert" -file "hw.cer" -keystore "pub.keystore" -storepass "pubwd123" -noprompt


keytool -importkeystore -srckeystore "pri.keystore" -srcalias hw 
-destkeystore "pub.keystore" -deststoretype PKCS12 -deststorepass "pubstore123" -destkeypass "pubkey123"

```