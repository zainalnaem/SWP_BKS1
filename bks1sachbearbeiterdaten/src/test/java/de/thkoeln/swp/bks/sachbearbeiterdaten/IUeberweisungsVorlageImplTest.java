package de.thkoeln.swp.bks.sachbearbeiterdaten;

import static org.junit.Assert.*;
import de.thkoeln.swp.bks.bksdbmodel.entities.Konto;
import de.thkoeln.swp.bks.bksdbmodel.entities.Kunde;
import de.thkoeln.swp.bks.bksdbmodel.entities.UeberweisungsVorlage;
import de.thkoeln.swp.bks.bksdbmodel.entities.Ueberweisung;
import de.thkoeln.swp.bks.bksdbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.bks.bksdbmodel.services.IDatabase;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

public class IUeberweisungsVorlageImplTest {
    private IUeberweisungsVorlageImpl classUnderTest;
    public static final IDatabase db = new IDatabaseImpl();
    private static EntityManager em;


    /**
     * @Before: angenommen()
     * angenommen der EntityManager wird korrekt geholt,
     * UND die Implementierung der IAntragAdmin Schnittstelle wird als classUnderTest instanziiert,
     * UND der EntityManager em wird per setEntityManager Methode der classUnderTest gesetzt,
     * UND die Transaktion von em wird gestartet,
     * UND die Daten der betreffenden Entitäten wurden im Persistence Context gelöscht
     */
    @Before
    public void angenommen() {
        em= db.getEntityManager();
        classUnderTest = new IUeberweisungsVorlageImpl();
        classUnderTest.setEntityManager(em);
        em.getTransaction().begin();

        Query query = em.createQuery("DELETE FROM Ueberweisung");
        query.executeUpdate();
        Query query1 = em.createQuery("DELETE FROM UeberweisungsVorlage");
        query1.executeUpdate();
    }

    /**
     * @After: amEnde()
     * am Ende wird die Transaktion zurückgesetzt.
     */
    @After
    public void amEnde() {
        em.getTransaction().rollback();
    }

    /**
     * @Test: getUeberweisungsVorlageByID_00()
     * WENN eine TestUeberweisungsVorlage bereits in der DB existiert,
     * UND die Methode getUeberweisungsVorlageByID mit der Id der TestUeberweisungsVorlage aufgerufen wird,
     * DANN sollte sie die TestUeberweisungsVorlage zurückliefern.
     */
    @Test
    public void getUeberweisungsVorlageByID_00() {
        Konto testKonto = new Konto(null, "t", LocalDate.of(2020, 1, 21), 1333.33, 0, "s");
        em.persist(testKonto);

        Kunde testKunde = new Kunde(null, "Herr", "Hans", "Gruber", LocalDate.of(2003, 1, 29), "m", "l");
        em.persist(testKunde);

        UeberweisungsVorlage testUeberweisungsVorlage = new UeberweisungsVorlage(null, LocalDate.of(2023, 10, 20), "Privat", 20);
        testUeberweisungsVorlage.setVonkonto(testKonto);
        testUeberweisungsVorlage.setKunde(testKunde);
        em.persist(testUeberweisungsVorlage);

        UeberweisungsVorlage methodTesting = classUnderTest.getUeberweisungsVorlageByID(testUeberweisungsVorlage.getUvid());

        assertEquals(testUeberweisungsVorlage, methodTesting);
    }


    /**
     * @Test: getUeberweisungsVorlageByID_01()
     * WENN eine TestUeberweisungsVorlage nicht in der DB existiert,
     * UND die Methode getUeberweisungsVorlageByID mit der Id der TestUeberweisungsVorlage aufgerufen wird,
     * DANN sollte sie die NULL zurückliefern.
     */
    @Test
    public void getUeberweisungsVorlageByID_01() {
        Konto testKonto = new Konto(null, "t", LocalDate.of(2020, 1, 21), 1333.33, 0, "s");
        em.persist(testKonto);

        Kunde testKunde = new Kunde(null, "Herr", "Hans", "Gruber", LocalDate.of(2003, 1, 29), "m", "l");
        em.persist(testKunde);

        UeberweisungsVorlage testUeberweisungsVorlage = new UeberweisungsVorlage(null, LocalDate.of(2023, 10, 20), "Privat", 20);
        testUeberweisungsVorlage.setVonkonto(testKonto);
        testUeberweisungsVorlage.setKunde(testKunde);
        em.persist(testUeberweisungsVorlage);
        em.remove(testUeberweisungsVorlage);

        UeberweisungsVorlage methodTesting = classUnderTest.getUeberweisungsVorlageByID(testUeberweisungsVorlage.getUvid());

        assertNull(methodTesting);
    }


