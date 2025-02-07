package de.thkoeln.swp.arzt.arztdaten;
import de.thkoeln.swp.arzt.arztdbmodel.entities.Arzt;
import de.thkoeln.swp.arzt.arztdbmodel.exceptions.NoEntityManagerException;
import de.thkoeln.swp.arzt.datenhaltungapi.ICRUDArzt;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.logging.Logger;

public class ICRUDArztImpl implements ICRUDArzt {

    private static final Logger LOGGER = Logger.getLogger(ICRUDArztImpl.class.getName());
    private EntityManager em;
    @Override
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean insertArzt(Arzt arzt) {
        if (em == null)
            throw new NoEntityManagerException();
        if (arzt == null || arzt.getAid() != null){
            return false;
        }
        em.persist(arzt);
        return true;
    }
}
