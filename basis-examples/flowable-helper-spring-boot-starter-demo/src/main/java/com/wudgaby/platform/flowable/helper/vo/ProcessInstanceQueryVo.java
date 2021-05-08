package com.wudgaby.platform.flowable.helper.vo;

import com.wudgaby.platform.core.model.form.PageForm;
import lombok.Data;

/**
 * @author : bruce.liu
 * @title: : QueryProcessInstanceVo
 * @projectName : flowable
 * @description: 查询流程实例VO
 * @date : 2019/11/2115:42
 */
@Data
public class ProcessInstanceQueryVo extends PageForm {
    private String formName;
    private String userCode;
    private String userName;
}
