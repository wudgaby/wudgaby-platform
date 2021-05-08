package com.wudgaby.platform.sys.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.sys.entity.SysDictItem;
import com.wudgaby.platform.sys.entity.SysDictType;
import com.wudgaby.platform.sys.form.DictTypeForm;
import com.wudgaby.platform.sys.mapper.SysDictTypeMapper;
import com.wudgaby.platform.sys.service.SysDictItemService;
import com.wudgaby.platform.sys.service.SysDictTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {
    private final SysDictItemService sysDictItemService;

    @Override
    public void addDictType(DictTypeForm dictTypeForm) {
        boolean exist = this.count(Wrappers.<SysDictType>lambdaQuery()
                .eq(SysDictType::getType, dictTypeForm.getType())
                .last("limit 1")) > 0;
        AssertUtil.isFalse(exist, "该类型 {} 已存在", dictTypeForm.getType());

        SysDictType sysDictType = new SysDictType();
        sysDictType.setName(dictTypeForm.getName());
        sysDictType.setType(dictTypeForm.getType());
        sysDictType.setRemark(dictTypeForm.getRemark());
        sysDictType.setStatus(dictTypeForm.getStatus());
        sysDictType.setSys(dictTypeForm.isSys());
        this.save(sysDictType);
    }

    @Override
    public void updateDictType(DictTypeForm dictTypeForm) {
        SysDictType sysDictType = this.getById(dictTypeForm.getId());
        AssertUtil.notNull(sysDictType, "该字典类型不存在.");

        boolean exist = this.count(Wrappers.<SysDictType>lambdaQuery()
                .eq(SysDictType::getType, dictTypeForm.getType())
                .last("limit 1")) > 0;
        AssertUtil.isFalse(exist, "该类型已存在!");

        String oldDictType = sysDictType.getType();

        sysDictType.setName(dictTypeForm.getName());
        sysDictType.setType(dictTypeForm.getType());
        sysDictType.setRemark(dictTypeForm.getRemark());
        sysDictType.setStatus(dictTypeForm.getStatus());
        sysDictType.setSys(dictTypeForm.isSys());
        this.updateById(sysDictType);

        SysDictItem sysDictItem = new SysDictItem();
        sysDictItem.setDictType(dictTypeForm.getType());
        sysDictItemService.update(sysDictItem, Wrappers.<SysDictItem>lambdaQuery().eq(SysDictItem::getDictType, oldDictType));
    }

    @Override
    public void delDictType(List<Long> ids) {
        for(Long id : ids){
            SysDictType sysDictType = this.getById(id);
            if(sysDictType == null){
                continue;
            }
            AssertUtil.isFalse(sysDictType.getSys(), "{} 不能删除!该类型是系统级别类型.", sysDictType.getType());

            boolean exist = sysDictItemService.count(Wrappers.<SysDictItem>lambdaQuery().eq(SysDictItem::getDictType, sysDictType.getType())) > 0;
            AssertUtil.isTrue(exist, "{} 不能删除!该类型已有字典项.", sysDictType.getType());
        }
        this.removeByIds(ids);
    }
}
