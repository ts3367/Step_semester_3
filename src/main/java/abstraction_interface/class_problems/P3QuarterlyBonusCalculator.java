package abstraction_interface.class_problems;

// P3. Quarterly Bonus Calculator
// StaffMember (abstract) + Auditable (interface) - two independent axes.
public class P3QuarterlyBonusCalculator {

    // Abstract class: the bonus formula genuinely differs by role.
    public abstract static class StaffMember {
        private double baseSalary;
        private final double bonusRate;

        // The simple constructor chains to the richer one using this(...).
        public StaffMember(double baseSalary) {
            this(baseSalary, 0.10);
        }

        public StaffMember(double baseSalary, double bonusRate) {
            this.baseSalary = baseSalary;
            this.bonusRate = bonusRate;
        }

        public abstract double calculateBonus();

        // JavaBean accessors - baseSalary is private and only reachable through these.
        public double getSalary() {
            return baseSalary;
        }

        public void setSalary(double baseSalary) {
            if (baseSalary < 0) {
                System.out.println("rejected, salary unchanged");
                return;
            }
            this.baseSalary = baseSalary;
        }

        protected double getBonusRate() {
            return bonusRate;
        }
    }

    // A completely separate concern from how bonuses are calculated.
    public interface Auditable {
        String auditRecord();
    }

    public static class TeamLead extends StaffMember implements Auditable {
        private final int teamSize;

        public TeamLead(double baseSalary, int teamSize) {
            super(baseSalary);
            this.teamSize = teamSize;
        }

        public TeamLead(double baseSalary, double bonusRate, int teamSize) {
            super(baseSalary, bonusRate);
            this.teamSize = teamSize;
        }

        public int getTeamSize() {
            return teamSize;
        }

        @Override
        public double calculateBonus() {
            return getSalary() * getBonusRate();
        }

        @Override
        public String auditRecord() {
            return "TeamLead audit: " + teamSize + " team members, salary $" + getSalary();
        }
    }

    // The instanceof check is against the INTERFACE, not a specific class, so any
    // future class that adds the Auditable capability works here too.
    public static String getAuditIfApplicable(StaffMember s) {
        if (s instanceof Auditable auditable) {
            return auditable.auditRecord();
        }
        return "No audit required";
    }

    public static void main(String[] args) {
        TeamLead t = new TeamLead(60000, 5);
        System.out.println(t.calculateBonus());
        // Expected: 6000.0  (default bonusRate 0.10)

        TeamLead t2 = new TeamLead(60000, 0.20, 5);
        System.out.println(t2.calculateBonus());
        // Expected: 12000.0 (explicit bonusRate)

        t.setSalary(-5000);
        System.out.println(t.getSalary()); // 60000.0 - unchanged

        // Upcasting: a TeamLead stored as its parent type.
        StaffMember ref = t;
        System.out.println(getAuditIfApplicable(ref));
        // Expected: TeamLead audit: 5 team members, salary $60000.0
    }
}
