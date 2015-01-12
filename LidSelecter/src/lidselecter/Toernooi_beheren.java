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
public class Toernooi_beheren extends javax.swing.JFrame {

    private boolean fieldsOk;
    private int new_toernooiID = 0;

    DefaultListModel jListModel = new DefaultListModel();

    /**
     * Creates new form Toevoegen_toernooi
     */
    public Toernooi_beheren() {
        initComponents();
        nieuwToernooiId();
        setLocationRelativeTo(null);
        locatieLabel.setText("<html>Loctie code:<br>(0 voor onbekende locatie)</html>");
        toernooiList.setModel(jListModel);
        vulLijst();
    }

    // Hier vul je de lijst met de toernooi namen
    private void vulLijst() {
        try {
            
            //Sql_connect.doConnect();
            String zoekVeld = zoekTxt.getText();
            ResultSet result;
            Sql_connect.doConnect();
            if (zoekVeld.equals(""))
            {
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT * FROM toernooi");
                result = stat.executeQuery();
            } else{
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT * FROM toernooi WHERE Naam LIKE ?");
                stat.setString(1, "%"+zoekVeld+"%");  
                result = stat.executeQuery();
            }
            
            jListModel.removeAllElements();     
            while (result.next()) {
                ModelItem item = new ModelItem();

                item.id = result.getInt("Id_toernooi");
                item.naam = result.getString("Naam");
                item.datum = result.getString("Datum");
                item.inschrijfKosten = result.getString("Inschrijfkosten");
                item.maxInschrijf = result.getString("Max_inschrijvingen_T");
                item.maxPTafel = result.getString("Max_speler_per_tafel");
                item.kaartCode = result.getString("Kaartspel_code");
                item.locatieCode = result.getString("Id_locatie");
                item.kaartType = result.getString("Kaartspeltype");
                jListModel.addElement(item);

                MELDINGFIELD.setText("Opvragen lijst gelukt!");
            }

        } catch (Exception e) {
            ePopup(e);
        }
    }

