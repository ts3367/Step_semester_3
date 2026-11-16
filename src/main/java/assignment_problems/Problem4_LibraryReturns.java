package assignment_problems;

import java.util.Scanner;

/**
 * CATEGORY A - CODING ASSIGNMENT, PROBLEM 4: Library Returns by Due Day
 *
 * TASK
 * Returned books are chained in drop order, each with the day it was due. Sort the chain by due
 * day (overdue books first) keeping the drop order for equal due days. Merge sort on the linked
 * list, changing LINKS ONLY - no value copying into arrays. Each node holds a title and a due day.
 *
 * ALGORITHM
 * 1. Split the list in half with slow/fast pointers (fast moves 2 steps, so slow ends just before
 *    the middle; cut the link after the middle node).
 * 2. Sort both halves recursively.
 * 3. Merge the halves by relinking nodes; on a tie take from the LEFT half ("<="), which makes
 *    the sort stable and preserves drop order for the same due day.
 *
 * COMPLEXITY
 * - Time : O(n log n) - log n split levels, O(n) merge work per level.
 * - Space: O(log n) recursion stack; NO auxiliary array - merging only re-points the existing
 *          nodes' next links, which is the O(1)-extra-space merge arrays cannot offer.
 *
 * WHY MERGE SORT SUITS LINKED LISTS BETTER THAN QUICK SORT
 * Merge sort only needs sequential access (split via pointers, merge by relinking) and needs no
 * extra array, while quick sort's partitioning relies on random access and index swaps that are
 * unnatural on a list. Merge sort is also stable and always O(n log n), whereas quick sort
 * degrades to O(n^2) on already-sorted input and is hard to make stable on links.
 *
 * INPUT  : "Title:day" pairs chained with "->" (empty input = empty list).
 * OUTPUT : the sorted list, e.g. AI:1 -> DSA:3 -> DBMS:3 -> OS:9 -> CN:12 -> Java:15 -> null
 *
 * Run:   java -cp target/classes assignment_problems.Problem4_LibraryReturns < input.txt
 */
public class Problem4_LibraryReturns {

    /** Node holds a title and a due day. */
    static class Node {
        String title;
        int dueDay;
        Node next;

        Node(String title, int dueDay) {
            this.title = title;
            this.dueDay = dueDay;
        }
    }

    /**
     * Required signature: merge sort the chain by dueDay (stable). Links change only.
     */
    public static Node sortByDueDay(Node head) {
        if (head == null || head.next == null) {
            return head;                    // 0 or 1 node: already sorted
        }

        // 1) split: slow ends at the node BEFORE the middle; cut the link after it
        Node slow = head;
        Node fast = head.next;              // head start so the first half is the shorter/equal one
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        Node secondHalf = slow.next;
        slow.next = null;                   // cut

        // 2) sort both halves
        Node left = sortByDueDay(head);
        Node right = sortByDueDay(secondHalf);

        // 3) merge by relinking (no new nodes, no value copies)
        return merge(left, right);
    }

    /** Stable merge: on equal due days the LEFT (earlier-dropped) book comes first. */
    private static Node merge(Node a, Node b) {
        Node dummy = new Node("", 0);       // dummy head keeps the first pick special-case-free
        Node tail = dummy;
        while (a != null && b != null) {
            if (a.dueDay <= b.dueDay) {     // "<=" = take from the left half on ties -> stable
                tail.next = a;
                a = a.next;
            } else {
                tail.next = b;
                b = b.next;
            }
            tail = tail.next;
        }
        tail.next = (a != null) ? a : b;    // one half ran out: append the remainder
        return dummy.next;
    }

    private static final String DEMO =
            "Java:15 -> DSA:3 -> OS:9 -> DBMS:3 -> CN:12 -> AI:1";   // PDF sample 1

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = "";
        if (sc.hasNextLine()) {
            input = sc.nextLine();
        }
        if (input.isBlank()) {              // NOTE: empty input IS sample 2 -> prints "null"
            input = DEMO;                   // remove this line to exercise the empty-list case
        }

        Node head = buildList(input);
        printList(sortByDueDay(head));
    }

    /** Parses "Java:15 -> DSA:3 -> OS:9" (tolerates extra spaces / commas). */
    private static Node buildList(String input) {
        Node head = null;
        Node tail = null;
        for (String piece : input.split("->")) {
            String entry = piece.trim().replace(",", "");
            if (entry.isEmpty()) {
                continue;
            }
            int colon = entry.lastIndexOf(':');
            Node node = new Node(entry.substring(0, colon).trim(),
                    Integer.parseInt(entry.substring(colon + 1).trim()));
            if (head == null) {
                head = node;
            } else {
                tail.next = node;
            }
            tail = node;
        }
        return head;
    }

    private static void printList(Node head) {
        StringBuilder sb = new StringBuilder();
        for (Node cur = head; cur != null; cur = cur.next) {
            sb.append(cur.title).append(':').append(cur.dueDay).append(" -> ");
        }
        sb.append("null");
        System.out.println(sb);
    }
}
