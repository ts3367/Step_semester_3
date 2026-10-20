package system_design.class_problems;

// Week 8 practice quiz answers. Each entry gives the correct option and the
// reason, so the list doubles as a revision sheet.
public class P6QuizAnswers {

    public record Answer(int question, String correctOption, String reason) {
    }

    private static final Answer[] ANSWERS = {
            new Answer(1, "C",
                    "Abstraction lets each transaction type define its own validation and processing, while runtime "
                            + "polymorphism lets the shared engine call the right implementation without modification."),
            new Answer(2, "B and D",
                    "The call is resolved at runtime on the actual object type, which is runtime polymorphism and "
                            + "dynamic dispatch; both terms describe the same mechanism."),
            new Answer(3, "D",
                    "A state diagram models the states of a book (Available, Borrowed) and the guarded transitions "
                            + "between them, which is exactly what this scenario describes."),
            new Answer(4, "C",
                    "Orders cannot exist without a Customer and are removed with it, so the lifecycle dependency is "
                            + "composition, not aggregation."),
            new Answer(5, "D",
                    "LineItems are created and owned by the FoodOrder and die with it, so they are composed parts."),
            new Answer(6, "C",
                    "Depending on the IPaymentMethod abstraction rather than a concrete method is abstraction, and "
                            + "it is what allows a new payment method to be added without touching the processor."),
            new Answer(7, "C",
                    "A sequence diagram shows the ordered messages between the Student, Examination and Attempt "
                            + "lifelines, which is the step-by-step interaction being asked about."),
            new Answer(8, "A, B and D",
                    "The salary is hidden behind a setter, so the class encapsulates it and hides data, and the "
                            + "validation keeps the object in a valid state at all times."),
            new Answer(9, "D",
                    "FileLogger realises the Logger interface, which is the UML realization relationship."),
            new Answer(10, "C",
                    "A course holds at most 50 students (0..50) and a student may take many courses (0..*).")
    };

    public static Answer[] getAnswers() {
        return ANSWERS.clone();
    }

    public static void main(String[] args) {
        for (Answer answer : ANSWERS) {
            System.out.println("Q" + answer.question() + ": " + answer.correctOption()
                    + " - " + answer.reason());
        }
    }
}
