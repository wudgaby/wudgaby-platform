package com.wudgaby.platform.sys.service;

import com.wudgaby.platform.sys.entity.SysDictItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.platform.sys.form.DictItemForm;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
public interface SysDictItemService extends IService<SysDictItem> {
    List<SysDictItem> getDictItemList(String type);
    SysDictItem getDictItem(String type, String itemVal);
    void addDictItem(DictItemForm dictItemForm);
    void updateDictItem(DictItemForm dictItemForm);
    void delDictItems(List<Long> ids);
}
