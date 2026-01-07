
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Lab4Saa {

    public static void main(String[] args) {

        // Create a list of Devices (polymorphism)
        ArrayList<Device> devices = new ArrayList<>();

        // At least three SmartDevice objects
        devices.add(new SmartLight("Living Room Light"));
        devices.add(new SmartThermostat("Main Thermostat"));
        devices.add(new SmartLight("Bedroom Lamp"));

        // Optionally add more if you want
        // devices.add(new SmartThermostat("Basement Thermostat"));

        // Start the interactive menu
        menu(devices);
    }

    public static void menu(ArrayList<Device> al) {

        // Setting up variables used in loop
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);

        while (!exit) {

            // Separator
            System.out.println("_______________");
            System.out.println("_______________");

            // Print list of devices with numeric value
            for (int i = 0; i < al.size(); i++) {
                System.out.print("[" + i + "] ");
                al.get(i).performFunction(); // polymorphic call
            }

            // Input solicitation
            try {

                // Get device choice from user
                System.out.println("Enter number [0-" + (al.size() - 1) + "] of device to control, or anything else to exit.");
                int choice = scanner.nextInt();

                // Checking validity of choice
                if (choice > al.size() - 1 || choice < 0) {
                    System.out.print("Input out of range, exiting");
                    exit = true;
                } else {

                    // Get and apply setting option
                    ((Controllable) al.get(choice)).displaySetting();
                    System.out.println("Enter + to increase setting, - to decrease setting.");
                    System.out.println("Enter 1 to turn ON, 0 to turn OFF.");
                    scanner.nextLine(); // clear newline
                    String action = scanner.nextLine();

                    // Checking validity of action
                    if (action.equals("+")) {
                        if (!((Controllable) al.get(choice)).increaseSetting())
                            // Returned false, must be at limit or off
                            System.out.println("At maximum or not on");
                    } else if (action.equals("-")) {
                        if (!((Controllable) al.get(choice)).decreaseSetting())
                            // Returned false, must be at limit or off
                            System.out.println("At minimum or not on");
                    } else if (action.equals("1")) {
                        al.get(choice).turnOn();
                    } else if (action.equals("0")) {
                        al.get(choice).turnOff();
                    } else {
                        System.out.println("Input invalid for setting");
                    }
                    ((Controllable) al.get(choice)).displaySetting();
                }

            } catch (InputMismatchException e) {
                System.out.print("Input invalid for device, exiting");
                exit = true;
            } catch (Exception e) {
                System.out.print("Exception caught, exiting");
                exit = true;
            }

        }

        scanner.close();
    }
}


