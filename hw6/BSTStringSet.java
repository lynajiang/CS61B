import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Lyna Jiang
 */
public class BSTStringSet implements SortedStringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        /**if (_root == null) {
         _root = new Node(s);
         }else {
         Node temp = this._root;
         while (temp != null) {
         if (temp.s.compareTo(s) > 0) {
         if (temp.right == null) {
         temp.right = new Node(s);
         break;
         }
         temp = temp.right;
         }
         if (temp.s.compareTo(s) < 0) {
         if (temp.left == null) {
         temp.left = new Node(s);
         break;
         }
         temp = temp.left;
         }

         if (temp == null) {

         }
         }
         }
         *
         */

        Node last = find(s);
        if (last == null) {
            _root = new Node(s);
        }

        else {
            if ((last.s).compareTo(s) > 0) {
                last.left = new Node(s);
            }
            if ((last.s).compareTo(s) < 0) {
                last.right = new Node(s);
            }
        }

    }

    private Node find(String s) {
        if (_root == null) {
            return null;
        }
        Node temp = _root;
        while (true) {
            Node next;
            int c = s.compareTo(temp.s);

            if (c > 0) {
                next = temp.right;
            }
            else if (c < 0) {
                next = temp.left;
            }
            else {
                return temp;
            }
            if (next == null) {
                return temp;
            }
            else {
                temp = next;
            }
        }



    }

    @Override
    public boolean contains(String s) {
        /**
         * if (_root == null) {
         *             return false;
         *         }
         *         Node temp = _root;
         *         while (temp != null) {
         *             if (temp.s.compareTo(s) == 0) {
         *                 return true;
         *             }
         *             if (temp.s.compareTo(s) > 0) {
         *                 if (temp.right == null) {
         *                     return false;
         *                 }
         *                 temp = temp.right;
         *             }
         *             if (temp.s.compareTo(s) < 0) {
         *                 if (temp.left == null) {
         *                     return false;
         *                 }
         *                 temp = temp.left;
         *             }
         *         }
         *         return false;
         */
        Node foundNode = find(s);
        if (foundNode != null && foundNode.s.equals(s)) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> allStrings = new ArrayList<>();
        if (_root == null) {
            return allStrings;
        }

        for (String node: this) {
            allStrings.add(node);
        }
        return allStrings;
        /**
         * BSTIterator newIterator = new BSTIterator(_root);
         *         ArrayList listOfNodes = new ArrayList();
         *         while (newIterator.hasNext()) {
         *             listOfNodes.add(newIterator.next());
         *         }
         *         ArrayList revListOfNodes = new ArrayList();
         *         for (int i = listOfNodes.size() - 1; i >= 0; i--) {
         *             revListOfNodes.add(listOfNodes.get(i));
         *         }
         *         return revListOfNodes;
         */

    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    @Override
    public Iterator<String> iterator(String low, String high) {
        return new lowHighIterator(_root, low, high);

    }
    private static class lowHighIterator implements Iterator<String> {

        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        lowHighIterator(Node node, String low, String high) {
            _low = low;
            _high = high;
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            if (node != null) {
                if (node.s.compareTo(_low) < 0) {
                    addTree(node.right);
                } else if (node.s.compareTo(_high) >= 0) {
                    addTree(node.left);
                } else {
                    _toDo.push(node);
                    node = node.left;
                }
            }
        }

        private String _low;
        private String _high;
    }


    /** Root node of the tree. */
    private Node _root;


}
