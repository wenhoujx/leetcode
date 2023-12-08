
class ListNode:
    def __init__(self, val, nxt):
        self.val = val
        self.nxt = nxt
        
d = {}
head = ListNode(0, None)

def add(url: str) -> None:
    if url in d:
        prev = d[url]
        prev.nxt = prev.nxt.nxt
        
    d[url] = head
    newNode = ListNode(url, None)
    if head.nxt:
        d[head.nxt.val] = newNode
    newNode.nxt = head.nxt
    head.nxt = newNode


def history() -> ...:
    tmp = head.nxt
    res = []
    while tmp:
        res.append(tmp.val)
        tmp = tmp.nxt
    return res


if __name__ == "__main__":
    add("A")
    add("B")
    add("C")
    for url in history():
        print(url)  # C, B, A
    
    print("-------")

    add("B")
    add("A")
    for url in history():
      print(url)  # A, B, C
