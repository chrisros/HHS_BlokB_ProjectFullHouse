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
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Aaik
 */
public class Locatie_beheren extends javax.swing.JFrame {

    private boolean fieldsOk;
    private int new_Id_locatie = 0;
    DefaultListModel jListModel = new DefaultListModel();

    /**
     * Creates new form Locatie_beheren
     */
    public Locatie_beheren() {
        initComponents();
        setLocationRelativeTo(null);

        LocatieList.setModel(jListModel);

    }

    private void ePopup(Exception e) {
        final String eMessage = "Er is iets fout gegaan, neem contact op met de aplicatiebouwer, geef deze foutmelding door: ";
        String error = eMessage + e;
        JOptionPane.showMessageDialog(rootPane, error);
    }

    // hier krijg je het eerst volgende nummer voor het id
    private int nieuwLocatieId() {
        // standaart waarde nieuw id

        try {
            // maak connectie
            Sql_connect.doConnect();
            // statement
            String prepSqlStatement = "select MAX(Id_toernooi) AS biggest from toernooi";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();
            while (result.next()) {
                new_Id_locatie = result.getInt("biggest");
            }
            new_Id_locatie = new_Id_locatie + 1;
            //return new_toernooiID;
            idLocatieTxt.setText(Integer.toString(new_Id_locatie));

        } catch (Exception ex) {
            Logger.getLogger(Ledeneditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new_Id_locatie;
    }

    private void vulLijst() {
        try {
            Sql_connect.doConnect();
            String prepSqlStatement = "SELECT Id_locatie, Naam_locatie FROM locatie ";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();

            jListModel.removeAllElements();
            while (result.next()) {
                ModelItem item = new ModelItem();
                item.id = result.getInt("Id_locatie");
                item.description = result.getString("Naam_locatie");
                jListModel.addElement(item);
                MELDINGFIELD.setText("Opvraag lijst gelukt!");
            }

        } catch (Exception e) {
            ePopup(e);
        }
    }

    // hier check je of de velden aan de eisen voldoen
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

    // hier geef je de eisen mee
    private boolean checkFields() {
        fieldsOk = true;
        checkStringField(idLocatieTxt, 1, 100);
        checkStringField(naamLocTxt, 2, 250);
        checkStringField(plaatsTxt, 10, 10);
        checkStringField(straatTxt, 4, 40);
        checkStringField(huisnummerTxt, 2, 100);
        checkStringField(telNummerTxt, 1, 100);
        return fieldsOk;
    }

    // hier weizig je de toernooien
    private void wijzigenToernooi() {
        try {
            // verkrijg de waarders uit de velden
            int idLocatie = Integer.parseInt(idLocatieTxt.getText());
            String naamLoc = naamLocTxt.getText();
            String plaats = plaatsTxt.getText();
            String straat = straatTxt.getText();
            String huisnummer = huisnummerTxt.getText();
            String telnummer = telNummerTxt.getText();

            Sql_connect.doConnect();
            String prepSqlStatement = "UPDATE toernooi SET "
                    + "Naam_locatie = ?, "
                    + "Plaats = ?, "
                    + "Straat = ?,"
                    + "Huisnummer = ?,"
                    + "Telefoonnummer = ?"
                    + "WHERE Id_locatie = ?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, naamLoc);
            stat.setString(2, plaats);
            stat.setString(3, straat);
            stat.setString(4, huisnummer);
            stat.setString(5, telnummer);
            stat.setInt(6, idLocatie);
            stat.executeUpdate();
            vulLijst();
            MELDINGFIELD.setText("Toernooi gewijzigd");

        } catch (Exception e) {
            ePopup(e);
            MELDINGFIELD.setText("Wijziging mislukt!");
        }

    }

    // hier verwijder je een toernooi, geselecteerd op id
    private void verwijderenToernooi() {
        try {

            int id_toernooi = Integer.parseInt(idLocatieTxt.getText());

            Sql_connect.doConnect();
            String prepSqlStatement = "DELETE FROM toernooi WHERE Id_toernooi = ?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setInt(1, id_toernooi);
            stat.executeUpdate();
            JOptionPane.showMessageDialog(this, "Toernooi met id: " + id_toernooi + " succesvol verwijderd.");
            vulLijst();

        } catch (Exception e) {
            ePopup(e);
            MELDINGFIELD.setText("Verwijderen mislukt");
        }
    }

    // hier wordt tijdens het klikken de jTextfields gevuld met de gegevens uit de database
    private void gegevensLijst() {
        try {
            if (LocatieList.getSelectedValue() == null) {
                MELDINGFIELD.setText("Niets Geselecteerd.");
            } else {
                ModelItem selectedItem = (ModelItem) LocatieList.getSelectedValue();
                idLocatieTxt.setText(Integer.toString(selectedItem.id));
                naamLocTxt.setText(selectedItem.description);
                plaatsTxt.setText(selectedItem.datum);
                huisnummerTxt.setText(selectedItem.inschrijfKosten);
                telNummerTxt.setText(selectedItem.maxInschrijf);
                straatTxt.setText(selectedItem.maxPTafel);

                MELDINGFIELD.setText("Opvraag ID gelukt!");
            }
        } catch (Exception e) {
            MELDINGFIELD.setText("Geen naam geselecteerd!");
        }
    }

    // hier voeg je een nieuw toernooi toe
    private void nieuwLocatie() {
        try {
            //maak een connectie
            Sql_connect.doConnect();

            idLocatieTxt.setText(Integer.toString(new_Id_locatie));

            // krijg de tekst uit de velden
            String Id_toernooi = idLocatieTxt.getText(); /* is tekstdie verkregen is uit nieuwToernooiId() methode */

            String naamLoc = naamLocTxt.getText();
            String plaats = plaatsTxt.getText();
            String straat = straatTxt.getText();
            String huisnummer = huisnummerTxt.getText();
            String telnummer = telNummerTxt.getText();

            // sql prepair statement
            String prepSqlStatement
                    = "INSERT INTO locatie (Id_locatie, Naam_locatie, Plaatas, "
                    + "Straat, Huisnummer, "
                    + "Telefoonnummer) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, Id_toernooi);
            stat.setString(2, naamLoc);
            stat.setString(3, plaats);
            stat.setString(4, straat);
            stat.setString(5, huisnummer);
            stat.setString(6, telnummer);
            stat.executeUpdate();
            // melding
            JOptionPane.showMessageDialog(rootPane, "Toevoegen nieuwe locatie gelukt");

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

        backToMain = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        MELDINGFIELD = new javax.swing.JLabel();
        voegtoeButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        naamLocTxt = new javax.swing.JTextField();
        idLocatieTxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        straatTxt = new javax.swing.JTextField();
        huisnummerTxt = new javax.swing.JTextField();
        telNummerTxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        plaatsTxt = new javax.swing.JFormattedTextField();
        feedback2 = new javax.swing.JLabel();
        wijzigenButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        LocatieList = new javax.swing.JList();
        verwijderenButton = new javax.swing.JButton();
        getListbutton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        backToMain.setText("Terug");
        backToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToMainActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        MELDINGFIELD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        voegtoeButton.setText("Voeg locatie toe");
        voegtoeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voegtoeButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Plaats");

        jLabel12.setText("Naam");

        naamLocTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                naamLocTxtFocusLost(evt);
            }
        });

        idLocatieTxt.setEditable(false);
        idLocatieTxt.setFocusCycleRoot(true);
        idLocatieTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idLocatieTxtActionPerformed(evt);
            }
        });
        idLocatieTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                idLocatieTxtFocusLost(evt);
            }
        });

        jLabel13.setText("Locatie id");

        jLabel14.setText("Straat");

        straatTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                straatTxtActionPerformed(evt);
            }
        });

        huisnummerTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                huisnummerTxtActionPerformed(evt);
            }
        });

        jLabel15.setText("Huisnummer");

        jLabel16.setText("Telefoonnummer");

        plaatsTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plaatsTxtActionPerformed(evt);
            }
        });

        feedback2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        wijzigenButton.setText("Wijzigen Locatie");
        wijzigenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wijzigenButtonActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel11.setText("Alle toernooien");

        LocatieList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LocatieListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(LocatieList);

        verwijderenButton.setText("Verwijder Locatie");
        verwijderenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verwijderenButtonActionPerformed(evt);
            }
        });

        getListbutton.setText("Haal locatie's op");
        getListbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getListbuttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(wijzigenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel16)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(idLocatieTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                    .addComponent(naamLocTxt)
                                    .addComponent(huisnummerTxt)
                                    .addComponent(straatTxt)
                                    .addComponent(plaatsTxt)
                                    .addComponent(telNummerTxt)))
                            .addComponent(feedback2, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(voegtoeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(verwijderenButton, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(getListbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(feedback2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(idLocatieTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(naamLocTxt))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(plaatsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(straatTxt)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(huisnummerTxt)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(telNummerTxt)
                                    .addComponent(jLabel16))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(verwijderenButton)
                            .addComponent(getListbutton)
                            .addComponent(voegtoeButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(wijzigenButton)
                        .addGap(93, 93, 93))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(backToMain)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backToMain)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToMainActionPerformed
        this.dispose();
        Main Main = new Main();
        Main.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_backToMainActionPerformed

    private void getListbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getListbuttonActionPerformed
        // TODO add your handling code here:
        vulLijst();
    }//GEN-LAST:event_getListbuttonActionPerformed

    private void verwijderenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verwijderenButtonActionPerformed
        // TODO add your handling code here:
        verwijderenToernooi();
    }//GEN-LAST:event_verwijderenButtonActionPerformed

    private void LocatieListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LocatieListMouseClicked
        // TODO add your handling code here:
        gegevensLijst();
    }//GEN-LAST:event_LocatieListMouseClicked

    private void wijzigenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wijzigenButtonActionPerformed
        // TODO add your handling code here:
        if (checkFields() == true) {
            wijzigenToernooi();
        }
    }//GEN-LAST:event_wijzigenButtonActionPerformed

    private void plaatsTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plaatsTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_plaatsTxtActionPerformed

    private void huisnummerTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_huisnummerTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_huisnummerTxtActionPerformed

    private void straatTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_straatTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_straatTxtActionPerformed

    private void idLocatieTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idLocatieTxtFocusLost

    }//GEN-LAST:event_idLocatieTxtFocusLost

    private void idLocatieTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idLocatieTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idLocatieTxtActionPerformed

    private void naamLocTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_naamLocTxtFocusLost

    }//GEN-LAST:event_naamLocTxtFocusLost

    private void voegtoeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voegtoeButtonActionPerformed
        if (checkFields() == true) {
            nieuwLocatie();
            this.dispose();
            Toernooien_main Toernooien_main = new Toernooien_main();
            Toernooien_main.setVisible(rootPaneCheckingEnabled);
        }
    }//GEN-LAST:event_voegtoeButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Locatie_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Locatie_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Locatie_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Locatie_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Locatie_beheren().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList LocatieList;
    private javax.swing.JLabel MELDINGFIELD;
    private javax.swing.JButton backToMain;
    private javax.swing.JLabel feedback2;
    private javax.swing.JButton getListbutton;
    private javax.swing.JTextField huisnummerTxt;
    private javax.swing.JTextField idLocatieTxt;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField naamLocTxt;
    private javax.swing.JFormattedTextField plaatsTxt;
    private javax.swing.JTextField straatTxt;
    private javax.swing.JTextField telNummerTxt;
    private javax.swing.JButton verwijderenButton;
    private javax.swing.JButton voegtoeButton;
    private javax.swing.JButton wijzigenButton;
    // End of variables declaration//GEN-END:variables

}
