package de.thkoeln.swp.bks.managersteuerung.impl;

import de.thkoeln.swp.bks.bksdbmodel.entities.Admin;
import de.thkoeln.swp.bks.bksdbmodel.entities.Antrag;
import de.thkoeln.swp.bks.bksdbmodel.entities.Manager;

import de.thkoeln.swp.bks.bksdbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.bks.bksdbmodel.services.IDatabase;

import de.thkoeln.swp.bks.datenhaltungapi.IAntragManager;
import de.thkoeln.swp.bks.datenhaltungapi.IAntragStellenAdmin;
import de.thkoeln.swp.bks.datenhaltungapi.ICRUDManager;
import de.thkoeln.swp.bks.datenhaltungapi.ISonderKontoService;

import de.thkoeln.swp.bks.steuerungapi.grenz.AdminGrenz;
import de.thkoeln.swp.bks.steuerungapi.grenz.AntragGrenz;
import de.thkoeln.swp.bks.steuerungapi.manager.IAntragManagerSteuerung;

import javax.persistence.EntityManager;
import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class IAntragManagerSteuerungImpl implements IAntragManagerSteuerung {

    /**
     * Der EntityManager "em", zur Verwaltung des Datenaustauschs zwischen dem
     * Programm und der Datenbank
     */
    private EntityManager em;

    /**
     * Das Datenschicht-Interface "ICRUDManager"
     */
    private ICRUDManager icrudManager;

    /**
     * Das Datenschicht-Interface "IAntragStellenAdmin"
     */
    private IAntragStellenAdmin iantragstellenAdmin;

    /**
     * Das Datenschicht-Interface "IAntragManager"
     */
    private IAntragManager iantragManager;

    /**
     * Das Datenschicht-Interface "ISonderKontoService"
     */
    private ISonderKontoService isonderkontoService;

    /**
     * Konstruktor von IAntragManagerSteuerungImpl.
     * Im Konstruktor werden, mit dem ServiceLoader,
     * alle benötigten Implementierungen, der Datenschicht-
     * Interfaces, geholt.
     */
    public IAntragManagerSteuerungImpl() {

        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();

        boolean loadErrorManager = false;
        Iterator<ICRUDManager> iteratorManager = ServiceLoader.load(ICRUDManager.class).iterator();

        if (iteratorManager.hasNext()) {
            icrudManager = iteratorManager.next();
            icrudManager.setEntityManager(em);
        } else {
            loadErrorManager = true;
        }
        if (loadErrorManager) {
            throw new FindException("Es konnte keine ICRUDManager-Implementierung gefunden werden");
        }

        boolean loadErrorStellenAdmin = false;
        Iterator<IAntragStellenAdmin> iteratorStellenAdmin = ServiceLoader.load(IAntragStellenAdmin
                .class).iterator();

        if (iteratorStellenAdmin.hasNext()) {
            iantragstellenAdmin = iteratorStellenAdmin.next();
            iantragstellenAdmin.setEntityManager(em);
        } else {
            loadErrorStellenAdmin = true;
        }
        if (loadErrorStellenAdmin) {
            throw new FindException("Es konnte keine IAntragStellenAdmin-Implementierung gefunden werden");
        }

        boolean loadErrorAntragManager = false;
        Iterator<IAntragManager> iteratorAntragManager = ServiceLoader.load(IAntragManager
                .class).iterator();

        if (iteratorAntragManager.hasNext()) {
            iantragManager = iteratorAntragManager.next();
            iantragManager.setEntityManager(em);
        } else {
            loadErrorAntragManager = true;
        }
        if (loadErrorAntragManager) {
            throw new FindException("Es konnte keine IAntragManager-Implementierung gefunden werden");
        }

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
     * Mithilfe der Methode getListeEigenerAntraege, aus dem Datenschicht-Interface
     * "IAntragManager", wird die Liste der Anträge zurückgegeben aus der Datenbank.
     * Dafür wird die ManagerID benötigt, welcher sich in der IActivateComponentImpl
     * befindet.
     * Dort ist nämlich die ID des eingeloggten Managers gespeichert.
     * @return Liefert eine Liste mit allen AntragGrenz-Objekten, die der Manager
     * @see Antrag Die Entitätsklasse "Antrag" der Datenbank
     * @see AntragGrenz Die Grenzklasse "AntragGrenz" für die GUI, enthält die gleichen Attribute
     * mit den gleichen Werten
     */
    @Override
    public List<AntragGrenz> getEigeneAntraege() {

        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        em.flush();
        em.clear();
        em.getTransaction().commit();

        int activeManagerID = IActivateComponentImpl.getActiveManagerId();

        List<Antrag> eigeneAntraege = iantragManager.getListeEigenerAntraege(activeManagerID);

        List<AntragGrenz> eigeneAntraegeGrenz = new ArrayList<>();

        for (int i = 0; i < eigeneAntraege.size(); i++) {
            AntragGrenz antragGrenz = new AntragGrenz();
            antragGrenz.setAtid(eigeneAntraege.get(i).getAtid());
            antragGrenz.setTyp(eigeneAntraege.get(i).getTyp());
            antragGrenz.setDaten(eigeneAntraege.get(i).getDaten());
            antragGrenz.setKommentare(eigeneAntraege.get(i).getKommentare());
            antragGrenz.setStatus(eigeneAntraege.get(i).getStatus());

            eigeneAntraegeGrenz.add(antragGrenz);
        }

        return eigeneAntraegeGrenz;
    }

    /**
     * @param antragConfirm Ein AntragGrenz-Objekt, aus der GUI, wird der Methode übergeben,
     * um den Antragsstatus, in der Datenbank, auf "genehmigt" zu setzen, falls dies
     * erfolgreich sein sollte.
     * @return Liefert einen Wahrheitswert zurück, ob der Antrag abgelehnt wurde oder nicht
     * @see Antrag Die Entitätsklasse "Antrag" der Datenbank
     * @see AntragGrenz Die Grenzklasse "AntragGrenz" für die GUI, enthält die gleichen Attribute
     * mit den gleichen Werten
     */
    @Override
    public boolean setAntragGenehmigt(AntragGrenz antragConfirm) {

        em.getTransaction().begin();

        boolean genehmigt;
        if (antragConfirm.getTyp().equals("ks") && antragConfirm.getAdmin() != null) {

            genehmigt = iantragManager.setKSAntragGenehmigt(antragConfirm.getAtid(),
                    antragConfirm.getAdmin().getAid());
        } else {
            genehmigt = iantragManager.setAntragGenehmigt(antragConfirm.getAtid());
        }

        if(genehmigt)
            em.getTransaction().commit();
        else
            em.getTransaction().rollback();
        return genehmigt;
    }

    /**
     * @param antragId Eine ID eines AntragGrenz-Objekts, aus der GUI, wird der Methode übergeben,
     * um den Antragsstatus, in der Datenbank, auf "ablehnen" zu setzen, falls dies
     * erfolgreich sein sollte.
     * @return Liefert einen Wahrheitswert zurück, ob der Antrag abgelehnt wurde oder nicht
     * @see Antrag Die Entitätsklasse "Antrag" der Datenbank
     * @see AntragGrenz Die Grenzklasse "AntragGrenz" für die GUI, enthält die gleichen Attribute
     * mit den gleichen Werten
     */
    @Override
    public boolean setAntragAbgelehnt(int antragId) {

        em.getTransaction().begin();
        boolean abgelehnt = iantragManager.setAntragAbgelehnt(antragId);

        if(abgelehnt)
            em.getTransaction().commit();
        else
            em.getTransaction().rollback();
        return abgelehnt;
    }

    /**
     * @param antragId Eine ID eines AntragGrenz-Objekts, eine
     * @param kontoId KontoID und ein
     * @param dispo Dispo wird der Methode übergeben.
     * Mithilfe der Methode neuesDispoSetzen, aus dem Datenschicht-Interface
     * "ISonderKontoService", wird der Dispo in der Datenbank gesetzt, falls
     * dies erfolgreich sein sollte. Und Mithilfe der Methode setAntragBearbeitet,
     * aus dem Datenschicht-Interface "IAntragManager", wird der Status des Antrags
     * auf "bearbeitet" gesetzt, falls dies ebenfalls erfolgreich sein sollte.
     * @return Liefert einen Wahrheitswert, ob der Dispo erfolgreich gesetzt
     * wurde.
     */
    @Override
    public boolean updateKontolimit(int antragId, int kontoId, double dispo) {

        em.getTransaction().begin();

        boolean aktualisiert = isonderkontoService.neuesDispoSetzen(kontoId, dispo);
        boolean bearbeitet = iantragManager.setAntragBearbeitet(antragId);

        if (aktualisiert && bearbeitet) {
            em.getTransaction().commit();
        } else
            em.getTransaction().rollback();
        return (aktualisiert && bearbeitet);
    }

    /**
     * @param antragAdd Ein AntragGrenz-Objekt, der GUI, wird der Methode übergeben.
     * Mithilfe der Methode getManagerByID, aus dem Datenschicht-Interface
     * "ICRUDManager", wird der Manager aus der Datenbank geholt, um dem Antrag-Objekt
     * den zugehörigen Manager zuzuordnen. Und mithilfe der Methode antragStellen,
     * aus dem Datenschicht-Interface "IAntragStellenAdmin", wird der neue Antrag
     * der Datenbank hinzugefügt.
     * @return Liefert einen Wahrheitswert, ob der Antrag erfolgreich erstellt
     * werden konnte.
     * @see Antrag Die Entitätsklasse "Antrag" der Datenbank
     * @see AntragGrenz Die Grenzklasse "AntragGrenz" für die GUI, enthält die gleichen Attribute
     * mit den gleichen Werten
     */
    @Override
    public boolean addAntrag(AntragGrenz antragAdd) {
        int activeManagerID = IActivateComponentImpl.getActiveManagerId();

        Manager activeManager = icrudManager.getManagerByID(activeManagerID);

        Antrag antragStellen = new Antrag(antragAdd.getAtid(), antragAdd.getTyp(),
                antragAdd.getDaten(), antragAdd.getKommentare(), antragAdd.getStatus());

        antragStellen.setManager(activeManager);

        if (antragAdd.getAdmin() != null) {
            AdminGrenz adminGrenz = antragAdd.getAdmin();
            Admin admin = em.find(Admin.class, adminGrenz.getAid());
            if (admin != null) {
                antragStellen.setAdmin(admin);
            } else {
                throw new RuntimeException("Admin with ID " + adminGrenz.getAid() + " not found");
            }
        }

        em.getTransaction().begin();
        boolean gestellt = iantragstellenAdmin.antragStellen(antragStellen);

        if(gestellt)
            em.getTransaction().commit();
        else
            em.getTransaction().rollback();
        return gestellt;
    }

    /**
     * @return Liefert alle Admins aus der Datenbank zurück, mithilfe der Methode
     * getAdminListe(), aus dem Datenschicht-Interface "IAntragStellenAdmin"
     * @see Admin Die Entitätsklasse "Admin" der Datenbank
     * @see AdminGrenz Die Grenzklasse "AdminGrenz" für die GUI, enthält die gleichen Attribute
     * mit den gleichen Werten
     */
    @Override
    public List<AdminGrenz> getAlleAdmins() {

        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        em.flush();
        em.clear();
        em.getTransaction().commit();

        List<Admin> adminList = iantragstellenAdmin.getAdminListe();

        List<AdminGrenz> adminGrenzList = new ArrayList<>();

        for (int i = 0; i < adminList.size(); i++) {
            AdminGrenz adminGrenz = new AdminGrenz(0);
            adminGrenz.setAid(adminList.get(i).getAid());
            adminGrenz.setTitle(adminList.get(i).getTitel());
            adminGrenz.setName(adminList.get(i).getName());
            adminGrenz.setVorname(adminList.get(i).getVorname());
            adminGrenz.setGeschlecht(adminList.get(i).getGeschlecht());
            adminGrenz.setGeburtsdatum(adminList.get(i).getGeburtsdatum());
            adminGrenz.setAdresse(adminList.get(i).getAdresse());
            adminGrenz.setTelefonnummer(adminList.get(i).getTelefon());
            adminGrenz.setAbteilung(adminList.get(i).getAbteilung());

            adminGrenzList.add(adminGrenz);
        }

        return adminGrenzList;
    }

    /**
     * @param antragId Eine ID eines AntragGrenz-Objekts, der GUI, wird der Methode übergeben.
     * Mithilfe der Methode setAntragStorniert, aus dem Datenschicht-Interface
     * "IAntragManager", wird der Antrag des Managers, den er selbst erstellt hat, aus der
     * Datenbank gelöscht. Dies geschieht nur, wenn die Aktion erfolgreich war.
     * @return Liefert einen Wahrheitswert, ob der Antrag erfolgreich storniert
     * werden konnte.
     * @see Antrag Die Entitätsklasse "Antrag" der Datenbank
     * @see AntragGrenz Die Grenzklasse "AntragGrenz" für die GUI, enthält die gleichen Attribute
     * mit den gleichen Werten
     */
    @Override
    public boolean setAntragStorniert(int antragId) {

        int managerId = IActivateComponentImpl.getActiveManagerId();

        em.getTransaction().begin();
        boolean storniert = iantragManager.setAntragStorniert(antragId, managerId);

        if(storniert)
            em.getTransaction().commit();
        else
            em.getTransaction().rollback();
        return storniert;
    }

    /**
     * @return Liefert alle Anträge aus der Datenbank, vom Typ "Konto-Schließung (ks)" und "Konto-Öffnung (ko)"
     * Mithilfe der Methode getAntragListe(), aus dem Datenschicht-Interface "IAntragManager" werden erstmal
     * alle Anträge vom Typ "Konto-Schließung (ks)", "Konto-Öffnung (ko)" und "Konto-Limit (kl)"
     * Durch ein If-Statement, werden alle Konto-Limit-Anträge rausgefiltert.
     * @see Antrag Die Entitätsklasse "Antrag" der Datenbank
     * @see AntragGrenz Die Grenzklasse "AntragGrenz" für die GUI, enthält die gleichen Attribute
     * mit den gleichen Werten
     */
    @Override
    public List<AntragGrenz> getAntragListKsKo() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        em.flush();
        em.clear();
        em.getTransaction().commit();

        int managerId = IActivateComponentImpl.getActiveManagerId();

        List<Antrag> antragListKsKoKl = iantragManager.getAntragListe(managerId) ;
        List<AntragGrenz> antragGrenzListKsKo = new ArrayList<>();

        for (int i = 0; i < antragListKsKoKl.size(); i++) {

            if ((antragListKsKoKl.get(i).getTyp().equals("ks")) || (antragListKsKoKl.get(i).getTyp().equals("ko"))) {

                AntragGrenz antragGrenzKsKo = new AntragGrenz();
                antragGrenzKsKo.setAtid(antragListKsKoKl.get(i).getAtid());
                antragGrenzKsKo.setTyp(antragListKsKoKl.get(i).getTyp());
                antragGrenzKsKo.setDaten(antragListKsKoKl.get(i).getDaten());
                antragGrenzKsKo.setKommentare(antragListKsKoKl.get(i).getKommentare());
                antragGrenzKsKo.setStatus(antragListKsKoKl.get(i).getStatus());

                antragGrenzListKsKo.add(antragGrenzKsKo);
            }
        }
        return antragGrenzListKsKo;
    }


    /**
     * @return Liefert alle Anträge aus der Datenbank, vom Typ "Konto-Limit (kl)"
     * Mithilfe der Methode getAntragListe(), aus dem Datenschicht-Interface "IAntragManager" werden erstmal
     * alle Anträge vom Typ "Konto-Schließung (ks)", "Konto-Öffnung (ko)" und "Konto-Limit (kl)"
     * Durch ein If-Statement, werden alle Konto-Öffnungs-Anträge und Konto-Schließungs-Anträge rausgefiltert.
     * @see Antrag Die Entitätsklasse "Antrag" der Datenbank
     * @see AntragGrenz Die Grenzklasse "AntragGrenz" für die GUI, enthält die gleichen Attribute
     * mit den gleichen Werten
     */
    @Override
    public List<AntragGrenz> getAntragListKl() {

        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        em.flush();
        em.clear();
        em.getTransaction().commit();

        int managerId = IActivateComponentImpl.getActiveManagerId();

        List<Antrag> antragListKsKoKl = iantragManager.getAntragListe(managerId);

        List<AntragGrenz> antragGrenzListKl = new ArrayList<>();

        for (int i = 0; i < antragListKsKoKl.size(); i++) {

            if (antragListKsKoKl.get(i).getTyp().equals("kl")) {

                AntragGrenz antragGrenzKl = new AntragGrenz();
                antragGrenzKl.setAtid(antragListKsKoKl.get(i).getAtid());
                antragGrenzKl.setTyp(antragListKsKoKl.get(i).getTyp());
                antragGrenzKl.setDaten(antragListKsKoKl.get(i).getDaten());
                antragGrenzKl.setKommentare(antragListKsKoKl.get(i).getKommentare());
                antragGrenzKl.setStatus(antragListKsKoKl.get(i).getStatus());

                antragGrenzListKl.add(antragGrenzKl);
            }
        }
        return antragGrenzListKl;
    }
}
