package system_design.class_problems;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Employee Leave Request Management. Leave policy is pluggable through an
// interface, and LeaveRequest owns the status transition rules.
public class P4LeaveRequestManagement {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public interface LeavePolicy {
        String getTypeName();

        int maxLeaveDays();
    }

    public static class FullTimePolicy implements LeavePolicy {
        @Override
        public String getTypeName() {
            return "Full-time";
        }

        @Override
        public int maxLeaveDays() {
            return 25;
        }
    }

    public static class PartTimePolicy implements LeavePolicy {
        @Override
        public String getTypeName() {
            return "Part-time";
        }

        @Override
        public int maxLeaveDays() {
            return 12;
        }
    }

    public static class ContractPolicy implements LeavePolicy {
        @Override
        public String getTypeName() {
            return "Contract";
        }

        @Override
        public int maxLeaveDays() {
            return 5;
        }
    }

    public static class Employee {
        private final String name;
        private final LeavePolicy leavePolicy;

        public Employee(String name, LeavePolicy leavePolicy) {
            this.name = name;
            this.leavePolicy = leavePolicy;
        }

        public String getName() {
            return name;
        }

        public LeavePolicy getLeavePolicy() {
            return leavePolicy;
        }
    }

    public enum LeaveStatus {
        PENDING("Pending"), APPROVED("Approved"), REJECTED("Rejected");

        private final String label;

        LeaveStatus(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public static class LeaveRequest {
        private final Employee employee;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private LeaveStatus status = LeaveStatus.PENDING;

        public LeaveRequest(Employee employee, LocalDate startDate, LocalDate endDate) {
            this.employee = employee;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Employee getEmployee() {
            return employee;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public LeaveStatus getStatus() {
            return status;
        }

        public int requestedDays() {
            return (int) (endDate.toEpochDay() - startDate.toEpochDay()) + 1;
        }

        public void changeStatus(LeaveStatus newStatus) {
            if (status != LeaveStatus.PENDING) {
                throw new IllegalStateException("Cannot change status: " + status.getLabel()
                        + " request cannot revert to " + newStatus.getLabel() + ".");
            }
            if (newStatus == LeaveStatus.PENDING) {
                throw new IllegalStateException("Cannot change status: a reviewed request cannot revert to "
                        + newStatus.getLabel() + ".");
            }
            this.status = newStatus;
        }
    }

    public static class LeaveManager {
        private final List<LeaveRequest> requests = new ArrayList<>();

        public List<LeaveRequest> getRequests() {
            return requests;
        }

        public LeaveRequest submit(Employee employee, LocalDate startDate, LocalDate endDate) {
            if (!startDate.isBefore(endDate)) {
                throw new IllegalArgumentException("Leave start date must be before the end date.");
            }
            if (employee.getLeavePolicy().maxLeaveDays() < endDate.toEpochDay() - startDate.toEpochDay() + 1) {
                throw new IllegalArgumentException("Requested days exceed the "
                        + employee.getLeavePolicy().getTypeName() + " leave limit.");
            }
            LeaveRequest request = new LeaveRequest(employee, startDate, endDate);
            requests.add(request);
            System.out.println("Leave request submitted by " + employee.getName() + " for "
                    + startDate.format(DATE_FORMAT) + " to " + endDate.format(DATE_FORMAT)
                    + ". Status: " + request.getStatus().getLabel() + ".");
            return request;
        }

        public void review(LeaveRequest request, LeaveStatus decision) {
            request.changeStatus(decision);
            System.out.println("Leave request for " + request.getEmployee().getName() + " "
                    + decision.name().toLowerCase() + ". Status: " + request.getStatus().getLabel() + ".");
        }
    }

    public static void main(String[] args) {
        LeaveManager manager = new LeaveManager();
        Employee john = new Employee("John Doe", new FullTimePolicy());
        Employee jane = new Employee("Jane Smith", new PartTimePolicy());

        LeaveRequest johnRequest = manager.submit(john, LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 12));
        manager.review(johnRequest, LeaveStatus.APPROVED);

        manager.submit(jane, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 5));

        try {
            johnRequest.changeStatus(LeaveStatus.PENDING);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
