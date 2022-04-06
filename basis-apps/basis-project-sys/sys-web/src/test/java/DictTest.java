import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import com.wudgaby.platform.sys.SysBootstrap;
import com.wudgaby.platform.sys.dict.DictHelper;
import com.wudgaby.platform.utils.TreeUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    @Test
    public void testListDictType(){
        Assert.assertNotNull(DictHelper.listDictTypes());
        System.out.println(DictHelper.listDictTypes());
    }

    @Test
    public void testItemByVal(){
        Assert.assertNotNull(DictHelper.getDictItemByVal("alert", "1"));
        System.out.println(DictHelper.getDictItemByVal("alert", "1"));
    }

    @Test
    public void testItemByLabel(){
        Assert.assertNotNull(DictHelper.getDictItemByLabel("alert", "贷款、代办信用卡类"));
        System.out.println(DictHelper.getDictItemByLabel("alert", "贷款、代办信用卡类"));
    }

    @Test
    public void testItemsByType(){
        Assert.assertNotNull(DictHelper.listDictItems("alert"));
        System.out.println(DictHelper.listDictItems("alert"));
    }

    @Test
    public void treeDictItems(){
        Assert.assertNotNull(DictHelper.treeDictItems("alert"));

        List<Tree<Long>> treeList = DictHelper.treeDictItems("alert");
        System.out.println(treeList);

        for(Tree<Long> tree : treeList) {
            List<TreeNode> treeNodeList = TreeUtil.treeToList(tree);
            treeNodeList.forEach(node -> System.out.println(node.getId() + " " + node.getName()));
            System.out.println();
        }
    }

}
