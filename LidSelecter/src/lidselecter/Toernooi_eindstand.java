/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lidselecter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Chris
 */
public class Toernooi_eindstand extends javax.swing.JFrame {

    int id;
    int inschrijvingen;
    double inschrijfkosten = 0;
    DefaultListModel SpelersToernooiModel = new DefaultListModel();
    /**
     * Creates new form Toernooi_eindstand
     */
    public Toernooi_eindstand(int id) {
        initComponents();
        this.id = id;
        getInschrijfingen();
        getNaam();
        SpelersToernooi.setModel(SpelersToernooiModel);
        getLijst();
        winnaarsWeergeven();
    }
    private void getNaam()
    {
        try {
            /* OPHALEN NAAM TOERNOOI */
            PreparedStatement stat4 = Sql_connect.getConnection().prepareStatement("SELECT Naam FROM toernooi WHERE Id_toernooi = ?");
            stat4.setInt(1, id);
            ResultSet result4 = stat4.executeQuery();

            while (result4.next()) {
                String Naam = result4.getString("Naam");
                Titel.setText(Naam);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_eindstand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void getLijst() {
        try {
            Sql_connect.doConnect();
            PreparedStatement stat2 = Sql_connect.getConnection().prepareStatement("SELECT P.Voornaam, P.Achternaam, P.Id_persoon, T.Positie FROM persoon P"
                    + " Join toernooideelnemer T on T.Id_persoon = P.Id_persoon"
                    + " WHERE T.Id_toernooi = ? ORDER BY T.Positie ASC");
            stat2.setInt(1, id);
            ResultSet result2 = stat2.executeQuery();
            SpelersToernooiModel.removeAllElements();
            while (result2.next()) {
                ModelItem item = new ModelItem();
                item.id = result2.getInt("P.Id_persoon");
                item.voornaam = result2.getString("P.Voornaam");
                item.achternaam = result2.getString("P.Achternaam");
                item.eindPositie = result2.getInt("T.Positie");
                SpelersToernooiModel.addElement(item);
                
            }

        } catch (Exception e) {
            //ePopup(e);
        }
    }
    private void updateRatring(int plaats, int id)
    {
        int rating = (15 /plaats);
        try {
            
            Sql_connect.doConnect();
            PreparedStatement stat7 = Sql_connect.getConnection().prepareStatement("UPDATE persoon SET Rating=Rating+? WHERE id_persoon=? LIMIT 1");
            stat7.setDouble(1, rating);
            stat7.setInt(2, id);
            stat7.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_eindstand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getInschrijfingen()
    {
        try {
            Sql_connect.doConnect();
            /* OPHALEN TOTAAL SPELERS DIE WERKELIJK ZIJN INGESCHREVEN */
            PreparedStatement stat1 = Sql_connect.getConnection().prepareStatement("SELECT count(Id_persoon) as inschrijvingen FROM toernooideelnemer WHERE Id_toernooi = ? AND isBetaald = 1");
            stat1.setInt(1, id);
            ResultSet result1 = stat1.executeQuery();

            while (result1.next()) {
                inschrijvingen = result1.getInt("inschrijvingen");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_eindstand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Sql_connect.doConnect();
            PreparedStatement stat4 = Sql_connect.getConnection().prepareStatement("select Inschrijfkosten from toernooi where Id_toernooi = ? ");
            stat4.setInt(1, id);
            ResultSet result = stat4.executeQuery();
            while (result.next()) {
                inschrijfkosten = result.getDouble("Inschrijfkosten");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_vordering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        private void winnaarsWeergeven()
        {
            int persoonId = 0;
        try {
            Sql_connect.doConnect();
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT P.Voornaam, P.Achternaam, P.Id_persoon FROM persoon P"
                    + " Join toernooideelnemer T on T.Id_persoon = P.Id_persoon"
                    + " WHERE T.Id_toernooi = ? AND T.Positie = 1 LIMIT 1");
            stat.setInt(1, id);
            ResultSet result = stat.executeQuery();
            
            while (result.next()) {
                String roepnaam = result.getString("voornaam");
                String achternaam = result.getString("achternaam");
                persoonId = result.getInt("Id_persoon");
                String naam = roepnaam+" "+achternaam;
                
                 String prijs = "€"+berekenPrijsGeld(1); 
                 naam1.setText(naam);
                 prijs1.setText(prijs);
                }
            updateRatring(1, persoonId);
            
            Sql_connect.doConnect();
                    PreparedStatement stat8 = Sql_connect.getConnection().prepareStatement("INSERT INTO rating (Id_persoon, Id_toernooi, behaalde_ronde, wijziging) VALUES (?, ?, ?, ?)");
                    stat8.setInt(1, persoonId);
                    stat8.setInt(2, id);
                    stat8.setString(3, "Finale");
                    stat8.setDouble(4, 15);
                    stat8.executeUpdate();
                    
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_eindstand.class.getName()).log(Level.SEVERE, null, ex);
        }
                try {
            Sql_connect.doConnect();
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT P.Voornaam, P.Achternaam, P.Id_persoon FROM persoon P"
                    + " Join toernooideelnemer T on T.Id_persoon = P.Id_persoon"
                    + " WHERE T.Id_toernooi = ? AND T.Positie = 2 LIMIT 1");
            stat.setInt(1, id);
            ResultSet result = stat.executeQuery();
            
            while (result.next()) {
                String roepnaam = result.getString("voornaam");
                String achternaam = result.getString("achternaam");
                persoonId = result.getInt("Id_persoon");
                String naam = roepnaam+" "+achternaam;
                
                 String prijs = "€"+berekenPrijsGeld(2); 
                 naam2.setText(naam);
                 prijs2.setText(prijs);
                }
            updateRatring(2, persoonId);
            
            Sql_connect.doConnect();
                    PreparedStatement stat8 = Sql_connect.getConnection().prepareStatement("INSERT INTO rating (Id_persoon, Id_toernooi, behaalde_ronde, wijziging) VALUES (?, ?, ?, ?)");
                    stat8.setInt(1, persoonId);
                    stat8.setInt(2, id);
                    stat8.setString(3, "Finale");
                    stat8.setDouble(4, 10);
                    stat8.executeUpdate();
                    
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_eindstand.class.getName()).log(Level.SEVERE, null, ex);
        }
                        try {
            Sql_connect.doConnect();
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT P.Voornaam, P.Achternaam, P.Id_persoon FROM persoon P"
                    + " Join toernooideelnemer T on T.Id_persoon = P.Id_persoon"
                    + " WHERE T.Id_toernooi = ? AND T.Positie = 3 LIMIT 1");
            stat.setInt(1, id);
            ResultSet result = stat.executeQuery();
            
            while (result.next()) {
                String roepnaam = result.getString("voornaam");
                String achternaam = result.getString("achternaam");
                persoonId = result.getInt("Id_persoon");                
                String naam = roepnaam+" "+achternaam;
                
                 String prijs = "€"+berekenPrijsGeld(3); 
                 naam3.setText(naam);
                 prijs3.setText(prijs);
                }
            updateRatring(3, persoonId);
            
            Sql_connect.doConnect();
                    PreparedStatement stat8 = Sql_connect.getConnection().prepareStatement("INSERT INTO rating (Id_persoon, Id_toernooi, behaalde_ronde, wijziging) VALUES (?, ?, ?, ?)");
                    stat8.setInt(1, persoonId);
                    stat8.setInt(2, id);
                    stat8.setString(3, "Finale");
                    stat8.setDouble(4, 5);
                    stat8.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_eindstand.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    
        private double berekenPrijsGeld(int plaats) {
        
        double totaal = inschrijvingen * inschrijfkosten;

        if (plaats == 1) {
            double roundOff = Math.round((0.4 * totaal) * 100.0) / 100.0;
            return roundOff;
        } else if (plaats == 2) {
            double roundOff = Math.round((0.25 * totaal) * 100.0) / 100.0;
            return roundOff;
        } else if (plaats == 3) {
            double roundOff = Math.round((0.10 * totaal) * 100.0) / 100.0;
            return roundOff;
        } else {
            return 0;
        }
    }
        
        private void close() {
        this.dispose();
        Main menu = new Main();
        menu.setVisible(rootPaneCheckingEnabled);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        naam1 = new javax.swing.JLabel();
        naam2 = new javax.swing.JLabel();
        naam3 = new javax.swing.JLabel();
        prijs1 = new javax.swing.JLabel();
        prijs2 = new javax.swing.JLabel();
        prijs3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        Titel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        SpelersToernooi = new javax.swing.JList();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Uitslag toernooi");
        setPreferredSize(new java.awt.Dimension(584, 446));

        jPanel1.setPreferredSize(new java.awt.Dimension(584, 446));
        jPanel1.setRequestFocusEnabled(false);

        jLabel1.setText("1e Plaats:");

        jLabel2.setText("2e Plaats:");

        jLabel3.setText("3e Plaats:");

        jLabel4.setText("Winnaars:");

        naam1.setText("naam1");

        naam2.setText("naam2");

        naam3.setText("naam3");

        prijs1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        prijs1.setText("prijs1");

        prijs2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        prijs2.setText("prijs2");

        prijs3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        prijs3.setText("prijs3");

        jLabel6.setText("Prijs:");

        Titel.setText("Naam toernooi uitslag");

        SpelersToernooi.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(SpelersToernooi);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(naam1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(naam2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(naam3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(prijs2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(prijs3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(prijs1))
                            .addComponent(jLabel6))
                        .addGap(195, 195, 195))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Titel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(Titel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(naam1)
                    .addComponent(prijs1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(naam2)
                    .addComponent(prijs2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(naam3)
                    .addComponent(prijs3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton1.setText("Terug");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(72, 72, 72)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        close();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Toernooi_eindstand.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Toernooi_eindstand.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Toernooi_eindstand.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Toernooi_eindstand.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new Toernooi_eindstand(2).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList SpelersToernooi;
    private javax.swing.JLabel Titel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel naam1;
    private javax.swing.JLabel naam2;
    private javax.swing.JLabel naam3;
    private javax.swing.JLabel prijs1;
    private javax.swing.JLabel prijs2;
    private javax.swing.JLabel prijs3;
    // End of variables declaration//GEN-END:variables
}
