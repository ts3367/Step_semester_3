package abstraction_interface.class_problems;

// P2. Home Safety Alert Network
// Alertable is a pure capability contract - it needs no shared ancestry at all.
public class P2SafetyAlertNetwork {

    // Interface: a shared promise, not a shared parent.
    public interface Alertable {
        String sendAlert(String message);
    }

    // Concrete base class - it is NOT alertable itself, it only holds shared state.
    public static class SecuritySensor {
        private final String zoneName;

        public SecuritySensor(String zoneName) {
            this.zoneName = zoneName;
        }

        public String getZoneName() {
            return zoneName;
        }
    }

    // Extends a concrete class AND implements the capability interface.
    public static class MotionSensor extends SecuritySensor implements Alertable {

        public MotionSensor(String zoneName) {
            super(zoneName);
        }

        @Override
        public String sendAlert(String message) {
            return "[" + getZoneName() + "] " + message;
        }
    }

    // Multilevel: three classes deep. The override reuses super's message and
    // only appends its own second zone on top.
    public static class DualZoneMotionSensor extends MotionSensor {
        private final String secondZoneName;

        public DualZoneMotionSensor(String zoneName, String secondZoneName) {
            super(zoneName);
            this.secondZoneName = secondZoneName;
        }

        public String getSecondZoneName() {
            return secondZoneName;
        }

        @Override
        public String sendAlert(String message) {
            return super.sendAlert(message) + " [also covering " + secondZoneName + "]";
        }
    }

    // Implements Alertable directly with ZERO connection to SecuritySensor -
    // proof that a capability interface only needs a shared promise.
    public static class SmokeDetector implements Alertable {
        private final String deviceId;

        public SmokeDetector(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        @Override
        public String sendAlert(String message) {
            return "[" + deviceId + "] " + message;
        }
    }

    // Loops a mixed array and calls sendAlert() on each, whatever it really is.
    public static void broadcastAll(Alertable[] devices, String message) {
        if (devices == null) return;
        for (Alertable device : devices) {
            if (device == null) continue;
            System.out.println(device.sendAlert(message));
        }
    }

    // instanceof guard, then a safe downcast to read the zone name.
    public static String getZoneIfMotionSensor(Alertable a) {
        if (a instanceof MotionSensor motionSensor) {
            return motionSensor.getZoneName();
        }
        return "Not a motion sensor";
    }

    public static void main(String[] args) {
        MotionSensor m = new MotionSensor("Living Room");
        System.out.println(m.sendAlert("Motion detected"));
        // Expected: [Living Room] Motion detected

        DualZoneMotionSensor d = new DualZoneMotionSensor("Hallway", "Stairwell");
        System.out.println(d.sendAlert("Motion detected"));
        // Expected: [Hallway] Motion detected [also covering Stairwell]

        SmokeDetector s = new SmokeDetector("SD-01");
        System.out.println(s.sendAlert("Smoke detected"));
        // Expected: [SD-01] Smoke detected

        System.out.println(getZoneIfMotionSensor(m));  // Living Room
        System.out.println(getZoneIfMotionSensor(s));  // Not a motion sensor

        broadcastAll(new Alertable[]{m, d, s}, "Test broadcast");
    }
}
