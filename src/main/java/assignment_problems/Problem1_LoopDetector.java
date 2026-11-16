package assignment_problems;

import java.util.Scanner;

/**
 * CATEGORY A - CODING ASSIGNMENT, PROBLEM 1: Delivery Route Loop Detector
 *
 * TASK
 * The route is a singly linked list; a data-entry bug may link the last stop back to an earlier
 * stop. Detect the loop and, if present, report the stop where it starts and how many stops are
 * in the loop. O(1) extra space: no HashSet, no visited flags, no node changes. Own Node class;
 * java.util.LinkedList is not used.
 *
 * ALGORITHM (Floyd's tortoise and hare)
 * 1. slow moves 1 step, fast moves 2 steps; if they meet, a loop exists.
 * 2. Keep one pointer at the meeting point and walk once around the cycle to count its length k.
 * 3. Put one pointer back at the head; advance BOTH one step at a time - they meet exactly at
 *    the loop start.
 *
 * WHY SLOW AND FAST MUST MEET INSIDE THE LOOP (2 lines)
 * Once both pointers are in the loop, the gap between them grows by exactly 1 each step
 * (fast gains 2, slow gains 1). Since the loop is cyclic with length k, a gap growing by 1 per
 * step must take every value 0..k-1 within k steps - including 0 - so they must meet.
 *
 * COMPLEXITY
 * - Time : O(n). The fast pointer reaches the meeting point in <= 2n single steps; counting the
 *          loop is O(k) and finding the start is O(n - k) <= O(n).
 * - Space: O(1) - only pointer variables, no auxiliary structures.
 *
 * INPUT  : the stop numbers, then pos (0-based index the last node links back to; -1 = no loop).
 *          e.g. "10 20 30 40 50" + "2"   (also tolerates "stops = 10 -> 20 ..., pos = 2")
 * OUTPUT : "Loop: no" or "Loop: yes, starts at stop s, length k"
 *
 * Run:   java -cp target/classes assignment_problems.Problem1_LoopDetector < input.txt
 */
public class Problem1_LoopDetector {

    /** Own node class: just data + next (no java.util.LinkedList). */
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    /**
     * Required signature. Returns the report string; O(1) extra space, nodes untouched.
     */
    public static String detectLoop(Node head) {
        if (head == null || head.next == null) {
            return "Loop: no";                       // 0 or 1 stop can never loop
        }

        // 1) detect with slow (1 step) and fast (2 steps)
        Node slow = head;
        Node fast = head;
        boolean hasLoop = false;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {                      // pointers met inside the loop
                hasLoop = true;
                break;
            }
        }
        if (!hasLoop) {
            return "Loop: no";                       // fast fell off the end: acyclic
        }

        // 2) count the loop: walk once round the cycle from the meeting point
        int length = 1;
        Node counter = fast.next;
        while (counter != fast) {
            length++;
            counter = counter.next;
        }

        // 3) find the start: one pointer back at the head, then both step 1 until they meet
        Node fromHead = head;
        Node fromMeeting = fast;
        while (fromHead != fromMeeting) {
            fromHead = fromHead.next;
            fromMeeting = fromMeeting.next;
        }

        return "Loop: yes, starts at stop " + fromHead.data + ", length " + length;
    }

    private static final String DEMO = "10 20 30 40 50\n2\n";   // PDF sample 1

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) {                         // nothing piped in -> run PDF sample 1
            sc = new Scanner(DEMO);
        }

        // Collect every integer in the input; the LAST one is pos, the rest are the stops.
        // This tolerates both "10 20 30 40 50 / 2" and "stops = 10 -> 20 ..., pos = 2".
        int[] ints = readAllInts(sc);
        int pos = ints[ints.length - 1];
        Node head = buildList(ints, 0, ints.length - 1, pos);

        System.out.println(detectLoop(head));
    }

    private static int[] readAllInts(Scanner sc) {
        java.util.ArrayList<Integer> values = new java.util.ArrayList<>();
        while (sc.hasNext()) {
            String token = sc.next();
            try {
                values.add(Integer.parseInt(token));
            } catch (NumberFormatException ignored) { // skip words like "stops", "pos", "=", "->"
            }
        }
        int[] ints = new int[values.size()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = values.get(i);
        }
        return ints;
    }

    /** Links stops[from..to-1] into a list; if pos >= 0, links the last node back to index pos. */
    private static Node buildList(int[] stops, int from, int to, int pos) {
        if (to <= from) {
            return null;
        }
        Node head = new Node(stops[from]);
        Node tail = head;
        Node loopEntry = null;
        if (pos == 0) {
            loopEntry = head;
        }
        for (int i = from + 1; i < to; i++) {
            tail.next = new Node(stops[i]);
            tail = tail.next;
            if (i == pos) {
                loopEntry = tail;
            }
        }
        if (pos >= 0 && loopEntry != null) {
            tail.next = loopEntry;                   // the data-entry bug
        }
        return head;
    }
}
