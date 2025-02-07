package de.thkoeln.swp.bks.sachbearbeiterdaten;

import de.thkoeln.swp.bks.bksdbmodel.entities.Sachbearbeiter;
import de.thkoeln.swp.bks.bksdbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.bks.bksdbmodel.services.IDatabase;
import org.junit.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

public class ICRUDSachImplTest {

    public ICRUDSachImpl classUnderTest;
    public static final IDatabase db = new IDatabaseImpl();
    public EntityManager em;


    /**
     *  @Before: angenommen()
     * Angenommen der EntityManager wird korrekt geholt,
     * UND die Implementierung der ICRUDSach Schnittstelle wird als classUnderTest
     * instanziiert,
     * UND der EntityManager em wird per setEntityManager Methode der classUnderTest
     * gesetzt,
     * UND die Transaktion von em wird gestartet,
     * UND die Daten der betreffenden Entitäten wurden im Persistence Context gelöscht.
     */
    @Before
    public void angenommen(){
        em=db.getEntityManager();
        classUnderTest=new ICRUDSachImpl();
        classUnderTest.setEntityManager(em);
        em.getTransaction().begin();
        em.clear();

        Query query= em.createQuery("DELETE FROM Antrag");
        query.executeUpdate();
        query=em.createQuery("DELETE FROM Sachbearbeiter");
        query.executeUpdate();
    }

    /**
     *  @After: amEnde()
     * Am Ende wird die Transaktion zurück gesetzt.
     */
    @After
    public void amEnde(){
        em.getTransaction().rollback();
    }

    /**
     * @Test: getSachByID_00()
     * WENN ein Testsachbearbeiter bereits in der DB existiert,
     * UND die Methode getSachByID mit der Id des Testsachbearbeiters aufgerufen wird,
     * DANN sollte sie den Testsachbearbeiter zurückliefern
      */
    @Test
    public void getSachByID_00(){
        Sachbearbeiter testSachbearbeiter=new Sachbearbeiter(null,"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A1");
        em.persist(testSachbearbeiter);

        Sachbearbeiter sachbearbeiterGefunden=classUnderTest.getSachByID(testSachbearbeiter.getSid());

        assertEquals(testSachbearbeiter,sachbearbeiterGefunden);
    }

    /**
     *  @Test: getSachByID_01()
     * WENN ein Testsachbearbeiter nicht in der DB existiert,
     * UND die Methode getSachByID mit der Id des Testsachbearbeiters aufgerufen wird,
     * DANN sollte sie NULL zurückliefern.
     */
    @Test
    public void getSachByID_01(){
        Sachbearbeiter testSachbearbeiter=new Sachbearbeiter(null,"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A1");
        em.persist(testSachbearbeiter);
        em.remove(testSachbearbeiter);

        Sachbearbeiter sachVorhanden=classUnderTest.getSachByID(testSachbearbeiter.getSid());
        assertNull(sachVorhanden);


    }

    /**
     *@Test: getSachListe_00()
     * WENN x (x>0) Sachbearbeiter in der DB existieren,
     * UND die Methode getSachListe aufgerufen wird,
     * DANN sollte sie eine Liste mit x Sachbearbeitern zurückliefern.
     */
    @Test
    public void getSachListe_00(){
        Sachbearbeiter testSachbearbeiter=new Sachbearbeiter(null,"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A1");
        em.persist(testSachbearbeiter);
        List<Sachbearbeiter> sachListe=classUnderTest.getSachListe();

        assertEquals( 1,sachListe.size());
    }

    /**
     *  @Test: getSachListe_01()
     * WENN keine Sachbearbeiter in der DB existieren,
     * UND die Methode getSachListe aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getSachListe_01(){
        List <Sachbearbeiter> sachListe=classUnderTest.getSachListe();
        assertEquals(0,sachListe.size());
    }

    /**
     *  @Test: insertSach_00()
     * WENN die Methode insertSach mit einem Testsachbearbeiter aufgerufen wird,
     * UND die ID des Testsachbearbeiters gleich null ist,
     * DANN sollte sie TRUE zurückliefern,
     * UND der Testsachbearbeiter sollte im Persistence Context existieren.
     */
    @Test
    public void insertSach_00(){
        Sachbearbeiter testSachbearbeiter=new Sachbearbeiter(null,"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A1");
        boolean pruf=classUnderTest.insertSach(testSachbearbeiter);
        assertTrue(pruf);

        Sachbearbeiter sachVorhanden=em.find(Sachbearbeiter.class,testSachbearbeiter.getSid());
        assertEquals(testSachbearbeiter,sachVorhanden);

    }

    /**
     *  @Test: insertSach_01()
     * WENN die Methode insertSach mit einem Testsachbearbeiter aufgerufen wird,
     * UND die ID des Testsachbearbeiter ungleich null ist,
     * DANN sollte sie FALSE zurückliefern,
     * UND der Persistence Context wurde nicht verändert.
     */
    @Test
    public void insertSach_01(){
        Sachbearbeiter testSachbearbeiter=new Sachbearbeiter(1,"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A1");
        boolean pruf=classUnderTest.insertSach(testSachbearbeiter);
        assertFalse(pruf);

        Sachbearbeiter sachVorhanden=em.find(Sachbearbeiter.class,testSachbearbeiter.getSid());
        assertNull(sachVorhanden);


    }

