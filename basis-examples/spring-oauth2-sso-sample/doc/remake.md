```cmd
#jks
keytool -genkeypair -alias myjks -keyalg RSA -validity 3650 -keystore my.jks
keytool -genkeypair -alias serverkey -keypass 123456 -storepass 123456 
-dname "C=CN,ST=GD,L=SZ,O=demo,OU=dev,CN=demo.com" -keyalg RSA -keysize 2048 -validity 3650 -keystore server.keystore

keytool -exportcert -keystore my.jks -file my.cer -alias myjks 
```

```
http://ssoserver.cn:8080/oauth2/authorize?response_type=code&client_id=my_client&scope=read%20write&redirect_uri=http://127.0.0.1:9000
```

### 教程参考
```
https://blog.csdn.net/tu_wer/article/details/124872706
https://blog.csdn.net/ACE_U_005A/article/details/128851814

https://github.com/spring-projects/spring-authorization-server/tree/0.4.x
```

## 问题汇总
```
问题1：
org.springframework.security.oauth2.core.OAuth2AuthenticationException: [authorization_request_not_found]

产生该问题的主要原因本机测试中的授权中心和客户端都是采用localhost主机名，对于浏览器来说同一个域名，
导致jsessionId在跳转到授权中心前的值和授权完毕后跳转回来的值不一致，浏览器认为是同一个域名所以JSessionID会被覆盖，
导致在客户端从session中获取保存的authorizationRequest时获取不到，authorizcationRequest存放在session的Attribute中，key为JSessionID; 
处理该问题方法就是让他们两个处于不同的域名或者ip，可以修改hosts增加一个域名。本文中OAuth2Server直接使用的局域网ip地址也可以解决这个问题，OAuth2Client使用localhost。

问题2:
[invalid_user_info_response] An error occurred while attempting to retrieve the UserInfo Resource from 'http://ssoserver.cn:8080/oauth2/user': response contains invalid content type 'text/html;charset=UTF-8'. The UserInfo Response should return a JSON object (content type 'application/json') that contains a collection of name and value pairs of the claims about the authenticated End-User. Please ensure the UserInfo Uri in UserInfoEndpoint for Client Registration 'myClient' conforms to the UserInfo Endpoint, as defined in OpenID Connect 1.0: 'https://openid.net/specs/openid-connect-core-1_0.html#UserInfo'
spring security配置 oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt) 需要放到DefaultSecurityConfig. 往前放.因为经过第一个认证就被拦截.

```