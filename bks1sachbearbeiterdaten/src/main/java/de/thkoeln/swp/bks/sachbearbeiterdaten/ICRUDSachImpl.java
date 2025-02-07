package de.thkoeln.swp.bks.sachbearbeiterdaten;

import de.thkoeln.swp.bks.bksdbmodel.entities.Sachbearbeiter;
import de.thkoeln.swp.bks.bksdbmodel.exceptions.NoEntityManagerException;
import de.thkoeln.swp.bks.datenhaltungapi.ICRUDSach;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class ICRUDSachImpl implements ICRUDSach {

    EntityManager em;


    /**
     * EntityManager setzten und verbinden der datenbank
     * @param em datenbanks Entitymanager
     */
    @Override
    public void setEntityManager(EntityManager em) {
    this.em=em;
    }


    /**
     * liefert den Sachbearbeiter mit der angegebenen Id
     *
     * @param i ist die Id des gesuchten Sachbearbeiter
     * @return wiedergabe des gefundenen Sachbearbeiter oder null
     */
    @Override
    public Sachbearbeiter getSachByID(int i) {
        if(em==null){
            throw new NoEntityManagerException();
        }
         Sachbearbeiter sachbearbeiter=em.find(Sachbearbeiter.class,i);
         return sachbearbeiter;
    }


    /**
     * liefert alle in der DB existierenden Sachbearbeiter
     *
     * @return wiedergabe der Lieste von der Datenbank
     */
    @Override
    public List<Sachbearbeiter> getSachListe() {
        if(em==null){
            throw new NoEntityManagerException();
        }

        TypedQuery<Sachbearbeiter> query= em.createQuery(
                "SELECT s FROM Sachbearbeiter s",Sachbearbeiter.class);

        return query.getResultList();
    }


    /**
     * fuegt einen neuen Sachbearbeiter zur DB hinzu
     *
     * @param sachbearbeiter der eingefuegt werden soll in die DB
     * @return erfolgreiches/fehlgeschlagenes einfuegen
     */
    @Override
    public boolean insertSach(Sachbearbeiter sachbearbeiter) {
        if(em==null){
            throw new NoEntityManagerException();
        }

        if (sachbearbeiter==null || sachbearbeiter.getSid()!=null){
            return false;
        }

        em.persist(sachbearbeiter);
        return true;
    }


    /**
     * modifiziert einen existierenden Sachbearbeiter.
     *
     * @param sachbearbeiter eigenschaften des objekts die umgeandert werden soll
     * @return erfolgreiches/fehlgeschlagenes umaendern
     */
    @Override
    public boolean editSach(Sachbearbeiter sachbearbeiter) {
        if(em==null){
            throw new NoEntityManagerException();
        }

        Sachbearbeiter sachbearbeiterGefunden=em.find(Sachbearbeiter.class,sachbearbeiter.getSid());

        if(sachbearbeiterGefunden==null){
            return false;
        }


        em.merge(sachbearbeiter);
        sachbearbeiterGefunden=em.find(Sachbearbeiter.class,sachbearbeiter.getSid());
        return sachbearbeiter.equals(sachbearbeiterGefunden);

    }


    /**
     * loescht einen existierenden Sachbearbeiter
     *
     * @param i dient fuer die ID des gesuchten obejts
     * @return erfolgreiches/fehlgeschlagenes loeschen
     */
    @Override
    public boolean deleteSach(int i) {
        if(em==null){
            throw new NoEntityManagerException();
        }
        Sachbearbeiter sachbearbeiter=em.find(Sachbearbeiter.class,i);

        if(sachbearbeiter==null){
            return false;
        }
        em.remove(sachbearbeiter);
        return true;
    }


    /**
     * liefert alle Sachbearbeiter, die der Abteilung A1 zugeordnet sind
     *
     * @return wiedergabe der lieste von der Abteilung A1
     */
    @Override
    public List<Sachbearbeiter> getSachA1() {

        if(em==null){
            throw new NoEntityManagerException();
        }

        TypedQuery<Sachbearbeiter> query= em.createQuery(
                "SELECT s FROM  Sachbearbeiter s WHERE s.abteilung='A1'",Sachbearbeiter.class);

        return query.getResultList();
    }


    /**
     * liefert alle Sachbearbeiter, die der Abteilung A2 zugeordnet sind
     *
     * @return wiedergabe der lieste von der Abteilugn A2
     */
    @Override
    public List<Sachbearbeiter> getSachA2() {

        if(em==null){
            throw new NoEntityManagerException();
        }

        TypedQuery<Sachbearbeiter> query= em.createQuery(
                "SELECT s FROM  Sachbearbeiter s WHERE s.abteilung='A2'",Sachbearbeiter.class);

        return query.getResultList();
    }
}
