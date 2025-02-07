module de.thkoeln.swp.bks.managergui {

    requires de.thkoeln.swp.bks.managersteuerung;
    requires de.thkoeln.swp.bks.steuerungapi;
    
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    exports de.thkoeln.swp.bks.managergui.application;

    opens de.thkoeln.swp.bks.managergui.control to javafx.fxml;
}
