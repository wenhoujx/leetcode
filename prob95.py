from typing import List, Optional


class TreeNode:
    def __init__(self, val, left=None, right=None) -> None:
        self.val = val
        self.left = left
        self.right = right

    def __str__(self) -> str:
        return str([self.val, str(self.left), str(self.right)])

    def __repr__(self) -> str:
        return str(self)


def generateTrees(n: int) -> List[Optional[TreeNode]]:
    memo = {
        1: [TreeNode(1, None, None)],
    }
    for i in range(2, n + 1):
        lst = []
        for t in memo[i - 1]:
            lst += as_right_child(t, i)
            # insert as root
            new_root = TreeNode(i, t, None)
            lst.append(new_root)
        memo[i] = lst
    return memo[n]


def as_right_child(node, i):
    val, l, r = node.val, node.left, node.right
    return [
        TreeNode(val, l, TreeNode(i, r, None)),
    ] + ([TreeNode(val, l, child) for child in as_right_child(r, i)] if r else [])


if __name__ == "__main__":
    res = generateTrees(3)
    for t in res:
        print(t)
