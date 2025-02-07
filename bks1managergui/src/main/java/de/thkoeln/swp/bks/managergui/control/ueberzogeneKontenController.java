package de.thkoeln.swp.bks.managergui.control;

import de.thkoeln.swp.bks.managersteuerung.impl.IKontoManagerSteuerungImpl;
import de.thkoeln.swp.bks.steuerungapi.grenz.KontoGrenz;
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
import java.util.*;

public class ueberzogeneKontenController implements Initializable {
    IKontoManagerSteuerungImpl iKontoManagerSteuerung;

    @FXML
    private TableView<KontoGrenz> ueberzogeneKontenTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TableColumn<KontoGrenz, String> ktoIDCol = new TableColumn<>("Konto ID");
        TableColumn<KontoGrenz, String> artCol = new TableColumn<>("Art");
        TableColumn<KontoGrenz, String> erstellungsdatumCol = new TableColumn<>("Erstellungsdatum");
        TableColumn<KontoGrenz, String> kontoStandCol = new TableColumn<>("Kontostand");
        TableColumn<KontoGrenz, String>  dispoCol = new TableColumn<>("Dispo");
        TableColumn<KontoGrenz, String>  statusCol = new TableColumn<>("Status");


        ktoIDCol.setCellValueFactory(new PropertyValueFactory<>("ktoid"));
        artCol.setCellValueFactory(new PropertyValueFactory<>("art"));
        erstellungsdatumCol.setCellValueFactory(new PropertyValueFactory<>("erstellungsdatum"));
        kontoStandCol.setCellValueFactory(new PropertyValueFactory<>("kontostand"));
        dispoCol.setCellValueFactory(new PropertyValueFactory<>("dispo"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));




        iKontoManagerSteuerung = new IKontoManagerSteuerungImpl();
        List<KontoGrenz> ueberzogeneKonten = iKontoManagerSteuerung.getAlleUeberzogenenKonten();

        if (ueberzogeneKonten != null && !ueberzogeneKonten.isEmpty()) {
            ObservableList<KontoGrenz> data = FXCollections.observableArrayList(ueberzogeneKonten);
            ueberzogeneKontenTableView.getColumns().addAll(ktoIDCol, artCol, erstellungsdatumCol, kontoStandCol, dispoCol, statusCol);
            ueberzogeneKontenTableView.setItems(data);
        }

    }

    @FXML
    private void zurueckUeberzogeneKontenToHauptfenster_Button_On(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hauptfenster.fxml"));

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
    private void mahnungSchreibenUeberzogeneKontenToMahnungSchreiben_Button_On(ActionEvent event) {
        KontoGrenz selectedItem = ueberzogeneKontenTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
               int ktoId = selectedItem.getKtoid();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mahnungSchreiben.fxml"));

                Scene scene = new Scene(loader.load());

                managerController controller = loader.getController();
                controller.setKontoId(ktoId);
                // Create a new stage for the new window
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle("Mahnung schreiben");
                newStage.initModality(Modality.APPLICATION_MODAL); // Makes the new window modal
                newStage.setResizable(false);
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Keine Auswahl");
            alert.setContentText("Bitte waehlen Sie ein Konto.");
            alert.showAndWait();
        }
    }

    @FXML
    private void kontoSperrenOderEntsperrenUeberzogeneKontenToKontoSperrenOderEntsperren_Button_On(ActionEvent event) {
        KontoGrenz selectedItem = ueberzogeneKontenTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            try {
                int ktoId = selectedItem.getKtoid();
                double kontostand = selectedItem.getKontostand();
                double dispo = selectedItem.getDispo();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/kontoSperrenOderEntsperren.fxml"));

                Scene scene = new Scene(loader.load());

                kontoSperrenOderEntsperrenSpinnerController controller = loader.getController();
                controller.setKontoId(ktoId);
                controller.setKontostand(kontostand);
                controller.setDispo(dispo);

                // Create a new stage for the new window
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle("Konto sperren oder entsperren.fxml");
                newStage.initModality(Modality.APPLICATION_MODAL); // Makes the new window modal
                newStage.setResizable(false);
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Keine Auswahl");
            alert.setContentText("Bitte waehlen Sie ein Konto.");
            alert.showAndWait();
        }

    }

    @FXML
    private void refreshUeberzogeneKonten_Button_On(ActionEvent event) {
        List<KontoGrenz> ueberzogeneKonten = iKontoManagerSteuerung.getAlleUeberzogenenKonten();
        ObservableList<KontoGrenz> data = FXCollections.observableArrayList(ueberzogeneKonten);
        ueberzogeneKontenTableView.setItems(data);
    }
}