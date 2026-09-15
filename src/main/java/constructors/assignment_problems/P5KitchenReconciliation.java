package constructors.assignment_problems;

// P5. Nightly Multi-Kitchen Reconciliation Engine — assignment (capstone)
// Combines constructor chaining, static block, final surge, instanceof, null-safety, length validation.
public class P5KitchenReconciliation {

    public static class DeliveryAccount {
        private final String studentId;
        private final double orderValue;

        private static String campusName;
        private static double defaultMinimumSurgePercent;

        static {
            campusName = "SRM Campus";
            defaultMinimumSurgePercent = 1.0;
            System.out.println("[DeliveryAccount] static block: campus=" + campusName);
        }

        public DeliveryAccount(String studentId, double orderValue) {
            if (studentId == null || studentId.trim().isEmpty())
                throw new IllegalArgumentException("studentId required");
            if (orderValue < 0) throw new IllegalArgumentException("orderValue cannot be negative");
            this.studentId = studentId.trim();
            this.orderValue = orderValue;
        }

        public DeliveryAccount(String studentId) {
            this(studentId, 0.0);
        }

        public final double calculateSurgeFee(int delayMinutes) {
            if (delayMinutes < 0) throw new IllegalArgumentException("delayMinutes cannot be negative");
            if (delayMinutes == 0) return 0.0;
            double tiered;
            if (delayMinutes <= 5) tiered = orderValue * 0.005 * delayMinutes;
            else if (delayMinutes <= 15) tiered = orderValue * 0.005 * 5 + orderValue * 0.01 * (delayMinutes - 5);
            else tiered = orderValue * 0.005 * 5 + orderValue * 0.01 * 10 + orderValue * 0.02 * (delayMinutes - 15);
            double floor = orderValue * (defaultMinimumSurgePercent / 100.0);
            return Math.max(tiered, floor);
        }

        public String getStudentId() { return studentId; }
        public double getOrderValue() { return orderValue; }
    }

    public static class PremiumDeliveryAccount extends DeliveryAccount {
        public PremiumDeliveryAccount(String studentId, double orderValue) { super(studentId, orderValue); }
        public PremiumDeliveryAccount(String studentId) { super(studentId); }
        // Premium settlement: 20% discount on surge fee (handled in processor via instanceof)
    }

    public static class ReconciliationEngine {

        public void processAccount(DeliveryAccount account, double amount, int delayMinutes) {
            if (account == null) return;
            double surge = account.calculateSurgeFee(delayMinutes);
            boolean isPremium = account instanceof PremiumDeliveryAccount;
            if (isPremium) surge *= 0.80; // premium gets 20% off surge
            String kind = isPremium ? "premium" : "regular";
            System.out.println(kind + " " + account.getStudentId() + " amount=" + amount + " surge=" + String.format("%.2f", surge));
        }

        // Fail-fast on length mismatch: do not silently misalign amounts to wrong students.
        public static void processBatch(DeliveryAccount[] accounts, double[] amounts, int[] delayMinutesArray) {
            if (accounts == null || amounts == null || delayMinutesArray == null) {
                System.out.println("Batch rejected: null input array");
                return;
            }
            if (accounts.length != amounts.length || accounts.length != delayMinutesArray.length) {
                System.out.println("Batch rejected: parallel arrays length mismatch (accounts=" + accounts.length
                        + ", amounts=" + amounts.length + ", delays=" + delayMinutesArray.length + ") — refusing to misalign data.");
                return;
            }
            ReconciliationEngine engine = new ReconciliationEngine();
            int processed = 0, nullSkipped = 0, premium = 0, regular = 0;
            double grandTotal = 0;
            for (int i = 0; i < accounts.length; i++) {
                DeliveryAccount acc = accounts[i];
                if (acc == null) { nullSkipped++; continue; }
                double surge = acc.calculateSurgeFee(delayMinutesArray[i]);
                if (acc instanceof PremiumDeliveryAccount) { surge *= 0.80; premium++; }
                else regular++;
                grandTotal += surge;
                engine.processAccount(acc, amounts[i], delayMinutesArray[i]);
                processed++;
            }
            System.out.println(processed + " processed | " + nullSkipped + " null skipped | "
                    + premium + " premium | " + regular + " regular | grand total surge fees = " + String.format("%.2f", grandTotal));
        }
    }

    public static void main(String[] args) {
        DeliveryAccount[] accounts = {
            new PremiumDeliveryAccount("STU001", 500),
            null,
            new DeliveryAccount("STU002", 300)
        };
        double[] amounts = {500, 400, 300};
        int[] delays = {10, 5, 0};
        ReconciliationEngine.processBatch(accounts, amounts, delays);

        // Length mismatch — fails fast
        ReconciliationEngine.processBatch(
            new DeliveryAccount[]{new DeliveryAccount("STU003", 200)},
            new double[]{200, 100},
            new int[]{5});
    }
}
