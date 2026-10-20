package data_structures.assignment_problems;

// Exam Score Band Counter. Two binary searches on the sorted score array: the
// first position at or above low, and the first position above high.
// Complexity: O(log n) time, O(1) space, against O(n) for a linear scan. The
// boundary positions must be lower and upper bounds rather than any match, since
// duplicate marks can sit on both edges of the band.
public class P4ExamScoreBandCounter {

    public static int lowerBound(int[] scores, int target) {
        int low = 0;
        int high = scores.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (scores[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    public static int upperBound(int[] scores, int target) {
        int low = 0;
        int high = scores.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (scores[mid] <= target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    public static int countInBand(int[] scores, int low, int high) {
        return upperBound(scores, high) - lowerBound(scores, low);
    }

    public static void main(String[] args) {
        int[] scores = {35, 42, 42, 50, 58, 58, 58, 63, 71, 88};
        System.out.println(countInBand(scores, 42, 58));
        System.out.println(countInBand(scores, 90, 100));
    }
}
