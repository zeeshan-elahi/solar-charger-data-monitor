package com.solarchargerdatamonitor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Timer;

/**
 * Created by Zeeshan on 23/10/2017.
 */
public class BaseController {

    @FXML
    public VBox mainContainer;
    @FXML
    public Menu mainMenu;
    @FXML
    public MenuItem viewStatisticsMenuItem;
    @FXML
    public MenuItem exportMenuItem;
    @FXML
    public MenuItem exitMenuItem;

    public static final String greyStatusHelpText = "Disabled or Not Monitored";
    public static final String greenStatusHelpText = "(Enabled or Monitored) & Connected";
    public static final String redStatusHelpText = "(Enabled or Monitored) & Not Connected";

    Timer refreshTimer = new java.util.Timer();

    final static Logger logger = Logger.getLogger(BaseController.class);

    @FXML
    public void handleViewStatisticsMenuItemClicked(final ActionEvent event) throws IOException {
        loadScreen(event);
    }

    @FXML
    public void handleViewStatusIndicatorLegendsMenuItemClicked(final ActionEvent actionEvent) {

        Dialog dialog = new Dialog();
        dialog.setTitle("Status Indicators Information");
        dialog.setHeaderText("");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Circle greyCircle = new Circle(20);
        greyCircle.setFill(Color.DARKGREY);
        grid.add(greyCircle, 0, 0);
        grid.add(new Label("    " + BaseController.greyStatusHelpText), 1, 0);

        Circle redCircle = new Circle(20);
        redCircle.setFill(Color.DARKGREEN);
        grid.add(redCircle, 0, 1);
        grid.add(new Label("    " + BaseController.greenStatusHelpText), 1, 1);

        Circle greenCircle = new Circle(20);
        greenCircle.setFill(Color.RED);
        grid.add(greenCircle, 0, 2);
        grid.add(new Label("    " + BaseController.redStatusHelpText), 1, 2);

        dialog.getDialogPane().setContent(grid);

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());

        dialog.show();
    }

    @FXML
    public void handleExportMenuItemClicked(final ActionEvent event) throws IOException {
        loadScreen(event);
    }

    @FXML
    public void handleExitMenuItemClicked(final ActionEvent event)
    {
        this.startHousekeeping();
        Platform.exit();
        System.exit(0);
    }

    public void loadScreen(ActionEvent event) throws IOException {
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        Parent parent;

        if(event.getSource() == viewStatisticsMenuItem){
            //Load Statistics Page
            //stage = (Stage) devicesTable.getScene().getWindow();
            parent = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        }else{
            //Load Export Page
            //stage = (Stage) exportHeadingLabel.getScene().getWindow();
            parent = FXMLLoader.load(getClass().getResource("ExportView.fxml"));
            startHousekeeping();
        }

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    public void startHousekeeping(){
        logger.info("Stopping Refresh TimerTask");
        this.refreshTimer.cancel();
    }

}
