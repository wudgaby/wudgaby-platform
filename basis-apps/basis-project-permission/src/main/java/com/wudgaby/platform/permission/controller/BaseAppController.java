package com.wudgaby.platform.permission.controller;


import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.permission.entity.BaseApp;
import com.wudgaby.platform.permission.service.BaseAppService;
import com.wudgaby.platform.permission.vo.AppForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统应用-基础信息 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Api(tags = "应用管理")
@RestController
@RequestMapping("/apps")
@AllArgsConstructor
public class BaseAppController {
    private final BaseAppService baseAppService;

    @ApiOperation(value = "获取应用列表")
    @GetMapping
    public ApiResult<List<BaseApp>> list(){
        return ApiResult.success(baseAppService.list());
    }

    @ApiOperation("查看应用详情")
    @GetMapping("/{appId}")
    public ApiResult<BaseApp> detail(@PathVariable Long appId){
        return ApiResult.success(baseAppService.getById(appId));
    }

    @ApiOperation(value = "重置应用秘钥")
    @PostMapping("/{appId}/resetSecret")
    public ApiResult<String> resetAppSecret(@PathVariable("appId") Long appId) {
        String result = baseAppService.resetAppSecret(appId);
        return ApiResult.success(result);
    }

    @ApiOperation(value = "添加应用", notes = "返回新增id")
    @PostMapping
    public ApiResult<Long> add(@Validated @RequestBody AppForm appForm){
        return ApiResult.<Long>success().data(baseAppService.addApp(appForm));
    }

    @ApiOperation("更新应用")
    @PutMapping("/{appId}")
    public ApiResult update(@PathVariable Long appId, @Validated @RequestBody AppForm appForm){
        appForm.setAppId(appId);
        baseAppService.updateApp(appForm);
        return ApiResult.success();
    }

    @ApiOperation("删除应用")
    @DeleteMapping("/{appId}")
    public ApiResult del(@PathVariable String appId){
        baseAppService.delApp(appId);
        return ApiResult.success();
    }
}
