package com.wudgaby.platform.springext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/10 10:20
 * @Desc :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaResource {
    private String pattern;
    private String method;
}
