/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lidselecter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Aaik
 */
public class Tutor_masterclass extends javax.swing.JFrame {

    private final DefaultTableModel table = new DefaultTableModel();
    DefaultListModel jListModel = new DefaultListModel();
    //private String zoekVeld = "";

    /**
     * Creates new form Masterclass_inschrijven
     */
    public Tutor_masterclass() {
        initComponents();
        setLocationRelativeTo(null);
        inschrijfList.setModel(jListModel);
        vulLijst();
        masterclassTabel.setModel(table);
        String[] Kolomnaam = {"Masterclass id", "Naam", "Datum", "Plaats code", "Max spelers"};
        table.setColumnIdentifiers(Kolomnaam);
        table.setRowCount(0);
        table.setColumnCount(5);

        toernooiVullen();
    }

    private void toernooiVullen() {
        // TODO add your handling code here:
        Sql_connect.doConnect();
        // declareer de variable voor in de rs
        String id;
        String naam;
        String datum;
        String plaats;
        String Max_inschrijvingen_M;

        try {
            // connect 
            Sql_connect.doConnect();
            // statement maken
            String prepSqlStatement = "select * from masterclass where Id_masterclass NOT IN (select Id_masterclass FROM masterClassGever)";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();

            // rijen
            int i = 0;

            while (result.next()) {
                i++;

            }
            table.setRowCount(i);
            result.beforeFirst();

            int d = 0;
            while (result.next()) {
                //Stop de variable in een rs
                id = result.getString("Id_masterclass");
                naam = result.getString("Naam_masterclass");
                datum = result.getString("Datum");
                plaats = result.getString("Id_locatie");
                Max_inschrijvingen_M = result.getString("Max_inschrijvingen_M");

                // vul vervolgens in de tabel de waardes in als volgt: resultset, aantal, plaats
                table.setValueAt(id, d, 0);
                table.setValueAt(naam, d, 1);
                table.setValueAt(plaats, d, 2);
                table.setValueAt(Max_inschrijvingen_M, d, 3);
                table.setValueAt(datum, d, 4);
                // verhoog aantal totdat alles was je hebt opgevraagd is geweest
                d++;

            }

            result.last();

            result.close();
            stat.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }

    private void gegevensOphalen() {

        try {
            int row = masterclassTabel.getSelectedRow();

            String Table_click = masterclassTabel.getModel().getValueAt(row, 0).toString();
            Sql_connect.doConnect();

            // statement maken
            String prepSqlStatement = "select * from masterclass where Id_masterclass = '" + Table_click + "'";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();

            if (result.next()) {

                String add6 = result.getString("Id_masterclass");
                masterclass_IdTxt.setText(add6);
            }

        } catch (Exception e) {
            //ePopup(e);
        }
    }

    private void ePopup(Exception e) {
        final String eMessage = "Er is iets fout gegaan, neem contact op met de aplicatiebouwer, geef deze foutmelding door: ";
        String error = eMessage + e;
        JOptionPane.showMessageDialog(rootPane, error);
    }

    private void inschrijvenMasterclass() {

        // krijg de tekst uit de velden
        int idMasterclass = Integer.parseInt(masterclass_IdTxt.getText());
        int idSpeler = Integer.parseInt(speler_codeTxt.getText());

        try {
            //maak een connectie
            Sql_connect.doConnect();

            String prepSqlStatement1 = "select Id_locatie from masterclass where Id_masterclass = ?";
            PreparedStatement stat1 = Sql_connect.getConnection().prepareStatement(prepSqlStatement1);
            stat1.setInt(1, idMasterclass);
            ResultSet result1 = stat1.executeQuery();
            int locatieGegevens = 0;
            while (result1.next()) {
                locatieGegevens = result1.getInt("Id_locatie");
            }

            // sql prepair statement
            String prepSqlStatement
                    = "INSERT INTO masterClassGever "
                    + "(Id_masterclass, Id_tutor, Id_locatie)"
                    + "VALUES (?, ?, ?)";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setInt(1, idMasterclass);
            stat.setInt(2, idSpeler);
            stat.setInt(3, locatieGegevens);

            stat.executeUpdate();
            // melding
            MELDINGFIELD.setText("Ingeschreven voor masterclass: " + idMasterclass + " met tutor code: " + idSpeler);
            toernooiVullen();

        } catch (Exception e) {
            ePopup(e);
            MELDINGFIELD.setText("Inschrijven voor masterclass: " + idMasterclass + " is mislukt");
        }
    }

    public String removeLastChar(String s) {
        if (s != null && s.length() > 0) {
            if (s.substring(s.length() - 1).equals(" ")) {
                return s.substring(0, s.length() - 1);
            } else {
                return s;
            }
        }
        return s;
    }

    // Hier vul je de lijst met de toernooi namen
    private void vulLijst() {
        try {
            Sql_connect.doConnect();
            String zoekVeld = removeLastChar(zoekTxt.getText());

            String[] parts = zoekVeld.split(" ");
            int partsLength = parts.length;

            if (partsLength == 2) {

                String voornaam = parts[0];
                String achternaam = parts[1];

                String prepSqlStatementVoorActer = "select Voornaam, Achternaam, Id_persoon from persoon WHERE Voornaam like ? AND Achternaam like ? AND Id_persoon IN (select Id_persoon from tutor);";
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatementVoorActer);
                stat.setString(1, "%" + voornaam + "%");
                stat.setString(2, "%" + achternaam + "%");

                ResultSet result = stat.executeQuery();

                jListModel.removeAllElements();

                while (result.next()) {
                    ModelItem item = new ModelItem();
                    item.id = result.getInt("Id_persoon");
                    item.voornaam = result.getString("voornaam");
                    item.achternaam = result.getString("achternaam");
                    jListModel.addElement(item);

                    MELDINGFIELD.setText("Opvragen lijst gelukt!");

                }

            } else {
                String prepSqlStatement = "select Voornaam, Achternaam, Id_persoon from persoon where Voornaam like ? AND Id_persoon IN (select Id_persoon from tutor);";
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
                stat.setString(1, "%" + zoekVeld + "%");
                ResultSet result = stat.executeQuery();

                jListModel.removeAllElements();
                while (result.next()) {
                    ModelItem item = new ModelItem();
                    item.id = result.getInt("Id_persoon");
                    item.voornaam = result.getString("voornaam");
                    item.achternaam = result.getString("achternaam");
                    jListModel.addElement(item);

                    MELDINGFIELD.setText("Opvragen lijst gelukt!");
                }
            }
        } catch (Exception e) {
            ePopup(e);
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

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        masterclassTabel = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        speler_codeTxt = new javax.swing.JTextField();
        masterclass_IdTxt = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        MELDINGFIELD = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        inschrijfList = new javax.swing.JList();
        zoekTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Terug");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        masterclassTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Masterclass Id", "Datum", "Plaats", "Max Spelers", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        masterclassTabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                masterclassTabelMouseClicked(evt);
            }
        });
        masterclassTabel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                masterclassTabelFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(masterclassTabel);

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel1.setText("Klik masterclass en tutor aan en schrijf in");

        jLabel2.setText("Masterclass id");

        jLabel3.setText("Tutor Code");

        speler_codeTxt.setEditable(false);

        masterclass_IdTxt.setEditable(false);

        jButton2.setText("Inschrijven");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        MELDINGFIELD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        inschrijfList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        inschrijfList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inschrijfListMouseClicked(evt);
            }
        });
        inschrijfList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                inschrijfListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(inschrijfList);

        zoekTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zoekTxtKeyReleased(evt);
            }
        });

        jLabel4.setText("Zoeken");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MELDINGFIELD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel2))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(speler_codeTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                            .addComponent(masterclass_IdTxt)))
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(masterclass_IdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(speler_codeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Masterclass_main Masterclass_main = new Masterclass_main();
        Masterclass_main.setVisible(rootPaneCheckingEnabled);
        this.dispose();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void masterclassTabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_masterclassTabelMouseClicked
        // TODO add your handling code here:
        gegevensOphalen();
    }//GEN-LAST:event_masterclassTabelMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        inschrijvenMasterclass();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void masterclassTabelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_masterclassTabelFocusGained
        // TODO add your handling code here:
        gegevensOphalen();
    }//GEN-LAST:event_masterclassTabelFocusGained

    private void zoekTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zoekTxtKeyReleased
        // TODO add your handling code here:

        vulLijst();
    }//GEN-LAST:event_zoekTxtKeyReleased

    private void inschrijfListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_inschrijfListValueChanged
        // TODO add your handling code here:
        gegevensLijst();
    }//GEN-LAST:event_inschrijfListValueChanged

    private void inschrijfListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inschrijfListMouseClicked
        // TODO add your handling code here:
        gegevensLijst();
    }//GEN-LAST:event_inschrijfListMouseClicked
// hier wordt tijdens het klikken de jTextfields gevuld met de gegevens uit de database

    private void gegevensLijst() {
        try {
            if (inschrijfList.getSelectedValue() == null) {
                MELDINGFIELD.setText("Niets Geselecteerd.");
            } else {
                ModelItem selectedItem = (ModelItem) inschrijfList.getSelectedValue();
                speler_codeTxt.setText(Integer.toString(selectedItem.id));

                MELDINGFIELD.setText("Opvraag ID gelukt!");
            }
        } catch (Exception e) {
            MELDINGFIELD.setText("Geen naam geselecteerd!");
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
            java.util.logging.Logger.getLogger(Tutor_masterclass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tutor_masterclass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tutor_masterclass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tutor_masterclass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tutor_masterclass().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel MELDINGFIELD;
    private javax.swing.JList inschrijfList;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable masterclassTabel;
    private javax.swing.JTextField masterclass_IdTxt;
    private javax.swing.JTextField speler_codeTxt;
    private javax.swing.JTextField zoekTxt;
    // End of variables declaration//GEN-END:variables
}
