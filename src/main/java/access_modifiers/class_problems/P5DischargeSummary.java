package access_modifiers.class_problems;

// P5. Immutable Discharge Summary & Nightly Ledger (capstone)
// Final class, all fields final, defensive copy in/out, wither pattern,
// static block, instanceof dispatch, null-safe batch processing.
public final class P5DischargeSummary {

    public static class DischargeSummary {
        private final String patientId;
        private final String[] medicationCodes;

        private static final java.util.regex.Pattern MED_PATTERN =
                java.util.regex.Pattern.compile("^MED-[A-Z]$");

        static {
            System.out.println("[DischargeSummary] static block initialised");
        }

        public DischargeSummary(String patientId, String[] medicationCodes) {
            if (patientId == null || patientId.trim().isEmpty())
                throw new IllegalArgumentException("patientId required");
            if (medicationCodes == null)
                throw new IllegalArgumentException("medicationCodes required");
            for (String code : medicationCodes) {
                if (code == null || !MED_PATTERN.matcher(code).matches())
                    throw new IllegalArgumentException("Bad med code: " + code);
            }
            this.patientId = patientId.trim();
            // defensive copy IN
            this.medicationCodes = medicationCodes.clone();
        }

        // Defensive copy OUT
        public String[] getMedicationCodes() {
            return medicationCodes.clone();
        }

        public String getPatientId() { return patientId; }

        // Wither: returns a brand-new object with one corrected entry.
        public DischargeSummary withCorrectedMedication(int index, String newCode) {
            if (newCode == null || !MED_PATTERN.matcher(newCode).matches())
                throw new IllegalArgumentException("Bad med code: " + newCode);
            String[] copy = medicationCodes.clone();
            copy[index] = newCode;
            return new DischargeSummary(patientId, copy);
        }
    }

    public static final class CriticalCareDischargeSummary extends DischargeSummary {
        private final int icuDays;

        public CriticalCareDischargeSummary(String patientId, String[] medicationCodes, int icuDays) {
            super(patientId, medicationCodes);
            this.icuDays = icuDays;
        }

        public int getIcuDays() { return icuDays; }
    }

    public static String processNightlyBatch(DischargeSummary[] summaries) {
        int processed = 0, nullSkipped = 0, critical = 0, routine = 0;
        for (DischargeSummary s : summaries) {
            if (s == null) { nullSkipped++; continue; }
            if (s instanceof CriticalCareDischargeSummary) critical++;
            else routine++;
            processed++;
        }
        return processed + " processed | " + nullSkipped + " null skipped | "
                + critical + " critical-care | " + routine + " routine";
    }

    public static void main(String[] args) {
        // Example 1 — bad med code rejected
        try {
            new DischargeSummary("MT2026-0142", new String[]{"MED-A", "bad"});
            System.out.println("constructed");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        // Example 2 — defensive copy + wither
        DischargeSummary d = new DischargeSummary("MT2026-0142", new String[]{"MED-A", "MED-B"});
        String[] codes = d.getMedicationCodes();
        codes[0] = "TAMPERED";
        System.out.println(d.getMedicationCodes()[0]); // MED-A (untouched)
        // Example 3 — batch
        System.out.println(processNightlyBatch(new DischargeSummary[]{
            new CriticalCareDischargeSummary("MT001", new String[]{"MED-X"}, 4),
            null,
            new DischargeSummary("MT002", new String[]{"MED-Y"})
        })); // 2 processed | 1 null skipped | 1 critical-care | 1 routine
    }
}