    /**
     * @Test: getUeberweisungsVorlagenDesKunden_00()
     * WENN ein TestKunde bereits in der DB existiert,
     * UND x (x>0) ÜberweisungsVorlagen in der DB existieren, deren Attribut kunde auf den TestKunden verweisen,
     * UND die Methode getUeberweisungsVorlagenDesKunden mit der ID des TestKunden aufgerufen wird,
     * DANN sollte sie die Liste mit den x ÜberweisungsVorlagen zurückliefern.
     */
    @Test
    public void getUeberweisungsVorlagenDesKunden_00() {
        Konto testKonto = new Konto(null, "t", LocalDate.of(2020, 1, 21), 1333.33, 0, "s");
        em.persist(testKonto);

        Kunde testKunde = new Kunde(null, "Herr", "Hans", "Gruber", LocalDate.of(2003, 1, 29), "m", "l");
        em.persist(testKunde);

        UeberweisungsVorlage testUeberweisungsVorlage1 = new UeberweisungsVorlage(null, LocalDate.of(2023, 10, 20), "Privat", 20);
        testUeberweisungsVorlage1.setVonkonto(testKonto);
        testUeberweisungsVorlage1.setKunde(testKunde);
        em.persist(testUeberweisungsVorlage1);

        UeberweisungsVorlage testUeberweisungsVorlage2 = new UeberweisungsVorlage(null, LocalDate.of(2022, 1, 2), "Business", 2);
        testUeberweisungsVorlage2.setVonkonto(testKonto);
        testUeberweisungsVorlage2.setKunde(testKunde);
        em.persist(testUeberweisungsVorlage2);

        List<UeberweisungsVorlage> methodTesting = classUnderTest.getUeberweisungsVorlagenDesKunden(testKunde.getKid());

        assertEquals(2, methodTesting.size());
    }

    /**
     * @Test: getUeberweisungsVorlagenDesKunden_01()
     * WENN ein TestKunde bereits in der DB existiert,
     * UND keine ÜberweisungsVorlagen in der DB existieren, deren Attribut kunde auf den TestKunden verweisen,
     * UND die Methode getUeberweisungsVorlagenDesKunden mit der ID des TestKunden aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern
     */
    @Test
    public void getUeberweisungsVorlagenDesKunden_01() {
        Kunde testKunde = new Kunde(null, "Herr", "Hans", "Gruber", LocalDate.of(2003, 1, 29), "m", "l");
        em.persist(testKunde);

        List<UeberweisungsVorlage> methodTesting = classUnderTest.getUeberweisungsVorlagenDesKunden(testKunde.getKid());

        assertTrue(methodTesting.isEmpty());
    }

    /**
     * @Test: getUeberweisungsVorlagenDesKunden_02()
     * WENN ein TestKunde nicht in der DB existiert,
     * UND die Methode getUeberweisungsVorlagenDesKunden mit der ID des TestKunden aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern
     */
    @Test
    public void getUeberweisungsVorlagenDesKunden_02() {
        Kunde testKunde = new Kunde(null, "Herr", "Hans", "Gruber", LocalDate.of(2003, 1, 29), "m", "l");
        em.persist(testKunde);
        em.remove(testKunde);

        List<UeberweisungsVorlage> methodTesting = classUnderTest.getUeberweisungsVorlagenDesKunden(testKunde.getKid());

        assertTrue(methodTesting.isEmpty());
    }

    /**
     * @Test: getUeberweisungsVorlagenDesKontos_00()
     * WENN ein TestKonto bereits in der DB existiert,
     * UND x (x>0) ÜberweisungsVorlagen in der DB existieren, deren Attribut vonkonto auf das TestKonto verweisen,
     * UND die Methode getUeberweisungsVorlagenDesKontos mit der ID des TestKontos aufgerufen wird,
     * DANN sollte sie die Liste mit den x ÜberweisungsVorlagen zurückliefern.
     */
    @Test
    public void getUeberweisungsVorlagenDesKontos_00() {
        Konto testKonto = new Konto(null, "t", LocalDate.of(2020, 1, 21), 1333.33, 0, "s");
        em.persist(testKonto);

        Kunde testKunde = new Kunde(null, "Herr", "Hans", "Gruber", LocalDate.of(2003, 1, 29), "m", "l");
        em.persist(testKunde);

        UeberweisungsVorlage testUeberweisungsVorlage1 = new UeberweisungsVorlage(null, LocalDate.of(2023, 10, 20), "Privat", 20);
        testUeberweisungsVorlage1.setVonkonto(testKonto);
        testUeberweisungsVorlage1.setKunde(testKunde);
        em.persist(testUeberweisungsVorlage1);

        UeberweisungsVorlage testUeberweisungsVorlage2 = new UeberweisungsVorlage(null, LocalDate.of(2022, 1, 2), "Business", 2);
        testUeberweisungsVorlage2.setVonkonto(testKonto);
        testUeberweisungsVorlage2.setKunde(testKunde);
        em.persist(testUeberweisungsVorlage2);

        List<UeberweisungsVorlage> methodTesting = classUnderTest.getUeberweisungsVorlagenDesKontos(testKonto.getKtoid());

        assertEquals(2, methodTesting.size());
    }

