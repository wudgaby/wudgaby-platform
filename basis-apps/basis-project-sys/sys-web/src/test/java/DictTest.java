import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import com.wudgaby.platform.sys.SysBootstrap;
import com.wudgaby.platform.utils.TreeUtil;
import com.wudgaby.starter.dict.load.DictCache;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/6 0006 10:54
 * @desc :
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SysBootstrap.class)
public class DictTest {
    @Autowired
    private DictCache dictCache;

    @Test
    public void testListDictType(){
        Assert.assertNotNull(dictCache.listDictTypes());
        System.out.println(dictCache.listDictTypes());
    }

    @Test
    public void testItemByVal(){
        Assert.assertNotNull(dictCache.getDictItemByVal("alert", "1"));
        System.out.println(dictCache.getDictItemByVal("alert", "1"));
    }

    @Test
    public void testItemByLabel(){
        Assert.assertNotNull(dictCache.getDictItemByLabel("alert", "贷款、代办信用卡类"));
        System.out.println(dictCache.getDictItemByLabel("alert", "贷款、代办信用卡类"));
    }

    @Test
    public void testItemsByType(){
        Assert.assertNotNull(dictCache.listDictItemsByType("alert"));
        System.out.println(dictCache.listDictItemsByType("alert"));
    }

    @Test
    public void treeDictItems(){
        List<Tree<Object>> list = dictCache.treeDictItemsByType("alert");
        Assert.assertNotNull(list);
        Assert.assertNotEquals(list.size(), 0);

        List<Tree<Object>> treeList = dictCache.treeDictItemsByType("alert");
        System.out.println(treeList);

        for(Tree<Object> tree : treeList) {
            List<TreeNode> treeNodeList = TreeUtil.treeToList(tree);
            treeNodeList.forEach(node -> System.out.println(node.getId() + " " + node.getName()));
            System.out.println();
        }
    }

}
