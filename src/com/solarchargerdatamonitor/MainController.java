package com.solarchargerdatamonitor;

import com.solarchargerdatamonitor.database.*;
import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimerTask;

public class MainController extends BaseController {

    @FXML
    public TableView devicesTable;

    public TableColumn<DeviceTableRowView, Hyperlink> deviceName;
    public TableColumn<DeviceTableRowView, Label> register4115;
    public TableColumn<DeviceTableRowView, Double> register4116;
    public TableColumn<DeviceTableRowView, Double> register4117;
    public TableColumn<DeviceTableRowView, Double> register4119;
    public TableColumn<DeviceTableRowView, Double> register4121;
    public TableColumn<DeviceTableRowView, Label> register4133;
    public TableColumn<DeviceTableRowView, Label> register4134;
    public TableColumn<DeviceTableRowView, String> register4120;
    public TableColumn<DeviceTableRowView, Circle> deviceStatus;
    public Label placeholderLabel;

    final static Logger logger = Logger.getLogger(MainController.class);

    //Initializing the controller class.
    //This method is automatically called after the fxml file has been loaded.
    @FXML
    private void initialize () {

        logger.info("Initializing Main Screen.");

        Properties properties = new Properties();

        try {
            InputStream fileInput = new FileInputStream("./application.properties");
            properties.load(fileInput);
            fileInput.close();
        } catch (Exception ex){
            logger.error("Error occurred in reading properties file.", ex);
        }

        int refreshInterval = Integer.parseInt(properties.getProperty("refreshInterval", "5")) * 1000;
        logger.info("Refresh Interval: " + refreshInterval);

        deviceName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        register4115.setCellValueFactory(cellData -> cellData.getValue().register4115Property());
        register4116.setCellValueFactory(cellData -> cellData.getValue().register4116Property().asObject());
        register4117.setCellValueFactory(cellData -> cellData.getValue().register4117Property().asObject());
        register4119.setCellValueFactory(cellData -> cellData.getValue().register4119Property().asObject());
        register4121.setCellValueFactory(cellData -> cellData.getValue().register4121Property().asObject());
        register4133.setCellValueFactory(cellData -> cellData.getValue().register4133Property());
        register4134.setCellValueFactory(cellData -> cellData.getValue().register4134Property());
        register4120.setCellValueFactory(cellData -> cellData.getValue().register4120Property());
        deviceStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        try {
            refreshTimer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    try {
                        searchDevices();
                    } catch (Exception ex) {
                        logger.error("", ex);
                    }
                }
            }, 0, refreshInterval);

        } catch (Exception ex) {
            logger.error("Error occurred in execution of TimerTask.", ex);
        }

    }

    //Search all Device
    @FXML
    public void searchDevices() {

        logger.info("Getting / Refreshing Devices information.");

        ResultSet devicesData = null;

        try {

            ObservableList<DeviceTableRowView> deviceTableRowViewObservableList = FXCollections.observableArrayList();

            //Get all Devices information
            devicesData = DeviceDao.getLatestLog();

            //Now Fetch RegistersLog for each device
            while (devicesData.next()) {

                DeviceTableRowView deviceTableRowView = new DeviceTableRowView();

                deviceTableRowView.setId(devicesData.getInt("Id"));

                String deviceName = devicesData.getString("name");
                String devicePowerControl = devicesData.getString("powerControl");
                Hyperlink deviceLink = new Hyperlink(deviceName);
                deviceLink.setId(devicePowerControl);
                deviceLink.setWrapText(true);
                deviceLink.setTextAlignment(TextAlignment.CENTER);
                deviceLink.setOnAction(event -> {
                    Hyperlink hyperlink = (Hyperlink) event.getSource();
                    String IP = hyperlink.getId();

                    Main.hostServices.showDocument("http://" + devicePowerControl);
                });
                deviceTableRowView.setName(deviceLink);

                //Update / set Status Indicator value
                String tooltipText = BaseController.greyStatusHelpText;
                Circle circle = new Circle(16);
                circle.setFill(Color.DARKGREY);
                int deviceStatus = devicesData.getInt("deviceStatus");

                if(deviceStatus == 1) {

                    int connectionStatus = devicesData.getInt("connectionStatus");
                    if (connectionStatus == 1) {
                        circle.setFill(Color.DARKGREEN);
                        tooltipText = BaseController.greenStatusHelpText;
                    } else {
                        circle.setFill(Color.RED);
                        tooltipText = BaseController.redStatusHelpText;
                    }
                }

                Tooltip tooltip = new Tooltip(tooltipText);
                Tooltip.install(circle, tooltip);

                deviceTableRowView.setStatus(circle);
                /////////////////////////////

                //Update / set Battery Voltage value as per register 4115
                deviceTableRowView.setRegister4115(getVoltageLabel(deviceStatus, devicesData.getDouble("batteryVoltage_4115")));
                /////////////////////////////////

                //Update / set FET Temperature value as per register 4133
                deviceTableRowView.setRegister4133(getTemperatureLabel(deviceStatus, devicesData.getDouble("FET_Temperature_4133")));
                /////////////////////////////////

                //Update / set PCB Temperature value as per register 4134
                deviceTableRowView.setRegister4134(getTemperatureLabel(deviceStatus, devicesData.getDouble("PCB_Temperature_4134")));
                /////////////////////////////////

                if(deviceStatus == 1) {

                    deviceTableRowView.setRegister4116(devicesData.getDouble("inputVoltage_4116"));
                    deviceTableRowView.setRegister4117(devicesData.getDouble("batteryCurrent_4117"));
                    deviceTableRowView.setRegister4119(devicesData.getDouble("powerToBattery_4119"));
                    deviceTableRowView.setRegister4120(devicesData.getString("comboChargeStage_4120"));
                    deviceTableRowView.setRegister4121(devicesData.getDouble("pvInputCurrent_4121"));
                }else{
                    deviceTableRowView.setRegister4116(0.0);
                    deviceTableRowView.setRegister4117(0.0);
                    deviceTableRowView.setRegister4119(0.0);
                    deviceTableRowView.setRegister4120("");
                    deviceTableRowView.setRegister4121(0.0);
                }

                deviceTableRowViewObservableList.add(deviceTableRowView);
            }

            //Populate Devices in TableView
            populateDevicesTable(deviceTableRowViewObservableList);

        } catch (Exception e){
            logger.error("Error occurred while getting devices information from DB.", e);
            this.devicesTable.setItems(null);
            this.placeholderLabel.setText("An error occurred in fetching information from database. Please check your connection information or database server status.");
        } finally {
            try {
                if(devicesData != null) {
                    devicesData.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Label getVoltageLabel(int deviceStatus, Double registerValue){
        Label registerLabel = new Label();
        registerLabel.setTextFill(Color.WHITE);
        Double voltageValue = 0.0;

        if(deviceStatus == 1){

            voltageValue = registerValue;

            if(voltageValue > 49){
                registerLabel.setTextFill(new Color(0.20,1,0,1));
            }else if(voltageValue < 45){
                registerLabel.setTextFill(Color.RED);
            }else{
                registerLabel.setTextFill(Color.YELLOW);
            }

        }

        registerLabel.setText(String.valueOf(voltageValue));

        return registerLabel;
    }

    private Label getTemperatureLabel(int deviceStatus, Double registerValue){
        Label registerLabel = new Label();
        registerLabel.setTextFill(Color.WHITE);
        Double temperatureValue = 0.0;

        if(deviceStatus == 1){

            temperatureValue = registerValue;

            if(temperatureValue < 52){
                registerLabel.setTextFill(new Color(0.20,1,0,1));
            }else if(temperatureValue > 59){
                registerLabel.setTextFill(Color.RED);
            }else{
                registerLabel.setTextFill(Color.YELLOW);
            }

        }

        registerLabel.setText(String.valueOf(temperatureValue));

        return registerLabel;
    }

    //Populate Devices
    @FXML
    private void populateDevicesTable (ObservableList<DeviceTableRowView> devicesData) throws ClassNotFoundException {
        //Set items to the devicesTable
        devicesTable.setItems(devicesData);
        if(devicesData.size() == 0){
            this.placeholderLabel.setText("No Device information to show.");
        }
    }

    public void startHousekeeping(){
        super.startHousekeeping();
    }

}
