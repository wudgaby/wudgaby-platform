package com.wudgaby.platform.sys.service;

import com.wudgaby.platform.sys.entity.SysDictType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.platform.sys.form.DictTypeForm;

import java.util.List;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
public interface SysDictTypeService extends IService<SysDictType> {
    void addDictType(DictTypeForm dictTypeForm);
    void updateDictType(DictTypeForm dictTypeForm);
    void delDictType(List<Long> ids);
}
