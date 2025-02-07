module de.thkoeln.swp.arzt.arztgui {

    requires de.thkoeln.swp.arzt.arztsteuerung;
    requires de.thkoeln.swp.arzt.steuerungapi;

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    exports de.thkoeln.swp.arzt.arztgui.application;

    opens de.thkoeln.swp.arzt.arztgui.control to javafx.fxml;
}
