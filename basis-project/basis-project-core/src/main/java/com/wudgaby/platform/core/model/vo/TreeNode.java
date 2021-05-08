package com.wudgaby.platform.core.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ace on 2017/6/12.
 */
@Data
public class TreeNode {
    private String id;
    private String parentId;
    private List<TreeNode> children = new ArrayList<>();

    public void add(TreeNode treeNode){
        children.add(treeNode);
    }
}
