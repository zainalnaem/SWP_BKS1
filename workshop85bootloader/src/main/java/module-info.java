module de.thkoeln.swp.arzt.bootloader {

    requires javafx.fxml;
    requires javafx.controls;
    
    requires de.thkoeln.swp.arzt.componentcontroller;

    requires de.thkoeln.swp.arzt.arztgui;
    requires de.thkoeln.swp.arzt.arztsteuerung;
    
    exports de.thkoeln.swp.arzt.bootloader;
    
    opens de.thkoeln.swp.arzt.bootloader to javafx.fxml;
}
