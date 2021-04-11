package com.wudgaby.platform.core.model.dto;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName : ValidationErrorDTO
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/5/29/029 11:07
 * @Desc :   TODO
 */
@Data
public class ValidationErrorDTO {
    private List<FieldErrorDTO> fieldErrors;

    public void addFieldError(String field, String message){
        if(fieldErrors == null){
            fieldErrors = Lists.newArrayList();
        }
        fieldErrors.add(new FieldErrorDTO(field, message));
    }

    @JSONField(serialize = false)
    public String getAllErrorMsg(){
        return Joiner.on(";").skipNulls().join(fieldErrors);
    }

    @JSONField(serialize = false)
    public String getFirstErrorMsg(){
        return Optional.ofNullable(CollUtil.getFirst(fieldErrors)).map(err -> err.getMessage()).orElse("");
    }
}
