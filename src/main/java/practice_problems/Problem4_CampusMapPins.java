package practice_problems;

/**
 * CATEGORY A - PRACTICE PROBLEM 4: Campus Map Pins
 *
 * TASK
 * A general pin and a hostel pin share a class-level banner and an instance-level name, and both
 * describe themselves with getDetails(). With the reference
 *
 *         CampusPin p = new HostelPin();
 *
 * print p.banner, p.name and p.getDetails() and state, per line, WHO decided the value.
 *
 * EXPECTED OUTPUT (with who decided it)
 *   banner: Campus        (decided by reference type)
 *   name:   General Pin   (decided by reference type)
 *   Details: Hostel Pin   (decided by object type)
 *
 * WHY (compile time vs run time)
 * - banner is a STATIC field: static members are HIDDEN, never overridden. p.banner is resolved
 *   against the REFERENCE type (CampusPin) at COMPILE TIME, so we see "Campus".
 * - name is an instance FIELD: fields are also hidden, not overridden; field access is resolved
 *   by the reference type at COMPILE TIME, so we see CampusPin's "General Pin".
 * - getDetails() is an overridden INSTANCE METHOD: method calls on objects are dispatched at
 *   RUN TIME from the actual object type (HostelPin). Inside HostelPin.getDetails() the bare
 *   identifier "name" means HostelPin's own field, so we see "Details: Hostel Pin".
 *
 * CONSTRAINT: p is never cast to HostelPin.
 */
public class Problem4_CampusMapPins {

    static class CampusPin {
        public static String banner = "Campus";       // class-level: one per class
        public String name = "General Pin";           // instance-level: one per object

        public String getDetails() {
            return "Details: " + name;                // 'name' here is CampusPin's field
        }
    }

    static class HostelPin extends CampusPin {
        public static String banner = "Hostel";       // HIDES CampusPin.banner (not an override)
        public String name = "Hostel Pin";            // HIDES CampusPin.name   (not an override)

        @Override
        public String getDetails() {                  // OVERRIDES CampusPin.getDetails()
            return "Details: " + name;                // 'name' here is HostelPin's own field
        }
    }

    public static void main(String[] args) {
        CampusPin p = new HostelPin();                // reference: CampusPin, object: HostelPin

        // Compile-time lookups (reference type CampusPin decides):
        System.out.println("banner: " + p.banner + " (decided by reference type)");
        System.out.println("name: " + p.name + " (decided by reference type)");

        // Run-time lookup (object type HostelPin decides):
        System.out.println(p.getDetails() + " (decided by object type)");
    }
}
