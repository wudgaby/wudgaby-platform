package com.wudgaby.platform.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wudgaby.platform.core.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件信息表
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysFile对象", description="文件信息表")
public class SysFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件仓库")
    private String fileBucket;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件后缀")
    private String fileSuffix;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "md5")
    private String fileMd5;

    @ApiModelProperty(value = "文件唯一标识")
    private String fileUniqueId;

    @ApiModelProperty(value = "文件大小kb")
    private Long fileSize;


}
