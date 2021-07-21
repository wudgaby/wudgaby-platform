package com.wudgaby.platform.core.model.form;

import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName : BaseQueryForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/16 15:59
 * @Desc :
 */
@Data
@Accessors(chain = true)
public class BaseQueryForm implements Serializable {
    @ApiModelProperty(hidden = true)
    private Map<String, String> paramMap;

    public Map<String, String> getParamMap(){
        if(paramMap == null){
            paramMap = Maps.newHashMap();
        }
        return paramMap;
    }
}
