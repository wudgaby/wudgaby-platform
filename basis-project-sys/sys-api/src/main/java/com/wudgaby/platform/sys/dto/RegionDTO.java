package com.wudgaby.platform.sys.dto;

import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.sys.entity.SysRegion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName : RegionDTO
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/9/28 18:02
 * @Desc :   TODO
 */
@Data
@ApiModel("地区传输对象")
@Accessors(chain = true)
public class RegionDTO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    public static RegionDTO conver(SysRegion sysRegion){
        AssertUtil.notNull(sysRegion, "地区转换错误");
        return new RegionDTO()
                .setId(sysRegion.getId())
                .setName(sysRegion.getName())
                ;
    }
}
