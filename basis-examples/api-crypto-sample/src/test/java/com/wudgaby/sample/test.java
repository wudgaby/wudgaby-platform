package com.wudgaby.sample;

import cn.hutool.crypto.symmetric.AES;
import com.wudgaby.starter.crypto.util.AESUtil;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/5 0005 14:30
 * @desc :
 */

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class test {
    //@Test
    public static void main(String[] args) throws Exception {
        String s = "{\"userId\":\"1\",\"userName\":\"test\",\"age\":20,\"info\":\"信息内容......\"}";

        AES aes = new AES("CBC", "PKCS7Padding",
                "1234567890!@#$%^".getBytes(),
                "2023070520113929".getBytes());
        AESUtil.init(aes);

        String encrypt = AESUtil.encryptHex(s);
        System.out.println("加密后:" +encrypt);
        String source = AESUtil.decrypt(encrypt);
        System.out.println("解密后:" + source);
    }
}
