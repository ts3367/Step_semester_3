package oop.class_problems;

// F1. From Procedural Mess to a Working Attendance System
// Replaces five sets of parallel variables with one SrmStudent class.
public class F1AttendanceSystem {

    static class SrmStudent {
        String name;
        String regNo;
        int attendance;

        SrmStudent(String name, String regNo, int attendance) {
            this.name = name;
            this.regNo = regNo;
            this.attendance = attendance;
        }

        void addAttendanceUpdate(int newAttendance) {
            this.attendance = newAttendance;
        }

        boolean isEligible() {
            return attendance >= 75;
        }

        // Static because the average belongs to the whole class of students,
        // not to any single student: it operates over an array of objects and
        // needs no instance state. isEligible is instance-level because it
        // depends on one specific student's attendance field.
        static double classAverage(SrmStudent[] students) {
            int sum = 0;
            for (SrmStudent s : students) {
                sum += s.attendance;
            }
            return (double) sum / students.length;
        }
    }

    public static void main(String[] args) {
        SrmStudent[] students = {
            new SrmStudent("Ravi", "RA231100301001", 82),
            new SrmStudent("Anitha", "RA231100301002", 68),
            new SrmStudent("Karthik", "RA231100301003", 91),
            new SrmStudent("Meera", "RA231100301004", 74),
            new SrmStudent("Suresh", "RA231100301005", 60)
        };

        for (SrmStudent s : students) {
            System.out.println(s.name + " - " + s.attendance + "% - "
                    + (s.isEligible() ? "Eligible" : "Detained"));
        }
        System.out.println("Class average: " + SrmStudent.classAverage(students) + "%");
    }
}
