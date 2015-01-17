/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lidselecter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Aaik
 */
public class LedenOverzicht extends javax.swing.JFrame {

    private final DefaultTableModel table = new DefaultTableModel();

    /**
     * Creates new form LedenOverzicht
     */
    public LedenOverzicht() {
        initComponents();
        setLocationRelativeTo(null);

        ledenTable.setModel(table);
        String[] Kolomnaam
                = {"ID",
                    "Voornaam",
                    "Achternaam",
                    "Adres",
                    "Postcode",
                    "Woonplaats",
                    "Land",
                    "Email",
                    "Tel.",
                    "Rating"
                };
        table.setColumnIdentifiers(Kolomnaam);
        table.setRowCount(0);
        table.setColumnCount(10);

        tabelVullen();
        tableEigenschappen();
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
             
    // hierin worden de gegevens opgeroepen om in de tabel te zetten
    private void tabelVullen() {
        // TODO add your handling code here:
        Sql_connect.doConnect();
        // declareer de variable voor in de rs
        String Id_persoon, Voornaam, Achternaam, Adres, Postcode, Woonplaats, Land, Email, Telefoonnummer, Rating;

        try {
            // connect 
            Sql_connect.doConnect();

            String zoekVeld = removeLastChar(zoekTxt.getText());
             
            String[] parts = zoekVeld.split(" ");
            int partsLength = parts.length;

            if (partsLength == 2) {

                String voornaam = parts[0];
                String achternaam = parts[1];

                // statement maken
                String prepSqlStatement = "select * from persoon where (Voornaam like ? AND Achternaam like ?);";
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
                stat.setString(1, "%" + voornaam + "%");
                stat.setString(2, "%" + achternaam + "%");
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
                    Id_persoon = result.getString("Id_persoon");
                    Voornaam = result.getString("Voornaam");
                    Achternaam = result.getString("Achternaam");
                    Adres = result.getString("Adres");
                    Postcode = result.getString("Postcode");
                    Woonplaats = result.getString("Woonplaats");
                    Land = result.getString("Land");
                    Email = result.getString("Email");
                    Telefoonnummer = result.getString("Telefoonnummer");
                    Rating = result.getString("Rating");

                    // vul vervolgens in de tabel de waardes in als volgt: resultset, aantal, plaats
                    table.setValueAt(Id_persoon, d, 0);
                    table.setValueAt(Voornaam, d, 1);
                    table.setValueAt(Achternaam, d, 2);
                    table.setValueAt(Adres, d, 3);
                    table.setValueAt(Postcode, d, 4);
                    table.setValueAt(Woonplaats, d, 5);
                    table.setValueAt(Land, d, 6);
                    table.setValueAt(Email, d, 7);
                    table.setValueAt(Telefoonnummer, d, 8);
                    table.setValueAt(Rating, d, 9);
                    // verhoog aantal totdat alles was je hebt opgevraagd is geweest
                    d++;

                }

                result.last();
                System.out.println(result.getRow());

                result.close();
                stat.close();
            } // if statement
            else {

                String prepSqlStatement = "select * from persoon where (Voornaam like ?);";
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
                    Id_persoon = result.getString("Id_persoon");
                    Voornaam = result.getString("Voornaam");
                    Achternaam = result.getString("Achternaam");
                    Adres = result.getString("Adres");
                    Postcode = result.getString("Postcode");
                    Woonplaats = result.getString("Woonplaats");
                    Land = result.getString("Land");
                    Email = result.getString("Email");
                    Telefoonnummer = result.getString("Telefoonnummer");
                    Rating = result.getString("Rating");

                    // vul vervolgens in de tabel de waardes in als volgt: resultset, aantal, plaats
                    table.setValueAt(Id_persoon, d, 0);
                    table.setValueAt(Voornaam, d, 1);
                    table.setValueAt(Achternaam, d, 2);
                    table.setValueAt(Adres, d, 3);
                    table.setValueAt(Postcode, d, 4);
                    table.setValueAt(Woonplaats, d, 5);
                    table.setValueAt(Land, d, 6);
                    table.setValueAt(Email, d, 7);
                    table.setValueAt(Telefoonnummer, d, 8);
                    table.setValueAt(Rating, d, 9);
                    // verhoog aantal totdat alles was je hebt opgevraagd is geweest
                    d++;

                }

                result.last();
                System.out.println(result.getRow());

                result.close();
                stat.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }

    private void tableEigenschappen() {

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        ledenTable.getColumn("ID").setCellRenderer(rightRenderer);
        ledenTable.getColumn("Voornaam").setCellRenderer(rightRenderer);
        ledenTable.getColumn("Achternaam").setCellRenderer(rightRenderer);
        ledenTable.getColumn("Adres").setCellRenderer(rightRenderer);
        ledenTable.getColumn("Postcode").setCellRenderer(rightRenderer);
        ledenTable.getColumn("Woonplaats").setCellRenderer(rightRenderer);
        ledenTable.getColumn("Land").setCellRenderer(rightRenderer);
        ledenTable.getColumn("Email").setCellRenderer(rightRenderer);
        ledenTable.getColumn("Tel.").setCellRenderer(rightRenderer);
        ledenTable.getColumn("Rating").setCellRenderer(rightRenderer);

        TableCellRenderer rendererFromHeader = ledenTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.RIGHT);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ledenTable = new javax.swing.JTable();
        backToMain = new javax.swing.JButton();
        zoekTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ledenTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Voornaam", "Achternaam", "Straat", "Postcode", "Woonplaats", "Land", "Email", "Tel. Nummer", "Rating"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ledenTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ledenTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(ledenTable);

        backToMain.setText("Terug");
        backToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToMainActionPerformed(evt);
            }
        });

        zoekTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zoekTxtKeyReleased(evt);
            }
        });

        jLabel1.setText("Zoeken");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 762, Short.MAX_VALUE)
                .addComponent(backToMain))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(zoekTxt)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(backToMain))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ledenTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ledenTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ledenTableMouseClicked

    private void backToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToMainActionPerformed
        this.dispose();
        Main Main = new Main();
        Main.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_backToMainActionPerformed

    private void zoekTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zoekTxtKeyReleased
        // TODO add your handling code here:
        tabelVullen();
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
            java.util.logging.Logger.getLogger(LedenOverzicht.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LedenOverzicht.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LedenOverzicht.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LedenOverzicht.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LedenOverzicht().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backToMain;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable ledenTable;
    private javax.swing.JTextField zoekTxt;
    // End of variables declaration//GEN-END:variables
}
