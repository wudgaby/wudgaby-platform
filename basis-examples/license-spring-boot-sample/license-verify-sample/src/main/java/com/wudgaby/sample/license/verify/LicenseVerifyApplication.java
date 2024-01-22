package com.wudgaby.sample.license.verify;

import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.starter.core.hdw.AbstractServerInfos;
import com.wudgaby.starter.core.hdw.LinuxServerInfos;
import com.wudgaby.starter.core.hdw.WindowsServerInfos;
import com.wudgaby.starter.license.verify.LicenseVerify;
import com.wudgaby.starter.license.verify.config.LicenseProp;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/18 0018 17:33
 * @desc :
 */
@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class LicenseVerifyApplication {
    private final LicenseVerify licenseVerify;
    private final LicenseProp licenseProp;

    public static void main(String[] args) {
        SpringApplication.run(LicenseVerifyApplication.class, args);
    }

    @GetMapping("/serverinfo")
    public ApiResult getServerInfo(@RequestParam(required = false) String osName){
        if(StrUtil.isBlank(osName)){
            osName = SystemUtil.getOsInfo().getName();
        }

        AbstractServerInfos abstractServerInfos;
        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        }else{
            abstractServerInfos = new LinuxServerInfos();
        }
        return ApiResult.success(abstractServerInfos.getServerInfos());
    }

    @PostMapping("/license/install")
    public ApiResult fileInstall() {
        licenseVerify.installLicense();
        return ApiResult.success(licenseVerify.verify() ? "验证通过!" : "验证未通过!");
    }

    @PostMapping("/license/import")
    public ApiResult importInstall(@RequestParam("licenseFile") MultipartFile licenseFile) throws IOException {
        licenseFile.transferTo(new File(licenseProp.getLicensePath()));

        licenseVerify.installLicense();
        return ApiResult.success(licenseVerify.verify() ? "验证通过!" : "验证未通过!");
    }

    @GetMapping("/hello")
    public ApiResult hello() {
        return ApiResult.success("你好");
    }

}
