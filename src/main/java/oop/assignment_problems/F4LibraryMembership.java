package oop.assignment_problems;

// F4. Designing the Instance/Static Boundary for a Library Membership System
// Reproduces the all-static bug, then fixes it with a correct instance/static split.
public class F4LibraryMembership {

    // ---- BROKEN version: every field static => shared across ALL members ----
    static class BrokenLibraryMember {
        static String name;
        static String memberId;
        static int booksIssued;

        BrokenLibraryMember(String name, String memberId, int booksIssued) {
            // Why static is wrong for each field:
            // name: each member has their own name; static keeps only ONE name
            //       slot, so the second member overwrites the first.
            // memberId: IDs must be unique per member; static forces every
            //           member object to report the last-assigned ID.
            // booksIssued: issue counts are per-member data; static merges
            //              everyone into a single shared counter.
            BrokenLibraryMember.name = name;
            BrokenLibraryMember.memberId = memberId;
            BrokenLibraryMember.booksIssued = booksIssued;
        }
    }

    // ---- FIXED version ----
    static class LibraryMember {
        String name;        // instance: unique per member
        String memberId;    // instance: unique per member
        int booksIssued;    // instance: unique per member

        static String libraryName = "Central Library";
        static int memberCount = 0;  // shared: counts all members

        LibraryMember(String name, int booksIssued) {
            memberCount++;
            this.name = name;
            this.memberId = "LM-" + (1000 + memberCount);
            this.booksIssued = booksIssued;
        }

        void printMemberCard() {
            System.out.println(name + " | " + memberId);
        }

        static void printTotalMembers() {
            System.out.println("Total members: " + memberCount);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Broken version ---");
        new BrokenLibraryMember("Aditi", "LM-1001", 2);
        new BrokenLibraryMember("Rohan", "LM-1002", 1);
        // Both print "Rohan": static fields hold only the last-written values.
        System.out.println(BrokenLibraryMember.name);
        System.out.println(BrokenLibraryMember.name);

        System.out.println("--- Fixed version ---");
        LibraryMember aditi = new LibraryMember("Aditi", 2);
        LibraryMember rohan = new LibraryMember("Rohan", 1);
        aditi.printMemberCard();
        rohan.printMemberCard();
        LibraryMember.printTotalMembers();
    }
}
