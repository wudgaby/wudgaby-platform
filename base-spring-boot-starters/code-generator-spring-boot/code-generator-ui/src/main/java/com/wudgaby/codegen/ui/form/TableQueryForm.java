package com.wudgaby.codegen.ui.form;

import com.github.houbb.heaven.util.lang.reflect.ClassUtil;
import com.wudgaby.platform.core.model.form.PageForm;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class TableQueryForm extends PageForm {
    private String tableName;

    private String url;
    private String username;
    private String password;

    private String packageName;
    private String controllerPackage;
    private String entityPackage;
    private String servicePackage;

}
