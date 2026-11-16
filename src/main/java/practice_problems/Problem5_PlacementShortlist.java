package practice_problems;

import java.util.Scanner;

/**
 * CATEGORY A - PRACTICE PROBLEM 5: Placement Drive Shortlist
 *
 * TASK
 * Six weekly mock tests per candidate. Build the shortlist: rank by total (highest first, stable),
 * report each candidate's best run of consecutive improvement ("test x to y"), answer CUTOFF x
 * instantly via binary search, and find two candidates whose totals add up to a target (PAIR t)
 * with two pointers.
 *
 * ALGORITHMS + COMPLEXITY (vs the brute force each replaces)
 * - Sorting       : stable merge sort, O(n log n)   (brute force: O(n^2) selection/bubble sort)
 * - Best climb    : Kadane's max-subarray over the 5 weekly changes, O(m) per candidate, m = 5
 *                   (brute force: O(m^2) over every start/end pair). Ties -> earliest run, because
 *                   the running sum is only replaced on a strict improvement.
 * - CUTOFF x      : binary search on the descending totals, O(log n) per query
 *                   (brute force: O(n) linear scan).
 * - PAIR t        : two pointers on the sorted (descending) totals, O(n) per query
 *                   (brute force: O(n^2) all-pairs check).
 *
 * "test x to y": weekly change i (1-based) sits between test i and test i+1, so a run of changes
 * from i..j is reported as "test i to j+1".
 *
 * INPUT  : n, then per candidate "Name s1 s2 s3 s4 s5 s6", then queries "CUTOFF x" / "PAIR t".
 * OUTPUT : "Shortlist:" + ranked list with best climbs, then one line per query.
 */
public class Problem5_PlacementShortlist {

    static class Candidate {
        String name;
        int[] scores = new int[6];
        int total;
        int climb;          // best gain over consecutive weekly changes (can be negative)
        int from, to;       // 1-based test range of that climb
    }

    // ------------------------------------------------------------------ stable merge sort (desc)
    private static void mergeSort(Candidate[] a, Candidate[] tmp, int lo, int hi) {
        if (hi - lo <= 1) {
            return;
        }
        int mid = (lo + hi) >>> 1;
        mergeSort(a, tmp, lo, mid);
        mergeSort(a, tmp, mid, hi);
        int i = lo, j = mid, k = lo;
        while (i < mid && j < hi) {
            // ">=" pulls from the LEFT half on equal totals -> merge sort stays STABLE,
            // so equal-total candidates keep their registration order.
            if (a[i].total >= a[j].total) {
                tmp[k++] = a[i++];
            } else {
                tmp[k++] = a[j++];
            }
        }
        while (i < mid) tmp[k++] = a[i++];
        while (j < hi)  tmp[k++] = a[j++];
        System.arraycopy(tmp, lo, a, lo, hi - lo);
    }

    // ------------------------------------------------------------------ Kadane (best climb)
    private static void computeBestClimb(Candidate c) {
        int best = Integer.MIN_VALUE;
        int cur = 0;
        int start = 0;                  // start change index (0-based) of the current run
        int bestStart = 0, bestEnd = 0;
        for (int i = 0; i < 5; i++) {   // change i: scores[i] -> scores[i+1]
            int change = c.scores[i + 1] - c.scores[i];
            if (cur < 0) {              // a negative running sum only drags us down: restart
                cur = change;
                start = i;
            } else {
                cur += change;
            }
            if (cur > best) {           // strict '>' keeps the EARLIEST run on ties
                best = cur;
                bestStart = start;
                bestEnd = i;
            }
        }
        c.climb = best;
        c.from = bestStart + 1;         // change i+1 (1-based) starts at test i+1
        c.to = bestEnd + 2;             // change j+1 (1-based) ends at test j+2
    }

    // ------------------------------------------------------------------ CUTOFF: binary search
    /** totals is sorted DESCENDING; returns how many are >= x. O(log n). */
    private static int countAtLeast(int[] totals, int x) {
        int lo = 0, hi = totals.length;         // find first index with totals[idx] < x
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (totals[mid] >= x) {
                lo = mid + 1;                   // still >= x, go right
            } else {
                hi = mid;                       // below the cutoff, go left
            }
        }
        return lo;                              // everything before lo is >= x
    }

    private static final String DEMO = String.join("\n",
            "5",
            "Asha 60 55 70 72 68 80",
            "Ravi 70 72 65 60 75 78",
            "Neha 50 65 62 70 74 84",
            "Kiran 80 70 66 72 70 60",
            "Mohan 55 60 58 64 70 73",
            "CUTOFF 405",
            "CUTOFF 410",
            "PAIR 785",
            "PAIR 900",
            "");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) {                    // nothing piped in -> run the example from the PDF
            sc = new Scanner(DEMO);
        }

        int n = sc.nextInt();
        Candidate[] list = new Candidate[n];
        for (int i = 0; i < n; i++) {
            Candidate c = new Candidate();
            c.name = sc.next();
            for (int t = 0; t < 6; t++) {
                c.scores[t] = sc.nextInt();
                c.total += c.scores[t];
            }
            computeBestClimb(c);
            list[i] = c;
        }

        mergeSort(list, new Candidate[n], 0, n);

        System.out.println("Shortlist:");
        int[] totals = new int[n];
        for (int i = 0; i < n; i++) {
            Candidate c = list[i];
            totals[i] = c.total;
            String climb = (c.climb >= 0 ? "+" : "") + c.climb;
            System.out.println((i + 1) + ". " + c.name + " " + c.total
                    + " | best climb " + climb + " (test " + c.from + " to " + c.to + ")");
        }

        while (sc.hasNext()) {
            String query = sc.next();
            if (query.equalsIgnoreCase("CUTOFF")) {
                int x = sc.nextInt();
                int count = countAtLeast(totals, x);
                System.out.println("CUTOFF " + x + ": " + count
                        + (count == 1 ? " candidate" : " candidates"));
            } else if (query.equalsIgnoreCase("PAIR")) {
                int target = sc.nextInt();
                // Two pointers on the descending list: too big -> move the BIG pointer right
                // (smaller total), too small -> move the SMALL pointer left (bigger total).
                int l = 0, r = n - 1;
                boolean found = false;
                while (l < r) {
                    int sum = totals[l] + totals[r];
                    if (sum == target) {
                        System.out.println("PAIR " + target + ": " + list[l].name + " + " + list[r].name);
                        found = true;
                        break;
                    } else if (sum > target) {
                        l++;
                    } else {
                        r--;
                    }
                }
                if (!found) {
                    System.out.println("PAIR " + target + ": no pair");
                }
            }
        }
    }
}