    /**
     * @Test: getUeberweisungsVorlagenDesKontos_01()
     * WENN ein TestKonto bereits in der DB existiert,
     * UND keine ÜberweisungsVorlagen in der DB existieren, deren Attribut vonkonto auf das TestKonto verweisen,
     * UND die Methode getUeberweisungsVorlagenDesKontos mit der ID des TestKontos aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getUeberweisungsVorlagenDesKontos_01() {
        Konto testKonto = new Konto(null, "t", LocalDate.of(2020, 1, 21), 1333.33, 0, "s");
        em.persist(testKonto);

        List<UeberweisungsVorlage> methodTesting = classUnderTest.getUeberweisungsVorlagenDesKontos(testKonto.getKtoid());

        assertTrue(methodTesting.isEmpty());
    }

    /**
     * @Test: getUeberweisungsVorlagenDesKontos_02()
     * WENN ein TestKonto nicht in der DB existiert,
     * UND die Methode getUeberweisungsVorlagenDesKontos mit der ID des TestKontos aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getUeberweisungsVorlagenDesKontos_02() {
        Konto testKonto = new Konto(null, "t", LocalDate.of(2020, 1, 21), 1333.33, 0, "s");
        em.persist(testKonto);
        em.remove(testKonto);

        List<UeberweisungsVorlage> methodTesting = classUnderTest.getUeberweisungsVorlagenDesKontos(testKonto.getKtoid());

        assertTrue(methodTesting.isEmpty());
    }

    /**
     * @Test: insertUeberweisungsVorlage_00()
     * WENN die Methode insertUeberweisungsVorlage mit einer TestUeberweisungsVorlage aufgerufen wird,
     * UND die ID der TestUeberweisungsVorlage gleich null ist,
     * DANN sollte sie TRUE zurückliefern,
     * UND die TestUeberweisungsVorlage sollte im Persistence Context existieren
     */
    @Test
    public void insertUeberweisungsVorlage_00(){
        Konto testKonto = new Konto(null, "t", LocalDate.of(2020, 1, 21), 1333.33, 0, "s");
        em.persist(testKonto);

        Kunde testKunde = new Kunde(null, "Herr", "Hans", "Gruber", LocalDate.of(2003, 1, 29), "m", "l");
        em.persist(testKunde);

        UeberweisungsVorlage testUeberweisungsVorlage = new UeberweisungsVorlage(null, LocalDate.of(2023, 10, 20), "Privat", 20);
        testUeberweisungsVorlage.setVonkonto(testKonto);
        testUeberweisungsVorlage.setKunde(testKunde);

        boolean methodTesting = classUnderTest.insertUeberweisungsVorlage(testUeberweisungsVorlage);

        assertTrue(methodTesting);

        assertNotNull(em.find(UeberweisungsVorlage.class,testUeberweisungsVorlage.getUvid()));
    }

    /**
     * @Test: insertUeberweisungsVorlage_01()
     * WENN die Methode insertUeberweisungsVorlage mit einer TestUeberweisungsVorlage aufgerufen wird,
     * UND die ID der TestUeberweisungsVorlage ungleich null ist,
     * DANN sollte sie FALSE zurückliefern,
     * UND der Persistence Context wurde nicht verändert
     */
    @Test
    public void insertUeberweisungsVorlage_01(){
        Konto testKonto = new Konto(null, "t", LocalDate.of(2020, 1, 21), 1333.33, 0, "s");
        em.persist(testKonto);

        Kunde testKunde = new Kunde(null, "Herr", "Hans", "Gruber", LocalDate.of(2003, 1, 29), "m", "l");
        em.persist(testKunde);

        UeberweisungsVorlage testUeberweisungsVorlage = new UeberweisungsVorlage(1, LocalDate.of(2023, 10, 20), "Privat", 20);
        testUeberweisungsVorlage.setVonkonto(testKonto);
        testUeberweisungsVorlage.setKunde(testKunde);

        boolean methodTesting = classUnderTest.insertUeberweisungsVorlage(testUeberweisungsVorlage);

        assertFalse(methodTesting);

        assertNull(em.find(UeberweisungsVorlage.class, testUeberweisungsVorlage.getUvid()));
    }

