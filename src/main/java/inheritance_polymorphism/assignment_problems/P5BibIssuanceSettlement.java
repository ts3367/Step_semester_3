package inheritance_polymorphism.assignment_problems;

// P5. Race-Wide Bib Issuance, Discount Codes & Nightly Settlement Engine
// final entryCode from static counter; char-by-char discount validation;
// overloaded pay reusing flat version; instanceof settlement with null-safety.
public class P5BibIssuanceSettlement {

    public static class RaceEntry {
        private static int bibCounter = 0;
        private final String entryCode;
        private final String bibNumber;
        private final double entryFee;
        private double balanceDue;

        public RaceEntry(String bibNumber, double entryFee) {
            if (bibNumber == null || bibNumber.trim().isEmpty() || bibNumber.trim().length() < 4)
                throw new IllegalArgumentException("bibNumber rejected");
            bibCounter++;
            this.entryCode = "BIB-" + bibCounter;
            this.bibNumber = bibNumber.trim();
            this.entryFee = entryFee;
            this.balanceDue = entryFee;
        }

        public String getEntryCode() { return entryCode; }
        public String getBibNumber() { return bibNumber; }

        public void pay(double amount) {
            if (amount <= 0) return;
            balanceDue -= amount;
        }

        public void pay(double amount, String mode) {
            System.out.println("Paying via " + mode);
            pay(amount);
        }

        public double getBalanceDue() { return balanceDue; }

        public String announce() {
            return "Race Entry | Bib: " + bibNumber + " | Balance: " + getBalanceDue();
        }

        public static int getBibCounter() {
            return bibCounter;
        }

        // Format "M" + 3 digits + 1 uppercase, via charAt / isDigit / isUpperCase only.
        public static boolean isValidDiscountCode(String code) {
            if (code == null) return false;
            if (code.length() != 5) return false;
            if (code.charAt(0) != 'M') return false;
            if (!Character.isDigit(code.charAt(1))) return false;
            if (!Character.isDigit(code.charAt(2))) return false;
            if (!Character.isDigit(code.charAt(3))) return false;
            if (!Character.isUpperCase(code.charAt(4))) return false;
            return true;
        }

        public static String settleNight(RaceEntry[] entries) {
            if (entries == null) return "0 processed | 0 null skipped | 0 relay | 0 individual";
            int processed = 0, nullSkipped = 0, relay = 0, individual = 0;
            for (RaceEntry e : entries) {
                if (e == null) {
                    nullSkipped++;
                    continue;
                }
                processed++;
                if (e instanceof RelayTeamEntry) {
                    relay++;
                } else {
                    individual++;
                }
            }
            return processed + " processed | " + nullSkipped + " null skipped | "
                    + relay + " relay | " + individual + " individual";
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

    public static class EliteRunnerEntry extends RunnerEntry {
        private final double sponsorBonus;

        public EliteRunnerEntry(String bibNumber, double entryFee, String category, double sponsorBonus) {
            super(bibNumber, entryFee, category);
            this.sponsorBonus = sponsorBonus;
        }

        @Override
        public String announce() {
            return "Elite Runner | Bib: " + getBibNumber() + " | Category: " + getCategory()
                    + " | Sponsor Bonus: " + sponsorBonus + " | Balance: " + getBalanceDue();
        }
    }

    public static class RelayTeamEntry extends RaceEntry {
        private final int teamSize;

        public RelayTeamEntry(String bibNumber, double entryFee, int teamSize) {
            super(bibNumber, entryFee);
            this.teamSize = teamSize;
        }

        public int getTeamSize() { return teamSize; }

        @Override
        public String announce() {
            return "Relay Team | Bib: " + getBibNumber() + " | Team Size: " + teamSize
                    + " | Balance: " + getBalanceDue();
        }
    }

    public static boolean isValidDiscountCode(String code) {
        return RaceEntry.isValidDiscountCode(code);
    }

    public static int getBibCounter() {
        return RaceEntry.getBibCounter();
    }

    public static String settleNight(RaceEntry[] entries) {
        return RaceEntry.settleNight(entries);
    }

    public static void main(String[] args) {
        System.out.println(RaceEntry.isValidDiscountCode("M123A")); // true
        System.out.println(RaceEntry.isValidDiscountCode("M12A"));  // false
        System.out.println(RaceEntry.isValidDiscountCode("X123A")); // false
        RunnerEntry r = new RunnerEntry("BIB2001", 80, "Open 10K");
        r.pay(10, "UPI");
        EliteRunnerEntry elite = new EliteRunnerEntry("BIB3001", 150, "Elite Full Marathon", 500);
        RelayTeamEntry relay = new RelayTeamEntry("BIB4001", 300, 4);
        System.out.println(settleNight(new RaceEntry[]{elite, null, relay}));
        System.out.println(RaceEntry.getBibCounter());
    }
}
