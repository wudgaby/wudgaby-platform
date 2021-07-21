package com.wudgaby.platform.flowable.helper.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName : UserTaskNodeVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/13 16:16
 * @Desc :
 */
@Data
@Accessors(chain = true)
public class UserTaskNodeVo {
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 描述
     */
    private String desc;

    /**
     * 审批名称
     */
    private List<IdentityVo> approverList;
}
