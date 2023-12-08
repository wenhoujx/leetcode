def main():
    print(list(permu([1, 2, 3])))
    print(list(combi([1, 2, 3] , 3 )))


def permu(lst):
    def helper(start):
        if start == len(lst):
            yield lst[:]
            return
        for i in range(start, len(lst)):
            lst[start], lst[i] = lst[i], lst[start]
            yield from helper(start + 1)
            lst[start], lst[i] = lst[i], lst[start]

    yield from helper(0)


def combi(lst, k ):
    def helper(start, path):
        if len(path) == k:
            yield path[:]
            return
        for i in range(start, len(lst)):
            path.append(lst[i])
            yield from helper(i + 1, path)
            path.pop()

    yield from helper(0, [])


if __name__ == "__main__":
    main()
