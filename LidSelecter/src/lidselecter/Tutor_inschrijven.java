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
public class Tutor_inschrijven extends javax.swing.JFrame {

    DefaultListModel speler = new DefaultListModel();
    DefaultListModel tutor = new DefaultListModel();
    private boolean fieldsOk;

    /**
     * Creates new form Tutor_inschrijven
     */
    public Tutor_inschrijven() {
        initComponents();
        setLocationRelativeTo(null);
        spelerList.setModel(speler);
        tutorList.setModel(tutor);
        vulLijst();
        vulLijstTutor();

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

    private void gegevensLijstSpeler() {
        try {
            if (spelerList.getSelectedValue() == null) {
                MELDINGFIELD.setText("Niets Geselecteerd.");
            } else {
                ModelItem selectedItem = (ModelItem) spelerList.getSelectedValue();
                idSpelerTxt.setText(Integer.toString(selectedItem.id));

                MELDINGFIELD.setText("Opvraag ID gelukt!");
            }
        } catch (Exception e) {
            MELDINGFIELD.setText("Geen naam geselecteerd!");
        }
    }

    private void gegevensLijstTutor() {
        try {
            if (spelerList.getSelectedValue() == null) {
                MELDINGFIELD.setText("Niets Geselecteerd.");
            } else {
                ModelItem selectedItem = (ModelItem) tutorList.getSelectedValue();
                idSpelerTxt.setText(Integer.toString(selectedItem.id));

                MELDINGFIELD.setText("Opvragen tutor gelukt!");
            }
        } catch (Exception e) {
            MELDINGFIELD.setText("Geen naam geselecteerd!");
        }
    }

    private void checkStringField(JTextField field, int minLength, int maxLength) {
        if (field.getText().equals("")) {
            MELDINGFIELD.setForeground(Color.orange);
            MELDINGFIELD.setText("veld mag niet leeg zijn");
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
                    //feedback.setForeground(Color.black);
                    //feedback.setText("");
                    field.setBackground(Color.white);
                }
            } catch (Exception e) {
                ePopup(e);
                fieldsOk = false;
            }
        }
    }
    /*
     * Loopt alle velden na en controleerd de input (veldaam, minimale waarde,
     * maximale waarde) returned true als alle velden volgens eis zijn ingevuld
     */

    private boolean checkFields() {
        fieldsOk = true;
        //checkIntField(id, 3, 12);
        checkStringField(minRatingTxt, 2, 30);
        checkStringField(inschrijfKostenTxt, 2, 30);
        checkStringField(idSpelerTxt, 1, 30);
        if (fieldsOk) {
            MELDINGFIELD.setForeground(Color.black);
        }
        return fieldsOk;
    }

    // Hier vul je de lijst met de toernooi namen gevuld
    private void vulLijst() {
        try {

            //Sql_connect.doConnect();
            ResultSet result;
            Sql_connect.doConnect();

            String zoekVeld = removeLastChar(zoekTxt.getText());

            String[] parts = zoekVeld.split(" ");
            int partsLength = parts.length;
            if (partsLength == 2) {

                String voornaam = parts[0];
                String achternaam = parts[1];

                String prepSqlStatementVoorActer = "SELECT * FROM persoon where Voornaam like ? AND Achternaam like ? ORDER BY voornaam";
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatementVoorActer);
                stat.setString(1, "%" + voornaam + "%");
                stat.setString(2, "%" + achternaam + "%");
                result = stat.executeQuery();
            } else {
                String prepSqlStatement = "SELECT * FROM persoon where Voornaam like ? ORDER BY voornaam";
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
                stat.setString(1, "%" + zoekVeld + "%");
                result = stat.executeQuery();
            }

            speler.removeAllElements();
            while (result.next()) {
                ModelItem item = new ModelItem();

                item.id = result.getInt("Id_persoon");
                item.voornaam = result.getString("voornaam");
                item.achternaam = result.getString("achternaam");
                speler.addElement(item);

                MELDINGFIELD.setText("Opvragen lijst gelukt!");
            }

        } catch (Exception e) {
            ePopup(e);
        }
    }

    private void vulLijstTutor() {

        try {

            //Sql_connect.doConnect();
            ResultSet result;
            Sql_connect.doConnect();

            String zoekVeld = removeLastChar(zoekTxt.getText());

            String[] parts = zoekVeld.split(" ");
            int partsLength = parts.length;
            if(zoekVeld.equals(""))
            {
                String prepSqlStatement = "SELECT t.Id_persoon, p.voornaam, p.achternaam "
                        + "FROM tutor t "
                        + "JOIN persoon p on t.Id_persoon = p.Id_persoon "
                        + "ORDER BY voornaam";
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
                result = stat.executeQuery();
            } else if (partsLength == 2) {

                String voornaam = parts[0];
                String achternaam = parts[1];

                String prepSqlStatementVoorActer = "SELECT t.Id_persoon, p.voornaam, p.achternaam "
                        + "FROM tutor t "
                        + "JOIN persoon p on t.Id_persoon = p.Id_persoon "
                        + "where Voornaam like ? AND Achternaam like ? "
                        + "ORDER BY voornaam";
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatementVoorActer);
                stat.setString(1, "%" + voornaam + "%");
                stat.setString(2, "%" + achternaam + "%");
                result = stat.executeQuery();
            } else {
                String prepSqlStatement = "SELECT t.Id_persoon, p.voornaam, p.achternaam "
                        + "FROM tutor t "
                        + "JOIN persoon p on t.Id_persoon = p.Id_persoon "
                        + "where Voornaam like ? "
                        + "ORDER BY voornaam";
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
                stat.setString(1, "%" + zoekVeld + "%");
                result = stat.executeQuery();
            }

            tutor.removeAllElements();
            while (result.next()) {
                ModelItem item = new ModelItem();

                item.id = result.getInt("Id_persoon");
                item.voornaam = result.getString("voornaam");
                item.achternaam = result.getString("achternaam");
                tutor.addElement(item);

                MELDINGFIELD.setText("Opvragen lijst gelukt!");
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

        jPanel2 = new javax.swing.JPanel();
        MELDINGFIELD = new javax.swing.JLabel();
        voegtoeButton = new javax.swing.JButton();
        idSpelerTxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        inschrijfKostenTxt = new javax.swing.JTextField();
        minRatingTxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        wijzigenButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        verwijderenButton = new javax.swing.JButton();
        backToMain = new javax.swing.JButton();
        leegButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        spelerList = new javax.swing.JList();
        zoekTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        zoekTutTxt = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tutorList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        MELDINGFIELD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        voegtoeButton.setText("Voeg tutor toe");
        voegtoeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voegtoeButtonActionPerformed(evt);
            }
        });

        idSpelerTxt.setEditable(false);
        idSpelerTxt.setBackground(new java.awt.Color(200, 200, 200));
        idSpelerTxt.setFocusCycleRoot(true);
        idSpelerTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idSpelerTxtActionPerformed(evt);
            }
        });
        idSpelerTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                idSpelerTxtFocusLost(evt);
            }
        });

        jLabel13.setText("Speler id");

        jLabel14.setText("Inschrijf kosten (00.00)");

        inschrijfKostenTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inschrijfKostenTxtActionPerformed(evt);
            }
        });

        minRatingTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minRatingTxtActionPerformed(evt);
            }
        });

        jLabel15.setText("Minimale rating");

        wijzigenButton.setText("Wijzig tutor");
        wijzigenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wijzigenButtonActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        verwijderenButton.setText("Verwijder tutor");
        verwijderenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verwijderenButtonActionPerformed(evt);
            }
        });

        backToMain.setText("Terug");
        backToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToMainActionPerformed(evt);
            }
        });

        leegButton.setText("Leeg velden");
        leegButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leegButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Zoek Speler");

        spelerList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                spelerListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(spelerList);

        zoekTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zoekTxtKeyReleased(evt);
            }
        });

        jLabel2.setText("Zoek Tutor");

        zoekTutTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zoekTutTxtKeyReleased(evt);
            }
        });

        tutorList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                tutorListValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(tutorList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(idSpelerTxt)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(minRatingTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(inschrijfKostenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(wijzigenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(voegtoeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(leegButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(verwijderenButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0))
                    .addComponent(MELDINGFIELD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(backToMain)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(zoekTutTxt))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(zoekTxt))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(idSpelerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(inschrijfKostenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(minRatingTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(verwijderenButton)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(voegtoeButton)
                                        .addGap(4, 4, 4)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(wijzigenButton)
                                            .addComponent(leegButton)))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(zoekTutTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(backToMain))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void voegtoeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voegtoeButtonActionPerformed
        nieuwTutor();
    }//GEN-LAST:event_voegtoeButtonActionPerformed

    private void ePopup(Exception e) {
        final String eMessage = "Er is iets fout gegaan, neem contact op met de aplicatiebouwer, geef deze foutmelding door: ";
        String error = eMessage + e;
        JOptionPane.showMessageDialog(rootPane, error);
    }

    // hier voeg je een nieuw toernooi toe
    private void nieuwTutor() {
        try {
            //maak een connectie
            Sql_connect.doConnect();

            //idSpelerTxt.setText(Integer.toString(new_toernooiID));
            // krijg de tekst uit de velden
            String Id_toernooi = idSpelerTxt.getText(); /* is tekstdie verkregen is uit nieuwToernooiId() methode */

            String Inschrijfkosten = inschrijfKostenTxt.getText();
            String minRating = minRatingTxt.getText();

            // sql prepair statement
            String prepSqlStatement
                    = "INSERT INTO tutor (Id_persoon, Minimalrating, Tutor_kosten) "
                    + "VALUES (?, ?, ?)";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, Id_toernooi);
            stat.setString(2, minRating);
            stat.setString(3, Inschrijfkosten);
            stat.executeUpdate();
            Tutor_inschrijven tutor = new Tutor_inschrijven();
            tutor.setVisible(rootPaneCheckingEnabled);
            this.dispose();
            MELDINGFIELD.setText("Toevoegen nieuwe tutor gelukt");

        } catch (Exception e) {
            ePopup(e);
        }
    }
    private void idSpelerTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idSpelerTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idSpelerTxtActionPerformed

    private void idSpelerTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idSpelerTxtFocusLost

    }//GEN-LAST:event_idSpelerTxtFocusLost

    private void inschrijfKostenTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inschrijfKostenTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inschrijfKostenTxtActionPerformed

    private void minRatingTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minRatingTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_minRatingTxtActionPerformed

    private void wijzigenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wijzigenButtonActionPerformed
        // TODO add your handling code here:
        wijzigenTutor();
    }//GEN-LAST:event_wijzigenButtonActionPerformed

    private void wijzigenTutor() {
        try {
            // verkrijg de waarders uit de velden
            int Id_persoon = Integer.parseInt(idSpelerTxt.getText());
            System.out.println("idpersoon " + Id_persoon);
            String Inschrijfkosten = inschrijfKostenTxt.getText();
            String minRating = minRatingTxt.getText();

            Sql_connect.doConnect();
            String prepSqlStatement = "UPDATE tutor SET Minimalrating = ?, "
                    + "Tutor_kosten = ?"
                    + "WHERE Id_persoon = ?;";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, minRating);
            stat.setString(2, Inschrijfkosten);
            stat.setInt(3, Id_persoon);
            stat.executeUpdate();
            vulLijst();
            vulLijstTutor();
            MELDINGFIELD.setText("Tutor gewijzigd");

        } catch (Exception e) {
            ePopup(e);
            MELDINGFIELD.setText("Wijziging mislukt!");
        }

    }

    private void verwijderenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verwijderenButtonActionPerformed
        // TODO add your handling code here:
        verwijderenTutor();
    }//GEN-LAST:event_verwijderenButtonActionPerformed

    private void verwijderenTutor() {
        try {

            int id_Speler = Integer.parseInt(idSpelerTxt.getText());
            
            if(id_Speler!=0)
            {
            Sql_connect.doConnect();
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement("DELETE FROM tutor WHERE Id_persoon = ?");
            stat.setInt(1, id_Speler);
            stat.executeUpdate();
            JOptionPane.showMessageDialog(this, "Tutor met id: " + id_Speler + " succesvol verwijderd.");
            Tutor_inschrijven tutor = new Tutor_inschrijven();
            tutor.setVisible(rootPaneCheckingEnabled);
            this.dispose();
            }
        } catch (Exception e) {
            ePopup(e);
            MELDINGFIELD.setText("Verwijderen mislukt");
        }

    }

    private void backToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToMainActionPerformed
        this.dispose();
        Main Main = new Main();
        Main.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_backToMainActionPerformed

    private void leegButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leegButtonActionPerformed
        //vulLijst();
        //vulLijstTutor();
        leegVelden();
    }//GEN-LAST:event_leegButtonActionPerformed
    private void leegVelden() {
        idSpelerTxt.setText("");
        inschrijfKostenTxt.setText("");
        minRatingTxt.setText("");
    }
    private void zoekTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zoekTxtKeyReleased
        vulLijst();
    }//GEN-LAST:event_zoekTxtKeyReleased

    private void spelerListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_spelerListValueChanged
        // TODO add your handling code here:
        gegevensLijstSpeler();
        getPerson();
    }//GEN-LAST:event_spelerListValueChanged

    private void zoekTutTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zoekTutTxtKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_zoekTutTxtKeyReleased

    private void tutorListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_tutorListValueChanged
        // TODO add your handling code here:

        gegevensLijstTutor();
        getPerson();
        ModelItem selectedItem = (ModelItem) tutorList.getSelectedValue();
        idSpelerTxt.setText(Integer.toString(selectedItem.id));
    }//GEN-LAST:event_tutorListValueChanged

    private void getPerson() {
        try {
            int l_code;
            try {
                l_code = Integer.parseInt(idSpelerTxt.getText());
            } catch (Exception e) {
                l_code = 0;
            }

            boolean hasRun = false;
            if (l_code > 0) {
                Sql_connect.doConnect();
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT * FROM tutor WHERE Id_persoon = ? LIMIT 1");
                stat.setInt(1, l_code);
                ResultSet result = stat.executeQuery();

                while (result.next()) {
                    inschrijfKostenTxt.setText(result.getString("Tutor_kosten"));
                    minRatingTxt.setText(result.getString("MinimalRating"));

                    hasRun = true;
                    MELDINGFIELD.setText("Opvragen tutor gelukt!");
                }
                if (!hasRun) {
                    MELDINGFIELD.setText("Speler is geen tutor");
                    minRatingTxt.setText("");
                    inschrijfKostenTxt.setText("");
                }
            } else {
                MELDINGFIELD.setText("Geen code geselecteerd");
            }
        } catch (Exception e) {
            ePopup(e);
            Logger.getLogger(Ledeneditor.class.getName()).log(Level.SEVERE, null, e);
            MELDINGFIELD.setText("Opvraag tutor mislukt!");
        }
    }

    private void getPerson1() {
        try {
            int l_code;
            try {
                l_code = Integer.parseInt(idSpelerTxt.getText());
            } catch (Exception e) {
                l_code = 0;
            }

            boolean hasRun = false;
            if (l_code > 0) {
                Sql_connect.doConnect();
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT * FROM tutor WHERE Id_persoon = ? LIMIT 1");
                stat.setInt(1, l_code);
                ResultSet result = stat.executeQuery();

                while (result.next()) {
                    inschrijfKostenTxt.setText(result.getString("Tutor_kosten"));
                    minRatingTxt.setText(result.getString("MinimalRating"));

                    hasRun = true;
                    MELDINGFIELD.setText("Opvraag tutor gelukt!");
                }
                if (!hasRun) {
                    MELDINGFIELD.setText("code bestaat niet");
                    minRatingTxt.setText("");
                    inschrijfKostenTxt.setText("");
                }
            } else {
                MELDINGFIELD.setText("Geen code ingevoerd");
            }
        } catch (Exception e) {
            ePopup(e);
            Logger.getLogger(Ledeneditor.class.getName()).log(Level.SEVERE, null, e);
            MELDINGFIELD.setText("Opvraag tutor mislukt!");
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
            java.util.logging.Logger.getLogger(Tutor_inschrijven.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tutor_inschrijven.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tutor_inschrijven.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tutor_inschrijven.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tutor_inschrijven().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel MELDINGFIELD;
    private javax.swing.JButton backToMain;
    private javax.swing.JTextField idSpelerTxt;
    private javax.swing.JTextField inschrijfKostenTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton leegButton;
    private javax.swing.JTextField minRatingTxt;
    private javax.swing.JList spelerList;
    private javax.swing.JList tutorList;
    private javax.swing.JButton verwijderenButton;
    private javax.swing.JButton voegtoeButton;
    private javax.swing.JButton wijzigenButton;
    private javax.swing.JTextField zoekTutTxt;
    private javax.swing.JTextField zoekTxt;
    // End of variables declaration//GEN-END:variables
}
