package com.wudgaby.platform.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/2/23 14:23
 * @Desc :
 */
@UtilityClass
public class TreeUtil {

    public static List<TreeNode> treeToList(Tree tree) {
        List<TreeNode> treeNodeList = Lists.newArrayList();
        if (null != tree) {
            LinkedList<Tree> list = new LinkedList<>();
            list.add(tree);

            String[] defaultKeyArray = {TreeNodeConfig.DEFAULT_CONFIG.getIdKey(), TreeNodeConfig.DEFAULT_CONFIG.getNameKey(),
                    TreeNodeConfig.DEFAULT_CONFIG.getParentIdKey(), TreeNodeConfig.DEFAULT_CONFIG.getWeightKey(), TreeNodeConfig.DEFAULT_CONFIG.getChildrenKey()};

            Tree currentNode;
            while (!list.isEmpty()) {
                currentNode = list.poll();

                TreeNode treeNode = new TreeNode(currentNode.getId(), currentNode.getParentId(), (String)currentNode.getName(), currentNode.getWeight());
                Map<String, Object> extraMap = Maps.newHashMap();
                currentNode.entrySet().stream().forEach((entry) -> {
                    Map.Entry<String, Object> entry1 = ((Map.Entry)entry);
                    if(!ArrayUtils.contains(defaultKeyArray, entry1.getKey())){
                        extraMap.put(entry1.getKey(), entry1.getValue());
                    }
                });
                if(MapUtils.isNotEmpty(extraMap)){
                    treeNode.setExtra(extraMap);
                }
                treeNodeList.add(treeNode);

                if (CollectionUtils.isNotEmpty(currentNode.getChildren())) {
                    list.addAll(currentNode.getChildren());
                }
            }
        }
        return treeNodeList;
    }

    public static void main(String[] args) {
        List<TreeNode<String>> nodeList = CollUtil.newArrayList();
        nodeList.add(new TreeNode<>("1", "0", "系统管理", 5));
        nodeList.add(new TreeNode<>("11", "1", "用户管理", 222222));
        nodeList.add(new TreeNode<>("111", "11", "用户添加", 0));
        nodeList.add(new TreeNode<>("2", "0", "店铺管理", 1));
        nodeList.add(new TreeNode<>("21", "2", "商品管理", 44));
        nodeList.add(new TreeNode<>("221", "2", "商品管理2", 2));

        TreeNode treeNode = new TreeNode<>("2221", "2", "商品管理3", 3);
        Map<String, Object> map = Maps.newHashMap();
        map.put("a", "a");
        map.put("b", 1);
        treeNode.setExtra(map);
        nodeList.add(treeNode);

        List<Tree<String>> treeList = cn.hutool.core.lang.tree.TreeUtil.build(nodeList, "0");
        for(Tree<String> tree : treeList) {
            List<TreeNode> treeNodeList = treeToList(tree);
            treeNodeList.forEach(node -> System.out.println(node.getId() + " " + node.getName()));
            System.out.println();
        }
    }
}
