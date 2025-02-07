package de.thkoeln.swp.arzt.arztdaten;

import de.thkoeln.swp.arzt.arztdbmodel.entities.Arzt;
import de.thkoeln.swp.arzt.arztdbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.arzt.arztdbmodel.services.IDatabase;

import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

public class ICRUDArztImlTest {

    public ICRUDArztImpl classUnderTest;
    public static EntityManager em;
    public static final IDatabase db = new IDatabaseImpl();

    @BeforeClass
    public static void einmaligVorAllenTests(){
        em = db.getEntityManager();
    }

    @Before
    public void angenommen(){
        classUnderTest = new ICRUDArztImpl();
        classUnderTest.setEntityManager(em);
        em.getTransaction().begin();
    }

    @After
    public void amEnde(){
        em.getTransaction().rollback();
    }

    @Test
    public void insertArzt_00(){
        Arzt arzt = new Arzt(null, "House", "Princeton");
        boolean actual = classUnderTest.insertArzt(arzt);
        assertTrue(actual);
    }

    @Test
    public void insertArzt_01(){
        Arzt arzt = new Arzt(2, "Cameron", "Princeton");
        boolean actual = classUnderTest.insertArzt(arzt);
        assertFalse(actual);
    }


}
