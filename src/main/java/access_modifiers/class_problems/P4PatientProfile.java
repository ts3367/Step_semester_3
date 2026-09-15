package access_modifiers.class_problems;

// P4. PatientProfile JavaBean, Chained Constructors & Locker PIN
// Write-once patientId (public setter, second call silently ignored).
// Write-only lockerPin (setter exists, no getter anywhere).
public class P4PatientProfile {

    public static class PatientProfile {
        private String patientId;
        private String name;
        private boolean discharged;
        private String lockerPin;  // write-only — no getter

        // All three constructors chained via this(...) — single init path.
        public PatientProfile() {
            this(null, null);
        }

        public PatientProfile(String name) {
            this(null, name);
        }

        public PatientProfile(String patientId, String name) {
            this.patientId = patientId;  // may be null (intake hasn't happened yet)
            this.name = name;
            this.discharged = false;
            this.lockerPin = null;
        }

        public String getPatientId() { return patientId; }

        // Write-once: takes effect only the first time.
        public void setPatientId(String id) {
            if (this.patientId == null) {
                this.patientId = id;
            }
            // second+ calls silently ignored
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public boolean isDischarged() { return discharged; }
        public void setDischarged(boolean discharged) { this.discharged = discharged; }

        // Write-only: setter exists, no matching getter anywhere.
        public void setLockerPin(String pin) { this.lockerPin = pin; }
    }

    public static void main(String[] args) {
        // Example 1: name-only → patientId is null
        System.out.println(new PatientProfile("Arjun Iyer").getPatientId()); // null
        // Example 2: id+name → returns the id
        System.out.println(new PatientProfile("MT2026-0142", "Arjun Iyer").getPatientId()); // MT2026-0142
        // Example 3: write-once setter
        PatientProfile p = new PatientProfile();
        p.setPatientId("MT2026-0142");
        p.setPatientId("HACKED-0000"); // silently ignored
        System.out.println(p.getPatientId()); // MT2026-0142
    }
}
