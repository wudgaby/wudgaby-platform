package com.wudgaby.platform.sys.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.ip2region.Ip2RegionHelper;
import com.wudgaby.ip2region.RegionInfo;
import com.wudgaby.logger.api.event.AccessLoggerAfterEvent;
import com.wudgaby.logger.api.vo.AccessLoggerInfo;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.security.core.UserInfo;
import com.wudgaby.platform.sys.entity.SysLog;
import com.wudgaby.platform.sys.mapper.SysLogMapper;
import com.wudgaby.platform.sys.service.SysLogService;
import com.wudgaby.platform.utils.JacksonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 访问日志表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
    private final Ip2RegionHelper ip2RegionHelper;

    @Async
    @EventListener
    public void accessLoggerAfterEvent(AccessLoggerAfterEvent event) {
        AccessLoggerInfo loggerInfo = event.getAccessLogInfoVo();

        UserInfo userInfo = SecurityUtils.getCurrentUser();
        SysLog sysLog = new SysLog();
        sysLog.setUserId(Optional.ofNullable(userInfo).map(u -> (long)u.getId()).orElse(0L));
        sysLog.setUserName(Optional.ofNullable(userInfo).map(UserInfo::getUsername).orElse(""));
        sysLog.setAction(loggerInfo.getAction());
        sysLog.setDesc(loggerInfo.getDescribe());
        sysLog.setUserAgent(loggerInfo.getUserAgent());
        sysLog.setReqUrl(loggerInfo.getUrl());
        sysLog.setReqMethod(loggerInfo.getHttpMethod());

        String contentType = loggerInfo.getHttpHeaders().get("content-type");
        if(StringUtils.isNotBlank(contentType) && contentType.contains("multipart/form-data")){
            sysLog.setReqParam(StringUtils.EMPTY);
        }else{
            Map<String, Object> filterMap = loggerInfo.getParameters().entrySet().stream()
                    .filter(entry -> entry.getValue() instanceof Serializable)
                    .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), LinkedHashMap::putAll);

            try{
                sysLog.setReqParam(JacksonUtil.serialize(filterMap));
            }catch (Exception e){
                log.error("日志记录,请求体序列化失败. {}", e.getMessage());
            }
        }

        sysLog.setReqTime(new Date(loggerInfo.getRequestTime()));
        sysLog.setRespTime(new Date(loggerInfo.getResponseTime()));
        sysLog.setReqHeaders(JacksonUtil.serialize(loggerInfo.getHttpHeaders()));
        sysLog.setReqIp(loggerInfo.getIp());
        sysLog.setHttpStatus(loggerInfo.getHttpStatus());
        if (sysLog.getReqIp() != null) {
            String reqRegion = Optional.ofNullable(ip2RegionHelper.btreeSearch(sysLog.getReqIp())).map(RegionInfo::getAddressAndIsp).orElseGet(() -> RegionInfo.createNotMatchRegionInfo().getAddressAndIsp());
            sysLog.setReqRegion(reqRegion);
        }
        try{
            sysLog.setServerAddr(InetAddress.getLocalHost().getHostAddress());
        }catch (UnknownHostException e) {
            log.error("获取本机ip失败. {}", e.getMessage());
        }

        sysLog.setError(Optional.ofNullable(loggerInfo.getException())
                .map(e -> ExceptionUtils.getMessage(loggerInfo.getException()) + "\n" + ExceptionUtils.getStackTrace(loggerInfo.getException()))
                .orElse(null));
        sysLog.setRunTime((int)(loggerInfo.getResponseTime() - loggerInfo.getRequestTime()));
        sysLog.setType("SYSTEM");

        if(loggerInfo.getResponse() instanceof ApiResult){
            sysLog.setSucceed(ApiResult.isSuccess((ApiResult)loggerInfo.getResponse()));
        }else{
            sysLog.setSucceed(StringUtils.isBlank(sysLog.getError()));
        }
        sysLog.setResponse(Optional.ofNullable(loggerInfo.getResponse()).map(resp -> {
            try {
                String result = JacksonUtil.serialize(resp);
                return StrUtil.sub(result, 0, Math.min(20000, result.length()));
            } catch (Exception e){
                log.error("日志记录,响应体序列化失败. {}", e.getMessage());
            }
            return null;
        }).orElse(null));

        UserAgent userAgent = UserAgentUtil.parse(loggerInfo.getUserAgent());
        if(userAgent != null){
            sysLog.setOs(userAgent.getOs().toString());
            sysLog.setBrowser(userAgent.getBrowser().toString());
        }
        this.save(sysLog);
    }
}
