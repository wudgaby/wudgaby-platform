package com.wudgaby.apiautomatic.service;

import com.wudgaby.apiautomatic.dto.ApiDTO;

import java.util.Collection;
import java.util.List;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/5/2 14:13
 * @Desc :
 */
public interface ApiRegisterService {
    /**
     * 注册
     * @param apiDTO
     */
    @Deprecated
    void register(ApiDTO apiDTO) ;

    /**
     * 批量注册
     * @param apiDTOList
     */
    void batchRegister(Collection<ApiDTO> apiDTOList);
}
