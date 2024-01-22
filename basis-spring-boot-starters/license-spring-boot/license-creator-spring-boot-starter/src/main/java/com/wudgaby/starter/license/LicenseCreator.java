package com.wudgaby.starter.license;

import com.wudgaby.starter.core.CustomLicenseManager;
import com.wudgaby.starter.core.param.CustomKeyStoreParam;
import com.wudgaby.starter.core.param.LicenseCreatorParam;
import de.schlichtherle.license.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.text.MessageFormat;
import java.util.prefs.Preferences;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/18 0018 15:24
 * @desc : 创建证书
 */
@Slf4j
@RequiredArgsConstructor
public class LicenseCreator {
    private final static X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal("CN=localhost, OU=localhost, O=localhost, L=GD, ST=SZ, C=CN");
    private final LicenseCreatorParam param;

    /**
     * 生成 License 证书
     */
    public boolean generateLicense() {
        try {
            LicenseManager licenseManager = new CustomLicenseManager(initLicenseParam());
            LicenseContent licenseContent = initLicenseContent();

            licenseManager.store(licenseContent, new File(param.getLicensePath()));

            return true;
        } catch(IllegalPasswordException illegalPasswordException) {
            log.error("证书生成失败！密码与默认策略不匹配:至少6个字符,由字母和数字组成!");
            return false;
        } catch (Exception e) {
            log.error(MessageFormat.format("证书生成失败：{0}", param), e);
            return false;
        }
    }

    /**
     * 初始化证书生成参数
     */
    private LicenseParam initLicenseParam() {
        Preferences preferences = Preferences.userNodeForPackage(LicenseCreator.class);

        //设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam privateStoreParam = new CustomKeyStoreParam(LicenseCreator.class
                , param.getPrivateKeysStorePath()
                , param.getPrivateAlias()
                , param.getStorePass()
                , param.getKeyPass());

        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , privateStoreParam
                , cipherParam);
    }

    /**
     * 设置证书生成正文信息
     */
    private LicenseContent initLicenseContent() {
        LicenseContent licenseContent = new LicenseContent();
        licenseContent.setHolder(DEFAULT_HOLDER_AND_ISSUER);
        licenseContent.setIssuer(DEFAULT_HOLDER_AND_ISSUER);

        licenseContent.setSubject(param.getSubject());
        licenseContent.setIssued(param.getIssuedTime());
        licenseContent.setNotBefore(param.getIssuedTime());
        licenseContent.setNotAfter(param.getExpiryTime());
        licenseContent.setConsumerType(param.getConsumerType());
        licenseContent.setConsumerAmount(param.getConsumerAmount());
        licenseContent.setInfo(param.getDescription());

        //扩展校验服务器硬件信息
        licenseContent.setExtra(param.getLicenseExtraParam());
        return licenseContent;
    }
}