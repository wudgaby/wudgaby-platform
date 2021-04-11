package com.wudgaby.platform.sys.controller;


import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sys.entity.SysNotice;
import com.wudgaby.platform.sys.form.NoticeForm;
import com.wudgaby.platform.sys.service.SysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 公告表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "通知公告")
@RestController
@RequestMapping("/notices")
@AllArgsConstructor
public class SysNoticeController {
    private final SysNoticeService sysNoticeService;

    @ApiOperation("添加公告")
    @PostMapping
    public ApiResult addNotice(@Validated @RequestBody NoticeForm noticeForm){
        sysNoticeService.addNotice(noticeForm);
        return ApiResult.success();
    }

    @ApiOperation("删除公告")
    @DeleteMapping("/{id}")
    public ApiResult delNotice(@PathVariable Long id){
        sysNoticeService.removeNotice(id);
        return ApiResult.success();
    }

    @ApiOperation("更新公告状态")
    @PutMapping("/{id}")
    public ApiResult updateNoticeStatus(@PathVariable Long id, @RequestParam int status){
        sysNoticeService.updateNoticeStatus(id, status);
        return ApiResult.success();
    }

    @ApiOperation("获取公告")
    @GetMapping
    public ApiPageResult<SysNotice> noticeList(PageForm pageForm){
        return ApiPageResult.<SysNotice>success(sysNoticeService.pageList(pageForm));
    }

}
