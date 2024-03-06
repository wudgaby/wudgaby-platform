package com.wudgaby.starter.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.wudgaby.starter.core.hdw.AbstractServerInfos;
import com.wudgaby.starter.core.hdw.LinuxServerInfos;
import com.wudgaby.starter.core.hdw.WindowsServerInfos;
import com.wudgaby.starter.core.param.LicenseExtraParam;
import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import lombok.extern.slf4j.Slf4j;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 自定义 License 管理，创建、安装、校验等
 * @author wudgaby
 */
@Slf4j
public class CustomLicenseManager extends LicenseManager {
    /**
     * XML 编码
     */
    private static final String XML_CHARSET = "UTF-8";
    /**
     * 默认 BUFFER SIZE
     */
    private static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

    public CustomLicenseManager(LicenseParam param) {
        super(param);
    }

    /**
     * 重写 License 创建
     */
    @Override
    protected synchronized byte[] create(LicenseContent content, LicenseNotary notary) throws Exception {
        initialize(content);
        this.validateCreate(content);
        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }

    /**
     * 重写 License 安装
     */
    @Override
    protected synchronized LicenseContent install(final byte[] key, final LicenseNotary notary) throws Exception {
        final GenericCertificate certificate = getPrivacyGuard().key2cert(key);

        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());
        this.validate(content);
        setLicenseKey(key);
        setCertificate(certificate);

        return content;
    }

    /**
     * 重写 License 校验
     */
    @Override
    protected synchronized LicenseContent verify(final LicenseNotary notary) throws Exception {
        GenericCertificate certificate;

        // Load license key from preferences,
        final byte[] key = getLicenseKey();
        if (null == key) {
            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
        }

        certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());
        this.validate(content);
        setCertificate(certificate);

        return content;
    }

    /**
     * 校验生成证书的参数信息
     */
    protected synchronized void validateCreate(final LicenseContent content) throws LicenseContentException {
        final Date now = new Date();
        final Date notBefore = content.getNotBefore();
        final Date notAfter = content.getNotAfter();
        if (null != notAfter && now.after(notAfter)) {
            throw new LicenseContentException("证书失效时间不能早于当前时间");
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)) {
            throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
        }
        final String consumerType = content.getConsumerType();
        if (null == consumerType) {
            throw new LicenseContentException("用户类型不能为空");
        }
    }

    /**
     * 重写 License 验证
     */
    @Override
    protected synchronized void validate(final LicenseContent content) throws LicenseContentException {
        //1. 首先调用父类的validate方法
        super.validate(content);

        //2. 然后校验自定义的License参数
        //License中可被允许的参数信息
        LicenseExtraParam licenseExtraParam = (LicenseExtraParam) content.getExtra();
        //当前服务器真实的参数信息
        LicenseExtraParam serverExtraParam = getServerInfos();

        checkExtraParam(licenseExtraParam, serverExtraParam);
    }

    /**
     * XMLDecoder 解析 XML
     */
    private Object load(String encoded) {
        try (
                BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(encoded.getBytes(XML_CHARSET)));
                XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(inputStream, DEFAULT_BUFFER_SIZE), null, null)
        ){
            return decoder.readObject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取当前服务器需要额外校验的 License 参数
     */
    private LicenseExtraParam getServerInfos() {
        //操作系统类型
        OsInfo osInfo = SystemUtil.getOsInfo();
        AbstractServerInfos abstractServerInfos;
        //根据不同操作系统类型选择不同的数据获取方法
        if (osInfo.isWindows()) {
            abstractServerInfos = new WindowsServerInfos();
        } else {
            abstractServerInfos = new LinuxServerInfos();
        }
        return abstractServerInfos.getServerInfos();
    }

    private void checkExtraParam(LicenseExtraParam licenseExtraParam, LicenseExtraParam serverExtraParam) throws LicenseContentException {
        if (licenseExtraParam != null && serverExtraParam != null) {
            //osuuid不为空时,优先校验uuid.
            if(StrUtil.isNotBlank(licenseExtraParam.getOsUuid())){
                if (!checkSerial(licenseExtraParam.getOsUuid(), serverExtraParam.getOsUuid())) {
                    throw new LicenseContentException("当前服务器的系统唯一值不匹配");
                }else{
                    return;
                }
            }

            //校验IP地址
            if (!checkIpAddress(licenseExtraParam.getIpAddress(), serverExtraParam.getIpAddress())) {
                throw new LicenseContentException("当前服务器的IP没在授权范围内");
            }

            //校验Mac地址
            if (!checkIpAddress(licenseExtraParam.getMacAddress(), serverExtraParam.getMacAddress())) {
                throw new LicenseContentException("当前服务器的Mac地址没在授权范围内");
            }

            //校验主板序列号
            if (!checkSerial(licenseExtraParam.getMainBoardSerial(), serverExtraParam.getMainBoardSerial())) {
                throw new LicenseContentException("当前服务器的主板序列号没在授权范围内");
            }

            //校验CPU序列号
            if (!checkSerial(licenseExtraParam.getCpuSerial(), serverExtraParam.getCpuSerial())) {
                throw new LicenseContentException("当前服务器的CPU序列号没在授权范围内");
            }

            //校验额外参数
            if (!checkExtra(licenseExtraParam.getExtraMap(), serverExtraParam.getExtraMap())) {
                throw new LicenseContentException("当前服务器的额外参数没在授权范围内");
            }

            if (!checkSerial(licenseExtraParam.getSystemSerial(), serverExtraParam.getSystemSerial())) {
                throw new LicenseContentException("当前服务器的系统序列号没在授权范围内");
            }
        } else {
            throw new LicenseContentException("不能获取服务器硬件信息");
        }
    }

    /**
     * 校验当前服务器的IP/Mac地址是否在可被允许的IP范围内
     */
    private boolean checkIpAddress(List<String> expectedList, List<String> serverList) {
        if (CollUtil.isEmpty(expectedList)) {
            return true;
        }
        if (CollUtil.isEmpty(serverList)) {
            return false;
        }

        return CollUtil.containsAny(expectedList, serverList);
    }

    /**
     * 校验当前服务器硬件（主板、CPU 等）序列号是否在可允许范围内
     */
    private boolean checkSerial(String expectedSerial, String serverSerial) {
        if (StrUtil.isBlank(expectedSerial)) {
            return true;
        }
        return StrUtil.equals(expectedSerial, serverSerial);
    }

    private boolean checkExtra(Map<String, Object> expectedMap, Map<String, Object> serverMap) {
        if (MapUtil.isEmpty(expectedMap)) {
            return true;
        }
        if (MapUtil.isEmpty(serverMap)) {
            return false;
        }

        //遍历expectedMap, 比较serverMap中的值是否相等
        for (Map.Entry<String, Object> entry : expectedMap.entrySet()) {
            Object serverVal = serverMap.get(entry.getKey());
            if (ObjectUtil.notEqual(entry.getValue(), serverVal)) {
                return false;
            }
        }
        return true;
    }
}