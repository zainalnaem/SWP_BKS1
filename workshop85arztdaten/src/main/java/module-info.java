module de.thkoeln.swp.arzt.arztdaten {

    requires de.thkoeln.swp.arzt.datenhaltungapi;
    requires de.thkoeln.swp.arzt.arztdbmodel;

    requires java.logging;

    provides de.thkoeln.swp.arzt.datenhaltungapi.ICRUDArzt with de.thkoeln.swp.arzt.arztdaten.ICRUDArztImpl;
}
