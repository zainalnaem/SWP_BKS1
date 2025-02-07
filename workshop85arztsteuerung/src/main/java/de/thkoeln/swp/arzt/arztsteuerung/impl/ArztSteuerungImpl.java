package de.thkoeln.swp.arzt.arztsteuerung.impl;

import de.thkoeln.swp.arzt.arztdbmodel.entities.Arzt;
import de.thkoeln.swp.arzt.arztdbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.arzt.arztdbmodel.services.IDatabase;
import de.thkoeln.swp.arzt.datenhaltungapi.ICRUDArzt;
import de.thkoeln.swp.arzt.grenzklassen.ArztGrenz;
import de.thkoeln.swp.arzt.steuerungapi.IArztSteuerung;

import javax.persistence.EntityManager;
import java.lang.module.FindException;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArztSteuerungImpl implements IArztSteuerung {

    private static final Logger LOGGER = Logger.getLogger(ArztSteuerungImpl.class.getName());

    private final EntityManager em;
    private ICRUDArzt icrudArzt;

    public ArztSteuerungImpl() {
        LOGGER.log(Level.FINE, "Konstruktor IArztSteuerungImpl");

        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();

        boolean loadError = false;
        Iterator<ICRUDArzt> iterator = ServiceLoader.load(ICRUDArzt.class).iterator();
        if (iterator.hasNext()) {
            icrudArzt = iterator.next();
            icrudArzt.setEntityManager(em);
        } else {
            LOGGER.log(Level.SEVERE, "ICRUDArzt-Implementierung wurde nicht gefunden !!!");
            loadError = true;
        }

        if (loadError) {
            LOGGER.log(Level.SEVERE, "IArztSteuerungImpl: mindestens eine Datenhaltung-Implementierung konnte nicht gefunden werden");
            throw new FindException("Es konnte keine ICRUDArzt-Implementierung gefunden werden");
        }
    }

    @Override
    public boolean insertArzt(ArztGrenz arztGrenz) {

        Arzt arzt = new Arzt();

        arzt.setName(arztGrenz.getName());
        arzt.setWohnort(arztGrenz.getWohnort());

        em.getTransaction().begin();

        boolean erfolgreich = icrudArzt.insertArzt(arzt);

        if(erfolgreich)
            em.getTransaction().commit();
        else
            em.getTransaction().rollback();

        return erfolgreich;
    }
}