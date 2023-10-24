package com.wudgaby.starter.mybatisplus.helper.mybatis;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/10/10 0010 11:37
 * @desc :
 */
public class CustomerIdGenerator implements IdentifierGenerator {

    @Override
    public Number nextId(Object entity) {
        return null;
    }

    @Override
    public String nextUUID(Object entity) {
        return IdentifierGenerator.super.nextUUID(entity);
    }
}