    /**
     * @Test: deleteUeberweisungsVorlage_00()
     * WENN eine TestUeberweisungsVorlage in der DB existiert,
     * UND die Methode deleteUeberweisungsVorlage mit der ID der TestUeberweisungsVorlage aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND die TestUeberweisungsVorlage sollte nicht mehr im Persistence Context existieren.
     */
    @Test
    public void deleteUeberweisungsVorlage_00(){
        Konto testKonto = new Konto(null, "t", LocalDate.of(2020, 1, 21), 1333.33, 0, "s");
        em.persist(testKonto);

        Kunde testKunde = new Kunde(null, "Herr", "Hans", "Gruber", LocalDate.of(2003, 1, 29), "m", "l");
        em.persist(testKunde);

        UeberweisungsVorlage testUeberweisungsVorlage = new UeberweisungsVorlage(null, LocalDate.of(2023, 10, 20), "Privat", 20);
        testUeberweisungsVorlage.setVonkonto(testKonto);
        testUeberweisungsVorlage.setKunde(testKunde);
        em.persist(testUeberweisungsVorlage);

        boolean methodTesting = classUnderTest.deleteUeberweisungsVorlage(testUeberweisungsVorlage.getUvid());

        assertTrue(methodTesting);

        assertNull(em.find(UeberweisungsVorlage.class,testUeberweisungsVorlage.getUvid()));
    }

    /**
     * @Test: deleteUeberweisungsVorlage_01()
     * WENN eine TestUeberweisungsVorlage nicht in der DB existiert,
     * UND die Methode deleteUeberweisungsVorlage mit der ID der TestUeberweisungsVorlage aufgerufen wird,
     * DANN sollte sie FALSE zurückliefern.
     */
    @Test
    public void deleteUeberweisungsVorlage_01(){
        Konto testKonto = new Konto(null, "t", LocalDate.of(2020, 1, 21), 1333.33, 0, "s");
        em.persist(testKonto);

        Kunde testKunde = new Kunde(null, "Herr", "Hans", "Gruber", LocalDate.of(2003, 1, 29), "m", "l");
        em.persist(testKunde);

        UeberweisungsVorlage testUeberweisungsVorlage = new UeberweisungsVorlage(null, LocalDate.of(2023, 10, 20), "Privat", 20);
        testUeberweisungsVorlage.setVonkonto(testKonto);
        testUeberweisungsVorlage.setKunde(testKunde);
        em.persist(testUeberweisungsVorlage);
        em.remove(testUeberweisungsVorlage);

        boolean methodTesting = classUnderTest.deleteUeberweisungsVorlage(testUeberweisungsVorlage.getUvid());

        assertFalse(methodTesting);
    }

    /**
     * @Test: editUeberweisungsVorlage_00
     * WENN eine TestUeberweisungsVorlage in der DB existiert,
     * UND die Methode editUeberweisungsVorlage mit einer veränderten
     * TestUeberweisungsVorlage (aber gleicher ID) aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND die TestUeberweisungsVorlage sollte im Persistence Context verändert sein.
     */
   @Test
    public void editUeberweisungsVorlage_00() {
       Konto konto =
               new Konto(null,"g",LocalDate.of(2016,3,18),5234.76,1000,"i" );
       Kunde kunde =
               new Kunde(null, "Herr","Wagner","Jonas",LocalDate.of(1994,6,23),"m","l");
       UeberweisungsVorlage existingVorlage =
                new UeberweisungsVorlage(null, LocalDate.of(2022,12,2), "Verwendungszweck 1",76);

       em.persist(konto);
       em.persist(kunde);
       existingVorlage.setVonkonto(konto);
       existingVorlage.setKunde(kunde);

       em.persist(existingVorlage);

       UeberweisungsVorlage newVorlage =
               new UeberweisungsVorlage(existingVorlage.getUvid(),LocalDate.of(2022,12,1), "Updated Verwendungszweck1",134);
        boolean result = classUnderTest.editUeberweisungsVorlage(newVorlage);
        assertTrue(result);
         //check if the UeberweisungsVorlage is updated in the Persistence Context
        UeberweisungsVorlage updatedVorlage = em.find(UeberweisungsVorlage.class, newVorlage.getUvid());
       assertEquals(newVorlage.getUvid(), updatedVorlage.getUvid());
       assertEquals(newVorlage.getDatum(), updatedVorlage.getDatum());
       assertEquals(newVorlage.getVerwendungszweck(), updatedVorlage.getVerwendungszweck());
       assertEquals(newVorlage.getBetrag(), updatedVorlage.getBetrag(),0.001);

    }

