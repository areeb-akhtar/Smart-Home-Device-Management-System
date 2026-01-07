
# Lab 4 Smart Home Device Management System

## Overview

This project is a Java based Smart Home Device Management System developed for Lab 4. The objective of the lab is to design and implement an object oriented application that models different smart home devices using core OOP concepts, specifically interfaces, abstract classes, inheritance, and polymorphism.

The system simulates a smart home environment containing multiple device types such as smart lights and smart thermostats. All devices share common behavior through a unified interface while also supporting device specific functionality through polymorphic method calls.

This lab emphasizes clean object oriented design rather than persistence or graphical interfaces. It serves as the foundation for Lab 5, which extends the system into a graphical user interface using Java Swing.

---

## Learning Objectives

* Understand and apply interfaces to enforce behavioral contracts
* Use abstract classes to share common state and behavior
* Apply inheritance to model real world hierarchies
* Demonstrate polymorphism by interacting with different devices through a common interface
* Design a menu driven console application using Java

---

## System Design

The system is structured around three core abstractions.

### SmartDevice Interface

`SmartDevice` defines the basic contract that all smart devices must follow.

```java
interface SmartDevice {
    void turnOn();
    void turnOff();
    void performFunction();
}
```

This interface ensures that every device supports power control and a device specific operation.

---

### Controllable Interface

`Controllable` defines behavior for devices that expose adjustable settings.

```java
interface Controllable {
    boolean increaseSetting();
    boolean decreaseSetting();
    void displaySetting();
}
```

The return value indicates whether the operation was successful, allowing boundary conditions to be handled cleanly.

---

### Abstract Class Device

`Device` is an abstract class that implements `SmartDevice` and provides shared state and behavior.

Responsibilities include:

* Storing device name
* Tracking on or off state
* Implementing common power control logic

Concrete subclasses inherit from `Device` and override behavior where required.

---

## Concrete Device Implementations

### SmartLight

* Inherits from `Device`
* Implements `Controllable`
* Maintains a brightness level `b` in the range [0, 10]
* Initial brightness is set to 5
* `performFunction()` displays light status and brightness

Brightness can only be modified when the device is turned on.

---

### SmartThermostat

* Inherits from `Device`
* Implements `Controllable`
* Maintains a target temperature `t` in the range [18.0, 28.0)
* Initial temperature is set to 20.0
* `performFunction()` displays thermostat status and temperature

Temperature adjustments are restricted to valid bounds and require the device to be on.

---

## Polymorphism and Device Management

All devices are stored in a single collection of type `ArrayList<Device>`. This allows the system to treat all devices uniformly while still executing device specific behavior through method overriding.

Polymorphism is demonstrated when `performFunction()` is called on each device without knowing its concrete type at compile time.

---

## Menu Driven Console Interface

The application includes a menu driven console interface that allows the user to:

* View all registered devices
* Select a device by index
* Turn devices on or off
* Increase or decrease device settings
* View current device settings

The menu logic safely handles invalid input using exception handling and boundary checks.

The provided `menu(ArrayList<Device> al)` method drives all user interaction.

---

## Program Entry Point

The main class `Lab4<initials>.java` performs the following tasks:

* Creates at least three smart devices
* Adds them to an `ArrayList<Device>`
* Demonstrates polymorphism through `performFunction()` calls
* Launches the interactive menu system

---

## Example Interaction Flow

1. Program displays a numbered list of devices
2. User selects a device by index
3. User chooses an action

   * `1` to turn on
   * `0` to turn off
   * `+` to increase setting
   * `-` to decrease setting
4. Updated device status is displayed

---

## Error Handling

The application handles:

* Invalid device index selection
* Invalid control commands
* Input mismatches
* Attempts to modify settings when a device is off

Graceful exits occur when unrecoverable input errors are encountered.

---

## Project Structure

```
Lab4-SmartHome
│
├── Device.java
│   ├── interface SmartDevice
│   ├── interface Controllable
│   ├── abstract class Device
│   ├── class SmartLight
│   └── class SmartThermostat
│
├── Lab4<initials>.java
└── README.md
```

---

## Future Work and Lab 5 Extension

This lab directly supports Lab 5, which extends the same device architecture into a graphical user interface using Java Swing.

In Lab 5:

* Devices will be managed through a GUI
* A `SmartHome` manager class will encapsulate device logic
* Users will interact through buttons, menus, and dialogs
* The same `Device`, `SmartLight`, and `SmartThermostat` classes will be reused

This demonstrates proper separation of concerns and reuse of object oriented design.

---

## Conclusion

This project demonstrates a structured and extensible approach to modeling smart devices using Java object oriented programming principles. The system cleanly separates concerns, enforces contracts through interfaces, and uses polymorphism to manage diverse device behavior through a common abstraction.

The design choices made in this lab ensure that the system can scale naturally into more complex interfaces and interaction models.


