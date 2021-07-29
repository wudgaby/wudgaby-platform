package com.wudgaby.platform.permission.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.permission.entity.BaseAction;
import com.wudgaby.platform.permission.service.BaseActionService;
import com.wudgaby.platform.permission.vo.ActionForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统资源-功能操作 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Api(tags = "功能按钮管理")
@RestController
@RequestMapping("/actions")
@AllArgsConstructor
public class BaseActionController {
    private final BaseActionService baseActionService;

    @ApiOperation(value = "获取功能按钮列表")
    @GetMapping
    public ApiResult<List<BaseAction>> list(){
        return ApiResult.success(baseActionService.list());
    }

    @ApiOperation(value = "获取接口资源分页列表")
    @GetMapping("/page")
    public ApiPageResult<IPage<BaseAction>> page(PageForm pageForm){
        return ApiPageResult.success(baseActionService.page(new Page(pageForm.getPageNum(), pageForm.getPageCount())));
    }

    @ApiOperation(value = "检查编码是否已存在")
    @GetMapping("/checkCode")
    public ApiResult<Boolean> page(@RequestParam String actionCode){
        boolean exist = baseActionService.count(Wrappers.<BaseAction>lambdaQuery().eq(BaseAction::getActionCode, actionCode)) > 0;
        return exist ? ApiResult.success(true).message("功能按钮编码已存在.") : ApiResult.success(false);
    }

    @ApiOperation("查看功能按钮详情")
    @GetMapping("/{actionId}")
    public ApiResult<BaseAction> detail(@PathVariable Long actionId){
        return ApiResult.success(baseActionService.getById(actionId));
    }

    @ApiOperation(value = "添加功能按钮资源", notes = "返回新增id")
    @PostMapping
    public ApiResult<Long> add(@Validated @RequestBody ActionForm actionForm){
        return ApiResult.<Long>success().data(baseActionService.addAction(actionForm));
    }

    @ApiOperation("更新功能按钮资源")
    @PutMapping("/{actionId}")
    public ApiResult update(@PathVariable Long actionId, @Validated @RequestBody ActionForm actionForm){
        actionForm.setActionId(actionId);
        baseActionService.updateAction(actionForm);
        return ApiResult.success();
    }

    @ApiOperation("删除功能按钮资源")
    @DeleteMapping("/{actionId}")
    public ApiResult del(@PathVariable Long actionId){
        baseActionService.delAction(actionId);
        return ApiResult.success();
    }
}
