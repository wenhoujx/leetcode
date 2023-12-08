from typing import List


def solution1(amount: int, coins: List[int]) -> int:
    res = set()

    def helper(cur, rem, available_coins):
        print(cur, rem, available_coins)
        if rem == 0:
            res.add(tuple(cur))
            return
        if not available_coins:
            return

        coin, *available_coins = available_coins
        for i in range(rem // coin + 1):
            helper(cur + ([coin] * i), rem - i * coin, available_coins)

    helper([], amount, coins)

    return len(res) if res else 0


def main():
    solution1(5, [1, 2, 5])


if __name__ == "__main__":
    main()
