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
import com.wudgaby.platform.sys.entity.SysDept;
import com.wudgaby.platform.sys.entity.SysOrg;
import com.wudgaby.platform.sys.service.SysDeptService;
import com.wudgaby.platform.sys.service.SysOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 机构表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "公司机构")
@RestController
@RequestMapping("/orgs")
@AllArgsConstructor
public class SysOrgController {
    private final SysOrgService sysOrgService;
    private final SysDeptService sysDeptService;

    @ApiOperation("完整机构树")
    @GetMapping
    public ApiResult<List<Tree<Long>>> orgTree(){
        List<SysOrg> sysOrgList = sysOrgService.list();

        if(CollectionUtils.isNotEmpty(sysOrgList)){
            List<TreeNode<Long>> treeNodeList = Lists.newArrayList();
            for(SysOrg sysOrg : sysOrgList){
                treeNodeList.add(new TreeNode<>(sysOrg.getId(), sysOrg.getParentId(), sysOrg.getAbbrName(), sysOrg.getSort()));
            }
            return ApiResult.success(TreeUtil.build(treeNodeList, 0L));
        }
        return ApiResult.success();
    }

    @ApiOperation("懒加载机构")
    @GetMapping("/childNode/{pid}")
    public ApiResult<List<SysOrg>> orgChildNode(@PathVariable(required = false) Long pid){
        List<SysOrg> sysOrgList = sysOrgService.list(Wrappers.<SysOrg>lambdaQuery().eq(SysOrg::getParentId, Optional.ofNullable(pid).orElse(0L)));
        return ApiResult.<List<SysOrg>>success().data(sysOrgList);
    }

    @ApiOperation("获取机构信息")
    @GetMapping("/{orgId}")
    public ApiResult<SysOrg> orgInfo(@PathVariable Long orgId){
        return ApiResult.<SysOrg>success().data(sysOrgService.getById(orgId));
    }

    @ApiOperation("添加机构")
    @PostMapping
    public ApiResult addOrg(@Validated @RequestBody SysOrg sysOrg){
        if(sysOrg.getParentId() == 0){
            sysOrg.setParentId(0L);
            sysOrg.setParentIds("");
            sysOrg.setLevel(1);
        } else {
            SysOrg parentSysOrg = sysOrgService.getById(sysOrg.getParentId());
            AssertUtil.notNull(parentSysOrg, "未知的上级机构");

            String parentIdPath = StringUtils.isBlank(parentSysOrg.getParentIds()) ? SymbolConstant.LEFT_DIVIDE : parentSysOrg.getParentIds() + SymbolConstant.LEFT_DIVIDE;
            sysOrg.setParentIds(parentIdPath + sysOrg.getParentId() + SymbolConstant.LEFT_DIVIDE);
            sysOrg.setLevel(parentSysOrg.getLevel() + 1);
        }

        sysOrgService.save(sysOrg);
        return ApiResult.success();
    }

    @ApiOperation("更新机构")
    @PutMapping
    public ApiResult updateOrg(@Validated @RequestBody SysOrg sysOrg){
        sysOrg.setParentIds(null);
        sysOrg.setParentId(null);
        sysOrg.setLevel(null);
        sysOrgService.updateById(sysOrg);
        return ApiResult.success();
    }

    @ApiOperation("删除机构")
    @DeleteMapping
    public ApiResult delOrg(@RequestParam Long id){
        int count = sysOrgService.count(Wrappers.<SysOrg>lambdaQuery().eq(SysOrg::getParentId, id));
        AssertUtil.isFalse(count > 0, "还有子机构,不能删除.");

        int depCount = sysDeptService.count(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getOrgId, id));
        AssertUtil.isFalse(depCount > 0, "该机构下还有关联部门,不能删除.");

        sysOrgService.removeById(id);
        return ApiResult.success();
    }


}
