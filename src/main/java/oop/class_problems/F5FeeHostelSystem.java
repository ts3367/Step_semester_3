package oop.class_problems;

// F5. Capstone: A Small Fee + Hostel Management Mini-System
// Composition (student HAS-A fee account and HAS-A room) + inheritance + null safety.
public class F5FeeHostelSystem {

    static class FeeAccount {
        private String regNo;
        private double totalFee;
        private double amountPaid;

        FeeAccount(String regNo, double totalFee, double amountPaid) {
            this.regNo = regNo;
            this.totalFee = totalFee;
            this.amountPaid = amountPaid;
        }

        void pay(double amount) {
            if (amount <= 0) {
                System.out.println("Payment rejected for " + regNo + ": amount must be positive.");
                return;
            }
            amountPaid += amount;
        }

        double getDue() {
            return totalFee - amountPaid;
        }
    }

    static class HostelFeeAccount extends FeeAccount {
        HostelFeeAccount(String regNo, double totalFee, double amountPaid) {
            super(regNo, totalFee, amountPaid);
        }

        void payInTwoInstallments(double amount) {
            pay(amount / 2);
            pay(amount / 2);
        }
    }

    static class HostelRoom {
        String roomNo;
        int beds;
        int occupied;

        HostelRoom(String roomNo, int beds, int occupied) {
            this.roomNo = roomNo;
            this.beds = beds;
            this.occupied = occupied;
        }

        boolean allot(String name) {
            if (occupied < beds) {
                occupied++;
                return true;
            }
            return false;
        }

        static HostelRoom findAvailableRoom(HostelRoom[] rooms) {
            for (HostelRoom r : rooms) {
                if (r.occupied < r.beds) {
                    return r;
                }
            }
            return null;
        }
    }

    static class SrmStudentRecord {
        String name;
        String regNo;
        HostelFeeAccount feeAccount;  // object field: composition
        HostelRoom room;              // object field: may be null (unallotted)

        static int totalStudents = 0;

        SrmStudentRecord(String name, String regNo, HostelFeeAccount feeAccount, HostelRoom room) {
            this.name = name;
            this.regNo = regNo;
            this.feeAccount = feeAccount;
            this.room = room;
            totalStudents++;
        }

        String fullStatus() {
            String roomInfo = (room == null) ? "unallotted" : room.roomNo;
            return name + " | Due: Rs " + feeAccount.getDue() + " | Room: " + roomInfo;
        }
    }

    public static void main(String[] args) {
        HostelRoom c214 = new HostelRoom("C-214", 3, 2);
        HostelRoom c507 = new HostelRoom("C-507", 2, 1);

        HostelFeeAccount raviFee = new HostelFeeAccount("RA001", 200000, 0);
        raviFee.payInTwoInstallments(60000);   // valid payment
        raviFee.pay(-5000);                    // rejected payment (negative)

        HostelFeeAccount anithaFee = new HostelFeeAccount("RA002", 180000, 0);
        HostelFeeAccount karthikFee = new HostelFeeAccount("RA003", 200000, 0);

        c214.allot("Ravi");
        c507.allot("Anitha");

        SrmStudentRecord ravi = new SrmStudentRecord("Ravi", "RA001", raviFee, c214);
        SrmStudentRecord anitha = new SrmStudentRecord("Anitha", "RA002", anithaFee, c507);
        SrmStudentRecord karthik = new SrmStudentRecord("Karthik", "RA003", karthikFee, null);

        System.out.println(ravi.fullStatus());
        System.out.println(anitha.fullStatus());
        System.out.println(karthik.fullStatus());
        System.out.println("Total students: " + SrmStudentRecord.totalStudents);
    }
}
