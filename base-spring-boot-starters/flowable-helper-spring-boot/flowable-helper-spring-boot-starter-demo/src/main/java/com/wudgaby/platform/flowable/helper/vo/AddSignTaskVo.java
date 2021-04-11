package com.wudgaby.platform.flowable.helper.vo;

import java.util.List;

/**
 * @author : bruce.liu
 * @title: : AddSignTaskVo
 * @projectName : flowable
 * @description: 加签Vo
 * @date : 2019/12/515:47
 */
public class AddSignTaskVo extends BaseProcessVo {
    /**
     * 被加签人
     */
    private List<String> signPersoneds;

    public List<String> getSignPersoneds() {
        return signPersoneds;
    }

    public void setSignPersoneds(List<String> signPersoneds) {
        this.signPersoneds = signPersoneds;
    }
}
