package inheritance_polymorphism.assignment_problems;

// P1. Race Entry Foundation & Batch Bib Validator
// RaceEntry is the shared foundation; RunnerEntry is single-inheritance specialization.
public class P1RaceEntryFoundation {

    public static class RaceEntry {
        private final String bibNumber;
        private final double entryFee;
        private double balanceDue;

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
        public double getEntryFee() { return entryFee; }

        public String announce() {
            return "Race Entry | Bib: " + bibNumber + " | Balance: " + getBalanceDue();
        }

        public static String registerBatch(String[] bibNumbers, double entryFee) {
            if (bibNumbers == null) return "Registered: 0 | Rejected: 0";
            int registered = 0, rejected = 0;
            for (String bib : bibNumbers) {
                try {
                    new RaceEntry(bib, entryFee);
                    registered++;
                } catch (IllegalArgumentException e) {
                    rejected++;
                }
            }
            return "Registered: " + registered + " | Rejected: " + rejected;
        }
    }

    public static class RunnerEntry extends RaceEntry {
        private final String category;

        public RunnerEntry(String bibNumber, double entryFee, String category) {
            super(bibNumber, entryFee);
            this.category = category;
        }

        public String getCategory() { return category; }

        @Override
        public String announce() {
            return "Runner Entry | Bib: " + getBibNumber() + " | Category: " + category
                    + " | Balance: " + getBalanceDue();
        }
    }

    public static String registerBatch(String[] bibNumbers, double entryFee) {
        return RaceEntry.registerBatch(bibNumbers, entryFee);
    }

    public static void main(String[] args) {
        try {
            new RaceEntry("B1", 50);
            System.out.println("constructed");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        RunnerEntry r = new RunnerEntry("BIB2001", 80, "Open 10K");
        r.pay(30);
        System.out.println(r.getBalanceDue()); // 50.0
        System.out.println(registerBatch(new String[]{"BIB1", "B1", "BIB2"}, 80));
        // Expected: Registered: 2 | Rejected: 1
    }
}
