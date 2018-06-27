package com.solarchargerdatamonitor.database;

import javafx.beans.property.*;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

/**
 * Created by Zeeshan on 17/10/2017.
 */
public class DeviceTableRowView {

    private IntegerProperty Id;
    private ObjectProperty<Hyperlink> Name;
    private ObjectProperty<Circle> Status;
    private ObjectProperty<Label> register4115;
    private DoubleProperty register4116;
    private DoubleProperty register4117;
    private DoubleProperty register4119;
    private StringProperty register4120;
    private DoubleProperty register4121;
    private ObjectProperty<Label> register4133;
    private ObjectProperty<Label> register4134;

    public DeviceTableRowView(){

        this.Id = new SimpleIntegerProperty();
        this.Name = new SimpleObjectProperty();
        this.Status = new SimpleObjectProperty();

        this.register4115 = new SimpleObjectProperty();
        this.register4116 = new SimpleDoubleProperty();
        this.register4117 = new SimpleDoubleProperty();
        this.register4119 = new SimpleDoubleProperty();
        this.register4120 = new SimpleStringProperty();
        this.register4121 = new SimpleDoubleProperty();
        this.register4133 = new SimpleObjectProperty();
        this.register4134 = new SimpleObjectProperty();
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

    public Hyperlink getName() {
        return Name.get();
    }

    public ObjectProperty nameProperty() {
        return Name;
    }

    public void setName(Hyperlink name) {
        this.Name.set(name);
    }

    public Circle getStatus() {
        return Status.get();
    }

    public ObjectProperty<Circle> statusProperty() {
        return Status;
    }

    public void setStatus(Circle status) {
        this.Status.set(status);
    }

    public Label getRegister4115() {
        return register4115.get();
    }

    public ObjectProperty<Label> register4115Property() {
        return register4115;
    }

    public void setRegister4115(Label register4115) {
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

    public Label getRegister4133() {
        return register4133.get();
    }

    public ObjectProperty<Label> register4133Property() {
        return register4133;
    }

    public void setRegister4133(Label register4133) {
        this.register4133.set(register4133);
    }

    public Label getRegister4134() {
        return register4134.get();
    }

    public ObjectProperty<Label> register4134Property() {
        return register4134;
    }

    public void setRegister4134(Label register4134) {
        this.register4134.set(register4134);
    }
}
