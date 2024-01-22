package com.wudgaby.starter.core.hdw;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.system.oshi.OshiUtil;

import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/18 0018 14:59
 * @desc : linux服务器信息
 */
public class LinuxServerInfos extends AbstractServerInfos {

    @Override
    protected List<String> getIpAddress() throws Exception {
        List<String> result = null;

        //获取所有网络接口
        List<InetAddress> inetAddresses = getLocalAllInetAddress();

        if (CollUtil.isNotEmpty(inetAddresses)) {
            result = inetAddresses.stream()
                    .map(InetAddress::getHostAddress)
                    .distinct()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Override
    protected List<String> getMacAddress() throws Exception {
        List<String> result = null;

        //1. 获取所有网络接口
        List<InetAddress> inetAddresses = getLocalAllInetAddress();

        if (CollUtil.isNotEmpty(inetAddresses)) {
            //2. 获取所有网络接口的 Mac 地址
            result = inetAddresses.stream()
                    .map(this::getMacByInetAddress)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Override
    protected String getCpuSerial() throws Exception {
        return OshiUtil.getProcessor().getProcessorIdentifier().getProcessorID();
    }

    @Override
    protected String getMainBoardSerial() throws Exception {
        return OshiUtil.getSystem().getBaseboard().getSerialNumber();
    }
}