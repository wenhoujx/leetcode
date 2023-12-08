def reverse(n):
    prev = None
    while n:
        prev, n.next, n = n, prev, n.next
    return prev


class ListNode:
    def __init__(self, val, next=None) -> None:
        self.val = val
        self.next = next

    def __str__(self) -> str:
        return f"{self.val} -> {self.next}"

    def __repr__(self) -> str:
        return self.val


def print_list(n):
    while n:
        print(n.val, end=" -> ")
        n = n.next


if __name__ == "__main__":
    linked_list = ListNode(1, ListNode(2, ListNode(3, ListNode(4, ListNode(5)))))
    print_list(reverse(linked_list))
