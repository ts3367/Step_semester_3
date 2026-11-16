package assignment_problems;

import java.util.Scanner;

/**
 * CATEGORY A - CODING ASSIGNMENT, PROBLEM 5: Token Mirror Check
 *
 * TASK
 * Decide whether a singly linked list of token numbers reads the same from both ends, using O(1)
 * extra space. Links may be changed TEMPORARILY but must be exactly restored before the method
 * returns. Own Node class; java.util.LinkedList is not used.
 *
 * ALGORITHM
 * 1. Find the end of the first half with slow/fast pointers (fast moves 2 steps, so when fast
 *    stops, slow sits at the middle / end of the first half).
 * 2. Reverse the second half in place.
 * 3. Compare the two halves node by node.
 * 4. Reverse the second half AGAIN and reattach it to the middle - the list is restored.
 *
 * WHY COPYING VALUES DOES NOT MEET THE REQUIREMENT
 * Copying the values into an array (or pushing them on a stack) uses O(n) extra space - for
 * 10^5 tokens that is a linear auxiliary structure, while the problem explicitly demands O(1)
 * extra space; only pointer surgery inside the list itself keeps the footprint constant.
 *
 * COMPLEXITY
 * - Time : O(n) - one pass to the middle, one pass over the second half to reverse it, one
 *          comparison pass, one reversal to restore.
 * - Space: O(1) - only pointer variables; the list itself is untouched afterwards.
 *
 * INPUT  : the token numbers, e.g. "4 7 9 7 4".
 * OUTPUT : "Palindrome: true/false" then "List after check: ... -> null" (the restored list).
 *
 * Run:   java -cp target/classes assignment_problems.Problem5_TokenMirror < input.txt
 */
public class Problem5_TokenMirror {

    /** Own node class: just data + next (no java.util.LinkedList). */
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    /**
     * Required signature. O(1) extra space; restores the list before returning.
     */
    public static boolean isPalindrome(Node head) {
        if (head == null || head.next == null) {
            return true;                    // 0 or 1 token: trivially a palindrome
        }

        // 1) slow ends at the middle (odd length) or the last node of the first half (even)
        Node slow = head;
        Node fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // 2) reverse the second half in place: O(1) space, links only
        Node secondHalf = reverse(slow.next);

        // 3) compare the halves node by node
        Node p1 = head;
        Node p2 = secondHalf;
        boolean palindrome = true;
        while (p2 != null) {                // second half is never longer than the first
            if (p1.data != p2.data) {
                palindrome = false;
                break;                      // keep going to the restore step regardless
            }
            p1 = p1.next;
            p2 = p2.next;
        }

        // 4) restore: reverse the second half back and reattach it to the first half
        slow.next = reverse(secondHalf);
        return palindrome;
    }

    /** In-place reversal of a linked list; returns the new head. */
    private static Node reverse(Node head) {
        Node prev = null;
        Node cur = head;
        while (cur != null) {
            Node forward = cur.next;
            cur.next = prev;
            prev = cur;
            cur = forward;
        }
        return prev;
    }

    private static final String DEMO = "4 7 9 7 4";   // PDF sample 1

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) {                // nothing piped in -> run PDF sample 1
            sc = new Scanner(DEMO);
        }

        Node head = null;
        Node tail = null;
        while (sc.hasNextInt()) {
            Node node = new Node(sc.nextInt());
            if (head == null) {
                head = node;
            } else {
                tail.next = node;
            }
            tail = node;
        }

        System.out.println("Palindrome: " + isPalindrome(head));
        System.out.print("List after check: ");
        StringBuilder sb = new StringBuilder();
        for (Node cur = head; cur != null; cur = cur.next) {
            sb.append(cur.data).append(" -> ");
        }
        sb.append("null");
        System.out.println(sb);
    }
}
