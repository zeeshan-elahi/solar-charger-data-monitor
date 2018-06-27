package com.solarchargerdatamonitor;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class Main extends Application {

    private MainController controller;

    final static Logger logger = Logger.getLogger(Main.class);

    public static HostServices hostServices ;

    @Override
    public void start(Stage primaryStage) throws Exception{

        hostServices = this.getHostServices();

        logger.info("Starting Application");
        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        primaryStage.setTitle("Solar Charger Data Monitor");
        primaryStage.setScene(new Scene(root));
        //primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception
    {
        logger.info("Stopping Application");

        if(controller != null)
        {
            controller.startHousekeeping();
        }

        super.stop();
        Platform.exit();
        System.exit(0);
    }
}
