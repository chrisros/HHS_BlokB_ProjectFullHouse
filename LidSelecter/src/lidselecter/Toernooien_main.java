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
public class Toernooien_main extends javax.swing.JFrame {

    //private Connection conn = null;
    // private Statement stmnt = null;
    //private ResultSet rs = null;
    ////private String sql;
    // private PreparedStatement pst = null;
    private final DefaultTableModel table = new DefaultTableModel();

    /**
     * Creates new form Toernooien
     */
    public Toernooien_main() {
        initComponents();
        setLocationRelativeTo(null);
        minSpelersTxt.setText("0");

        toernooiTabel.setModel(table);
        String[] Kolomnaam = {"Toernooi id", "Datum", "Plaats code", "Max spelers"};
        table.setColumnIdentifiers(Kolomnaam);
        table.setRowCount(0);
        table.setColumnCount(4);

        toernooiVullen();
        tableEigenschappen();
    }

    // hierin wordt gezorgd dat de inhoud rechts staat
    private void tableEigenschappen() {

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        toernooiTabel.getColumn("Toernooi id").setCellRenderer(rightRenderer);
        toernooiTabel.getColumn("Datum").setCellRenderer(rightRenderer);
        toernooiTabel.getColumn("Plaats code").setCellRenderer(rightRenderer);
        toernooiTabel.getColumn("Max spelers").setCellRenderer(rightRenderer);

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
    private void toernooiVullen() {
        // TODO add your handling code here:
        Sql_connect.doConnect();
        // declareer de variable voor in de rs
        String id;
        String naam;
        String plaats;
        String Max_inschrijvingen_T;

        try {
            // connect 
            Sql_connect.doConnect();
            // statement maken
            String prepSqlStatement = "select * from toernooi;";
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
                id = result.getString("Id_toernooi");
                naam = result.getString("Datum");
                plaats = result.getString("Id_locatie");
                Max_inschrijvingen_T = result.getString("Max_inschrijvingen_T");

                // vul vervolgens in de tabel de waardes in als volgt: resultset, aantal, plaats
                table.setValueAt(id, d, 0);
                table.setValueAt(naam, d, 1);
                table.setValueAt(plaats, d, 2);
                table.setValueAt(Max_inschrijvingen_T, d, 3);
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
                String add1 = result.getString("Naam");
                naamTxt.setText(add1);
                String add2 = result.getString("Inschrijfkosten");
                inschrijfKostTxt.setText("€" + add2);
                String add3 = result.getString("Max_speler_per_tafel");
                spelersPrTafelTxt.setText(add3);
                String add4 = result.getString("Kaartspel_code");
                kaartspelTxt.setText(add4);
                String add5 = result.getString("Kaartspeltype");
                spelTypeTxt.setText(add5);
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
        inschrijfProcesButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        minSpelersTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        naamTxt = new javax.swing.JTextField();
        inschrijfKostTxt = new javax.swing.JTextField();
        spelersPrTafelTxt = new javax.swing.JTextField();
        kaartspelTxt = new javax.swing.JTextField();
        spelTypeTxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        maxSpelersTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        toernooiTabel = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        inschrijvenToernooiButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        inschrijfProcesButton.setText("Check inschrijfproces");
        inschrijfProcesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inschrijfProcesButtonActionPerformed(evt);
            }
        });

        progressBar.setForeground(new java.awt.Color(153, 153, 255));

        minSpelersTxt.setEditable(false);

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel1.setText("Naam");

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel2.setText("Inschrijfkosten");

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel3.setText("Spelers per tafel");

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel4.setText("Kaartspel");

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel5.setText("Speltype");

        naamTxt.setEditable(false);

        inschrijfKostTxt.setEditable(false);

        spelersPrTafelTxt.setEditable(false);

        kaartspelTxt.setEditable(false);

        spelTypeTxt.setEditable(false);

        jLabel6.setText("Ingeschreven spelers");

        jLabel7.setText("van");

        maxSpelersTxt.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(naamTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inschrijfKostTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spelersPrTafelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(kaartspelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spelTypeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(inschrijfProcesButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(minSpelersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(maxSpelersTxt))
                            .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {inschrijfKostTxt, kaartspelTxt, naamTxt, spelTypeTxt, spelersPrTafelTxt});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(naamTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inschrijfKostTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spelersPrTafelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kaartspelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spelTypeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(minSpelersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(maxSpelersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(inschrijfProcesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {inschrijfKostTxt, kaartspelTxt, naamTxt, spelTypeTxt, spelersPrTafelTxt});

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

        jButton1.setText("Terug");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        inschrijvenToernooiButton.setText("Inschrijven Toernooi");
        inschrijvenToernooiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inschrijvenToernooiButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(inschrijvenToernooiButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(inschrijvenToernooiButton)))
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

    }//GEN-LAST:event_toernooiTabelMouseClicked

    private void inschrijvenToernooiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inschrijvenToernooiButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        Toernooi_inschrijven Toernooi_inschrijven = new Toernooi_inschrijven();
        Toernooi_inschrijven.setVisible(rootPaneCheckingEnabled);

    }//GEN-LAST:event_inschrijvenToernooiButtonActionPerformed

    private void inschrijfProcesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inschrijfProcesButtonActionPerformed
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

    }//GEN-LAST:event_inschrijfProcesButtonActionPerformed

    private void toernooiTabelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_toernooiTabelFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_toernooiTabelFocusGained
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
            java.util.logging.Logger.getLogger(Toernooien_main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Toernooien_main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Toernooien_main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Toernooien_main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Toernooien_main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField inschrijfKostTxt;
    private javax.swing.JButton inschrijfProcesButton;
    private javax.swing.JButton inschrijvenToernooiButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField kaartspelTxt;
    private javax.swing.JTextField maxSpelersTxt;
    private javax.swing.JTextField minSpelersTxt;
    private javax.swing.JTextField naamTxt;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextField spelTypeTxt;
    private javax.swing.JTextField spelersPrTafelTxt;
    private javax.swing.JTable toernooiTabel;
    // End of variables declaration//GEN-END:variables
}
