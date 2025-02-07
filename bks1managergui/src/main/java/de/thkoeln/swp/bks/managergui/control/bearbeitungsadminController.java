package de.thkoeln.swp.bks.managergui.control;

import de.thkoeln.swp.bks.managersteuerung.impl.IAntragManagerSteuerungImpl;
import de.thkoeln.swp.bks.steuerungapi.grenz.AdminGrenz;
import de.thkoeln.swp.bks.steuerungapi.grenz.AntragGrenz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class bearbeitungsadminController implements Initializable {
    IAntragManagerSteuerungImpl iAntragManagerSteuerung;
    AntragGrenz antragGrenz;
    private String antragTyp,daten,kommentar;


    @FXML
    private TableView<AdminGrenz> bearbeitungsadminTableView;

    private Stage mainStage; // Reference to the main stage

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
    public void setAntragTyp(String antragTyp){
        this.antragTyp = antragTyp;
    }
    public void setDaten(String daten) {
        this.daten = daten;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TableColumn<AdminGrenz, String> idCol = new TableColumn<>("Admin ID");
        TableColumn<AdminGrenz, String> titleCol = new TableColumn<>("Title");
        TableColumn<AdminGrenz, String> nameCol = new TableColumn<>("Name");
        TableColumn<AdminGrenz, String> vornameCol = new TableColumn<>("Vorname");
        TableColumn<AdminGrenz, String> geschlechtCol = new TableColumn<>("Geschlecht");
        TableColumn<AdminGrenz, String> geburtsdatumCol = new TableColumn<>("Geburtsdatum");
        TableColumn<AdminGrenz, String> adresseCol = new TableColumn<>("Adresse");
        TableColumn<AdminGrenz, String> telefonnummerCol = new TableColumn<>("Telefonnummer");
        TableColumn<AdminGrenz, String> abteilungCol = new TableColumn<>("Abteilung");

        // Define cell value factories for each column
        idCol.setCellValueFactory(new PropertyValueFactory<>("aid"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        vornameCol.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        geschlechtCol.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
        geburtsdatumCol.setCellValueFactory(new PropertyValueFactory<>("geburtsdatum"));
        adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        telefonnummerCol.setCellValueFactory(new PropertyValueFactory<>("telefonnummer"));
        abteilungCol.setCellValueFactory(new PropertyValueFactory<>("abteilung"));



        iAntragManagerSteuerung = new IAntragManagerSteuerungImpl();
            ObservableList<AdminGrenz> data2 = FXCollections.observableArrayList(iAntragManagerSteuerung.getAlleAdmins());
            bearbeitungsadminTableView.getColumns().addAll(idCol, titleCol, nameCol, vornameCol, geschlechtCol, geburtsdatumCol, adresseCol, telefonnummerCol, abteilungCol);
            bearbeitungsadminTableView.setItems(data2);
    }

    @FXML
    private void bestaetigenBearbeitungsAdminCloseWindow_Button_On(ActionEvent event) {

        AdminGrenz selectedAdmin = bearbeitungsadminTableView.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {
            antragGrenz= new AntragGrenz();
            antragGrenz.setAdmin(selectedAdmin);
            antragGrenz.setTyp(antragTyp);
            antragGrenz.setDaten(daten);
            antragGrenz.setKommentare(kommentar);
            antragGrenz.setStatus("n");

            boolean antragAdded = iAntragManagerSteuerung.addAntrag(antragGrenz);



            if (antragAdded){

                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.close();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("erfolgreich");
                alert.setHeaderText("Antrag erstellt ");
                alert.setContentText("Der Antrag wurde erfolgreich gestellt ");
                alert.showAndWait();

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Fehler beim Hinzufügen des Antrags");
                alert.setContentText("Es ist ein Fehler beim Hinzufügen des Antrags aufgetreten.");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Keine Auswahl");
            alert.setContentText("Bitte wählen Sie einen Admin aus.");
            alert.showAndWait();
        }
    }

    @FXML
    private void abbrechenBearbeitungsAdminCloseWindow_Button_On(ActionEvent event) {
        // Get the current stage and close it
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}