/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lidselecter;

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
 * @author Lorenzo
 */
public class Masterclass_Betalen extends javax.swing.JFrame {

    DefaultListModel spelerListModel = new DefaultListModel();
    DefaultTableModel table = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    int toernooideelnemernummer = 0;
    boolean heeftbetaald = false;

    boolean isWelBetaald = false;
    boolean isNietBetaald = false;
    int tableClick;
    int isBetaald;
    String toernooicode = "";
    String Table_click;
    int aantalFiches;
    int Table_id_click;

    public Masterclass_Betalen() {
        initComponents();
        setLocationRelativeTo(null);

        IngeschrevenDeelnemer.setModel(spelerListModel);

        TableToernooi.setModel(table);
        String[] Kolomnaam = {"Id", "Naam", "Datum", "Max spelers", "Betaald", "Niet Betaald"};
        table.setColumnIdentifiers(Kolomnaam);
        table.setRowCount(0);
        table.setColumnCount(6);

        tabelVullen();
        tableEigenschappen();

    }

    private void tableEigenschappen() {

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        TableToernooi.getColumn("Id").setCellRenderer(rightRenderer);
        TableToernooi.getColumn("Naam").setCellRenderer(rightRenderer);
        TableToernooi.getColumn("Datum").setCellRenderer(rightRenderer);
        TableToernooi.getColumn("Max spelers").setCellRenderer(rightRenderer);
        TableToernooi.getColumn("Betaald").setCellRenderer(rightRenderer);
        TableToernooi.getColumn("Niet Betaald").setCellRenderer(rightRenderer);

        TableCellRenderer rendererFromHeader = TableToernooi.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.RIGHT);

    }

    private void tabelVullen() {

        Sql_connect.doConnect();

        String id, naam, datum, Max_inschrijvingen_T;
        int Betaald, Niet_Betaald;

        try {

            Sql_connect.doConnect();
            String zoekVeld = zoekToernooiTxt.getText();

            String prepSqlStatement = ""
                    + "SELECT M.Id_masterclass ,M.Naam_masterclass, M.Datum, M.Max_inschrijvingen_M,"
                    + "(count(MD.Id_persoon) - SUM(MD.Betaald)) as nietBetaald, "
                    + "SUM(MD.Betaald) as betaald "
                    + "FROM masterclass M "
                    + "JOIN masterclassdeelnemer MD "
                    + "ON MD.Id_masterclass = M.Id_masterclass "
                    + "WHERE M.Naam_masterclass like ?"
                    + "GROUP BY M.Id_masterclass";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, "%" + zoekVeld + "%");

            ResultSet result = stat.executeQuery();

            int i = 0;

            while (result.next()) {
                i++;

            }
            table.setRowCount(i);
            result.beforeFirst();

            int d = 0;
            while (result.next()) {

                id = result.getString("Id_masterclass");
                naam = result.getString("Naam_masterclass");
                datum = result.getString("Datum");
                Max_inschrijvingen_T = result.getString("Max_inschrijvingen_M");
                Betaald = result.getInt("betaald");
                Niet_Betaald = result.getInt("nietBetaald");

                table.setValueAt(id, d, 0);
                table.setValueAt(naam, d, 1);
                table.setValueAt(datum, d, 2);
                table.setValueAt(Max_inschrijvingen_T, d, 3);
                table.setValueAt(Betaald, d, 4);
                table.setValueAt(Niet_Betaald, d, 5);

                d++;

            }

            result.last();
            result.close();
            stat.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }

    private void gegevensOphalenTabel() {

        try {
            spelerListModel.removeAllElements();
            int row = TableToernooi.getSelectedRow();

            Table_click = TableToernooi.getModel().getValueAt(row, 0).toString();
            TxtToernooi_ID.setText(Table_click);
            
            if (isBetaald == 1){
                welBetaald();
            } else {
                nietBetaald();
            }
            
            Sql_connect.doConnect();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void gegevensLijst() {
        try {
            if (IngeschrevenDeelnemer.getSelectedValue() == null) {
                MELDINGFIELD.setText("Niets Geselecteerd.");
            } else {
                ModelItem selectedItem = (ModelItem) IngeschrevenDeelnemer.getSelectedValue();
                TxtSpelerId.setText(Integer.toString(selectedItem.id));
                isBetaald = selectedItem.isBetaald;
                aantalFiches = selectedItem.aantalFiches;

                if (isBetaald == 0) {
                    TxtBetaald.setText("Nee");
                } else if (isBetaald == 1) {
                    TxtBetaald.setText("Ja");
                }
                MELDINGFIELD.setText("Opvraag ID gelukt!");
            }
        } catch (Exception e) {
            MELDINGFIELD.setText("Geen naam geselecteerd!");
        }

    }

    private void nietBetaald() {

        try {
            Sql_connect.doConnect();
            String zoekVeld = removeLastChar(ZoekSpelerTxt.getText());
            ResultSet result;
            Table_id_click = Integer.parseInt(Table_click);

            String[] parts = zoekVeld.split(" ");
            int partsLength = parts.length;
            if (partsLength == 2){
                
                String voornaam = parts[0];
                String achternaam = parts[1];

            String prepSqlStatementVoorActer = "select p.Voornaam, p.Achternaam, p.Id_persoon, MD.Betaald, MD.Ingeschreven from persoon p "
                    + "JOIN masterclassdeelnemer MD on MD.Id_persoon = p.Id_persoon where Betaald = 0 AND MD.Id_masterclass = ? AND p.Voornaam like ? AND p.Achternaam like?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatementVoorActer);
            stat.setInt(1, Table_id_click);
            stat.setString(2, "%"+voornaam+"%");
            stat.setString(3, "%"+achternaam+"%");

            result = stat.executeQuery();
            result.beforeFirst();
            } else {
                String prepSqlStatementVoorActer = "select p.Voornaam, p.Achternaam, p.Id_persoon, MD.Betaald, MD.Ingeschreven from persoon p "
                    + "JOIN masterclassdeelnemer MD on MD.Id_persoon = p.Id_persoon where Betaald = 0 AND MD.Id_masterclass = ? AND p.Voornaam like ?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatementVoorActer);
            stat.setInt(1, Table_id_click);
            stat.setString(2, "%"+zoekVeld+"%");
            
            result = stat.executeQuery();
            result.beforeFirst();
            }

            spelerListModel.removeAllElements();

            while (result.next()) {
                ModelItem item = new ModelItem();
                item.id = result.getInt("Id_persoon");
                item.voornaam = result.getString("voornaam");
                item.achternaam = result.getString("achternaam");
                item.isBetaald = result.getInt("Betaald");
                spelerListModel.addElement(item);

                MELDINGFIELD.setText("Opvragen lijst gelukt!");

            }

        } catch (Exception e) {
            ePopup(e);
        }

    }

    private void welBetaald() {

        try {
            Sql_connect.doConnect();
            String zoekVeld = removeLastChar(ZoekSpelerTxt.getText());
            ResultSet result;
            Table_id_click = Integer.parseInt(Table_click);

            String[] parts = zoekVeld.split(" ");
            int partsLength = parts.length;
            if (partsLength == 2){
                
                String voornaam = parts[0];
                String achternaam = parts[1];

            String prepSqlStatementVoorActer = "select p.Voornaam, p.Achternaam, p.Id_persoon, MD.Betaald, MD.Ingeschreven from persoon p "
                    + "JOIN masterclassdeelnemer MD on MD.Id_persoon = p.Id_persoon where Betaald = 1 AND MD.Id_masterclass = ? AND p.Voornaam like ? AND p.Achternaam like?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatementVoorActer);
            stat.setInt(1, Table_id_click);
            stat.setString(2, "%"+voornaam+"%");
            stat.setString(3, "%"+achternaam+"%");

            result = stat.executeQuery();
            result.beforeFirst();
            } else {
                String prepSqlStatementVoorActer = "select p.Voornaam, p.Achternaam, p.Id_persoon, MD.Betaald, MD.Ingeschreven from persoon p "
                    + "JOIN masterclassdeelnemer MD on MD.Id_persoon = p.Id_persoon where Betaald = 1 AND MD.Id_masterclass = ? AND p.Voornaam like ?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatementVoorActer);
            stat.setInt(1, Table_id_click);
            stat.setString(2, "%"+zoekVeld+"%");
            
            result = stat.executeQuery();
            result.beforeFirst();
            }

            spelerListModel.removeAllElements();

            while (result.next()) {
                ModelItem item = new ModelItem();
                item.id = result.getInt("Id_persoon");
                item.voornaam = result.getString("voornaam");
                item.achternaam = result.getString("achternaam");
                item.isBetaald = result.getInt("Betaald");
                spelerListModel.addElement(item);

                MELDINGFIELD.setText("Opvragen lijst gelukt!");

            }

        } catch (Exception e) {
            ePopup(e);
        }

    }

    private void vulLijst() {
        try {
            Sql_connect.doConnect();
            String zoekVeld = ZoekSpelerTxt.getText();
            ResultSet result;

            String[] parts = zoekVeld.split(" ");

            String prepSqlStatementVoorActer = "select p.Voornaam, p.Achternaam, p.Id_persoon, MD.Betaald, MD.Ingeschreven from persoon p "
                    + "JOIN masterclassdeelnemer MD on MD.Id_persoon = p.Id_persoon where MD.Id_masterclass = ?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatementVoorActer);
            stat.setInt(1, Table_id_click);

            result = stat.executeQuery();

            spelerListModel.removeAllElements();
            result.beforeFirst();

            while (result.next()) {
                ModelItem item = new ModelItem();
                item.id = result.getInt("Id_persoon");
                item.voornaam = result.getString("voornaam");
                item.achternaam = result.getString("achternaam");
                item.isBetaald = result.getInt("TD.IsBetaald");
                spelerListModel.addElement(item);

                MELDINGFIELD.setText("Opvragen lijst gelukt!");

            }

        } catch (Exception e) {
            ePopup(e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableToernooi = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        IngeschrevenDeelnemer = new javax.swing.JList();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        ZoekSpelerTxt = new javax.swing.JTextField();
        zoekToernooiTxt = new javax.swing.JTextField();
        MELDINGFIELD = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TxtSpelerId = new javax.swing.JTextField();
        TxtBetaald = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        Updaten = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        TxtToernooi_ID = new javax.swing.JTextField();
        betaaldbutton = new javax.swing.JToggleButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setPreferredSize(new java.awt.Dimension(634, 430));

        TableToernooi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TableToernooi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableToernooiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TableToernooi);

        IngeschrevenDeelnemer.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        IngeschrevenDeelnemer.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                IngeschrevenDeelnemerValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(IngeschrevenDeelnemer);

        jLabel8.setText("Masterclass zoeken: ");

        jLabel5.setText("Speler zoeken:");

        ZoekSpelerTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ZoekSpelerTxtKeyReleased(evt);
            }
        });

        zoekToernooiTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zoekToernooiTxtKeyReleased(evt);
            }
        });

        MELDINGFIELD.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel3.setText("Spelers code");

        TxtSpelerId.setEditable(false);

        TxtBetaald.setEditable(false);

        jLabel4.setText("Betaald");

        Updaten.setText("Update");
        Updaten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdatenActionPerformed(evt);
            }
        });

        jLabel6.setText("Toernooi");

        TxtToernooi_ID.setEditable(false);

        betaaldbutton.setText("Heeft niet betaald");
        betaaldbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                betaaldbuttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(TxtSpelerId, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(36, 36, 36)
                                        .addComponent(TxtToernooi_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(TxtBetaald, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Updaten, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(zoekToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(betaaldbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 36, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(ZoekSpelerTxt))))
                .addGap(17, 17, 17))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel5)
                    .addComponent(ZoekSpelerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zoekToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(TxtToernooi_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(betaaldbutton))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(TxtSpelerId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(TxtBetaald, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Updaten)))
                    .addComponent(jScrollPane1))
                .addGap(26, 26, 26)
                .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton2.setText("Terug");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UpdatenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdatenActionPerformed

        updateBetalen();
    }//GEN-LAST:event_UpdatenActionPerformed

    private void updateBetalen() {

        try {
            Sql_connect.doConnect();

            int krijgId = Integer.parseInt(TxtSpelerId.getText());
            ModelItem selectedItem = (ModelItem) IngeschrevenDeelnemer.getSelectedValue();
            isBetaald = selectedItem.isBetaald;
            aantalFiches = selectedItem.aantalFiches;

            if (isBetaald == 0) {
                String prepSqlStatementVoorActer = "UPDATE masterclassdeelnemer set Betaald = 1, Ingeschreven = 1 WHERE Id_masterclass = ? AND Id_persoon = ?";
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatementVoorActer);
                stat.setInt(1, Table_id_click);
                stat.setInt(2, krijgId);
                
                stat.executeUpdate();

                nietBetaald();
                tabelVullen();

            } else if (isBetaald == 1) {
                String prepSqlStatementVoorActer = "UPDATE masterclassdeelnemer set Betaald = 0, Ingeschreven = 0 WHERE Id_masterclass = ? AND Id_persoon = ?";

                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatementVoorActer);
                stat.setInt(1, Table_id_click);
                stat.setInt(2, krijgId);

                stat.executeUpdate();

                welBetaald();
                tabelVullen();

            }

        } catch (Exception e) {
            //ePopup(e);
            ePopup(e);
        }

    }

    private void close() {
        this.dispose();
        Main menu = new Main();
        menu.setVisible(rootPaneCheckingEnabled);
    }

    private void ZoekSpelerTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ZoekSpelerTxtKeyReleased
        if (isBetaald == 1){
                welBetaald();
            } else {
                nietBetaald();
            }
    }//GEN-LAST:event_ZoekSpelerTxtKeyReleased

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

    private void ePopup(Exception e) {
        final String eMessage = "Er is iets fout gegaan, neem contact op met de aplicatiebouwer, geef deze foutmelding door: ";
        String error = eMessage + e;
        JOptionPane.showMessageDialog(rootPane, error);
    }

    private void TableToernooiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableToernooiMouseClicked
        gegevensOphalenTabel();
    }//GEN-LAST:event_TableToernooiMouseClicked

    private void zoekToernooiTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zoekToernooiTxtKeyReleased

        tabelVullen();
    }//GEN-LAST:event_zoekToernooiTxtKeyReleased

    private void IngeschrevenDeelnemerValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_IngeschrevenDeelnemerValueChanged

        gegevensLijst();
    }//GEN-LAST:event_IngeschrevenDeelnemerValueChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        close();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void betaaldbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_betaaldbuttonActionPerformed
        // TODO add your handling code here:
         if(betaaldbutton.getText().equals("Heeft niet betaald"))
        {
            betaaldbutton.setText("Heeft wel betaald");
            isBetaald = 1;
        } else if(betaaldbutton.getText().equals("Heeft wel betaald"))
        {
            betaaldbutton.setText("Heeft niet betaald");
            isBetaald = 0;
        } 
         gegevensOphalenTabel();
    }//GEN-LAST:event_betaaldbuttonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Masterclass_Betalen.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Masterclass_Betalen().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList IngeschrevenDeelnemer;
    private javax.swing.JLabel MELDINGFIELD;
    private javax.swing.JTable TableToernooi;
    private javax.swing.JTextField TxtBetaald;
    private javax.swing.JTextField TxtSpelerId;
    private javax.swing.JTextField TxtToernooi_ID;
    private javax.swing.JButton Updaten;
    private javax.swing.JTextField ZoekSpelerTxt;
    private javax.swing.JToggleButton betaaldbutton;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField zoekToernooiTxt;
    // End of variables declaration//GEN-END:variables
}
