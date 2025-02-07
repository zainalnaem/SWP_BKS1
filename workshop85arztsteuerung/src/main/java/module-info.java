module de.thkoeln.swp.arzt.arztsteuerung {

    requires de.thkoeln.swp.arzt.componentcontroller;
    requires de.thkoeln.swp.arzt.datenhaltungapi;
    requires de.thkoeln.swp.arzt.arztdbmodel;
    requires de.thkoeln.swp.arzt.steuerungapi;

    requires java.logging;

    exports de.thkoeln.swp.arzt.arztsteuerung.impl;

    uses de.thkoeln.swp.arzt.datenhaltungapi.ICRUDArzt;
}
