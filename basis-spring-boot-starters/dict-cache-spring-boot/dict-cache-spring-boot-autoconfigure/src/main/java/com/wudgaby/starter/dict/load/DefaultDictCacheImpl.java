package com.wudgaby.starter.dict.load;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.wudgaby.starter.dict.api.DictCachedApi;
import com.wudgaby.starter.dict.api.DictVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/11/2 0002 17:23
 * @desc :
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultDictCacheImpl implements DictCache {
    private final int REFRESH_DELAY = 60 * 1000;

    private final Cache<String, DictVO> DICT_TYPE_CACHED = Caffeine.newBuilder()
            .maximumSize(10_000)
            .build();

    private final Cache<String, List<DictVO>> DICT_ITEM_CACHED = Caffeine.newBuilder()
            .maximumSize(10_000)
            .build();

    private final DictCachedApi dictCachedApi;

    /**
     * 最后更新时间
     */
    private volatile Date lastUpdatedTimeForDictType;
    private volatile Date lastUpdatedTimeForDictItem;

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void init2Cached() {
        log.info("初始化字典, 缓存至内存");
        load();
    }

    @Override
    @Scheduled(initialDelay = REFRESH_DELAY, fixedDelay = REFRESH_DELAY)
    public void refresh() {
        log.info("刷新字典缓存");
        load();
    }

    @Override
    public List<DictVO> listDictTypes() {
        return Lists.newArrayList(DICT_TYPE_CACHED.asMap().values());
    }

    @Override
    public List<DictVO> listDictItems() {
        List<DictVO> allItems = Lists.newArrayList();
        DICT_ITEM_CACHED.asMap().values().forEach(allItems::addAll);
        return allItems;
    }

    @Override
    public Optional<DictVO> getDictItemByVal(String type, String value) {
        List<DictVO> dictDTOList = DICT_ITEM_CACHED.getIfPresent(type);
        if(CollectionUtil.isEmpty(dictDTOList)) {
            return Optional.empty();
        }
        return dictDTOList.stream().filter(dictDTO -> StrUtil.equals(dictDTO.getValue(), value)).findFirst();
    }

    @Override
    public Optional<DictVO> getDictItemByLabel(String type, String label) {
        List<DictVO> dictDTOList = DICT_ITEM_CACHED.getIfPresent(type);
        if(CollectionUtil.isEmpty(dictDTOList)) {
            return Optional.empty();
        }
        return dictDTOList.stream().filter(dictDTO -> StrUtil.equals(dictDTO.getLabel(), label)).findFirst();
    }

    @Override
    public List<DictVO> listDictItemsByType(String type) {
        return DICT_ITEM_CACHED.getIfPresent(type);
    }

    @Override
    public List<Tree<Object>> treeDictItemsByType(String type) {
        List<DictVO> listDictItems = this.listDictItemsByType(type);
        if(CollectionUtil.isEmpty(listDictItems)) {
            return Lists.newArrayList();
        }

        //TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 最大递归深度
        //treeNodeConfig.setDeep(3);

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

    private void load(){
        List<DictVO> sysDictTypeList = null;
        List<DictVO> sysDictItemList = null;

        if(lastUpdatedTimeForDictType == null || lastUpdatedTimeForDictItem == null) {
            sysDictTypeList = dictCachedApi.listDictTypes();
            sysDictItemList = dictCachedApi.listDictItems();
        } else {
            boolean hasUpdateType = dictCachedApi.hasUpdateForType(lastUpdatedTimeForDictType);
            if(hasUpdateType){
                log.info("字典类型有更新");
                sysDictTypeList = dictCachedApi.listDictTypes();
            }

            boolean hasUpdateForItem = dictCachedApi.hasUpdateForItem(lastUpdatedTimeForDictItem);
            if(hasUpdateForItem){
                log.info("字典项有更新");
                sysDictItemList = dictCachedApi.listDictItems();
            }
        }

        if(CollectionUtil.isNotEmpty(sysDictTypeList)){
            // 字典类型
            Map<String, DictVO> dictTypeMap = sysDictTypeList.stream().collect(Collectors.toMap(DictVO::getDictType, dict -> dict));
            DICT_TYPE_CACHED.invalidateAll();
            DICT_TYPE_CACHED.putAll(dictTypeMap);

            lastUpdatedTimeForDictType = sysDictTypeList.stream().filter(s -> Objects.nonNull(s.getUpdateTime()))
                    .max(Comparator.comparing(DictVO::getUpdateTime))
                    .map(DictVO::getUpdateTime)
                    .orElse(new Date());
        }

        if(CollectionUtil.isNotEmpty(sysDictItemList)){
            // 字典类型分组
            Map<String, List<DictVO>> dictItemMap = sysDictItemList.stream().collect(Collectors.groupingBy(DictVO::getDictType));
            DICT_ITEM_CACHED.invalidateAll();
            DICT_ITEM_CACHED.putAll(dictItemMap);

            lastUpdatedTimeForDictItem = sysDictItemList.stream().filter(s -> Objects.nonNull(s.getUpdateTime()))
                    .max(Comparator.comparing(DictVO::getUpdateTime))
                    .map(DictVO::getUpdateTime)
                    .orElse(new Date());
        }
    }
}
