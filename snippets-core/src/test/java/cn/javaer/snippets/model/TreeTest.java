package cn.javaer.snippets.model;

import cn.hutool.core.collection.CollUtil;
import cn.javaer.snippets.jackson.Json;
import cn.javaer.snippets.model.pojo.Areas;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * @author cn-src
 */
class TreeTest {

    @Test
    void of() throws Exception {
        final Areas areas1 = new Areas("河北省", "石家庄市", "长安区");
        final Areas areas2 = new Areas("河北省", "石家庄市", "新华区");
        final Areas areas3 = new Areas("河北省", "唐山市", "开平区");
        final Areas areas4 = new Areas("山东省", "太原市", "小店区");
        final TreeConf<Areas> conf =
            TreeConf.of(areas ->
                new String[]{areas.getArea1(), areas.getArea2(), areas.getArea3()});
        final List<TreeNode> treeNodes =
            Tree.of(Arrays.asList(areas1, areas2, areas3, areas4),
                conf);
        System.out.println(Json.NON_EMPTY.write(treeNodes));
        assertThat(treeNodes).hasSize(2);
        //language=JSON
        JSONAssert.assertEquals("[{\"title\":\"河北省\",\"children\":[{\"title\":\"石家庄市\"," +
                "\"children\":[{\"title\":\"长安区\"},{\"title\":\"新华区\"}]},{\"title\":\"唐山市\"," +
                "\"children\":[{\"title\":\"开平区\"}]}]},{\"title\":\"山东省\"," +
                "\"children\":[{\"title\":\"太原市\",\"children\":[{\"title\":\"小店区\"}]}]}]",
            Json.NON_EMPTY.write(treeNodes), false);
    }

    @Test
    void ofWith() throws Exception {
//        final Areas areas1 = new Areas("河北省", "石家庄市", "长安区");
//        final Areas areas2 = new Areas("河北省", "石家庄市", "新华区");
//        final Areas areas3 = new Areas("河北省", "唐山市", "开平区");
//        final Areas areas4 = new Areas("山东省", "太原市", "小店区");
//
//        final List<TreeNode> treeNodes = Tree.of(
//            Arrays.asList(areas1, areas2, areas3, areas4),
//            (treeNode, entity, depth, index) -> {
//                treeNode.putDynamic("depth", depth);
//                treeNode.putDynamic("index", index);
//            },
//            Areas::getArea1, Areas::getArea2, Areas::getArea3);
//        assertThat(treeNodes).hasSize(2);
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