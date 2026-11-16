package assignment_problems;

import java.util.Scanner;

/**
 * CATEGORY A - CODING ASSIGNMENT, PROBLEM 2: Exam Script Batches
 *
 * TASK
 * Reverse a linked list in groups of k nodes; a final group with fewer than k nodes keeps its
 * original order. Links are changed only - no new nodes, no arrays. Own Node class;
 * java.util.LinkedList is not used.
 *
 * ALGORITHM (iterative, dummy node)
 * A dummy node sits before the head so the first group needs no special case. For each group:
 * 1. walk k nodes ahead to check that a full group exists (kth);
 * 2. reverse exactly those k links, with the node AFTER the group (nextGroup) as the reverse's
 *    starting "prev" so the group tail is correctly re-attached;
 * 3. connect the previous group's tail to the new group head (kth) and continue from the old
 *    group start, which is now the group tail.
 *
 * COMPLEXITY
 * - Time : O(n) - every node is visited a constant number of times.
 * - Space: O(1) - a handful of pointer variables.
 * - A recursive solution would use O(n/k) stack frames (one per group); this iterative version
 *   avoids that entirely with the dummy node + explicit re-linking.
 *
 * INPUT  : the list values, then k (also tolerates "scripts = 1 -> 2 ..., k = 3").
 * OUTPUT : the rearranged list, e.g. 3 -> 2 -> 1 -> 6 -> 5 -> 4 -> 7 -> 8 -> null
 *
 * Run:   java -cp target/classes assignment_problems.Problem2_ReverseInGroups < input.txt
 */
public class Problem2_ReverseInGroups {

    /** Own node class: just data + next (no java.util.LinkedList). */
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    /**
     * Required signature: reverse in groups of k, return the new head. O(n) time, O(1) space.
     */
    public static Node reverseInGroups(Node head, int k) {
        if (head == null || k <= 1) {
            return head;                    // nothing to regroup
        }

        Node dummy = new Node(0);           // sits before the head; never part of the list
        dummy.next = head;
        Node groupPrev = dummy;             // tail of the already-processed part

        while (true) {
            // 1) does a full group of k nodes start at groupPrev.next?
            Node kth = groupPrev;
            for (int i = 0; i < k && kth != null; i++) {
                kth = kth.next;
            }
            if (kth == null) {
                break;                      // fewer than k nodes remain: keep original order
            }

            // 2) reverse exactly [groupStart..kth]; prev starts at the node AFTER the group
            Node groupStart = groupPrev.next;
            Node nextGroup = kth.next;
            Node prev = nextGroup;
            Node cur = groupStart;
            while (cur != nextGroup) {
                Node forward = cur.next;    // remember the rest of the list
                cur.next = prev;            // flip the link backwards
                prev = cur;
                cur = forward;
            }

            // 3) prev is now the group's new head (old kth); groupStart is the new tail
            groupPrev.next = prev;          // previous group's tail -> new group head
            groupPrev = groupStart;         // continue from the new tail
        }
        return dummy.next;
    }

    private static final String DEMO = "1 2 3 4 5 6 7 8\n3\n";   // PDF sample 1

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) {                // nothing piped in -> run PDF sample 1
            sc = new Scanner(DEMO);
        }

        // Collect every integer; the LAST one is k, the rest are the list values.
        int[] ints = readAllInts(sc);
        int k = ints[ints.length - 1];
        Node head = buildList(ints, 0, ints.length - 1);

        printList(reverseInGroups(head, k));
    }

    private static int[] readAllInts(Scanner sc) {
        java.util.ArrayList<Integer> values = new java.util.ArrayList<>();
        while (sc.hasNext()) {
            String token = sc.next();
            try {
                values.add(Integer.parseInt(token));
            } catch (NumberFormatException ignored) { // skip words like "scripts", "k", "=", "->"
            }
        }
        int[] ints = new int[values.size()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = values.get(i);
        }
        return ints;
    }

    private static Node buildList(int[] values, int from, int to) {
        Node head = null;
        Node tail = null;
        for (int i = from; i < to; i++) {
            Node node = new Node(values[i]);
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
            sb.append(cur.data).append(" -> ");
        }
        sb.append("null");
        System.out.println(sb);
    }
}
