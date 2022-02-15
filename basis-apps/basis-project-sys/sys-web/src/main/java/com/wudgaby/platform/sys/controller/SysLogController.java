package com.wudgaby.platform.sys.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.sys.entity.SysLog;
import com.wudgaby.platform.sys.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 访问日志表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "访问日志")
@RestController
@RequestMapping("/sysLog")
@AllArgsConstructor
public class SysLogController {
    private final SysLogService sysLogService;

    @ApiOperation("日志列表")
    @GetMapping
    public ApiPageResult<SysLog> logList(PageForm pageForm){
        return ApiPageResult.success(sysLogService.page(
                new Page<>(pageForm.getPageNum(), pageForm.getPageSize()),
                Wrappers.<SysLog>lambdaQuery().orderByDesc(SysLog::getCreateTime))
        );
    }
}
