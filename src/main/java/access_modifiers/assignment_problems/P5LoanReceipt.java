package access_modifiers.assignment_problems;

// P5. Immutable Loan Receipt & Nightly Circulation Ledger (capstone)
// Final class, all fields final, defensive copy in/out, wither pattern,
// static block, instanceof dispatch, null-safe batch processing.
public final class P5LoanReceipt {

    public static class LoanReceipt {
        private final String memberId;
        private final String[] bookIds;

        private static final java.util.regex.Pattern BOOK_PATTERN =
                java.util.regex.Pattern.compile("^BK-\\d{3}$");

        static {
            System.out.println("[LoanReceipt] static block initialised");
        }

        public LoanReceipt(String memberId, String[] bookIds) {
            if (memberId == null || memberId.trim().isEmpty())
                throw new IllegalArgumentException("memberId required");
            if (bookIds == null)
                throw new IllegalArgumentException("bookIds required");
            for (String id : bookIds) {
                if (id == null || !BOOK_PATTERN.matcher(id).matches())
                    throw new IllegalArgumentException("Bad book ID: " + id);
            }
            this.memberId = memberId.trim();
            // defensive copy IN
            this.bookIds = bookIds.clone();
        }

        // Defensive copy OUT
        public String[] getBookIds() {
            return bookIds.clone();
        }

        public String getMemberId() { return memberId; }

        // Wither: returns a brand-new object with one corrected entry.
        public LoanReceipt withCorrectedBookId(int index, String newId) {
            if (newId == null || !BOOK_PATTERN.matcher(newId).matches())
                throw new IllegalArgumentException("Bad book ID: " + newId);
            String[] copy = bookIds.clone();
            copy[index] = newId;
            return new LoanReceipt(memberId, copy);
        }
    }

    public static final class ReferenceOnlyLoanReceipt extends LoanReceipt {
        private final String roomNumber;

        public ReferenceOnlyLoanReceipt(String memberId, String[] bookIds, String roomNumber) {
            super(memberId, bookIds);
            this.roomNumber = roomNumber;
        }

        public String getRoomNumber() { return roomNumber; }
    }

    public static String processNightlyCirculation(LoanReceipt[] receipts) {
        int processed = 0, nullSkipped = 0, referenceOnly = 0, regular = 0;
        for (LoanReceipt r : receipts) {
            if (r == null) { nullSkipped++; continue; }
            if (r instanceof ReferenceOnlyLoanReceipt) referenceOnly++;
            else regular++;
            processed++;
        }
        return processed + " processed | " + nullSkipped + " null skipped | "
                + referenceOnly + " reference-only | " + regular + " regular";
    }

    public static void main(String[] args) {
        // Example 1 — bad book ID rejected
        try {
            new LoanReceipt("LIB-8841", new String[]{"BK-100", "bad"});
            System.out.println("constructed");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        // Example 2 — defensive copy + wither
        LoanReceipt r = new LoanReceipt("LIB-8841", new String[]{"BK-100", "BK-101"});
        String[] ids = r.getBookIds();
        ids[0] = "HACKED";
        System.out.println(r.getBookIds()[0]); // BK-100 (untouched)
        // Example 3 — batch
        System.out.println(processNightlyCirculation(new LoanReceipt[]{
            new ReferenceOnlyLoanReceipt("LIB-001", new String[]{"BK-200"}, "Reading Room 3"),
            null,
            new LoanReceipt("LIB-002", new String[]{"BK-201"})
        })); // 2 processed | 1 null skipped | 1 reference-only | 1 regular
    }
}