    // hier krijg je het eerst volgende nummer voor het id
    private int nieuwToernooiId() {
        // standaart waarde nieuw id

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

    // hier voeg je een nieuw toernooi toe
    private void nieuwToernooi() {
        try {
            //maak een connectie
            Sql_connect.doConnect();

            idToernooiTxt.setText(Integer.toString(new_toernooiID));

            // krijg de tekst uit de velden
            String Id_toernooi = idToernooiTxt.getText(); /* is tekstdie verkregen is uit nieuwToernooiId() methode */

            String naam = naamToernooiTxt.getText();
            String Datum = datumTxt.getText();
            String Inschrijfkosten = inschrijfKostenTxt.getText();
            String Max_inschrijvingen_T = maxInschrijfTxt.getText();
            String Max_speler_per_tafel = maxSpelersTafelTxt.getText();
            String Kaartspel_code = codeKaartspelTxt.getText();
            String Id_locatie = codeLocatieTxt.getText();
            String Kaartspeltype = typeKaartspelTxt.getText();

            // sql prepair statement
            String prepSqlStatement
                    = "INSERT INTO toernooi (Id_toernooi, Naam, Datum, "
                    + "Inschrijfkosten, Max_inschrijvingen_T, "
                    + "Max_speler_per_tafel, Kaartspel_code, "
                    + "Id_locatie, Kaartspeltype) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, Id_toernooi);
            stat.setString(2, naam);
            stat.setString(3, Datum);
            stat.setString(4, Inschrijfkosten);
            stat.setString(5, Max_inschrijvingen_T);
            stat.setString(6, Max_speler_per_tafel);
            stat.setString(7, Kaartspel_code);
            stat.setString(8, Id_locatie);
            stat.setString(9, Kaartspeltype);
            stat.executeUpdate();
            // melding
            JOptionPane.showMessageDialog(rootPane, "Toevoegen nieuw toernooi gelukt");


        } catch (Exception e) {
            ePopup(e);
        }
    }
    
    // hier weizig je de toernooien
    private void wijzigenToernooi() {
        try {
            // verkrijg de waarders uit de velden
            int Id_toernooi = Integer.parseInt(idToernooiTxt.getText());
            String naam = naamToernooiTxt.getText();
            String Datum = datumTxt.getText();
            String Inschrijfkosten = inschrijfKostenTxt.getText();
            String Max_inschrijvingen_T = maxInschrijfTxt.getText();
            String Max_speler_per_tafel = maxSpelersTafelTxt.getText();
            String Kaartspel_code = codeKaartspelTxt.getText();
            String Locatie_code = codeLocatieTxt.getText();
            String Kaartspeltype = typeKaartspelTxt.getText();

            Sql_connect.doConnect();
            String prepSqlStatement = "UPDATE toernooi SET "
                    + "Naam = ?, "
                    + "Datum = ?, "
                    + "Inschrijfkosten = ?,"
                    + "Max_inschrijvingen_T = ?,"
                    + "Max_speler_per_tafel = ?,"
                    + "Kaartspel_code = ?,"
                    + "Locatie_code = ?,"
                    + "Kaartspeltype = ? "
                    + "WHERE Id_toernooi = ?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, naam);
            stat.setString(2, Datum);
            stat.setString(3, Inschrijfkosten);
            stat.setString(4, Max_inschrijvingen_T);
            stat.setString(5, Max_speler_per_tafel);
            stat.setString(6, Kaartspel_code);
            stat.setString(7, Locatie_code);
            stat.setString(8, Kaartspeltype);
            stat.setInt(9, Id_toernooi);
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

            int id_toernooi = Integer.parseInt(idToernooiTxt.getText());

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
            if (toernooiList.getSelectedValue() == null) {
                MELDINGFIELD.setText("Niets Geselecteerd.");
            } else {
                ModelItem selectedItem = (ModelItem) toernooiList.getSelectedValue();
                idToernooiTxt.setText(Integer.toString(selectedItem.id));
                naamToernooiTxt.setText(selectedItem.naam);
                datumTxt.setText(selectedItem.datum);
                inschrijfKostenTxt.setText(selectedItem.inschrijfKosten);
                maxInschrijfTxt.setText(selectedItem.maxInschrijf);
                maxSpelersTafelTxt.setText(selectedItem.maxPTafel);
                codeKaartspelTxt.setText(selectedItem.kaartCode);
                codeLocatieTxt.setText(selectedItem.locatieCode);
                typeKaartspelTxt.setText(selectedItem.kaartType);

                MELDINGFIELD.setText("Opvraag ID gelukt!");
            }
        } catch (Exception e) {
            MELDINGFIELD.setText("Geen naam geselecteerd!");
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

        terugButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        MELDINGFIELD = new javax.swing.JLabel();
        voegtoeButton = new javax.swing.JButton();
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
        locatieLabel = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        codeKaartspelTxt = new javax.swing.JTextField();
        codeLocatieTxt = new javax.swing.JTextField();
        typeKaartspelTxt = new javax.swing.JTextField();
        datumTxt = new javax.swing.JFormattedTextField();
        feedback2 = new javax.swing.JLabel();
        wijzigenButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        toernooiList = new javax.swing.JList();
        verwijderenButton = new javax.swing.JButton();
        zoekTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        backToMain = new javax.swing.JButton();

        terugButton.setText("Terug");
        terugButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terugButtonActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Niew toernooi");
        setResizable(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        MELDINGFIELD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        voegtoeButton.setText("Voeg toernooi toe");
        voegtoeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voegtoeButtonActionPerformed(evt);
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

        jLabel14.setText("Inschrijf kosten (00.00)");

        inschrijfKostenTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inschrijfKostenTxtActionPerformed(evt);
            }
        });

        maxInschrijfTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxInschrijfTxtActionPerformed(evt);
            }
        });

        jLabel15.setText("Maximale inschrijvingen:");

        jLabel16.setText("Maximale spelers per tafel:");

        jLabel17.setText("Kaartspel code");

        locatieLabel.setText("Locatie code: (vul 0 in ");

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

        wijzigenButton.setText("Wijzigen toernooi");
        wijzigenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wijzigenButtonActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        toernooiList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toernooiListMouseClicked(evt);
            }
        });
        toernooiList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                toernooiListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(toernooiList);

        verwijderenButton.setText("Verwijder toernooi");
        verwijderenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verwijderenButtonActionPerformed(evt);
            }
        });

        zoekTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoekTxtActionPerformed(evt);
            }
        });
        zoekTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zoekTxtKeyReleased(evt);
            }
        });

        jLabel4.setText("Zoeken");

        backToMain.setText("Terug");
        backToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToMainActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(backToMain))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(feedback2, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17)
                                    .addComponent(locatieLabel)
                                    .addComponent(jLabel19)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(wijzigenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(voegtoeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(1, 1, 1)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(maxInschrijfTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(maxSpelersTafelTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(inschrijfKostenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(codeKaartspelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(codeLocatieTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(typeKaartspelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(naamToernooiTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                            .addComponent(idToernooiTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                            .addComponent(datumTxt)))
                                    .addComponent(verwijderenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(362, 362, 362)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(165, Short.MAX_VALUE)))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {codeKaartspelTxt, codeLocatieTxt, datumTxt, idToernooiTxt, inschrijfKostenTxt, maxInschrijfTxt, maxSpelersTafelTxt, naamToernooiTxt});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(feedback2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13)
                                .addComponent(idToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
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
                                    .addComponent(locatieLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(typeKaartspelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(verwijderenButton)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(voegtoeButton)
                                        .addGap(4, 4, 4)
                                        .addComponent(wijzigenButton))))
                            .addComponent(jScrollPane1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(backToMain)
                        .addGap(0, 20, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 21, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

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

    private void voegtoeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voegtoeButtonActionPerformed
        if (checkFields() == true) {
            nieuwToernooi();
            this.dispose();
        Toernooi_main Toernooien_main = new Toernooi_main();
        Toernooien_main.setVisible(rootPaneCheckingEnabled);
        }
        
    }//GEN-LAST:event_voegtoeButtonActionPerformed

    private void terugButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terugButtonActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_terugButtonActionPerformed

    private void backToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToMainActionPerformed
        this.dispose();
        Main Main = new Main();
        Main.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_backToMainActionPerformed

    private void verwijderenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verwijderenButtonActionPerformed
        // TODO add your handling code here:
        verwijderenToernooi();
    }//GEN-LAST:event_verwijderenButtonActionPerformed

    private void wijzigenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wijzigenButtonActionPerformed
        // TODO add your handling code here:
        if (checkFields() == true) {
            wijzigenToernooi();
        }
    }//GEN-LAST:event_wijzigenButtonActionPerformed

    private void toernooiListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_toernooiListMouseClicked
        // TODO add your handling code here:
        

    }//GEN-LAST:event_toernooiListMouseClicked

    private void maxInschrijfTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxInschrijfTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maxInschrijfTxtActionPerformed

    private void zoekTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zoekTxtKeyReleased
        // TODO add your handling code here:

        vulLijst();

        /*
        zoekVeld = zoekTxt.getText();
        int posSpatie = zoekVeld.indexOf(" ");

        if (posSpatie > 0) {

            String achternaam = zoekVeld.subString(posSpatie + 1, s.length());
        }
        pos met indexOf(" ");
        substring (postion + 1 tot lengte

            met split, if array is 2
            */
    }//GEN-LAST:event_zoekTxtKeyReleased

    private void zoekTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoekTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_zoekTxtActionPerformed

    private void toernooiListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_toernooiListValueChanged
        gegevensLijst();
    }//GEN-LAST:event_toernooiListValueChanged

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
            java.util.logging.Logger.getLogger(Toernooi_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Toernooi_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Toernooi_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Toernooi_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Toernooi_beheren().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel MELDINGFIELD;
    private javax.swing.JButton backToMain;
    private javax.swing.JTextField codeKaartspelTxt;
    private javax.swing.JTextField codeLocatieTxt;
    private javax.swing.JFormattedTextField datumTxt;
    private javax.swing.JLabel feedback2;
    private javax.swing.JTextField idToernooiTxt;
    private javax.swing.JTextField inschrijfKostenTxt;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel locatieLabel;
    private javax.swing.JTextField maxInschrijfTxt;
    private javax.swing.JTextField maxSpelersTafelTxt;
    private javax.swing.JTextField naamToernooiTxt;
    private javax.swing.JButton terugButton;
    private javax.swing.JList toernooiList;
    private javax.swing.JTextField typeKaartspelTxt;
    private javax.swing.JButton verwijderenButton;
    private javax.swing.JButton voegtoeButton;
    private javax.swing.JButton wijzigenButton;
    private javax.swing.JTextField zoekTxt;
    // End of variables declaration//GEN-END:variables

}
