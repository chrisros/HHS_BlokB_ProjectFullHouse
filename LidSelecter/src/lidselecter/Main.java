/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lidselecter;

/**
 *
 * @author chris
 */
public class Main extends javax.swing.JFrame {

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        beherenSpelerButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        agendaToernooiButton = new javax.swing.JButton();
        beherenToernooiButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        agendaMasterclassButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        locatieButton = new javax.swing.JButton();
        overzichtSpelerButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        beherenSpelerButton.setText("Speler beheer");
        beherenSpelerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beherenSpelerButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Spelers");

        jLabel2.setText("Toernooien");

        agendaToernooiButton.setText("Toernooien agenda");
        agendaToernooiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agendaToernooiButtonActionPerformed(evt);
            }
        });

        beherenToernooiButton.setText("Toernooi beheer");
        beherenToernooiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beherenToernooiButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Masterclass");

        agendaMasterclassButton.setText("Masterclass agenda");
        agendaMasterclassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agendaMasterclassButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Locatie");

        locatieButton.setText("Locatie´s");
        locatieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locatieButtonActionPerformed(evt);
            }
        });

        overzichtSpelerButton.setText("Speler overzicht");
        overzichtSpelerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overzichtSpelerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(beherenSpelerButton)
                    .addComponent(jLabel4)
                    .addComponent(locatieButton)
                    .addComponent(overzichtSpelerButton))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(agendaMasterclassButton)
                    .addComponent(jLabel3)
                    .addComponent(agendaToernooiButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(beherenToernooiButton)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {agendaMasterclassButton, agendaToernooiButton, beherenToernooiButton});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {beherenSpelerButton, locatieButton, overzichtSpelerButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(beherenSpelerButton)
                    .addComponent(agendaToernooiButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(beherenToernooiButton)
                    .addComponent(overzichtSpelerButton))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(agendaMasterclassButton)
                    .addComponent(locatieButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void beherenSpelerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beherenSpelerButtonActionPerformed
        this.dispose();
        Ledeneditor ledenedit = new Ledeneditor();
        ledenedit.setVisible(rootPaneCheckingEnabled);
        ledenedit.setLocationRelativeTo(null);
    }//GEN-LAST:event_beherenSpelerButtonActionPerformed

    private void agendaToernooiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agendaToernooiButtonActionPerformed
        this.dispose();
        Toernooien_main toernooi = new Toernooien_main();
        toernooi.setVisible(rootPaneCheckingEnabled);
        toernooi.setLocationRelativeTo(null);
    }//GEN-LAST:event_agendaToernooiButtonActionPerformed

    private void beherenToernooiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beherenToernooiButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        Toernooi_beheren toernooi = new Toernooi_beheren();
        toernooi.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_beherenToernooiButtonActionPerformed

    private void agendaMasterclassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agendaMasterclassButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        Masterclass_main Masterclass_main = new Masterclass_main();
        Masterclass_main.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_agendaMasterclassButtonActionPerformed

    private void locatieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locatieButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        Locatie_main Locatie_main = new Locatie_main();
        Locatie_main.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_locatieButtonActionPerformed

    private void overzichtSpelerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overzichtSpelerButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        LedenOverzicht LedenOverzicht = new LedenOverzicht();
        LedenOverzicht.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_overzichtSpelerButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agendaMasterclassButton;
    private javax.swing.JButton agendaToernooiButton;
    private javax.swing.JButton beherenSpelerButton;
    private javax.swing.JButton beherenToernooiButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton locatieButton;
    private javax.swing.JButton overzichtSpelerButton;
    // End of variables declaration//GEN-END:variables
}