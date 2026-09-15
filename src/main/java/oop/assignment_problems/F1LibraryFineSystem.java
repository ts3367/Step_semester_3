package oop.assignment_problems;

// F1. From Procedural Mess to a Working Library Fine System
public class F1LibraryFineSystem {

    static class BookIssue {
        String title;
        String borrowerName;
        int daysOverdue;

        BookIssue(String title, String borrowerName, int daysOverdue) {
            this.title = title;
            this.borrowerName = borrowerName;
            this.daysOverdue = daysOverdue;
        }

        double fineAmount() {
            return daysOverdue > 0 ? daysOverdue * 5 : 0;
        }

        boolean isSeverelyOverdue() {
            return daysOverdue > 14;
        }

        // Static because it aggregates over MANY books and belongs to the class
        // as a whole (called as BookIssue.totalFineCollected(...)), needing no
        // single book's state. fineAmount is instance-level: it reads one
        // specific book's daysOverdue field.
        static double totalFineCollected(BookIssue[] issues) {
            double total = 0;
            for (BookIssue b : issues) {
                total += b.fineAmount();
            }
            return total;
        }
    }

    public static void main(String[] args) {
        BookIssue[] issues = {
            new BookIssue("Clean Code", "Aarav", 18),
            new BookIssue("Effective Java", "Diya", 5),
            new BookIssue("Refactoring", "Kabir", 0),
            new BookIssue("DSA Handbook", "Ishita", 21),
            new BookIssue("Design Patterns", "Rohan", 9)
        };

        for (BookIssue b : issues) {
            System.out.println(b.title + " - " + b.daysOverdue + " days - "
                    + (b.isSeverelyOverdue() ? "Severely overdue" : "OK"));
        }
        System.out.println("Total fine collected: Rs " + BookIssue.totalFineCollected(issues));
    }
}
