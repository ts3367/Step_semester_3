package access_modifiers.assignment_problems;

// P3. Book Copy Circulation Guard
// All fields private; constructor rejects invalid starting count;
// checkOut/checkIn silently reject transitions out of bounds.
public class P3CirculationGuard {

    public static class BookInventory {
        private final int copiesTotal;
        private int copiesAvailable;

        public BookInventory(int copiesTotal) {
            if (copiesTotal <= 0) throw new IllegalArgumentException("copiesTotal must be > 0");
            this.copiesTotal = copiesTotal;
            this.copiesAvailable = copiesTotal; // starts fully stocked
        }

        // Silently reject if nothing available.
        public void checkOut() {
            if (copiesAvailable > 0) {
                copiesAvailable--;
            }
        }

        // Silently reject if already at full capacity.
        public void checkIn() {
            if (copiesAvailable < copiesTotal) {
                copiesAvailable++;
            }
        }

        public int getCopiesAvailable() { return copiesAvailable; }
        public int getCopiesTotal() { return copiesTotal; }
    }

    public static void main(String[] args) {
        // Example 1 — 0 rejected
        try {
            new BookInventory(0);
            System.out.println("constructed");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        // Example 2 — 4th checkout rejected silently, stays at 0
        BookInventory b = new BookInventory(3);
        b.checkOut(); b.checkOut(); b.checkOut();
        b.checkOut(); // rejected — nothing left
        System.out.println(b.getCopiesAvailable()); // 0
        // Example 3 — 4th check-in rejected silently, stays at 3
        b.checkIn(); b.checkIn(); b.checkIn();
        b.checkIn(); // rejected — already full
        System.out.println(b.getCopiesAvailable()); // 3
    }
}
