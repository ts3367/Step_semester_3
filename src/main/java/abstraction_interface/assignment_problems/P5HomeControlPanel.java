package abstraction_interface.assignment_problems;

// P5. Connected Home Control Panel
// WashingMachine and Refrigerator share HomeDevice but adopt different
// capabilities; MobileApp is only RemoteControllable and is not a device.
public class P5HomeControlPanel {

    public abstract static class HomeDevice {
        // Shared static counter: incremented once inside HomeDevice's own constructor.
        private static int serialCounter = 1000;

        private final String serialNumber;

        protected HomeDevice() {
            serialCounter++;
            this.serialNumber = "HD-" + serialCounter;
        }

        public abstract String activate();

        public String getSerialNumber() {
            return serialNumber;
        }
    }

    public interface RemoteControllable {
        String connect(String appId);
    }

    public interface EnergyTrackable {
        double getConsumptionWatts();
    }

    // Implements BOTH capabilities.
    public static class WashingMachine extends HomeDevice implements RemoteControllable, EnergyTrackable {
        private final double consumptionWatts;

        public WashingMachine(double consumptionWatts) {
            this.consumptionWatts = consumptionWatts;
        }

        @Override
        public String activate() {
            return "Washing machine " + getSerialNumber() + " started a cycle";
        }

        @Override
        public String connect(String appId) {
            return getSerialNumber() + " connected to " + appId;
        }

        @Override
        public double getConsumptionWatts() {
            return consumptionWatts;
        }
    }

    // Same parent as WashingMachine, but only EnergyTrackable - sharing a parent
    // never forces two siblings to share the same capabilities.
    public static class Refrigerator extends HomeDevice implements EnergyTrackable {
        private final double consumptionWatts;

        public Refrigerator(double consumptionWatts) {
            this.consumptionWatts = consumptionWatts;
        }

        @Override
        public String activate() {
            return "Refrigerator " + getSerialNumber() + " started cooling";
        }

        @Override
        public double getConsumptionWatts() {
            return consumptionWatts;
        }
    }

    // Only RemoteControllable, with no relationship to HomeDevice - it connects
    // to devices but it is not one.
    public static class MobileApp implements RemoteControllable {
        private final String appName;

        public MobileApp(String appName) {
            this.appName = appName;
        }

        public String getAppName() {
            return appName;
        }

        @Override
        public String connect(String appId) {
            return appName + " connected to " + appId;
        }
    }

    // Loops a mixed array and calls connect() on each.
    public static void connectAll(RemoteControllable[] items, String appId) {
        if (items == null) return;
        for (RemoteControllable item : items) {
            if (item == null) continue;
            System.out.println(item.connect(appId));
        }
    }

    // instanceof guard, then a safe cast to read the energy consumption.
    public static double getConsumptionIfTrackable(HomeDevice d) {
        if (d instanceof EnergyTrackable trackable) {
            return trackable.getConsumptionWatts();
        }
        return -1.0;
    }

    public static void main(String[] args) {
        WashingMachine wm = new WashingMachine(500.0);
        System.out.println(wm.activate());               // Washing machine HD-1001 started a cycle
        System.out.println(wm.connect("HomeConnect"));   // HD-1001 connected to HomeConnect

        Refrigerator fridge = new Refrigerator(150.0);
        System.out.println(getConsumptionIfTrackable(fridge)); // 150.0

        MobileApp app = new MobileApp("HomeConnect App");
        System.out.println(app.connect("HomeConnect"));   // HomeConnect App connected to HomeConnect

        // Upcasting: a WashingMachine stored as its parent type.
        HomeDevice ref = wm;
        System.out.println(getConsumptionIfTrackable(ref)); // 500.0

        connectAll(new RemoteControllable[]{wm, app}, "HomeConnect");
    }
}
