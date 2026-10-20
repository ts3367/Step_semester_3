package system_design.assignment_problems;

import java.util.ArrayList;
import java.util.List;

// Code Sprint Judging Desk. Track owns the scoring rule and Hackathon owns the
// Open -> Judging -> Published state that locks results.
public class P1HackathonJudgingDesk {

    public interface Track {
        String getTrackName();

        double calculateFinalScore(int idea, int execution, int presentation);
    }

    public static class InnovationTrack implements Track {
        @Override
        public String getTrackName() {
            return "Innovation";
        }

        @Override
        public double calculateFinalScore(int idea, int execution, int presentation) {
            return idea * 0.5 + execution * 0.3 + presentation * 0.2;
        }
    }

    public static class OpenTrack implements Track {
        @Override
        public String getTrackName() {
            return "Open";
        }

        @Override
        public double calculateFinalScore(int idea, int execution, int presentation) {
            return (idea + execution + presentation) / 3.0;
        }
    }

    public enum HackathonState {
        OPEN, JUDGING, PUBLISHED
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

    public static class Score {
        private final int idea;
        private final int execution;
        private final int presentation;
        private final double finalScore;

        public Score(int idea, int execution, int presentation, double finalScore) {
            this.idea = idea;
            this.execution = execution;
            this.presentation = presentation;
            this.finalScore = finalScore;
        }

        public int getIdea() {
            return idea;
        }

        public int getExecution() {
            return execution;
        }

        public int getPresentation() {
            return presentation;
        }

        public double getFinalScore() {
            return finalScore;
        }
    }

    public static class Project {
        private final String name;
        private final Team team;
        private Score score;

        public Project(String name, Team team) {
            this.name = name;
            this.team = team;
        }

        public String getName() {
            return name;
        }

        public Team getTeam() {
            return team;
        }

        public Score getScore() {
            return score;
        }

        void recordScore(Score score) {
            this.score = score;
        }
    }

    public static class Team {
        private final String name;
        private final Track track;
        private final List<Student> members = new ArrayList<>();
        private Project project;

        public Team(String name, Track track) {
            this.name = name;
            this.track = track;
        }

        public String getName() {
            return name;
        }

        public Track getTrack() {
            return track;
        }

        public List<Student> getMembers() {
            return members;
        }

        public Project getProject() {
            return project;
        }

        public void addMember(Student student) {
            members.add(student);
        }

        void submitProject(Project submittedProject) {
            this.project = submittedProject;
        }
    }

    public static class Judge {
        private final String name;

        public Judge(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void score(Hackathon hackathon, Project project, int idea, int execution, int presentation) {
            hackathon.ensureJudging();
            if (project.getScore() != null) {
                throw new IllegalStateException("A score is already recorded for '" + project.getName() + "'.");
            }
            double finalScore = project.getTeam().getTrack()
                    .calculateFinalScore(idea, execution, presentation);
            project.recordScore(new Score(idea, execution, presentation, finalScore));
            System.out.println("Score recorded for '" + project.getName() + "'. Final score: "
                    + String.format("%.2f", finalScore) + ".");
        }
    }

    public static class Hackathon {
        private final String name;
        private final List<Team> teams = new ArrayList<>();
        private HackathonState state = HackathonState.OPEN;

        public Hackathon(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<Team> getTeams() {
            return teams;
        }

        public HackathonState getState() {
            return state;
        }

        public Team registerTeam(String teamName, Track track, List<Student> members) {
            if (state != HackathonState.OPEN) {
                throw new IllegalStateException("Registration failed: registration is closed for '" + name + "'.");
            }
            if (members.size() < 2 || members.size() > 4) {
                throw new IllegalArgumentException("Registration failed: A team must have 2 to 4 members.");
            }
            for (Student member : members) {
                for (Team existing : teams) {
                    for (Student enrolled : existing.getMembers()) {
                        if (enrolled.getName().equals(member.getName())) {
                            throw new IllegalStateException("Registration failed: " + member.getName()
                                    + " already belongs to team " + existing.getName() + " in this hackathon.");
                        }
                    }
                }
            }
            Team team = new Team(teamName, track);
            for (Student member : members) {
                team.addMember(member);
            }
            teams.add(team);
            System.out.println("Team " + teamName + " registered (" + members.size() + " members, "
                    + track.getTrackName() + " track).");
            return team;
        }

        public Project submitProject(Team team, String projectName) {
            if (team.getProject() != null) {
                throw new IllegalStateException("Submission failed: " + team.getName()
                        + " has already submitted a project.");
            }
            Project project = new Project(projectName, team);
            team.submitProject(project);
            state = HackathonState.JUDGING;
            System.out.println("Project '" + projectName + "' submitted by " + team.getName() + ".");
            return project;
        }

        public void ensureJudging() {
            if (state == HackathonState.PUBLISHED) {
                throw new IllegalStateException("Results have already been published.");
            }
        }

        public void publishResults() {
            state = HackathonState.PUBLISHED;
            System.out.println("Results published.");
        }
    }

    public static void main(String[] args) {
        Hackathon hackathon = new Hackathon("Code Sprint 2026");
        Judge judge = new Judge("Judge Rao");

        Team byteBusters = hackathon.registerTeam("ByteBusters", new InnovationTrack(),
                List.of(new Student("Asha"), new Student("Ravi"), new Student("Neha")));

        try {
            hackathon.registerTeam("SoloCoder", new OpenTrack(), List.of(new Student("Kiran")));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            hackathon.registerTeam("DuplicateTeam", new OpenTrack(),
                    List.of(new Student("Asha"), new Student("Kiran")));
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        Project project = hackathon.submitProject(byteBusters, "SmartAttend");
        judge.score(hackathon, project, 8, 7, 9);

        hackathon.publishResults();

        try {
            judge.score(hackathon, project, 10, 7, 9);
        } catch (IllegalStateException e) {
            System.out.println("Rescore rejected: " + e.getMessage());
        }
    }
}
