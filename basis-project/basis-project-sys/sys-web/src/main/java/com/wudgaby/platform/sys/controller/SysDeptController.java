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
import com.wudgaby.platform.sys.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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
 * 部门表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("/depts")
@AllArgsConstructor
public class SysDeptController {
    private final SysDeptService sysDeptService;

    @ApiOperation("完整部门树")
    @GetMapping("/tree")
    public ApiResult<List<Tree<Long>>> deptTree(){
        List<SysDept> sysDeptList = sysDeptService.list();

        if(CollectionUtils.isNotEmpty(sysDeptList)){
            List<TreeNode<Long>> treeNodeList = Lists.newArrayList();
            for(SysDept sysDept : sysDeptList){
                treeNodeList.add(new TreeNode<>(sysDept.getId(), sysDept.getParentId(), sysDept.getDeptName(), sysDept.getSort()));
            }
            return ApiResult.success(TreeUtil.build(treeNodeList, 0L));
        }
        return ApiResult.success();
    }

    @ApiOperation("懒加载部门")
    @GetMapping("/childNode/{pid}")
    public ApiResult<List<SysDept>> deptChildNode(@PathVariable(required = false) Long pid){
        List<SysDept> sysDeptList = sysDeptService.list(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getParentId, Optional.ofNullable(pid).orElse(0L)));
        return ApiResult.<List<SysDept>>success().data(sysDeptList);
    }

    @ApiOperation("获取部门信息")
    @GetMapping
    public ApiResult<SysDept> info(@RequestParam Long depId){
        return ApiResult.<SysDept>success().data(sysDeptService.getById(depId));
    }

    @ApiOperation("添加部门")
    @PostMapping
    public ApiResult addDept(@Validated @RequestBody SysDept sysDept){
        if(sysDept.getParentId() == 0){
            sysDept.setParentId(0L);
            sysDept.setParentIds("");
            sysDept.setLevel(1);
        } else {
            SysDept parentSysDept = sysDeptService.getById(sysDept.getParentId());
            AssertUtil.notNull(parentSysDept, "未知的上级部门");

            String parentIdPath = StringUtils.isBlank(parentSysDept.getParentIds()) ? SymbolConstant.L_SLASH : parentSysDept.getParentIds() + SymbolConstant.L_SLASH;
            sysDept.setParentIds(parentIdPath + sysDept.getParentId() + SymbolConstant.L_SLASH);
            sysDept.setLevel(parentSysDept.getLevel() + 1);
        }

        sysDeptService.save(sysDept);
        return ApiResult.success();
    }

    @ApiOperation("更新部门")
    @PutMapping
    public ApiResult updateDept(@Validated @RequestBody SysDept sysDept){
        sysDept.setParentIds(null);
        sysDept.setParentId(null);
        sysDept.setLevel(null);
        sysDeptService.updateById(sysDept);
        return ApiResult.success();
    }

    @ApiOperation("删除部门")
    @DeleteMapping
    public ApiResult delDept(@RequestParam Long id){
        int count = sysDeptService.count(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getParentId, id));
        AssertUtil.isFalse(count > 0, "还有子部门,不能删除.");
        sysDeptService.removeById(id);
        return ApiResult.success();
    }
}
