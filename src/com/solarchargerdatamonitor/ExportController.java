package com.solarchargerdatamonitor;

import com.solarchargerdatamonitor.database.Device;
import com.solarchargerdatamonitor.database.DeviceDao;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class ExportController extends BaseController {

    @FXML
    public Label exportHeadingLabel;

    final static Logger logger = Logger.getLogger(ExportController.class);
    public ComboBox deviceList;
    public DatePicker fromDate;
    public DatePicker toDate;
    public ProgressIndicator progressIndicator;
    public Button exportToCSVButton;
    public Label downloadMessage;

    //Initializing the controller class.
    //This method is automatically called after the fxml file has been loaded.
    @FXML
    private void initialize () {

        logger.info("Initializing Export Screen.");

        try {

            this.deviceList.setItems(null);

            //ObservableList<Device> devicesObservableList = DeviceDao.selectDevicesForComboBox();
            ObservableList<Device> devicesObservableList = DeviceDao.selectDevices();
            if (devicesObservableList.size() > 0) {
                this.deviceList.setItems(devicesObservableList);
                this.deviceList.getSelectionModel().selectFirst();
            }

            this.deviceList.setConverter(new StringConverter<Device>(){
                @Override
                public String toString(Device device) {

                    System.out.print("converting object: ");
                    if (device==null) {
                        System.out.println("null");
                        return "[none]";
                    }
                    System.out.println(device.getName().toString());
                    return device.getName().toString();
                }

                @Override
                public Device fromString(String name) {
                    throw new RuntimeException("not required for non editable ComboBox");
                }

            });

        } catch (Exception e){
            logger.error("Error occurred while getting devices information from DB.", e);
            this.deviceList.setItems(null);
        }
    }

    public void showFileChooser(ActionEvent event) {

        if(isValid()) {

            Stage stage = (Stage) exportToCSVButton.getScene().getWindow();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV");

            Device selectedDevice = (Device) deviceList.getSelectionModel().getSelectedItem();
            String deviceName = "All";
            if(selectedDevice.getId() != 0){
                deviceName = selectedDevice.getName().replace(" ", "_");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fromDateStr = fromDate.getValue().format(formatter).toString();
            String toDateStr = toDate.getValue().format(formatter).toString();
            String fileName = deviceName + "_log_from_" + fromDateStr + "_to_" + toDateStr + ".csv";
            fileChooser.setInitialFileName(fileName);

            //Set to user directory or go to default if cannot access
            String userDirectoryString = System.getProperty("user.home");
            File userDirectory = new File(userDirectoryString);
            if(!userDirectory.canRead()) {
                userDirectory = new File("c:/");
            }
            fileChooser.setInitialDirectory(userDirectory);

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Comma Separated Values (.csv)", "*.csv")
            );
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    logger.info(file.getPath() + " " + file.getName());

                    Task<Boolean> task = generateCSVTask(file);

                    task.setOnRunning((e) -> {
                        progressIndicator.setVisible(true);
                        exportToCSVButton.setVisible(false);
                    });

                    task.setOnSucceeded((e) -> {

                        if (task.getValue() == true) {

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Export to CSV");
                            alert.setHeaderText(null);
                            alert.setContentText("All data in given range has been exported successfully!");

                            alert.showAndWait();

                        } else {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Export to CSV");
                            alert.setHeaderText(null);
                            alert.setContentText("Unable to export data to CSV. Please try again.");

                            alert.showAndWait();
                        }

                        progressIndicator.setVisible(false);
                        exportToCSVButton.setVisible(true);

                    });

                    task.setOnFailed((e) -> {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Export to CSV");
                        alert.setHeaderText(null);
                        alert.setContentText("Unable to export data to CSV. Please try again.");

                        alert.showAndWait();

                        // eventual error handling by catching exceptions from task.get()
                        progressIndicator.setVisible(false);
                        exportToCSVButton.setVisible(true);

                    });

                    new Thread(task).start();

                } catch (Exception ex) {
                    logger.error("", ex);

                    StringWriter errors = new StringWriter();
                    ex.printStackTrace(new PrintWriter(errors));
                    this.showException("An error has occurred.", errors.toString());

                } finally {
                    progressIndicator.setVisible(false);
                    exportToCSVButton.setVisible(true);
                }
            }
        }
    }

    private boolean isValid(){
        Boolean result = true;
        String message = "";

        if(this.deviceList.getSelectionModel().isEmpty()){
            result = false;
            message = "Please select one from `Device`.";
        }else if(this.fromDate.getValue() == null || this.fromDate.getValue().equals("")){
            result = false;
            message = "Please enter `From` date.";
        }else if(this.toDate.getValue() == null || this.toDate.getValue().equals("")){
            result = false;
            message = "Please enter `To` date.";
        }else if(this.fromDate.getValue().isAfter(this.toDate.getValue())){
            result = false;
            message = "`From` date should be less than or equal to `To` date.";
        }

        if(result == false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText(message);
            alert.showAndWait();
        }

        return result;
    }

    private void showException(String message, String exceptionText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("");
        alert.setContentText(message);

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public Task<Boolean> generateCSVTask(File file) throws InterruptedException {
        return new Task<Boolean>() {
            @Override
            public Boolean call() throws InterruptedException {
                Boolean returnValue = null;

                ResultSet resultSet = null;

                        // Assume default encoding.
                FileWriter fileWriter = null;

                // Always wrap FileWriter in BufferedWriter.
                BufferedWriter bufferedWriter = null;

                logger.info("Starting Export to CSV Operation");

                Device selectedDevice = (Device) deviceList.getSelectionModel().getSelectedItem();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String fromDateStr = fromDate.getValue().format(formatter).toString();
                String toDateStr = toDate.getValue().format(formatter).toString();

                try {

                    if (!file.exists()) {
                        file.createNewFile();
                        logger.info(file.getPath() + " csv file has been created!");
                    }

                    logger.info("Starting writing to file operation");

                    // Assume default encoding.
                    fileWriter = new FileWriter(file.getPath());

                    // Always wrap FileWriter in BufferedWriter.
                    bufferedWriter =new BufferedWriter(fileWriter);

                    //String csv_header = "\"Date Time\", [4104], [4109], [4110], [4115], [4116], [4117], [4118], [4119], [4120], [4121], [4125], [4130], [4131], [4133], [4134], [4135], [4182], [4240], [4241], [4272], [4276], [4277], [4357], [4358], [4359], [4360], [4362], [4365], [4366], [4367], [4368], [4369], [4370], [4371], [4372]\n";
                    String csv_header = "[Date Time], [4104 Flags], [Battery Voltage], [Solar Panel Voltage], [Battery Current]," +
                            " [Avg. Daily Energy to Batt.], [Power from Solar Panels], [Charge Stage and State], [PV Input current], [Daily Amp Hr.]," +
                            " [Info Flags LSB (4130)], [Info Flags MSB (4130)], [FET Temp], [PCB Temp], [Minutes of no power counter], [Batt Curr.]," +
                            " [Batt. Volt.], [PV Volt.], [WhizBang Net Ahr LSB], [WhizBang Net Ahr MSB], [WhizBang Current]\n";

                    // Note that write() does not automatically
                    // append a newline character.
                    bufferedWriter.write(csv_header);

                    try {
                        // Always close files.
                        bufferedWriter.close();
                        fileWriter.close();
                    } catch (IOException ex){
                        logger.error("", ex);
                    }

                    // Assume default encoding.
                    fileWriter = new FileWriter(file.getPath(), true);

                    // Always wrap FileWriter in BufferedWriter.
                    bufferedWriter =new BufferedWriter(fileWriter);

                    resultSet = DeviceDao.getFilteredLog(selectedDevice.getId(), fromDateStr, toDateStr);

                    logger.info("Starting Loop on result set to generate CSV");
                    //Now Fetch RegistersLog for each device
                    while (resultSet.next()) {

                        Date dateLogged = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
                                .parse(resultSet.getString("dateLogged"));
                        String dateLoggedStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateLogged);

                        String csv = dateLoggedStr + ", ";
                        csv += resultSet.getDouble("infoFlags_4104") + ", ";
                        //csv += resultSet.getDouble("4109") + ", ";
                        //csv += resultSet.getDouble("4110") + ", ";
                        csv += resultSet.getDouble("batteryVoltage_4115") + ", ";
                        csv += resultSet.getDouble("inputVoltage_4116") + ", ";
                        csv += resultSet.getDouble("batteryCurrent_4117") + ", ";
                        csv += resultSet.getDouble("energyToBattery_4118") + ", ";
                        csv += resultSet.getDouble("powerToBattery_4119") + ", ";
                        csv += resultSet.getString("comboChargeStage_4120") + ", ";
                        csv += resultSet.getDouble("pvInputCurrent_4121") + ", ";
                        csv += resultSet.getDouble("ampHours_4125") + ", ";
                        csv += resultSet.getDouble("infoFlagsLSB_4130") + ", ";
                        csv += resultSet.getDouble("infoFlagsMSB_4131") + ", ";
                        csv += resultSet.getDouble("FET_Temperature_4133") + ", ";
                        csv += resultSet.getDouble("PCB_Temperature_4134") + ", ";
                        csv += resultSet.getDouble("niteMinutesNoPower_4135") + ", ";
                        //csv += resultSet.getDouble("4182") + ", ";
                        //csv += resultSet.getDouble("4240") + ", ";
                        //csv += resultSet.getDouble("4241") + ", ";
                        csv += resultSet.getDouble("inputCurrent_4272") + ", ";
                        csv += resultSet.getDouble("batteryVoltage_4276") + ", ";
                        csv += resultSet.getDouble("pvInputVoltage_4277") + ", ";
                        //csv += resultSet.getDouble("4357") + ", ";
                        //csv += resultSet.getDouble("4358") + ", ";
                        //csv += resultSet.getDouble("4359") + ", ";
                        //csv += resultSet.getDouble("4360") + ", ";
                        //csv += resultSet.getDouble("4362") + ", ";
                        //csv += resultSet.getDouble("4365") + ", ";
                        //csv += resultSet.getDouble("4366") + ", ";
                        //csv += resultSet.getDouble("4367") + ", ";
                        //csv += resultSet.getDouble("4368") + ", ";
                        csv += resultSet.getDouble("wbJrAmpHourNetLSB_4369") + ", ";
                        csv += resultSet.getDouble("wbJrAmpHourNetMSB_4370") + ", ";
                        csv += resultSet.getDouble("wbJrCurrent_4371");
                        //csv += resultSet.getDouble("4372");
                        csv += "\n";

                        // Note that write() does not automatically
                        // append a newline character.
                        bufferedWriter.write(csv);
                    }

                    logger.info("Ending Loop on result set to generate CSV");

                } catch (SQLException e) {
                    logger.error("", e);

                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    showException("An error has occurred.", errors.toString());

                } catch (ClassNotFoundException e) {
                    logger.error("", e);

                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    showException("An error has occurred.", errors.toString());

                } catch (IllegalAccessException e) {
                    logger.error("", e);

                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    showException("An error has occurred.", errors.toString());

                } catch (InstantiationException e) {
                    logger.error("", e);

                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    showException("An error has occurred.", errors.toString());

                } catch (Exception e) {
                    logger.error("", e);

                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    showException("An error has occurred.", errors.toString());
                } finally {

                    try {
                        if(resultSet != null) {
                            resultSet.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    try {
                        // Always close files.
                        bufferedWriter.close();
                        fileWriter.close();
                    } catch (IOException ex){
                        logger.error("", ex);
                    }

                    logger.info("Ending writing to file operation");
                }

                logger.info("Ending Export to CSV Operation");

                returnValue = true;
                // do your operation in here
                return returnValue;
            }
        };
    }
}
