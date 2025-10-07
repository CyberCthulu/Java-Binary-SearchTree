import java.util.*;

/**
 * Study.com - Trees / BST Assignment (Windows console app)
 *
 * Menu:
 * 1) Create a binary search tree
 * 2) Add a node
 * 3) Delete a node
 * 4) Print nodes by InOrder
 * 5) Print nodes by PreOrder
 * 6) Print nodes by PostOrder
 * 7) Exit program
 */
public class BinarySearchTree {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BSTree bst = new BSTree();

        while (true) {
            printMenu();
            int choice = readInt(sc, "Select an option (1-7): ");

            switch (choice) {
                case 1 -> {
                    // Create the exact balanced BST for 1..7 with root 4 (matching the diagram)
                    int[] data = {1, 2, 3, 4, 5, 6, 7};
                    bst = new BSTree();
                    bst.buildBalancedFromSorted(data);
                    System.out.println("Balanced Binary Search Tree created with values 1..7 (root = 4).");
                }
                case 2 -> {
                    int v = readInt(sc, "Enter a value to add: ");
                    if (bst.insert(v)) {
                        System.out.println("Inserted " + v);
                    } else {
                        System.out.println("Value " + v + " already exists. Duplicate ignored.");
                    }
                }
                case 3 -> {
                    int v = readInt(sc, "Enter a value to delete: ");
                    if (bst.delete(v)) {
                        System.out.println("Deleted " + v);
                    } else {
                        System.out.println("Value " + v + " was not found.");
                    }
                }
                case 4 -> {
                    System.out.print("InOrder:   ");
                    if (bst.isEmpty()) {
                        System.out.println("Tree is empty.");
                    } else {
                        bst.printInOrder();
                        System.out.println();
                    }
                }
                case 5 -> {
                    System.out.print("PreOrder:  ");
                    if (bst.isEmpty()) {
                        System.out.println("Tree is empty.");
                    } else {
                        bst.printPreOrder();
                        System.out.println();
                    }
                }
                case 6 -> {
                    System.out.print("PostOrder: ");
                    if (bst.isEmpty()) {
                        System.out.println("Tree is empty.");
                    } else {
                        bst.printPostOrder();
                        System.out.println();
                    }
                }
                case 7 -> {
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("Please choose a valid option (1-7).");
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("""
                ===============================
                Binary Search Tree - Menu
                ===============================
                1) Create a binary search tree
                2) Add a node
                3) Delete a node
                4) Print nodes by InOrder
                5) Print nodes by PreOrder
                6) Print nodes by PostOrder
                7) Exit program
                """);
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid integer. Please try again.");
            }
        }
    }
}

/** ------------------ BST Implementation ------------------ */
class BSTree {

    /** Node structure */
    private static class Node {
        int key;
        Node left, right;
        Node(int key) { this.key = key; }
    }

    private Node root;

    public boolean isEmpty() { return root == null; }

    /** Build the exact balanced BST from a sorted array (e.g., {1..7} -> 4 is root) */
    public void buildBalancedFromSorted(int[] sorted) {
        root = buildBalanced(sorted, 0, sorted.length - 1);
    }

    private Node buildBalanced(int[] a, int lo, int hi) {
        if (lo > hi) return null;
        int mid = (lo + hi) >>> 1;
        Node n = new Node(a[mid]);
        n.left = buildBalanced(a, lo, mid - 1);
        n.right = buildBalanced(a, mid + 1, hi);
        return n;
    }

    /** Insert (no duplicates). Returns true if inserted, false if duplicate. */
    public boolean insert(int key) {
        if (root == null) {
            root = new Node(key);
            return true;
        }
        Node cur = root, parent = null;
        while (cur != null) {
            parent = cur;
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else return false; // duplicate
        }
        if (key < parent.key) parent.left = new Node(key);
        else parent.right = new Node(key);
        return true;
    }

    /** Delete a value. Returns true if deleted, false if not found. */
    public boolean delete(int key) {
        Node cur = root, parent = null;
        while (cur != null && cur.key != key) {
            parent = cur;
            if (key < cur.key) cur = cur.left;
            else cur = cur.right;
        }
        if (cur == null) return false; // not found

        // Case 1: node with 0 or 1 child
        if (cur.left == null || cur.right == null) {
            Node child = (cur.left != null) ? cur.left : cur.right;
            if (parent == null) { // deleting root
                root = child;
            } else if (parent.left == cur) {
                parent.left = child;
            } else {
                parent.right = child;
            }
            return true;
        }

        // Case 2: node with 2 children -> replace with inorder successor
        Node succParent = cur, succ = cur.right;
        while (succ.left != null) {
            succParent = succ;
            succ = succ.left;
        }
        cur.key = succ.key; // copy successor's key

        // delete successor (which has at most one child)
        if (succParent.left == succ) succParent.left = succ.right;
        else succParent.right = succ.right;

        return true;
    }

    /** Traversals */
    public void printInOrder()  { inOrder(root); }
    public void printPreOrder() { preOrder(root); }
    public void printPostOrder(){ postOrder(root); }

    private void inOrder(Node n) {
        if (n == null) return;
        inOrder(n.left);
        System.out.print(n.key + " ");
        inOrder(n.right);
    }

    private void preOrder(Node n) {
        if (n == null) return;
        System.out.print(n.key + " ");
        preOrder(n.left);
        preOrder(n.right);
    }

    private void postOrder(Node n) {
        if (n == null) return;
        postOrder(n.left);
        postOrder(n.right);
        System.out.print(n.key + " ");
    }
}
