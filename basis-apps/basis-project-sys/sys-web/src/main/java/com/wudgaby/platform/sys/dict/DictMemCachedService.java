package com.wudgaby.platform.sys.dict;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.wudgaby.platform.sys.entity.SysDictItem;
import com.wudgaby.platform.sys.entity.SysDictType;
import com.wudgaby.platform.sys.service.SysDictItemService;
import com.wudgaby.platform.sys.service.SysDictTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/2 0002 18:22
 * @desc :
 */
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
@Service
public class DictMemCachedService implements DictCachedService{
    private final Cache<String, DictDTO> DICT_TYPE_CACHED = Caffeine.newBuilder()
            .maximumSize(10_000)
            .build();

    private final Cache<String, List<DictDTO>> DICT_ITEM_CACHED = Caffeine.newBuilder()
            .maximumSize(10_000)
            .build();

    private final int REFRESH_DELAY = 1 * 60 * 1000;

    /**
     * 最后更新时间
     */
    private volatile Date lastUpdatedTimeForDictType;
    private volatile Date lastUpdatedTimeForDictItem;

    private final SysDictTypeService sysDictTypeService;
    private final SysDictItemService sysDictItemService;

    @PostConstruct
    @Override
    public void init2Cached() {
        log.info("初始化字典缓存");
        load();
    }

    @Scheduled(initialDelay = REFRESH_DELAY, fixedDelay = REFRESH_DELAY)
    @Override
    public void refresh() {
        log.info("刷新字典缓存");
        load();
    }

    private void load(){
        List<SysDictType> sysDictTypeList = null;
        List<SysDictItem> sysDictItemList = null;

        if(lastUpdatedTimeForDictType == null || lastUpdatedTimeForDictItem == null) {
            sysDictTypeList = sysDictTypeService.list(Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getStatus, "1").orderByAsc(SysDictType::getSort));
            sysDictItemList = sysDictItemService.list(Wrappers.<SysDictItem>lambdaQuery().eq(SysDictItem::getStatus, "1").orderByAsc(SysDictItem::getSort));
        } else {
            boolean hasUpdateType = sysDictTypeService.getOne(Wrappers.<SysDictType>lambdaQuery().gt(SysDictType::getUpdateTime, lastUpdatedTimeForDictType).last("LIMIT 1")) != null;
            if(hasUpdateType){
                log.info("字典类型有更新");
                sysDictTypeList = sysDictTypeService.list(Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getStatus, "1").orderByAsc(SysDictType::getSort));
            }

            boolean hasUpdateForItem = sysDictItemService.getOne(Wrappers.<SysDictItem>lambdaQuery().gt(SysDictItem::getUpdateTime, lastUpdatedTimeForDictItem).last("LIMIT 1")) != null;
            if(hasUpdateForItem){
                log.info("字典项有更新");
                sysDictItemList = sysDictItemService.list(Wrappers.<SysDictItem>lambdaQuery().eq(SysDictItem::getStatus, "1").orderByAsc(SysDictItem::getSort));
            }
        }

        if(CollectionUtil.isNotEmpty(sysDictTypeList)){
            // 字典类型
            Map<String, DictDTO> dictTypeMap = sysDictTypeList.stream().map(dictType -> {
                DictDTO dictDTO = new DictDTO();
                dictDTO.setId(dictType.getId());
                dictDTO.setLabel(dictType.getName());
                dictDTO.setValue(dictType.getType());
                dictDTO.setDictType(dictType.getType());
                dictDTO.setSort(dictType.getSort());
                return dictDTO;
            }).collect(Collectors.toMap(DictDTO::getDictType, dict -> dict));
            DICT_TYPE_CACHED.invalidateAll();
            DICT_TYPE_CACHED.putAll(dictTypeMap);

            lastUpdatedTimeForDictType = sysDictTypeList.stream().filter(s -> Objects.nonNull(s.getUpdateTime()))
                    .max(Comparator.comparing(SysDictType::getUpdateTime))
                    .map(s -> s.getUpdateTime())
                    .orElse(new Date());
        }

        if(CollectionUtil.isNotEmpty(sysDictItemList)){
            // 字典类型分组
            Map<String, List<DictDTO>> dictItemMap = sysDictItemList.stream().map(dictItem -> {
                DictDTO dictDTO = new DictDTO();
                dictDTO.setId(dictItem.getId());
                dictDTO.setPid(dictItem.getParentId());
                dictDTO.setLabel(dictItem.getDictName());
                dictDTO.setValue(dictItem.getDictValue());
                dictDTO.setDictType(dictItem.getDictType());
                dictDTO.setSort(dictItem.getSort());
                return dictDTO;
            }).collect(Collectors.groupingBy(DictDTO::getDictType));
            DICT_ITEM_CACHED.invalidateAll();
            DICT_ITEM_CACHED.putAll(dictItemMap);

            lastUpdatedTimeForDictItem = sysDictItemList.stream().filter(s -> Objects.nonNull(s.getUpdateTime()))
                    .max(Comparator.comparing(SysDictItem::getUpdateTime))
                    .map(s -> s.getUpdateTime())
                    .orElse(new Date());
        }
    }

    @Override
    public List<DictDTO> listDictTypes() {
        return Lists.newArrayList(DICT_TYPE_CACHED.asMap().values());
    }

    @Override
    public Optional<DictDTO> getDictItemByVal(String type, String value) {
        List<DictDTO> dictDTOList = DICT_ITEM_CACHED.getIfPresent(type);
        if(CollectionUtil.isEmpty(dictDTOList)) {
            return null;
        }
        return dictDTOList.stream().filter(dictDTO -> StrUtil.equals(dictDTO.getValue(), value)).findFirst();
    }

    @Override
    public Optional<DictDTO> getDictItemByLabel(String type, String label) {
        List<DictDTO> dictDTOList = DICT_ITEM_CACHED.getIfPresent(type);
        if(CollectionUtil.isEmpty(dictDTOList)) {
            return null;
        }
        return dictDTOList.stream().filter(dictDTO -> StrUtil.equals(dictDTO.getLabel(), label)).findFirst();
    }

    @Override
    public List<DictDTO> listDictItems(String type) {
        return DICT_ITEM_CACHED.getIfPresent(type);
    }

    @Override
    public List<Tree<Long>> treeDictItems(String type) {
        List<DictDTO> listDictItems = this.listDictItems(type);
        if(CollectionUtil.isEmpty(listDictItems)) {
            return null;
        }

        return TreeUtil.build(listDictItems, 0L,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getPid());
                    tree.setWeight(treeNode.getSort());
                    tree.setName(treeNode.getLabel());
                    // 扩展属性 ...
                    tree.putExtra("dictType", treeNode.getDictType());
                    tree.putExtra("dictValue", treeNode.getValue());
                });
    }
}
