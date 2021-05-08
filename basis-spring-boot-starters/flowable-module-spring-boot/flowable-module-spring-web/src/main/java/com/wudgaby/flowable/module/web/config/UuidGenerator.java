package com.wudgaby.flowable.module.web.config;

import cn.hutool.core.lang.UUID;
import org.flowable.common.engine.impl.cfg.IdGenerator;

/**
 * @ClassName : UuidGenerator
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/27 9:27
 * @Desc :
 */
public class UuidGenerator implements IdGenerator {
    @Override
    public String getNextId() {
        return UUID.fastUUID().toString(true);
    }
}
