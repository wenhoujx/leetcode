import collection.mutable.ListBuffer
import collection.mutable

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

// https://leetcode.com/problems/valid-palindrome-ii/
// if a string is valid palindrome after removing 0 or 1 char from it
def validPalindrome(s: String): Boolean = {
  val isPalindrome: String => Boolean = s => s == s.reverse
  s match {
    case s if s.length <= 1 => true
    case s =>
      val (first, last) = (s.charAt(0), s.charAt(s.length - 1))
      if (first != last) {
        // remove either first or last
        isPalindrome(s.substring(1)) || isPalindrome(
          s.substring(0, s.length - 1)
        )
      } else {
        validPalindrome(s.substring(1, s.length - 1))
      }
  }
}

println(f"validPalindrome('aba'): ${validPalindrome("aba")}")
println(f"validPalindrome('abca'): ${validPalindrome("abca")}")

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

// https://leetcode.com/problems/plus-one/
// plus one to the number represented by an array of digits,
// [1,2,9] -> [1,3,0]
def plusOne(digits: Array[Int]): Array[Int] = {
  val (carry, result): (Int, ListBuffer[Int]) =
    // use foldLeft with initial tuple
    digits.reverse.foldLeft((1, ListBuffer.empty[Int])) {
      case ((carry, result), digit) =>
        val newDigit = digit + carry
        val newCarry = newDigit / 10
        result.prepend(newDigit % 10)
        (newCarry, result)
    }
  if (carry == 1) {
    result.prepend(carry)
  }
  result.toArray
}

println(f"plusOne([1,2,9]): ${plusOne(Array(1, 2, 9)).mkString(", ")}")

// https://leetcode.com/problems/add-binary/
// add two binary strings "11" + "1" = "100"
def addBinary(a: String, b: String): String = {
  val (a1, b1) = (a.reverse, b.reverse)
  // zipAll pads the shorter list with '0'
  val (result, carry) =
    (a1.zipAll(b1, '0', '0').foldLeft((ListBuffer.empty[Char], 0))) {
      case ((result, carry), (x, y)) =>
        val newCarry = (x.asDigit + y.asDigit + carry) / 2
        val newDigit = (x.asDigit + y.asDigit + carry) % 2
        (result.prepend(newDigit.toString.head), newCarry)
    }
  if (carry == 1) {
    result.prepend('1')
  }
  result.mkString
}

println(f"addBinary('11', '1'): ${addBinary("11", "1")}")

// https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/
// find the first occurrence of a substring in a string
def strStr(haystack: String, needle: String): Int = {
  if (needle.isEmpty) {
    return 0
  }
  haystack
    // sliding window, step is default 1
    .sliding(needle.length)
    .zipWithIndex
    .find({ case (s, _) => s == needle })
    // _1 is the sliding string, _2 is the index
    .map(_._2)
    .getOrElse(-1)
}

// https://leetcode.com/problems/reverse-words-in-a-string/
// reverse words in a string, separated by one spaces
// if input string has multiple spaces between words, reduce them to one space
def reverseWords(s: String): String = {
  s.split(" ").filter(_.nonEmpty).reverse.mkString(" ")
}

println(
  "reverseWords('  the sky is   blue'): " + reverseWords("  the sky is   blue")
)

// https://leetcode.com/problems/move-zeroes/
// move zeros in an array to the end, in-place
def moveZeroes(nums: Array[Int]): Unit = {
  val (a, b) = nums.partition(_ != 0)
  // this allocation of new arrays is cheating.
  (a ++ b).copyToArray(nums)
}

val moveZeroesArr = Array(0, 1, 0, 3, 12)
println(
  f"moveZeroes(${moveZeroesArr.mkString(", ")}): ${moveZeroes(moveZeroesArr)} ${moveZeroesArr.mkString(", ")}"
)

// https://leetcode.com/problems/excel-sheet-column-title/
// A->1, Z->26, AA->27, AB->28 ...
// given the number, return the column title
def convertToTitle(n: Int): String = {
  val (result, _) = (1 to n).foldLeft((ListBuffer.empty[Char], n)) {
    case ((result, n), _) =>
      val newChar = ('A' + (n - 1) % 26).toChar
      val newN = (n - 1) / 26
      result.prepend(newChar)
      (result, newN)
  }
  result.mkString
}

// https://leetcode.com/problems/count-primes/
// count the number of prime numbers less than a non-negative number, n
def countPrimes(n: Int): Int = {
  if (n <= 2) {
    return 0
  }
  val isPrime = Array.fill(n)(true)
  isPrime(0) = false
  isPrime(1) = false
  (2 until n).foreach { i =>
    if (isPrime(i)) {
      (i * i until n by i).foreach { j =>
        // incr by j, they are all non prime
        isPrime(j) = false
      }
    }
  }
  isPrime.count(identity)
}

// https://leetcode.com/problems/happy-number/
// a happy number is defined by the following process:
// starting with any positive integer, replace the number by the sum of the squares of its digits
// repeat the process until the number equals 1 (where it will stay),
// or it loops endlessly in a cycle which does not include 1
def isHappy(n: Int): Boolean = {
  def helper(n: Int, seen: Set[Int]): Boolean = {
    if (n == 1) {
      return true
    }
    if (seen.contains(n)) {
      return false
    }
    val newSeen = seen + n
    val newN = n.toString.map(_.asDigit).map(x => x * x).sum
    helper(newN, newSeen)
  }
  helper(n, Set.empty[Int])
}

class TreeNode(
    var value: Int = 0,
    var left: TreeNode = null,
    var right: TreeNode = null
)

// https://leetcode.com/problems/maximum-depth-of-binary-tree/
// find the max depth of a binary tree
def maxDepth(root: TreeNode): Int = {
  if (root == null) {
    return 0
  }
  1 + math.max(maxDepth(root.left), maxDepth(root.right))
}

// https://leetcode.com/problems/valid-parentheses/
// check if a string of parentheses is valid, including (), [], {}
def isValidParentheses(s: String): Boolean = {
  val pairs = List("()", "[]", "{}").map(x => (x(1), x(0))).toMap
  val stack = mutable.Stack.empty[Char]
  util.control.Breaks.breakable {
    for {
      ch <- s
    } yield ch match {
      case '(' | '[' | '{' => stack.push(ch)
      case ch => {
        if (stack.isEmpty || stack.top != pairs(ch)) {
          // make sure stack is not empty
          stack.push(')')
          // break early
          util.control.Breaks.break()
        } else {
          stack.pop()
        }
      }
    }

  }
  stack.isEmpty
}

println(f"isValidParentheses('()'): ${isValidParentheses("()")}")
println(f"isValidParentheses('()[}]{}'): ${isValidParentheses("()[}]{}")}")

// https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/description/
// sorted array to balanced BST
def sortedArrayToBST(nums: Array[Int]): TreeNode = {
  if (nums.isEmpty) {
    return null
  }
  val mid = nums.length / 2
  val root = new TreeNode(nums(mid))
  root.left = sortedArrayToBST(nums.take(mid))
  root.right = sortedArrayToBST(nums.drop(mid + 1))
  root
}

// https://leetcode.com/problems/remove-duplicates-from-sorted-list/description/
// remove duplicates from a sorted linked list
def deleteDuplicates(head: ListNode): ListNode = {
  if (head == null || head.next == null) {
    return head
  }
  if (head.x == head.next.x) {
    // remove dup node.
    head.next = head.next.next
    deleteDuplicates(head)
  } else {
    head.next = deleteDuplicates(head.next)
    head
  }
}
