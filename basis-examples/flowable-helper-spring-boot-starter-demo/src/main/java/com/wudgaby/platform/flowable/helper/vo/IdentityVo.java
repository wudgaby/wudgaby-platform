package com.wudgaby.platform.flowable.helper.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName : UserVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/13 17:10
 * @Desc :
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class IdentityVo {
    public enum IdentityType{
        USER, GROUP, ROLE;
    }
    private String id;
    private String name;
    private IdentityType type;
}
