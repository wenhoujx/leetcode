// https://leetcode.com/problems/reverse-linked-list/
// reverse-linked-list
class ListNode(_x: Int = 0, _next: ListNode = null) {
  var next: ListNode = null
  var x: Int = _x
}

def reverseList(head: ListNode): ListNode = {
  var prev: ListNode = null
  var curr: ListNode = head
  // iterate through the list, reverse links
  while (curr != null) {
    val nextTemp = curr.next
    curr.next = prev
    prev = curr
    curr = nextTemp
  }
  prev
}

// https://leetcode.com/problems/search-insert-position/
// find the leftmost insert index for a sorted array, i.e. python bisect_left
def searchInsert(nums: Array[Int], target: Int): Int = {
  nums.zipWithIndex
    .find({ case (x, i) => x >= target }) // find the first
    .map(_._2)
    .getOrElse(nums.length) // if not found
}

// https://leetcode.com/problems/palindrome-number/
// input string contains only digits
def isPalindromeNumber(x: Int): Boolean = {
  x.toString match {
    case s if s.length <= 1 => true
    case s =>
      val (first, last) = (s.charAt(0), s.charAt(s.length - 1))
      if (first != last) {
        false
      } else {
        isPalindromeNumber(s.substring(1, s.length - 1).toInt)
      }
  }
}

println(f"isPalindromeNumber(121): ${isPalindromeNumber(121)}")
println(f"isPalindromeNumber(-121): ${isPalindromeNumber(-121)}")

// https://leetcode.com/problems/maximum-subarray/
// classic kadane's algorithm: find the max subarray sum
def maxSubArray(nums: Array[Int]): Int = {
  var maxSoFar = nums(0)
  var maxEndingHere = nums(0)
  nums.tail
    .foldLeft((maxSoFar, maxEndingHere)) {
      case ((maxSoFar, maxEndingHere), num) =>
        val newMaxEndingHere = math.max(maxEndingHere + num, num)
        val newMaxSoFar = math.max(maxSoFar, newMaxEndingHere)
        (newMaxSoFar, newMaxEndingHere)
    }
    ._1
}

println(
  f"maxSubArray([-2,1,-3,4,-1,2,1,-5,4]): ${maxSubArray(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4))} [4,-1,2,1]"
)

// https://leetcode.com/problems/climbing-stairs/
// classic DP problem, can take either 1 step or 2 steps
// starting from 0
def climbStairs(n: Int): Int = {
  if (n == 1) { return 1 }
  if (n == 2) {
    return 2 // 1,1 or 2
  }

  (3 to n)
    .foldLeft((1, 2)) { case ((a, b), _) =>
      (b, a + b)
    }
    ._1
}

// https://leetcode.com/problems/valid-anagram/
// two strings with same char counts are anagram
def isAnagram(s: String, t: String): Boolean = {
  if (s.length != t.length) {
    return false
  }
  val sMap = s.groupBy(identity).mapValues(_.length)
  val tMap = t.groupBy(identity).mapValues(_.length)
  sMap == tMap
}

println(f"isAnagram('anagram', 'nagaram'): ${isAnagram("anagram", "nagaram")}")

// https://leetcode.com/problems/merge-sorted-array/
// input arr1, m, arr2, n
// arr1, arr2 are sorted
// m + n == arr1.length, arr1 starting from m is 0
// arr1 has enough space to hold arr2
// merge arr2 into arr1 sorted
def merge(nums1: Array[Int], m: Int, nums2: Array[Int], n: Int): Unit = {
  def helper(ns1: Array[Int], ns2: Array[Int]): Array[Int] = {
    (ns1.length, ns2.length) match {
      case (0, 0) => Array()
      case (0, _) => ns2
      case (_, 0) => ns1
      case (_, _) =>
        if (ns1.head < ns2.head) {
          Array(ns1.head) ++ helper(ns1.tail, ns2)
        } else {
          Array(ns2.head) ++ helper(ns1, ns2.tail)
        }
    }
  }
  helper(nums1.take(m), nums2).copyToArray(nums1)
}
var arr1 = Array(1, 2, 3, 0, 0, 0)
println(
  f"merge([1,2,3,0,0,0], 3, [2,5,6], 3): ${merge(arr1, 3, Array(2, 5, 6), 3)} ${arr1.mkString(", ")}"
)

// https://leetcode.com/problems/rotate-array/
// rotate array to the right by k steps
def rotate(nums: Array[Int], k: Int): Unit = {
  val n = nums.length
  val (left, right) = nums.splitAt(n - k % n)
  (right ++ left).copyToArray(nums)
}

// https://leetcode.com/problems/longest-common-prefix/
// given a list of strings, find the longest common prefix
def longestCommonPrefix(strs: Array[String]): String = {
  val minLength = strs.map(_.length).min
  strs
    .map(
      _.toCharArray.take(minLength)
    ) // take the first minLength chars from each string.
    .transpose
    .takeWhile(_.distinct.length == 1) // unique
    .map(_.head) // get the first char from a list of same chars.
    .mkString
}

println(
  f"longestCommonPrefix(['flower', 'flow', 'flight']): ${longestCommonPrefix(Array("flower", "flow", "flight"))}"
)

// https://leetcode.com/problems/majority-element/
// classic Boyer-Moore majority vote algorithm
def majorityElement(nums: Array[Int]): Int = {
  nums.tail
    .foldLeft((nums.head, 1)) {
      case ((_, 0), x) => (x, 1)
      case ((y, count), x) => {
        if (x == y) {
          (y, count + 1)
        } else {
          (y, count - 1)
        }
      }

    }
    ._1
}
println(f"majorityElement([3,2,3]): ${majorityElement(Array(3, 2, 3))}")

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
// find the max profit from a list of stock prices, one transaction only (buy then later sell)
def maxProfit(prices: Array[Int]): Int = {
  prices.tail
    .foldLeft((prices.head, 0)) { case ((minPrice, maxProfit), price) =>
      val newMinPrice = math.min(minPrice, price)
      val newMaxProfit = math.max(maxProfit, price - minPrice)
      (newMinPrice, newMaxProfit)
    }
    ._2
}

// https://leetcode.com/problems/valid-sudoku/
// check if a sudoku board is valid
// board is not filled, '.' is empty

def isValidSudoku(board: Array[Array[Char]]): Boolean = {
  val isValid = (xs: Array[Char]) => {
    val filtered = xs.filter(_ != '.')
    // non . cells are all unique
    filtered.length == filtered.distinct.length
  }
  val rows = board
  val cols = board.transpose
  // partition the board into 3x3 boxes
  // then flatten each box into a list
  val boxes = board
    .grouped(3)
    .flatMap(_.transpose.grouped(3))
    .map(_.flatten)
    .toArray
  // they are all 9 * 9 matrices.
  (rows ++ cols ++ boxes).forall(isValid)
}
