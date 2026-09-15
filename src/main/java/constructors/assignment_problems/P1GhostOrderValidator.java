package constructors.assignment_problems;

// P1. Ghost Order Validator — assignment
// FoodOrder has no no-arg constructor; blank/null/whitespace rejected at construction.
public class P1GhostOrderValidator {

    public static class FoodOrder {
        private final String studentName;
        private final String dishName;
        private boolean delivered;

        // No no-arg constructor — only this validated parameterized one.
        public FoodOrder(String studentName, String dishName) {
            if (studentName == null || studentName.trim().isEmpty())
                throw new IllegalArgumentException("studentName blank/null/whitespace");
            if (dishName == null || dishName.trim().isEmpty())
                throw new IllegalArgumentException("dishName blank/null/whitespace");
            this.studentName = studentName.trim();
            this.dishName = dishName.trim();
            this.delivered = false;
        }

        public void markDelivered() {
            if (delivered) {
                System.out.println("Already delivered: " + studentName + " -> " + dishName + " (double-serve warning!)");
            } else {
                delivered = true;
                System.out.println("Delivered: " + studentName + " -> " + dishName);
            }
        }

        public String getStudentName() { return studentName; }
        public String getDishName() { return dishName; }

        public static void processBatch(String[][] rawOrders) {
            if (rawOrders == null) {
                System.out.println("Valid: 0 | Rejected: 0");
                return;
            }
            int valid = 0, rejected = 0;
            for (String[] entry : rawOrders) {
                if (entry == null || entry.length < 2) { rejected++; continue; }
                try {
                    new FoodOrder(entry[0], entry[1]);
                    valid++;
                } catch (IllegalArgumentException e) {
                    rejected++;
                }
            }
            System.out.println("Valid: " + valid + " | Rejected: " + rejected);
        }
    }

    public static void main(String[] args) {
        String[][] batch = {
            {"Ravi", "Paneer Butter Masala"},
            {"", "Chole Bhature"},
            {"Meera", " "},
            {"Divya", "Veg Biryani"}
        };
        FoodOrder.processBatch(batch);
        // Expected Valid: 2 | Rejected: 2

        FoodOrder o = new FoodOrder("Ravi", "Paneer Butter Masala");
        o.markDelivered();
        o.markDelivered(); // second call warns about double-serve
    }
}
