package de.thkoeln.swp.arzt.arztgui.control;

import de.thkoeln.swp.arzt.arztsteuerung.impl.ArztSteuerungImpl;
import de.thkoeln.swp.arzt.grenzklassen.ArztGrenz;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ArztController {

    @FXML
    private Button Abbrechen_Button;

    @FXML
    private Button Einfuegen_Button;

    @FXML
    private Label Name_Label;

    @FXML
    private TextField Name_TextField;

    @FXML
    private Label Wohnort_Label;

    @FXML
    private TextField Wohnort_TextField;


    @FXML
    public void onAbbrechenButtonClick(ActionEvent event) {
        Abbrechen_Button.getScene().getWindow().hide();
    }

    @FXML
    public void insertButton(ActionEvent event) {
        String name = Name_TextField.getText();
        String wohnort = Wohnort_TextField.getText();

       ArztGrenz arztGrenz = new ArztGrenz();
        arztGrenz.setName(name);
        arztGrenz.setWohnort(wohnort);
        ArztSteuerungImpl arztSteuerung = new ArztSteuerungImpl();
        boolean erfolgreich = arztSteuerung.insertArzt(arztGrenz);
        if (erfolgreich) {
            System.out.println("EINGEFUEGT");
        }else {
            System.out.println("Fehler beim Einfügen des Arztes.");
        }


    }

}
