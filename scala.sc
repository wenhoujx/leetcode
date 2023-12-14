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
    def helper(ns1: Array[Int], ns2: Array[Int]): Array[Int] =  {
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
var arr1 = Array(1,2,3,0,0,0)
println(f"merge([1,2,3,0,0,0], 3, [2,5,6], 3): ${merge(arr1, 3, Array(2,5,6), 3)} ${arr1.mkString(", ")}" )

