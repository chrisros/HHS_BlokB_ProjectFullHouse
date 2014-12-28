/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lidselecter;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Aaik
 */
public class Toevoegen_toernooi extends javax.swing.JFrame {

    private boolean fieldsOk;

    /**
     * Creates new form Toevoegen_toernooi
     */
    public Toevoegen_toernooi() {
        initComponents();
        nieuwToernooiId();
    }

    private int nieuwToernooiId() {
        // standaart waarde nieuw id
        int new_toernooiID = 0;

        try {
            // maak connectie
            Sql_connect.doConnect();
            // statement
            String prepSqlStatement = "select MAX(Id_toernooi) AS biggest from toernooi";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();
            while (result.next()) {
                new_toernooiID = result.getInt("biggest");
            }
            new_toernooiID = new_toernooiID + 1;
            //return new_toernooiID;
            idToernooiTxt.setText(Integer.toString(new_toernooiID));

        } catch (Exception ex) {
            Logger.getLogger(Ledeneditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new_toernooiID;
    }

    private void addToernooi() {
        try {
            //maak een connectie
            Sql_connect.doConnect();

            // krijg niew ID
            int code = nieuwToernooiId();
            // krijg de tekst uit de velden
            String Id_toernooi = idToernooiTxt.getText();
            String naam = naamToernooiTxt.getText();
            String Datum = datumTxt.getText();
            String Inschrijfkosten = inschrijfKostenTxt.getText();
            String Max_inschrijvingen_T = maxInschrijfTxt.getText();
            String Max_speler_per_tafel = maxSpelersTafelTxt.getText();
            String Kaartspel_code = codeKaartspelTxt.getText();
            String Locatie_code = codeLocatieTxt.getText();
            String Kaartspeltype = typeKaartspelTxt.getText();

            // sql prepair statement
            String prepSqlStatement = "INSERT INTO toernooi (Id_toernooi, Naam, Datum, "
                    + "Inschrijfkosten, Max_inschrijvingen_T, "
                    + "Max_speler_per_tafel, Kaartspel_code, "
                    + "Locatie_code, Kaartspeltype) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, Id_toernooi);
            stat.setString(2, naam);
            stat.setString(3, Datum);
            stat.setString(4, Inschrijfkosten);
            stat.setString(5, Max_inschrijvingen_T);
            stat.setString(6, Max_speler_per_tafel);
            stat.setString(7, Kaartspel_code);
            stat.setString(8, Locatie_code);
            stat.setString(9, Kaartspeltype);
            stat.executeUpdate();
            // melding
            MELDINGFIELD.setText("Toevoegen toernooi gelukt!");

        } catch (Exception e) {
            ePopup(e);
        }
    }

    private void checkStringField(JTextField field, int minLength, int maxLength) {

        if (field.getText().equals("")) {
            MELDINGFIELD.setForeground(Color.orange);
            MELDINGFIELD.setText("Veld mag niet leeg zijn");
            field.setBackground(Color.orange);
            fieldsOk = false;
        } else {
            try {
                if (field.getText().length() < minLength) {
                    MELDINGFIELD.setForeground(Color.red);
                    MELDINGFIELD.setText("Input te kort");
                    field.setBackground(Color.red);
                    fieldsOk = false;
                } else if (field.getText().length() > maxLength) {
                    MELDINGFIELD.setForeground(Color.red);
                    MELDINGFIELD.setText("Input te lang");
                    field.setBackground(Color.red);
                    fieldsOk = false;
                } else {
                    MELDINGFIELD.setForeground(Color.black);
                    MELDINGFIELD.setText("");
                    field.setBackground(Color.white);
                }
            } catch (Exception e) {
                ePopup(e);
                fieldsOk = false;
            }
        }

    }

    /*private void checkIntField(JTextField field, int minLength, int maxLength) {

        try {
            if (field.getText().equals("")) {
                MELDINGFIELD.setForeground(Color.orange);
                MELDINGFIELD.setText("Veld mag niet leeg zijn");
                field.setBackground(Color.orange);
                fieldsOk = false;
            } else if (field.getText().length() < minLength) {
                MELDINGFIELD.setForeground(Color.red);
                MELDINGFIELD.setText("Input te kort");
                field.setBackground(Color.red);
                fieldsOk = false;
            } else if (field.getText().length() > maxLength) {
                MELDINGFIELD.setForeground(Color.red);
                MELDINGFIELD.setText("Input te lang");
                field.setBackground(Color.red);
                fieldsOk = false;
            } else {
                Integer.parseInt(field.getText());
                MELDINGFIELD.setForeground(Color.black);
                MELDINGFIELD.setText("");
                field.setBackground(Color.white);
            }
        } catch (Exception e) {
            MELDINGFIELD.setForeground(Color.red);
            MELDINGFIELD.setText("Alleen cijfers toegestaan");
            field.setBackground(Color.red);
            fieldsOk = false;
        }

    } */

    private boolean checkFields() {
        fieldsOk = true;
        checkStringField(idToernooiTxt, 1, 100);
        checkStringField(naamToernooiTxt, 2, 250);
        checkStringField(datumTxt, 10, 10);
        checkStringField(inschrijfKostenTxt, 4, 40);
        checkStringField(maxInschrijfTxt, 2, 100);
        checkStringField(maxSpelersTafelTxt, 1, 100);
        checkStringField(codeKaartspelTxt, 1, 100);
        checkStringField(codeLocatieTxt, 1, 100);
        checkStringField(typeKaartspelTxt, 1, 255);
        return fieldsOk;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        terugButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        MELDINGFIELD = new javax.swing.JLabel();
        voegtoe1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        naamToernooiTxt = new javax.swing.JTextField();
        idToernooiTxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        inschrijfKostenTxt = new javax.swing.JTextField();
        maxInschrijfTxt = new javax.swing.JTextField();
        maxSpelersTafelTxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        codeKaartspelTxt = new javax.swing.JTextField();
        codeLocatieTxt = new javax.swing.JTextField();
        typeKaartspelTxt = new javax.swing.JTextField();
        datumTxt = new javax.swing.JFormattedTextField();
        feedback2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        terugButton.setText("Terug");
        terugButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terugButtonActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Nieuw Toernooi"));

        MELDINGFIELD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        voegtoe1.setText("Voeg toernooi toe");
        voegtoe1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voegtoe1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Datum (yyyy-mm-dd)");

        jLabel12.setText("Naam");

        naamToernooiTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                naamToernooiTxtFocusLost(evt);
            }
        });

