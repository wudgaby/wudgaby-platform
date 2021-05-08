package com.wudgaby.codegen.ui.form;

import com.wudgaby.platform.core.model.form.PageForm;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @ClassName : TableQueryForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/3 11:48
 * @Desc :   TODO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("数据表查询表单")
public class CodeDownLoadForm extends PageForm {
    private List<String> tableNames;

    private String url;
    private String username;
    private String password;

    private String basePackage = "com.wudgaby";
    // "com.wudgaby.platform.core.entity.BaseEntity";
    private String superEntityClass;
    private String superEntityColumns;
    private String controllerPackage = "controller";
    private String entityPackage = "model.entity";
    private String servicePackage = "service";
    private String serviceImplPackage = "service.impl";
    private String mapperPackage = "mapper";
    private String xmlPackage = "mapper.xml";

    private String tablePrefix;
    //private String fieldPrefix;

}
