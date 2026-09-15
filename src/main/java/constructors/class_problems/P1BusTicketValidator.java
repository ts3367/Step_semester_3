package constructors.class_problems;

import java.util.HashSet;
import java.util.Set;

// P1. Bus Ticket Booking Validator — class problems (practice)
// Validates at construction, rejects duplicates, tracks checked-in state.
public class P1BusTicketValidator {

    public static class BusTicket {
        private final String passengerName;
        private final String destination;
        private boolean checkedIn;

        // No no-arg constructor exists — only this parameterized one.
        public BusTicket(String passengerName, String destination) {
            String p = validateName(passengerName);
            String d = validateDestination(destination);
            this.passengerName = p;
            this.destination = d;
            this.checkedIn = false;
        }

        // Name: non-null, trimmed not empty, no digits, letters/spaces only,
        // at least 2 characters after trim.
        private static String validateName(String name) {
            if (name == null) throw new IllegalArgumentException("passengerName is null");
            String t = name.trim();
            if (t.isEmpty()) throw new IllegalArgumentException("passengerName blank/whitespace");
            if (t.length() < 2) throw new IllegalArgumentException("passengerName too short");
            if (!t.matches("[A-Za-z]+([ ]+[A-Za-z]+)*"))
                throw new IllegalArgumentException("passengerName must contain only letters and spaces, no digits: " + name);
            return t;
        }

        // Destination: same rules — meaningful non-blank, no digits.
        private static String validateDestination(String dest) {
            if (dest == null) throw new IllegalArgumentException("destination is null");
            String t = dest.trim();
            if (t.isEmpty()) throw new IllegalArgumentException("destination blank/whitespace");
            if (t.length() < 2) throw new IllegalArgumentException("destination too short");
            if (!t.matches("[A-Za-z]+([ ]+[A-Za-z]+)*"))
                throw new IllegalArgumentException("destination must contain only letters and spaces: " + dest);
            return t;
        }

        public void markCheckedIn() {
            if (checkedIn) {
                System.out.println(passengerName + " -> " + destination + " already checked in!");
            } else {
                checkedIn = true;
                System.out.println(passengerName + " -> " + destination + " checked in.");
            }
        }

        public String getPassengerName() { return passengerName; }
        public String getDestination() { return destination; }

        // Batch processor: classifies each raw attempt as accepted/rejected/duplicate.
        public static void processBatch(String[][] rawBookings) {
            if (rawBookings == null) {
                System.out.println("Valid: 0 | Rejected: 0 | Duplicates skipped: 0");
                return;
            }
            Set<String> seen = new HashSet<>();
            int valid = 0, rejected = 0, duplicates = 0;
            for (String[] entry : rawBookings) {
                if (entry == null || entry.length < 2) {
                    rejected++;
                    continue;
                }
                String rawName = entry[0];
                String rawDest = entry[1];
                try {
                    BusTicket t = new BusTicket(rawName, rawDest);
                    String key = t.passengerName.toLowerCase() + "|" + t.destination.toLowerCase();
                    if (!seen.add(key)) {
                        duplicates++;
                    } else {
                        valid++;
                    }
                } catch (IllegalArgumentException e) {
                    rejected++;
                }
            }
            System.out.println("Valid: " + valid + " | Rejected: " + rejected + " | Duplicates skipped: " + duplicates);
        }
    }

    public static void main(String[] args) {
        String[][] batch = {
            {"Divya", "Chennai"},
            {"", "Bangalore"},
            {"Ravi123", "Pune"},
            {"Divya", "Chennai"},
            {" ", " "}
        };
        BusTicket.processBatch(batch);
        // Expected: Valid: 1 | Rejected: 3 | Duplicates skipped: 1
        //  ""/Bangalore -> blank name rejected
        //  Ravi123/Pune  -> name contains digits rejected
        //  " "/" "       -> whitespace-only rejected

        BusTicket t = new BusTicket("Divya", "Chennai");
        t.markCheckedIn();
        t.markCheckedIn(); // second call warns
    }
}
