package com.wudgaby.zxing.sample;

import com.google.zxing.NotFoundException;
import com.wudgaby.platform.zxing.utils.QrCodeUtil;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName : QrCodeTest
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/7/17 15:33
 * @Desc :   TODO
 */
public class QrCodeTest {
    public static void main(String[] args) throws NotFoundException, IOException {
        QrCodeUtil.decodeQrCode(new File("D://1.jpg"));
        QrCodeUtil.decodeQrCode(new File("D://2.jpg"));

        QrCodeUtil.decodeMultiQCodes(new File("D://1.jpg"));
        QrCodeUtil.decodeMultiQCodes(new File("D://3.png"));
        QrCodeUtil.decodeMultiQCodes(new File("D://4.jpg"));
    }
}
