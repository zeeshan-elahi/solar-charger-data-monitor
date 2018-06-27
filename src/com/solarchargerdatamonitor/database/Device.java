package com.solarchargerdatamonitor.database;

import javafx.beans.property.*;

/**
 * Created by Zeeshan on 16/10/2017.
 */
public class Device {

    private IntegerProperty Id;
    private StringProperty Name;
    private StringProperty IP;
    private IntegerProperty Port;
    private StringProperty powerControl;
    private IntegerProperty connectionStatus;
    private IntegerProperty deviceStatus;

    public Device(){
        this.Id = new SimpleIntegerProperty();
        this.Name = new SimpleStringProperty();
        this.IP = new SimpleStringProperty();
        this.Port = new SimpleIntegerProperty();
        this.powerControl = new SimpleStringProperty();
        this.connectionStatus = new SimpleIntegerProperty();
        this.deviceStatus = new SimpleIntegerProperty();
    }

    public int getId() {
        return Id.get();
    }

    public IntegerProperty idProperty() {
        return Id;
    }

    public void setId(int id) {
        this.Id.set(id);
    }

    public String getName() {
        return Name.get();
    }

    public StringProperty nameProperty() {
        return Name;
    }

    public void setName(String name) {
        this.Name.set(name);
    }

    public String getIP() {
        return IP.get();
    }

    public StringProperty IPProperty() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP.set(IP);
    }

    public int getPort() {
        return Port.get();
    }

    public IntegerProperty portProperty() {
        return Port;
    }

    public void setPort(int port) {
        this.Port.set(port);
    }

    public int getConnectionStatus() {
        return connectionStatus.get();
    }

    public IntegerProperty connectionStatusProperty() {
        return connectionStatus;
    }

    public void setConnectionStatus(int connectionStatus) {
        this.connectionStatus.set(connectionStatus);
    }

    public int getDeviceStatus() {
        return deviceStatus.get();
    }

    public IntegerProperty deviceStatusProperty() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus.set(deviceStatus);
    }

    public String getPowerControl() {
        return powerControl.get();
    }

    public StringProperty powerControlProperty() {
        return powerControl;
    }

    public void setPowerControl(String powerControl) {
        this.powerControl.set(powerControl);
    }
}
