package de.thkoeln.swp.arzt.arztsteuerung.impl;

import de.thkoeln.swp.arzt.componentcontroller.services.CompType;
import de.thkoeln.swp.arzt.componentcontroller.services.IActivateComponent;
import java.util.logging.Level;
import java.util.logging.Logger;
public class IActivateComponentImpl implements IActivateComponent {
    private static final Logger LOGGER = Logger.getLogger (IActivateComponentImpl.class.getName());
    private boolean isActivated;
    public IActivateComponentImpl() {
         isActivated = false;
         }
    @Override
    public CompType getCompType() {
        return CompType.ARZT;

    }

    @Override
    public boolean activateComponent(int userId) {
        if (userId == 29 && !isActivated) {
             isActivated = true;
             LOGGER.log(Level.FINE, "Eingeloggt");
             return true;
             }

         return false;
    }

    @Override
    public boolean deactivateComponent() {
        if (isActivated) {
             isActivated = false;

             LOGGER.log(Level.FINE, "Ausgeloggt");
             return true;
             }
         return false;
         }

    @Override
    public boolean isActivated() {
        return isActivated;
    }
}
