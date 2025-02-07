package de.thkoeln.swp.bks.sachbearbeiterdaten;

import de.thkoeln.swp.bks.bksdbmodel.entities.Antrag;
import de.thkoeln.swp.bks.bksdbmodel.entities.Sachbearbeiter;
import de.thkoeln.swp.bks.bksdbmodel.impl.IDatabaseImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IAntragSachImplTest {
    public IAntragSachImpl classUnderTest;
    public static final IDatabaseImpl db = new IDatabaseImpl();
    public static EntityManager em;

    /**
     * @Before: angenommen()
     * Angenommen der EntityManager wird korrekt geholt,
     * UND die Implementierung der IAntragSach Schnittstelle wird als classUnderTest instanziiert,
     * UND der EntityManager em wird per setEntityManager Methode der classUnderTest gesetzt,
     * UND die Transaktion von em wird gestartet,
     * UND die Daten der betreffenden Entitäten wurden im Persistence Context gelöscht.
     */
    @Before
    public void angenommen() {
        em = db.getEntityManager();
        classUnderTest = new IAntragSachImpl();
        classUnderTest.setEntityManager(em);
        em.getTransaction().begin();
        Query deleteAntragQuery = em.createQuery("DELETE FROM Antrag a");
        Query deleteSachQuery = em.createQuery("DELETE FROM Sachbearbeiter s");
        deleteAntragQuery.executeUpdate();
        deleteSachQuery.executeUpdate();
        // Deleting possibly existing data from the context
        em.clear();
    }

    /**
     * @After: amEnde()
     * Am Ende wird die Transaktion zurück gesetzt.
     */
    @After
    public void amEnde() {
        em.getTransaction().rollback();
    }

    /*+
    @Test: getAntragListe_00()
        WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
        UND x (x>0) Anträge in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
        UND die Methode getAntragListe mit der ID des TestSachbearbeiters aufgerufen wird,
        DANN sollte sie die Liste mit diesen x Anträgen zurückliefern.
    */
    @Test
    public void getAntragListe_00(){
        // Creating Objects to put them into Database for testing if they are acutally inside of it
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","Peter",
                "Hans","m",LocalDate.of(1996,1,1),"1");
        Antrag antrag = new Antrag(null ,"sd","","#","b");

        // Writing Objects inside the Database
        em.persist(testSachbearbeiter);
        em.persist(antrag);

        //Creating list for the searched Anträge from Sachbearbeiter
        antrag.setSachbearbeiter(testSachbearbeiter); // Connecting antrag with a given Sachbearbeiter

        // Checking if the antreage from a certain sachbearbeiter also are the same as the ones in the database of that sachbearbeiter
        assertEquals(1,classUnderTest.getAntragListe(testSachbearbeiter.getSid()).size());

    }
    /*
    *   getAntragListe_01()
        WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
        UND keine Anträge in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
        UND die Methode getAntragListe mit der ID des TestSachbearbeiters aufgerufen wird,
        DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getAntragListe_01(){
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","Bersan","Student","m",
                LocalDate.of(1996,9,9),"A1");
        em.persist(testSachbearbeiter);
        // Taking the Size of the list from testSachbearbeiter and comparing it to the list of that same Sachbearbeiter
        assertEquals(0,classUnderTest.getAntragListe(testSachbearbeiter.getSid()).size());
    }
    /*
        getAntragListe_02()
        WENN ein TestSachbearbeiter nicht in der Datenbank existiert,
        UND die Methode getAntragListe mit der ID des TestSachbearbeiters aufgerufen wird,
        DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getAntragListe_02(){
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(1,"Herr","Müller","Thomas","m",
                LocalDate.of(1996,9,9),"A1");
        assertEquals(0,classUnderTest.getAntragListe(testSachbearbeiter.getSid()).size());
    }
    /*
    setAntragBearbeitet_00()
    WENN ein Testantrag bereits in der DB existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Testantrag den Typ "Kontoeröffnungs-Antrag" besitzt,
    UND der Testantrag den Status "genehmigt" besitzt,
    UND der Testantrag vom TestSachbearbeiter erstellt wurde,
    UND die Methode setAntragBearbeitet mit der ID des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte der Status des Testantrags auf "bearbeitet" gesetzt sein,
    UND TRUE zurückliefern
     */
    @Test
    public void setAntragBearbeitet_00(){
        Antrag testAntrag = new Antrag(null ,"ko","Daten","#","g");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        // Creating AntragListe for my testSachbearbeiter
        em.persist(testAntrag);

        em.persist(testSachbearbeiter);

        testAntrag.setSachbearbeiter(testSachbearbeiter);

        assertTrue(classUnderTest.setAntragBearbeitet(testAntrag.getAtid(), testSachbearbeiter.getSid()));
        // Pruefen ob antrag status b hat
        assertEquals("b" , testAntrag.getStatus());


    }
    /*
    setAntragBearbeitet_01()
    WENN ein Testantrag bereits in der DB existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Testantrag den Typ "Kontolimit-Antrag" besitzt,
    UND der Testantrag den Status "genehmigt" besitzt,
    UND der Testantrag vom TestSachbearbeiter erstellt wurde,
    UND die Methode setAntragBearbeitet mit der Id des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie FALSE zurückliefern,
    UND der Testantrag wurde nicht verändert.
     */
    @Test
    public void setAntragBearbeitet_01(){
        Antrag testAntrag = new Antrag(null ,"kl","Daten","#","g");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");

        em.persist(testAntrag);
        em.persist(testSachbearbeiter); // Sachbearbeitet manage antrag

        testAntrag.setSachbearbeiter(testSachbearbeiter);

        assertFalse(classUnderTest.setAntragBearbeitet(testAntrag.getAtid(), testSachbearbeiter.getSid()));
        Antrag antrag = em.find(Antrag.class , testAntrag.getAtid());

        assertEquals(testAntrag,antrag);
        // Prüfen ob Status Veraenderung pruefen
        assertEquals("g" , testAntrag.getStatus());
    }
    /*
    setAntragBearbeitet_02()
    WENN ein Testantrag bereits in der DB existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Testantrag den Typ "Sachbearbeiter-Bearbeiten-Antrag" besitzt,
    UND der Testantrag den Status "genehmigt" besitzt,
    UND die Methode setAntragBearbeitet mit der Id des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie FALSE zurückliefern,
    UND der Testantrag wurde nicht verändert.
     */
    @Test
    public void setAntragBearbeitet_02(){
        Antrag testAntrag = new Antrag(null ,"sb","Daten","#","g");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");

        em.persist(testAntrag);
        em.persist(testSachbearbeiter); // Sachbearbeitet manage antrag

        assertFalse(classUnderTest.setAntragBearbeitet(testAntrag.getAtid(), testSachbearbeiter.getSid()));
        Antrag antrag = em.find(Antrag.class , testAntrag.getAtid());

        assertEquals(testAntrag,antrag);
        assertEquals("g" , testAntrag.getStatus());
    }
    /*
    setAntragBearbeitet_03()
    WENN ein Testantrag bereits in der DB existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Testantrag den Typ "Kontoschließungs-Antrag" besitzt,
    UND der Testantrag den Status "genehmigt" besitzt,
    UND der Testantrag vom TestSachbearbeiter erstellt wurde,
    UND die Methode setAntragBearbeitet mit der Id des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie FALSE zurückliefern,
    UND der Testantrag wurde nicht verändert.
     */
    @Test
    public void setAntragBearbeitet_03(){
        Antrag testAntrag = new Antrag(null ,"ks","Daten","#","g");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");

        em.persist(testAntrag);
        em.persist(testSachbearbeiter); // Sachbearbeitet manage antrag

        testAntrag.setSachbearbeiter(testSachbearbeiter);

        assertFalse(classUnderTest.setAntragBearbeitet(testAntrag.getAtid(), testSachbearbeiter.getSid()));
        Antrag antrag = em.find(Antrag.class , testAntrag.getAtid());
        assertEquals(testAntrag,antrag);

        assertEquals("g" , testAntrag.getStatus());

    }
    /*
    setAntragBearbeitet_04()
    WENN ein Testantrag bereits in der DB existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Testantrag den Typ "Sachbearbeiter-Löschen-Antrag" besitzt,
    UND die Methode setAntragBearbeitet mit der Id des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie FALSE zurückliefern,
    UND der Testantrag wurde nicht verändert.
     */
    @Test
    public void setAntragBearbeitet_04(){
        Antrag testAntrag = new Antrag(null ,"sd","Daten","#","g");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");

        //Adding entities into database
        em.persist(testAntrag);
        em.persist(testSachbearbeiter); // Sachbearbeitet manage antrag

        assertFalse(classUnderTest.setAntragBearbeitet(testAntrag.getAtid(), testSachbearbeiter.getSid()));
        //Checking if Antrag is changed or not
        Antrag antrag = em.find(Antrag.class , testAntrag.getAtid());
        assertEquals(testAntrag,antrag);
        assertEquals("g" , testAntrag.getStatus());

    }
    /*
    setAntragBearbeitet_05()
    WENN ein Testantrag bereits in der DB existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Testantrag den Typ "Sachbearbeiter-Anlegen-Antrag" besitzt,
    UND die Methode setAntragBearbeitet mit der Id des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie FALSE zurückliefern,
    UND der Testantrag wurde nicht verändert
     */
    @Test
    public void setAntragBearbeitet_05(){
        Antrag testAntrag = new Antrag(null ,"so","Daten","No Comment","g");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");

        //Adding entities into database
        em.persist(testAntrag);
        em.persist(testSachbearbeiter); // Sachbearbeitet manage antrag


        assertFalse(classUnderTest.setAntragBearbeitet(testAntrag.getAtid(), testSachbearbeiter.getSid()));

        //Searching entity within the database with the testAntrag-id
        Antrag antrag = em.find(Antrag.class , testAntrag.getAtid());
        //Checking if the testAntrag status is still the same
        assertEquals(testAntrag,antrag);
        assertEquals("g" , testAntrag.getStatus());
    }
    /*
    setAntragBearbeitet_06()
    WENN ein Testantrag bereits in der DB existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Testantrag den Typ "Kontoeröffnungs-Antrag" besitzt,
    UND der Testantrag den Status "neu" besitzt,
    UND der Testantrag vom TestSachbearbeiter erstellt wurde,
    UND die Methode setAntragBearbeitet mit der Id des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie FALSE zurückliefern,
    UND der Testantrag wurde nicht verändert
     */
    @Test
    public void setAntragBearbeitet_06(){
        Antrag testAntrag = new Antrag(null ,"ko","Daten","No Comment","n");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");

        //Adding entities into database
        em.persist(testAntrag);
        em.persist(testSachbearbeiter); // Sachbearbeitet manage antrag

        testAntrag.setSachbearbeiter(testSachbearbeiter);


        assertFalse(classUnderTest.setAntragBearbeitet(testAntrag.getAtid(), testSachbearbeiter.getSid()));

        //Searching entity within the database with the testAntrag-id
        Antrag antrag = em.find(Antrag.class , testAntrag.getAtid());
        //Checking if the testAntrag status is still the same
        assertEquals(testAntrag,antrag);
        assertEquals("n" , testAntrag.getStatus());
    }
    /*
    setAntragBearbeitet_07()
    WENN ein Testantrag nicht in der DB existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND die Methode setAntragBearbeitet mit der Id des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie FALSE zurückliefern.
     */
    @Test
    public void setAntragBearbeitet_07(){
        Antrag testAntrag = new Antrag(1 ,"so","Daten","No Comment","g");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");

        //Adding entities into database
        em.persist(testSachbearbeiter); // Sachbearbeitet manage antrag

        assertFalse(classUnderTest.setAntragBearbeitet(testAntrag.getAtid(), testSachbearbeiter.getSid()));

    }
    /*
    antragStornieren_00()
    WENN ein TestAntrag bereits in der Datenbank existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Status des TestAntrags "neu" ist,
    UND der Typ des TestAntrags "Kontoeröffnungs-Antrag" ist,
    UND der Testantrag vom TestSachbearbeiter erstellt wurde,
    UND die Methode antragStornieren mit der Id des TestAntrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie TRUE zurückliefern,
    UND der TestAntrag sollte im Persistennce Context den Status "storniert" besitzen.
     */
    @Test
    public void antragStonieren_00(){
        Antrag testAntrag = new Antrag(null,"ko","Daten","No Comment","n");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        // Adding entities into database
        em.persist(testAntrag);
        em.persist(testSachbearbeiter);
        // Adding Sachbearbeiter to Antrag
        testAntrag.setSachbearbeiter(testSachbearbeiter);

        assertTrue(classUnderTest.antragStornieren(testAntrag.getAtid(),testSachbearbeiter.getSid()));
        // Checking if the type of antrag is "storniert"
        Antrag testContextContract = em.find(Antrag.class ,testAntrag.getAtid());
        assertEquals("s",testContextContract.getStatus());
    }
    /*
    antragStornieren_01()
    WENN ein TestAntrag bereits in der Datenbank existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Status des TestAntrags "genehmigt" ist,
    UND der Typ des TestAntrags "Kontoeröffnungs-Antrag" ist,
    UND der Testantrag vom TestSachbearbeiter erstellt wurde,
    UND die Methode antragStornieren mit der Id des TestAntrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie TRUE zurückliefern,
    UND der TestAntrag sollte im Persistennce Context den Status "storniert" besitzen.
     */
    @Test
    public void antragStonieren_01(){
        Antrag testAntrag = new Antrag(null,"ko","Daten","No Comment","g");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        // Adding entities into database
        em.persist(testAntrag);
        em.persist(testSachbearbeiter);
        // Adding Sachbearbeiter to Antrag
        testAntrag.setSachbearbeiter(testSachbearbeiter);

        assertTrue(classUnderTest.antragStornieren(testAntrag.getAtid(),testSachbearbeiter.getSid()));
        // Checking if the type of antrag is "storniert"
        Antrag testContextContract = em.find(Antrag.class ,testAntrag.getAtid());
        assertEquals("s",testContextContract.getStatus());
    }
    /*
    antragStornieren_02()
    WENN ein TestAntrag bereits in der Datenbank existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Status des TestAntrags "neu" ist,
    UND der Typ des TestAntrags "Kontoschließungs-Antrag" ist,
    UND der Testantrag vom TestSachbearbeiter erstellt wurde,
    UND die Methode antragStornieren mit der Id des TestAntrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie TRUE zurückliefern,
    UND der TestAntrag sollte im Persistennce Context den Status "storniert" besitzen.
     */
    @Test
    public void antragStonieren_02(){
        Antrag testAntrag = new Antrag(null,"ks","Daten","No Comment","n");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        // Adding entities into database
        em.persist(testAntrag);
        em.persist(testSachbearbeiter);
        // Adding Sachbearbeiter to Antrag
        testAntrag.setSachbearbeiter(testSachbearbeiter);

        assertTrue(classUnderTest.antragStornieren(testAntrag.getAtid(),testSachbearbeiter.getSid()));
        // Checking if the type of antrag is "storniert"
        Antrag testContextContract = em.find(Antrag.class ,testAntrag.getAtid());
        assertEquals("s",testContextContract.getStatus());
    }
    /*
    antragStornieren_03()
    WENN ein TestAntrag bereits in der Datenbank existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Status des TestAntrags "genehmigt" ist,
    UND der Typ des TestAntrags "Kontoschließungs-Antrag" ist,
    UND der Testantrag vom TestSachbearbeiter erstellt wurde,
    UND die Methode antragStornieren mit der Id des TestAntrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie TRUE zurückliefern,
    UND der TestAntrag sollte im Persistennce Context den Status "storniert" besitzen.
     */
    @Test
    public void antragStonieren_03(){
        Antrag testAntrag = new Antrag(null,"ks","Daten","No Comment","g");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        // Adding entities into database
        em.persist(testAntrag);
        em.persist(testSachbearbeiter);
        // Adding Sachbearbeiter to Antrag
        testAntrag.setSachbearbeiter(testSachbearbeiter);

        assertTrue(classUnderTest.antragStornieren(testAntrag.getAtid(),testSachbearbeiter.getSid()));
        // Checking if the type of antrag is "storniert"
        Antrag testContextContract = em.find(Antrag.class ,testAntrag.getAtid());
        assertEquals("s",testContextContract.getStatus());
    }
    /*
    antragStornieren_04()
    WENN ein TestAntrag bereits in der Datenbank existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Status des TestAntrags "neu" ist,
    UND der Typ des TestAntrags "Kontolimit-Antrag" ist,
    UND der Testantrag vom TestSachbearbeiter erstellt wurde,
    UND die Methode antragStornieren mit der Id des TestAntrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie TRUE zurückliefern,
    UND der TestAntrag sollte im Persistennce Context den Status "storniert" besitzen.
     */
    @Test
    public void antragStonieren_04(){
        Antrag testAntrag = new Antrag(null,"kl","Daten","No Comment","n");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        // Adding entities into database
        em.persist(testAntrag);
        em.persist(testSachbearbeiter);
        // Adding Sachbearbeiter to Antrag
        testAntrag.setSachbearbeiter(testSachbearbeiter);

        assertTrue(classUnderTest.antragStornieren(testAntrag.getAtid(),testSachbearbeiter.getSid()));
        // Checking if the type of antrag is "storniert"
        Antrag testContextContract = em.find(Antrag.class ,testAntrag.getAtid());
        assertEquals("s",testContextContract.getStatus());

    }
    /*
    antragStornieren_05()
    WENN ein TestAntrag nicht in der Datenbank existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND die Methode antragStornieren mit der Id des TestAntrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie FALSE zurückliefern,
    UND der Persistence Context wurde nicht verändert.
     */
    @Test
    public void antragStonieren_05(){
        Antrag testAntrag = new Antrag(1,"ko","Daten","No Comment","n");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        // Adding entities into database
        em.persist(testSachbearbeiter);

        assertFalse(classUnderTest.antragStornieren(testAntrag.getAtid(),testSachbearbeiter.getSid()));
        // Checking with persistence Context
        assertFalse(em.contains(testAntrag));
    }
    /*
    antragStornieren_06()
    WENN ein TestAntrag bereits in der Datenbank existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Typ des TestAntrags "Kontoeröffnungs-Antrag" ist,
    UND der Status des TestAntrags "abgelehnt" ist,
    UND der Testantrag vom TestSachbearbeiter erstellt wurde,
    UND die Methode antragStornieren mit der Id des TestAntrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie FALSE zurückliefern,
    UND der Persistence Context wurde nicht verändert.
    */
    @Test
    public void antragStonieren_06(){
        Antrag testAntrag = new Antrag(null,"ko","Daten","No Comment","a");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        // Adding entities into database
        em.persist(testAntrag);
        em.persist(testSachbearbeiter);
        // Adding Sachbearbeiter to Antrag
        testAntrag.setSachbearbeiter(testSachbearbeiter);

        assertFalse(classUnderTest.antragStornieren(testAntrag.getAtid(),testSachbearbeiter.getSid()));
        // Checking if the type of antrag is "storniert"
        Antrag testContextContract = em.find(Antrag.class ,testAntrag.getAtid());
        assertEquals("a",testContextContract.getStatus());
    }
    /*
    antragStornieren_07()
    WENN ein TestAntrag bereits in der Datenbank existiert,
    UND ein TestSachbearbeiter bereits in der DB existiert,
    UND der Typ des TestAntrags "Sachbearbeiter-Anlegen-Antrag" ist,
    UND die Methode antragStornieren mit der Id des TestAntrags und der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie FALSE zurückliefern,
    UND der Persistence Context wurde nicht verändert.
     */
    @Test
    public void antragStonieren_07(){
        Antrag testAntrag = new Antrag(null,"so","Daten","No Comment","b");
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        // Adding entities into database
        em.persist(testAntrag);
        em.persist(testSachbearbeiter);
        // Adding Sachbearbeiter to Antrag
        testAntrag.setSachbearbeiter(testSachbearbeiter);

        assertFalse(classUnderTest.antragStornieren(testAntrag.getAtid(),testSachbearbeiter.getSid()));
        // Checking if the type of antrag is "storniert"
        Antrag testContextContract = em.find(Antrag.class ,testAntrag.getAtid());
        assertEquals("b",testContextContract.getStatus());
    }
    /*
    getNeueAntraege_00()
    WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
    UND x (x>0) Anträge mit Status "neu" in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
    UND die Methode getNeueAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie die Liste mit diesen x Anträgen zurückliefern.
     */
    @Test
    public void getNeueAntraege_00(){
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        Antrag antragOne = new Antrag(null,"sb","daten","no comment","n");
        Antrag antragTwo = new Antrag(null,"sb","daten","no comment","n");

        // Adding objects to database
        em.persist(testSachbearbeiter);
        em.persist(antragOne);
        em.persist(antragTwo);


        //Connecting the values of the single objects to testsachbearbeiter
        antragOne.setSachbearbeiter(testSachbearbeiter);
        antragTwo.setSachbearbeiter(testSachbearbeiter);

        //Testing the methode getNeueAntrage if the return value is the given list of antraege from this testSachbearbeiter
        assertEquals(2,classUnderTest.getNeueAntraege(testSachbearbeiter.getSid()).size());

    }
    /*
    getNeueAntraege_01()
    WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
    UND keine Anträge mit Status "neu" in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
    UND die Methode getNeueAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie eine leere Liste zurückliefern.
    */
    @Test
    public void getNeueAntraege_01(){
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(null,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        Antrag antragOne = new Antrag(null,"sb","daten","no comment","b");
        Antrag antragTwo = new Antrag(null,"sb","daten","no comment","b");

        // Adding objects to database
        em.persist(testSachbearbeiter);
        em.persist(antragOne);
        em.persist(antragTwo);


        //Connecting the values of the single objects to testsachbearbeiter
        antragOne.setSachbearbeiter(testSachbearbeiter);
        antragTwo.setSachbearbeiter(testSachbearbeiter);

        //Testing the methode getNeueAntrage if the return value is the given list of antraege from this testSachbearbeiter
        assertEquals(0,classUnderTest.getNeueAntraege(testSachbearbeiter.getSid()).size());
    }
    /*
    getNeueAntraege_02()
    WENN ein TestSachbearbeiter nicht in der Datenbank existiert,
    UND die Methode getNeueAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
    DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getNeueAntraege_02(){
        Sachbearbeiter testSachbearbeiter = new Sachbearbeiter(1,"Herr","peter","Thomas","m",
                LocalDate.of(1999,2,3),"A1");
        //Testing the methode getNeueAntrage if the return value is the given list of antraege from this testSachbearbeiter
        assertEquals(0,classUnderTest.getNeueAntraege(testSachbearbeiter.getSid()).size());
    }
    /*
    getAlleAntraege_00()
    WENN x (x>0) Anträge in der DB existieren,
    UND die Methode getAlleAntraege aufgerufen wird,
    DANN sollte sie die Liste mit diesen x Anträgen zurückliefern.
     */
    @Test
    public void getAlleAntraege_00(){
        Antrag antragOne = new Antrag(null,"sb","daten","no comment","n");
        Antrag antragTwo = new Antrag(null,"sb","daten","no comment","b");
        Antrag antragThree = new Antrag(null,"sb","daten","no comment","b");

        // Adding objects to database
        em.persist(antragOne);
        em.persist(antragTwo);
        em.persist(antragThree);
        //Testing if the x > 0 Antraege are in the database
        assertEquals(3,classUnderTest.getAlleAntraege().size());
    }
    /*
    getAlleAntraege_01()
    WENN keine Anträge in der DB existieren,
    UND die Methode getAlleAntraege aufgerufen wird,
    DANN sollte sie eine leere Liste zurückliefern.
    */
    @Test
    public void getAlleAntraege_01(){
        // Testing if zero Antraege are in the database
        assertEquals(0,classUnderTest.getAlleAntraege().size());
    }

    /**
     * @Test: deleteAntrag_00()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND ein TestAntrag mit Status "abgelehnt" und Typ "Kontolimit-Antrag" bereits der DB existiert,
     * UND der TestSachbearbeiter den TestAntrag erstellt hat,
     * UND die Methode deleteAntrag mit der ID des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND der TestAntrag sollte aus der DB entfernt sein.
     */
    @Test
    public void deleteAntrag_00() {
        Sachbearbeiter testBearbeiter =
                new Sachbearbeiter(null, "Herr", "test", "testName", "m",
                        LocalDate.of(1, 1, 1), "A1");
        Antrag testAntrag = new Antrag(null, "kl", "test", "test", "a");
        em.persist(testBearbeiter);
        em.persist(testAntrag);
        testAntrag.setSachbearbeiter(testBearbeiter);

        boolean result = classUnderTest.deleteAntrag(testAntrag.getAtid(),
                testBearbeiter.getSid());

        assertTrue(result);
        assertNull(em.find(Antrag.class, testAntrag.getAtid()));
    }

    /**
     * @Test: deleteAntrag_01()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND ein TestAntrag mit Status "abgelehnt" und Typ "Kontoschließungs-Antrag" bereits der DB existiert,
     * UND der TestSachbearbeiter den TestAntrag erstellt hat,
     * UND die Methode deleteAntrag mit der ID des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND der TestAntrag sollte aus der DB entfernt sein.
     */
    @Test
    public void deleteAntrag_01() {
        Sachbearbeiter testBearbeiter =
                new Sachbearbeiter(null, "Herr", "test", "testName", "m",
                        LocalDate.of(1, 1, 1), "A1");
        Antrag testAntrag = new Antrag(null, "ks", "test", "test", "a");
        em.persist(testBearbeiter);
        em.persist(testAntrag);
        testAntrag.setSachbearbeiter(testBearbeiter);

        boolean result = classUnderTest.deleteAntrag(testAntrag.getAtid(),
                testBearbeiter.getSid());

        assertTrue(result);
        assertNull(em.find(Antrag.class, testAntrag.getAtid()));
    }

    /**
     * @Test: deleteAntrag_02()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND ein TestAntrag mit Status "abgelehnt" und Typ "Kontoeröffnungs-Antrag" bereits der DB existiert,
     * UND der TestSachbearbeiter den TestAntrag erstellt hat,
     * UND die Methode deleteAntrag mit der ID des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND der TestAntrag sollte aus der DB entfernt sein.
     */
    @Test
    public void deleteAntrag_02() {
        Sachbearbeiter testBearbeiter =
                new Sachbearbeiter(null, "Herr", "test", "testName", "m",
                        LocalDate.of(1, 1, 1), "A1");
        Antrag testAntrag = new Antrag(null, "ko", "test", "test", "a");
        em.persist(testBearbeiter);
        em.persist(testAntrag);
        testAntrag.setSachbearbeiter(testBearbeiter);

        boolean result = classUnderTest.deleteAntrag(testAntrag.getAtid(),
                testBearbeiter.getSid());

        assertTrue(result);
        assertNull(em.find(Antrag.class, testAntrag.getAtid()));
    }

    /**
     * @Test: deleteAntrag_03()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND ein TestAntrag mit Status "genehmigt" und Typ "Kontoeröffnungs-Antrag" bereits der DB existiert,
     * UND der TestSachbearbeiter den TestAntrag erstellt hat,
     * UND die Methode deleteAntrag mit der ID des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie FALSE zurückliefern,
     * UND der TestAntrag sollte nicht aus der DB entfernt sein.
     */
    @Test
    public void deleteAntrag_03() {
        Sachbearbeiter testBearbeiter =
                new Sachbearbeiter(null, "Herr", "test", "testName", "m",
                        LocalDate.of(1, 1, 1), "A1");
        Antrag testAntrag = new Antrag(null, "ko", "test", "test", "g");
        em.persist(testBearbeiter);
        em.persist(testAntrag);
        testAntrag.setSachbearbeiter(testBearbeiter);

        boolean result = classUnderTest.deleteAntrag(testAntrag.getAtid(),
                testBearbeiter.getSid());

        assertFalse(result);
        assertEquals(testAntrag, em.find(Antrag.class, testAntrag.getAtid()));
    }

    /**
     * @Test: deleteAntrag_04()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND ein TestAntrag mit Status "abgelehnt" und Typ "Kontoschließungs-Antrag" bereits der DB existiert,
     * UND der TestSachbearbeiter den TestAntrag nicht erstellt hat,
     * UND die Methode deleteAntrag mit der ID des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie FALSE zurückliefern,
     * UND der TestAntrag sollte nicht aus der DB entfernt sein.
     */
    @Test
    public void deleteAntrag_04() {
        Sachbearbeiter testBearbeiter =
                new Sachbearbeiter(null, "Herr", "test", "testName", "m",
                        LocalDate.of(1, 1, 1), "A1");
        Antrag testAntrag = new Antrag(null, "ks", "test", "test", "a");
        em.persist(testBearbeiter);
        em.persist(testAntrag);

        boolean result = classUnderTest.deleteAntrag(testAntrag.getAtid(),
                testBearbeiter.getSid());

        assertFalse(result);
        assertEquals(testAntrag, em.find(Antrag.class, testAntrag.getAtid()));
    }

    /**
     * @Test: deleteAntrag_05()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND ein TestAntrag mit Status "abgelehnt" und Typ "Sachbearbeiter-Anlegen" bereits der DB existiert,
     * UND die Methode deleteAntrag mit der ID des Testantrags und der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie FALSE zurückliefern,
     * UND der TestAntrag sollte nicht aus der DB entfernt sein.
     */
    @Test
    public void deleteAntrag_05() {
        Sachbearbeiter testBearbeiter =
                new Sachbearbeiter(null, "Herr", "test", "testName", "m",
                        LocalDate.of(1, 1, 1), "A1");
        Antrag testAntrag = new Antrag(null, "so", "test", "test", "a");
        em.persist(testBearbeiter);
        em.persist(testAntrag);

        boolean result = classUnderTest.deleteAntrag(testAntrag.getAtid(),
                testBearbeiter.getSid());

        assertFalse(result);
        assertEquals(testAntrag, em.find(Antrag.class, testAntrag.getAtid()));
    }

    /**
     * Utility method to delete every entry from the relevant tables
     */
    private void deleteFromRelevantTables() {
        // Resetting the database state to empty tables
        Query query = em.createQuery("DELETE FROM Antrag");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM Sachbearbeiter");
        query.executeUpdate();
    }

    /**
     * Utility Class as return type of the utility insertAntraege... function
     */
    private static class AntraegeWithSachbearbeiter {
        Sachbearbeiter sachbearbeiter;
        List<Antrag> antraege;

        AntraegeWithSachbearbeiter(Sachbearbeiter sachbearbeiter,
                                   List<Antrag> antraege) {
            this.sachbearbeiter = sachbearbeiter;
            this.antraege = antraege;
        }
    }

    /**
     * Utility method to insert antraege with a zugehöriger sachbearbeiter
     *
     * @param status the status of all the antraege
     * @param amount the amount of antraege to insert
     * @return an object containing the created sachbearbeiter and antraege
     */
    private AntraegeWithSachbearbeiter insertAntraegeWithStatusAndSachbearbeiter(
            String status, int amount) {
        List<Antrag> testAntraege = new ArrayList<Antrag>();
        Sachbearbeiter testBearbeiter =
                new Sachbearbeiter(null, "Herr", "test", "testName", "m",
                        LocalDate.of(1, 1, 1), "A1");
        for (int i = 1; i <= amount; i++) {
            Antrag testAntrag = new Antrag(null, "ko", "test", "test", status);
            em.persist(testAntrag);
            testAntraege.add(testAntrag);
        }
        em.persist(testBearbeiter);

        for (Antrag a : testAntraege) {
            a.setSachbearbeiter(testBearbeiter);
        }

        return new AntraegeWithSachbearbeiter(testBearbeiter, testAntraege);
    }

    /**
     * @Test: getAbgelehnteAntraege_00()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND x (x>0) Anträge mit Status "abgelehnt" in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
     * UND die Methode getAbgelehnteAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie die Liste mit diesen x Anträgen zurückliefern.
     */
    @Test
    public void getAbgelehnteAntraege_00() {
        int numberAntraege = 5;
        AntraegeWithSachbearbeiter antraegeAndBearbeiter;
        antraegeAndBearbeiter =
                insertAntraegeWithStatusAndSachbearbeiter("a", numberAntraege);
        List<Antrag> resultList = classUnderTest.getAbgelehnteAntraege(
                antraegeAndBearbeiter.sachbearbeiter.getSid());
        assertArrayEquals(antraegeAndBearbeiter.antraege.toArray(),
                resultList.toArray());
    }

    /**
     * @Test: getAbgelehnteAntraege_01()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND keine Anträge mit Status "abgelehnt" in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
     * UND die Methode getAbgelehnteAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getAbgelehnteAntraege_01() {
        Sachbearbeiter testBearbeiter =
                new Sachbearbeiter(null, "Herr", "test", "testName", "m",
                        LocalDate.of(1, 1, 1), "A1");
        em.persist(testBearbeiter);

        List<Antrag> resultList =
                classUnderTest.getAbgelehnteAntraege(testBearbeiter.getSid());
        assertEquals(0, resultList.size());
    }

    /**
     * @Test: getAbgelehnteAntraege_02()
     * WENN ein TestSachbearbeiter nicht in der Datenbank existiert,
     * UND die Methode getAbgelehnteAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getAbgelehnteAntraege_02() {
        deleteFromRelevantTables();
        List<Antrag> resultList = classUnderTest.getAbgelehnteAntraege(1);
        assertEquals(0, resultList.size());
    }

    /**
     * @Test: getBearbeiteteAntraege_00()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND x (x>0) Anträge mit Status "bearbeitet" in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
     * UND die Methode getBearbeiteteAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie die Liste mit diesen x Anträgen zurückliefern.
     */
    @Test
    public void getBearbeiteteAntraege_00() {
        int numberAntraege = 5;
        AntraegeWithSachbearbeiter antraegeWithSachbearbeiter;
        antraegeWithSachbearbeiter =
                insertAntraegeWithStatusAndSachbearbeiter("b", numberAntraege);
        List<Antrag> resultList = classUnderTest.getBearbeiteteAntraege(
                antraegeWithSachbearbeiter.sachbearbeiter.getSid());
        assertArrayEquals(antraegeWithSachbearbeiter.antraege.toArray(),
                resultList.toArray());
    }

    /**
     * @Test: getBearbeiteteAntraege_01()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND keine Anträge mit Status "bearbeitet" in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
     * UND die Methode getBearbeiteteAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getBearbeiteteAntraege_01() {
        Sachbearbeiter testBearbeiter =
                new Sachbearbeiter(null, "Herr", "test", "testName", "m",
                        LocalDate.of(1, 1, 1), "A1");
        em.persist(testBearbeiter);

        List<Antrag> resultList =
                classUnderTest.getBearbeiteteAntraege(testBearbeiter.getSid());
        assertEquals(0, resultList.size());
    }

    /**
     * @Test: getBearbeiteteAntraege_02()
     * WENN ein TestSachbearbeiter nicht in der Datenbank existiert,
     * UND die Methode getBearbeiteteAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getBearbeiteteAntraege_02() {
        deleteFromRelevantTables();
        List<Antrag> resultList = classUnderTest.getBearbeiteteAntraege(1);
        assertEquals(0, resultList.size());
    }

    /**
     * @Test: getStornierteAntraege_00()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND x (x>0) Anträge mit Status "storniert" in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
     * UND die Methode getStornierteAntraege mit der ID des TestSachbearbeiters aufgerufen
     * wird,
     * DANN sollte sie die Liste mit diesen x Anträgen zurückliefern.
     */
    @Test
    public void getStornierteAntraege_00() {
        int numberAntraege = 5;
        AntraegeWithSachbearbeiter antraegeWithSachbearbeiter;
        antraegeWithSachbearbeiter =
                insertAntraegeWithStatusAndSachbearbeiter("s", numberAntraege);
        List<Antrag> resultList = classUnderTest.getStornierteAntraege(
                antraegeWithSachbearbeiter.sachbearbeiter.getSid());
        assertArrayEquals(antraegeWithSachbearbeiter.antraege.toArray(),
                resultList.toArray());
    }

    /**
     * @Test: getStornierteAntraege_01()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND keine Anträge mit Status "storniert" in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
     * UND die Methode getStornierteAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getStornierteAntraege_01() {
        Sachbearbeiter testBearbeiter =
                new Sachbearbeiter(null, "Herr", "test", "testName", "m",
                        LocalDate.of(1, 1, 1), "A1");
        em.persist(testBearbeiter);

        List<Antrag> resultList =
                classUnderTest.getStornierteAntraege(testBearbeiter.getSid());
        assertEquals(0, resultList.size());
    }

    /**
     * @Test: getStornierteAntraege_02()
     * WENN ein TestSachbearbeiter nicht in der Datenbank existiert,
     * UND die Methode getStornierteAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getStornierteAntraege_02() {
        deleteFromRelevantTables();
        List<Antrag> resultList = classUnderTest.getStornierteAntraege(1);
        assertEquals(0, resultList.size());
    }

    /**
     * @Test: getGenehmigteAntraege_00()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND x (x>0) Anträge mit Status "genehmigt" in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
     * UND die Methode getGenehmigteAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie die Liste mit diesen x Anträgen zurückliefern.
     */
    @Test
    public void getGenehmigteAntraege_00() {
        int numberAntraege = 5;
        AntraegeWithSachbearbeiter antraegeWithSachbearbeiter;
        antraegeWithSachbearbeiter =
                insertAntraegeWithStatusAndSachbearbeiter("g", numberAntraege);
        List<Antrag> resultList = classUnderTest.getGenehmigteAntraege(
                antraegeWithSachbearbeiter.sachbearbeiter.getSid());
        assertArrayEquals(antraegeWithSachbearbeiter.antraege.toArray(),
                resultList.toArray());
    }

    /**
     * @Test: getGenehmigteAntraege_01()
     * WENN ein TestSachbearbeiter bereits in der Datenbank existiert,
     * UND keine Anträge mit Status "genehmigt" in der DB existieren, die dieser TestSachbearbeiter erstellt hat,
     * UND die Methode getGenehmigteAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getGenehmigteAntraege_01() {
        Sachbearbeiter testBearbeiter =
                new Sachbearbeiter(null, "Herr", "test", "testName", "m",
                        LocalDate.of(1, 1, 1), "A1");
        em.persist(testBearbeiter);

        List<Antrag> resultList =
                classUnderTest.getGenehmigteAntraege(testBearbeiter.getSid());
        assertEquals(0, resultList.size());
    }

    /**
     * @Test: getGenehmigteAntraege_02()
     * WENN ein TestSachbearbeiter nicht in der Datenbank existiert,
     * UND die Methode getGenehmigteAntraege mit der ID des TestSachbearbeiters aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getGenehmigteAntraege_02() {
        deleteFromRelevantTables();
        List<Antrag> resultList = classUnderTest.getGenehmigteAntraege(1);
        assertEquals(0, resultList.size());
    }
}
