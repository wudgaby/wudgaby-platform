package com.wudgaby.platform.flowable.helper.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CcToVo {
    private String userId;
    private String userName;

    @Override
    public String toString(){
        return String.format("%s[%s]",this.userName,this.userId);
    }
}
