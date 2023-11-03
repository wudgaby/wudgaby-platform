package com.wudgaby.platform.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wudgaby.platform.sys.entity.SysDictItem;
import com.wudgaby.platform.sys.entity.SysDictType;
import com.wudgaby.platform.sys.service.SysDictItemService;
import com.wudgaby.platform.sys.service.SysDictTypeService;
import com.wudgaby.starter.dict.api.DictCachedApi;
import com.wudgaby.starter.dict.api.DictVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/2 0002 18:22
 * @desc :
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class DictCachedApiImpl implements DictCachedApi {
    private final SysDictTypeService sysDictTypeService;
    private final SysDictItemService sysDictItemService;

    @Override
    public List<DictVO> listDictTypes() {
        return sysDictTypeService.list().stream().map(dictType -> {
            DictVO dictVO = new DictVO();
            dictVO.setId(dictType.getId());
            dictVO.setPid(0L);
            dictVO.setLabel(dictType.getName());
            dictVO.setValue(dictType.getType());
            dictVO.setDictType(dictType.getType());
            dictVO.setSort(dictType.getSort());
            dictVO.setUpdateTime(dictType.getUpdateTime());
            return dictVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DictVO> listDictItems() {
        return sysDictItemService.list().stream().map(dictType -> {
            DictVO dictVO = new DictVO();
            dictVO.setId(dictType.getId());
            dictVO.setPid(dictType.getParentId());
            dictVO.setLabel(dictType.getDictName());
            dictVO.setValue(dictType.getDictValue());
            dictVO.setDictType(dictType.getDictType());
            dictVO.setSort(dictType.getSort());
            dictVO.setUpdateTime(dictType.getUpdateTime());
            return dictVO;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean hasUpdateForType(Date lastUpdatedTime) {
        if(lastUpdatedTime == null){
            return true;
        }
        return sysDictTypeService.getOne(Wrappers.<SysDictType>lambdaQuery().gt(SysDictType::getUpdateTime, lastUpdatedTime).last("LIMIT 1")) != null;
    }

    @Override
    public boolean hasUpdateForItem(Date lastUpdatedTime) {
        if(lastUpdatedTime == null){
            return true;
        }
        return sysDictItemService.getOne(Wrappers.<SysDictItem>lambdaQuery().gt(SysDictItem::getUpdateTime, lastUpdatedTime).last("LIMIT 1")) != null;
    }
}
