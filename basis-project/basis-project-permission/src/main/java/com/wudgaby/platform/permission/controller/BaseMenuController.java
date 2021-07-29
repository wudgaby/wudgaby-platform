package com.wudgaby.platform.permission.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.permission.entity.BaseAction;
import com.wudgaby.platform.permission.entity.BaseMenu;
import com.wudgaby.platform.permission.service.BaseActionService;
import com.wudgaby.platform.permission.service.BaseMenuService;
import com.wudgaby.platform.permission.vo.ActionForm;
import com.wudgaby.platform.permission.vo.MenuForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统资源-菜单信息 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
@Api(tags = "菜单管理")
@AllArgsConstructor
@RestController
@RequestMapping("/menus")
public class BaseMenuController {
    private final BaseMenuService baseMenuService;
    private final BaseActionService baseActionService;

    @ApiOperation(value = "获取菜单列表")
    @GetMapping
    public ApiResult<List<BaseMenu>> list(){
        return ApiResult.success(baseMenuService.list());
    }

    @ApiOperation(value = "获取菜单分页列表")
    @GetMapping("/page")
    public ApiPageResult<IPage<BaseMenu>> page(PageForm pageForm){
        return ApiPageResult.success(baseMenuService.page(new Page(pageForm.getPageNum(), pageForm.getPageCount())));
    }

    @ApiOperation(value = "获取菜单下所有操作")
    @GetMapping("/{menuId}/actions")
    public ApiResult<List<BaseAction>> getMenuActions(@PathVariable Long menuId) {
        return ApiResult.<List<BaseAction>>success().data(baseActionService.list(Wrappers.<BaseAction>lambdaQuery().eq(BaseAction::getMenuId, menuId)));
    }


    @ApiOperation(value = "检查编码是否已存在")
    @GetMapping("/checkCode")
    public ApiResult<Boolean> page(@RequestParam String menuCode){
        boolean exist = baseMenuService.count(Wrappers.<BaseMenu>lambdaQuery().eq(BaseMenu::getMenuCode, menuCode)) > 0;
        return exist ? ApiResult.success(true).message("菜单编码已存在.") : ApiResult.success(false);
    }

    @ApiOperation("查看菜单详情")
    @GetMapping("/{menuId}")
    public ApiResult<BaseMenu> detail(@PathVariable Long menuId){
        return ApiResult.success(baseMenuService.getById(menuId));
    }

    @ApiOperation(value = "添加菜单", notes = "返回新增id")
    @PostMapping
    public ApiResult<Long> add(@Validated @RequestBody MenuForm menuForm){
        BaseMenu baseMenu = new BaseMenu();
        baseMenu.setParentId(menuForm.getParentId());
        baseMenu.setMenuCode(menuForm.getMenuCode());
        baseMenu.setMenuName(menuForm.getMenuName());
        baseMenu.setMenuDesc(menuForm.getMenuDesc());
        baseMenu.setScheme(menuForm.getScheme());
        baseMenu.setPath(menuForm.getPath());
        baseMenu.setIcon(menuForm.getIcon());
        baseMenu.setTarget(menuForm.getTarget());
        baseMenu.setPriority(menuForm.getPriority());
        baseMenu.setServiceId(menuForm.getServiceId());
        return ApiResult.<Long>success().data(baseMenuService.addMenu(baseMenu));
    }

    @ApiOperation("更新菜单")
    @PutMapping("/{menuId}")
    public ApiResult update(@PathVariable Long menuId, @Validated @RequestBody MenuForm menuForm){
        BaseMenu baseMenu = new BaseMenu();
        baseMenu.setMenuId(menuId);
        baseMenu.setParentId(menuForm.getParentId());
        baseMenu.setMenuCode(menuForm.getMenuCode());
        baseMenu.setMenuName(menuForm.getMenuName());
        baseMenu.setMenuDesc(menuForm.getMenuDesc());
        baseMenu.setScheme(menuForm.getScheme());
        baseMenu.setPath(menuForm.getPath());
        baseMenu.setIcon(menuForm.getIcon());
        baseMenu.setTarget(menuForm.getTarget());
        baseMenu.setPriority(menuForm.getPriority());
        baseMenu.setServiceId(menuForm.getServiceId());
        baseMenuService.updateMenu(baseMenu);
        return ApiResult.success();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/{menuId}")
    public ApiResult del(@PathVariable Long menuId){
        baseMenuService.delMenu(menuId);
        return ApiResult.success();
    }
}
