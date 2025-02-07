package de.thkoeln.swp.bks.managersteuerung.impl;

import de.thkoeln.swp.bks.bksdbmodel.entities.Konto;
import de.thkoeln.swp.bks.bksdbmodel.entities.Kunde;
import de.thkoeln.swp.bks.bksdbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.bks.bksdbmodel.services.IDatabase;
import de.thkoeln.swp.bks.datenhaltungapi.ISonderKontoService;

import de.thkoeln.swp.bks.steuerungapi.grenz.KontoGrenz;
import de.thkoeln.swp.bks.steuerungapi.grenz.KundeGrenz;
import de.thkoeln.swp.bks.steuerungapi.manager.IKontoManagerSteuerung;

import javax.persistence.EntityManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class IKontoManagerSteuerungImpl implements IKontoManagerSteuerung {

    /**
     * Der EntityManager "em", zur Verwaltung des Datenaustauschs zwischen dem
     * Programm und der Datenbank
     */
    private EntityManager em;

    /**
     * Das Datenschicht-Interface "ISonderKontoService"
     */
    private ISonderKontoService isonderkontoService;

    /**
     * Konstruktor von IKontoManagerSteuerungImpl.
     * Im Konstruktor werden, mit dem ServiceLoader,
     * alle benötigten Implementierungen, der Datenschicht-
     * Interfaces, geholt.
     */
    public IKontoManagerSteuerungImpl() {

        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();

        boolean loadError = false;
        Iterator<ISonderKontoService> iterator = ServiceLoader.load(ISonderKontoService
                .class).iterator();

        if (iterator.hasNext()) {
            isonderkontoService = iterator.next();
            isonderkontoService.setEntityManager(em);
        } else {
            loadError = true;
        }
        if (loadError) {
            throw new FindException("Es konnte keine ISonderKontoService-Implementierung gefunden werden");
        }
    }


    /**
     * @return Liefert eine Liste aller überzogenen KontoGrenz-Objekte.
     * Mithilfe der Methode getUeberzogeneKontos, aus dem Datenschicht-Interface "ISonderKontoService"
     * werden alle überzogenen Konten, aus der Datenbank, zurückgegeben.
     * @see Konto Die Entitätsklasse "Konto" der Datenbank
     * @see KontoGrenz Die Grenzklasse "KontoGrenz" für die GUI, enthält die gleichen Attribute
     * mit den gleichen Werten
     */
    @Override
    public List<KontoGrenz> getAlleUeberzogenenKonten() {

        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        em.flush();
        em.clear();
        em.getTransaction().commit();

        List<Konto> kontoList = isonderkontoService.getUeberzogeneKontos();

        List<KontoGrenz> kontoGrenzList = new ArrayList<>();

        for (int i = 0; i < kontoList.size(); i++) {
            KontoGrenz kontoGrenz = new KontoGrenz();
            kontoGrenz.setKtoid(kontoList.get(i).getKtoid());
            kontoGrenz.setArt(kontoList.get(i).getArt());
            kontoGrenz.setErstellungsdatum(kontoList.get(i).getErstellungsdatum());
            kontoGrenz.setKontostand(kontoList.get(i).getKontostand());
            kontoGrenz.setDispo(kontoList.get(i).getDispo());
            kontoGrenz.setStatus(kontoList.get(i).getStatus());

            kontoGrenzList.add(kontoGrenz);
        }

        return kontoGrenzList;
    }

    /**
     * @param file Eine Textdatei und eine
     * @param s Zeichenkette, aus dem Textfeld der GUI, wird übergeben.
     * Der BufferedWriter erstellt eine neue Datei, falls die Datei nicht
     * existiert
     * @return Liefert einen Wahrheitswert, ob die Mahnung erfolgreich erstellt
     * wurde.
     */
    @Override
    public boolean erstelleMahnung(File file, String s) {

        BufferedWriter writer = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(s);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    /**
     * @param kontoId Eine ID eines KontoGrenz-Objekts, aus der GUI, wird der Methode übergeben.
     * @return Liefert das gesuchte KontoGrenz-Objekt zurück, falls das Konto-Objekt in der
     * Datenbank existieren sollte. Mithilfe der Methode getKontoByID, aus dem Datenschicht-Interface
     * "ISonderKontoService".
     * @see Konto Die Entitätsklasse "Konto" der Datenbank
     * @see KontoGrenz Die Grenzklasse "KontoGrenz" für die GUI, enthält die gleichen Attribute
     * mit den gleichen Werten
     */
    @Override
    public KontoGrenz getKontoById(int kontoId) {

        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        em.flush();
        em.clear();
        em.getTransaction().commit();

        Konto kontoByID = isonderkontoService.getKontoByID(kontoId);

        KontoGrenz kontoGrenzByID = new KontoGrenz();
        KundeGrenz inhaber = new KundeGrenz();

        if ((kontoByID != null) && (kontoByID.getKunde() != null)) {
            kontoGrenzByID.setKtoid(kontoByID.getKtoid());
            kontoGrenzByID.setArt(kontoByID.getArt());
            kontoGrenzByID.setErstellungsdatum(kontoByID.getErstellungsdatum());
            kontoGrenzByID.setKontostand(kontoByID.getKontostand());
            kontoGrenzByID.setStatus(kontoByID.getStatus());
            kontoGrenzByID.setDispo(kontoByID.getDispo());

            inhaber.setKid(kontoByID.getKunde().getKid());
            kontoGrenzByID.setKunde(inhaber);

            return kontoGrenzByID;
        }
        return null;
    }

    /**
     * @param kontoId Eine ID eines KontoGrenz-Objekts und ein
     * @param dispo Dispo wird übergeben.
     * Mithilfe der Methode neuesDispoSetzen, aus dem Datenschicht-Interface
     * "ISonderKontoService", wird der Dispo in der Datenbank gesetzt, falls
     * dies erfolgreich sein sollte.
     * @return Liefert einen Wahrheitswert, ob der Dispo erfolgreich gesetzt
     * wurde.
     */
    @Override
    public boolean setzeDispoLimit(int kontoId, double dispo) {

        em.getTransaction().begin();
        boolean dispoGesetzt = isonderkontoService.neuesDispoSetzen(kontoId, dispo);

        if(dispoGesetzt)
            em.getTransaction().commit();
        else
            em.getTransaction().rollback();
        return dispoGesetzt;
    }
}
