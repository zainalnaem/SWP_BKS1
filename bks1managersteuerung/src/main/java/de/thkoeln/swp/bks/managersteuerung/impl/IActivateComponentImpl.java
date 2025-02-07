package de.thkoeln.swp.bks.managersteuerung.impl;

import de.thkoeln.swp.bks.bksdbmodel.entities.Manager;
import de.thkoeln.swp.bks.bksdbmodel.exceptions.NoEntityManagerException;
import de.thkoeln.swp.bks.bksdbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.bks.bksdbmodel.services.IDatabase;
import de.thkoeln.swp.bks.componentcontroller.services.CompType;
import de.thkoeln.swp.bks.componentcontroller.services.IActivateComponent;
import de.thkoeln.swp.bks.datenhaltungapi.ICRUDManager;
import de.thkoeln.swp.bks.datenhaltungapi.ISonderKontoService;

import javax.persistence.EntityManager;
import java.lang.module.FindException;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;

public class IActivateComponentImpl implements IActivateComponent {


    /**
     * Zeigt an, ob die IActivateComponent aktiv ist
     */
    private boolean isActivated;

    /**
     * Der EntityManager "em", zur Verwaltung des Datenaustauschs zwischen dem
     * Programm und der Datenbank
     */
    private EntityManager em;

    /**
     * Das Datenschicht-Interface "ICRUDManager"
     */
    private ICRUDManager icrudManager;

    /* Das Attribut "activeManagerId" speichert den Manager, welcher aktuell
     * in der GUI eingeloggt ist. Dieses Attribut ist statisch, da der Manager
     * sich unabhängig von einem "IActivateComponentImpl"-Objekt einloggt.
     */
    private static int activeManagerId;

    /**
     * Der Konstruktor für "IActivateComponentImpl".
     * Der EntityManager "em" wird initialisiert,
     * mit dem EntityManager aus der Datenbank "db".
     * Und "isActivated" wird auf "false", da die Komponente
     * noch nicht aktiviert wurde, sondern nur initialisiert.
     * Der ServiceLoader holt auch alle Implementierungen von
     * "ICRUDManager".
     */
    public IActivateComponentImpl() {
        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();
        isActivated = false;

        boolean loadError = false;
        Iterator<ICRUDManager> iterator = ServiceLoader.load(ICRUDManager.class).iterator();

        if (iterator.hasNext()) {
            icrudManager = iterator.next();
            icrudManager.setEntityManager(em);
        } else {
            loadError = true;
        }
        if (loadError) {
            throw new FindException("Es konnte keine ICRUDManager-Implementierung gefunden werden");
        }
    }

    /**
     * @return Liefert den Komponententyp zurück; in unserem Fall "Manager"
     * @see CompType Eine Enumeration, die alle Komponententypen enthält
     */
    @Override
    public CompType getCompType() {
        return CompType.MANAGER;
    }

    /**
     * @param userId Die ManagerID, mit dem sich der Manager, im Bootloader, einloggt.
     * @return Liefert einen Wahrheitswert zurück, ob die Komponente aktiviert wurde oder
     * nicht. Die Komponente wird nur aktiviert, wenn ein Manager mit der übergebenen ID
     * existiert. Wenn der Manager existiert, dann wird die userID an "activeManagerId"
     * übergeben.
     */
    @Override
    public boolean activateComponent(int userId) {

        if (em == null)
            throw new NoEntityManagerException();

        if (icrudManager.getManagerByID(userId) != null && !isActivated) {
            isActivated = true;
            activeManagerId = userId;
            return true;
        }
        return false;
    }

    /**
     * @return Liefert einen Wahrheitswert zurück, ob die Komponente deaktiviert wurde
     * oder nicht. Gibt "false" zurück, wenn die Komponente bereits deaktiviert ist
     */
    @Override
    public boolean deactivateComponent() {
        if (isActivated) {
            isActivated = false;
            return true;
        }
        return false;
    }

    /**
     * @return Gibt ein Wahrheitswert zurück, die zeigt, ob die Komponente aktuell aktiv
     * ist, oder nicht
     */
    @Override
    public boolean isActivated() {
        return isActivated;
    }

    /**
     * @return Liefert den aktuellen Manager, welcher in der GUI eingeloggt ist.
     * Diese Methode ist statisch, da sie unabhängig von der Instanz, den Manager
     * liefern soll, welcher
     */
    public static int getActiveManagerId() {
        return activeManagerId;
    }
}
