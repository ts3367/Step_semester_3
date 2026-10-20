package system_design.assignment_problems;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Smart Lab Control Panel. A device holds capabilities as parts, so a new
// capability or a new combination never needs a new device subclass.
public class P3SmartLabControlPanel {

    public interface Capability {
        String getCapabilityName();

        String apply(String action, double value);
    }

    public static class PowerCapability implements Capability {
        private boolean on;

        @Override
        public String getCapabilityName() {
            return "Power";
        }

        @Override
        public String apply(String action, double value) {
            if ("ON".equalsIgnoreCase(action)) {
                on = true;
            } else if ("OFF".equalsIgnoreCase(action)) {
                on = false;
            } else {
                throw new IllegalArgumentException("power action must be ON or OFF.");
            }
            return on ? "ON" : "OFF";
        }

        public boolean isOn() {
            return on;
        }
    }

    public static class BrightnessCapability implements Capability {
        private int brightness;

        @Override
        public String getCapabilityName() {
            return "Brightness";
        }

        @Override
        public String apply(String action, double value) {
            if (value < 0 || value > 100) {
                throw new IllegalArgumentException("brightness must be between 0% and 100%.");
            }
            this.brightness = (int) value;
            return "brightness set to " + this.brightness + "%";
        }

        public int getBrightness() {
            return brightness;
        }
    }

    public static class TemperatureCapability implements Capability {
        private int temperature;

        @Override
        public String getCapabilityName() {
            return "Temperature";
        }

        @Override
        public String apply(String action, double value) {
            if (value < 16 || value > 30) {
                throw new IllegalArgumentException("temperature must be between 16\u00B0C and 30\u00B0C.");
            }
            this.temperature = (int) value;
            return "temperature set to " + this.temperature + "\u00B0C";
        }

        public int getTemperature() {
            return temperature;
        }
    }

    public static class Device {
        private final String name;
        private final Map<String, Capability> capabilities = new LinkedHashMap<>();

        public Device(String name, Capability... initialCapabilities) {
            this.name = name;
            for (Capability capability : initialCapabilities) {
                capabilities.put(capability.getCapabilityName(), capability);
            }
        }

        public String getName() {
            return name;
        }

        public Map<String, Capability> getCapabilities() {
            return capabilities;
        }

        public boolean hasCapability(String capabilityName) {
            return capabilities.containsKey(capabilityName);
        }

        public void addCapability(Capability capability) {
            capabilities.put(capability.getCapabilityName(), capability);
            System.out.println(name + ": " + capability.getCapabilityName() + " capability added.");
        }

        public boolean applyStep(SceneStep step) {
            Capability capability = capabilities.get(step.getCapabilityName());
            if (capability == null) {
                return false;
            }
            try {
                String result = capability.apply(step.getAction(), step.getValue());
                System.out.println(name + ": " + result + ".");
                return true;
            } catch (IllegalArgumentException e) {
                System.out.println("Rejected: " + name + " " + e.getMessage());
                return false;
            }
        }
    }

    public static class SceneStep {
        private final String capabilityName;
        private final String action;
        private final double value;

        public SceneStep(String capabilityName, String action, double value) {
            this.capabilityName = capabilityName;
            this.action = action;
            this.value = value;
        }

        public String getCapabilityName() {
            return capabilityName;
        }

        public String getAction() {
            return action;
        }

        public double getValue() {
            return value;
        }
    }

    public static class Scene {
        private final String name;
        private final List<SceneStep> steps = new ArrayList<>();

        public Scene(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<SceneStep> getSteps() {
            return steps;
        }

        public void addStep(SceneStep step) {
            steps.add(step);
        }
    }

    public static class SmartLab {
        private final List<Device> devices = new ArrayList<>();

        public List<Device> getDevices() {
            return devices;
        }

        public void addDevice(Device device) {
            devices.add(device);
        }

        public Device findDevice(String name) {
            for (Device device : devices) {
                if (device.getName().equalsIgnoreCase(name)) {
                    return device;
                }
            }
            return null;
        }

        public void runScene(Scene scene) {
            System.out.println("Scene '" + scene.getName() + "' started.");
            int applied = 0;
            for (SceneStep step : scene.getSteps()) {
                for (Device device : devices) {
                    if (device.applyStep(step)) {
                        applied++;
                    }
                }
            }
            System.out.println("Scene '" + scene.getName() + "' completed: " + applied + " actions applied.");
        }
    }

    public static void main(String[] args) {
        SmartLab lab = new SmartLab();

        Device labAc = new Device("Lab AC", new PowerCapability(), new TemperatureCapability());
        lab.addDevice(labAc);

        Device ceilingLights = new Device("Ceiling Lights", new PowerCapability(), new BrightnessCapability());
        lab.addDevice(ceilingLights);

        Device projector = new Device("Projector", new PowerCapability());
        lab.addDevice(projector);

        Scene lectureMode = new Scene("Lecture Mode");
        lectureMode.addStep(new SceneStep("Power", "ON", 0));
        lectureMode.addStep(new SceneStep("Brightness", "SET", 40));
        lectureMode.addStep(new SceneStep("Temperature", "SET", 24));
        lab.runScene(lectureMode);

        labAc.applyStep(new SceneStep("Temperature", "SET", 12));

        projector.addCapability(new BrightnessCapability());
        projector.applyStep(new SceneStep("Brightness", "SET", 70));
    }
}
