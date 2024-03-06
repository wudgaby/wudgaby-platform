package com.wudgaby.starter.core.hdw;

import com.wudgaby.starter.core.param.LicenseExtraParam;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/18 0018 14:53
 * @desc :
 */
@Getter
@Slf4j
public abstract class AbstractServerInfos {
    @Setter
    private Map<String, Object> extraParam;
    /**
     * 组装需要额外校验的License参数
     */
    public LicenseExtraParam getServerInfos(){
        LicenseExtraParam result = new LicenseExtraParam();

        try {
            result.setIpAddress(this.getIpAddress());
            result.setMacAddress(this.getMacAddress());
            result.setCpuSerial(this.getCpuSerial());
            result.setMainBoardSerial(this.getMainBoardSerial());
            result.setExtraMap(this.getExtraParam());
        }catch (Exception e){
            log.error("获取服务器硬件信息失败",e);
        }

        return result;
    }

    /**
     * 获取IP地址
     */
    protected abstract List<String> getIpAddress() throws Exception;

    /**
     * 获取Mac地址
     */
    protected abstract List<String> getMacAddress() throws Exception;

    /**
     * 获取CPU序列号
     */
    protected abstract String getCpuSerial() throws Exception;

    /**
     * 获取主板序列号
     */
    protected abstract String getMainBoardSerial() throws Exception;

    /**
     * 系统序列号
     */
    protected abstract String getSystemSerial() throws Exception;

    /**
     * 系统hash
     */
    protected abstract String getOsUuid() throws Exception;

    /**
     * 获取当前服务器所有符合条件的InetAddress
     */
    protected List<InetAddress> getLocalAllInetAddress() throws Exception {
        List<InetAddress> result = new ArrayList<>(4);

        // 遍历所有的网络接口
        for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements(); ) {
            NetworkInterface iface = networkInterfaces.nextElement();
            // 在所有的接口下再遍历IP
            for (Enumeration<InetAddress> inetAddresses = iface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                InetAddress inetAddr = inetAddresses.nextElement();

                //排除LoopbackAddress、SiteLocalAddress、LinkLocalAddress、MulticastAddress类型的IP地址
                if(!inetAddr.isLoopbackAddress() /*&& !inetAddr.isSiteLocalAddress()*/
                        && !inetAddr.isLinkLocalAddress() && !inetAddr.isMulticastAddress()){
                    result.add(inetAddr);
                }
            }
        }

        return result;
    }

    /**
     * 获取某个网络接口的Mac地址
     */
    protected String getMacByInetAddress(InetAddress inetAddr){
        try {
            byte[] mac = NetworkInterface.getByInetAddress(inetAddr).getHardwareAddress();
            StringBuilder sb = new StringBuilder();

            for(int i=0;i<mac.length;i++){
                if(i != 0) {
                    sb.append("-");
                }
                //将十六进制byte转化为字符串
                String temp = Integer.toHexString(mac[i] & 0xff);
                if(temp.length() == 1){
                    sb.append("0").append(temp);
                }else{
                    sb.append(temp);
                }
            }
            return sb.toString().toUpperCase();
        } catch (SocketException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

}
