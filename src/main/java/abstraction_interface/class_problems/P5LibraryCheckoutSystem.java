package abstraction_interface.class_problems;

// P5. Community Library Checkout System
// Textbook and Magazine share LibraryItem but adopt different capabilities;
// DigitalPass is only Renewable and has no link to LibraryItem at all.
public class P5LibraryCheckoutSystem {

    // Abstract class: everything the library physically lends out.
    public abstract static class LibraryItem {
        private static int itemCounter = 1000;

        private final String itemId;

        protected LibraryItem() {
            itemCounter++;
            this.itemId = "LI-" + itemCounter;
        }

        public abstract int getLoanPeriodDays();

        public String getItemId() {
            return itemId;
        }
    }

    public interface Renewable {
        String renew();
    }

    public interface Reservable {
        String reserve();
    }

    // Implements BOTH capabilities.
    public static class Textbook extends LibraryItem implements Renewable, Reservable {
        private final String title;

        public Textbook(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public int getLoanPeriodDays() {
            return 14;
        }

        @Override
        public String renew() {
            return title + " renewed";
        }

        @Override
        public String reserve() {
            return title + " reserved";
        }
    }

    // Same parent as Textbook, but only Renewable - sharing a parent never
    // forces two siblings to share the same capabilities.
    public static class Magazine extends LibraryItem implements Renewable {
        private final String title;

        public Magazine(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public int getLoanPeriodDays() {
            return 30;
        }

        @Override
        public String renew() {
            return title + " renewed";
        }
    }

    // Only Renewable, with no relationship to LibraryItem whatsoever.
    public static class DigitalPass implements Renewable {
        private final String resourceName;

        public DigitalPass(String resourceName) {
            this.resourceName = resourceName;
        }

        public String getResourceName() {
            return resourceName;
        }

        @Override
        public String renew() {
            return resourceName + " renewed";
        }
    }

    // Loops LibraryItem references and prints each loan period polymorphically.
    public static void processCheckouts(LibraryItem[] items) {
        if (items == null) return;
        for (LibraryItem item : items) {
            if (item == null) continue;
            String name = item instanceof Textbook textbook ? textbook.getTitle()
                    : item instanceof Magazine magazine ? magazine.getTitle()
                    : item.getItemId();
            System.out.println(name + ": " + item.getLoanPeriodDays() + " days");
        }
    }

    // Takes a plain Object on purpose - Reservable and LibraryItem are
    // completely independent of each other.
    public static String reserveIfSupported(Object o) {
        if (o instanceof Reservable reservable) {
            return reservable.reserve();
        }
        return "Reservation not supported";
    }

    public static void main(String[] args) {
        Textbook t = new Textbook("Java Fundamentals");
        System.out.println(t.getLoanPeriodDays());  // 14
        System.out.println(t.renew());               // Java Fundamentals renewed
        System.out.println(t.reserve());            // Java Fundamentals reserved

        Magazine m = new Magazine("Tech Monthly");
        System.out.println(reserveIfSupported(m));  // Reservation not supported

        DigitalPass d = new DigitalPass("E-Journal Access");
        System.out.println(reserveIfSupported(d));  // Reservation not supported

        // Upcasting: a Textbook stored as its parent type.
        LibraryItem ref = t;
        System.out.println(reserveIfSupported(ref)); // Java Fundamentals reserved

        processCheckouts(new LibraryItem[]{t, m});
    }
}
