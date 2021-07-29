package com.wudgaby.platform.permission.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.permission.consts.AuthorityConst;
import com.wudgaby.platform.permission.entity.BaseApi;
import com.wudgaby.platform.permission.service.BaseApiService;
import com.wudgaby.platform.permission.vo.ApiForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统资源-API接口 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Api(tags = "接口资源管理")
@RestController
@RequestMapping("/apis")
@AllArgsConstructor
public class BaseApiController {
    private final BaseApiService baseApiService;

    @ApiOperation(value = "获取接口资源列表")
    @GetMapping
    public ApiResult<List<BaseApi>> list(){
        return ApiResult.success(baseApiService.list());
    }

    @ApiOperation(value = "获取接口资源分页列表")
    @GetMapping("/page")
    public ApiPageResult<IPage<BaseApi>> page(PageForm pageForm){
        return ApiPageResult.success(baseApiService.page(new Page(pageForm.getPageNum(), pageForm.getPageCount())));
    }

    @ApiOperation(value = "检查编码是否已存在")
    @GetMapping("/checkCode")
    public ApiResult<Boolean> page(@RequestParam String apiCode){
        boolean exist = baseApiService.count(Wrappers.<BaseApi>lambdaQuery().eq(BaseApi::getApiCode, apiCode)) > 0;
        return exist ? ApiResult.success(true).message("接口编码已存在.") : ApiResult.success(false);
    }

    @ApiOperation("查看API详情")
    @GetMapping("/{apiId}")
    public ApiResult<BaseApi> detail(@PathVariable Long apiId){
        return ApiResult.success(baseApiService.getById(apiId));
    }

    @ApiOperation(value = "添加接口资源", notes = "返回新增id")
    @PostMapping
    public ApiResult<Long> add(@Validated @RequestBody ApiForm apiForm){
        BaseApi baseApi = new BaseApi();
        baseApi.setApiCode(apiForm.getApiCode());
        baseApi.setApiName(apiForm.getApiName());
        baseApi.setApiCategory(apiForm.getApiCategory());
        baseApi.setApiDesc(apiForm.getApiDesc());
        baseApi.setRequestMethod(apiForm.getRequestMethod());
        baseApi.setContentType(apiForm.getContentType());
        baseApi.setServiceId(apiForm.getServiceId());
        baseApi.setPath(apiForm.getPath());
        baseApi.setPriority(apiForm.getPriority());
        baseApi.setIsAuth(apiForm.getIsAuth());
        baseApi.setIsOpen(apiForm.getIsOpen());
        baseApi.setClassName(apiForm.getClassName());
        baseApi.setMethodName(apiForm.getMethodName());
        return ApiResult.<Long>success().data(baseApiService.addApi(baseApi));
    }

    @ApiOperation("更新接口资源")
    @PutMapping("/{apiId}")
    public ApiResult update(@PathVariable Long apiId, @Validated @RequestBody ApiForm apiForm){
        BaseApi baseApi = new BaseApi();
        baseApi.setApiId(apiId);
        baseApi.setApiId(apiForm.getApiId());
        baseApi.setApiCode(apiForm.getApiCode());
        baseApi.setApiName(apiForm.getApiName());
        baseApi.setApiCategory(apiForm.getApiCategory());
        baseApi.setApiDesc(apiForm.getApiDesc());
        baseApi.setRequestMethod(apiForm.getRequestMethod());
        baseApi.setContentType(apiForm.getContentType());
        baseApi.setServiceId(apiForm.getServiceId());
        baseApi.setPath(apiForm.getPath());
        baseApi.setPriority(apiForm.getPriority());
        baseApi.setIsAuth(apiForm.getIsAuth());
        baseApi.setIsOpen(apiForm.getIsOpen());
        baseApi.setClassName(apiForm.getClassName());
        baseApi.setMethodName(apiForm.getMethodName());
        baseApiService.updateApi(baseApi);
        return ApiResult.success();
    }

    @ApiOperation("删除接口资源")
    @DeleteMapping("/{apiId}")
    public ApiResult del(@PathVariable Long apiId){
        baseApiService.delApi(apiId);
        return ApiResult.success();
    }

    @ApiOperation("批量删除接口资源")
    @DeleteMapping("/batch")
    public ApiResult batchDel(@RequestParam List<Long> apiIds){
        baseApiService.remove(Wrappers.<BaseApi>lambdaQuery().in(BaseApi::getApiId, apiIds).eq(BaseApi::getIsPersist, 0));
        return ApiResult.success();
    }
}
