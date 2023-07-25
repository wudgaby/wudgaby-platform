package com.wudgaby.apiautomatic.service;

import com.wudgaby.apiautomatic.dto.ApiDTO;

import java.util.Collection;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/5/2 14:13
 * @Desc :
 */
public interface ApiRegisterService {
    /**
     * 批量注册
     * @param apiDTOList
     */
    void batchRegister(Collection<ApiDTO> apiDTOList);

}
