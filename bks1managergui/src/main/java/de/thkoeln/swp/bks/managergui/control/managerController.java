package de.thkoeln.swp.bks.managergui.control;

import de.thkoeln.swp.bks.managersteuerung.impl.IKontoManagerSteuerungImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class managerController {
    private int kontoId;
    IKontoManagerSteuerungImpl iKontoManagerSteuerung;
    @FXML
    private Label bestaetigenLabel;
    @FXML
    private TextArea mahnungTextField;
    @FXML
    private TextArea datenTextArea, kommentarTextArea;
    @FXML
    private RadioButton neuesProfilAntrag, bearbeitungsantrag, loeschungsantrag, mahnung, kontoSperrenEntsperren, genehmigen, ablehnen;

    //IKontoManagerSteuerungImpl iKontoManagerSteuerung;

    @FXML
    private void hauptfensterToAntragHauptfenster_Button_On(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/antragHauptfenster.fxml"));

            Scene scene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Antrag Hauptfenster");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ueberzogenenKontenHauptfensterToAntragHauptfenster_Button_On(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ueberzogeneKonten.fxml"));

            Scene scene = new Scene(loader.load());


            // Get the current stage and set the new scene
            Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ueberzogene Konten");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void abbrechenAntragStellenToCloseWindow_Button_On(ActionEvent event) {
        // Get the current stage and close it
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void bestaetigenAntragStellenToBearbeitungsAdmin_Button_On(ActionEvent event) {
        String antragTyp = null;
        String daten = datenTextArea.getText();
        String kommentar = kommentarTextArea.getText();


        // Check which radio button is selected and update the label
        if (neuesProfilAntrag.isSelected()) {
            bestaetigenLabel.setText("Sie haben " + neuesProfilAntrag.getText() + " gewaehlt");
            antragTyp = "so";
        } else if (bearbeitungsantrag.isSelected()) {
            bestaetigenLabel.setText("Sie haben " + bearbeitungsantrag.getText() + " gewaehlt");
            antragTyp = "sb";
        } else if (loeschungsantrag.isSelected()) {
            bestaetigenLabel.setText("Sie haben " + loeschungsantrag.getText() + " gewaehlt");
            antragTyp = "sd";
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bearbeitungsadmin.fxml"));
            Scene scene = new Scene(loader.load());

            bearbeitungsadminController controller = loader.getController();
            controller.setAntragTyp(antragTyp);
            controller.setDaten(daten);
            controller.setKommentar(kommentar);

            Stage mainStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            controller.setMainStage(mainStage);

            mainStage.setScene(scene);
            mainStage.setTitle("Bearbeitungsadmin");
            mainStage.setResizable(false);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void abbrechenGenehmigenOderAblehnenToCloseWindow_Button_On(ActionEvent event) {
        // Get the current stage and close it
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void abbrechenMahnungSchreibenOderKontoSperrenToDestroyWindow_Button_On(ActionEvent event) {
        // Get the current stage and close it
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void bestaetigenMahnungSchreibenOderKontoSperrenToRadioButtonChoice_Button_On(ActionEvent event) {
        if (mahnung.isSelected()){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mahnungSchreiben.fxml"));
                Scene scene = new Scene(loader.load());

                // Get the current stage and set the new scene
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Mahnung schreiben");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (kontoSperrenEntsperren.isSelected()){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/kontoSperrenOderEntsperren.fxml"));
                Scene scene = new Scene(loader.load());

                // Get the current stage and set the new scene
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Konto sperren oder entsperren");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void abbrechenMahnungSchreibenToDestroyWindow_Button_On(ActionEvent event) {
        // Get the current stage and close it
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void bestaetigenMahnungSchreibenToDestroyWindow_Button_On(ActionEvent event) {
//        // Open a FileChooser to select the file
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select File for Mahnung");
//        File selectedFile = fileChooser.showOpenDialog(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));

        String userHome = System.getProperty("user.home");
        String downloadsDir = userHome + File.separator + "Downloads";
        String timeStamp = new SimpleDateFormat("yyyyMMddmmss").format(new Date());
        String fileName = "mahnung_" + timeStamp + ".txt";
        File file = new File (downloadsDir, fileName);

        if (mahnungTextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Keine Mahnung geschrieben");
            alert.setContentText("Bitte schreiben Sie die Mahnung ein.");
            alert.showAndWait();
        } else {
            // File was selected, proceed with the method call
            iKontoManagerSteuerung = new IKontoManagerSteuerungImpl();
            boolean mahnungErstellt = iKontoManagerSteuerung.erstelleMahnung(file, mahnungTextField.getText());
            if (mahnungErstellt) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Erfolgreich");
                alert.setHeaderText("Mahnung erfolgreich erstellt");
                alert.setContentText("Mahnung ist im Downloads erfolgreich gespeichert.");
                alert.showAndWait();
                // Get the current stage and close it
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Fehler beim Erstellen der Mahnung");
                alert.setContentText("Es ist ein Fehler beim Erstellen der Mahnung aufgetreten.");
                alert.showAndWait();
            }
        }

    }


    public void setKontoId(int kontoId){
        this.kontoId = kontoId;

    }
}