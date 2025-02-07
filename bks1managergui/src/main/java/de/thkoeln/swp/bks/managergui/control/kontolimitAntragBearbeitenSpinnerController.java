package de.thkoeln.swp.bks.managergui.control;

import de.thkoeln.swp.bks.managersteuerung.impl.IAntragManagerSteuerungImpl;
import de.thkoeln.swp.bks.managersteuerung.impl.IKontoManagerSteuerungImpl;
import de.thkoeln.swp.bks.steuerungapi.grenz.KontoGrenz;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class kontolimitAntragBearbeitenSpinnerController implements Initializable {
    @FXML
    private Spinner<Double> spinner;
    @FXML
    private TextArea daten_TextArea;
    @FXML
    private TextField kontoID_Label;
    @FXML
    private Label Kontoart_Label;
    @FXML
    private Label Erstellungsdatum_Label;
    @FXML
    private Label Kontostand_Label;
    @FXML
    private Label Kontostatus_Label;
    @FXML
    private Label aktuellerDispo_Label;
    @FXML
    private Label kundeID_Label;
    @FXML
    double currentValue;

    IAntragManagerSteuerungImpl iAntragManagerSteuerung;
    IKontoManagerSteuerungImpl iKontoManagerSteuerung;
    private int kontoId;
    private int antragID;
    private String status;
    private String daten;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(00.00, 10000000.00);
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
    private void eingeben_Button_On(ActionEvent event) {
        String kontoID_String = kontoID_Label.getText();
        try {
            kontoId = Integer.parseInt(kontoID_String); // Assign parsed value to kontoId

            iKontoManagerSteuerung = new IKontoManagerSteuerungImpl();
            KontoGrenz kontoGrenz = iKontoManagerSteuerung.getKontoById(kontoId); // Use kontoId here

            if (kontoGrenz != null) {
                Kontoart_Label.setText(kontoGrenz.getArt());
                Erstellungsdatum_Label.setText(kontoGrenz.getErstellungsdatum().toString()); // Convert LocalDate to String
                Kontostand_Label.setText(Double.toString(kontoGrenz.getKontostand())); // Convert double to String
                Kontostatus_Label.setText(kontoGrenz.getStatus());
                aktuellerDispo_Label.setText(Double.toString(kontoGrenz.getDispo())); // Convert double to String
                kundeID_Label.setText(Integer.toString(kontoGrenz.getKunde().getKid()));
            } else {
                Kontoart_Label.setText("Konto nicht gefunden");
                Erstellungsdatum_Label.setText("Konto nicht gefunden");
                Kontostand_Label.setText("Konto nicht gefunden");
                Kontostatus_Label.setText("Konto nicht gefunden");
                kundeID_Label.setText("Konto nicht gefunden");
                aktuellerDispo_Label.setText("Konto nicht gefunden");
            }
        } catch (NumberFormatException e) {
            Kontoart_Label.setText("Ungueltige Konto-ID");
            Erstellungsdatum_Label.setText("Ungueltige Konto-ID");
            Kontostand_Label.setText("Ungueltige Konto-ID");
            Kontostatus_Label.setText("Ungueltige Konto-ID");
            kundeID_Label.setText("Ungueltige Konto-ID");
            aktuellerDispo_Label.setText("Ungueltige Konto-ID");
        } catch (Exception e) {
            Kontoart_Label.setText("Error: " + e.getMessage());
            Erstellungsdatum_Label.setText("Error: " + e.getMessage());
            Kontostand_Label.setText("Error: " + e.getMessage());
            Kontostatus_Label.setText("Error: " + e.getMessage());
            kundeID_Label.setText("Error: " + e.getMessage());
            aktuellerDispo_Label.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void bestaetigenDispoBarbeitenToDestroyWindow_Button_On(ActionEvent event) {
        if (kontoID_Label.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Keine Konto ID eingegeben");
            alert.setContentText("Bitte geben Sie ein Konto ID ein.");
            alert.showAndWait();
        } else {
            try {
                // Check if currentValue is a number and positive
                if (currentValue <= 0) {
                    throw new IllegalArgumentException("Bitte geben Sie nur eine positive Zahl ein.");
                }

                if (status.equals("n")) {
                    iAntragManagerSteuerung = new IAntragManagerSteuerungImpl();
                    iAntragManagerSteuerung.updateKontolimit(antragID, kontoId, currentValue);

                    // Get the current stage and close it
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    stage.close();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Erfolgreich");
                    alert.setHeaderText("Dispo geändert");
                    alert.setContentText("Das Dispo des ausgewählten KL-Antrags ist erfolgreich geändert.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler");
                    alert.setHeaderText("Falscher Antragstatus");
                    alert.setContentText("Bitte nur Anträge mit Status 'n' auswählen.");
                    alert.showAndWait();
                }
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Ungueltiger Wert");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }


    @FXML
    private void abbrechenDispoBarbeitenToDestroyWindow_Button_On(ActionEvent event) {
        // Get the current stage and close it
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void setDaten(String daten) {
        this.daten = daten;
        if (daten_TextArea != null) {
            daten_TextArea.setText(String.valueOf(daten));
        }
    }

    @FXML
    public void setAntragID(int antragID) {
        this.antragID = antragID;
    }

    @FXML
    public void setStatus(String status) {
        this.status = status;
    }
}
