package com.wudgaby.starter.core.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/18 0018 12:03
 * @desc : 扩展参数
 */
@Data
public class LicenseExtraParam implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 可被允许的IP地址
     */
    private List<String> ipAddress;

    /**
     * 可被允许的MAC地址
     */
    private List<String> macAddress;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;

    /**
     * 系统序列号
     */
    private String systemSerial;

    /**
     * os全局唯一
     */
    private String osUuid;

    /**
     * 扩展参数
     */
    private Map<String, Object> extraMap;
}
