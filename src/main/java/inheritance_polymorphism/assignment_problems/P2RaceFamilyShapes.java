package inheritance_polymorphism.assignment_problems;

// P2. Three Shapes of One Race Family
// EliteRunnerEntry extends RunnerEntry (multilevel); RelayTeamEntry extends RaceEntry (hierarchical).
public class P2RaceFamilyShapes {

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

        public String announce() {
            return "Race Entry | Bib: " + bibNumber + " | Balance: " + getBalanceDue();
        }

        public static String classifyGeneration(RaceEntry entry) {
            if (entry instanceof EliteRunnerEntry) {
                return "Multilevel descendant (3 generations deep)";
            } else if (entry instanceof RelayTeamEntry) {
                return "Hierarchical sibling (independent branch)";
            } else if (entry instanceof RunnerEntry) {
                return "Single inheritance child (2 generations deep)";
            } else {
                return "Base class (1 generation)";
            }
        }

        public static double getTotalBalanceDue(RaceEntry[] entries) {
            double total = 0;
            if (entries == null) return 0;
            for (RaceEntry e : entries) {
                if (e == null) continue;
                total += e.getBalanceDue();
            }
            return total;
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

    public static String classifyGeneration(RaceEntry entry) {
        return RaceEntry.classifyGeneration(entry);
    }

    public static double getTotalBalanceDue(RaceEntry[] entries) {
        return RaceEntry.getTotalBalanceDue(entries);
    }

    public static void main(String[] args) {
        RunnerEntry r = new RunnerEntry("BIB2001", 80, "Open 10K");
        EliteRunnerEntry e = new EliteRunnerEntry("BIB3001", 150, "Elite Full Marathon", 500);
        RelayTeamEntry t = new RelayTeamEntry("BIB4001", 300, 4);
        System.out.println(r.announce());
        System.out.println(e.announce());
        System.out.println(t.announce());
        System.out.println(classifyGeneration(e));
        System.out.println(classifyGeneration(t));
        System.out.println(getTotalBalanceDue(new RaceEntry[]{r, e, t})); // 530.0
    }
}
