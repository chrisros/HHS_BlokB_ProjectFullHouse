/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lidselecter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lorenzo
 */
public class Toernooi_Betalen extends javax.swing.JFrame {

    private final DefaultTableModel table = new DefaultTableModel();
    DefaultListModel jListModel = new DefaultListModel();
    int toernooideelnemernummer = 0;
    boolean heeftbetaald = false;
    String toernooicode = "";

    public Toernooi_Betalen() {
        initComponents();
        setLocationRelativeTo(null);
        IngeschrevenDeelnemer.setModel(jListModel);
        ToernooiOphalen.setModel(table);
        String[] Kolomnaam = {"Toernooi id", "Datum", "Plaats code", "Max spelers"};
        table.setColumnIdentifiers(Kolomnaam);
        table.setRowCount(0);
        table.setColumnCount(4);
        toernooiVullen();
        jLabel1.setText("");
    }

    private void toernooiVullen() {
        // TODO add your handling code here:
        Sql_connect.doConnect();
        // declareer de variable voor in de rs
        String id;
        String naam;
        String plaats;
        String Max_inschrijvingen_T;


        try {

            Sql_connect.doConnect();

            String prepSqlStatement = "select * from toernooi;";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();


            int i = 0;

            while (result.next()) {
                i++;

            }
            table.setRowCount(i);
            result.beforeFirst();

            int d = 0;
            while (result.next()) {

                id = result.getString("Id_toernooi");
                naam = result.getString("Datum");
                plaats = result.getString("Id_locatie");
                Max_inschrijvingen_T = result.getString("Max_inschrijvingen_T");



                table.setValueAt(id, d, 0);
                table.setValueAt(naam, d, 1);
                table.setValueAt(plaats, d, 2);
                table.setValueAt(Max_inschrijvingen_T, d, 3);


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
            jListModel.removeAllElements();
            int row = ToernooiOphalen.getSelectedRow();

            String Table_click = ToernooiOphalen.getModel().getValueAt(row, 0).toString();
            Sql_connect.doConnect();

            // statement maken
            String prepSqlStatement = "select p.Id_persoon,p.Voornaam,p.Achternaam,t.Isbetaald from toernooideelnemer t join persoon p on p.Id_persoon = t.Id_persoon where t.Id_toernooi = '" + Table_click + "' ;";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();
            toernooicode = Table_click;
            while (result.next()) {

                ModelItem item = new ModelItem();
                item.id = result.getInt("Id_persoon");
                item.voornaam = result.getString("voornaam");
                item.achternaam = result.getString("achternaam");

                jListModel.addElement(item);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void gegevensLijst() {
        jLabel1.setText("");
        try {
            if (IngeschrevenDeelnemer.getSelectedValue() == null) {
            } else {
                ModelItem selectedItem = (ModelItem) IngeschrevenDeelnemer.getSelectedValue();
                String Table_click = Integer.toString(selectedItem.id);

                String prepSqlStatement = "select p.Id_persoon,p.Voornaam,p.Achternaam,t.Isbetaald from toernooideelnemer t join persoon p on p.Id_persoon = t.Id_persoon where t.Id_persoon = '" + Table_click + "' LIMIT 1  ;";
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
                ResultSet result = stat.executeQuery();

                if (result.next()) {

                    jLabel1.setText(result.getString("voornaam") + " " + result.getString("achternaam"));
                    int betaald = result.getInt("Isbetaald");
                    if (betaald == 1) {
                        Betalen.setSelected(true);
                        heeftbetaald = true;
                    } else {
                        Betalen.setSelected(false);
                        heeftbetaald = false;
                    }
                    toernooideelnemernummer = result.getInt("Id_persoon");
                }

            }
        } catch (Exception e) {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        IngeschrevenDeelnemer = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        Betalen = new javax.swing.JCheckBox();
        Updaten = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        ToernooiOphalen = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        IngeschrevenDeelnemer.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        IngeschrevenDeelnemer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IngeschrevenDeelnemerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(IngeschrevenDeelnemer);

        jLabel1.setText("jLabel1");

        Betalen.setText("Betaald");

        Updaten.setText("Update");
        Updaten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdatenActionPerformed(evt);
            }
        });

        ToernooiOphalen.setModel(new javax.swing.table.DefaultTableModel(
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
        ToernooiOphalen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ToernooiOphalenMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(ToernooiOphalen);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Betalen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Updaten)
                .addGap(39, 39, 39))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Betalen)
                    .addComponent(Updaten))
                .addGap(79, 79, 79))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ToernooiOphalenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ToernooiOphalenMouseClicked
        gegevensOphalen();
    }//GEN-LAST:event_ToernooiOphalenMouseClicked

    private void IngeschrevenDeelnemerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IngeschrevenDeelnemerMouseClicked
        gegevensLijst();
    }//GEN-LAST:event_IngeschrevenDeelnemerMouseClicked

    private void UpdatenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdatenActionPerformed
        //if (toernooideelnemernummer != 0) {
        System.out.println(toernooideelnemernummer +" || "+ toernooicode);
            try {
                String prepSqlStatement;
                Sql_connect.doConnect();
                if (Betalen.isSelected()) {
                    prepSqlStatement = "UPDATE toernooideelnemer SET Isbetaald='1' where Id_toernooi = '"+toernooicode+"' and Id_persoon = '"+toernooideelnemernummer+"' ;";
                } else {
                    prepSqlStatement = "UPDATE toernooideelnemer SET Isbetaald='0' where Id_toernooi = '"+toernooicode+"' and Id_persoon = '"+toernooideelnemernummer+"' ;";
                }
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
                stat.execute();//   executeUpdate();
                
                Betalen.setSelected(false);
                heeftbetaald = false;
                toernooideelnemernummer = 0;
                jLabel1.setText("");
            } catch (Exception e) {
                System.out.print(e);
            }
        //}

    }//GEN-LAST:event_UpdatenActionPerformed

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
            java.util.logging.Logger.getLogger(Toernooi_Betalen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Toernooi_Betalen().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Betalen;
    private javax.swing.JList IngeschrevenDeelnemer;
    private javax.swing.JTable ToernooiOphalen;
    private javax.swing.JButton Updaten;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