    /**
     * @Test: editUeberweisungsVorlage_01
     * WENN eine TestUeberweisungsVorlage nicht in der DB existiert,
     * UND die Methode editUeberweisungsVorlage mit der TestUeberweisungsVorlage
     * aufgerufen wird,
     * DANN sollte sie FALSE zurückliefern,
     * UND die TestUeberweisungsVorlage sollte nicht im Persistence Context existieren.
     */
    @Test
    public void editUeberweisungsVorlage_01(){
        Konto konto =
                new Konto(null,"g",LocalDate.of(2015,2,12),2341.43,1500,"i" );
        Kunde kunde =
                new Kunde(null, "Frau","Mueller","Anna",LocalDate.of(1991,7,7),"w","l");
        UeberweisungsVorlage existingVorlage =
                new UeberweisungsVorlage(null, LocalDate.of(2022,12,2), "Verwendungszweck 2",250);

        em.persist(konto);
        em.persist(kunde);
        existingVorlage.setVonkonto(konto);
        existingVorlage.setKunde(kunde);

        em.persist(existingVorlage);
        em.remove(existingVorlage);

        UeberweisungsVorlage newVorlage =
                new UeberweisungsVorlage(existingVorlage.getUvid(),LocalDate.of(2022,12,5),"Updated Verwendungszweck2",243);
        boolean result = classUnderTest.editUeberweisungsVorlage(newVorlage);
        assertFalse(result);
        UeberweisungsVorlage updatedVorlage = em.find(UeberweisungsVorlage.class, newVorlage.getUvid());
        assertNull(updatedVorlage);
    }

    /**
     * @Test: getUeberweisungenZuKonto_00()
     * WENN ein Testkonto bereits in der Datenbank existiert,
     * UND x (x>0) Überweisungen in der DB existieren, die über das Attribut zukonto auf das
     * Testkonto verweisen,
     * UND die Methode getUeberweisungenZuKonto mit der ID des TestKontos aufgerufen wird,
     * DANN sollte sie die Liste mit den x Überweisungen zurückliefern
     */
    @Test
    public void getUeberweisungenZuKonto_00(){
        int numberOfUeberweisungen = 5;
        Konto konto =
                new Konto(null,"s",LocalDate.of(2012,2,22),7200.22,500,"i" );
        Kunde kunde =
                new Kunde(null, "Herr","Schmidt","Felix",LocalDate.of(1976,1,15),"m","v");
        em.persist(kunde);
        em.persist(konto);

       for (int i = 0; i < numberOfUeberweisungen; i++) {
            Ueberweisung ueberweisung =
                    new Ueberweisung(null, 87, LocalDate.of(2023, 7, 4), "us");

            ueberweisung.setZukonto(konto);
            ueberweisung.setKunde(kunde);
            ueberweisung.setVonkonto(konto);
            em.persist(ueberweisung);
        }
            List<Ueberweisung> ueberweisungenList  = classUnderTest.getUeberweisungenZuKonto(konto.getKtoid());
        assertEquals(numberOfUeberweisungen, ueberweisungenList.size());
    }

    /**
     * @Test: getUeberweisungenZuKonto_01()
     * WENN ein Testkonto bereits in der Datenbank existiert,
     * UND keine Überweisungen in der DB existieren, die über das Attribut zukonto auf das
     * Testkonto verweisen,
     * UND die Methode getUeberweisungenZuKonto mit der ID des TestKontos aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern
     */
    @Test
    public void getUeberweisungenZuKonto_01() {
        Konto konto =
                new Konto(null,"g",LocalDate.of(2013,8,30),6200,2000,"i" );
        em.persist(konto);

        List<Ueberweisung> ueberweisungenList = classUnderTest.getUeberweisungenZuKonto(konto.getKtoid());
       assertTrue(ueberweisungenList.isEmpty());
    }

    /**
     * @Test: getUeberweisungenZuKonto_02()
     * WENN ein Testkonto nicht in der Datenbank existiert,
     * UND die Methode getUeberweisungenZuKonto mit der ID des TestKontos aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern
     */
    @Test
    public void getUeberweisungenZuKonto_02() {
        Konto konto =
                new Konto(null,"g",LocalDate.of(2010,4,18),3421,500,"i" );
        em.persist(konto);
        em.remove(konto);
        List<Ueberweisung> resultList = classUnderTest.getUeberweisungenZuKonto(konto.getKtoid());
        assertTrue(resultList.isEmpty());
    }

