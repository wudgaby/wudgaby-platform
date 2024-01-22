package com.wudgaby.starter.license.verify.listener;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.wudgaby.starter.core.exception.LicenseException;
import com.wudgaby.starter.license.verify.LicenseVerify;
import com.wudgaby.starter.license.verify.config.LicenseProp;
import com.wudgaby.starter.license.verify.enums.InstallEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.DigestUtils;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;


/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/19 0019 15:26
 * @desc : 项目启动时安装证书&定时检测lic变化，自动更替lic
 */
@Slf4j
@RequiredArgsConstructor
public class LicenseVerifyInstallListener implements ApplicationListener<ContextRefreshedEvent> {
    private final LicenseProp licenseProp;
    private final LicenseVerify licenseVerify;

    private static String licenseFileMD5;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(!FileUtil.exist(licenseProp.getLicensePath())){
            log.error("++++++++ 未找到证书文件 ++++++++");
            checkLicenseFile();
            return;
        }

        log.info("++++++++ 开始安装证书 ++++++++");
        boolean isSuccess = licenseVerify.installLicense();
        if(!isSuccess){
            checkLicenseFile();
        }
        LicenseVerifyInstallListener.licenseFileMD5 = getMd5(licenseProp.getLicensePath());
    }

    @Scheduled(cron = "0/10 * * * * ?")
    protected void timer(){
        log.info("++++++++ 定时检测证书 ++++++++");

        if(!FileUtil.exist(licenseProp.getLicensePath())){
            log.error("++++++++ 未找到证书文件 ++++++++");
            licenseVerify.unInstallLicense();
            checkLicenseFile();
            return;
        }

        String readMd5 = getMd5(licenseProp.getLicensePath());
        if(!StrUtil.equals(LicenseVerifyInstallListener.licenseFileMD5, readMd5)){
            log.info("++++++++ license变化, 重新安装证书 ++++++++");
            licenseVerify.installLicense();
            LicenseVerifyInstallListener.licenseFileMD5 = readMd5;
        }
    }

    private String getMd5(String filePath){
        try(FileInputStream is = new FileInputStream(ResourceUtils.getFile(filePath)))
        {
            byte[] data = new byte[is.available()];
            int i = is.read(data);
            return DigestUtils.md5DigestAsHex(data);
        } catch (IOException e) {
            log.error("获取文件的md5失败." + e.getMessage(), e);
        }
        return "";
    }

    private void checkLicenseFile(){
        boolean isFromFile = licenseProp.getInstallType() == InstallEnum.FILE;
        if(isFromFile){
            System.exit(1);
            throw new LicenseException("软件授权失败,退出应用");
        }
    }

}