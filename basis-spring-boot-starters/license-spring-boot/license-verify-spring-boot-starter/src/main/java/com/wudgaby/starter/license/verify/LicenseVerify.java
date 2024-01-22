package com.wudgaby.starter.license.verify;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/18 0018 15:31
 * @desc :
 */

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.wudgaby.starter.core.CustomLicenseManager;
import com.wudgaby.starter.core.param.CustomKeyStoreParam;
import de.schlichtherle.license.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

/**
 * License 校验、安装、卸载
 * @author wudgaby
 */
@Slf4j
@RequiredArgsConstructor
public class LicenseVerify {
    private final static String VERIFY_CACHE_KEY = "VERIFY_CACHE";
    /**
     * 证书subject
     */
    private final String subject;
    /**
     * 公钥别称
     */
    private final String publicAlias;
    /**
     * 访问公钥库的密码
     */
    private final String storePass;
    /**
     * 证书生成路径
     */
    private final String licensePath;
    /**
     * 密钥库存储路径
     */
    private final String publicKeysStorePath;
    /**
     * LicenseManager
     */
    private LicenseManager licenseManager;
    /**
     * 证书是否安装成功标记
     */
    private boolean installSuccess;

    //https://www.jianshu.com/p/00c99bcc201d
    private final LoadingCache<String, Boolean> licenseCache = Caffeine.newBuilder()
            .initialCapacity(1)
            .maximumSize(1)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .refreshAfterWrite(5, TimeUnit.MINUTES)
            .build(key -> verify());

    /**
     * 装 License 证书，读取证书相关的信息, 在 Bean 加入容器的时候自动调用
     */
    public boolean installLicense() {
        try {
            Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);
            CipherParam cipherParam = new DefaultCipherParam(storePass);
            KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class,
                    publicKeysStorePath,
                    publicAlias,
                    storePass,
                    null);
            LicenseParam licenseParam = new DefaultLicenseParam(subject, preferences, publicStoreParam, cipherParam);

            licenseManager = new CustomLicenseManager(licenseParam);
            licenseManager.uninstall();
            LicenseContent licenseContent = licenseManager.install(new File(licensePath));
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            installSuccess = true;
            licenseCache.refresh(VERIFY_CACHE_KEY);
            log.info("------------------------------- 证书安装成功 -------------------------------");
            log.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
        } catch (Exception e) {
            installSuccess = false;
            log.error("------------------------------- 证书安装失败 -------------------------------");
            log.error(e.getMessage(), e);
        }
        return installSuccess;
    }

    /**
     * 卸载证书，在 Bean 从容器移除的时候自动调用
     */
    public void unInstallLicense() {
        if (installSuccess) {
            try {
                licenseManager.uninstall();
                installSuccess = false;
                licenseCache.refresh(VERIFY_CACHE_KEY);
                log.info("------------------------------- 证书卸载成功 -------------------------------");
            } catch (Exception e) {
                log.error("------------------------------- 证书卸载失败 -------------------------------");
                log.error(e.getMessage(), e);
            }
        }
    }

    public boolean verifyFromCache() {
        return Boolean.TRUE.equals(licenseCache.get(VERIFY_CACHE_KEY));
    }

    /**
     * 证书校验
     */
    public boolean verify() {
        if (installSuccess) {
            try {
                LicenseContent licenseContent = licenseManager.verify();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                log.info(MessageFormat.format("证书有效期：{0} - {1}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
                return true;
            } catch (Exception e) {
                log.error("证书校验失败." + e.getMessage(), e);
                return false;
            }
        }
        return false;
    }
}