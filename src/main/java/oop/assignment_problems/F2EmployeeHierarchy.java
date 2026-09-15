package oop.assignment_problems;

// F2. Extending Employee Without Touching It
// ManagerEmployee and InternEmployee extend a tested Employee class as-is.
public class F2EmployeeHierarchy {

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

    public static void main(String[] args) {
        Employee plain = new Employee("E001", "Asha", 40000);
        ManagerEmployee manager = new ManagerEmployee("E002", "Vikram", 70000, 8000);
        InternEmployee intern = new InternEmployee("E003", "Neha", 12000, 10000);

        Employee[] staff = { plain, manager, intern };
        for (Employee e : staff) {
            if (e instanceof ManagerEmployee m) {
                System.out.println("Manager effective pay: Rs " + m.effectiveSalary());
            } else if (e instanceof InternEmployee i) {
                System.out.println("Intern effective pay: Rs " + i.effectiveSalary());
            } else {
                System.out.println("Plain employee pay: Rs " + e.getSalary());
            }
        }
    }
}
