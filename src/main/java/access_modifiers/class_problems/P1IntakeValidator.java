package access_modifiers.class_problems;

// P1. Field Visibility & Intake Validator
// AccessRuleEngine classifies access attempts; PatientRecord validates at construction.
public class P1IntakeValidator {

    // Deterministic rule table: private / default / protected / public × 3 contexts.
    // protected behaves like default across SAME_CLASS, SAME_PACKAGE, DIFFERENT_PACKAGE
    // (the distinction only appears with cross-package inheritance — added in P2).
    private static final boolean[][] RULE_TABLE = {
        // SAME_CLASS  SAME_PACKAGE  DIFFERENT_PACKAGE
        { true,  false, false },  // private
        { true,  true,  false },  // default
        { true,  true,  false },  // protected
        { true,  true,  true  },  // public
    };

    public static String classifyAccess(String fieldModifier, String accessorContext) {
        int modRow = switch (fieldModifier) {
            case "private" -> 0;
            case "default" -> 1;
            case "protected" -> 2;
            case "public" -> 3;
            default -> -1;
        };
        int ctxCol = switch (accessorContext) {
            case "SAME_CLASS" -> 0;
            case "SAME_PACKAGE" -> 1;
            case "DIFFERENT_PACKAGE" -> 2;
            default -> -1;
        };
        if (modRow < 0 || ctxCol < 0) return "UNKNOWN";
        return RULE_TABLE[modRow][ctxCol] ? "ALLOWED" : "DENIED";
    }

    public static String summarizeBatch(String[][] attempts) {
        int allowed = 0, denied = 0;
        for (String[] row : attempts) {
            if ("ALLOWED".equals(classifyAccess(row[0], row[1]))) allowed++;
            else denied++;
        }
        return "Allowed: " + allowed + " | Denied: " + denied;
    }

    // PatientRecord: patientId private (unique), wardCode protected (package + subclass),
    // vitalsScore private (sensitive), facilityName public (info).
    // No usable no-arg constructor (no default constructor defined).
    public static class PatientRecord {
        private final String patientId;
        protected final String wardCode;
        private final double vitalsScore;
        public final String facilityName;

        public PatientRecord(String patientId, String wardCode, double vitalsScore, String facilityName) {
            if (patientId == null || patientId.trim().isEmpty() || patientId.trim().length() < 4)
                throw new IllegalArgumentException("patientId rejected");
            this.patientId = patientId.trim();
            this.wardCode = wardCode;
            this.vitalsScore = vitalsScore;
            this.facilityName = facilityName;
        }

        public String getPatientId() { return patientId; }
        public double getVitalsScore() { return vitalsScore; }
    }

    public static void main(String[] args) {
        // Example 1
        System.out.println(classifyAccess("private", "SAME_CLASS"));         // ALLOWED
        // Example 2
        System.out.println(classifyAccess("default", "DIFFERENT_PACKAGE"));  // DENIED
        // Example 3
        System.out.println(summarizeBatch(new String[][] {
            {"protected", "SAME_PACKAGE"},
            {"protected", "DIFFERENT_PACKAGE"},
            {"public", "DIFFERENT_PACKAGE"}
        })); // Allowed: 2 | Denied: 1
        // Example 4 — construction rejected (MT9 is 3 chars)
        try {
            new PatientRecord("MT9", "W3", 98.2, "MediTrack Central");
            System.out.println("constructed");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        // Compare: MT94 should succeed
        try {
            new PatientRecord("MT94", "W3", 98.2, "MediTrack Central");
            System.out.println("constructed ok");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
    }
}
