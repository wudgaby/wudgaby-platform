package com.wudgaby.sample.license;

import cn.hutool.core.util.StrUtil;
import com.github.linyuzai.download.core.aop.annotation.Download;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.starter.core.param.LicenseCreatorParam;
import com.wudgaby.starter.license.LicenseCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/18 0018 17:32
 * @desc :
 */
@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class LicenseCreatorApplication {
    @Value("${licensePath}")
    private String licensePath;

    public static void main(String[] args) {
        SpringApplication.run(LicenseCreatorApplication.class, args);
    }

    /**
     * {
     *   "subject": "XXX公司",
     *   "privateAlias": "hw",
     *   "keyPass": "prikey123",
     *   "storePass": "pristore123",
     *   "licensePath": "D:\\license-demo\\license.lic",
     *   "privateKeysStorePath": "D:\\license-demo\\pri.keystore",
     *   "issuedTime": "2024-01-10 14:48:12",
     *   "expiryTime": "2025-12-31 00:00:00",
     *   "consumerType": "User",
     *   "consumerAmount": 1,
     *   "description": "这是证书描述信息",
     *   "licenseExtraParam": {
     *     "ipAddress": [
     *       "10.11.40.177"
     *     ],
     *     "macAddress": [
     *       "40-EC-99-09-DD-78"
     *     ],
     *     "cpuSerial": "BFEBFBFF000706E5",
     *     "mainBoardSerial": "PF2A2XTE"
     *   }
     * }
     */
    @PostMapping("/license")
    public ApiResult createLicense(@RequestBody LicenseCreatorParam param) {
        if(StrUtil.isBlank(param.getLicensePath())){
            param.setLicensePath(licensePath);
        }

        LicenseCreator licenseCreator = new LicenseCreator(param);
        return ApiResult.success(licenseCreator.generateLicense() ? "生成证书成功!" : "生成证书失败!");
    }

    @Download
    @PostMapping("/licenseDownload")
    public String licenseDownload(@RequestBody LicenseCreatorParam param) {
        if(StrUtil.isBlank(param.getLicensePath())){
            param.setLicensePath(licensePath);
        }

        LicenseCreator licenseCreator = new LicenseCreator(param);
        licenseCreator.generateLicense();
        return param.getLicensePath();
    }
}
