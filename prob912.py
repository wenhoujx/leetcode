from typing import List


class Solution:
    def sortArray(self, nums: List[int]) -> List[int]:
        def partition(left, right): 
            print(left, right)
            if left >= right: 
                return 

            mid = (left + right) // 2 
            pivot = nums[mid]
            nums[mid], nums[right] = nums[right], nums[mid]
            less = sum((1 for n in nums[left:right] if n < pivot))
            iless = left 
            iequal = left + less 
            for j in range(left, right): 
                if nums[j] < pivot: 
                    nums[iless], nums[j] = nums[j], nums[iless]
                    iless +=1 
                elif nums[j] == pivot: 
                    nums[iequal], nums[j] = nums[j], nums[iequal]
                    iequal += 1 
                else: 
                    continue 

            # print(nums, pivot, left, iless, iequal, right )
            nums[iequal] , nums[right] = nums[right], nums[iequal]
            print('left', left, iless-1)
            partition(left, iless-1)
            print('right', iequal+1, right)
            partition(iequal+1 , right)
        
        partition(0, len(nums) -1 )
        return nums 

if __name__ == "__main__":
    arr = [5,1,1,2,0,0]
    Solution().sortArray(arr)
    print(arr)

    
