package data_structures.class_problems;

// Part C concept answers for the data structures practice sheet.
public class P7ConceptAnswers {

    public record Concept(int question, String questionTitle, String answer) {
    }

    private static final Concept[] CONCEPTS = {
            new Concept(1, "Big-O and the growth trend",
                    "Big-O describes how the running time of an algorithm grows relative to the input size, not how "
                            + "many steps it takes for one particular input, so the comparison is made as n grows "
                            + "large. Algorithm A runs in 3n + 10 steps, which grows linearly, while Algorithm B runs "
                            + "in n^2 + 2 steps, which grows quadratically; for small n the constant 10 and the "
                            + "constant 2 can make B look cheaper, but a quadratic term eventually dwarfs any linear "
                            + "term, so A is the better algorithm for large inputs. Big-O therefore keeps only the "
                            + "highest-order term and drops constant multipliers and lower-order terms, giving A an "
                            + "O(n) bound and B an O(n^2) bound, and that is the classification used to compare them."),
            new Concept(2, "Primitive and non-primitive data structures",
                    "A primitive data structure is a single value stored directly in memory, such as an int, a double "
                            + "or a boolean, with the storage layout defined by the language itself. A non-primitive "
                            + "data structure is a collection of values organised by a defined relationship, such as an "
                            + "array, a linked list, a stack, a tree or a hash table, and it is normally implemented on "
                            + "top of the primitives. They differ in purpose and memory representation: a primitive "
                            + "models a single attribute, while a non-primitive models a group of related values and "
                            + "adds the operations that make the grouping useful. A non-primitive structure is "
                            + "essential whenever related values must be kept together and looked up efficiently, for "
                            + "example a hash map from ISBN to title in a library catalog, where the relationship "
                            + "between the two values is part of the data and neither a plain array of primitives nor a "
                            + "list of separate variables can express it together with fast key lookup."),
            new Concept(3, "Why binary search needs sorted data",
                    "Binary search works by comparing the target with the middle element and then discarding half of "
                            + "the remaining range. That decision is only valid if the data is sorted, because the "
                            + "sorted order is what guarantees that everything discarded on one side of the middle "
                            + "element is either smaller or larger than the target. On an unsorted array the same "
                            + "comparison can eliminate the half that still contains the answer, so the algorithm "
                            + "silently returns the wrong result. The essential property is therefore a sorted (or "
                            + "monotonic) ordering of the collection, and the data does not have to be unique. When "
                            + "that property is missing it must be established first, and sorting costs O(n log n) "
                            + "time, so a single binary search over unsorted data is O(n log n) overall, which is "
                            + "worse than a plain linear scan at O(n) unless many searches reuse the same sorted copy.")
    };

    public static Concept[] getConcepts() {
        return CONCEPTS.clone();
    }

    public static void main(String[] args) {
        for (Concept concept : CONCEPTS) {
            System.out.println("Q" + concept.question() + " - " + concept.questionTitle());
            System.out.println(concept.answer());
            System.out.println();
        }
    }
}
