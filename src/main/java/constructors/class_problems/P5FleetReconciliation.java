package constructors.class_problems;

// P5. Nightly Fleet Reconciliation Engine — class problems (capstone)
// Combines constructor chaining, static block, final penalty, instanceof, null-safety, length validation.
public class P5FleetReconciliation {

    public static class BusTicketAccount {
        private final String bookingId;
        private final double ticketFare;

        // Class-level state initialized once via static block.
        private static String fleetName;
        private static double defaultMinimumPenaltyPercent;

        static {
            fleetName = "SRM Fleet";
            defaultMinimumPenaltyPercent = 1.0;
            System.out.println("[BusTicketAccount] static block: fleet=" + fleetName);
        }

        public BusTicketAccount(String bookingId, double ticketFare) {
            if (bookingId == null || bookingId.trim().isEmpty())
                throw new IllegalArgumentException("bookingId required");
            if (ticketFare < 0) throw new IllegalArgumentException("ticketFare cannot be negative");
            this.bookingId = bookingId.trim();
            this.ticketFare = ticketFare;
        }

        // Provisional constructor chains to full with fare 0.
        public BusTicketAccount(String bookingId) {
            this(bookingId, 0.0);
        }

        // Final penalty — subclasses cannot alter the rule. Reuses P4 tiered logic.
        public final double calculatePenalty(int minutesLate) {
            if (minutesLate < 0) throw new IllegalArgumentException("minutesLate cannot be negative");
            if (minutesLate == 0) return 0.0;
            double tiered;
            if (minutesLate <= 5) tiered = ticketFare * 0.005 * minutesLate;
            else if (minutesLate <= 15) tiered = ticketFare * 0.005 * 5 + ticketFare * 0.01 * (minutesLate - 5);
            else tiered = ticketFare * 0.005 * 5 + ticketFare * 0.01 * 10 + ticketFare * 0.02 * (minutesLate - 15);
            double floor = ticketFare * (defaultMinimumPenaltyPercent / 100.0);
            return Math.max(tiered, floor);
        }

        public String getBookingId() { return bookingId; }
        public double getTicketFare() { return ticketFare; }
    }

    // Sleeper settles differently: adds 10% surcharge on penalty as sleeper premium.
    public static class SleeperBusTicketAccount extends BusTicketAccount {
        public SleeperBusTicketAccount(String bookingId, double ticketFare) {
            super(bookingId, ticketFare);
        }
        public SleeperBusTicketAccount(String bookingId) { super(bookingId); }
    }

    public static class ReconciliationEngine {

        public void processAccount(BusTicketAccount account, double amount, int minutesLate) {
            if (account == null) return; // null-safety: skip, never throw
            double penalty = account.calculatePenalty(minutesLate);
            String kind = (account instanceof SleeperBusTicketAccount) ? "sleeper" : "regular";
            // Sleeper settles with 10% extra on penalty to model different settlement
            if (account instanceof SleeperBusTicketAccount) {
                penalty *= 1.10;
            }
            System.out.println(kind + " " + account.getBookingId() + " amount=" + amount + " penalty=" + String.format("%.2f", penalty));
        }

        // Parallel arrays: if lengths mismatch we FAIL FAST before processing anything,
        // because silently misaligning amounts to wrong bookings is worse than no run.
        public static void processBatch(BusTicketAccount[] accounts, double[] amounts, int[] minutesLateArray) {
            if (accounts == null || amounts == null || minutesLateArray == null) {
                System.out.println("Batch rejected: null input array");
                return;
            }
            if (accounts.length != amounts.length || accounts.length != minutesLateArray.length) {
                System.out.println("Batch rejected: parallel arrays length mismatch (accounts=" + accounts.length
                        + ", amounts=" + amounts.length + ", minutesLate=" + minutesLateArray.length + ") — refusing to misalign data.");
                return;
            }
            ReconciliationEngine engine = new ReconciliationEngine();
            int processed = 0, nullSkipped = 0, sleeper = 0, regular = 0;
            double grandTotal = 0;
            for (int i = 0; i < accounts.length; i++) {
                BusTicketAccount acc = accounts[i];
                if (acc == null) { nullSkipped++; continue; }
                double penalty = acc.calculatePenalty(minutesLateArray[i]);
                if (acc instanceof SleeperBusTicketAccount) { penalty *= 1.10; sleeper++; }
                else regular++;
                grandTotal += penalty;
                engine.processAccount(acc, amounts[i], minutesLateArray[i]);
                processed++;
            }
            System.out.println(processed + " processed | " + nullSkipped + " null skipped | "
                    + sleeper + " sleeper | " + regular + " regular | grand total penalties = " + String.format("%.2f", grandTotal));
        }
    }

    public static void main(String[] args) {
        BusTicketAccount[] accounts = {
            new SleeperBusTicketAccount("BK001", 2000),
            null,
            new BusTicketAccount("BK002", 1200)
        };
        double[] amounts = {1200, 900, 700};
        int[] minutesLate = {10, 5, 0};
        ReconciliationEngine.processBatch(accounts, amounts, minutesLate);

        // Length mismatch demo — fails fast
        ReconciliationEngine.processBatch(
            new BusTicketAccount[]{new BusTicketAccount("BK003", 500)},
            new double[]{500, 400},
            new int[]{5});
    }
}
