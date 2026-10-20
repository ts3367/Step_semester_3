package system_design.class_problems;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Online Examination System. Question is an abstract class so new question
// types plug in without touching the attempt/evaluation workflow.
public class P1OnlineExaminationSystem {

    public abstract static class Question {
        private final int number;
        private final String text;

        protected Question(int number, String text) {
            this.number = number;
            this.text = text;
        }

        public int getNumber() {
            return number;
        }

        public String getText() {
            return text;
        }

        public abstract boolean evaluate(String answer);

        public abstract String getType();
    }

    public static class MultipleChoiceQuestion extends Question {
        private final List<String> options;
        private final String correctAnswer;

        public MultipleChoiceQuestion(int number, String text, List<String> options, String correctAnswer) {
            super(number, text);
            this.options = List.copyOf(options);
            this.correctAnswer = correctAnswer;
        }

        public List<String> getOptions() {
            return options;
        }

        @Override
        public boolean evaluate(String answer) {
            return answer != null && correctAnswer.equalsIgnoreCase(answer.trim());
        }

        @Override
        public String getType() {
            return "MULTIPLE_CHOICE";
        }
    }

    public static class TrueFalseQuestion extends Question {
        private final boolean correctAnswer;

        public TrueFalseQuestion(int number, String text, boolean correctAnswer) {
            super(number, text);
            this.correctAnswer = correctAnswer;
        }

        @Override
        public boolean evaluate(String answer) {
            return answer != null && Boolean.parseBoolean(answer.trim()) == correctAnswer;
        }

        @Override
        public String getType() {
            return "TRUE_FALSE";
        }
    }

    public static class Student {
        private final String name;

        public Student(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Attempt {
        private final Examination examination;
        private final Student student;
        private final Map<Integer, String> answers = new LinkedHashMap<>();
        private boolean submitted;
        private int correctCount;

        public Attempt(Examination examination, Student student) {
            this.examination = examination;
            this.student = student;
        }

        public Student getStudent() {
            return student;
        }

        public boolean isSubmitted() {
            return submitted;
        }

        public int getCorrectCount() {
            return correctCount;
        }

        public void answer(Question question, String answer) {
            if (submitted) {
                throw new IllegalStateException("Attempt already submitted: answers cannot be changed.");
            }
            answers.put(question.getNumber(), answer);
            System.out.println("Question " + question.getNumber() + " answered with '" + answer + "'.");
        }

        public String submit() {
            if (submitted) {
                throw new IllegalStateException("Attempt already submitted: cannot submit twice.");
            }
            submitted = true;
            correctCount = 0;
            for (Question question : examination.getQuestions()) {
                String given = answers.get(question.getNumber());
                if (given != null && question.evaluate(given)) {
                    correctCount++;
                }
            }
            System.out.println("Examination '" + examination.getTitle() + "' submitted successfully.");
            return "Result for '" + examination.getTitle() + "' attempt: " + correctCount + "/"
                    + examination.getQuestions().size() + " correct";
        }
    }

    public static class Examination {
        private final String title;
        private final List<Question> questions = new ArrayList<>();
        private final List<Attempt> attempts = new ArrayList<>();

        public Examination(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public List<Question> getQuestions() {
            return questions;
        }

        public List<Attempt> getAttempts() {
            return attempts;
        }

        public void addQuestion(Question question) {
            questions.add(question);
        }

        public Attempt startAttempt(Student student) {
            for (Attempt attempt : attempts) {
                if (attempt.getStudent().getName().equals(student.getName()) && attempt.isSubmitted()) {
                    throw new IllegalStateException(
                            "Student already has a submitted attempt for '" + title + "'.");
                }
            }
            Attempt attempt = new Attempt(this, student);
            attempts.add(attempt);
            System.out.println("Examination '" + title + "' started by " + student.getName() + ".");
            return attempt;
        }
    }

    public static void main(String[] args) {
        Examination exam = new Examination("Math Quiz");
        exam.addQuestion(new MultipleChoiceQuestion(1, "2 + 2 = ?",
                List.of("A. 3", "B. 4", "C. 5", "D. 6"), "A"));
        exam.addQuestion(new MultipleChoiceQuestion(2, "The capital of France is ?",
                List.of("A. Rome", "B. Paris", "C. Madrid", "D. Berlin"), "B"));

        Student student = new Student("Student");
        Attempt attempt = exam.startAttempt(student);
        attempt.answer(exam.getQuestions().get(0), "A");
        attempt.answer(exam.getQuestions().get(1), "C");
        System.out.println(attempt.submit());

        try {
            attempt.answer(exam.getQuestions().get(0), "B");
        } catch (IllegalStateException e) {
            System.out.println("Answer rejected: " + e.getMessage());
        }
    }
}
