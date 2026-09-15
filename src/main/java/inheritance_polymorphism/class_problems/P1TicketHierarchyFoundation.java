package inheritance_polymorphism.class_problems;

// P1. Ticket Hierarchy Foundation & Batch Registration Validator
// EventTicket is the shared foundation with validated constructor;
// WorkshopTicket is a single-inheritance specialization;
// registerBatch processes bulk attempts via try/catch.
public class P1TicketHierarchyFoundation {

    public static class EventTicket {
        private final String attendeeId;
        private final double basePrice;
        private double balanceDue;

        public EventTicket(String attendeeId, double basePrice) {
            if (attendeeId == null || attendeeId.trim().isEmpty() || attendeeId.trim().length() < 4)
                throw new IllegalArgumentException("attendeeId rejected");
            this.attendeeId = attendeeId.trim();
            this.basePrice = basePrice;
            this.balanceDue = basePrice;
        }

        // Convenience overload for simplified examples like new EventTicket(500)
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

        public String getAttendeeId() { return attendeeId; }
        public double getBasePrice() { return basePrice; }

        public String printTicket() {
            return "Standard Event Ticket | Balance Due: " + getBalanceDue();
        }

        // Batch validator: constructs one EventTicket per entry, counts rejections.
        // It must not pre-validate strings itself — only try/catch construction.
        public static String registerBatch(String[] attendeeIds, double basePrice) {
            if (attendeeIds == null) return "Registered: 0 | Rejected: 0";
            int registered = 0, rejected = 0;
            for (String id : attendeeIds) {
                try {
                    new EventTicket(id, basePrice);
                    registered++;
                } catch (IllegalArgumentException e) {
                    rejected++;
                }
            }
            return "Registered: " + registered + " | Rejected: " + rejected;
        }
    }

    public static class WorkshopTicket extends EventTicket {
        private final String track;

        // Single inheritance: forward shared fields via super(...), no duplication
        // of attendeeId or basePrice as separate fields here.
        public WorkshopTicket(String attendeeId, double basePrice, String track) {
            super(attendeeId, basePrice);
            this.track = track;
        }

        // Convenience overload for examples like new WorkshopTicket(1200, "AI/ML")
        public WorkshopTicket(double basePrice, String track) {
            super("AUTO", basePrice);
            this.track = track;
        }

        public String getTrack() { return track; }

        @Override
        public String printTicket() {
            return "Workshop Ticket | Track: " + track + " | Balance Due: " + getBalanceDue();
        }
    }

    // Outer convenience delegate (so tests calling P1...registerBatch also work)
    public static String registerBatch(String[] attendeeIds, double basePrice) {
        return EventTicket.registerBatch(attendeeIds, basePrice);
    }

    public static void main(String[] args) {
        try {
            new EventTicket("ST1", 500);
            System.out.println("constructed");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        WorkshopTicket w = new WorkshopTicket("STU2", 1200, "AI/ML");
        w.pay(500);
        System.out.println(w.getBalanceDue()); // 700.0
        System.out.println(registerBatch(new String[]{"STU1", "ST1", "STU2", " ", "STU3"}, 500));
        // Expected: Registered: 3 | Rejected: 2
    }
}
