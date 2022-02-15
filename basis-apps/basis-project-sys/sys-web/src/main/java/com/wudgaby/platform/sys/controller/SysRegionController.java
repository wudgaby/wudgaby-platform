package com.wudgaby.platform.sys.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sys.dto.RegionDTO;
import com.wudgaby.platform.sys.entity.SysRegion;
import com.wudgaby.platform.sys.service.SysRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 全国地区表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "全国地区接口")
@RestController
@RequestMapping("/sysRegion")
@AllArgsConstructor
public class SysRegionController {
    private final SysRegionService sysRegionService;

    @ApiOperation("地区懒加载")
    @GetMapping("/children")
    public ApiResult<List<RegionDTO>> childrenList(@RequestParam(required = false) Long pid){
        Long regionPid = Optional.ofNullable(pid).orElse(0L);
        List<SysRegion> regionList = sysRegionService.list(Wrappers.<SysRegion>lambdaQuery()
                .eq(SysRegion::getPid, regionPid)
                .orderByAsc(SysRegion::getId)
        );
        List<RegionDTO> regionDTOList = regionList.stream()
                .map(RegionDTO::conver)
                .collect(Collectors.toList());
        return ApiResult.success(regionDTOList);
    }

    @ApiOperation("地区详情")
    @GetMapping("/info")
    public ApiResult<SysRegion> getRegionInfo(@RequestParam Long regionId){
        SysRegion sysRegion = sysRegionService.getById(regionId);
        return ApiResult.success(sysRegion);
    }
}
