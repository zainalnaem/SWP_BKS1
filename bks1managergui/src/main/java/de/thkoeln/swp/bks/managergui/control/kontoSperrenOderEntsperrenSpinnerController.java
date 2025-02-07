package de.thkoeln.swp.bks.managergui.control;

import de.thkoeln.swp.bks.managersteuerung.impl.IKontoManagerSteuerungImpl;
import de.thkoeln.swp.bks.steuerungapi.grenz.KontoGrenz;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class kontoSperrenOderEntsperrenSpinnerController implements Initializable {
    @FXML
    private Spinner<Double> spinner;
    @FXML
    double currentValue;
    KontoGrenz kontoGrenz;
    @FXML
    private Text kontostatus_Text, kontoDispo_Text;
    private int kontoId;
    private double kontostand;
    private double dispo;
    IKontoManagerSteuerungImpl iKontoManagerSteuerung;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(00.00,10000000.00);
        valueFactory.setValue(100.00);
        spinner.setValueFactory(valueFactory);
        currentValue = spinner.getValue();



        spinner.valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue<? extends Double> observableValue, Double aDouble, Double t1) {
                currentValue = spinner.getValue();
            }
        });
    }

    @FXML
    private void bestaetigenKontoSperrenOderEntsperrenToDestroyWindow_Button_On(ActionEvent event) {
        iKontoManagerSteuerung = new IKontoManagerSteuerungImpl();
        iKontoManagerSteuerung.setzeDispoLimit(kontoId,currentValue);

        try {
            if (currentValue <= 0) {
                throw new IllegalArgumentException("Bitte geben Sie nur eine positive Zahl ein.");
            }
            if (currentValue < Math.abs(kontostand)){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Erfolgreich");
                alert.setHeaderText("Konto erfolgreich gesperrt.");
                alert.setContentText("Das Konto wurde erfolgreich gesperrt.");
                alert.showAndWait();

                // Get the current stage and close it
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Erfolgreich");
                alert.setHeaderText("Konto erfolgreich entsperrt.");
                alert.setContentText("Das Konto wurde erfolgreich entsperrt.");
                alert.showAndWait();

                // Get the current stage and close it
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Ungueltiger Wert");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void abbrechenKontoSperrenOderEntsperrenToDestroyWindow_Button_On(ActionEvent event) {
        // Get the current stage and close it
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setKontoId(int kontoId) {
        this.kontoId = kontoId;
    }

    public void setKontostand(double kontostand) {
        this.kontostand = kontostand;
        if (kontostatus_Text != null) {
            kontostatus_Text.setText(String.valueOf(kontostand));
        }
    }

    public void setDispo(double dispo) {
        this.dispo = dispo;
        if (kontoDispo_Text != null) {
            kontoDispo_Text.setText(String.valueOf(dispo));
        }
    }
}
