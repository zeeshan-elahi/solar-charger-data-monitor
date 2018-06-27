package com.solarchargerdatamonitor.database;

import javafx.beans.property.*;

/**
 * Created by Zeeshan on 16/10/2017.
 */
public class RegistersLog {

    private IntegerProperty registersLogId;
    private IntegerProperty deviceId;
    private SimpleObjectProperty dateLogged;
    private DoubleProperty register4115;
    private DoubleProperty register4116;
    private DoubleProperty register4117;
    private DoubleProperty register4119;
    private StringProperty register4120;
    private DoubleProperty register4121;
    private DoubleProperty register4133;
    private DoubleProperty register4134;


    public RegistersLog(){
        this.registersLogId = new SimpleIntegerProperty();
        this.deviceId = new SimpleIntegerProperty();
        this.dateLogged = new SimpleObjectProperty();
        this.register4115 = new SimpleDoubleProperty();
        this.register4116 = new SimpleDoubleProperty();
        this.register4117 = new SimpleDoubleProperty();
        this.register4119 = new SimpleDoubleProperty();
        this.register4120 = new SimpleStringProperty();
        this.register4121 = new SimpleDoubleProperty();
        this.register4133 = new SimpleDoubleProperty();
        this.register4134 = new SimpleDoubleProperty();
    }

    public int getRegistersLogId() {
        return registersLogId.get();
    }

    public IntegerProperty registersLogIdProperty() {
        return registersLogId;
    }

    public void setRegistersLogId(int registersLogId) {
        this.registersLogId.set(registersLogId);
    }

    public int getDeviceId() {
        return deviceId.get();
    }

    public IntegerProperty deviceIdProperty() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId.set(deviceId);
    }

    public Object getDateLogged() {
        return dateLogged.get();
    }

    public SimpleObjectProperty dateLoggedProperty() {
        return dateLogged;
    }

    public void setDateLogged(Object dateLogged) {
        this.dateLogged.set(dateLogged);
    }

    public double getRegister4115() {
        return register4115.get();
    }

    public DoubleProperty register4115Property() {
        return register4115;
    }

    public void setRegister4115(double register4115) {
        this.register4115.set(register4115);
    }

    public double getRegister4116() {
        return register4116.get();
    }

    public DoubleProperty register4116Property() {
        return register4116;
    }

    public void setRegister4116(double register4116) {
        this.register4116.set(register4116);
    }

    public double getRegister4117() {
        return register4117.get();
    }

    public DoubleProperty register4117Property() {
        return register4117;
    }

    public void setRegister4117(double register4117) {
        this.register4117.set(register4117);
    }

    public double getRegister4119() {
        return register4119.get();
    }

    public DoubleProperty register4119Property() {
        return register4119;
    }

    public void setRegister4119(double register4119) {
        this.register4119.set(register4119);
    }

    public String getRegister4120() {
        return register4120.get();
    }

    public StringProperty register4120Property() {
        return register4120;
    }

    public void setRegister4120(String register4120) {
        this.register4120.set(register4120);
    }

    public double getRegister4121() {
        return register4121.get();
    }

    public DoubleProperty register4121Property() {
        return register4121;
    }

    public void setRegister4121(double register4121) {
        this.register4121.set(register4121);
    }

    public double getRegister4133() {
        return register4133.get();
    }

    public DoubleProperty register4133Property() {
        return register4133;
    }

    public void setRegister4133(double register4133) {
        this.register4133.set(register4133);
    }

    public double getRegister4134() {
        return register4134.get();
    }

    public DoubleProperty register4134Property() {
        return register4134;
    }

    public void setRegister4134(double register4134) {
        this.register4134.set(register4134);
    }
}
