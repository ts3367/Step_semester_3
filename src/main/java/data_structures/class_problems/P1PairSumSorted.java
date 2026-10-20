package data_structures.class_problems;

// Pair Sum in a Sorted Array. Two-pointer scan over an ascending-sorted array.
// Complexity: O(n) time, O(1) space. A brute-force nested loop would cost O(n^2).
public class P1PairSumSorted {

    public record Pair(int first, int second) {
        public String toDisplay() {
            return "(" + first + ", " + second + ")";
        }
    }

    public static Pair pairSumSorted(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == target) {
                return new Pair(nums[left], nums[right]);
            }
            if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return null;
    }

    public static String describe(Pair pair) {
        return pair == null ? "Not Found" : pair.toDisplay();
    }

    public static void main(String[] args) {
        int[] nums = {-4, -1, 0, 3, 5, 9};
        System.out.println(describe(pairSumSorted(nums, 4)));
        System.out.println(describe(pairSumSorted(new int[]{1, 2, 3}, 100)));
    }
}