        idToernooiTxt.setEditable(false);
        idToernooiTxt.setFocusCycleRoot(true);
        idToernooiTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idToernooiTxtActionPerformed(evt);
            }
        });
        idToernooiTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                idToernooiTxtFocusLost(evt);
            }
        });

        jLabel13.setText("Toernooi id");

        jLabel14.setText("Inschrijf kosten");

        inschrijfKostenTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inschrijfKostenTxtActionPerformed(evt);
            }
        });

        jLabel15.setText("Maximale inschrijvingen:");

        jLabel16.setText("Maximale spelers per tafel:");

        jLabel17.setText("Kaartspel code");

        jLabel18.setText("Locatie code:");

        jLabel19.setText("Kaartspel type");

        codeLocatieTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codeLocatieTxtActionPerformed(evt);
            }
        });

        datumTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datumTxtActionPerformed(evt);
            }
        });

        feedback2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(feedback2, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(voegtoe1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(maxInschrijfTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(maxSpelersTafelTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(inschrijfKostenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(codeKaartspelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(codeLocatieTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(typeKaartspelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(naamToernooiTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                    .addComponent(idToernooiTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                    .addComponent(datumTxt)))))))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {codeKaartspelTxt, codeLocatieTxt, idToernooiTxt, inschrijfKostenTxt, maxInschrijfTxt, maxSpelersTafelTxt, naamToernooiTxt, typeKaartspelTxt});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(idToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(naamToernooiTxt))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(datumTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inschrijfKostenTxt)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(maxInschrijfTxt)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(maxSpelersTafelTxt)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(codeKaartspelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(codeLocatieTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(typeKaartspelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(voegtoe1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(feedback2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(4, 4, 4)
                .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton3.setText("Terug");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3))
        );

        jPanel2.getAccessibleContext().setAccessibleName("Niew Toernooi");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void datumTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datumTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_datumTxtActionPerformed

    private void codeLocatieTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codeLocatieTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codeLocatieTxtActionPerformed

    private void inschrijfKostenTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inschrijfKostenTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inschrijfKostenTxtActionPerformed

    private void idToernooiTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idToernooiTxtFocusLost

    }//GEN-LAST:event_idToernooiTxtFocusLost

    private void idToernooiTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idToernooiTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idToernooiTxtActionPerformed

    private void naamToernooiTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_naamToernooiTxtFocusLost

    }//GEN-LAST:event_naamToernooiTxtFocusLost

    private void voegtoe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voegtoe1ActionPerformed
        if (checkFields() == true) {
            addToernooi();
        }
    }//GEN-LAST:event_voegtoe1ActionPerformed

    private void terugButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terugButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        Toernooien toernooien = new Toernooien();
        toernooien.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_terugButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.dispose();
        Toernooien Toernooien = new Toernooien();
        Toernooien.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void ePopup(Exception e) {
        final String eMessage = "Er is iets fout gegaan, neem contact op met de aplicatiebouwer, geef deze foutmelding door: ";
        String error = eMessage + e;
        JOptionPane.showMessageDialog(rootPane, error);
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
            java.util.logging.Logger.getLogger(Toevoegen_toernooi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Toevoegen_toernooi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Toevoegen_toernooi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Toevoegen_toernooi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Toevoegen_toernooi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel MELDINGFIELD;
    private javax.swing.JTextField achternaam;
    private javax.swing.JTextField adres;
    private javax.swing.JTextField codeKaartspelTxt;
    private javax.swing.JTextField codeLocatieTxt;
    private javax.swing.JFormattedTextField datumTxt;
    private javax.swing.JTextField email;
    private javax.swing.JLabel feedback;
    private javax.swing.JLabel feedback2;
    private javax.swing.JTextField id;
    private javax.swing.JTextField idToernooiTxt;
    private javax.swing.JTextField inschrijfKostenTxt;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField land;
    private javax.swing.JTextField maxInschrijfTxt;
    private javax.swing.JTextField maxSpelersTafelTxt;
    private javax.swing.JTextField naamToernooiTxt;
    private javax.swing.JTextField postcode;
    private javax.swing.JTextField roepnaam;
    private javax.swing.JTextField telefoon;
    private javax.swing.JButton terugButton;
    private javax.swing.JButton toon;
    private javax.swing.JButton toonId;
    private javax.swing.JButton toonlijst;
    private javax.swing.JTextField typeKaartspelTxt;
    private javax.swing.JButton verwijder;
    private javax.swing.JButton voegtoe;
    private javax.swing.JButton voegtoe1;
    private javax.swing.JButton wijzig;
    private javax.swing.JTextField woonplaats;
    // End of variables declaration//GEN-END:variables
}
