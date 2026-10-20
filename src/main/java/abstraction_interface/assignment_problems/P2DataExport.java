package abstraction_interface.assignment_problems;

// P2. One-Click Data Export
// ReportGenerator and UserProfile share no hierarchy at all - only the
// Exportable contract, and the single shared export counter.
public class P2DataExport {

    // Shared static counter. It needs a common home because an interface cannot
    // hold mutable static state (interface fields are public static final).
    private static int totalExports = 0;

    public interface Exportable {
        String exportData();
    }

    public static class ReportGenerator implements Exportable {
        private final String reportName;

        public ReportGenerator(String reportName) {
            this.reportName = reportName;
        }

        public String getReportName() {
            return reportName;
        }

        @Override
        public String exportData() {
            totalExports++;
            return "Exported report: " + reportName;
        }
    }

    public static class UserProfile implements Exportable {
        private final String username;

        public UserProfile(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        @Override
        public String exportData() {
            totalExports++;
            return "Exported profile: " + username;
        }
    }

    // Counts every actual exportData() call, whoever made it.
    public static int getTotalExports() {
        return totalExports;
    }

    // Loops a mixed array and calls exportData() on each.
    public static void exportAll(Exportable[] items) {
        if (items == null) return;
        for (Exportable item : items) {
            if (item == null) continue;
            System.out.println(item.exportData());
        }
    }

    public static void main(String[] args) {
        ReportGenerator r = new ReportGenerator("Sales Q1");
        UserProfile u = new UserProfile("jane_doe");

        // Direct calls show the returned message.
        System.out.println(r.exportData());   // Exported report: Sales Q1
        System.out.println(u.exportData());   // Exported profile: jane_doe
        System.out.println(getTotalExports()); // 2

        // Upcasting: a ReportGenerator stored as the Exportable interface type.
        Exportable ref = r;
        exportAll(new Exportable[]{ref, u});
        // prints both messages

        System.out.println(getTotalExports()); // 4
    }
}
