package com.wudgaby.platform.sys.listener;

import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/29 0029 14:37
 * @desc : 字典初始化缓存至内存
 */
public class DictInitListener implements ApplicationListener<ApplicationContextInitializedEvent>, Ordered {
    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
