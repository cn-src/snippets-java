package cn.javaer.snippets.model;

import cn.hutool.core.collection.CollUtil;
import cn.javaer.snippets.model.pojo.Areas;
import cn.javaer.snippets.test.JsonAssert;
import cn.javaer.snippets.test.Log;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * @author cn-src
 */
class TreeTest {
    private final static List<Areas> TEST_DATA;

    static {
        Areas areas1 = new Areas("河北省", "石家庄市", "长安区", 4L);
        Areas areas2 = new Areas("河北省", "石家庄市", "新华区", 1L);
        Areas areas3 = new Areas("河北省", "唐山市", "开平区", 2L);
        Areas areas4 = new Areas("山东省", "太原市", "小店区", 3L);
        TEST_DATA = Arrays.asList(areas1, areas2, areas3, areas4);
    }

    @Test
    void of() {
        final TreeConf<Areas> conf = TreeConf.of(areas ->
            new String[]{areas.getArea1(), areas.getArea2(), areas.getArea3()});
        final List<TreeNode> treeNodes = Tree.of(TEST_DATA, conf);
        JsonAssert.assertEqualsAndOrder("model/TreeTest.of.json", treeNodes);
    }

    @Test
    void ofWithSort() {
        final TreeConf<Areas> conf = TreeConf.<Areas>builder()
            .nameFun(areas -> new String[]{areas.getArea1(), areas.getArea2(), areas.getArea3()})
            .sortFun(Areas::getSort)
            .build();
        final List<TreeNode> treeNodes = Tree.of(TEST_DATA, conf);
        Log.json(treeNodes);
        JsonAssert.assertEqualsAndOrder("model/TreeTest.ofWithSort.json", treeNodes);
    }

    @Test
    void ofWithDynamic() {
        final TreeConf<Areas> conf = TreeConf.<Areas>builder()
            .nameFun(areas -> new String[]{areas.getArea1(), areas.getArea2(), areas.getArea3()})
            .handler(treeInfo -> {
                treeInfo.getDynamic().put("depth", treeInfo.getDepth());
                treeInfo.getDynamic().put("index", treeInfo.getIndex());
                treeInfo.getDynamic().put("leaf", treeInfo.isLeaf());
            })
            .build();
        final List<TreeNode> treeNodes = Tree.of(TEST_DATA, conf);
        Log.json(treeNodes);
        JsonAssert.assertEqualsAndOrder("model/TreeTest.ofWithDynamic.json", treeNodes);
    }

    @Test
    void toModel() {
        final TreeNode tn11 = TreeNode.of("石家庄市");
        final TreeNode tn1 = TreeNode.of("河北省", tn11);
        final TreeNode tn2 = TreeNode.of("山东省");
        final List<Areas> areas = Tree.toModel(Arrays.asList(tn1, tn2), strings -> {
            Areas areas1 = new Areas();
            areas1.setArea1(CollUtil.get(strings, 0));
            areas1.setArea2(CollUtil.get(strings, 1));
            areas1.setArea3(CollUtil.get(strings, 2));
            return areas1;
        });
        assertThat(areas).hasSize(2)
            .extracting(Areas::getArea1, Areas::getArea2, Areas::getArea3)
            .contains(
                tuple("河北省", "石家庄市", null),
                tuple("山东省", null, null)
            );
    }
}