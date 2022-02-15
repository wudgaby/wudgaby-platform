package com.wudgaby.platform.sys.controller;


import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.wudgaby.platform.core.constant.SymbolConstant;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.sys.entity.SysResource;
import com.wudgaby.platform.sys.service.SysResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 资源表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "资源菜单")
@RestController
@RequestMapping("/sysResource")
@RequiredArgsConstructor
public class SysResourceController {
    private final SysResourceService sysResourceService;

    @ApiOperation("完整资源树")
    @GetMapping("/tree")
    public ApiResult<List<Tree<Long>>> resTree(){
        List<SysResource> sysResList = sysResourceService.list();

        if(CollectionUtils.isNotEmpty(sysResList)){
            List<TreeNode<Long>> treeNodeList = Lists.newArrayList();
            for(SysResource sysRes : sysResList){
                treeNodeList.add(new TreeNode<>(sysRes.getId(), sysRes.getParentId(), sysRes.getResName(), sysRes.getSort()));
            }
            return ApiResult.success(TreeUtil.build(treeNodeList, 0L));
        }
        return ApiResult.success();
    }

    @ApiOperation("懒加载资源")
    @GetMapping("/childNode/{pid}")
    public ApiResult<List<SysResource>> resChildNode(@PathVariable(required = false) Long pid){
        List<SysResource> sysRestList = sysResourceService.list(Wrappers.<SysResource>lambdaQuery().eq(SysResource::getParentId, Optional.ofNullable(pid).orElse(0L)));
        return ApiResult.<List<SysResource>>success().data(sysRestList);
    }

    @ApiOperation("获取资源信息")
    @GetMapping
    public ApiResult<SysResource> regInfo(@RequestParam Long resId){
        return ApiResult.<SysResource>success().data(sysResourceService.getById(resId));
    }

    @ApiOperation("添加资源")
    @PostMapping
    public ApiResult addRes(@Validated @RequestBody SysResource sysResource){
        if(sysResource.getParentId() == 0){
            sysResource.setParentId(0L);
            sysResource.setParentIds("");
            sysResource.setLevel(1);
        } else {
            SysResource parentSysRes = sysResourceService.getById(sysResource.getParentId());
            AssertUtil.notNull(parentSysRes, "未知的上级资源");

            String parentIdPath = StringUtils.isBlank(parentSysRes.getParentIds()) ? SymbolConstant.L_SLASH : parentSysRes.getParentIds() + SymbolConstant.L_SLASH;
            sysResource.setParentIds(parentIdPath + sysResource.getParentId() + SymbolConstant.L_SLASH);
            sysResource.setLevel(parentSysRes.getLevel() + 1);
        }
        sysResourceService.save(sysResource);
        return ApiResult.success();
    }

    @ApiOperation("更新资源")
    @PutMapping
    public ApiResult updateRes(@Validated @RequestBody SysResource sysResource){
        sysResource.setParentId(null);
        sysResource.setParentIds(null);
        sysResource.setLevel(null);
        sysResourceService.updateById(sysResource);
        return ApiResult.success();
    }

    @ApiOperation("删除资源")
    @DeleteMapping
    public ApiResult delRes(@RequestParam Long id){
        int count = sysResourceService.count(Wrappers.<SysResource>lambdaQuery().eq(SysResource::getParentId, id));
        AssertUtil.isFalse(count > 0, "还有子资源,不能删除.");
        sysResourceService.removeById(id);
        return ApiResult.success();
    }
}
