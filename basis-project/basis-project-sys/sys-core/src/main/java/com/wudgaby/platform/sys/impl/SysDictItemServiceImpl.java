package com.wudgaby.platform.sys.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.sys.entity.SysDictItem;
import com.wudgaby.platform.sys.form.DictItemForm;
import com.wudgaby.platform.sys.mapper.SysDictItemMapper;
import com.wudgaby.platform.sys.service.SysDictItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {
    @Override
    public List<SysDictItem> getDictItemList(String type) {
        return this.list(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictType, type)
                .orderByDesc(SysDictItem::getSort)
        );
    }

    @Override
    public SysDictItem getDictItem(String type, String dictVal) {
        return this.getOne(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictType, type)
                .eq(SysDictItem::getDictValue, dictVal)
        );
    }

    @Override
    public void addDictItem(DictItemForm dictItemForm) {
        boolean exist = this.count(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictType, dictItemForm.getType())
                .eq(SysDictItem::getDictValue, dictItemForm.getDictVal())
                .last("limit 1")) > 0;

        AssertUtil.isFalse(exist, "该字典已存在");

        SysDictItem sysDictItem = new SysDictItem();
        sysDictItem.setDictType(dictItemForm.getType());
        sysDictItem.setDictName(dictItemForm.getDictName());
        sysDictItem.setDictValue(dictItemForm.getDictVal());
        sysDictItem.setRemark(dictItemForm.getRemark());
        sysDictItem.setSort(dictItemForm.getSort());
        sysDictItem.setStatus(dictItemForm.getStatus());
        this.save(sysDictItem);
    }

    @Override
    public void updateDictItem(DictItemForm dictItemForm) {
        SysDictItem sysDictItem = new SysDictItem();
        sysDictItem.setId(dictItemForm.getId());
        sysDictItem.setDictType(dictItemForm.getType());
        sysDictItem.setDictName(dictItemForm.getDictName());
        sysDictItem.setDictValue(dictItemForm.getDictVal());
        sysDictItem.setRemark(dictItemForm.getRemark());
        sysDictItem.setSort(dictItemForm.getSort());
        sysDictItem.setStatus(dictItemForm.getStatus());
        this.updateById(sysDictItem);
    }

    @Override
    public void delDictItems(List<Long> ids) {
        this.removeByIds(ids);
    }
}
