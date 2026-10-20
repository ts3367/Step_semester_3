package system_design.assignment_problems;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

// Elective Seat Rush. Credit limits are a pluggable policy and the waitlist is
// a private queue inside the elective.
public class P4ElectiveSeatRush {

    public interface CreditPolicy {
        String getTypeName();

        int getCreditLimit();
    }

    public static class RegularPolicy implements CreditPolicy {
        @Override
        public String getTypeName() {
            return "Regular";
        }

        @Override
        public int getCreditLimit() {
            return 24;
        }
    }

    public static class HonorsPolicy implements CreditPolicy {
        @Override
        public String getTypeName() {
            return "Honors";
        }

        @Override
        public int getCreditLimit() {
            return 28;
        }
    }

    public static class ExchangePolicy implements CreditPolicy {
        @Override
        public String getTypeName() {
            return "Exchange";
        }

        @Override
        public int getCreditLimit() {
            return 20;
        }
    }

    public static class Student {
        private final String name;
        private final CreditPolicy creditPolicy;
        private int currentCredits;

        public Student(String name, CreditPolicy creditPolicy, int currentCredits) {
            this.name = name;
            this.creditPolicy = creditPolicy;
            this.currentCredits = currentCredits;
        }

        public String getName() {
            return name;
        }

        public CreditPolicy getCreditPolicy() {
            return creditPolicy;
        }

        public int getCurrentCredits() {
            return currentCredits;
        }

        public void addCredits(int credits) {
            this.currentCredits += credits;
        }

        public void removeCredits(int credits) {
            this.currentCredits -= credits;
        }
    }

    public static class Elective {
        private final String name;
        private final int creditValue;
        private final int capacity;
        private final List<Student> enrolled = new ArrayList<>();
        private final Deque<Student> waitlist = new ArrayDeque<>();

        public Elective(String name, int creditValue, int capacity) {
            this.name = name;
            this.creditValue = creditValue;
            this.capacity = capacity;
        }

        public String getName() {
            return name;
        }

        public int getCreditValue() {
            return creditValue;
        }

        public int getCapacity() {
            return capacity;
        }

        public List<Student> getEnrolled() {
            return enrolled;
        }

        public Deque<Student> getWaitlist() {
            return waitlist;
        }

        public boolean isFull() {
            return enrolled.size() >= capacity;
        }

        public boolean isEnrolled(Student student) {
            return enrolled.stream().anyMatch(s -> s.getName().equals(student.getName()));
        }

        public boolean isWaitlisted(Student student) {
            return waitlist.stream().anyMatch(s -> s.getName().equals(student.getName()));
        }
    }

    public static class EnrollmentService {
        private final List<Elective> electives = new ArrayList<>();

        public List<Elective> getElectives() {
            return electives;
        }

        public void createElective(Elective elective) {
            electives.add(elective);
        }

        public boolean canTakeCredits(Student student, Elective elective) {
            return student.getCurrentCredits() + elective.getCreditValue()
                    <= student.getCreditPolicy().getCreditLimit();
        }

        public void enroll(Student student, Elective elective) {
            if (elective.isEnrolled(student) || elective.isWaitlisted(student)) {
                System.out.println("Enrollment failed: " + student.getName() + " is already enrolled or "
                        + "waitlisted in " + elective.getName() + ".");
                return;
            }
            if (!canTakeCredits(student, elective)) {
                int attempted = student.getCurrentCredits() + elective.getCreditValue();
                System.out.println("Enrollment failed: " + student.getName() + " would exceed the "
                        + student.getCreditPolicy().getTypeName() + " credit limit (" + attempted + "/"
                        + student.getCreditPolicy().getCreditLimit() + ").");
                return;
            }
            if (elective.isFull()) {
                elective.getWaitlist().addLast(student);
                System.out.println(elective.getName() + " is full. " + student.getName()
                        + " added to waitlist (position " + elective.getWaitlist().size() + ").");
                return;
            }
            elective.getEnrolled().add(student);
            student.addCredits(elective.getCreditValue());
            System.out.println(student.getName() + " enrolled in " + elective.getName() + " (credits: "
                    + student.getCurrentCredits() + "/" + student.getCreditPolicy().getCreditLimit() + ").");
        }

        public void drop(Student student, Elective elective) {
            if (!elective.isEnrolled(student)) {
                System.out.println("Drop failed: " + student.getName() + " is not enrolled in "
                        + elective.getName() + ".");
                return;
            }
            elective.getEnrolled().removeIf(s -> s.getName().equals(student.getName()));
            student.removeCredits(elective.getCreditValue());
            System.out.println(student.getName() + " dropped " + elective.getName() + " (credits: "
                    + student.getCurrentCredits() + "/" + student.getCreditPolicy().getCreditLimit() + ").");
            promoteFromWaitlist(elective);
        }

        private void promoteFromWaitlist(Elective elective) {
            if (elective.isFull()) {
                return;
            }
            List<Student> notEligible = new ArrayList<>();
            Student promoted = null;
            while (!elective.getWaitlist().isEmpty()) {
                Student candidate = elective.getWaitlist().pollFirst();
                if (canTakeCredits(candidate, elective)) {
                    promoted = candidate;
                    break;
                }
                notEligible.add(candidate);
            }
            for (Student candidate : notEligible) {
                elective.getWaitlist().addLast(candidate);
            }
            if (promoted == null) {
                return;
            }
            elective.getEnrolled().add(promoted);
            promoted.addCredits(elective.getCreditValue());
            System.out.println(promoted.getName() + " promoted from waitlist and enrolled in "
                    + elective.getName() + " (credits: " + promoted.getCurrentCredits() + "/"
                    + promoted.getCreditPolicy().getCreditLimit() + ").");
        }
    }

    public static void main(String[] args) {
        EnrollmentService service = new EnrollmentService();
        Elective cloudComputing = new Elective("Cloud Computing", 4, 2);
        service.createElective(cloudComputing);

        Student asha = new Student("Asha", new RegularPolicy(), 20);
        Student ravi = new Student("Ravi", new HonorsPolicy(), 22);
        Student neha = new Student("Neha", new ExchangePolicy(), 12);
        Student kiran = new Student("Kiran", new RegularPolicy(), 22);

        service.enroll(asha, cloudComputing);
        service.enroll(ravi, cloudComputing);
        service.enroll(neha, cloudComputing);
        service.enroll(kiran, cloudComputing);

        service.drop(asha, cloudComputing);
    }
}
