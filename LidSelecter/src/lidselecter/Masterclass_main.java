/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lidselecter;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Aaik
 */
public class Masterclass_main extends javax.swing.JFrame {

    private final DefaultTableModel table = new DefaultTableModel();
    DefaultListModel jListModel = new DefaultListModel();

    /**
     * Creates new form Masterclass_main
     */
    public Masterclass_main() {
        initComponents();
        setLocationRelativeTo(null);
        maxSpelersTxt.setText("0");
        inschrijfList.setModel(jListModel);

        masterclassTable.setModel(table);
        String[] Kolomnaam = {"Masterclass id", "Min Rating", "Prijs", "Max spelers", "Locatie code", "Datum"};
        table.setColumnIdentifiers(Kolomnaam);
        table.setRowCount(0);
        table.setColumnCount(6);

        tabelVullen();
        tableEigenschappen();

    }

    private void tableEigenschappen() {

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        masterclassTable.getColumn("Masterclass id").setCellRenderer(rightRenderer);
        masterclassTable.getColumn("Min Rating").setCellRenderer(rightRenderer);
        masterclassTable.getColumn("Prijs").setCellRenderer(rightRenderer);
        masterclassTable.getColumn("Max spelers").setCellRenderer(rightRenderer);
        masterclassTable.getColumn("Locatie code").setCellRenderer(rightRenderer);
        masterclassTable.getColumn("Datum").setCellRenderer(rightRenderer);

        TableCellRenderer rendererFromHeader = masterclassTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.RIGHT);

    }

    private void setProgress(int currentPlayers, int maxPlayers) {
        try {
            progressBar.setMinimum(0);
            progressBar.setMaximum(maxPlayers);
            progressBar.setName("Inschrijvingen");
            progressBar.setValue(currentPlayers);
            if //als er nog niemand in geschreven staat
                    (currentPlayers == 0) {
                progressBar.setString("Nog geen spelers");//set de text van de progressbar
                progressBar.setStringPainted(rootPaneCheckingEnabled);
                progressBar.setForeground(Color.red);//set de kleur van de text
            } //als het toernooi vol zit
            else if (currentPlayers == maxPlayers) {
                progressBar.setString("Vol!");//set de text van de progressbar
                progressBar.setStringPainted(rootPaneCheckingEnabled);
                progressBar.setForeground(Color.green);//set de kleur van de text            
            } //als er wel mensen ingeschreven stan, maar het toernooi niet vol zit
            else if (currentPlayers < maxPlayers) {
                int restant = maxPlayers - currentPlayers;
                String plaatsen = "";
                if (restant == 1) {
                    plaatsen = " plaats ";
                } else {
                    plaatsen = " plaatsen ";
                }
                String text = "Nog " + restant + plaatsen + "over";//set de text van de progressbar
                progressBar.setString(text);
                progressBar.setStringPainted(rootPaneCheckingEnabled);
                progressBar.setForeground(Color.black);//set de kleur van de text
            } //als er meer mensen ingeschreven staan dan mogelijk is bij dit toernooi
            else if (currentPlayers > maxPlayers) {
                int overschot = currentPlayers - maxPlayers;
                progressBar.setString("max overschreden met " + overschot);//set de text van de progressbar
                progressBar.setStringPainted(rootPaneCheckingEnabled);
                progressBar.setForeground(Color.white);//set de kleur van de text
            }
        } catch (Exception e) {
            ePopup(e);
        }

    }

    private void ePopup(Exception e) {
        final String eMessage = "Er is iets fout gegaan, neem contact op met de aplicatiebouwer, geef deze foutmelding door: ";
        String error = eMessage + e;
        JOptionPane.showMessageDialog(rootPane, error);
    }

    private void tabelVullen() {
        // TODO add your handling code here:
        Sql_connect.doConnect();
        // declareer de variable voor in de rs
        String id;
        String rating;
        String prijs;
        String Max_inschrijvingen_M;
        String Id_locatie;
        String Datum;

        try {
            // connect 
            Sql_connect.doConnect();
            // statement maken
            String prepSqlStatement = "select * from masterclass;";
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
                rating = result.getString("Minimale_rating");
                prijs = result.getString("Inschrijf_prijs");
                Max_inschrijvingen_M = result.getString("Max_inschrijvingen_M");
                Id_locatie = result.getString("Id_locatie");
                Datum = result.getString("Datum");

                // vul vervolgens in de tabel de waardes in als volgt: resultset, aantal, plaats
                table.setValueAt(id, d, 0);
                table.setValueAt(rating, d, 1);
                table.setValueAt(prijs, d, 2);
                table.setValueAt(Max_inschrijvingen_M, d, 3);
                table.setValueAt(Id_locatie, d, 4);
                table.setValueAt(Datum, d, 5);

                // verhoog aantal totdat alles was je hebt opgevraagd is geweest
                d++;

            }

            result.last();
            System.out.println(result.getRow());

            result.close();
            stat.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }

    private void gegevensOphalen() {

        try {
            int row = masterclassTable.getSelectedRow();

            String Table_click = masterclassTable.getModel().getValueAt(row, 0).toString();
            Sql_connect.doConnect();

            // statement maken
            String prepSqlStatement = "select * from masterclass where Id_masterclass = '" + Table_click + "'";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();

            if (result.next()) {
                String maxSpelers = result.getString("Max_inschrijvingen_M");
                maxSpelersTxt.setText(maxSpelers);
                String idMasterclass = result.getString("Id_masterclass");
                masterclass_IdTxt.setText(idMasterclass);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void vulLijst() {
        try {
            Sql_connect.doConnect();
            String zoekVeld = zoekTxt.getText();

            String[] parts = zoekVeld.split(" ");
            int partsLength = parts.length;

            if (partsLength == 2) {

                String voornaam = parts[0];
                String achternaam = parts[1];

                String prepSqlStatementVoorActer = "SELECT * FROM persoon where Voornaam like ? AND Achternaam like ?";
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
                String prepSqlStatement = "SELECT * FROM persoon where Voornaam like ?";
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

    private void inschrijvenMasterclass() {

        // krijg de tekst uit de velden
        int idMasterclass = Integer.parseInt(masterclass_IdTxt.getText());
        int idSpeler = Integer.parseInt(speler_codeTxt.getText());
        try {
            //maak een connectie
            Sql_connect.doConnect();

            // sql prepair statement
            String prepSqlStatement
                    = "INSERT INTO masterclassdeelnemer "
                    + "(Id_persoon, Id_masterclass, betaald, Ingeschreven)"
                    + "VALUES (?, ?, 0, 0)";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setInt(1, idSpeler);
            stat.setInt(2, idMasterclass);

            stat.executeUpdate();
            // melding
            MELDINGVELD.setText("Ingeschreven voor toernooi: " + idMasterclass + " met speler code: " + idSpeler);

        } catch (Exception e) {
            ePopup(e);
            MELDINGVELD.setText("Inschrijven voor toernooi: " + idMasterclass + " is mislukt");
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

        inschrijvenToernooiButton = new javax.swing.JButton();
        MELDINGFIELD = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        masterclassTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        inschrijvenMasterclassButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        minSpelersTxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        maxSpelersTxt = new javax.swing.JTextField();
        inschrijfProcesButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        speler_codeTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        masterclass_IdTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        MELDINGVELD = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        inschrijfList = new javax.swing.JList();
        jLabel4 = new javax.swing.JLabel();
        zoekTxt = new javax.swing.JTextField();

        inschrijvenToernooiButton.setText("Inschrijven Toernooi");
        inschrijvenToernooiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inschrijvenToernooiButtonActionPerformed(evt);
            }
        });

        MELDINGFIELD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        masterclassTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Masterclass Id", "Minimum rating", "Prijs", "Max inschrijvingen", "Locatie"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        masterclassTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                masterclassTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(masterclassTable);

        jButton1.setText("Terug");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        inschrijvenMasterclassButton.setText("Inschrijven Masterclass");
        inschrijvenMasterclassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inschrijvenMasterclassButtonActionPerformed(evt);
            }
        });

        jLabel6.setText("Ingeschreven spelers");

        minSpelersTxt.setEditable(false);

        jLabel7.setText("van");

        maxSpelersTxt.setEditable(false);

        inschrijfProcesButton.setText("Check inschrijfproces");
        inschrijfProcesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inschrijfProcesButtonActionPerformed(evt);
            }
        });

        progressBar.setForeground(new java.awt.Color(153, 153, 255));

        jLabel3.setText("Spelers code");

        jLabel2.setText("Masterclass id");

        masterclass_IdTxt.setEditable(false);

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel1.setText("Klik masterclass aan en vul je id in en klik op inschrijven");

        MELDINGVELD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

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

        jLabel4.setText("Zoeken");

        zoekTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zoekTxtKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(MELDINGVELD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(12, 12, 12))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel3)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(speler_codeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(inschrijvenMasterclassButton))))
                                    .addComponent(maxSpelersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(zoekTxt))
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addComponent(masterclass_IdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(inschrijfProcesButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(minSpelersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addGap(131, 131, 131)))
                        .addGap(140, 140, 140)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(minSpelersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)
                                    .addComponent(maxSpelersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(inschrijfProcesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(masterclass_IdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(speler_codeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inschrijvenMasterclassButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MELDINGVELD, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void masterclassTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_masterclassTableMouseClicked
        // TODO add your handling code here:
        gegevensOphalen();

    }//GEN-LAST:event_masterclassTableMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        Main menu = new Main();
        menu.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void inschrijvenToernooiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inschrijvenToernooiButtonActionPerformed

    }//GEN-LAST:event_inschrijvenToernooiButtonActionPerformed

    private void inschrijvenMasterclassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inschrijvenMasterclassButtonActionPerformed
        // TODO add your handling code here:
        inschrijvenMasterclass();

    }//GEN-LAST:event_inschrijvenMasterclassButtonActionPerformed

    private void inschrijfProcesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inschrijfProcesButtonActionPerformed
        try {
            int row = masterclassTable.getSelectedRow();

            String Table_click = masterclassTable.getModel().getValueAt(row, 0).toString();
            Sql_connect.doConnect();

            String prepSqlStatement = "select count(Id_persoon) as inschrijvingen from masterclassdeelnemer where Id_masterclass = '" + Table_click + "'";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();

            if (result.next()) {
                String add1 = result.getString("inschrijvingen");
                minSpelersTxt.setText(add1);
            }

            setProgress(Integer.parseInt(minSpelersTxt.getText()), Integer.parseInt(maxSpelersTxt.getText()));

        } catch (Exception e) {
            //ePopup(e);
            System.out.println(e);
        }
    }//GEN-LAST:event_inschrijfProcesButtonActionPerformed

    private void inschrijfListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inschrijfListMouseClicked
        // TODO add your handling code here:
        gegevensLijst();
    }//GEN-LAST:event_inschrijfListMouseClicked

    private void inschrijfListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_inschrijfListValueChanged
        // TODO add your handling code here:
        gegevensLijst();
    }//GEN-LAST:event_inschrijfListValueChanged

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
            java.util.logging.Logger.getLogger(Masterclass_main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Masterclass_main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Masterclass_main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Masterclass_main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Masterclass_main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel MELDINGFIELD;
    private javax.swing.JLabel MELDINGVELD;
    private javax.swing.JList inschrijfList;
    private javax.swing.JButton inschrijfProcesButton;
    private javax.swing.JButton inschrijvenMasterclassButton;
    private javax.swing.JButton inschrijvenToernooiButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable masterclassTable;
    private javax.swing.JTextField masterclass_IdTxt;
    private javax.swing.JTextField maxSpelersTxt;
    private javax.swing.JTextField minSpelersTxt;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextField speler_codeTxt;
    private javax.swing.JTextField zoekTxt;
    // End of variables declaration//GEN-END:variables

}
