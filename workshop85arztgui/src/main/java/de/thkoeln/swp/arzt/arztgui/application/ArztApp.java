package de.thkoeln.swp.arzt.arztgui.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ArztApp extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage)
    {
        Parent content;
        try {

            FXMLLoader viewLoader = new FXMLLoader();
            viewLoader.setLocation(getClass().getResource("/fxml/arzt.fxml"));
            content = viewLoader.load();

            stage = new Stage();
            stage.setTitle("SWP - ABC - Arzt");
            stage.setScene(new Scene(content, 400, 200));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeArztApp()
    {
        if(stage != null)
            stage.close();
    }

    public static void main(String[] args) { launch(args); }
}
