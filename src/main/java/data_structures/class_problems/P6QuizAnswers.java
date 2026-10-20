package data_structures.class_problems;

// Part B quiz answers for the data structures practice sheet.
public class P6QuizAnswers {

    public record Answer(int question, String correctOption, String reason) {
    }

    private static final Answer[] ANSWERS = {
            new Answer(1, "B",
                    "The two-pointer scan moves both ends inward, so each element is visited at most once: O(n) time "
                            + "with O(1) additional space, against O(n^2) for nested loops."),
            new Answer(2, "C",
                    "Traversal is the operation that visits every element of a structure, whether for reading, summing "
                            + "or printing."),
            new Answer(3, "C",
                    "With a prefix sum array the range sum is a subtraction of two stored values, so it is O(1) instead "
                            + "of O(n) for a fresh sum over the range."),
            new Answer(4, "C",
                    "When a feature is used constantly and users expect an immediate response, latency matters more "
                            + "than the extra memory a faster algorithm would need."),
            new Answer(5, "C",
                    "Inserting or deleting in the middle of a 1D array shifts every later element, so each such "
                            + "operation costs O(n) time."),
            new Answer(6, "C",
                    "A hash-based structure gives O(1) average-case lookup, insertion and deletion by key, which is "
                            + "exactly the mix of operations described."),
            new Answer(7, "C",
                    "Binary search discards half of the remaining range on every step, and that is only valid when the "
                            + "data is sorted."),
            new Answer(8, "A, C and E",
                    "Constant multipliers are dropped, sequential loops over different inputs add their complexities, "
                            + "and nested loops over the same input multiply them. Lower-order terms are discarded and "
                            + "the worst case is assumed, so B and D are wrong."),
            new Answer(9, "B, C and D",
                    "A sliding window keeps a contiguous range over an array, each element enters and leaves the "
                            + "window at most once, and it turns an O(n^2) or O(n * k) recomputation into O(n). The "
                            + "window size need not be fixed and the technique applies to linear structures, so A and "
                            + "E are wrong."),
            new Answer(10, "C",
                    "Inserting at the beginning or the middle of a 1D array shifts all following elements, so it costs "
                    + "O(n) time.")
    };

    public static Answer[] getAnswers() {
        return ANSWERS.clone();
    }

    public static void main(String[] args) {
        for (Answer answer : ANSWERS) {
            System.out.println("Q" + answer.question() + ": " + answer.correctOption()
                    + " - " + answer.reason());
        }
    }
}
