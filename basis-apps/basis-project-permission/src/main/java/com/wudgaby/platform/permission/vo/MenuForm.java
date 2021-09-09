package com.wudgaby.platform.permission.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/7/17 0:14
 * @Desc :
 */
@ApiModel("菜单表单")
@Data
public class MenuForm {
    @ApiModelProperty(value = "菜单Id", hidden = true)
    private Long menuId;

    @ApiModelProperty(value = "父级菜单")
    private Long parentId;

    @ApiModelProperty(value = "菜单编码", required = true)
    @NotBlank(message = "请填写菜单编码")
    private String menuCode;

    @ApiModelProperty(value = "菜单名称", required = true)
    @NotBlank(message = "请填写菜单名称")
    private String menuName;

    @ApiModelProperty(value = "描述")
    private String menuDesc;

    @ApiModelProperty(value = "路径前缀", required = true)
    @NotBlank(message = "请填写路径前缀")
    private String scheme;

    @ApiModelProperty(value = "请求路径")
    private String path;

    @ApiModelProperty(value = "菜单标题")
    private String icon;

    @ApiModelProperty(value = "打开方式:_self窗口内,_blank新窗口")
    private String target;

    @ApiModelProperty(value = "优先级 越小越靠前")
    private Long priority;

    @ApiModelProperty(value = "服务名")
    private String serviceId;
}
