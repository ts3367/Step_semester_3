package inheritance_polymorphism.class_problems;

// P5. Fest-Wide Ticket Issuance, Promo Codes & Nightly Settlement Engine
// final ticketId from static counter; char-by-char promo validation;
// overloaded pay reusing flat version; instanceof settlement with null-safety.
public class P5TicketIssuanceSettlement {

    public static class EventTicket {
        private static int ticketCounter = 1000;
        private final String ticketId;
        private final String attendeeId;
        private final double basePrice;
        private double balanceDue;

        public EventTicket(String attendeeId, double basePrice) {
            if (attendeeId == null || attendeeId.trim().isEmpty() || attendeeId.trim().length() < 4)
                throw new IllegalArgumentException("attendeeId rejected");
            ticketCounter++;
            this.ticketId = "TCK-" + ticketCounter;
            this.attendeeId = attendeeId.trim();
            this.basePrice = basePrice;
            this.balanceDue = basePrice;
        }

        public EventTicket(double basePrice) {
            this("AUTO", basePrice);
        }

        public String getTicketId() { return ticketId; }
        // Allow field-style access via method too
        public String ticketId() { return ticketId; }

        public void pay(double amount) {
            if (amount <= 0) return;
            balanceDue -= amount;
        }

        public void pay(double amount, String mode) {
            System.out.println("Paying via " + mode);
            pay(amount);
        }

        public double getBalanceDue() { return balanceDue; }

        public String printTicket() {
            return "Standard Event Ticket | Balance Due: " + getBalanceDue();
        }

        public static int getTicketsIssued() {
            return ticketCounter - 1000;
        }

        // Format "F" + 3 digits + 1 uppercase, via charAt / isDigit / isUpperCase only.
        public static boolean isValidPromoCode(String code) {
            if (code == null) return false;
            if (code.length() != 5) return false;
            if (code.charAt(0) != 'F') return false;
            if (!Character.isDigit(code.charAt(1))) return false;
            if (!Character.isDigit(code.charAt(2))) return false;
            if (!Character.isDigit(code.charAt(3))) return false;
            if (!Character.isUpperCase(code.charAt(4))) return false;
            return true;
        }

        public static String processNightlySettlement(EventTicket[] tickets) {
            if (tickets == null) return "0 processed | 0 null skipped | 0 group | 0 individual";
            int processed = 0, nullSkipped = 0, group = 0, individual = 0;
            for (EventTicket t : tickets) {
                if (t == null) {
                    nullSkipped++;
                    continue;
                }
                processed++;
                if (t instanceof GroupTicket) {
                    group++;
                } else {
                    individual++;
                }
            }
            return processed + " processed | " + nullSkipped + " null skipped | "
                    + group + " group | " + individual + " individual";
        }
    }

    public static class GroupTicket extends EventTicket {
        private final int groupSize;

        public GroupTicket(String attendeeId, double basePrice, int groupSize) {
            super(attendeeId, basePrice);
            this.groupSize = groupSize;
        }

        public GroupTicket(double basePrice, int groupSize) {
            super("AUTO", basePrice);
            this.groupSize = groupSize;
        }

        public int getGroupSize() { return groupSize; }

        @Override
        public String printTicket() {
            return "Group Ticket | Size: " + groupSize + " | Balance Due: " + getBalanceDue();
        }
    }

    public static boolean isValidPromoCode(String code) {
        return EventTicket.isValidPromoCode(code);
    }

    public static int getTicketsIssued() {
        return EventTicket.getTicketsIssued();
    }

    public static String processNightlySettlement(EventTicket[] tickets) {
        return EventTicket.processNightlySettlement(tickets);
    }

    public static void main(String[] args) {
        EventTicket t1 = new EventTicket(500);
        System.out.println(t1.getTicketId());
        System.out.println(EventTicket.getTicketsIssued());
        System.out.println(EventTicket.isValidPromoCode("F123A")); // true
        System.out.println(EventTicket.isValidPromoCode("F12A"));  // false
        System.out.println(EventTicket.isValidPromoCode("X123A")); // false
        t1.pay(200);
        t1.pay(200, "UPI");
        System.out.println(t1.getBalanceDue()); // 100.0
        System.out.println(processNightlySettlement(new EventTicket[]{
                new GroupTicket(2000, 5), null, new EventTicket(500)}));
    }
}
