package de.thkoeln.swp.bks.managergui.control;

import de.thkoeln.swp.bks.managersteuerung.impl.IAntragManagerSteuerungImpl;
import de.thkoeln.swp.bks.steuerungapi.grenz.AntragGrenz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class antragHauptfensterController implements Initializable {
    IAntragManagerSteuerungImpl iAntragManagerSteuerung;

    @FXML
    private TableView<AntragGrenz> kundeAntraegeTableView;

    @FXML
    private TableView<AntragGrenz> meineAntraegeTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Kunde Antraege Tab
        TableColumn<AntragGrenz, String> idCol = new TableColumn<>("ID");
        TableColumn<AntragGrenz, String> typCol = new TableColumn<>("Typ");
        TableColumn<AntragGrenz, String> datenCol = new TableColumn<>("Daten");
        TableColumn<AntragGrenz, String> kommentareCol = new TableColumn<>("Kommentare");
        TableColumn<AntragGrenz, String> statusCol = new TableColumn<>("Status");

        // MeineAntraege Tab
        TableColumn<AntragGrenz, String> idCol2 = new TableColumn<>("ID");
        TableColumn<AntragGrenz, String> typCol2 = new TableColumn<>("Typ");
        TableColumn<AntragGrenz, String> datenCol2 = new TableColumn<>("Daten");
        TableColumn<AntragGrenz, String> kommentareCol2 = new TableColumn<>("Kommentare");
        TableColumn<AntragGrenz, String> statusCol2 = new TableColumn<>("Status");

        // kunde Antraege
        idCol.setCellValueFactory(new PropertyValueFactory<>("atid"));
        typCol.setCellValueFactory(new PropertyValueFactory<>("typ"));
        datenCol.setCellValueFactory(new PropertyValueFactory<>("daten"));
        kommentareCol.setCellValueFactory(new PropertyValueFactory<>("kommentare"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // meine Antraege:
        idCol2.setCellValueFactory(new PropertyValueFactory<>("atid"));
        typCol2.setCellValueFactory(new PropertyValueFactory<>("typ"));
        datenCol2.setCellValueFactory(new PropertyValueFactory<>("daten"));
        kommentareCol2.setCellValueFactory(new PropertyValueFactory<>("kommentare"));
        statusCol2.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Kunde Antraege
        iAntragManagerSteuerung = new IAntragManagerSteuerungImpl();
        List<AntragGrenz> kundeAntraege1 = iAntragManagerSteuerung.getAntragListKsKo();
        List<AntragGrenz> kundeAntraege2 = iAntragManagerSteuerung.getAntragListKl();

        List<AntragGrenz> combinedAntraege = new ArrayList<>();
        if (kundeAntraege1 != null) combinedAntraege.addAll(kundeAntraege1);
        if (kundeAntraege2 != null) combinedAntraege.addAll(kundeAntraege2);

        if (!combinedAntraege.isEmpty()) {
            ObservableList<AntragGrenz> data1 = FXCollections.observableArrayList(combinedAntraege);
            kundeAntraegeTableView.getColumns().addAll(idCol, typCol, datenCol, kommentareCol, statusCol);
            kundeAntraegeTableView.setItems(data1);
        }

        //Meine Antraege
        iAntragManagerSteuerung = new IAntragManagerSteuerungImpl();
        List<AntragGrenz> antraege = iAntragManagerSteuerung.getEigeneAntraege();
        if (antraege != null && !antraege.isEmpty()) {
            ObservableList<AntragGrenz> data2 = FXCollections.observableArrayList(antraege);
            meineAntraegeTableView.getColumns().addAll(idCol2, typCol2, datenCol2, kommentareCol2, statusCol2);
            meineAntraegeTableView.setItems(data2);
            meineAntraegeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
    }

    @FXML
    private void zurueckAntragHauptfensterToHauptfenster_Button_On(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hauptfenster.fxml"));

            Scene scene = new Scene(loader.load());


            // Get the current stage and set the new scene
            Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SWP - BKS - Manager");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void antragStellenAntragHauptfensterToAntragStellen_Button_On(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/antragStellen.fxml"));

            Scene scene = new Scene(loader.load());

            // Create a new stage for the new window
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Antrag stellen");
            newStage.initModality(Modality.APPLICATION_MODAL); // Makes the new window modal
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void antragStornierenAntragHauptfensterToAntragStornieren_Button_On(ActionEvent event) {

        ObservableList<AntragGrenz> selectedItems = meineAntraegeTableView.getSelectionModel().getSelectedItems();
        if (selectedItems != null && !selectedItems.isEmpty()) {
            for (AntragGrenz antrag : selectedItems) {
                boolean success = iAntragManagerSteuerung.setAntragStorniert(antrag.getAtid());
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Erfolgreich");
                    alert.setHeaderText("Antrag erfolgreich storniert");
                    alert.setContentText("Antrag ist erfolgreich storniert. Bitte aktualisieren Sie die Seite, um die Aenderungen zu sehen.");
                    alert.showAndWait();

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler");
                    alert.setHeaderText("Falscher Antragsstatus");
                    alert.setContentText("Bitte nur Antraege mit Status 'n' auswaehlen.");
                    alert.showAndWait();

                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Keine Auswahl");
            alert.setContentText("Bitte wählen Sie einen Antrag aus.");
            alert.showAndWait();

        }
    }

    @FXML
    private void kontoBearbeitenAntragHauptfensterToKontolimitAntragBearbeiten_Button_On(ActionEvent event) {
        AntragGrenz selectedItem = kundeAntraegeTableView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            if (selectedItem.getTyp().equals("kl")) {
                    try {
                        String daten = selectedItem.getDaten();
                        int antragID = selectedItem.getAtid();
                        String status = selectedItem.getStatus();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kontolimitAntragBearbeiten.fxml"));

                        Scene scene = new Scene(loader.load());

                        kontolimitAntragBearbeitenSpinnerController controller = loader.getController();
                        controller.setDaten(daten);
                        controller.setAntragID(antragID);
                        controller.setStatus(status);

                        // Create a new stage for the new window
                        Stage newStage = new Stage();
                        newStage.setScene(scene);
                        newStage.setTitle("Kontolimit bearbeiten");
                        newStage.initModality(Modality.APPLICATION_MODAL); // Makes the new window modal
                        newStage.setResizable(false);
                        newStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Falscher Antragstyp");
                alert.setContentText("Bitte nur Antraege vom Typ 'Kl' auswaehlen.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Keine Auswahl");
            alert.setContentText("Bitte einen Antrag auswaehlen.");
            alert.showAndWait();
        }
    }

    @FXML
    private void antragGenehmigenAntragHauptfensterToMultipleWindows_Button_On(ActionEvent event) {
        AntragGrenz selectedItem = kundeAntraegeTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String typValue = selectedItem.getTyp();
            String statusValue = selectedItem.getStatus();
            int antragId = selectedItem.getAtid();

            try {
                FXMLLoader loader;
                Scene scene;
                Stage newStage;

                if ("n".equals(statusValue)){
                    if ("ko".equals(typValue)) {
                        iAntragManagerSteuerung = new IAntragManagerSteuerungImpl();
                        iAntragManagerSteuerung.setAntragGenehmigt(selectedItem);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Erfolgreich");
                        alert.setHeaderText("KO-Antrag erfolgreich genehmigt");
                        alert.setContentText("Der ausgewaehlte KO-Antrag ist erfolgreich genehmigt.");
                        alert.showAndWait();
                    } else if ("ks".equals(typValue)) {
                        // Open bearbeitungsadminGenehmigen.fxml
                        loader = new FXMLLoader(getClass().getResource("/bearbeitungsadminGenehmigen.fxml"));
                        scene = new Scene(loader.load());
                        bearbeitungsAdminGenehmigenController controller = loader.getController();
                        controller.setAntragID(antragId);

                        newStage = new Stage();
                        newStage.setScene(scene);
                        newStage.setTitle("Bearbeitungsadmin");
                        newStage.initModality(Modality.APPLICATION_MODAL);
                        newStage.setResizable(false);
                        newStage.show();

                    } else if ("kl".equals(typValue)) {
                        // Show an alert if the type is not recognized
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Fehler");
                        alert.setHeaderText("Falscher Antragstyp");
                        alert.setContentText("Bitte waehlen Sie nur Antraege von Typen 'KO' und 'KS'.");
                        alert.showAndWait();
                    } else {
                        // Show an alert if the type is not recognized
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Fehler");
                        alert.setHeaderText("Falscher Antragstyp");
                        alert.setContentText("Bitte waehlen Sie nur Antraege von Typen 'KO' und 'KS'.");
                        alert.showAndWait();
                    }
                } else {
                    // Show an alert if the type is not recognized
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler");
                    alert.setHeaderText("Falscher Antragsstatus");
                    alert.setContentText("Bitte nur Antraege mit Status 'n' auswaehlen.");
                    alert.showAndWait();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Show an alert if no item is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Keine Auswahl");
            alert.setContentText("Bitte wählen Sie einen Antrag aus.");
            alert.showAndWait();
        }
    }


    @FXML
    private void antragAblehnenAntragHauptfenster_Button_On(ActionEvent event) {
        AntragGrenz selectedItem = kundeAntraegeTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String typValue = selectedItem.getTyp();
            if ("ks".equals(typValue) || "ko".equals(typValue) || "kl".equals(typValue)) {
                int antragId = selectedItem.getAtid();
                iAntragManagerSteuerung = new IAntragManagerSteuerungImpl();
                boolean success = iAntragManagerSteuerung.setAntragAbgelehnt(antragId);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Erfolgreich");
                    alert.setHeaderText("Antrag erfolgreich abgelehnt");
                    alert.setContentText("Der ausgewaehlte Antrag wurde erfolgreich abgelehnt.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler");
                    alert.setHeaderText("Falscher Antragsstatus");
                    alert.setContentText("Bitte waehlen Sie nur Antraege mit Status 'n'.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Falscher Antragstyp");
                alert.setContentText("Der ausgewaehlte Antrag hat einen unbekannten Typ.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Keine Auswahl");
            alert.setContentText("Bitte waehlen Sie einen Antrag.");
            alert.showAndWait();
        }
    }
    @FXML
    private void refreshMeineAntraege_Button_On(ActionEvent event) {
        IAntragManagerSteuerungImpl iAntragManagerSteuerung1 = new IAntragManagerSteuerungImpl();
        List<AntragGrenz> antraege = iAntragManagerSteuerung1.getEigeneAntraege();
        ObservableList<AntragGrenz> data2 = FXCollections.observableArrayList(antraege);
        meineAntraegeTableView.setItems(data2);
    }

    @FXML
    private void refreshKundeAntraege_Button_On(ActionEvent event) {
        IAntragManagerSteuerungImpl iAntragManagerSteuerung2 = new IAntragManagerSteuerungImpl();
        List<AntragGrenz> kundeAntraege1 = iAntragManagerSteuerung2.getAntragListKsKo();
        List<AntragGrenz> kundeAntraege2 = iAntragManagerSteuerung2.getAntragListKl();

        List<AntragGrenz> combinedAntraege = new ArrayList<>();
        combinedAntraege.addAll(kundeAntraege1);
        combinedAntraege.addAll(kundeAntraege2);

        ObservableList<AntragGrenz> data1 = FXCollections.observableArrayList(combinedAntraege);
        kundeAntraegeTableView.setItems(data1);

    }

}
