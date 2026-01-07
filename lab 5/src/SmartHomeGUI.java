import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Model class: SmartHome

class SmartHome {

    private String homeName;
    private ArrayList<Device> smartThings;

    public SmartHome(String homeName) {
        this.homeName = homeName;
        this.smartThings = new ArrayList<>();
    }

    public String getHomeName() {
        return homeName;
    }

    public ArrayList<Device> getSmartThings() {
        return smartThings;
    }

    /**
     * Creates a new device of the given type and name, and adds it to smartThings.
     *
     * @param type "SmartLight" or "SmartThermostat"
     * @param name device name
     * @throws Exception if type is unknown
     */
    public void addDevice(String type, String name) throws Exception {
        Device newDevice;

        if (type.equalsIgnoreCase("SmartLight")) {
            newDevice = new SmartLight(name);
        } else if (type.equalsIgnoreCase("SmartThermostat")) {
            newDevice = new SmartThermostat(name);
        } else {
            // requirement: throw Exception if type of Device cannot be created
            throw new Exception("Unknown device type: " + type);
        }

        smartThings.add(newDevice);
    }

    /**
     * Controls the device with the given name using the given command.
     *
     * controlCommand:
     *   '1' => turn on
     *   '0' => turn off
     *   '+' => increase setting
     *   '-' => decrease setting
     *
     * @return true if a device was found and controlled
     * @throws Exception if device not found or command invalid
     */
    public boolean controlDevice(String name, char controlCommand) throws Exception {
        Device target = null;

        for (Device dev : smartThings) {
            if (dev.getName().equals(name)) {
                target = dev;
                break;
            }
        }

        if (target == null) {
            // requirement: throw Exception if name is not in ArrayList smartThings
            throw new Exception("Device not found: " + name);
        }

        if (!(target instanceof Controllable)) {
            throw new Exception("Device is not controllable: " + name);
        }

        Controllable ctrl = (Controllable) target;

        switch (controlCommand) {
            case '1':
                target.turnOn();
                break;
            case '0':
                target.turnOff();
                break;
            case '+':
                // do not throw if at limit, just let increaseSetting() return false
                ctrl.increaseSetting();
                break;
            case '-':
                ctrl.decreaseSetting();
                break;
            default:
                // requirement: throw Exception if controlCommand is invalid
                throw new Exception("Invalid control command: " + controlCommand);
        }

        return true;
    }
}


// View + Controller: GUI
public class SmartHomeGUI extends JFrame {

    // Model
    private SmartHome myHome;

    // GUI widgets
    private JComboBox<String> deviceSelector;
    private JTextArea statusArea;
    private JButton onButton, offButton, increaseButton, decreaseButton;

    // Menu
    private JMenuBar menuBar;
    private JMenu deviceManageMenu, addDeviceItem;
    private JMenuItem addLight, addThermostat;

    public SmartHomeGUI() {
        super("Smart Home – MMA");

        // Initialize model with a unique name
        myHome = new SmartHome("Smart Home (MMA)");

        // Add at least two lights and two thermostats
        try {
            myHome.addDevice("SmartLight", "Living Room Light");
            myHome.addDevice("SmartLight", "Bedroom Light");
            myHome.addDevice("SmartThermostat", "Main Thermostat");
            myHome.addDevice("SmartThermostat", "Basement Thermostat");
        } catch (Exception e) {
            System.err.println("Error creating initial devices: " + e.getMessage());
        }

        // Basic frame setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 450);

        // Build the GUI
        buildGUI();

        // Show window
        setVisible(true);
    }

