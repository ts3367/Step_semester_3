package constructors.assignment_problems;

// P2. ASAP or Scheduled — Delivery Slot Booking — assignment
// Two constructors linked via this(...); ASAP default typed only once.
public class P2DeliverySlotBooking {

    public static class DeliverySlot {
        private final String orderId;
        private final String timeSlot;

        // Single source of truth — full constructor.
        public DeliverySlot(String orderId, String timeSlot) {
            if (orderId == null || orderId.trim().isEmpty())
                throw new IllegalArgumentException("orderId required");
            this.orderId = orderId.trim();
            this.timeSlot = timeSlot; // allowed values are fixed slot strings or "ASAP"
        }

        // Chains via this(...) — ASAP appears only here, never duplicated.
        public DeliverySlot(String orderId) {
            this(orderId, "ASAP");
        }

        public boolean isPeakHour() {
            return "12:00-13:00".equals(timeSlot)
                || "13:00-14:00".equals(timeSlot)
                || "19:00-20:00".equals(timeSlot)
                || "20:00-21:00".equals(timeSlot);
        }

        public String getOrderId() { return orderId; }
        public String getTimeSlot() { return timeSlot; }
    }

    public static void main(String[] args) {
        DeliverySlot s1 = new DeliverySlot("ORD101", "13:00-14:00");
        System.out.println(s1.getOrderId() + " " + s1.getTimeSlot() + " peak=" + s1.isPeakHour()); // true

        DeliverySlot s2 = new DeliverySlot("ORD102");
        System.out.println(s2.getOrderId() + " " + s2.getTimeSlot() + " peak=" + s2.isPeakHour()); // false, defaults to ASAP

        DeliverySlot s3 = new DeliverySlot("ORD103", "19:00-20:00");
        System.out.println(s3.isPeakHour()); // true
    }
}
