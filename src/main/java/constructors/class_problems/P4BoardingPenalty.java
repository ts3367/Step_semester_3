package constructors.class_problems;

// P4. Tiered Boarding Penalty Calculator — class problems
// Demonstrates final at class, field, and method level together.
public final class P4BoardingPenalty {

    // Final class: cannot be subclassed — the penalty rule is locked.
    public static final class BoardingPenaltyCalculator {
        // Final field: minimum penalty percent cannot be reassigned after construction.
        private final double minimumPenaltyPercent;

        public BoardingPenaltyCalculator(double minimumPenaltyPercent) {
            if (minimumPenaltyPercent < 0)
                throw new IllegalArgumentException("minimumPenaltyPercent cannot be negative");
            this.minimumPenaltyPercent = minimumPenaltyPercent;
        }

        // Final method: subclasses (if any) cannot override the calculation rule.
        public final double calculatePenalty(double ticketFare, int minutesLate) {
            if (ticketFare < 0) throw new IllegalArgumentException("ticketFare cannot be negative");
            if (minutesLate < 0) throw new IllegalArgumentException("minutesLate cannot be negative");
            if (minutesLate == 0) return 0.0; // on-time never triggers floor

            // Tiered: 1-5 at 0.5%/min, 6-15 at 1%/min, 16+ at 2%/min — O(1) closed form.
            double tiered;
            if (minutesLate <= 5) {
                tiered = ticketFare * 0.005 * minutesLate;
            } else if (minutesLate <= 15) {
                tiered = ticketFare * 0.005 * 5 + ticketFare * 0.01 * (minutesLate - 5);
            } else {
                tiered = ticketFare * 0.005 * 5 + ticketFare * 0.01 * 10 + ticketFare * 0.02 * (minutesLate - 15);
            }
            double floor = ticketFare * (minimumPenaltyPercent / 100.0);
            return Math.max(tiered, floor);
        }

        public double getMinimumPenaltyPercent() { return minimumPenaltyPercent; }
    }

    public static void main(String[] args) {
        BoardingPenaltyCalculator calc = new BoardingPenaltyCalculator(1.0);
        System.out.println("0 min: Rs " + calc.calculatePenalty(1000, 0));  // 0.0 — floor not applied
        System.out.println("1 min: Rs " + calc.calculatePenalty(1000, 1));  // 10.0 — floor wins over 5.0 tiered
        System.out.println("16 min: Rs " + calc.calculatePenalty(1000, 16)); // 145.0 — spans all brackets
        System.out.println("5 min: Rs " + calc.calculatePenalty(1000, 5));  // 25.0 — bracket boundary
        System.out.println("15 min: Rs " + calc.calculatePenalty(1000, 15)); // 125.0
    }
}
