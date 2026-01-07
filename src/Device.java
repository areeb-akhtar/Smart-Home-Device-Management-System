
// Lab 4 - Smart Home Device Management System

// 1. Interface SmartDevice
interface SmartDevice {
    void turnOn();
    void turnOff();
    void performFunction();   // device-specific behavior
}

// 3. Interface Controllable
//    increaseSetting / decreaseSetting:
//        - change the setting if possible
//        - return true if changed, false if not (at limit or device off)
//    displaySetting:
//        - print the current setting to System.out
interface Controllable {
    boolean increaseSetting();
    boolean decreaseSetting();
    void displaySetting();
}

// 2. Abstract Class Device
public abstract class Device implements SmartDevice {

    // TODO fields: name and isOn
    protected String name;
    protected boolean isOn;

    // Constructor taking name, isOn should start false
    public Device(String name) {
        this.name = name;
        this.isOn = false;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public boolean isOn() {
        return isOn;
    }

    // Provided in your lab, but shown here completed
    @Override
    public void turnOn() {
        isOn = true;
        System.out.println(name + " is now ON.");
    }

    @Override
    public void turnOff() {
        isOn = false;
        System.out.println(name + " is now OFF.");
    }

    // Each subclass must still implement performFunction()
    @Override
    public abstract void performFunction();

    // Optional but very useful for debugging, menus, and GUI (Lab 5)
    @Override
    public String toString() {
        return name + " (on: " + isOn + ")";
    }
}

// 4. Concrete Class: SmartLight
//    - extends Device
//    - implements Controllable
//    - brightness b in [0, 10]
//    - constructed with b = 5
class SmartLight extends Device implements Controllable {

    private int brightness;  // range 0 to 10 inclusive

    public SmartLight(String name) {
        super(name);
        this.brightness = 5;
    }

    @Override
    public void performFunction() {
        // Device-specific behavior description
        System.out.println("SmartLight \"" + name + "\" is "
                + (isOn ? "ON" : "OFF")
                + " with brightness " + brightness + ".");
    }

    @Override
    public boolean increaseSetting() {
        // Only change setting if the light is on
        if (!isOn) {
            return false;
        }
        if (brightness < 10) {
            brightness++;
            return true;
        }
        return false;  // at max
    }

    @Override
    public boolean decreaseSetting() {
        // Only change setting if the light is on
        if (!isOn) {
            return false;
        }
        if (brightness > 0) {
            brightness--;
            return true;
        }
        return false;  // at min
    }

    @Override
    public void displaySetting() {
        System.out.println("Brightness of \"" + name + "\" is " + brightness + " (range 0–10).");
    }

    @Override
    public String toString() {
        return "SmartLight \"" + name + "\" [on=" + isOn + ", brightness=" + brightness + "]";
    }
}

// 4. Concrete Class: SmartThermostat
//    - extends Device
//    - implements Controllable
//    - temperature t in [18.0, 28.0)
//    - constructed with t = 20.0
class SmartThermostat extends Device implements Controllable {

    private double temperature;  // 18.0 <= t < 28.0

    public SmartThermostat(String name) {
        super(name);
        this.temperature = 20.0;
    }

    @Override
    public void performFunction() {
        System.out.println("SmartThermostat \"" + name + "\" is "
                + (isOn ? "ON" : "OFF")
                + " with target temperature " + temperature + "°C.");
    }

    @Override
    public boolean increaseSetting() {
        // Only change setting if thermostat is on
        if (!isOn) {
            return false;
        }
        // choose step size 0.5 or 1.0, your choice; using 0.5 here
        double step = 0.5;
        if (temperature + step < 28.0) {
            temperature += step;
            return true;
        }
        return false;  // at or above upper limit
    }

    @Override
    public boolean decreaseSetting() {
        if (!isOn) {
            return false;
        }
        double step = 0.5;
        if (temperature - step >= 18.0) {
            temperature -= step;
            return true;
        }
        return false;  // at or below lower limit
    }

    @Override
    public void displaySetting() {
        System.out.println("Target temperature of \"" + name + "\" is "
                + temperature + "°C (range 18.0–28.0).");
    }

    @Override
    public String toString() {
        return "SmartThermostat \"" + name + "\" [on=" + isOn + ", temp=" + temperature + "°C]";
    }
}
