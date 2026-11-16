package practice_problems;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * CATEGORY A - PRACTICE PROBLEM 2: Ride Fare Engine
 *
 * TASK
 * A ride app prices autos, cabs and bikes differently; all rides are priced together in one loop.
 *
 * FARE RULES
 * - Auto : base 30 + 15 per km.
 * - Cab  : base 50 + 20 per km, then the WHOLE fare is multiplied by the surge factor
 *          (surge applied after the base calculation).
 * - Bike : base 20 + 10 per km, plus 2 per waiting minute (waiting charge added after the
 *          distance charge).
 *
 * POLYMORPHISM
 * All rides live in one Ride[] array; the loop calls calculateFare() through the abstract
 * supertype and dynamic dispatch picks the right subclass implementation.
 *
 * COMPLEXITY
 * Pricing all n rides: O(n) time (each ride priced once), O(n) space to hold the rides.
 *
 * INPUT  : one ride per line, e.g.
 *            Auto 4 km
 *            Cab 6 km surge 1.5
 *            Bike 3 km waiting 5 min
 * OUTPUT : "Type: fare" per ride, then "Total: t".
 */
public class Problem2_RideFareEngine {

    /** Abstract supertype: defines the shared distance field and the fare contract. */
    abstract static class Ride {
        double distance;                    // km

        Ride(double distance) {
            this.distance = distance;
        }

        abstract double calculateFare();    // every subclass prices itself differently

        abstract String type();
    }

    static class Auto extends Ride {
        Auto(double distance) {
            super(distance);
        }

        @Override
        double calculateFare() {
            return 30 + 15 * distance;      // base 30 + 15 per km
        }

        @Override
        String type() {
            return "Auto";
        }
    }

    static class Cab extends Ride {
        double surge;                       // 1.0 <= surge <= 3.0

        Cab(double distance, double surge) {
            super(distance);
            this.surge = surge;
        }

        @Override
        double calculateFare() {
            return (50 + 20 * distance) * surge;   // surge AFTER the base calculation
        }

        @Override
        String type() {
            return "Cab";
        }
    }

    static class Bike extends Ride {
        int waitingMinutes;

        Bike(double distance, int waitingMinutes) {
            super(distance);
            this.waitingMinutes = waitingMinutes;
        }

        @Override
        double calculateFare() {
            return (20 + 10 * distance) + 2 * waitingMinutes; // waiting charge after distance charge
        }

        @Override
        String type() {
            return "Bike";
        }
    }

    private static final String DEMO = "Auto 4 km\nCab 6 km surge 1.5\nBike 3 km waiting 5 min\n";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) {                // nothing piped in -> run the example from the PDF
            sc = new Scanner(DEMO);
        }

        List<Ride> rides = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            rides.add(parseRide(line.split("\\s+")));
        }

        double grandTotal = 0;
        for (Ride ride : rides) {           // ONE loop prices every ride via dynamic dispatch
            double fare = ride.calculateFare();
            grandTotal += fare;
            System.out.println(ride.type() + ": " + fmt(fare));
        }
        System.out.println("Total: " + fmt(grandTotal));
    }

    /** Parses "Cab 6 km surge 1.5" / "Bike 3 km waiting 5 min" / "Auto 4 km". */
    private static Ride parseRide(String[] tokens) {
        String type = tokens[0];
        double distance = 0;
        double surge = 1.0;
        int waiting = 0;
        String mode = "";                   // which keyword the next number belongs to
        for (int i = 1; i < tokens.length; i++) {
            String t = tokens[i];
            if (t.equalsIgnoreCase("surge")) {
                mode = "surge";
            } else if (t.equalsIgnoreCase("waiting")) {
                mode = "wait";
            } else if (isNumber(t)) {
                double v = Double.parseDouble(t);
                if (mode.equals("surge")) {
                    surge = v;
                } else if (mode.equals("wait")) {
                    waiting = (int) v;
                } else {
                    distance = v;
                }
            }
        }
        switch (type) {
            case "Auto": return new Auto(distance);
            case "Cab":  return new Cab(distance, surge);
            case "Bike": return new Bike(distance, waiting);
            default:
                throw new IllegalArgumentException("Unknown ride type: " + type);
        }
    }

    private static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Print whole numbers without a decimal point (90 not 90.0). */
    static String fmt(double v) {
        return v == Math.rint(v) ? String.valueOf((long) v) : String.valueOf(v);
    }
}