    /**
     * @Test: editSach_00()
     * WENN ein Testsachbearbeiter in der DB existiert,
     * UND die Methode editSach mit einem veränderten Testsachbearbeiter (aber gleicher ID)
     * aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND der Testsachbearbeiter sollte im Persistence Context verändert sein.
     */
    @Test
    public void editSach_00(){
        Sachbearbeiter testSachbearbeiter=new Sachbearbeiter(null,"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A1");
        em.persist(testSachbearbeiter);

        Sachbearbeiter sachEdit=new Sachbearbeiter(testSachbearbeiter.getSid(),"Frau","Schmidt","Lea",
                "w",LocalDate.of(2001,11,5),"Aachen","222","A1");
        boolean pruf=classUnderTest.editSach(sachEdit);
        assertTrue(pruf);

        Sachbearbeiter sachVorhanden=em.find(Sachbearbeiter.class,sachEdit.getSid());
        assertEquals("Frau",sachVorhanden.getTitel());
        assertEquals("Schmidt",sachVorhanden.getName());
        assertEquals("Lea",sachVorhanden.getVorname());
        assertEquals("w",sachVorhanden.getGeschlecht());
        assertEquals("222",sachVorhanden.getTelefon());

    }

    /**
     *  @Test: editSach_01()
     * WENN ein Testsachbearbeiter nicht in der DB existiert,
     * UND die Methode editSach mit dem Testsachbearbeiter aufgerufen wird,
     * DANN sollte sie FALSE zurückliefern,
     * UND der Testsachbearbeiter sollte nicht im Persistence Context existieren.
     */
    @Test
    public void editSach_01(){
        Sachbearbeiter testSachbearbeiter=new Sachbearbeiter(null,"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A1");
        em.persist(testSachbearbeiter);
        em.remove(testSachbearbeiter);

        Sachbearbeiter sachEdit=new Sachbearbeiter(testSachbearbeiter.getSid(),"Frau","Schmidt","Lea",
                "w",LocalDate.of(2001,11,5),"Aachen","222","A1");
        boolean pruf=classUnderTest.editSach(sachEdit);
        assertFalse(pruf);

       Sachbearbeiter sachVorhanden=em.find(Sachbearbeiter.class,sachEdit.getSid());
        assertNull(sachVorhanden);
    }

    /**
     *  @Test: deleteSach_00()
     * WENN ein Testsachbearbeiter in der DB existiert,
     * UND die Methode deleteSach mit der ID des Testsachbearbeiters aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND der Testsachbearbeiter sollte nicht mehr im Persistence Context existieren.
     */
    @Test
    public void deleteSach_00(){
        Sachbearbeiter testSachbearbeiter=new Sachbearbeiter(null,"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A1");
        em.persist(testSachbearbeiter);
        boolean pruf=classUnderTest.deleteSach(testSachbearbeiter.getSid());
        assertTrue(pruf);

        Sachbearbeiter sachGleich=em.find(Sachbearbeiter.class,testSachbearbeiter.getSid());
        assertNull(sachGleich);

    }

    /**
     *  @Test: deleteSach_01()
     * WENN ein Testsachbearbeiter nicht in der DB existiert,
     * UND die Methode deleteSach mit der ID des Testsachbearbeiters aufgerufen wird,
     * DANN sollte sie FALSE zurückliefern.
     */
    @Test
    public void deletSach_01(){
        Sachbearbeiter testSachbearbeiter=new Sachbearbeiter(null,"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A1");

        //wichtig damit der testSachbearbeiter eine ID bekommt
        em.persist(testSachbearbeiter);
        em.remove(testSachbearbeiter);

        Sachbearbeiter sachEdit=new Sachbearbeiter(testSachbearbeiter.getSid(),"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A1");
        boolean pruf=classUnderTest.deleteSach(sachEdit.getSid());
        assertFalse(pruf);

    }

    /**
     *  @Test: getSachA1_00()
     * WENN x (x>0) Sachbearbeiter in der DB existieren, die zur Abteilung A1 gehören,
     * UND die Methode getSachA1 aufgerufen wird,
     * DANN sollte sie eine Liste mit x Sachbearbeitern zurückliefern.
     */
    @Test
    public void getSachA1_00(){
        Sachbearbeiter testSachbearbeiter=new Sachbearbeiter(null,"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A1");
        em.persist(testSachbearbeiter);
       List <Sachbearbeiter> sachList=classUnderTest.getSachA1();


       assertEquals(1,sachList.size());

    }

    /**
     *  @Test: getSachA1_01()
     * WENN keine Sachbearbeiter in der DB existieren, die zur Abteilung A1 gehören,
     * UND die Methode getSachA1 aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getSachA1_01(){
        List<Sachbearbeiter> sachList=classUnderTest.getSachA1();
        assertEquals(0,sachList.size());

    }

    /**
     *  @Test: getSachA2_00()
     * WENN x (x>0) Sachbearbeiter in der DB existieren, die zur Abteilung A2 gehören,
     * UND die Methode getSachA2 aufgerufen wird,
     * DANN sollte sie eine Liste mit x Sachbearbeitern zurückliefern.
     */
    @Test
    public void getSachA2_00(){
        Sachbearbeiter testSachbearbeiter=new Sachbearbeiter(null,"Herr","Mueller","Johnas",
                "m", LocalDate.of(2001,11,5),"Aachen","111","A2");
        em.persist(testSachbearbeiter);
        List<Sachbearbeiter> sachList=classUnderTest.getSachA2();


        assertEquals(1,sachList.size());
    }

    /**
     *  @Test: getSachA2_01()
     * WENN keine Sachbearbeiter in der DB existieren, die zur Abteilung A2 gehören,
     * UND die Methode getSachA2 aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getSachA2_01(){
        List<Sachbearbeiter> sachList=classUnderTest.getSachA2();
        assertEquals(0,sachList.size());

    }



}
