package com.wudgaby.platform.flowable.helper.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName : AddSubSignTaskVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/30 12:28
 * @Desc :   
 */
@Data
public class AddSubSignTaskVo {
    private String taskId;
    private List<String> userIds;
}
