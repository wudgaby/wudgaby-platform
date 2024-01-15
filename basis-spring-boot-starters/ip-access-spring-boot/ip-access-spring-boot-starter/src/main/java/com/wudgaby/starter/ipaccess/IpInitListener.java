package com.wudgaby.starter.ipaccess;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.extra.spring.SpringUtil;
import com.wudgaby.starter.ipaccess.config.IpAccessProp;
import com.wudgaby.starter.ipaccess.enums.BwType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/30 0030 11:34
 * @desc :
 */
@Slf4j
public class IpInitListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("初始化ip黑白名单.");

        TimeInterval timer = DateUtil.timer();
        initIpWhiteAndBlack();

        log.info("初始化ip黑白名单完成. 耗时: {} 毫秒", timer.interval());
    }

    private void initIpWhiteAndBlack(){
        IpManage ipManage = SpringUtil.getBean(IpManage.class);
        IpAccessProp ipAccessProp = SpringUtil.getBean(IpAccessProp.class);

        if(CollectionUtil.isNotEmpty(ipAccessProp.getBlackList())) {
            ipAccessProp.getBlackList().forEach(ip -> {
                log.info("载入黑名单ip: {}", ip);
                ipManage.batchAdd(Ipv4Util.list(ip, false), BwType.BLACK);
            });
        }

        if(CollectionUtil.isNotEmpty(ipAccessProp.getWhiteList())) {
            ipAccessProp.getWhiteList().forEach(ip -> {
                log.info("载入白名单ip: {}", ip);
                ipManage.batchAdd(Ipv4Util.list(ip, false), BwType.WHITE);
            });
        }
    }
}
