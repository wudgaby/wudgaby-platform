package com.wudgaby.platform.core.model.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/5/29/029 11:08
 * @Desc :
 */
@Data
@RequiredArgsConstructor
public class FieldErrorDTO {
    @NonNull
    private String field;
    @NonNull
    private String message;
}
