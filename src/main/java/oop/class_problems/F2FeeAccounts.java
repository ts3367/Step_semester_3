package oop.class_problems;

// F2. Extending FeeAccount Without Touching It
// New account types are added via inheritance; FeeAccount is never edited.
public class F2FeeAccounts {

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

    static class ScholarshipFeeAccount extends FeeAccount {
        private double scholarshipPercent;

        ScholarshipFeeAccount(String regNo, double totalFee, double amountPaid, double scholarshipPercent) {
            super(regNo, totalFee, amountPaid);
            this.scholarshipPercent = scholarshipPercent;
        }

        double effectiveDue() {
            return getDue() * (1 - scholarshipPercent / 100);
        }
    }

    public static void main(String[] args) {
        FeeAccount plain = new FeeAccount("RA001", 150000, 150000);
        HostelFeeAccount hostel = new HostelFeeAccount("RA002", 200000, 0);
        hostel.payInTwoInstallments(60000);
        ScholarshipFeeAccount scholarship =
                new ScholarshipFeeAccount("RA003", 180000, 0, 20);

        FeeAccount[] accounts = { plain, hostel, scholarship };
        for (FeeAccount acc : accounts) {
            if (acc instanceof ScholarshipFeeAccount s) {
                System.out.println("Scholarship account effective due: Rs " + s.effectiveDue());
            } else if (acc instanceof HostelFeeAccount) {
                System.out.println("Hostel account due: Rs " + acc.getDue());
            } else {
                System.out.println("Plain account due: Rs " + acc.getDue());
            }
        }
    }
}
