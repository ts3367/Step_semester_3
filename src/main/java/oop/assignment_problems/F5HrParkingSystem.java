package oop.assignment_problems;

// F5. Capstone: A Small HR + Parking Allocation Mini-System
// Composition (record HAS-A employee and HAS-A slot) + inheritance + null safety.
public class F5HrParkingSystem {

    static class Employee {
        private String empId;
        private String empName;
        private double salary;

        Employee(String empId, String empName, double salary) {
            this.empId = empId;
            this.empName = empName;
            this.salary = salary;
        }

        double getSalary() {
            return salary;
        }
    }

    static class ManagerEmployee extends Employee {
        private double teamBonus;

        ManagerEmployee(String empId, String empName, double salary, double teamBonus) {
            super(empId, empName, salary);
            this.teamBonus = teamBonus;
        }

        double effectiveSalary() {
            return getSalary() + teamBonus;
        }
    }

    static class InternEmployee extends Employee {
        private double stipendCap;

        InternEmployee(String empId, String empName, double salary, double stipendCap) {
            super(empId, empName, salary);
            this.stipendCap = stipendCap;
        }

        double effectiveSalary() {
            return Math.min(getSalary(), stipendCap);
        }
    }

    static class ParkingSlot {
        String slotNo;
        int capacity;
        int occupiedCount;

        ParkingSlot(String slotNo, int capacity, int occupiedCount) {
            this.slotNo = slotNo;
            this.capacity = capacity;
            this.occupiedCount = occupiedCount;
        }

        boolean allot(String vehicleNo) {
            if (occupiedCount < capacity) {
                occupiedCount++;
                return true;
            }
            return false;
        }

        static ParkingSlot findAvailableSlot(ParkingSlot[] slots) {
            for (ParkingSlot s : slots) {
                if (s.occupiedCount < s.capacity) {
                    return s;
                }
            }
            return null;
        }
    }

    static class CompanyEmployeeRecord {
        String name;
        String empId;
        Employee employee;      // object field: composition (may be a subclass)
        ParkingSlot slot;       // object field: may be null (unallotted)

        static int totalRecords = 0;

        CompanyEmployeeRecord(String name, String empId, Employee employee, ParkingSlot slot) {
            this.name = name;
            this.empId = empId;
            this.employee = employee;
            this.slot = slot;
            totalRecords++;
        }

        double effectivePay() {
            if (employee instanceof ManagerEmployee m) {
                return m.effectiveSalary();
            } else if (employee instanceof InternEmployee i) {
                return i.effectiveSalary();
            }
            return employee.getSalary();
        }

        String fullProfile() {
            String slotInfo = (slot == null) ? "no parking assigned" : slot.slotNo;
            return name + " | Pay: Rs " + effectivePay() + " | Slot: " + slotInfo;
        }
    }

    public static void main(String[] args) {
        ParkingSlot a1 = new ParkingSlot("A1", 4, 3);
        ParkingSlot a2 = new ParkingSlot("A2", 5, 4);
        ParkingSlot[] slots = { a1, a2 };

        ManagerEmployee divya = new ManagerEmployee("E001", "Divya", 70000, 8000);
        Employee karan = new Employee("E002", "Karan", 40000);
        InternEmployee meera = new InternEmployee("E003", "Meera", 12000, 10000);

        ParkingSlot s1 = ParkingSlot.findAvailableSlot(slots);
        if (s1 != null) {
            s1.allot("DIVYA-CAR");
        }
        ParkingSlot s2 = ParkingSlot.findAvailableSlot(slots);
        if (s2 != null) {
            s2.allot("KARAN-CAR");
        }

        CompanyEmployeeRecord r1 = new CompanyEmployeeRecord("Divya", "E001", divya, s1);
        CompanyEmployeeRecord r2 = new CompanyEmployeeRecord("Karan", "E002", karan, s2);
        CompanyEmployeeRecord r3 = new CompanyEmployeeRecord("Meera", "E003", meera, null);

        System.out.println(r1.fullProfile());
        System.out.println(r2.fullProfile());
        System.out.println(r3.fullProfile());
        System.out.println("Total records: " + CompanyEmployeeRecord.totalRecords);
    }
}
