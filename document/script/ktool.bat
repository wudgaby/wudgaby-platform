@echo off
rem 解决中文乱码
chcp 65001

rem 存储库名称
set var_keystore=hw.keystore
rem 密钥对的名称
set var_alias=hw
rem 密钥密码
set var_keypass=778899
rem 有效期(天)
set var_validity=3650
rem 存储库密码
set var_storepass=112233
rem 密钥库类型
set var_storetype=PKCS12

rem 描述
set var_dname= "C=CN,ST=GD,L=SZ,O=HW,OU=helloworld,CN=developer"
rem 密钥算法
set var_keyalg=EC
rem 密钥大小
set var_keysize=521
rem 签名算法
set var_sigalg=SHA256withECDSA
rem 证书名称
set var_cert_file=hw.cer


:main
echo 1.新建密钥对
echo 2.导出cer证书文件
echo 3.导入证书
echo 4.列表
echo 5.退出

set /p opt=输入选项:

if %opt% == 1 call:genkeypair
if %opt% == 2 call:exportcert
if %opt% == 3 goto import
if %opt% == 4 goto list
if %opt% == 5 goto end

echo 无效选项
goto main

:genkeypair
rem 新建密钥对
call keytool -genkeypair -alias %var_alias% -keypass %var_keypass% -storepass %var_storepass% -storetype %var_storetype% ^
        -dname %var_dname% -keyalg %var_keyalg% -keysize %var_keysize% -sigalg %var_sigalg% -validity %var_validity% -keystore %var_keystore% -v
goto:eof

:exportcert
rem 导出cer证书文件.公钥. cer转pem
rem 私钥是无法从证书库中导出的.如果你特别需要私钥或是私钥字符串,只能考虑用编程的方式从密钥库文件中去获取
call keytool -exportcert -keystore %var_keystore% -file %var_cert_file% -alias %var_alias%
goto:eof

:import
rem 导入证书
call keytool -import -alias "publicCert" -file "certfile.cer" -keystore "publicCerts.keystore"
goto:eof

:importkeystore
rem 导入证书
rem call keytool -importkeystore -srckeystore %var_keystore% -destkeystore fanyfull.p12 -deststoretype PKCS12 -srcalias fanyfull -deststorepass ffsp123456 -destkeypass ffkp123456
goto:eof


:list
rem 证书列表
call keytool -list -v -alias %var_alias% -storepass %var_storepass% -keystore %var_keystore%
goto:eof

:end
echo DONE
exit 0


keytool -genkeypair -alias hw -keypass 778899 -storepass 112233 -storetype PKCS12 ^
        -keyalg EC -keysize 521 -sigalg SHA256withECDSA -validity 3650 -keystore "hw.keystore" ^
        -dname "CN=localhost, OU=localhost, O=localhost, L=GD, ST=SZ, C=CN" -v

keytool -list -v -alias hw -storepass 112233 -keystore "hw.keystore"

keytool -exportcert -keystore "hw.keystore" -file hw.cer -alias hw
keytool -import -alias "publicCert" -file hw.cer -keystore "publicCerts.keystore"

keytool -importkeystore -srckeystore "hw.keystore" -srcalias hw -destkeystore fanyfull.p12 -deststoretype PKCS12  -deststorepass 123456 -destkeypass 123456

keytool -genkeypair -storetype PKCS12 -alias raviSSL -keyalg EC -keysize 521 -sigalg SHA256withECDSA ^
-dname "CN=Hivesplace,OU=oAuth2.0 Authorization Server,O=123.com,L=Guangzhou,S=Guangdong,C=CN" ^
-keystore raviSSL.keystore -keypass 123456789 -storepass 123456789 -validity 36500 -v
