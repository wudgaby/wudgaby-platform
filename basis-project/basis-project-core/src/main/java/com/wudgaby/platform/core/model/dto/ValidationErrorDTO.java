package com.wudgaby.platform.core.model.dto;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/5/29/029 11:07
 * @Desc :
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

    @JsonIgnore
    public String getAllErrorMsg(){
        if(fieldErrors == null){
            return "";
        }
        return fieldErrors.stream().map(fe -> fe.getMessage()).collect(Collectors.joining(";"));
    }

    @JsonIgnore
    public String getFirstErrorMsg(){
        return Optional.ofNullable(CollUtil.getFirst(fieldErrors)).map(err -> err.getMessage()).orElse("");
    }
}
