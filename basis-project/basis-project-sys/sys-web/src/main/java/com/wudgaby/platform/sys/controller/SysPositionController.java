package com.wudgaby.platform.sys.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.sys.entity.SysPosition;
import com.wudgaby.platform.sys.service.SysPositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 职位表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "职位管理")
@RestController
@RequestMapping("/positions")
@AllArgsConstructor
public class SysPositionController {
    private final SysPositionService sysPositionService;

    @ApiOperation("职位列表")
    @GetMapping
    public ApiResult<List<SysPosition>> positionList(){
        return ApiResult.success(sysPositionService.list());
    }

    @ApiOperation("添加职位")
    @PostMapping
    public ApiResult addPosition(@Validated @RequestBody SysPosition sysPosition){
        int count = sysPositionService.count(Wrappers.<SysPosition>lambdaQuery().eq(SysPosition::getPositionCode, sysPosition.getPositionCode()));
        AssertUtil.isFalse(count > 0, "职位编码重复.");
        sysPositionService.save(sysPosition);
        return ApiResult.success();
    }

    @ApiOperation("更新职位")
    @PutMapping
    public ApiResult updatePosition(@Validated @RequestBody SysPosition sysPosition){
        sysPosition.setPositionCode(null);
        sysPositionService.updateById(sysPosition);
        return ApiResult.success();
    }

    @ApiOperation("删除职位")
    @DeleteMapping
    public ApiResult delPosition(@RequestParam Long id){
        sysPositionService.removeById(id);
        return ApiResult.success();
    }
}
