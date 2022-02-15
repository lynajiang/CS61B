import org.junit.Test;
import static org.junit.Assert.*;
public class redBlackTreeTest {

    @Test
    public void redBlackTreeTest1() {
        RedBlackTree<Integer> rbtree = new RedBlackTree<>();
        rbtree.insert(1);
        rbtree.insert(2);
        rbtree.insert(3);
    }

    @Test
    public void longerTest() {
        RedBlackTree<Integer> rbtree = new RedBlackTree<>();
        rbtree.insert(1);
        rbtree.insert(2);
        rbtree.insert(3);
        rbtree.insert(4);
        rbtree.insert(5);
        rbtree.insert(6);
        rbtree.insert(7);
        rbtree.insert(8);
    }
}
