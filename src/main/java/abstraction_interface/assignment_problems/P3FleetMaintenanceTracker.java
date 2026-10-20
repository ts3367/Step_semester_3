package abstraction_interface.assignment_problems;

// P3. Fleet Maintenance Tracker
// ServiceableVehicle (abstract) + Insurable (interface) - two independent axes.
public class P3FleetMaintenanceTracker {

    public abstract static class ServiceableVehicle {
        private double mileage = 0.0;

        public ServiceableVehicle() {
        }

        public abstract String performMaintenance();

        // JavaBean accessors - mileage is private and only reachable through these.
        public double getMileage() {
            return mileage;
        }

        // Reject first, add second - never the other way around.
        public void addMileage(double km) {
            if (km < 0) {
                System.out.println("Rejected negative mileage");
                return;
            }
            this.mileage += km;
        }
    }

    // Insurance information is a completely separate concern from servicing.
    public interface Insurable {
        String getInsuranceInfo();
    }

    public static class Forklift extends ServiceableVehicle implements Insurable {
        private final String assetTag;

        public Forklift(String assetTag) {
            this.assetTag = assetTag;
        }

        public String getAssetTag() {
            return assetTag;
        }

        @Override
        public String performMaintenance() {
            return "Forklift " + assetTag + ": hydraulic and fork inspection complete";
        }

        @Override
        public String getInsuranceInfo() {
            return "Insured under fleet policy - Asset " + assetTag;
        }
    }

    // Multilevel: three classes deep. The override reuses Forklift's own
    // message and only appends its extra step.
    public static class HeavyDutyForklift extends Forklift {

        public HeavyDutyForklift(String assetTag) {
            super(assetTag);
        }

        @Override
        public String performMaintenance() {
            return super.performMaintenance() + " | high-pressure hydraulic check complete";
        }
    }

    // A serviceable machine that is simply not insured - exercises the else branch.
    public static class ScissorLift extends ServiceableVehicle {
        private final String assetTag;

        public ScissorLift(String assetTag) {
            this.assetTag = assetTag;
        }

        public String getAssetTag() {
            return assetTag;
        }

        @Override
        public String performMaintenance() {
            return "Scissor lift " + assetTag + ": deck and battery inspection complete";
        }
    }

    // The instanceof check is against the INTERFACE, not a specific class.
    public static String getInsuranceIfApplicable(ServiceableVehicle v) {
        if (v instanceof Insurable insurable) {
            return insurable.getInsuranceInfo();
        }
        return "No insurance record exists";
    }

    public static void main(String[] args) {
        Forklift f = new Forklift("FL-22");
        f.addMileage(120);
        System.out.println(f.getMileage());   // 120.0
        f.addMileage(-50);                   // rejected, mileage unchanged
        System.out.println(f.getMileage());   // 120.0

        System.out.println(f.performMaintenance());
        // Forklift FL-22: hydraulic and fork inspection complete

        HeavyDutyForklift hd = new HeavyDutyForklift("HD-9");
        System.out.println(hd.performMaintenance());
        // Forklift HD-9: hydraulic and fork inspection complete | high-pressure hydraulic check complete

        System.out.println(getInsuranceIfApplicable(f));
        // Insured under fleet policy - Asset FL-22

        System.out.println(getInsuranceIfApplicable(new ScissorLift("SL-4")));
        // No insurance record exists
    }
}
