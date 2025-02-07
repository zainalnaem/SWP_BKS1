package de.thkoeln.swp.arzt.bootloader;

import de.thkoeln.swp.arzt.arztgui.application.ArztApp;
import de.thkoeln.swp.arzt.componentcontroller.services.IActivateComponent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    ChoiceBox cbComponent;
    @FXML
    ImageView ampelArzt;
    @FXML
    Button bt_login;
    @FXML
    TextField logTextField;
    @FXML
    TextField tf_login;

    Image greenAmpel = new Image("greenlight24x24.png");
    Image yellowAmpel = new Image("yellowlight24x24.png");

    private List<IActivateComponent> actList;

    private ArztApp arztApp;

    private String defaultTextfieldStyle;

    @FXML
    public void loginButtonClicked(){

        Stage stage = (Stage)bt_login.getScene().getWindow();

        logTextField.setStyle(defaultTextfieldStyle);
        String component = cbComponent.getValue().toString();
        try {
            switch (component) {
                case "Arzt":
                    if (actList.get(0).activateComponent(Integer.valueOf(tf_login.getText()))) {
                        arztApp = new ArztApp();
                        arztApp.start(stage);
                        ampelArzt.setImage(greenAmpel);
                        logTextField.setText("Arzt Login successful");
                    } else {
                        logTextField.setStyle("-fx-background-color: red;");
                        logTextField.setText("Arzt Login failed");
                        ampelArzt.setImage(yellowAmpel);
                    }
                    break;
            }
        } catch (UnsupportedOperationException e){
            logTextField.setStyle("-fx-background-color: red;");
            logTextField.setText("Not Yet Supported !!");
        }
    }

    @FXML
    public void logoutButtonClicked(){
        String component = cbComponent.getValue().toString();
        try {
            switch (component) {
                case "Arzt":
                    if (actList.get(0).deactivateComponent()) {
                        if(arztApp != null) { arztApp.closeArztApp(); }
                        ampelArzt.setImage(yellowAmpel);
                        logTextField.setText("Arzt Logout successful");
                    } else {
                        logTextField.setStyle("-fx-background-color: red;");
                        logTextField.setText("Arzt was not logged in!");
                    }
                    break;
            }
        } catch (UnsupportedOperationException e){
            logTextField.setStyle("-fx-background-color: red;");
            logTextField.setText("Not Yet Supported !!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        defaultTextfieldStyle = logTextField.getStyle();
        actList = new ArrayList<>();
        actList.add(new de.thkoeln.swp.arzt.arztsteuerung.impl.IActivateComponentImpl());
    }
}
