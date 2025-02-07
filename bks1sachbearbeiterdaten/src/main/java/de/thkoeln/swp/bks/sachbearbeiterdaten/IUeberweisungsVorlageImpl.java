package de.thkoeln.swp.bks.sachbearbeiterdaten;

import de.thkoeln.swp.bks.bksdbmodel.entities.Ueberweisung;
import de.thkoeln.swp.bks.bksdbmodel.entities.UeberweisungsVorlage;
import de.thkoeln.swp.bks.bksdbmodel.exceptions.NoEntityManagerException;
import de.thkoeln.swp.bks.datenhaltungapi.IUeberweisungsVorlage;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class IUeberweisungsVorlageImpl implements IUeberweisungsVorlage {

    private EntityManager em;

    /**
     * Sets the entity manager to be used by the service.
     *
     * @param em The entity manager to be set.
     */
    @Override
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    /**
     * Retrieves an UeberweisungsVorlage entity by its ID.
     *
     * @param uvid The ID of the UeberweisungsVorlage entity to retrieve.
     * @return The retrieved UeberweisungsVorlage entity, or null if not found.
     */
    @Override
    public UeberweisungsVorlage getUeberweisungsVorlageByID(int uvid) {
        if (em == null) {
            throw new NoEntityManagerException();
        }
        return em.find(UeberweisungsVorlage.class, uvid);
    }

    /**
     * Retrieves a list of UeberweisungsVorlage entities associated with the specified Kunde ID.
     *
     * @param kid The Kunde ID.
     * @return A list of UeberweisungsVorlage entities associated with the specified Kunde entity.
     */

    @Override
    public List<UeberweisungsVorlage> getUeberweisungsVorlagenDesKunden(int kid) {
        if (em == null) {
            throw new NoEntityManagerException();
        }
        TypedQuery<UeberweisungsVorlage> query = em.createQuery("SELECT uv FROM UeberweisungsVorlage uv WHERE uv.kunde.kid = :kid", UeberweisungsVorlage.class);
        query.setParameter("kid", kid);
        return query.getResultList();
    }

    /**
     * Retrieves a list of UeberweisungsVorlage entities associated with the specified Konto ID.
     *
     * @param ktoid The ID of the Konto.
     * @return A list of UeberweisungsVorlage entities associated with the specified Konto entity.
     */
    @Override
    public List<UeberweisungsVorlage> getUeberweisungsVorlagenDesKontos(int ktoid) {
        if (em == null) {
            throw new NoEntityManagerException();
        }
        TypedQuery<UeberweisungsVorlage> query = em.createQuery("SELECT uv FROM UeberweisungsVorlage uv WHERE uv.vonkonto.ktoid = :ktoid", UeberweisungsVorlage.class);
        query.setParameter("ktoid", ktoid);
        return query.getResultList();
    }


    /**
     * Inserts a new UeberweisungsVorlage entity.
     *
     * @param uv The UeberweisungsVorlage entity to insert.
     * @return True if the insertion was successful, false otherwise.
     */
    @Override
    public boolean insertUeberweisungsVorlage(UeberweisungsVorlage uv) {
        // Check if the EntityManager is null
        if(em==null){
            throw new NoEntityManagerException();
        }
        // Check if the provided UeberweisungsVorlage object is null or already has an ID
        else if (uv==null || uv.getUvid()!=null){
            return false;
        }
        // If all conditions are met, persist the UeberweisungsVorlage entity and return true
        else {
            em.persist(uv);
            return true;
        }
    }

    /**
     * Deletes an existing UeberweisungsVorlage entity by its ID.
     *
     * @param uvid The ID of the UeberweisungsVorlage entity to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    @Override
    public boolean deleteUeberweisungsVorlage(int uvid) {
        // Find the UeberweisungsVorlage entity by its ID
        UeberweisungsVorlage ueberweisungsVorlage = em.find(UeberweisungsVorlage.class,uvid);
        if(em==null){
            throw new NoEntityManagerException();
        }
        // Check if the UeberweisungsVorlage entity is null
        else if (ueberweisungsVorlage==null){
            return false;
        }
        // If all conditions are met, remove the UeberweisungsVorlage entity and return true
        else {
            em.remove(ueberweisungsVorlage);
            return true;
        }
    }

/**
     * Edits the details of an existing bank transfer template.
     * @param uv The bank transfer template to be edited.
     * @return True if the editing operation is successful, false otherwise.
     */
    @Override
    public boolean editUeberweisungsVorlage(UeberweisungsVorlage uv) {
        if(em==null){
            throw new NoEntityManagerException();
        }
        // Find the managed entity by its ID
        UeberweisungsVorlage existingVorlage  = em.find(UeberweisungsVorlage.class,uv.getUvid());

        if (existingVorlage  == null){
            return false; // Entity not found
        }
        // Update the existing Vorlage by merging the new object directly
       em.merge(uv);
        return true;
    }

    /**
     * Returns a list of bank transfer transactions that are sent to the specified account.
     * @param ktoid The ID of the account to which the bank transfer transactions are sent.
     * @return A list of bank transfer transactions sent to the specified account.
     */
    @Override
    public List<Ueberweisung> getUeberweisungenZuKonto(int ktoid) {
        if(em==null){
            throw new NoEntityManagerException();
        }
        TypedQuery<Ueberweisung> query = em.createQuery("SELECT u FROM Ueberweisung u WHERE u.zukonto.ktoid = :ktoid", Ueberweisung.class);
        query.setParameter("ktoid", ktoid);
        return query.getResultList();
    }

    /**
     * Returns a list of bank transfer transactions that are sent from the specified account.
     * @param ktoid The ID of the account from which the bank transfer transactions are sent.
     * @return A list of bank transfer transactions sent from the specified account.
     */
    @Override
    public List<Ueberweisung> getUeberweisungenVonKonto(int ktoid) {
        if(em==null){
            throw new NoEntityManagerException();
        }
        TypedQuery<Ueberweisung> query = em.createQuery("SELECT u FROM Ueberweisung u WHERE u.vonkonto.ktoid = :ktoid", Ueberweisung.class);
        query.setParameter("ktoid",ktoid);
        return query.getResultList();
    }

    /**
     * Returns a list of bank transfers that are eligible for deletion.
     * @return A list of deletable bank transfer transactions.
     */
    @Override
    public List<Ueberweisung> getLoeschbareUeberweisungen() {
        if(em==null){
            throw new NoEntityManagerException();
        }
        TypedQuery<Ueberweisung> query = em.createQuery("SELECT u FROM Ueberweisung u WHERE u.status IN ('us', 'st', 'nu')", Ueberweisung.class);
        return query.getResultList();
    }


    /**
     * Deletes a bank transfer transaction from the database if it meets certain criteria.
     * @param ubid The ID of the bank transfer transaction to be deleted.
     * @return True if the deletion operation is successful, false otherwise.
     */
    @Override
    public boolean loescheUeberweisung(int ubid) {
        if(em==null){
            throw new NoEntityManagerException();
        }
        Ueberweisung ueberweisung = em.find(Ueberweisung.class, ubid);
        if (ueberweisung != null) {
                String status = ueberweisung.getStatus();
                // Check if the status allows deletion
                if (status.equals("us") || status.equals("st") || status.equals("nu")) {
                    em.remove(ueberweisung);
                    return true;
                } else {
                    return false; //if the status does not allow deletion
                }

        }
        return  false; //if the transaction does not exist
    }

    /**
     * Returns a list of bank transfer transactions that are pending.
     * @return A list of pending bank transfer transactions.
     */
    @Override
    public List<Ueberweisung> getWartendeUeberweisungen() {
        if(em==null){
            throw new NoEntityManagerException();
        }
        TypedQuery<Ueberweisung> query = em.createQuery("SELECT u FROM Ueberweisung u WHERE u.status = 'wt'", Ueberweisung.class);
            return query.getResultList();
    }
}
