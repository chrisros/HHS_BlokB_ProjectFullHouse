/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lidselecter;

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
 * @author Aaik
 */
public class Toernooi_start extends javax.swing.JFrame {

    DefaultListModel tafelListModel = new DefaultListModel();
    DefaultListModel spelerListModel = new DefaultListModel();
    DefaultListModel rondeListModel = new DefaultListModel();

    int whereClaus;
    int inschrijvingen;
    int maxPertafel;
    int aantalTafels;
    int totaalAantalTafels;
    int spelers;
    int overigeSpelers;
    int bonusTafel1 = 0;
    int bonusTafel2 = 0;
    int rondeId = 1;
    final int minSpelersPerTafel = 4;
    final int fiches = 1000;
    
    /**
     * Creates new form Toernooi_start
     * @param id = currently selected toernooi
     */
    public Toernooi_start(int id) {
        whereClaus=id;
        initComponents();
        setLocationRelativeTo(null);
        TafelList.setModel(tafelListModel);
        SpelerList.setModel(spelerListModel);
        RondeList.setModel(rondeListModel);
        toernooiGegevens();
        toevoegenSpelers();
        //vulLijst();

    }

    private void elimineerSpeler()
    {
        
    }

    
    private void toernooiGegevens() {
        try {
            Sql_connect.doConnect();
            /* OPHALEN TOTAAL SPELERS DIE WERKELIJK ZIJN INGESCHREVEN */
            PreparedStatement stat1 = Sql_connect.getConnection().prepareStatement("SELECT count(Id_persoon) as inschrijvingen FROM toernooideelnemer WHERE Id_toernooi = ? AND isBetaald = 1");
            stat1.setInt(1, whereClaus);
            ResultSet result1 = stat1.executeQuery();

            while (result1.next()) {
                inschrijvingen = result1.getInt("inschrijvingen");
            }

            /* OPHALEN SPELERS PER TAFEL */
            PreparedStatement stat2 = Sql_connect.getConnection().prepareStatement("SELECT Max_speler_per_tafel FROM toernooi WHERE Id_toernooi = ?");
            stat2.setInt(1, whereClaus);
            ResultSet result2 = stat2.executeQuery();

            while (result2.next()) {
                maxPertafel = result2.getInt("Max_speler_per_tafel");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
        }
        totaalAantalTafels=aantalTafels;
        aantalTafels = inschrijvingen / maxPertafel;
        spelers = (aantalTafels * maxPertafel);
        overigeSpelers = inschrijvingen - spelers;
        if (overigeSpelers > 0) {
            if (overigeSpelers < minSpelersPerTafel) {
                int overschot = maxPertafel + overigeSpelers;
                    //aantalTafels++;
                    bonusTafel1=(overschot/2);
                    bonusTafel2 = overschot-bonusTafel1;
                    totaalAantalTafels=totaalAantalTafels+2;
            } else
            {
                //aantalTafels++;
                bonusTafel1 = overigeSpelers;
                bonusTafel2 = 0;
                totaalAantalTafels++;
            }
        }
        
        System.out.println("aantalTafels"+aantalTafels);
        System.out.println("bonusTafel1"+bonusTafel1);
        System.out.println("bonusTafel2"+bonusTafel2);
        toevoegenTafel();
    }

    
    private void toevoegenTafel()
    {
        int count = 0;
        while(count<aantalTafels)
        {
            try {
                Sql_connect.doConnect();
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("INSERT INTO tafel (spelerStart, spelerAanwezig, toernooi) VALUES (?, ?, ?)");
                stat.setInt(1, maxPertafel);
                stat.setInt(2, maxPertafel);
                stat.setInt(3, whereClaus);
                stat.executeUpdate();
                count++;
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        if(bonusTafel1 !=0)
                {
            try {
                Sql_connect.doConnect();
                PreparedStatement stat2 = Sql_connect.getConnection().prepareStatement("INSERT INTO tafel (spelerStart, spelerAanwezig, toernooi) VALUES (?, ?, ?)");
                stat2.setInt(1, bonusTafel1);
                stat2.setInt(2, bonusTafel1);
                stat2.setInt(3, whereClaus);
                stat2.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
            }

            }
            if(bonusTafel2 !=0)
                {
            try {
                Sql_connect.doConnect();
                PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement("INSERT INTO tafel (spelerStart, spelerAanwezig, toernooi) VALUES (?, ?, ?)");
                stat3.setInt(1, bonusTafel2);
                stat3.setInt(2, bonusTafel2);
                stat3.setInt(3, whereClaus);
                stat3.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    }
    
    private void toevoegenSpelers()
    {
        
        int tafelCount = 1;
        while (tafelCount<=aantalTafels)
        {
        try {
                Sql_connect.doConnect();
                PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement("UPDATE toernooideelnemer SET Tafel_code=?, Fiches=? WHERE Tafel_code is null AND Id_toernooi = ? LIMIT ?");
                stat3.setInt(1, tafelCount);
                stat3.setInt(2, fiches);
                stat3.setInt(3, whereClaus);
                stat3.setInt(4, maxPertafel);
                stat3.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
            }
            tafelCount++;
            
    } if(bonusTafel1!=0)
    {
        
        {
        try {
                Sql_connect.doConnect();
                PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement("UPDATE toernooideelnemer SET Tafel_code=?, Fiches=? WHERE Tafel_code is null AND Id_toernooi = ? LIMIT ?");
                stat3.setInt(1, tafelCount);
                stat3.setInt(2, fiches);
                stat3.setInt(3, whereClaus);
                stat3.setInt(4, bonusTafel1);
                stat3.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
            }
                tafelCount++;
        
    } if(bonusTafel2!=0)
    {
            try {
                Sql_connect.doConnect();
                PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement("UPDATE toernooideelnemer SET Tafel_code=?, Fiches=? WHERE Tafel_code is null AND Id_toernooi = ? LIMIT ?");
                stat3.setInt(1, tafelCount);
                stat3.setInt(2, fiches);
                stat3.setInt(3, whereClaus);
                stat3.setInt(4, bonusTafel2);
                stat3.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_start.class.getName()).log(Level.SEVERE, null, ex);
            }
                tafelCount++;
        } 
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
    }
    private boolean nextRoundFinal()
    {
        if(totaalAantalTafels>maxPertafel){return true;}
        else{return false;}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void krijgSpeler() {
        try {
            Sql_connect.doConnect();
            spelerListModel.removeAllElements();
            /* HIER WORDEN SPELERS RANDOM GEKOZEN EN KRIJGEN ZE EEN TAFEL ID MEE */
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

            /* HIER WORDT OPGEHAALD EN DAARNA GETOOND HOEVEEL SPELERS ER NOG NIET ZIJN INGESCHREVEN */
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
             */
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
                 */
                PreparedStatement stat6 = Sql_connect.getConnection().prepareStatement("SELECT count(*) FROM toernooideelnemer WHERE Tafel_code = ? AND Id_toernooi = ?;");
                stat6.setInt(1, selectedItem.id);
                stat6.setInt(2, whereClaus);
                ResultSet result6 = stat6.executeQuery();
                String aanTafel = "";
                while (result6.next()) {
                    aanTafel = result6.getString("count(*)");
                }
                if (Integer.parseInt(aanTafel) == maxPertafel) {
                    /* CHECK FOR DIALOOG VENSTER */
                    if (JOptionPane.showConfirmDialog(null, "Er zitten al meer spelers aan deze tafel wilt u hier meer spelers aan toevoegen?", "WAARSCHUWING",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        /* HIER WORDT DE TAFEL_CODE GEUPDATE */
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
            while(nieuwAantalTafels>1)
            {
                System.out.println("aantal " + aantalTafels);
                System.out.println("mpt " + maxPertafel);
                System.out.println("at" + nieuwAantalTafels);                
                
                ModelItem item = new ModelItem();
                item.naam = "ronde " + Rondes;
                rondeListModel.addElement(item);
                nieuwAantalTafels = nieuwAantalTafels/nieuwMaxPerTafel;
                //nieuwAantalTafels++;
                System.out.println("at" + nieuwAantalTafels);  
                Rondes++;
            }
            
                    

                
            
            //vulLijst();
        } catch (Exception e) {
            ePopup(e);
        }
    }

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
        jScrollPane2 = new javax.swing.JScrollPane();
        RondeList = new javax.swing.JList();
        vulRondeBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        SpelerList = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        idToernooiTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        naamToernooiTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        MELDINGFIELD = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Toernooi voortgang");

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
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addGap(40, 40, 40))
        );

        jLabel2.setText("Ronde");

        RondeList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        RondeList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                RondeListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(RondeList);

        vulRondeBtn.setText("vul Rondes");
        vulRondeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vulRondeBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(vulRondeBtn))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(vulRondeBtn)
                .addContainerGap())
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
        SpelerList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                SpelerListValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(SpelerList);

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jButton2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jButton2))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(idToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(naamToernooiTxt))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(MELDINGFIELD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel1, jPanel2, jPanel3});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(naamToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(idToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButton1))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel1, jPanel2, jPanel3});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        Toernooi_main Toernooien_main = new Toernooi_main();
        Toernooien_main.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void vulRondeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vulRondeBtnActionPerformed
        // TODO add your handling code here:
        krijgRondes();
    }//GEN-LAST:event_vulRondeBtnActionPerformed

    private void RondeListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_RondeListValueChanged
        // TODO add your handling code here:
        krijgTafels();
    }//GEN-LAST:event_RondeListValueChanged

    private void SpelerListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_SpelerListValueChanged
        // TODO add your handling code here:
        gegevensLijst();
    }//GEN-LAST:event_SpelerListValueChanged

    private void SpelerListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SpelerListMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_SpelerListMouseClicked

    private void TafelListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TafelListMouseClicked
        // TODO add your handling code here:
        krijgSpeler();
    }//GEN-LAST:event_TafelListMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        elimineerSpeler();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void gegevensLijst() {
        try {
            ModelItem selectedItem = (ModelItem) SpelerList.getSelectedValue();
            MELDINGFIELD.setText("Speler heeft tafel_code: " + selectedItem);

        } catch (Exception e) {
            MELDINGFIELD.setText("U heeft geen speler geselecteerd");
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
            java.util.logging.Logger.getLogger(Toernooi_start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Toernooi_start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Toernooi_start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Toernooi_start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JList RondeList;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JTextField naamToernooiTxt;
    private javax.swing.JButton vulRondeBtn;
    // End of variables declaration//GEN-END:variables
}
