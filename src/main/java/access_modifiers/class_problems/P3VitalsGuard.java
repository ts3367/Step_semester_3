package access_modifiers.class_problems;

// P3. Vitals Monitoring Encapsulation Guard
// All fields private; constructor reuses recordReading; getAllReadings returns defensive copy.
public class P3VitalsGuard {

    public static class PatientVitals {
        private static int nextPatientNum = 1;

        private final int patientNum;
        private final double[] readings;
        private int count;

        // Constructor seeds history via recordReading — single validation path.
        public PatientVitals(double[] initialReadings) {
            this.patientNum = nextPatientNum++;
            this.readings = new double[500]; // max 500 per constraints
            this.count = 0;
            if (initialReadings != null) {
                for (double r : initialReadings) {
                    recordReading(r);
                }
            }
        }

        // Silently reject readings <= 0 or > 45.
        public void recordReading(double reading) {
            if (reading <= 0 || reading > 45) return;
            if (count < readings.length) {
                readings[count++] = reading;
            }
        }

        public double getAverage() {
            if (count == 0) return 0.0;
            double sum = 0;
            for (int i = 0; i < count; i++) sum += readings[i];
            return sum / count;
        }

        // Defensive copy — every call returns a fresh array.
        public double[] getAllReadings() {
            double[] copy = new double[count];
            System.arraycopy(readings, 0, copy, 0, count);
            return copy;
        }
    }

    public static void main(String[] args) {
        // Example 1: -2 rejected silently
        PatientVitals v = new PatientVitals(new double[]{36.5, -2, 37.1});
        double[] r = v.getAllReadings();
        System.out.print("[");
        for (int i = 0; i < r.length; i++) {
            System.out.print(r[i] + (i < r.length - 1 ? ", " : ""));
        }
        System.out.println("]");  // [36.5, 37.1]

        // Example 2: defensive copy — tamper has no effect
        double[] copy = v.getAllReadings();
        copy[0] = 999;
        System.out.println(v.getAllReadings()[0]); // 36.5
    }
}
