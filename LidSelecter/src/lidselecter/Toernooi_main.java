/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lidselecter;

import java.awt.Color;
import java.sql.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author chris overzicht van toernooien
 *
 * datum plaats id max aantal spelers wanneer geselecteerd geef proces en alles
 * uit database
 */
public class Toernooi_main extends javax.swing.JFrame {

    //private Connection conn = null;
    // private Statement stmnt = null;
    //private ResultSet rs = null;
    ////private String sql;
    // private PreparedStatement pst = null;
    private final DefaultTableModel table = new DefaultTableModel();

    private String meeneemId;
    private String meeneemNaam;
    /**
     * Creates new form Toernooien
     */
    public Toernooi_main() {
        initComponents();
        setLocationRelativeTo(null);
        minSpelersTxt.setText("0");

        toernooiTabel.setModel(table);
        String[] Kolomnaam = {"Toernooi id", "Naam", "Datum", "Kosten", "Max spelers", "Per tafel", "Kaart code", "Plaats code", "Type"};
        table.setColumnIdentifiers(Kolomnaam);
        table.setRowCount(0);
        table.setColumnCount(9);

        tabelVullen();
        tableEigenschappen();
    }
    //vult de progress bar
    private void updateProgressBar()
    {
                try {
            int row = toernooiTabel.getSelectedRow();

            String Table_click = toernooiTabel.getModel().getValueAt(row, 0).toString();
            Sql_connect.doConnect();

            String prepSqlStatement = "select count(Id_persoon) as inschrijvingen from toernooideelnemer where Id_toernooi = '" + Table_click + "'";
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
    }

    // hierin wordt gezorgd dat de inhoud rechts staat
    private void tableEigenschappen() {

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        toernooiTabel.getColumn("Toernooi id").setCellRenderer(rightRenderer);
        toernooiTabel.getColumn("Naam").setCellRenderer(rightRenderer);
        toernooiTabel.getColumn("Datum").setCellRenderer(rightRenderer);
        toernooiTabel.getColumn("Kosten").setCellRenderer(rightRenderer);
        toernooiTabel.getColumn("Max spelers").setCellRenderer(rightRenderer);
        toernooiTabel.getColumn("Per tafel").setCellRenderer(rightRenderer);
        toernooiTabel.getColumn("Kaart code").setCellRenderer(rightRenderer);
        toernooiTabel.getColumn("Plaats code").setCellRenderer(rightRenderer);
        toernooiTabel.getColumn("Type").setCellRenderer(rightRenderer);

        TableCellRenderer rendererFromHeader = toernooiTabel.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.RIGHT);

    }

