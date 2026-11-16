package practice_problems;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * CATEGORY A - PRACTICE PROBLEM 3: Round-Robin Lab Scheduler
 *
 * TASK
 * One lab machine is shared by several jobs. Each job runs for at most one quantum per turn;
 * an unfinished job goes to the BACK of the queue, a finished job leaves.
 *
 * ALGORITHM
 * Keep a current-time counter. Dequeue a job, run it for min(quantum, remaining); if it still
 * has time left, enqueue it again, otherwise record its finishing time.
 *
 * COMPLEXITY
 * Let n = number of jobs, T = total time required, q = quantum.
 * - Time : every quantum consumed produces exactly one printed time slice and each job is
 *          re-enqueued at most ceil(time/q) times, so O(T/q + n) slices overall.
 * - Space: the queue never holds more than n jobs -> O(n).
 *
 * INPUT  : "Quantum q" then one line per job: "Name totalTime".
 * OUTPUT : all time slices on one line, then "finish Job t, ..." in finishing order.
 */
public class Problem3_RoundRobinScheduler {

    static class Job {
        String name;
        int remaining;              // time still required
        int finishTime = -1;        // set once, when the job leaves the queue for good

        Job(String name, int timeRequired) {
            this.name = name;
            this.remaining = timeRequired;
        }
    }

    private static final String DEMO = "Quantum 3\nP1 5\nP2 3\nP3 7\n";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) {                // nothing piped in -> run the example from the PDF
            sc = new Scanner(DEMO);
        }

        int quantum = 1;
        Queue<Job> queue = new ArrayDeque<>();
        List<Job> finished = new ArrayList<>();     // jobs in the order they finish

        while (sc.hasNext()) {              // token parse: "Quantum" keyword or "Name time"
            String token = sc.next();
            if (token.equalsIgnoreCase("quantum")) {
                quantum = sc.nextInt();
            } else {
                queue.add(new Job(token, sc.nextInt()));
            }
        }

        int now = 0;
        StringBuilder slices = new StringBuilder();
        while (!queue.isEmpty()) {
            Job job = queue.poll();
            int run = Math.min(quantum, job.remaining);   // never exceed one quantum
            int start = now;
            now += run;                                   // advance the shared clock
            job.remaining -= run;

            if (slices.length() > 0) {
                slices.append(' ');
            }
            slices.append(job.name).append('(').append(start).append('-').append(now).append(')');

            if (job.remaining > 0) {
                queue.add(job);             // unfinished -> back of the line
            } else {
                job.finishTime = now;       // finished -> leaves for good
                finished.add(job);
            }
        }

        System.out.println(slices);

        StringBuilder finishLine = new StringBuilder("finish");
        for (int i = 0; i < finished.size(); i++) {
            Job job = finished.get(i);
            if (i > 0) {
                finishLine.append(',');
            }
            finishLine.append(' ').append(job.name).append(' ').append(job.finishTime);
        }
        System.out.println(finishLine);
    }
}
