package inheritance_polymorphism.assignment_problems;

// P4. Race-Day Announcer Board
// announceAll loops polymorphically with StringBuilder; instanceof guards downcast to team size.
public class P4AnnouncerBoard {

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

        protected void applyLateFee(double amount) {
            balanceDue += amount;
        }

        public double getBalanceDue() { return balanceDue; }
        public String getBibNumber() { return bibNumber; }

        public String announce() {
            return "Race Entry | Bib: " + bibNumber + " | Balance: " + getBalanceDue();
        }

        public static String announceAll(RaceEntry[] entries) {
            StringBuilder sb = new StringBuilder();
            if (entries == null) return "";
            for (RaceEntry e : entries) {
                sb.append(e.announce());
                if (e instanceof RelayTeamEntry) {
                    RelayTeamEntry relay = (RelayTeamEntry) e;
                    sb.append(" [Team size via downcast: ").append(relay.getTeamSize()).append("]");
                }
                sb.append(" | ");
            }
            return sb.toString();
        }
    }

    public static class RunnerEntry extends RaceEntry {
        private final String category;

        public RunnerEntry(String bibNumber, double entryFee, String category) {
            super(bibNumber, entryFee);
            this.category = category;
        }

        @Override
        public String announce() {
            return "Runner Entry | Bib: " + getBibNumber() + " | Category: " + category
                    + " | Balance: " + getBalanceDue();
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

    public static String announceAll(RaceEntry[] entries) {
        return RaceEntry.announceAll(entries);
    }

    public static void main(String[] args) {
        RunnerEntry runner = new RunnerEntry("BIB2001", 80, "Open 10K");
        runner.pay(30);
        // Simulate doubled late fee history path? Directly pay/fee for 90.0 balance demo:
        // 80 - 30 = 50; add doubled fee via subclass path would need applyLateFee;
        // For announcer demo, use fresh entries:
        RunnerEntry r2 = new RunnerEntry("BIB2001", 80, "Open 10K");
        RelayTeamEntry relay = new RelayTeamEntry("BIB4001", 300, 4);
        RaceEntry[] fleet = {r2, relay};
        System.out.println(announceAll(fleet));
        RaceEntry plain = new RaceEntry("BIB5001", 50);
        try {
            RelayTeamEntry bad = (RelayTeamEntry) plain;
            System.out.println(bad.getTeamSize());
        } catch (ClassCastException e) {
            System.out.println("ClassCastException at runtime");
        }
    }
}
