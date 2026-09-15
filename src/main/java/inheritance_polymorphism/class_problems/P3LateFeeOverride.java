package inheritance_polymorphism.class_problems;

import java.util.Arrays;

// P3. Late-Registration Penalty Override & Audit Trail
// WorkshopTicket overrides applyLateFee, doubling via super, with private audit array.
public class P3LateFeeOverride {

    public static class EventTicket {
        private final String attendeeId;
        private final double basePrice;
        private double balanceDue;
        private final double[] lateFeeHistory = new double[10];
        private int lateFeeCount = 0;

        public EventTicket(String attendeeId, double basePrice) {
            if (attendeeId == null || attendeeId.trim().isEmpty() || attendeeId.trim().length() < 4)
                throw new IllegalArgumentException("attendeeId rejected");
            this.attendeeId = attendeeId.trim();
            this.basePrice = basePrice;
            this.balanceDue = basePrice;
        }

        public EventTicket(double basePrice) {
            this("AUTO", basePrice);
        }

        public void pay(double amount) {
            if (amount <= 0) return;
            balanceDue -= amount;
        }

        public double getBalanceDue() {
            return balanceDue;
        }

        protected void applyLateFee(double amount) {
            balanceDue += amount;
            if (lateFeeCount < lateFeeHistory.length) {
                lateFeeHistory[lateFeeCount++] = amount;
            }
        }

        // Defensive copy: never return the real internal array.
        public double[] getLateFeeHistory() {
            return Arrays.copyOf(lateFeeHistory, lateFeeCount);
        }

        public String printTicket() {
            return "Standard Event Ticket | Balance Due: " + getBalanceDue();
        }
    }

    public static class WorkshopTicket extends EventTicket {
        private final String track;

        public WorkshopTicket(String attendeeId, double basePrice, String track) {
            super(attendeeId, basePrice);
            this.track = track;
        }

        public WorkshopTicket(double basePrice, String track) {
            super("AUTO", basePrice);
            this.track = track;
        }

        // Convenience for simplified example new WorkshopTicket(1200)
        public WorkshopTicket(double basePrice) {
            super("AUTO", basePrice);
            this.track = "General";
        }

        public String getTrack() { return track; }

        @Override
        protected void applyLateFee(double amount) {
            super.applyLateFee(amount * 2);
        }

        @Override
        public String printTicket() {
            return "Workshop Ticket | Track: " + track + " | Balance Due: " + getBalanceDue();
        }
    }

    public static void main(String[] args) {
        WorkshopTicket w = new WorkshopTicket("STU2", 1200, "AI/ML");
        w.pay(1200);
        w.applyLateFee(100);
        System.out.println(w.getBalanceDue()); // 200.0
        double[] history = w.getLateFeeHistory();
        System.out.println(Arrays.toString(history)); // [200.0]
        history[0] = 999;
        System.out.println(Arrays.toString(w.getLateFeeHistory())); // [200.0]
    }
}