    /**
     * @Test: getUeberweisungenVonKonto_00()
     * WENN ein Testkonto bereits in der Datenbank existiert,
     * UND x (x>0) Überweisungen in der DB existieren, die über das Attribut vonkonto auf das
     * Testkonto verweisen,
     * UND die Methode getUeberweisungenVonKonto mit der ID des TestKontos aufgerufen wird,
     * DANN sollte sie die Liste mit den x Überweisungen zurückliefern.
     **/
    @Test
    public void getUeberweisungenVonKonto_00(){
        int numberOfUeberweisungen = 5;
        Konto konto =
                new Konto(null,"s",LocalDate.of(2009,2,13),2500,500,"i" );
        Kunde kunde =
                new Kunde(null, "Herr","Hoffmann","Paul",LocalDate.of(1973,7,26),"m","w");
        em.persist(kunde);
        em.persist(konto);
        for (int i = 0; i < numberOfUeberweisungen; i++) {
            Ueberweisung ueberweisung =
                    new Ueberweisung(null, 320, LocalDate.of(2017, 3, 4), "us");

            ueberweisung.setZukonto(konto);
            ueberweisung.setKunde(kunde);
            ueberweisung.setVonkonto(konto);
            em.persist(ueberweisung);
        }
        List<Ueberweisung> ueberweisungenList  = classUnderTest.getUeberweisungenVonKonto(konto.getKtoid());
        assertEquals(numberOfUeberweisungen, ueberweisungenList.size());
    }

    /**
     * @Test: getUeberweisungenVonKonto_01()
     * WENN ein Testkonto bereits in der Datenbank existiert,
     * UND keine Überweisungen in der DB existieren, die über das Attribut vonkonto auf das
     * Testkonto verweisen,
     * UND die Methode getUeberweisungenVonKonto mit der ID des TestKontos aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getUeberweisungenVonKonto_01(){
        Konto konto =
                new Konto(null,"g",LocalDate.of(2014,7,18),6450,1500,"i" );
        em.persist(konto);

        List<Ueberweisung> ueberweisungenList = classUnderTest.getUeberweisungenVonKonto(konto.getKtoid());
        assertTrue(ueberweisungenList.isEmpty());
    }

    /**
     * @Test: getUeberweisungenVonKonto_02()
     * WENN ein Testkonto nicht in der Datenbank existiert,
     * UND die Methode getUeberweisungenVonKonto mit der ID des TestKontos aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getUeberweisungenVonKonto_02(){
        Konto konto =
                new Konto(null,"s",LocalDate.of(2008,7,14),2470,500,"i" );
        em.persist(konto);
        em.remove(konto);
        List<Ueberweisung> ueberweisungenList = classUnderTest.getUeberweisungenVonKonto(konto.getKtoid());
        assertTrue(ueberweisungenList.isEmpty());
    }

    /**
     * @Test: getLoeschbareUeberweisungen_00()
     * WENN x (x>0) Überweisungen mit Status "ueberwiesen" in der DB existieren,
     * UND y (y>0) Überweisungen mit Status "storniert" in der DB existieren,
     * UND z (z>0) Überweisungen mit Status "nicht ueberweisbar" in der DB existieren,
     * UND die Methode getLoeschbareUeberweisungen aufgerufen wird,
     * DANN sollte sie die Liste mit den x+y+z Überweisungen zurückliefern.
     */
    @Test
    public void getLoeschbareUeberweisungen_00(){
        int x = 5; // Number of Ueberweisungen with status "ueberwiesen"
        int y = 3; // Number of Ueberweisungen with status "storniert"
        int z = 2; // Number of Ueberweisungen with status "nicht ueberweisbar"
        Konto konto =
                new Konto(null,"g",LocalDate.of(2022,1,6),6290,1200,"i" );
        Kunde kunde =
                new Kunde(null, "Herr","Schmitz","Simon",LocalDate.of(1974,8,23),"m","v");
        em.persist(kunde);
        em.persist(konto);

        for (int i = 0; i < x; i++) {
            Ueberweisung ueberweisung =
                    new Ueberweisung(null, 122, LocalDate.of(2021,5,23), "us");
            ueberweisung.setZukonto(konto);
            ueberweisung.setKunde(kunde);
            ueberweisung.setVonkonto(konto);
            em.persist(ueberweisung);
        }
        for (int i = 0; i < y; i++) {
            Ueberweisung ueberweisung =
                    new Ueberweisung(null, 230, LocalDate.of(2021,11,4), "st");
            ueberweisung.setZukonto(konto);
            ueberweisung.setKunde(kunde);
            ueberweisung.setVonkonto(konto);
            em.persist(ueberweisung);
        }
        for (int i = 0; i < z; i++) {
            Ueberweisung ueberweisung =
                    new Ueberweisung(null, 1200, LocalDate.of(2022,3,7), "nu");
            ueberweisung.setZukonto(konto);
            ueberweisung.setKunde(kunde);
            ueberweisung.setVonkonto(konto);
            em.persist(ueberweisung);
        }
        List<Ueberweisung> loeschbareUeberweisungen = classUnderTest.getLoeschbareUeberweisungen();
        int expectedSize = x + y + z;
        assertEquals(expectedSize, loeschbareUeberweisungen.size());
    }

