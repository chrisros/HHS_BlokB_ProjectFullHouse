/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lidselecter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
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

    /**
     * Creates new form Toernooi_start
     */
    public Toernooi_start() {
        initComponents();
        setLocationRelativeTo(null);
        TafelList.setModel(tafelListModel);
        SpelerList.setModel(spelerListModel);
        RondeList.setModel(rondeListModel);
        //vulLijst();

    }

    private void krijgSpeler() {
        try {
            Sql_connect.doConnect();
            int whereClaus = Integer.parseInt(idToernooiTxt.getText());
            spelerListModel.removeAllElements();

            PreparedStatement stat1 = Sql_connect.getConnection().prepareStatement("select count(Id_persoon) as inschrijvingen from toernooideelnemer where Id_toernooi = ?");
            stat1.setInt(1, whereClaus);
            ResultSet result1 = stat1.executeQuery();
            String inschr = "";
            while (result1.next()) {
                inschr = result1.getString("inschrijvingen");
                //System.out.println("aantal: " + inschr);
            }

            PreparedStatement stat2 = Sql_connect.getConnection().prepareStatement("select * from toernooi where Id_toernooi = ?");
            stat2.setInt(1, whereClaus);
            ResultSet result2 = stat2.executeQuery();
            String maxPT = "";
            while (result2.next()) {
                maxPT = result2.getString("Max_speler_per_tafel");
                //System.out.println("per tafel: " + maxPT);
            }

            int aantalTafels = Integer.parseInt(inschr) / Integer.parseInt(maxPT);
            //System.out.println("aantal tafels = " + aantalTafels);
            int spelers = (aantalTafels * Integer.parseInt(maxPT));
            int overigeSpelers = Integer.parseInt(inschr) - spelers;
            //System.out.println("overige spelers: " + overigeSpelers);

            PreparedStatement stat3 = Sql_connect.getConnection().prepareStatement(""
                    + "SELECT * FROM toernooideelnemer "
                    + "where Id_toernooi = ? "
                    + "AND (Tafel_code is null OR Tafel_code > ? )"
                    + "ORDER BY RAND() "
                    + "LIMIT ?");
            stat3.setInt(1, whereClaus);
            stat3.setInt(2, aantalTafels);
            stat3.setInt(3, Integer.parseInt(maxPT));

            ResultSet result3 = stat3.executeQuery();

            while (result3.next()) {
                ModelItem item = new ModelItem();
                String random = result3.getString("Id_persoon");
                item.naam = random;
                System.out.println("random: " + random);

                ModelItem selectedItem = (ModelItem) TafelList.getSelectedValue();
                item.id = selectedItem.id;

                spelerListModel.addElement(item);
                // WERKENDE VERSIE //

            } // while (result3.next()) {

            String nogOver = "";
            PreparedStatement stat5 = Sql_connect.getConnection().prepareStatement(""
                    + "select count(*) from toernooideelnemer where Tafel_code is null and Id_toernooi = ?;");
            stat5.setInt(1, whereClaus);
            ResultSet result5 = stat5.executeQuery();
            while (result5.next()) {
                nogOver = result5.getString("count(*)");
            }
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

                PreparedStatement stat6 = Sql_connect.getConnection().prepareStatement("select count(*) from toernooideelnemer where Tafel_code = ? and Id_toernooi = ?;");
                stat6.setInt(1, selectedItem.id);
                stat6.setInt(2, whereClaus);
                ResultSet result6 = stat6.executeQuery();
                String aanTafel = "";
                while (result6.next()) {
                    aanTafel = result6.getString("count(*)");
                }
                if (Integer.parseInt(aanTafel) == Integer.parseInt(maxPT)) {

                    if (JOptionPane.showConfirmDialog(null, "Er zitten al meer spelers aan deze tafel wilt u hier meer spelers aan toevoegen?", "WAARSCHUWING",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                        for (int i = 0; i < SpelerList.getModel().getSize(); i++) {
                            //ModelItem selectedItem = (ModelItem) TafelList.getSelectedValue();
                            Object listItems = SpelerList.getModel().getElementAt(i);
                            PreparedStatement stat4 = Sql_connect.getConnection().prepareStatement(""
                                    + "UPDATE toernooideelnemer "
                                    + "set Tafel_code = ? "
                                    + "where Id_toernooi = ? "
                                    + "AND Id_persoon = ? "
                                    + "AND (Tafel_code is null OR Tafel_code > ? )"
                                    + "LIMIT ?");
                            stat4.setInt(1, selectedItem.id);
                            stat4.setInt(2, whereClaus);
                            stat4.setInt(3, Integer.parseInt(listItems.toString()));
                            stat4.setInt(4, aantalTafels);
                            stat4.setInt(5, Integer.parseInt(maxPT));

                            stat4.executeUpdate();
                        } // for (int i = 0; i < SpelerList.getModel().getSize(); i++) {
                    } // yes option
                    else {
                        MELDINGFIELD.setText("U heeft geen extra spelers toegevoegd");
                    }
                } // if (Integer.parseInt(aanTafel) == Integer.parseInt(maxPT)) {
                else {
                    for (int i = 0; i < SpelerList.getModel().getSize(); i++) {
                        //ModelItem selectedItem = (ModelItem) TafelList.getSelectedValue();
                        Object listItems = SpelerList.getModel().getElementAt(i);
                        PreparedStatement stat4 = Sql_connect.getConnection().prepareStatement(""
                                + "UPDATE toernooideelnemer "
                                + "set Tafel_code = ? "
                                + "where Id_toernooi = ? "
                                + "AND Id_persoon = ? "
                                + "AND (Tafel_code is null OR Tafel_code > ? )"
                                + "LIMIT ?");
                        stat4.setInt(1, selectedItem.id);
                        stat4.setInt(2, whereClaus);
                        stat4.setInt(3, Integer.parseInt(listItems.toString()));
                        System.out.println("where id_persoon = " + Integer.parseInt(listItems.toString()));
                        stat4.setInt(4, aantalTafels);
                        stat4.setInt(5, Integer.parseInt(maxPT));

                        stat4.executeUpdate();
                    } // for (int i = 0; i < SpelerList.getModel().getSize(); i++) {
                }
            } // else Integer.parseInt(nogOver) == 0)

            //vulLijst();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void krijgTafels() {
        try {
            Sql_connect.doConnect();
            String wat = idToernooiTxt.getText();
            System.out.println("wat:" + wat);
            int whereClaus = Integer.parseInt(idToernooiTxt.getText());

            PreparedStatement stat1 = Sql_connect.getConnection().prepareStatement("select count(Id_persoon) as inschrijvingen from toernooideelnemer where Id_toernooi = ?");
            stat1.setInt(1, whereClaus);
            ResultSet result1 = stat1.executeQuery();

            PreparedStatement stat2 = Sql_connect.getConnection().prepareStatement("select * from toernooi where Id_toernooi = ?");
            stat2.setInt(1, whereClaus);
            ResultSet result2 = stat2.executeQuery();

            String inschr = "";
            String maxPT = "";

            while (result1.next()) {
                inschr = result1.getString("inschrijvingen");
                //System.out.println("aantal: " + inschr);

            }
            while (result2.next()) {
                maxPT = result2.getString("Max_speler_per_tafel");
                //System.out.println("per tafel: " + maxPT);
            }
            int aantalTafels = Integer.parseInt(inschr) / Integer.parseInt(maxPT);
            //System.out.println("aantal tafels = " + aantalTafels);
            int spelers = (aantalTafels * Integer.parseInt(maxPT));
            int overigeSpelers = Integer.parseInt(inschr) - spelers;
            //System.out.println("overige spelers: " + overigeSpelers);
            tafelListModel.removeAllElements();

            //Random rnd = new Random();
            if ((aantalTafels == 0) & (overigeSpelers < Integer.parseInt(maxPT))) {
                ModelItem item = new ModelItem();
                item.id = 1;
                item.naam = "finale tafel";
                tafelListModel.addElement(item);
            } else {
                for (int i1 = 1; i1 <= aantalTafels; i1++) {
                    System.out.println("tafel:" + i1);
                    ModelItem item = new ModelItem();
                    item.id = i1;
                    item.naam = "tafel " + i1;
                    tafelListModel.addElement(item);

                }
            }
            //vulLijst();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void krijgRondes() {
        try {
            Sql_connect.doConnect();
            int whereClaus = Integer.parseInt(idToernooiTxt.getText());

            PreparedStatement stat1 = Sql_connect.getConnection().prepareStatement("select count(Id_persoon) as inschrijvingen from toernooideelnemer where Id_toernooi = " + whereClaus);
            ResultSet result1 = stat1.executeQuery();

            PreparedStatement stat2 = Sql_connect.getConnection().prepareStatement("select * from toernooi where Id_toernooi = " + whereClaus);
            ResultSet result2 = stat2.executeQuery();

            String inschr = "";
            String maxPT = "";

            while (result1.next()) {
                inschr = result1.getString("inschrijvingen");
                System.out.println("aantal: " + inschr);

            }
            while (result2.next()) {
                maxPT = result2.getString("Max_speler_per_tafel");
                System.out.println("per tafel: " + maxPT);
            }
            int aantalTafels = Integer.parseInt(inschr) / Integer.parseInt(maxPT);
            System.out.println("aantal tafels = " + aantalTafels);
            int spelers = (aantalTafels * Integer.parseInt(maxPT));
            int overigeSpelers = Integer.parseInt(inschr) - spelers;
            System.out.println("overige spelers: " + overigeSpelers);

            rondeListModel.removeAllElements();
            //Random rnd = new Random();
            int helftTafels = aantalTafels / 2;
            int Rondes;
            if (helftTafels > (Integer.parseInt(maxPT) / 2)) {
                Rondes = helftTafels + 1;
            } else {
                Rondes = 2;
            }
            if (aantalTafels == 0) {
                ModelItem item = new ModelItem();
                item.naam = "finale ronde";
                rondeListModel.addElement(item);
            } else {
                for (int i1 = 1; i1 <= Rondes; i1++) {
                    System.out.println("tafel:" + i1);
                    ModelItem item = new ModelItem();
                    item.naam = "ronde " + i1;
                    rondeListModel.addElement(item);

                }
            }
            //vulLijst();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void vulLijst() {
        try {
            Sql_connect.doConnect();

            String prepSqlStatementVoorActer = "SELECT * FROM toernooi";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatementVoorActer);

            ResultSet result = stat.executeQuery();

            tafelListModel.removeAllElements();

//
//                //MELDINGFIELD.setText("Opvragen lijst gelukt!");
//
//            }
            for (int i = 0; i < 2; i++) {
                while (result.next()) {
                    ModelItem item = new ModelItem();
                    item.naam = "tafel " + i;
                    tafelListModel.addElement(item);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
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
                        .addComponent(jScrollPane1)
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
                        .addGap(0, 228, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
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

        /* 
         als hoeveelheid tafels meer is dan (helft van de aantal spelers per tafel) in ronde 1 dan ronde^n
        
        
        
         */
    }//GEN-LAST:event_vulRondeBtnActionPerformed

    private void RondeListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_RondeListValueChanged
        // TODO add your handling code here:
        krijgTafels();

    }//GEN-LAST:event_RondeListValueChanged

    private void SpelerListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_SpelerListValueChanged
        // TODO add your handling code here:
        System.out.println("klik");
        gegevensLijst();
    }//GEN-LAST:event_SpelerListValueChanged

    private void SpelerListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SpelerListMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_SpelerListMouseClicked

    private void TafelListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TafelListMouseClicked
        // TODO add your handling code here:
        krijgSpeler();
    }//GEN-LAST:event_TafelListMouseClicked

    private void gegevensLijst() {
        try {
            ModelItem selectedItem = (ModelItem) SpelerList.getSelectedValue();
            System.out.println("id: " + selectedItem.id);

        } catch (Exception e) {
            //System.out.println("niks geselecteerd");
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
                new Toernooi_start().setVisible(true);
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
