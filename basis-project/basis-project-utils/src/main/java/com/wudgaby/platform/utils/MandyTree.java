package com.wudgaby.platform.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/2/23 11:50
 * @Desc : MandyTree 实现 deepClone, 需要 Serializable
 */
public interface MandyTree<T> extends Serializable {

    Long getParentId();
    void setParentId(Long parentId);

    Long getId();
    void setId(Long id);

    List<T> getChildren();
    void setChildren(List<T> children);

    /**
     * java8 接口支持默认实现了
     * tree 转 list
     * @param tree
     * @param <T>
     * @return
     */
    static <T extends MandyTree<T>> List<T> treeToList(List<T> tree) {
        List<T> nodes = new ArrayList<>();
        for (T node : tree) {
            T newNode = deepClone(node);
            newNode.setChildren(null);
            nodes.add(newNode);
            if (node.getChildren() != null && ! node.getChildren().isEmpty()) {
                nodes.addAll(treeToList(node.getChildren()));
            }
        }
        return nodes;
    }

    /**
     * list 转 tree
     * @param nodes
     * @param <T>
     * @return
     */
    static <T extends MandyTree<T>> List<T> listToTree(List<T> nodes) {
        List<T> tree = new ArrayList<>();
        for (T node : nodes) {
            T parent;
            Long parentId = node.getParentId();
            parent = findParent(parentId, tree);
            if (null == parent){
                //T newNode;
                //tree.add(node);  有问题的代码，要用deepClone噢
                tree.add(deepClone(node));
            }else {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                //parent.getChildren().add(node);
                parent.getChildren().add(deepClone(node));
            }
        }
        return tree;
    }

    static <T extends MandyTree<T>> T findParent(Long parentId, List<T> nodeList) {
        for (T it : nodeList) {
            if (it.getId().equals(parentId)){
                return it;
            }else if (null != it.getChildren()){
                T parentNode = findParent(parentId, it.getChildren());
                if (null != parentNode){
                    return parentNode;
                }
            }
        }
        return null;
    }

    /**
     * 一定要深克隆, 不能用原来的入参中的引用, 否则, 你试试看 :)
     * @param src
     * @param <T>
     * @return
     */
    static <T extends MandyTree<T>> T deepClone(T src) {
        //static <T extends MandyTree<T>> T deepClone(T src) {
        T dest = null;
        try { // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(src);
            //将流序列化成对象
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
            dest = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dest;
    }
}