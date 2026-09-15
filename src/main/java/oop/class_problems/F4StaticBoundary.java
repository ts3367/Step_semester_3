package oop.class_problems;

// F4. Designing the Instance/Static Boundary for a College-Wide System
// Reproduces the all-static bug, then fixes it with a correct instance/static split.
public class F4StaticBoundary {

    // ---- BROKEN version: every field static => shared across ALL students ----
    static class BrokenSrmStudent {
        static String name;
        static String regNo;
        static int attendance;

        BrokenSrmStudent(String name, String regNo, int attendance) {
            // Why static is wrong for each field:
            // name: each student has their own name; static keeps only ONE name
            //       slot, so the second student overwrites the first.
            // regNo: register numbers must be unique per student; static forces
            //        every student object to report the last-assigned regNo.
            // attendance: attendance is per-student data; static merges everyone
            //             into a single shared counter.
            BrokenSrmStudent.name = name;
            BrokenSrmStudent.regNo = regNo;
            BrokenSrmStudent.attendance = attendance;
        }
    }

    // ---- FIXED version ----
    static class SrmStudent {
        String name;       // instance: unique per student
        String regNo;      // instance: unique per student
        int attendance;    // instance: unique per student

        static String university = "SRM";
        static int admissionCount = 0;  // shared: counts all admissions

        SrmStudent(String name, int attendance) {
            admissionCount++;
            this.name = name;
            this.regNo = "RA2311003010" + (10 + admissionCount);
            this.attendance = attendance;
        }

        void printIdCard() {
            System.out.println(name + " | " + regNo);
        }

        static void printTotalAdmissions() {
            System.out.println("Students admitted so far: " + admissionCount);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Broken version ---");
        new BrokenSrmStudent("Ravi", "RA231100301011", 82);
        new BrokenSrmStudent("Meera", "RA231100301012", 74);
        // Both print "Meera": static fields hold only the last-written values.
        System.out.println(BrokenSrmStudent.name);
        System.out.println(BrokenSrmStudent.name);

        System.out.println("--- Fixed version ---");
        SrmStudent ravi = new SrmStudent("Ravi", 82);
        SrmStudent meera = new SrmStudent("Meera", 74);
        ravi.printIdCard();
        meera.printIdCard();
        SrmStudent.printTotalAdmissions();
    }
}
