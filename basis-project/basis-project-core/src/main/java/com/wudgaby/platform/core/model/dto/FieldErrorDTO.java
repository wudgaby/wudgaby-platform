package com.wudgaby.platform.core.model.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : FieldErrorDTO
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/5/29/029 11:08
 * @Desc :   TODO
 */
@Data
@RequiredArgsConstructor
public class FieldErrorDTO {
    @NonNull
    private String field;
    @NonNull
    private String message;
}
