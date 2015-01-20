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
public class Locatie_main extends javax.swing.JFrame {

    private final DefaultTableModel table = new DefaultTableModel();

    /**
     * Creates new form Locatie_main
     */
    public Locatie_main() {
        initComponents();
        setLocationRelativeTo(null);

        locatieTable.setModel(table);
        String[] Kolomnaam = {"Locatie id", "Naam", "Plaats", "Straat", "Huisnummer", "Telefoonnummer"};
        table.setColumnIdentifiers(Kolomnaam);
        table.setRowCount(0);
        table.setColumnCount(6);

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
     
    public void tabelVullen() {

        // TODO add your handling code here:
        Sql_connect.doConnect();
        // declareer de variable voor in de rs
        String id;
        String naam;
        String plaats;
        String Straat;
        String Huisnummer;
        String Telefoonnummer;

        try {
            // connect 
            Sql_connect.doConnect();

            String zoekVeld = removeLastChar(zoekTxt.getText());

            ResultSet result; 
                // statement maken
            if (zoekVeld.equals(""))
            {
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT * FROM locatie");
                result = stat.executeQuery();
            } else{
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("select * from locatie where Naam_locatie like ? OR Plaats like ?;");
                stat.setString(1, "%" + zoekVeld + "%");
                stat.setString(2, "%" + zoekVeld + "%");
                result = stat.executeQuery();
            }
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
                    id = result.getString("Id_locatie");
                    naam = result.getString("Naam_locatie");
                    plaats = result.getString("plaats");
                    Straat = result.getString("Straat");
                    Huisnummer = result.getString("Huisnummer");
                    Telefoonnummer = result.getString("Telefoonnummer");

                    // vul vervolgens in de tabel de waardes in als volgt: resultset, aantal, plaats
                    table.setValueAt(id, d, 0);
                    table.setValueAt(naam, d, 1);
                    table.setValueAt(plaats, d, 2);
                    table.setValueAt(Straat, d, 3);
                    table.setValueAt(Huisnummer, d, 4);
                    table.setValueAt(Telefoonnummer, d, 5);
                    // verhoog aantal totdat alles was je hebt opgevraagd is geweest
                    d++;

                }

                result.last();
                System.out.println(result.getRow());

                result.close();
                //stat.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }

    }

    private void tableEigenschappen() {

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        locatieTable.getColumn("Locatie id").setCellRenderer(rightRenderer);
        locatieTable.getColumn("Naam").setCellRenderer(rightRenderer);
        locatieTable.getColumn("Plaats").setCellRenderer(rightRenderer);
        locatieTable.getColumn("Straat").setCellRenderer(rightRenderer);
        locatieTable.getColumn("Huisnummer").setCellRenderer(rightRenderer);
        locatieTable.getColumn("Telefoonnummer").setCellRenderer(rightRenderer);

        TableCellRenderer rendererFromHeader = locatieTable.getTableHeader().getDefaultRenderer();
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

        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        locatieTable = new javax.swing.JTable();
        locatieToevoegenButton = new javax.swing.JButton();
        terugButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        zoekTxt = new javax.swing.JTextField();

        jButton2.setText("Terug");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        locatieTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Locatie Id", "Naam", "Plaats", "Straat", "Huisnummer", "Telefoonnumer"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        locatieTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                locatieTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(locatieTable);

        locatieToevoegenButton.setText("Locatie toevoegen");
        locatieToevoegenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locatieToevoegenButtonActionPerformed(evt);
            }
        });

        terugButton.setText("Terug");
        terugButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terugButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Zoeken");

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
                .addComponent(locatieToevoegenButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(terugButton))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(zoekTxt)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(locatieToevoegenButton)
                    .addComponent(terugButton)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void locatieTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_locatieTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_locatieTableMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void terugButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terugButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        Main menu = new Main();
        menu.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_terugButtonActionPerformed

    private void locatieToevoegenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locatieToevoegenButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        Locatie_beheren Locatie_beheren = new Locatie_beheren();
        Locatie_beheren.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_locatieToevoegenButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Locatie_main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Locatie_main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Locatie_main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Locatie_main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Locatie_main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable locatieTable;
    private javax.swing.JButton locatieToevoegenButton;
    private javax.swing.JButton terugButton;
    private javax.swing.JTextField zoekTxt;
    // End of variables declaration//GEN-END:variables
}
