package de.thkoeln.swp.bks.sachbearbeiterdaten;

import de.thkoeln.swp.bks.bksdbmodel.entities.Antrag;
import de.thkoeln.swp.bks.bksdbmodel.entities.Sachbearbeiter;
import de.thkoeln.swp.bks.bksdbmodel.exceptions.NoEntityManagerException;
import de.thkoeln.swp.bks.datenhaltungapi.IAntragSach;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IAntragSachImpl implements IAntragSach {
    private EntityManager em;

    @Override
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    /**
     * Deliver all Anträge from a Sachbearbeiter with the given sid
     * @return a list of antraege based of sid of a Sachbearbeiter
     * @param sid id of Sachbearbeiter
     * */
    @Override
    public List<Antrag> getAntragListe(int sid) {
        if(this.em == null){ throw new NoEntityManagerException();}
        Sachbearbeiter sachbearbeiter = em.find(Sachbearbeiter.class,sid);
        Query antragListe = em.createQuery("SELECT a FROM Antrag a WHERE a.sachbearbeiter =: Sachbearbeiter");
        antragListe.setParameter("Sachbearbeiter", sachbearbeiter);
        return antragListe.getResultList();
    }

    /**
     * sets the status of a Antrag on "bearbeitet" if the Sachbearbeiter with the sid worked on that spezific Antrag
     * @param antid is the id of the Antrag that is searched for.
     * @param sid is the id of the sachbearbeiter who worked on that id
     * @return return true if the status is set to "bearbeitet"
     */
    @Override
    public boolean setAntragBearbeitet(int antid, int sid) {
        if(this.em == null ){throw new NoEntityManagerException();}

        Antrag antrag = em.find(Antrag.class,antid);
        Sachbearbeiter sachbearbeiter = em.find(Sachbearbeiter.class,sid);
        if(antrag == null || sachbearbeiter == null){return false;}

        // Check if list is not empty and has exactly one Object of Antraege
        if(antrag.getTyp().equals("ko") && antrag.getSachbearbeiter().getSid().equals(sachbearbeiter.getSid()) && antrag.getStatus().equals("g")){ // Kontoeröffnungsantrag set Status b = "Bearbeitet"
            antrag.setStatus("b");
            em.merge(antrag);
            return true;
        }

        return false;
    }

    /**
     * Setting the status of a Sachbearbeiter worked on Antrag as "Stoniert"
     *
     * @param antid is the id of Antrag
     * @param sid is the id of the Sachbearbeiter
     * @return return if the status is set to "stoniert"
     */
    @Override
    public boolean antragStornieren(int antid, int sid) {
        if(this.em == null){throw new NoEntityManagerException();}
        // Searching the sachbearbeiter with his given sid and Antrag antid
        Sachbearbeiter sachbearbeiter = em.find(Sachbearbeiter.class,sid);
        Antrag antrag = em.find(Antrag.class,antid);

        if(antrag != null && sachbearbeiter != null ){
            if( !(antrag.getStatus().equals("n") || antrag.getStatus().equals("g")) ){
                return false;
            }

            if( !( antrag.getTyp().equals("ko") || antrag.getTyp().equals("kl") || antrag.getTyp().equals("ks")) ) {
                return false;
            }
            antrag.setStatus("s");
            em.merge(antrag);
            return true;
        }

        return false;
    }
    /**
     * Searching by a given sid all Antraege that are new and returing them as a list
     *
     * @param sid is the id of Sachbearbeiter
     * @return returns a list of all new Antraege given a sid of a sachbearbeiter
     */
    @Override
    public List<Antrag> getNeueAntraege(int sid) {
        if(this.em == null){ throw new NoEntityManagerException();}
        // Creating query for all instances of sachbearbeitet with the given sid and Antrag-Status is "n" = new.
        Sachbearbeiter sachbearbeiter = em.find(Sachbearbeiter.class,sid);
        List<Antrag> listAntraege = null;
        Query query = em.createQuery("SELECT a FROM Antrag a WHERE a.sachbearbeiter = :sachbearbeiter AND a.status = 'n' ");
        query.setParameter("sachbearbeiter",sachbearbeiter);
        listAntraege = query.getResultList();

        //Getting list out of query
        return listAntraege;
    }

    /**
     * Returns a whole list of the Database of the entity of Antraege
     * @return a list of all Antraege in the Database
     */
    @Override
    public List<Antrag> getAlleAntraege() {
        if( em == null){ throw new NoEntityManagerException(); }
        Query query = em.createQuery("SELECT a FROM Antrag a");
            return query.getResultList();
    }

    /**
     * Deletes an Antrag if the status is "a" (abgelehnt) and the
     * sachbearbeiter with the sid is the bearbeiter of the Antrag with
     * the specified antid.
     *
     * @param antid the id of the Antrag
     * @param sid   the id of the sachbearbeiter
     * @return if the deletion was successful or not
     */
    @Override
    public boolean deleteAntrag(int antid, int sid) {
        if (em == null) throw new NoEntityManagerException();

        // get the specified resources
        Antrag antrag = em.find(Antrag.class, antid);
        Sachbearbeiter sachbearbeiter = em.find(Sachbearbeiter.class, sid);

        // any of the resources does not exist -> deletion not possible
        if (antrag == null || sachbearbeiter == null) return false;
        // antrag has no sachbearbeiter, or sachbearbeiter is not the specified one -> deletion not desired
        if (antrag.getSachbearbeiter() == null) return false;
        if (antrag.getSachbearbeiter().getSid() != sid) return false;
        // antrag does not have status "a" (abgelehnt) -> deletion not desired
        if (!antrag.getStatus().equals("a")) return false;

        // deleting the antrag from the db
        em.remove(antrag);
        return true;
    }

    /**
     * Utility/Helper method that returns the antraege of the sachbearbeiter with sid that have the
     * specified status
     *
     * @param status the status to filter
     * @param sid    the id of the sachbearbeiter
     * @return a list of the matching antraege
     */
    private List<Antrag> getAntraegeWithStatus(String status, int sid) {
        if (em == null) throw new NoEntityManagerException();

        // get the specified sachbearbeiter
        Sachbearbeiter sachbearbeiter = em.find(Sachbearbeiter.class, sid);

        // generating a query to get all antraege of the sachbearbeiter with the specified status
        Query query = em.createQuery("""
                SELECT a FROM Antrag a WHERE a.sachbearbeiter = :sachbearbeiter AND a.status = :status
                """, Antrag.class);
        query.setParameter("status", status);
        query.setParameter("sachbearbeiter", sachbearbeiter);

        // executing query and returning the result
        return query.getResultList();
    }

    /**
     * Gets all antraege with status "a" (abgelehnt) of a sachbearbeiter with the specified sid.
     *
     * @param sid the id of the sachbearbeiter
     * @return a list of antraege
     */
    @Override
    public List<Antrag> getAbgelehnteAntraege(int sid) {
        return getAntraegeWithStatus("a", sid);
    }

    /**
     * Gets all antraege with status "b" (bearbeitet) of a sachbearbeiter with the specified sid.
     *
     * @param sid the id of the sachbearbeiter
     * @return a list of antraege
     */
    @Override
    public List<Antrag> getBearbeiteteAntraege(int sid) {
        return getAntraegeWithStatus("b", sid);
    }

    /**
     * Gets all antraege with status "s" (storniert) of a sachbearbeiter with the specified sid.
     *
     * @param sid the id of the sachbearbeiter
     * @return a list of antraege
     */
    @Override
    public List<Antrag> getStornierteAntraege(int sid) {
        return getAntraegeWithStatus("s", sid);
    }

    /**
     * Gets all antraege with status "g" (genehmigt) of a sachbearbeiter with the specified sid.
     *
     * @param sid the id of the sachbearbeiter
     * @return a list of antraege
     */
    @Override
    public List<Antrag> getGenehmigteAntraege(int sid) {
        return getAntraegeWithStatus("g", sid);
    }
}
