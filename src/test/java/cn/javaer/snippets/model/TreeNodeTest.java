package cn.javaer.snippets.model;

import cn.javaer.snippets.jackson.Json;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class TreeNodeTest {

    @Test
    void addChildren() {
        final TreeNode t1 = TreeNode.of("t1");
        t1.addChildren(TreeNode.of("t1_1"));
        t1.addChildren(TreeNode.of("t1_2"));

        assertThat(t1).extracting(TreeNode::getTitle).isEqualTo("t1");
        assertThat(t1.getChildren()).hasSize(2)
            .extracting(TreeNode::getTitle)
            .contains("t1_1", "t1_2");
    }

    @Test
    void json() {
        //language=JSON
        final TreeNode node1 = Json.DEFAULT.read("{\"title\": \"t1\"}", TreeNode.class);
        assertThat(node1).extracting(TreeNode::getTitle).isEqualTo("t1");
    }

    @Test
    void json2() {
        //language=JSON
        final TreeNode node2 = Json.DEFAULT.read("{\"title\": \"t1\",\"children\":[{\"title\": " +
            "\"t2\"}]}", TreeNode.class);
        assertThat(node2).extracting(TreeNode::getTitle).isEqualTo("t1");
        assertThat(node2.getChildren()).hasSize(1).extracting(TreeNode::getTitle).contains("t2");
    }

    @Test
    void json3() {
        //language=JSON
        final TreeNode node3 = Json.DEFAULT.read("{\"title\": \"t1\",\"children\":[{\"title\": " +
            "\"t2\"}],\"m1\": 1}", TreeNode.class);
        assertThat(node3).extracting(TreeNode::getTitle).isEqualTo("t1");
        assertThat(node3.getChildren()).hasSize(1).extracting(TreeNode::getTitle).contains("t2");
        assertThat(node3.getDynamic()).containsEntry("m1", 1);
    }
}