package com.wudgaby.platform.sys.controller;


import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sys.entity.SysDictItem;
import com.wudgaby.platform.sys.form.DictItemForm;
import com.wudgaby.platform.sys.service.SysDictItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "字典项管理")
@RestController
@RequestMapping("/dictItems")
@AllArgsConstructor
public class SysDictItemController {
    private final SysDictItemService sysDictItemService;

    @ApiOperation("字典项列表")
    @GetMapping("/{type}")
    public ApiResult<List<SysDictItem>> list(@PathVariable String type){
        return ApiResult.success(sysDictItemService.getDictItemList(type));
    }

    @ApiOperation("字典项树")
    @GetMapping("/tree/{type}")
    public ApiResult<List<Tree<Long>>> listItemTree(@PathVariable String type){
        List<SysDictItem> sysDictItemList = sysDictItemService.getDictItemList(type);

        List<TreeNode<Long>> treeNodeList = Lists.newArrayList();
        for(SysDictItem sysDictItem : sysDictItemList){
            Map<String, Object> extra = Maps.newHashMap();
            extra.put("val", sysDictItem.getDictValue());
            TreeNode treeNode = new TreeNode<>(sysDictItem.getId(), sysDictItem.getParentId(), sysDictItem.getDictName(), sysDictItem.getSort());
            treeNode.setExtra(extra);
            treeNodeList.add(treeNode);
        }
        return ApiResult.success(TreeUtil.build(treeNodeList, 0L));
    }

    @ApiOperation("字典项子节点列表")
    @GetMapping("/{type}/{pid}")
    public ApiResult<List<SysDictItem>> getChildByPid(@PathVariable String type, @PathVariable String pid){
        List<SysDictItem> sysDictItemList = sysDictItemService.list(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictType, type).eq(SysDictItem::getParentId, pid)
                .orderByDesc(SysDictItem::getSort)
        );
        return ApiResult.success(sysDictItemList);
    }

    @ApiOperation("添加字典项")
    @PostMapping
    public ApiResult add(@Validated @RequestBody DictItemForm dictItemForm){
        sysDictItemService.addDictItem(dictItemForm);
        return ApiResult.success();
    }

    @ApiOperation("更新字典项")
    @PutMapping
    public ApiResult update(@Validated @RequestBody DictItemForm dictItemForm){
        sysDictItemService.updateDictItem(dictItemForm);
        return ApiResult.success();
    }

    @ApiOperation("删除字典项")
    @DeleteMapping
    public ApiResult delete(@RequestBody List<Long> ids){
        sysDictItemService.delDictItems(ids);
        return ApiResult.success();
    }
}
