package constructors.assignment_problems;

// P4. Exam-Week Surge Fee Calculator — assignment
// Mirrors boarding penalty: tiered brackets + floor, final at class/method/field.
public final class P4SurgeFeeCalculator {

    public static final class SurgeFeeCalculator {
        private final double minimumSurgePercent;

        public SurgeFeeCalculator(double minimumSurgePercent) {
            if (minimumSurgePercent < 0)
                throw new IllegalArgumentException("minimumSurgePercent cannot be negative");
            this.minimumSurgePercent = minimumSurgePercent;
        }

        public final double calculateSurgeFee(double orderValue, int delayMinutes) {
            if (orderValue < 0) throw new IllegalArgumentException("orderValue cannot be negative");
            if (delayMinutes < 0) throw new IllegalArgumentException("delayMinutes cannot be negative");
            if (delayMinutes == 0) return 0.0; // on-time never triggers floor

            double tiered;
            if (delayMinutes <= 5) {
                tiered = orderValue * 0.005 * delayMinutes;
            } else if (delayMinutes <= 15) {
                tiered = orderValue * 0.005 * 5 + orderValue * 0.01 * (delayMinutes - 5);
            } else {
                tiered = orderValue * 0.005 * 5 + orderValue * 0.01 * 10 + orderValue * 0.02 * (delayMinutes - 15);
            }
            double floor = orderValue * (minimumSurgePercent / 100.0);
            return Math.max(tiered, floor);
        }

        public double getMinimumSurgePercent() { return minimumSurgePercent; }
    }

    public static void main(String[] args) {
        SurgeFeeCalculator calc = new SurgeFeeCalculator(1.0);
        System.out.println("0 min: Rs " + calc.calculateSurgeFee(500, 0));   // 0.0 floor not applied
        System.out.println("1 min: Rs " + calc.calculateSurgeFee(500, 1));   // 5.0 floor wins over 2.5 tiered
        System.out.println("16 min: Rs " + calc.calculateSurgeFee(500, 16)); // 72.5 spans all brackets
        System.out.println("5 min: Rs " + calc.calculateSurgeFee(500, 5));
        System.out.println("15 min: Rs " + calc.calculateSurgeFee(500, 15));
    }
}
