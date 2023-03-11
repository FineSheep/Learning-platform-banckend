package fun.example.learningplatformbanckend;

import java.util.LinkedList;

/**
 * @author yang
 * @createTime 2023/3/6 10:30
 * @description
 */

public class Main {
    boolean result;
    LinkedList<Integer> path = new LinkedList<>();

    public boolean hasPathSum(TreeNode root, int targetSum) {
        System.out.println();
        if (root == null) return false;
        back(root, targetSum);
        return result;
    }

    public void back(TreeNode root, int targetSum) {
        if (root.left == null && root.right == null) {
            Integer count = path.stream().reduce((a, b) -> a + b).get();
            if (count == targetSum) {
                result = true;
            }
            return;
        }
        if (root.left != null) {
            path.add(root.val);
            back(root.left, targetSum);
            path.removeLast();
        }
        if (root.right != null) {
            path.add(root.val);
            back(root.right, targetSum);
            path.removeLast();
        }
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
