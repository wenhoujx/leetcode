from typing import List


def solution(mat: List[List[int]]) -> List[List[int]]:
    if not mat or not mat[0]:
        return mat

    m, n = len(mat), len(mat[0])
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

    def dfs(row, col, distance):
        if row < 0 or row >= m or col < 0 or col >= n or mat[row][col] <= distance:
            return

        mat[row][col] = distance

        for d in directions:
            new_row, new_col = row + d[0], col + d[1]
            dfs(new_row, new_col, distance + 1)

    for i in range(m):
        for j in range(n):
            if mat[i][j] == 0:
                # Start DFS from '0' cells
                dfs(i, j, 0)

    return mat


if __name__ == "__main__":
    mat = [[0, 0, 0], [0, 1, 0], [1, 1, 1]]
    print(solution(mat))



