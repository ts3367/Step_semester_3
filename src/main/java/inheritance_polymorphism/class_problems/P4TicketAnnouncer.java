package inheritance_polymorphism.class_problems;

// P4. Nightly Ticket Announcer
// batchPrint loops polymorphically with StringBuilder; instanceof guards downcast to track.
public class P4TicketAnnouncer {

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

        public EventTicket(double basePrice) {
            this("AUTO", basePrice);
        }

        public void pay(double amount) {
            if (amount <= 0) return;
            balanceDue -= amount;
        }

        public double getBalanceDue() { return balanceDue; }

        public String printTicket() {
            return "Standard | Balance: " + getBalanceDue();
        }

        // Polymorphic loop with StringBuilder — no instanceof if-else for printing.
        public static String batchPrint(EventTicket[] tickets) {
            StringBuilder sb = new StringBuilder();
            if (tickets == null) return "";
            for (EventTicket t : tickets) {
                sb.append(t.printTicket());
                if (t instanceof WorkshopTicket) {
                    WorkshopTicket w = (WorkshopTicket) t;
                    sb.append(" [Track via downcast: ").append(w.getTrack()).append("]");
                }
                sb.append(" | ");
            }
            return sb.toString();
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

        public String getTrack() { return track; }

        @Override
        public String printTicket() {
            return "Workshop | Track: " + track + " | Balance: " + getBalanceDue();
        }
    }

    public static String batchPrint(EventTicket[] tickets) {
        return EventTicket.batchPrint(tickets);
    }

    public static void main(String[] args) {
        EventTicket[] list = { new EventTicket(500), new WorkshopTicket(1200, "AI/ML") };
        System.out.println(batchPrint(list));
        // Expected: Standard | Balance: 500.0 | Workshop | Track: AI/ML | Balance: 1200.0 [Track via downcast: AI/ML] |
        EventTicket plain = new EventTicket(500);
        try {
            WorkshopTicket bad = (WorkshopTicket) plain;
            System.out.println(bad.getTrack());
        } catch (ClassCastException e) {
            System.out.println("ClassCastException at runtime");
        }
    }
}
