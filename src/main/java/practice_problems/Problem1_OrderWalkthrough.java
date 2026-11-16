package practice_problems;

import java.util.Scanner;

/**
 * CATEGORY A - PRACTICE PROBLEM 1: Order Walkthrough with Nested Types
 *
 * TASK
 * An online order stores its line items in an internal array. Staff walk through the order one
 * line at a time and see the total cost.
 *
 * DESIGN + ONE-LINE JUSTIFICATIONS
 * - LineItem is a STATIC NESTED class: it is self-contained data (product, quantity, unit price)
 *   that never reads the Order's fields, so it needs no outer Order object to exist.
 * - Cursor is a MEMBER INNER class: every cursor belongs to exactly one Order and must read that
 *   order's private items array, which only a (non-static) inner class can do.
 *
 * COMPLEXITY
 * - addItem: O(1) amortized (array doubling on growth); O(n) total space for n line items.
 * - Walking the order with the cursor: O(n) time, O(1) extra space.
 *
 * INPUT  : one line item per line: product quantity unitPrice   (e.g. "Pen 3 10")
 * OUTPUT : each line item's total, the order total, and the two justifications.
 *
 * Run with: java -cp target/classes practice_problems.Problem1_OrderWalkthrough
 * (type input, end with Ctrl+Z [Windows] / Ctrl+D [Linux]; running with empty input uses the
 *  example from the problem statement).
 */
public class Problem1_OrderWalkthrough {

    /**
     * Static nested class - "needs no order": it can be created on its own
     * (new LineItem(...)) without any Order instance, and it cannot touch Order's fields.
     */
    static class LineItem {
        String productName;
        int quantity;
        double unitPrice;

        LineItem(String productName, int quantity, double unitPrice) {
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        double total() {
            return quantity * unitPrice;
        }
    }

    private LineItem[] items = new LineItem[8];   // internal array of line items
    private int count = 0;

    /** Adding an item is O(1) amortized; the array doubles when full. */
    public void addItem(String product, int quantity, double unitPrice) {
        if (count == items.length) {
            LineItem[] bigger = new LineItem[items.length * 2];
            System.arraycopy(items, 0, bigger, 0, items.length);
            items = bigger;
        }
        items[count++] = new LineItem(product, quantity, unitPrice);
    }

    /** Order total: O(n). */
    public double orderTotal() {
        double total = 0;
        for (int i = 0; i < count; i++) {
            total += items[i].total();
        }
        return total;
    }

    /**
     * Member inner class - "reads the order's array": each Cursor is bound to one Order
     * instance (it holds a hidden reference to Problem1_OrderWalkthrough.this) and reads
     * the outer object's PRIVATE items/count fields directly.
     */
    class Cursor {
        private int index = 0;

        boolean hasNext() {
            return index < count;          // reads the outer object's private field
        }

        LineItem next() {
            return items[index++];         // walks the outer object's private array
        }

        void reset() {
            index = 0;
        }
    }

    /** Factory so callers do not need the awkward Order.Cursor syntax. */
    public Cursor cursor() {
        return new Cursor();
    }

    private static final String DEMO = "Pen 3 10\nBook 2 250\nBag 1 700\n";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) {               // nothing piped in -> run the example from the PDF
            sc = new Scanner(DEMO);
        }

        Problem1_OrderWalkthrough order = new Problem1_OrderWalkthrough();
        while (sc.hasNext()) {
            String product = sc.next();
            int quantity = sc.nextInt();
            double unitPrice = sc.nextDouble();
            order.addItem(product, quantity, unitPrice);
        }

        Cursor cursor = order.cursor();    // one cursor tied to this one order
        while (cursor.hasNext()) {
            LineItem li = cursor.next();
            System.out.println(li.productName + " " + fmt(li.total()));
        }
        System.out.println("Total " + fmt(order.orderTotal()));
        System.out.println("cursor: inner (reads the order's array)");
        System.out.println("line item: static nested (needs no order)");
    }

    /** Print whole numbers without a decimal point (30 not 30.0). */
    static String fmt(double v) {
        return v == Math.rint(v) ? String.valueOf((long) v) : String.valueOf(v);
    }
}