    /**
     * @Test: getLoeschbareUeberweisungen_01()
     * WENN keine Überweisungen mit Status "ueberwiesen" in der DB existieren,
     * UND keine Überweisungen mit Status "storniert" in der DB existieren,
     * UND keine Überweisungen mit Status "nicht ueberweisbar" in der DB existieren,
     * UND die Methode getLoeschbareUeberweisungen aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getLoeschbareUeberweisungen_01(){
        List<Ueberweisung> loeschbareUeberweisungen = classUnderTest.getLoeschbareUeberweisungen();
        assertTrue(loeschbareUeberweisungen.isEmpty());
    }

    /**
     * @Test: loescheUeberweisung_00()
     * WENN eine TestUeberweisung bereits in der DB existiert,
     * UND diese TestUeberweisung den Status "ueberwiesen" besitzt
     * UND die Methode loescheUeberweisung mit der Id der TestUeberweisung aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND die TestUeberweisung sollte nicht mehr im Persistence Context existieren.
     */
    @Test
    public void loescheUeberweisung_00(){
        Kunde kunde =
                new Kunde(null, "Herr","Klein","Timo",LocalDate.of(1994,6,23),"m","l");
        Konto konto =
                new Konto(null,"g",LocalDate.of(2016,3,18),5234.76,43.76,"i" );
        em.persist(kunde);
        em.persist(konto);
        Ueberweisung ueberweisung =
                new Ueberweisung(null,65,LocalDate.of(2023,4,6),"us");
        ueberweisung.setZukonto(konto);
        ueberweisung.setKunde(kunde);
        ueberweisung.setVonkonto(konto);
        em.persist(ueberweisung);
        boolean result = classUnderTest.loescheUeberweisung(ueberweisung.getUbid());
        assertTrue(result);
        assertNull(em.find(Ueberweisung.class, ueberweisung.getUbid()));
    }

    /**
     * @Test: loescheUeberweisung_01()
     * WENN eine TestUeberweisung bereits in der DB existiert,
     * UND diese TestUeberweisung den Status "storniert" besitzt
     * UND die Methode loescheUeberweisung mit der Id der TestUeberweisung aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND die TestUeberweisung sollte nicht mehr im Persistence Context existieren
     */
    @Test
    public void loescheUeberweisung_01(){
        Kunde kunde =
                new Kunde(null, "Herr","Becker","Maximilian",LocalDate.of(2000,4,23),"m","l");
        Konto konto =
                new Konto(null,"g",LocalDate.of(2017,3,18),1254,53.2,"i" );
        em.persist(kunde);
        em.persist(konto);
        Ueberweisung ueberweisung =
                new Ueberweisung(null,987,LocalDate.of(2023,4,14),"st");
        ueberweisung.setZukonto(konto);
        ueberweisung.setKunde(kunde);
        ueberweisung.setVonkonto(konto);
        em.persist(ueberweisung);
        boolean result = classUnderTest.loescheUeberweisung(ueberweisung.getUbid());
        assertTrue(result);
        assertNull(em.find(Ueberweisung.class, ueberweisung.getUbid()));
    }

    /**
     * @Test: loescheUeberweisung_02()
     * WENN eine TestUeberweisung bereits in der DB existiert,
     * UND diese TestUeberweisung den Status "nicht ueberweisbar" besitzt
     * UND die Methode loescheUeberweisung mit der Id der TestUeberweisung aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND die TestUeberweisung sollte nicht mehr im Persistence Context existieren
     */
    @Test
    public void loescheUeberweisung_02(){
        Kunde kunde =
                new Kunde(null, "Frau","Müller","Sophia ",LocalDate.of(1986,5,26),"w","l");
        Konto konto =
                new Konto(null,"g",LocalDate.of(2011,3,18),6732,400,"i" );
        em.persist(kunde);
        em.persist(konto);
        Ueberweisung ueberweisung =
                new Ueberweisung(null,230,LocalDate.of(2023,1,6),"nu");
        ueberweisung.setZukonto(konto);
        ueberweisung.setKunde(kunde);
        ueberweisung.setVonkonto(konto);
        em.persist(ueberweisung);
        boolean result = classUnderTest.loescheUeberweisung(ueberweisung.getUbid());
        assertTrue(result);
        assertNull(em.find(Ueberweisung.class, ueberweisung.getUbid()));
    }

