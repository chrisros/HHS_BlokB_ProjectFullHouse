/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lidselecter;

import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris
 */
public class Toernooi_start extends javax.swing.JFrame {

    DefaultListModel tafelListModel = new DefaultListModel();
    DefaultListModel spelerListModel = new DefaultListModel();
    
    int tafelCounter;           //nieuwe id voor de tafel
    int whereClaus;             //Toernooi ID 
    int inschrijvingen;         //hoeveelheid mensen die zich hebben ingescreven en hebben 
    int maxPertafel;            //maximale hoeveelheid spelers per tafel          
    int aantalTafels;           //hoeveelheid complete tafels
    int totaalAantalTafels;     //totale hoefeelheid tafel
    int spelers;                //aantal spelers aan hele tafels
    int rating;                 //actuele hoeveelheid mensen in toernooi               
    int overigeSpelers;         //spelers niet in hele tafel
    int bonusTafel1 = 0;        //hoeveelheid mensen aan niet complete tafel1
    int bonusTafel2 = 0;        //hoeveelheid mensen aan niet complete tafel1
    int rondeId = 1;            //hoeveelste ronde van het toernooi het is (bij start altijd 1)
    int minSpelersPerTafel = 4;
    int fiches = 1000;

    /**
     * Creates new form Toernooi_start
     *
     * @param id = currently selected toernooi
     */
    public Toernooi_start(int id) {
        whereClaus = id;                                        //id van toernooi
        tafelCounter = getTafelCount();                         //
        initComponents();
        setLocationRelativeTo(null);
        TafelList.setModel(tafelListModel);
        SpelerList.setModel(spelerListModel);
        idToernooiTxt.setText(Integer.toString(whereClaus));
        toernooiGegevens();
        minSpelersPerTafel = (maxPertafel/2);
        rondeLabel.setText(Integer.toString(rondeId));
        toevoegenRonde();
        toevoegenTafel();
        toevoegenSpelers();
        weergevenTafels();
        //vulLijst();

    }
    //ophalen nieuwe id voor de tafel
    private int getTafelCount() {
        try {
            PreparedStatement stat1 = Sql_connect.getConnection().prepareStatement("SELECT MAX(Tafel_code) as hoogsteTafelId FROM tafel");
            ResultSet result1 = stat1.executeQuery();
            while (result1.next()) {
                int hoogsteTafelId = result1.getInt("hoogsteTafelId");
                hoogsteTafelId++;
                return hoogsteTafelId;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_vordering.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }
    
    private double berekenRating(double newRating)
    {
        int modifier;
        if (rating>(inschrijvingen*0.8))
        {
            modifier = -5;
        } else if(rating>(inschrijvingen*0.65))
        {
            modifier = -3;
        } else if(rating>(inschrijvingen*0.5))
        {
            modifier = -1;
        }else if(rating>(inschrijvingen*0.35))
        {
            modifier = 1;
        }else if(rating>(inschrijvingen*0.20))
        {
            modifier = 3;
        }else
        {
           modifier = 5; 
        }
        double multiplier = newRating/100;
        double returnStat;
        if(multiplier==1)
        {
        returnStat = modifier;  
        }else if(multiplier > 1&&modifier>0)
        {
        returnStat = (multiplier*modifier);    
        }else if(multiplier > 1&&modifier<0)
        {
        returnStat = (modifier/multiplier);    
        }else if(multiplier < 1&&modifier>0)
        {
        returnStat = (multiplier*modifier);    
        }else
        {
        returnStat = modifier;   
        }
        returnStat = Math.round(returnStat);
        return returnStat;
    }

    private void toernooiGegevens() {
        try {
            Sql_connect.doConnect();
            /* OPHALEN TOTAAL SPELERS DIE WERKELIJK ZIJN INGESCHREVEN */
            PreparedStatement stat1 = Sql_connect.getConnection().prepareStatement("SELECT count(Id_persoon) as inschrijvingen FROM toernooideelnemer WHERE Id_toernooi = ? AND isBetaald = 1 AND Positie = 0");
            stat1.setInt(1, whereClaus);
            ResultSet result1 = stat1.executeQuery();

            while (result1.next()) {
                inschrijvingen = result1.getInt("inschrijvingen");
            }

            /* OPHALEN SPELERS PER TAFEL */
            PreparedStatement stat2 = Sql_connect.getConnection().prepareStatement("SELECT Max_speler_per_tafel, Aantal_fiches FROM toernooi WHERE Id_toernooi = ?");
            stat2.setInt(1, whereClaus);
            ResultSet result2 = stat2.executeQuery();

            while (result2.next()) {
                maxPertafel = result2.getInt("Max_speler_per_tafel");
                fiches = result2.getInt("Aantal_fiches");
            }
            /* kijken of toernooi al is gestart */
            PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement("SELECT count(*) AS rondes FROM ronde WHERE Id_toernooi = ?");
            stat3.setInt(1, whereClaus);
            ResultSet result3 = stat3.executeQuery();

            while (result3.next()) {

                if (result3.getInt("rondes") != 0) {
                    rondeId = result3.getInt("rondes");
                }
            }
            /* OPHALEN NAAM TOERNOOI */
            PreparedStatement stat4 = Sql_connect.getConnection().prepareStatement("SELECT Naam FROM toernooi WHERE Id_toernooi = ?");
            stat4.setInt(1, whereClaus);
            ResultSet result4 = stat4.executeQuery();

            while (result4.next()) {
                String Naam = result4.getString("Naam");
                naamToernooiTxt.setText(Naam);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
        }
        aantalTafels = inschrijvingen / maxPertafel;
        spelers = (aantalTafels * maxPertafel);
        overigeSpelers = inschrijvingen % maxPertafel;
        totaalAantalTafels = aantalTafels;
        rating = inschrijvingen;
        if (overigeSpelers > 0) {
            if (overigeSpelers < minSpelersPerTafel) {
                int overschot = maxPertafel + overigeSpelers;
                bonusTafel1 = (overschot / 2);
                bonusTafel2 = overschot - bonusTafel1;
                aantalTafels--;
                totaalAantalTafels++;
            } else {
                bonusTafel1 = overigeSpelers;
                bonusTafel2 = 0;
                totaalAantalTafels++;
            }
        }

        //System.out.println("aantalTafels" + aantalTafels);
        //System.out.println("bonusTafel1" + bonusTafel1);
        //System.out.println("bonusTafel2" + bonusTafel2);

    }

    private void toevoegenTafel() {
        int count = 0;
        while (count < aantalTafels) {
            try {
                Sql_connect.doConnect();
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("INSERT INTO tafel (spelerStart, spelerAanwezig, toernooi, ronde) VALUES (?, ?, ?, ?)");
                stat.setInt(1, maxPertafel);
                stat.setInt(2, maxPertafel);
                stat.setInt(3, whereClaus);
                stat.setInt(4, rondeId);
                stat.executeUpdate();
                count++;
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (bonusTafel1 != 0) {
            try {
                Sql_connect.doConnect();
                PreparedStatement stat2 = Sql_connect.getConnection().prepareStatement("INSERT INTO tafel (spelerStart, spelerAanwezig, toernooi, ronde) VALUES (?, ?, ?, ?)");
                stat2.setInt(1, bonusTafel1);
                stat2.setInt(2, bonusTafel1);
                stat2.setInt(3, whereClaus);
                stat2.setInt(4, rondeId);
                stat2.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (bonusTafel2 != 0) {
            try {
                Sql_connect.doConnect();
                PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement("INSERT INTO tafel (spelerStart, spelerAanwezig, toernooi, ronde) VALUES (?, ?, ?, ?)");
                stat3.setInt(1, bonusTafel2);
                stat3.setInt(2, bonusTafel2);
                stat3.setInt(3, whereClaus);
                stat3.setInt(4, rondeId);
                stat3.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void toevoegenRonde() {
        try {
            Sql_connect.doConnect();
            PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement("INSERT INTO ronde (Id_toernooi, Id_ronde, Tafel_aantal) VALUES (?, ?, ?)");
            stat3.setInt(1, whereClaus);
            stat3.setInt(2, rondeId);
            stat3.setInt(3, totaalAantalTafels);
            stat3.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void toevoegenSpelers() {

        int tafelCount = 1;
        while (tafelCount <= aantalTafels) {
            try {
                Sql_connect.doConnect();
                PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement("UPDATE toernooideelnemer SET Tafel_code=?, Fiches=? WHERE Tafel_code is null AND Id_toernooi = ? LIMIT ?");
                stat3.setInt(1, tafelCounter);
                stat3.setInt(2, fiches);
                stat3.setInt(3, whereClaus);
                stat3.setInt(4, maxPertafel);
                stat3.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
            }
            tafelCount++;
            tafelCounter++;


        }
        if (bonusTafel1 != 0) {

            {
                try {
                    Sql_connect.doConnect();
                    PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement("UPDATE toernooideelnemer SET Tafel_code=?, Fiches=? WHERE Tafel_code is null AND Id_toernooi = ? LIMIT ?");
                    stat3.setInt(1, tafelCounter);
                    stat3.setInt(2, fiches);
                    stat3.setInt(3, whereClaus);
                    stat3.setInt(4, bonusTafel1);
                    stat3.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
                }
                tafelCount++;
                tafelCounter++;


            }
            if (bonusTafel2 != 0) {
                try {
                    Sql_connect.doConnect();
                    PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement("UPDATE toernooideelnemer SET Tafel_code=?, Fiches=? WHERE Tafel_code is null AND Id_toernooi = ? LIMIT ?");
                    stat3.setInt(1, tafelCounter);
                    stat3.setInt(2, fiches);
                    stat3.setInt(3, whereClaus);
                    stat3.setInt(4, bonusTafel2);
                    stat3.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
                }
                tafelCount++;
                tafelCounter++;

            }

        }
    }

    private boolean nextRoundFinal() {
        if (totaalAantalTafels > maxPertafel) {
            return false;
        } else {
            return true;
        }
    }

    private void weergevenTafels() {
        try {
            //System.out.println("weergevenTafels");
            String prepSqlStatement = "SELECT * FROM tafel WHERE toernooi = ? AND ronde = ?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setInt(1, whereClaus);
            stat.setInt(2, rondeId);
            ResultSet result = stat.executeQuery();

            tafelListModel.removeAllElements();
            int i = 1;
            while (result.next()) {
                ModelItem item = new ModelItem();
                item.idTafel = result.getString("Tafel_code");
                item.maxPTafel = result.getString("spelerStart");
                item.naam = "Tafel " + i + " (" + result.getString("spelerAanwezig") + " Spelers)";
                i++;
                tafelListModel.addElement(item);
            }
        } catch (Exception e) {
            ePopup(e);
        }
    }

    private int getSelectedTafel() {
        try {
            if (TafelList.getSelectedValue() == null) {
                return 1;
            } else {
                ModelItem selectedItem = (ModelItem) TafelList.getSelectedValue();
                return Integer.parseInt(selectedItem.idTafel);
            }
        } catch (Exception e) {
            return 1;
        }

    }

    private void weergevenSpelers() {
        //System.out.println(whereClaus);
        //System.out.println(getSelectedTafel());
        try {
            String prepSqlStatement = "Select P.Voornaam, P.Achternaam, P.Id_persoon from persoon P join toernooideelnemer T on T.Id_persoon = P.Id_persoon WHERE T.Id_toernooi = ? AND T.Tafel_code = ? AND T.Fiches>0";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setInt(1, whereClaus);
            stat.setInt(2, getSelectedTafel());
            ResultSet result = stat.executeQuery();

            spelerListModel.removeAllElements();

            while (result.next()) {
                ModelItem item = new ModelItem();
                item.voornaam = result.getString("P.Voornaam");
                item.achternaam = result.getString("P.Achternaam");
                item.id = result.getInt("P.Id_persoon");
                spelerListModel.addElement(item);
            }
        } catch (Exception e) {

            ePopup(e);
        }
    }

    private void elimineren() {
        if (SpelerList.getSelectedValue() == null || TafelList.getSelectedValue() == null) {
            final String eMessage = "Er is geen speler en/of tafel geselcteerd";
            JOptionPane.showMessageDialog(rootPane, eMessage);
        } else {
            try {
                ModelItem selectedTafel = (ModelItem) TafelList.getSelectedValue();
                String eliminatie_tafel = selectedTafel.idTafel;
                int spelersAanTafel = 0;

                Sql_connect.doConnect();
                PreparedStatement stat4 = Sql_connect.getConnection().prepareStatement("select count(Id_persoon) as aantalPersonen from toernooideelnemer where Tafel_code = ? AND Positie = 0 AND Id_toernooi = ?");
                stat4.setString(1, eliminatie_tafel);
                stat4.setInt(2, whereClaus);
                ResultSet result = stat4.executeQuery();
                while (result.next()) {
                    spelersAanTafel = result.getInt("aantalPersonen");
                }
                double newRating=100;
                
                PreparedStatement stat6 = Sql_connect.getConnection().prepareStatement("SELECT AVG(P.Rating) AS avgRating FROM persoon P"
                        + " JOIN toernooideelnemer T on T.Id_persoon = P.Id_persoon"
                        + " WHERE T.Tafel_code = ? AND Positie = 0 AND Id_toernooi = ?");
                stat6.setString(1, eliminatie_tafel);
                stat6.setInt(2, whereClaus);
                ResultSet result2 = stat6.executeQuery();
                while (result2.next()) {
                    newRating = result2.getInt("avgRating");
                }
                
                int newspelersAanTafel = spelersAanTafel;
                Sql_connect.doConnect();
                    PreparedStatement stat5 = Sql_connect.getConnection().prepareStatement("UPDATE tafel SET spelerAanwezig=? WHERE Tafel_code = ? AND toernooi = ?  LIMIT 1");
                    stat5.setInt(1, newspelersAanTafel);
                    stat5.setString(2, eliminatie_tafel);
                    stat5.setInt(3, whereClaus);
                    stat5.executeUpdate();
                    
                if (spelersAanTafel > 1) {
                    ModelItem selectedItem = (ModelItem) SpelerList.getSelectedValue();
                    int eliminatie_id = selectedItem.id;

                    Sql_connect.doConnect();
                    PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement("UPDATE toernooideelnemer SET Positie=?, Fiches=0 WHERE id_persoon=? AND Id_toernooi = ?  LIMIT 1");
                    stat3.setInt(1, rating);
                    stat3.setInt(2, eliminatie_id);
                    stat3.setInt(3, whereClaus);
                    stat3.executeUpdate();
                    int selectedIndex = SpelerList.getSelectedIndex();
                    spelerListModel.remove(selectedIndex);
                    
                    Sql_connect.doConnect();
                    PreparedStatement stat7 = Sql_connect.getConnection().prepareStatement("UPDATE persoon SET Rating=Rating+? WHERE id_persoon=? LIMIT 1");
                    stat7.setDouble(1, berekenRating(newRating));
                    stat7.setInt(2, eliminatie_id);
                    stat7.executeUpdate();
                    
                    Sql_connect.doConnect();
                    PreparedStatement stat8 = Sql_connect.getConnection().prepareStatement("INSERT INTO rating (Id_persoon, Id_toernooi, behaalde_ronde, wijziging) VALUES (?, ?, ?, ?)");
                    stat8.setInt(1, eliminatie_id);
                    stat8.setInt(2, whereClaus);
                    stat8.setInt(3, rondeId);
                    stat8.setDouble(4, berekenRating(newRating));
                    stat8.executeUpdate();
                    
                } else if (spelersAanTafel == 1) {
                    final String eMessage = "Dit is de laatste speler van de tafel, deze gaat door naar de volgende ronde";
                    JOptionPane.showMessageDialog(rootPane, eMessage);

                    if (checkIfRondeIsOver()) {
                        eindigRonde();
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
            }

            rating--;
        }
    }

    private boolean checkIfRondeIsOver() {
        try {
            Sql_connect.doConnect();
            PreparedStatement stat4 = Sql_connect.getConnection().prepareStatement("select count(Id_persoon) as aantalPersonen from toernooideelnemer where Id_toernooi = ? AND Positie = 0 AND Isbetaald = 1");
            stat4.setInt(1, whereClaus);
            ResultSet result = stat4.executeQuery();

            while (result.next()) {
                int spelersInToernooi = result.getInt("aantalPersonen");
                //System.out.println(spelersInToernooi);

                if (spelersInToernooi == totaalAantalTafels) {
                    return true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void eindigRonde() {
        String eMessage = "Alle tafels zijn uitgespeeld";
        JOptionPane.showMessageDialog(rootPane, eMessage);
        int id = whereClaus;
        rondeId++;
        //System.out.println("ronde id "+rondeId);
        //System.out.println("whereClaus "+whereClaus);
        int inschrijvingen = totaalAantalTafels;

        Toernooi_vordering Toernooi_vordering = new Toernooi_vordering(id, rondeId, inschrijvingen, maxPertafel, rating, nextRoundFinal());
        Toernooi_vordering.setVisible(rootPaneCheckingEnabled);

        this.dispose();
    }

    
    /*
    private double berekenPrijsGeld(int plaats) {
        double inschrijfkosten = 0;
        try {
            Sql_connect.doConnect();
            PreparedStatement stat4 = Sql_connect.getConnection().prepareStatement("select Inschrijfkosten from toernooi where Id_toernooi = ? ");
            stat4.setInt(1, whereClaus);
            ResultSet result = stat4.executeQuery();
            while (result.next()) {
                inschrijfkosten = result.getDouble("Inschrijfkosten");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
        }
        double totaal = inschrijvingen * inschrijfkosten;

        if (plaats == 1) {
            return (0.4 * totaal);
        } else if (plaats == 2) {
            return (0.25 * totaal);
        } else if (plaats == 3) {
            return (0.1 * totaal);
        } else {
            return 0;
        }
    }

    /*
     private void krijgSpeler() {
     try {
     Sql_connect.doConnect();
     spelerListModel.removeAllElements();
     /* HIER WORDEN SPELERS RANDOM GEKOZEN EN KRIJGEN ZE EEN TAFEL ID MEE 
     PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement(""
     + "SELECT Id_persoon FROM toernooideelnemer "
     + "WHERE Id_toernooi = ? "
     + "AND Tafel_code is null "
     + "ORDER BY RAND() "
     + "LIMIT ?");
     stat3.setInt(1, whereClaus);
     stat3.setInt(2, maxPertafel);

     ResultSet result3 = stat3.executeQuery();

     while (result3.next()) {
     ModelItem item = new ModelItem();
     String random = result3.getString("Id_persoon");
     item.naam = random;

     ModelItem selectedItem = (ModelItem) TafelList.getSelectedValue();
     item.id = selectedItem.id;

     spelerListModel.addElement(item);
     // WERKENDE VERSIE //

     } // while (result3.next()) {

     /* HIER WORDT OPGEHAALD EN DAARNA GETOOND HOEVEEL SPELERS ER NOG NIET ZIJN INGESCHREVEN 
     String nogOver = "";
     PreparedStatement stat5 = Sql_connect.getConnection().prepareStatement(""
     + "SELECT count(*) as count FROM toernooideelnemer WHERE Tafel_code is null AND Id_toernooi = ?;");
     stat5.setInt(1, whereClaus);
     ResultSet result5 = stat5.executeQuery();
     while (result5.next()) {
     nogOver = result5.getString("count");
     }
     /* 
     ALS ER GEEN SPELERS MEER TE VERDELEN ZIJN KRIJG JE EEN DIALOOG SCHERM TE ZIEN
     HIERMEE WORDT JE DOORGESTUURD NAAR HET VOLGENDE SCHERM OM SPELERS TE KUNNEN UITSCHAKELEN
             
     if (Integer.parseInt(nogOver) == 0) {
     JOptionPane.showMessageDialog(rootPane, "Er vallen geen spelers meer te verdelen, u word door gestuurd naar het volgende scherm");

     Toernooi_eliminatie Toernooi_eliminatie = new Toernooi_eliminatie();
     Toernooi_eliminatie.setVisible(rootPaneCheckingEnabled);
     Toernooi_eliminatie.setLocationRelativeTo(null);
     this.dispose();

     } else {
     int teVerdelen = Integer.parseInt(nogOver);
     if (teVerdelen == 1) {
     MELDINGFIELD.setText("Er valt nog " + teVerdelen + " speler te verdelen, verdeel de rest en ga dan door");

     } else {
     MELDINGFIELD.setText("Er vallen nog " + teVerdelen + " spelers te verdelen, verdeel de rest en ga dan door");
     }

     ModelItem item = new ModelItem();
     ModelItem selectedItem = (ModelItem) TafelList.getSelectedValue();
     item.id = selectedItem.id;

     /* 
     BIJ DEZE FUNCTIE WORDT GEKEKEN OF ER AL MAX AANTAL SPELERS PER TAFEL ZITTEN, 
     ALS DIT HET GEVAL IS KRIJG JE EEN DIALOOG VENSTER MET KEUZE NOG MEER SPELERS TOE TE VOEGEN 
                 
     PreparedStatement stat6 = Sql_connect.getConnection().prepareStatement("SELECT count(*) FROM toernooideelnemer WHERE Tafel_code = ? AND Id_toernooi = ?;");
     stat6.setInt(1, selectedItem.id);
     stat6.setInt(2, whereClaus);
     ResultSet result6 = stat6.executeQuery();
     String aanTafel = "";
     while (result6.next()) {
     aanTafel = result6.getString("count(*)");
     }
     if (Integer.parseInt(aanTafel) == maxPertafel) {
     /* CHECK FOR DIALOOG VENSTER 
     if (JOptionPane.showConfirmDialog(null, "Er zitten al meer spelers aan deze tafel wilt u hier meer spelers aan toevoegen?", "WAARSCHUWING",
     JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
     /* HIER WORDT DE TAFEL_CODE GEUPDATE 
     for (int i = 0; i < SpelerList.getModel().getSize(); i++) {
     //ModelItem selectedItem = (ModelItem) TafelList.getSelectedValue();
     Object listItems = SpelerList.getModel().getElementAt(i);
     PreparedStatement stat4 = Sql_connect.getConnection().prepareStatement(""
     + "UPDATE toernooideelnemer "
     + "set Tafel_code = ?"
     + "WHERE Id_toernooi = ? "
     + "AND Id_persoon = ? "
     + "AND Tafel_code is null "
     + "LIMIT ?");
     stat4.setInt(1, selectedItem.id);
     stat4.setInt(2, whereClaus);
     stat4.setInt(3, Integer.parseInt(listItems.toString()));
     stat4.setInt(4, maxPertafel);

     stat4.executeUpdate();
     } // for (int i = 0; i < SpelerList.getModel().getSize(); i++) {
     } // yes option
     else {
     MELDINGFIELD.setText("U heeft geen extra spelers toegevoegd");
     }
     } // if (Integer.parseInt(aanTafel) == maxPertafel) {
     else {
     for (int i = 0; i < SpelerList.getModel().getSize(); i++) {
     //ModelItem selectedItem = (ModelItem) TafelList.getSelectedValue();
     Object listItems = SpelerList.getModel().getElementAt(i);
     PreparedStatement stat4 = Sql_connect.getConnection().prepareStatement(""
     + "UPDATE toernooideelnemer "
     + "set Tafel_code = ?"
     + "WHERE Id_toernooi = ? "
     + "AND Id_persoon = ? "
     + "AND Tafel_code is null "
     + "LIMIT ?");
     stat4.setInt(1, selectedItem.id);
     stat4.setInt(2, whereClaus);
     stat4.setInt(3, Integer.parseInt(listItems.toString()));
     stat4.setInt(4, maxPertafel);

     stat4.executeUpdate();
     } // for (int i = 0; i < SpelerList.getModel().getSize(); i++) {
     }
     } // else Integer.parseInt(nogOver) == 0)

     //vulLijst();
     } catch (Exception e) {
     ePopup(e);
     }
     }

     private void krijgTafels() {
     try {
     tafelListModel.removeAllElements();

     if ((aantalTafels == 0) & (overigeSpelers < maxPertafel)) {
     ModelItem item = new ModelItem();
     item.id = 1;
     item.naam = "finale tafel";
     tafelListModel.addElement(item);
     } else {
     for (int i1 = 1; i1 <= aantalTafels; i1++) {
     ModelItem item = new ModelItem();
     item.id = i1;
     item.naam = "tafel " + i1;
     tafelListModel.addElement(item);

     }
     }
     //vulLijst();
     } catch (Exception e) {
     ePopup(e);
     }
     }

     private void krijgRondes() {
     try {
     rondeListModel.removeAllElements();
     int Rondes = 1;
     double nieuwAantalTafels = aantalTafels;
     double nieuwMaxPerTafel = maxPertafel;
     while (nieuwAantalTafels > 1) {
     System.out.println("aantal " + aantalTafels);
     System.out.println("mpt " + maxPertafel);
     System.out.println("at" + nieuwAantalTafels);

     ModelItem item = new ModelItem();
     item.naam = "ronde " + Rondes;
     rondeListModel.addElement(item);
     nieuwAantalTafels = nieuwAantalTafels / nieuwMaxPerTafel;
     //nieuwAantalTafels++;
     System.out.println("at" + nieuwAantalTafels);
     Rondes++;
     }

     //vulLijst();
     } catch (Exception e) {
     ePopup(e);
     }
     }
     */
    private void ePopup(Exception e) {
        final String eMessage = "Er is iets fout gegaan, neem contact op met de aplicatiebouwer, geef deze foutmelding door: ";
        String error = eMessage + e;
        JOptionPane.showMessageDialog(rootPane, error);
    }

    /**
     * This method is called FROM within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TafelList = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        rondeLabel = new javax.swing.JLabel();
        MELDINGFIELD = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        SpelerList = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        idToernooiTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        naamToernooiTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Toernooi voortgang");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel1.setText("Tafel");

        TafelList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        TafelList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TafelListMouseClicked(evt);
            }
        });
        TafelList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                TafelListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(TafelList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(243, 243, 243))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel2.setText("Ronde:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(rondeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 446, Short.MAX_VALUE))
                    .addComponent(MELDINGFIELD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(rondeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)))
                    .addComponent(MELDINGFIELD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel3.setText("Speler");

        SpelerList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        SpelerList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SpelerListMouseClicked(evt);
            }
        });
        SpelerList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SpelerListKeyPressed(evt);
            }
        });
        SpelerList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                SpelerListValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(SpelerList);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Terug");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        idToernooiTxt.setEditable(false);
        idToernooiTxt.setText("1");

        jLabel4.setText("Toernooi id");

        naamToernooiTxt.setEditable(false);

        jLabel5.setText("Toernooi naam");

        jButton2.setText("elimineer");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton1)
                                        .addContainerGap())
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(idToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(naamToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel1, jPanel3});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(idToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(naamToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addGap(8, 8, 8)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jButton1)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        Toernooi_main Toernooien_main = new Toernooi_main();
        Toernooien_main.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void SpelerListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_SpelerListValueChanged
        // TODO add your handling code here:
        gegevensLijst();
    }//GEN-LAST:event_SpelerListValueChanged

    private void SpelerListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SpelerListMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_SpelerListMouseClicked

    private void TafelListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TafelListMouseClicked
        // TODO add your handling code here:
        //krijgSpeler();
    }//GEN-LAST:event_TafelListMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        elimineren();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void TafelListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_TafelListValueChanged
        weergevenSpelers();

    }//GEN-LAST:event_TafelListValueChanged

    private void SpelerListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SpelerListKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            elimineren();
        }
    }//GEN-LAST:event_SpelerListKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Ledeneditor Ledeneditor = new Ledeneditor();
            Ledeneditor.setVisible(rootPaneCheckingEnabled);
            this.dispose();
        }
    }//GEN-LAST:event_formKeyPressed

    private void gegevensLijst() {
        try {
            ModelItem selectedItem = (ModelItem) SpelerList.getSelectedValue();
            //MELDINGFIELD.setText("Speler heeft tafel_code: " + selectedItem);

        } catch (Exception e) {
            //MELDINGFIELD.setText("U heeft geen speler geselecteerd");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Toernooi_start.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Toernooi_start.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Toernooi_start.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Toernooi_start.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new Toernooi_start().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel MELDINGFIELD;
    private javax.swing.JList SpelerList;
    private javax.swing.JList TafelList;
    public javax.swing.JTextField idToernooiTxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JTextField naamToernooiTxt;
    private javax.swing.JLabel rondeLabel;
    // End of variables declaration//GEN-END:variables
}
