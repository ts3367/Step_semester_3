package inheritance_polymorphism.assignment_problems;

import java.util.Arrays;

// P3. Late-Withdrawal Penalty Override & Audit Trail
// RunnerEntry overrides applyLateFee, doubling via super; private audit array with defensive copy.
public class P3WithdrawalPenaltyOverride {

    public static class RaceEntry {
        private final String bibNumber;
        private final double entryFee;
        private double balanceDue;
        private final double[] lateFeeHistory = new double[10];
        private int lateFeeCount = 0;

        public RaceEntry(String bibNumber, double entryFee) {
            if (bibNumber == null || bibNumber.trim().isEmpty() || bibNumber.trim().length() < 4)
                throw new IllegalArgumentException("bibNumber rejected");
            this.bibNumber = bibNumber.trim();
            this.entryFee = entryFee;
            this.balanceDue = entryFee;
        }

        public void pay(double amount) {
            if (amount <= 0) return;
            balanceDue -= amount;
        }

        public double getBalanceDue() { return balanceDue; }
        public String getBibNumber() { return bibNumber; }

        protected void applyLateFee(double amount) {
            balanceDue += amount;
            if (lateFeeCount < lateFeeHistory.length) {
                lateFeeHistory[lateFeeCount++] = amount;
            }
        }

        public double[] getLateFeeHistory() {
            return Arrays.copyOf(lateFeeHistory, lateFeeCount);
        }

        public String announce() {
            return "Race Entry | Bib: " + bibNumber + " | Balance: " + getBalanceDue();
        }
    }

    public static class RunnerEntry extends RaceEntry {
        private final String category;

        public RunnerEntry(String bibNumber, double entryFee, String category) {
            super(bibNumber, entryFee);
            this.category = category;
        }

        @Override
        protected void applyLateFee(double amount) {
            super.applyLateFee(amount * 2);
        }

        @Override
        public String announce() {
            return "Runner Entry | Bib: " + getBibNumber() + " | Category: " + category
                    + " | Balance: " + getBalanceDue();
        }
    }

    public static void main(String[] args) {
        RunnerEntry r = new RunnerEntry("BIB2001", 80, "Open 10K");
        r.pay(30);
        r.applyLateFee(20);
        System.out.println(r.getBalanceDue()); // 90.0
        double[] history = r.getLateFeeHistory();
        System.out.println(Arrays.toString(history)); // [40.0]
        history[0] = 999;
        System.out.println(Arrays.toString(r.getLateFeeHistory())); // [40.0]
    }
}
