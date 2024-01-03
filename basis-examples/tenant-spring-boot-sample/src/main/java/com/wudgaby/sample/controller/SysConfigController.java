package com.wudgaby.sample.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.sample.domain.SysConfig;
import com.wudgaby.sample.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 配置表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "系统配置")
@RestController
@RequestMapping("/sysConfigs")
@AllArgsConstructor
public class SysConfigController {
    private final SysConfigService sysConfigService;

    @ApiOperation("系统配置列表")
    @GetMapping
    public ApiPageResult<SysConfig> sysConfigList(PageForm pageForm){
        IPage<SysConfig> page = new Page<>(pageForm.getPageNum(), pageForm.getPageSize());
        sysConfigService.page(page);
        return ApiPageResult.success(page);
    }

    @ApiOperation("通过id获取系统配置")
    @GetMapping("/{id}")
    public ApiResult<SysConfig> getSysConfigById(@PathVariable Long id){
        return ApiResult.success(sysConfigService.getById(id));
    }

    @ApiOperation("通过key获取系统配置")
    @GetMapping("/{configKey}")
    public ApiResult<SysConfig> getSysConfigByKey(@PathVariable String configKey){
        return ApiResult.success(sysConfigService.getOne(Wrappers.<SysConfig>lambdaQuery().eq(SysConfig::getConfigKey, configKey)));
    }

    @ApiOperation("添加系统配置")
    @PostMapping
    public ApiResult<SysConfig> addSysConfig(@Validated @RequestBody SysConfig sysConfig){
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("1").eq("config_key", sysConfig.getConfigKey());
        queryWrapper.last("limit 1");

        boolean exist = CollectionUtils.isNotEmpty(sysConfigService.list(queryWrapper));
        AssertUtil.notNull(exist, StrUtil.format("Key: {} 已存在", sysConfig.getConfigKey()));

        sysConfigService.save(sysConfig);
        return ApiResult.<SysConfig>success().data(sysConfig);
    }

    @ApiOperation("更新系统配置")
    @PutMapping
    public ApiResult updateSysConfig(@Validated @RequestBody SysConfig sysConfig){
        SysConfig dbSysConfig = sysConfigService.getById(sysConfig.getId());
        AssertUtil.notNull(dbSysConfig, "不存在的系统配置");
        sysConfigService.updateById(sysConfig);
        return ApiResult.success();
    }

    @ApiOperation("删除系统配置")
    @DeleteMapping
    public ApiResult delSysConfig(@RequestParam Long id){
        SysConfig sysConfig = sysConfigService.getById(id);
        AssertUtil.notNull(sysConfig, "不存在的系统配置");
        sysConfigService.removeById(id);
        return ApiResult.success();
    }
}