    /**
     * Builds and lays out all GUI components.
     */
    private void buildGUI() {
        // Menu bar and menus
        
        menuBar = new JMenuBar();
        deviceManageMenu = new JMenu("Devices");
        addDeviceItem = new JMenu("Add Device");      // submenu

        addLight = new JMenuItem("Add Smart Light");
        addThermostat = new JMenuItem("Add Smart Thermostat");

        // Hierarchy:
        // menuBar
        //   -> deviceManageMenu
        //        -> addDeviceItem
        //             -> addLight, addThermostat
        addDeviceItem.add(addLight);
        addDeviceItem.add(addThermostat);
        deviceManageMenu.add(addDeviceItem);
        menuBar.add(deviceManageMenu);
        setJMenuBar(menuBar);

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        // Device selector
        deviceSelector = new JComboBox<>();
        refreshDeviceSelector();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(deviceSelector, gbc);

        // Status area
        statusArea = new JTextArea(12, 45);
        statusArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statusArea);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(scrollPane, gbc);

        // Reset for buttons
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

        // Control buttons
        onButton = new JButton("On");
        offButton = new JButton("Off");
        increaseButton = new JButton("Increase");
        decreaseButton = new JButton("Decrease");

        gbc.gridy = 2;

        gbc.gridx = 0;
        add(onButton, gbc);

        gbc.gridx = 1;
        add(offButton, gbc);

        gbc.gridx = 2;
        add(increaseButton, gbc);

        gbc.gridx = 3;
        add(decreaseButton, gbc);

        // Button listeners
        onButton.addActionListener(e -> controlSelectedDevice('1'));
        offButton.addActionListener(e -> controlSelectedDevice('0'));
        increaseButton.addActionListener(e -> controlSelectedDevice('+'));
        decreaseButton.addActionListener(e -> controlSelectedDevice('-'));

        // Menu item listeners
        addLight.addActionListener(e -> addDeviceViaDialog("SmartLight"));
        addThermostat.addActionListener(e -> addDeviceViaDialog("SmartThermostat"));

        // Initial status
        updateStatus("");
    }

    /**
     * Repopulates the combo box with device names from myHome.
     */
    private void refreshDeviceSelector() {
        if (deviceSelector == null) {
            deviceSelector = new JComboBox<>();
        } else {
            deviceSelector.removeAllItems();
        }

        for (Device dev : myHome.getSmartThings()) {
            deviceSelector.addItem(dev.getName());
        }
    }

    /**
     * Called by button listeners to control whatever device is currently selected.
     */
    private void controlSelectedDevice(char cmd) {
        String selectedName = (String) deviceSelector.getSelectedItem();
        if (selectedName == null) {
            updateStatus("No device selected.");
            return;
        }

        try {
            myHome.controlDevice(selectedName, cmd);
            updateStatus("");
        } catch (Exception ex) {
            updateStatus(ex.getMessage());
        }
    }

    /**
     * Opens a dialog, creates a new device in myHome, and updates GUI.
     */
    private void addDeviceViaDialog(String type) {
        String prettyType = type.equalsIgnoreCase("SmartLight")
                ? "Smart Light"
                : "Smart Thermostat";

        String name = JOptionPane.showInputDialog(
                this,
                "Enter name for new " + prettyType + ":",
                "Add " + prettyType,
                JOptionPane.PLAIN_MESSAGE
        );

        if (name == null) {
            return; // user cancelled
        }

        name = name.trim();
        if (name.isEmpty()) {
            updateStatus("Device name cannot be empty.");
            return;
        }

        try {
            myHome.addDevice(type, name);
            refreshDeviceSelector();
            updateStatus("Added " + prettyType + " \"" + name + "\".");
        } catch (Exception ex) {
            updateStatus(ex.getMessage());
        }
    }

    /**
     * Updates the text in the statusArea with device info and optional error message.
     * This plays the same role as the provided updateStatus(String errorMsg) in your lab.
     */
    private void updateStatus(String errorMsg) {
        StringBuilder sb = new StringBuilder();

        if (errorMsg != null && !errorMsg.isEmpty()) {
            sb.append("Message: ").append(errorMsg).append("\n\n");
        }

        sb.append("Devices in ").append(myHome.getHomeName()).append(":\n");

        for (Device dev : myHome.getSmartThings()) {
            // We rely on SmartLight/SmartThermostat toString() showing on/off and setting.
            sb.append(" - ").append(dev.toString()).append("\n");
        }

        statusArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeGUI::new);
    }
}
