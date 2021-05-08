package com.wudgaby.platform.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author WudGaby
 * @since 2020-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysRegion对象", description="")
public class SysRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "父id")
    private Integer pid;

    @ApiModelProperty(value = "简称")
    private String shortname;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "全称")
    private String mergerName;

    @ApiModelProperty(value = "层级 0 1 2 省市区县")
    private Integer level;

    @ApiModelProperty(value = "拼音")
    private String pinyin;

    @ApiModelProperty(value = "长途区号")
    private String code;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "首字母")
    private String first;

    @ApiModelProperty(value = "经度")
    private String lng;

    @ApiModelProperty(value = "纬度")
    private String lat;

    @ApiModelProperty(value = "合并id")
    private String mergeId;


}
