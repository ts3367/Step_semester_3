package constructors.class_problems;

import java.util.Arrays;

// P2. Remainder-Fair FareSplitter — class problems
// Three constructors chained via this(...), fair penny-distribution, no money lost.
public class P2FareSplitter {

    public static class FareSplitter {
        private final String tripId;
        private final double totalFare;
        private final int passengerCount;

        // Full constructor — single source of truth for validation and assignment.
        public FareSplitter(String tripId, double totalFare, int passengerCount) {
            if (tripId == null || tripId.trim().isEmpty())
                throw new IllegalArgumentException("tripId required");
            if (totalFare < 0) throw new IllegalArgumentException("totalFare cannot be negative");
            if (passengerCount <= 0) throw new IllegalArgumentException("passengerCount must be > 0");
            this.tripId = tripId.trim();
            this.totalFare = totalFare;
            this.passengerCount = passengerCount;
        }

        // Two-arg chains to full with default passengerCount=2.
        public FareSplitter(String tripId, double totalFare) {
            this(tripId, totalFare, 2);
        }

        // One-arg provisional chains with fare 0 and count 2 — never crashes.
        public FareSplitter(String tripId) {
            this(tripId, 0.0, 2);
        }

        // Distributes fare to 2 decimal places with zero loss: remainder goes to last shares.
        public double[] fareBreakdown() {
            long totalCents = Math.round(totalFare * 100);
            long base = totalCents / passengerCount;
            long remainder = totalCents % passengerCount;
            double[] out = new double[passengerCount];
            for (int i = 0; i < passengerCount; i++) {
                long cents = base + (i >= passengerCount - remainder ? 1 : 0);
                out[i] = cents / 100.0;
            }
            return out;
        }

        public boolean isConfirmationOverdue(int confirmed, int expected) {
            return confirmed < expected;
        }

        public String getTripId() { return tripId; }
        public double getTotalFare() { return totalFare; }
        public int getPassengerCount() { return passengerCount; }
    }

    public static void main(String[] args) {
        FareSplitter s1 = new FareSplitter("TRIP001", 100000, 3);
        System.out.println(Arrays.toString(s1.fareBreakdown()));
        // Expected [33333.33, 33333.33, 33333.34] — extra paisa on last share
        System.out.println("Sum=" + Arrays.stream(s1.fareBreakdown()).sum());

        FareSplitter s2 = new FareSplitter("TRIP003");
        System.out.println(Arrays.toString(s2.fareBreakdown()));
        // Expected [0.0, 0.0]

        FareSplitter s3 = new FareSplitter("TRIP002", 100, 3);
        System.out.println(Arrays.toString(s3.fareBreakdown()));
        // 100/3 = 33.33 *2 + 33.34 = 100.0 exactly
        System.out.println("Overdue? " + s1.isConfirmationOverdue(1, 3));
    }
}
