## 摘要签名认证设计



1. #### 设计参考

```
支付宝api
https://opendocs.alipay.com/open/291/sign
```

```
阿里云api网关
https://help.aliyun.com/document_detail/29475.html?spm=a2c4g.11186623.2.19.7d864ae0ZVtces
```

```
阿里云日志服务
https://help.aliyun.com/document_detail/29012.html
```

```
阿里云域名
https://help.aliyun.com/document_detail/67699.html?spm=a2c4g.11186623.6.651.51aa25d5U0HJht
```



2. #### 签名规则

   客户端需要从Http请求中提取出关键数据，组合成一个签名串，生成的签名串的格式如下：

   ```
   HTTPMethod
   Content-Type
   Content-MD5
   Headers
   PathAndParameters
   ```

   以上字段构成整个签名串，字段之间使用\n间隔，如果Headers为空，则不需要加\n，其他字段如果为空都需要保留\n。签名大小写敏感。下面介绍下每个字段的提取规则：

   

   HTTPMethod：HTTP的方法，全部大写，比如POST

   Content-Type：请求中的Content-Type头的值，可为空

   Content-MD5：请求中的Content-MD5头的值，可为空只有在请求存在Body且Body为非Form形式时才计算							Content-MD5头，下面是Java的Content-MD5值的参考计算方式：

   ```
   String content-MD5 = Base64.encodeBase64(MD5(bodyStream.getbytes("UTF-8")));
   ```

   

   Headers用户可以选取指定的header参与签名，关于header的签名串拼接方式有以下规则：

   ​	参与签名计算的Header的Key**按照字典排序**后使用如下方式拼接

   ```
   HeaderKey1 + ":" + HeaderValue1 + "\n"\+
   HeaderKey2 + ":" + HeaderValue2 + "\n"\+
   ...
   HeaderKeyN + ":" + HeaderValueN + "\n"
   ```

   某个Header的Value为空，则使用HeaderKey+":"+"\n"参与签名，需要保留Key和英文冒号



​		PathAndParameters这个字段包含Path，Query和Form中的所有参数，具体组织形式如下：

```
Path + "?" + Key1 + "=" + Value1 + "&" + Key2 + "=" + Value2 + ... "&" + KeyN + "=" + ValueN
```

​		Query和Form参数对的Key按照字典排序后使用上面的方式拼接；

​		Query和Form参数为空时，则直接使用Path，不需要添加？；

​		参数的Value为空时只保留Key参与签名，等号不需要再加入签名；

​		Query和Form存在数组参数时（key相同，value不同的参数） ，取第一个Value参与签名计算。



#### 例子:

```http
POST /http2test/test?param1=test HTTP/1.1
host:api.aliyun.com
accept:application/json; charset=utf-8
content-type:application/x-www-form-urlencoded; charset=utf-8
x-ca-timestamp:1525872629832
date:Wed, 09 May 2018 13:30:29 GMT+00:00
user-agent:ALIYUN-ANDROID-DEMO
x-ca-nonce:c9f15cbf-f4ac-4a6c-b54d-f51abf4b5b44
content-length:33

username=xiaoming&password=123456789
```



#### 生成的正确签名串为：

```http
POST
application/json; charset=utf-8

x-ca-appkey:203753385
x-ca-nonce:c9f15cbf-f4ac-4a6c-b54d-f51abf4b5b44
x-ca-timestamp:1525872629832
/http2test/test?param1=test&password=123456789&username=xiaoming
```



#### 计算签名:

```java
String md5Result = new HmacUtils(HmacAlgorithms.HMAC_SHA_256,signatureVo.getSecret()).hmacHex(finalContent);
String signature = Base64.getEncoder().encodeToString(md5Result.getBytes(StandardCharsets.UTF_8));
```



#### 最终携带签名的整个HTTP请求:

```http
POST /http2test/test?param1=test HTTP/1.1
host:api.aliyun.com
accept:application/json; charset=utf-8
ca_version:1
content-type:application/x-www-form-urlencoded; charset=utf-8
x-ca-timestamp:1525872629832
date:Wed, 09 May 2018 13:30:29 GMT+00:00
user-agent:ALIYUN-ANDROID-DEMO
x-ca-nonce:c9f15cbf-f4ac-4a6c-b54d-f51abf4b5b44
x-ca-appKey:203753385
x-ca-signature:xfX+bZxY2yl7EB/qdoDy9v/uscw3Nnj1pgoU+Bm6xdM=
content-length:33

username=xiaoming&password=123456789
```