    private void ePopup(Exception e) {
        final String eMessage = "Er is iets fout gegaan, neem contact op met de aplicatiebouwer, geef deze foutmelding door: ";
        String error = eMessage + e;
        JOptionPane.showMessageDialog(rootPane, error);
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

    // hierin worden de gegevens opgeroepen om in de tabel te zetten
    private void tabelVullen() {
        // TODO add your handling code here:
        Sql_connect.doConnect();
        // declareer de variable voor in de rs
        String id;
        String naam;
        String datum;
        String kosten;
        String Max_inschrijvingen_T;
        String Max_speler_per_tafel;
        String kaartCode;
        String plaats;
        String kaartType;

        try {
            // connect 
            Sql_connect.doConnect();

            String zoekVeld = zoekTxt.getText();

            // statement maken
            String prepSqlStatement = "select * from toernooi where Naam like ?;";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, "%" + zoekVeld + "%");
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
                id = result.getString("Id_toernooi");
                naam = result.getString("Naam");
                datum = result.getString("Datum");
                kosten = result.getString("Inschrijfkosten");
                Max_inschrijvingen_T = result.getString("Max_inschrijvingen_T");
                Max_speler_per_tafel = result.getString("Max_speler_per_tafel");
                kaartCode = result.getString("Kaartspel_code");
                plaats = result.getString("Id_locatie");
                kaartType = result.getString("Kaartspeltype");

                // vul vervolgens in de tabel de waardes in als volgt: resultset, aantal, plaats
                table.setValueAt(id, d, 0);
                table.setValueAt(naam, d, 1);
                table.setValueAt(datum, d, 2);
                table.setValueAt(kosten, d, 3);
                table.setValueAt(Max_inschrijvingen_T, d, 4);
                table.setValueAt(Max_speler_per_tafel, d, 5);
                table.setValueAt(kaartCode, d, 6);
                table.setValueAt(plaats, d, 7);
                table.setValueAt(kaartType, d, 8);
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

    // hierin worden de gegevens opgeroepen om weer te geven in jtextfielden met de overige eigenschappen
    // ook wordt hier de max inschrijvingen gevuld
    private void gegevensOphalen() {

        try {
            int row = toernooiTabel.getSelectedRow();

            String Table_click = toernooiTabel.getModel().getValueAt(row, 0).toString();
            Sql_connect.doConnect();

            // statement maken
            String prepSqlStatement = "select * from toernooi where Id_toernooi = '" + Table_click + "'";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();

            if (result.next()) {
                
                String add6 = result.getString("Max_inschrijvingen_T");
                maxSpelersTxt.setText(add6);
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

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        toernooiTabel = new javax.swing.JTable();
        zoekTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        progressBar = new javax.swing.JProgressBar();
        jLabel6 = new javax.swing.JLabel();
        minSpelersTxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        maxSpelersTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        zoekTxt2 = new javax.swing.JTextField();
        inschrijvenToernooiButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jButton1.setText("Terug");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        toernooiTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Toenooi id", "Datum", "Plaats", "Max Spelers"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        toernooiTabel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                toernooiTabelFocusGained(evt);
            }
        });
        toernooiTabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toernooiTabelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(toernooiTabel);
        if (toernooiTabel.getColumnModel().getColumnCount() > 0) {
            toernooiTabel.getColumnModel().getColumn(0).setHeaderValue("Toenooi id");
            toernooiTabel.getColumnModel().getColumn(1).setHeaderValue("Datum");
            toernooiTabel.getColumnModel().getColumn(2).setHeaderValue("Plaats");
            toernooiTabel.getColumnModel().getColumn(3).setHeaderValue("Max Spelers");
        }

        zoekTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zoekTxtKeyReleased(evt);
            }
        });

        jLabel8.setText("Toernooi zoeken:");

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        progressBar.setForeground(new java.awt.Color(153, 153, 255));

        jLabel6.setText("Ingeschreven spelers:");

        minSpelersTxt.setEditable(false);

        jLabel7.setText("Van:");

        maxSpelersTxt.setEditable(false);

        jLabel1.setText("Speler zoeken:");

        inschrijvenToernooiButton.setText("Inschrijven Toernooi");
        inschrijvenToernooiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inschrijvenToernooiButtonActionPerformed(evt);
            }
        });

        startButton.setText("Start toernooi");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(269, 269, 269)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1))
                            .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(minSpelersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(maxSpelersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(inschrijvenToernooiButton))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(zoekTxt2))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inschrijvenToernooiButton)
                            .addComponent(startButton)
                            .addComponent(maxSpelersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(minSpelersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(zoekTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        Main menu = new Main();
        menu.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void toernooiTabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_toernooiTabelMouseClicked
        // TODO add your handling code here:
        gegevensOphalen();
        updateProgressBar();
    }//GEN-LAST:event_toernooiTabelMouseClicked

    private void inschrijvenToernooiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inschrijvenToernooiButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        Toernooi_inschrijven Toernooi_inschrijven = new Toernooi_inschrijven();
        Toernooi_inschrijven.setVisible(rootPaneCheckingEnabled);

    }//GEN-LAST:event_inschrijvenToernooiButtonActionPerformed

    private void toernooiTabelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_toernooiTabelFocusGained
        
    }//GEN-LAST:event_toernooiTabelFocusGained

    private void zoekTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zoekTxtKeyReleased
        // TODO add your handling code here:
        tabelVullen();
    }//GEN-LAST:event_zoekTxtKeyReleased

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        // TODO add your handling code here:
        //toernooiMeenemen();
        
        Toernooi_start Toernooi_start = new Toernooi_start();
        toernooiMeenemen();
        Toernooi_start.idToernooiTxt.setText(meeneemId);
        Toernooi_start.naamToernooiTxt.setText(meeneemNaam);
        
        this.dispose();
        Toernooi_start.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_startButtonActionPerformed
    
    private void toernooiMeenemen(){
        try {
            int row = toernooiTabel.getSelectedRow();

            String Table_click = toernooiTabel.getModel().getValueAt(row, 0).toString();
            Sql_connect.doConnect();

            // statement maken
            String prepSqlStatement = "select * from toernooi where Id_toernooi = '" + Table_click + "'";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();

            if (result.next()) {
                
                meeneemId = result.getString("Id_toernooi");
                meeneemNaam = result.getString("Naam");
                
            }

        } catch (Exception e) {
            System.out.println(e);
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
            java.util.logging.Logger.getLogger(Toernooi_main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Toernooi_main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Toernooi_main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Toernooi_main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Toernooi_main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton inschrijvenToernooiButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField maxSpelersTxt;
    private javax.swing.JTextField minSpelersTxt;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton startButton;
    public javax.swing.JTable toernooiTabel;
    private javax.swing.JTextField zoekTxt;
    private javax.swing.JTextField zoekTxt2;
    // End of variables declaration//GEN-END:variables
}