    /**
     * @Test: loescheUeberweisung_03()
     * WENN eine TestUeberweisung bereits in der DB existiert,
     * UND diese TestUeberweisung den Status "wartet" besitzt
     * UND die Methode loescheUeberweisung mit der Id der TestUeberweisung aufgerufen wird,
     * DANN sollte sie FALSE zurückliefern,
     * UND die TestUeberweisung sollte noch in der DB existieren.
     */
    @Test
    public void loescheUeberweisung_03(){
        Kunde kunde =
                new Kunde(null, "Herr","Fischer","Daniel",LocalDate.of(1990,6,1),"m","l");
        Konto konto =
                new Konto(null,"g",LocalDate.of(2013,3,23),3856,200,"i" );
        em.persist(kunde);
        em.persist(konto);
        Ueberweisung ueberweisung =
                new Ueberweisung(null,65,LocalDate.of(2022,4,6),"wt");
        ueberweisung.setZukonto(konto);
        ueberweisung.setKunde(kunde);
        ueberweisung.setVonkonto(konto);
        em.persist(ueberweisung);
        boolean result = classUnderTest.loescheUeberweisung(ueberweisung.getUbid());
        assertFalse(result);
        assertNotNull(em.find(Ueberweisung.class, ueberweisung.getUbid()));
    }

    /**
     * @Test: loescheUeberweisung_04()
     * WENN eine TestUeberweisung nicht in der DB existiert,
     * UND die Methode loescheUeberweisung mit der Id der TestUeberweisung aufgerufen wird,
     * DANN sollte sie FALSE zurückliefern
     */
    @Test
    public void loescheUeberweisung_04(){
        Kunde kunde =
                new Kunde(null, "Frau","Zimmermann","Isabella",LocalDate.of(1994,9,19),"w","l");
        Konto konto =
                new Konto(null,"g",LocalDate.of(2013,1,18),2865,150,"i" );
        em.persist(kunde);
        em.persist(konto);
        Ueberweisung ueberweisung =
                new Ueberweisung(null,326,LocalDate.of(2023,3,16),"wt");
        ueberweisung.setKunde(kunde);
        ueberweisung.setVonkonto(konto);
        ueberweisung.setZukonto(konto);
        em.persist(ueberweisung);
        em.remove(ueberweisung);
        boolean result = classUnderTest.loescheUeberweisung(ueberweisung.getUbid());
        assertFalse(result);
    }

    /**
     * @Test: getWartendeUeberweisungen_00()
     * WENN x (x>0) Überweisungen mit Status "wartet" in der DB existieren,
     * UND die Methode getWartendeUeberweisungen aufgerufen wird,
     * DANN sollte sie die Liste mit den x Überweisungen zurückliefern.
     */
    @Test
    public void getWartendeUeberweisungen_00(){
        int numberOfUeberweisungen = 3;
        Konto konto =
                new Konto(null,"g",LocalDate.of(2018,9,18),1243,250,"i" );
        Kunde kunde =
                new Kunde(null, "Herr","Schulz","Timo",LocalDate.of(1999,2,9),"m","l");
        em.persist(kunde);
        em.persist(konto);
        for (int i = 0; i < numberOfUeberweisungen; i++) {
            Ueberweisung ueberweisung =
                    new Ueberweisung(null, 420, LocalDate.of(2020, 4, 4), "wt");

            ueberweisung.setZukonto(konto);
            ueberweisung.setKunde(kunde);
            ueberweisung.setVonkonto(konto);
            em.persist(ueberweisung);
        }
        List<Ueberweisung> wartendeUeberweisungen = classUnderTest.getWartendeUeberweisungen();
        assertEquals(numberOfUeberweisungen,wartendeUeberweisungen.size());
    }

    /**
     * @Test: getWartendeUeberweisungen_01()
     * WENN keine Überweisungen mit Status "wartet" in der DB existieren,
     * UND die Methode getWartendeUeberweisungen aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getWartendeUeberweisungen_01(){
        List<Ueberweisung> wartendeUeberweisungen = classUnderTest.getWartendeUeberweisungen();
        assertTrue(wartendeUeberweisungen.isEmpty());
    }
}
