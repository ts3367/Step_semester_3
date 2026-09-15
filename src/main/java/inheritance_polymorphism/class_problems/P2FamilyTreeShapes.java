package inheritance_polymorphism.class_problems;

// P2. Three Shapes of One Family Tree
// PremiumWorkshopTicket extends WorkshopTicket (multilevel, 3 deep);
// HackathonTicket extends EventTicket directly (hierarchical sibling).
public class P2FamilyTreeShapes {

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

        public double getBalanceDue() {
            return balanceDue;
        }

        public String getAttendeeId() { return attendeeId; }

        public String printTicket() {
            return "Standard Event Ticket | Balance Due: " + getBalanceDue();
        }

        // Classifier uses instanceof checks alone — no manual "type" field.
        public static String classifyGeneration(EventTicket ticket) {
            if (ticket instanceof PremiumWorkshopTicket) {
                return "Multilevel descendant (3 generations deep)";
            } else if (ticket instanceof HackathonTicket) {
                return "Hierarchical sibling (independent branch)";
            } else if (ticket instanceof WorkshopTicket) {
                return "Single inheritance child (2 generations deep)";
            } else {
                return "Base class (1 generation)";
            }
        }

        // Polymorphic total: each ticket's own getBalanceDue(), no type checks.
        public static double getTotalBalanceDue(EventTicket[] tickets) {
            double total = 0;
            if (tickets == null) return 0;
            for (EventTicket t : tickets) {
                if (t == null) continue;
                total += t.getBalanceDue();
            }
            return total;
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
            return "Workshop Ticket | Track: " + track + " | Balance Due: " + getBalanceDue();
        }
    }

    // Multilevel: EventTicket -> WorkshopTicket -> PremiumWorkshopTicket
    public static class PremiumWorkshopTicket extends WorkshopTicket {
        private final double kitFee;

        public PremiumWorkshopTicket(String attendeeId, double basePrice, String track, double kitFee) {
            super(attendeeId, basePrice, track);
            this.kitFee = kitFee;
        }

        public double getKitFee() { return kitFee; }

        @Override
        public String printTicket() {
            return "Premium Workshop Ticket | Track: " + getTrack() + " | Kit Fee: " + kitFee
                    + " | Balance Due: " + getBalanceDue();
        }
    }

    // Hierarchical sibling: extends base directly, independent of WorkshopTicket
    public static class HackathonTicket extends EventTicket {
        private final String teamName;

        public HackathonTicket(String attendeeId, double basePrice, String teamName) {
            super(attendeeId, basePrice);
            this.teamName = teamName;
        }

        public String getTeamName() { return teamName; }

        @Override
        public String printTicket() {
            return "Hackathon Ticket | Team: " + teamName + " | Balance Due: " + getBalanceDue();
        }
    }

    public static String classifyGeneration(EventTicket ticket) {
        return EventTicket.classifyGeneration(ticket);
    }

    public static double getTotalBalanceDue(EventTicket[] tickets) {
        return EventTicket.getTotalBalanceDue(tickets);
    }

    public static void main(String[] args) {
        EventTicket s = new EventTicket("STU1", 500);
        WorkshopTicket w = new WorkshopTicket("STU2", 1200, "AI/ML");
        PremiumWorkshopTicket p = new PremiumWorkshopTicket("STU3", 2000, "Cloud Native", 300);
        HackathonTicket h = new HackathonTicket("STU4", 800, "Byte Force");
        System.out.println(s.printTicket());
        System.out.println(w.printTicket());
        System.out.println(p.printTicket());
        System.out.println(h.printTicket());
        System.out.println(classifyGeneration(p));
        System.out.println(classifyGeneration(h));
        System.out.println(getTotalBalanceDue(new EventTicket[]{s, w, p, h})); // 4500.0
    }
}
