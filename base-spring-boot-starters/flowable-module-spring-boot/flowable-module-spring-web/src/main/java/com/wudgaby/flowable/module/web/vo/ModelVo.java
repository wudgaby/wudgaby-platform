package com.wudgaby.flowable.module.web.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : bruce.liu
 * @title: : ModelVo
 * @projectName : flowable
 * @description: modelVo
 * @date : 2019/12/710:20
 */
@Data
public class ModelVo implements Serializable {
    //流程id
    private String processId;
    //流程名称
    private String processName;
    /**
     * 分类Id
     */
    private String categoryId;
    //流程的xml
    private String xml;
}
