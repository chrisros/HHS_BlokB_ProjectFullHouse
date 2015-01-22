/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lidselecter;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Jim
 */
public class Masterclass_beheren extends javax.swing.JFrame {

    private boolean fieldsOk;
    private int new_masterclassID = 0;

    DefaultListModel masterclass = new DefaultListModel();
    DefaultListModel locatie = new DefaultListModel();
    private boolean spelersOk;
    
    /**
     * Creates new form Masterclass_beheren
     */
    public Masterclass_beheren() {
        initComponents();
        nieuwMasterclassId();
        setLocationRelativeTo(null);
        //locatieLabel.setText("<html>Loctie code:<br>(0 voor onbekende locatie)</html>");
        masterclassList.setModel(masterclass);
        locatieList.setModel(locatie);
        vulLijst();
        vulLijst2();
    }
    
    // hier krijg je het eerst volgende nummer voor het id
    private int nieuwMasterclassId() {
        // standaard waarde nieuw id

        try {
            // maak connectie
            Sql_connect.doConnect();
            // statement
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement("select MAX(Id_masterclass) AS biggest from masterclass");
            ResultSet result = stat.executeQuery();
            while (result.next()) {
                new_masterclassID = result.getInt("biggest");
            }
            new_masterclassID = new_masterclassID + 1;
            //return new_masterclassID;
            idMasterclassTxt.setText(Integer.toString(new_masterclassID));

        } catch (Exception ex) {
            Logger.getLogger(Ledeneditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new_masterclassID;
    }
    
    // hier voeg je een nieuwe masterclass toe
    private void nieuwMasterclass() {
        try {
            //maak een connectie
            Sql_connect.doConnect();

            idMasterclassTxt.setText(Integer.toString(new_masterclassID));

            // krijg de tekst uit de velden
            String Id_masterclass = idMasterclassTxt.getText(); /* is tekstdie verkregen is uit nieuwToernooiId() methode */
            int rating = Integer.parseInt(minRatingTxt.getText());
            String Inschrijfkosten = inschrijfKostenTxt.getText();
            String Max_inschrijvingen_M = maxInschrijfTxt.getText();
            int Id_locatie = getLocatie(codeLocatieTxt.getText());
            String Datum = datumTxt.getText();
            String naam = naamMasterclassTxt.getText();
           

            // sql prepair statement
            String prepSqlStatement
                    = "INSERT INTO toernooi (Id_masterclass, Minimale_rating, Inschrijf_prijs, "
                    + "Max_inschrijvingen_M, Id_locatie, "
                    + "Datum, Naam_masterclass)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, Id_masterclass);
            stat.setInt(2, rating);
            stat.setString(3, Inschrijfkosten);
            stat.setString(4, Max_inschrijvingen_M);
            stat.setInt(5, Id_locatie);
            stat.setString(6, Datum);
            stat.setString(7, naam);
            stat.executeUpdate();
            // melding
            JOptionPane.showMessageDialog(rootPane, "Toevoegen nieuwe masterclass gelukt");


        } catch (Exception e) {
            ePopup(e);
        }
    }
    
    // hier weizig je de toernooien
    private void wijzigenMasterclass() {
        try {
            // verkrijg de waarders uit de velden
            int Id_masterclass = Integer.parseInt(idMasterclassTxt.getText());  
            int rating = Integer.parseInt(minRatingTxt.getText());
            String Inschrijfkosten = inschrijfKostenTxt.getText();
            String Max_inschrijvingen_M = maxInschrijfTxt.getText();
            int Id_locatie = getLocatie(codeLocatieTxt.getText());
            String Datum = datumTxt.getText();
            String naam = naamMasterclassTxt.getText();
         
            Sql_connect.doConnect();
            String prepSqlStatement = "UPDATE masterclass SET "
                    + "Minimale_rating = ?, "
                    + "Inschrijf_prijs = ?, "
                    + "Max_inschrijvingen_M = ?,"
                    + "Id_locatie = ?,"
                    + "Datum = ?,"
                    + "Naam_masterclass = ? "
                    + "WHERE Id_toernooi = ?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setInt(1, Id_masterclass);
            stat.setInt(2, rating);
            stat.setString(3, Inschrijfkosten);
            stat.setString(4, Max_inschrijvingen_M);
            stat.setInt(5, Id_locatie);
            stat.setString(6, Datum);
            stat.setString(7, naam);
            stat.executeUpdate();
            vulLijst();
            MELDINGFIELD.setText("Masterclass gewijzigd");

        } catch (Exception e) {
            ePopup(e);
            MELDINGFIELD.setText("Wijziging mislukt!");
        }

    }
    
     // hier verwijder je een toernooi, geselecteerd op id
    private void verwijderenMasterclass() {
        try {

            int id_masterclass = Integer.parseInt(idMasterclassTxt.getText());

            Sql_connect.doConnect();
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement("DELETE FROM masterclass WHERE Id_masterclass = ?");
            stat.setInt(1, id_masterclass);
            stat.executeUpdate();
            JOptionPane.showMessageDialog(this, "Masterclass met id: " + id_masterclass + " succesvol verwijderd.");
            vulLijst();

        } catch (Exception e) {
            ePopup(e);
            MELDINGFIELD.setText("Verwijderen mislukt");
        }
    }
    
    //leegt de velden
    private void leegVelden()
    {
        idMasterclassTxt.setText("");
        minRatingTxt.setText("");
        inschrijfKostenTxt.setText("");
        maxInschrijfTxt.setText("");
        codeLocatieTxt.setText("");
        datumTxt.setText("");
        naamMasterclassTxt.setText("");
        nieuwMasterclassId();
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
        checkStringField(idMasterclassTxt, 1, 100);
        checkStringField(minRatingTxt, 1, 100);
        checkStringField(inschrijfKostenTxt, 4, 40);
        checkStringField(maxInschrijfTxt, 2, 100);
        checkStringField(datumTxt, 10, 10);
         checkStringField(naamMasterclassTxt, 2, 250);
        
       
        return fieldsOk;
    }
    
     private void gegevensLijst() {
        try {
            if (masterclassList.getSelectedValue() == null) {
                MELDINGFIELD.setText("Niets Geselecteerd.");
            } else {
                ModelItem selectedItem = (ModelItem) masterclassList.getSelectedValue();
                idMasterclassTxt.setText(Integer.toString(selectedItem.id));
                minRatingTxt.setText(Integer.toString(selectedItem.rating));
                inschrijfKostenTxt.setText(selectedItem.inschrijfKosten);
                maxInschrijfTxt.setText(selectedItem.maxInschrijf);
                datumTxt.setText(selectedItem.datum);
                codeLocatieTxt.setText(getLocatieNaam(selectedItem.locatieCode));
                naamMasterclassTxt.setText(selectedItem.naam);
                

                MELDINGFIELD.setText("Opvraag ID gelukt!");
            }
        } catch (Exception e) {
            MELDINGFIELD.setText("Geen naam geselecteerd!");
        }
    }
     //haalt de locatie op van de huidig geselecteerde masterclass
    private String getLocatieNaam(int locatieID)
    {
        String value = "";
        try {
            Sql_connect.doConnect();
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT Naam_locatie FROM locatie WHERE Id_locatie = ? LIMIT 1");
            stat.setInt(1, locatieID);
            ResultSet result = stat.executeQuery();
            while (result.next()) {
            value = result.getString("Naam_locatie");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_beheren.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return value;
    }
    private int getLocatie(String locatieNaam)
    {
        int value = 0;
        try {
            Sql_connect.doConnect();
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT Id_locatie FROM locatie WHERE Naam_locatie = ?");
            stat.setString(1, locatieNaam);
            ResultSet result = stat.executeQuery();
            while (result.next()) {
            value = result.getInt("Id_locatie");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Toernooi_beheren.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
    
    private void setLocatie()
    {
        ModelItem selectedItem = (ModelItem) locatieList.getSelectedValue();
        codeLocatieTxt.setText(selectedItem.naam);
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
      private void vulLijst() {
        try {
            
            //Sql_connect.doConnect();
            String zoekVeld = removeLastChar(zoekTxt.getText());
            ResultSet result;
            Sql_connect.doConnect();
            if (zoekVeld.equals(""))
            {
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT * FROM masterclass");
                result = stat.executeQuery();
            } else{
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT * FROM masterclass WHERE Naam_masterclass LIKE ?");
                stat.setString(1, "%"+zoekVeld+"%");  
                result = stat.executeQuery();
            }
            
            masterclass.removeAllElements();     
            while (result.next()) {
                ModelItem item = new ModelItem();

                item.id = result.getInt("Id_masterclass");
                item.rating = result.getInt("Minimale_rating");
                item.inschrijfKosten = result.getString("Inschrijf_prijs");
                item.maxInschrijf = result.getString("Max_inschrijvingen_M");
                item.locatieCode = result.getInt("Id_locatie");
                item.datum = result.getString("Datum");
                item.naam = result.getString("Naam_masterclass");
               
                masterclass.addElement(item);

                MELDINGFIELD.setText("Opvragen lijst gelukt!");
            }

        } catch (Exception e) {
            ePopup(e);
        }
    }
       // Hier vul je de lijst met de locaties gevuld
    private void vulLijst2() {
        try {
            
            //Sql_connect.doConnect();
            String zoekVeld = removeLastChar(zoekTxt2.getText());
            ResultSet result;
            Sql_connect.doConnect();
            if (zoekVeld.equals(""))
            {
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT Id_locatie, Naam_locatie FROM locatie");
                result = stat.executeQuery();
            } else{
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT Id_locatie, Naam_locatie FROM locatie WHERE Naam_locatie LIKE ?");
                stat.setString(1, "%"+zoekVeld+"%");  
                result = stat.executeQuery();
            }
            
            locatie.removeAllElements();     
            while (result.next()) {
                ModelItem item = new ModelItem();

                item.id = result.getInt("Id_locatie");
                item.naam = result.getString("Naam_locatie");
                locatie.addElement(item);
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

        jPanel1 = new javax.swing.JPanel();
        idMasterclassTxt = new javax.swing.JTextField();
        minRatingTxt = new javax.swing.JTextField();
        inschrijfKostenTxt = new javax.swing.JTextField();
        maxInschrijfTxt = new javax.swing.JTextField();
        codeLocatieTxt = new javax.swing.JTextField();
        naamMasterclassTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Voegtoe_Button = new javax.swing.JButton();
        Verwijder_Button = new javax.swing.JButton();
        Leegvelden_Button = new javax.swing.JButton();
        Wijzigen_Button = new javax.swing.JButton();
        zoekTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        Terug_Button = new javax.swing.JButton();
        datumTxt = new javax.swing.JFormattedTextField();
        jSeparator2 = new javax.swing.JSeparator();
        MELDINGFIELD = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        masterclassList = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        locatieList = new javax.swing.JList();
        zoekTxt2 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(850, 456));

        jLabel1.setText("Id Masterclass");

        jLabel2.setText("Minimale rating");

        jLabel3.setText("Inschrijf kosten (00.00)");

        jLabel4.setText("Maximaal aantal inschrijvingen");

        jLabel5.setText("Locatie");

        jLabel6.setText("Datum (yyyy-mm-dd)");

        jLabel7.setText("Naam Masterclass");

        Voegtoe_Button.setText("Voeg masterclass toe");
        Voegtoe_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Voegtoe_ButtonActionPerformed(evt);
            }
        });

        Verwijder_Button.setText("Verwijder masterclass");
        Verwijder_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Verwijder_ButtonActionPerformed(evt);
            }
        });

        Leegvelden_Button.setText("Leeg velden");
        Leegvelden_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Leegvelden_ButtonActionPerformed(evt);
            }
        });

        Wijzigen_Button.setText("Wijzigen masterclass");
        Wijzigen_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Wijzigen_ButtonActionPerformed(evt);
            }
        });

        zoekTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zoekTxtKeyReleased(evt);
            }
        });

        jLabel8.setText("Zoek  Masterclass");

        Terug_Button.setText("Terug");
        Terug_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Terug_ButtonActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        masterclassList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        masterclassList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                masterclassListMouseClicked(evt);
            }
        });
        masterclassList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                masterclassListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(masterclassList);

        locatieList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        locatieList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                locatieListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(locatieList);

        zoekTxt2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zoekTxt2KeyReleased(evt);
            }
        });

        jLabel9.setText("Zoek Locatie");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7))
                            .addGap(31, 31, 31)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(idMasterclassTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addComponent(minRatingTxt, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(inschrijfKostenTxt, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(maxInschrijfTxt, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(codeLocatieTxt)
                                .addComponent(naamMasterclassTxt)
                                .addComponent(datumTxt))
                            .addGap(16, 16, 16))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(Leegvelden_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Verwijder_Button, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
                            .addGap(165, 165, 165))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(Voegtoe_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Wijzigen_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(zoekTxt2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                        .addComponent(Terug_Button)))
                .addGap(10, 34, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idMasterclassTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(zoekTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(minRatingTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inschrijfKostenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(maxInschrijfTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(codeLocatieTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(datumTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(naamMasterclassTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addComponent(Voegtoe_Button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Wijzigen_Button)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(Verwijder_Button)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(Terug_Button)
                                .addGap(11, 11, 11)))
                        .addComponent(Leegvelden_Button)
                        .addGap(28, 28, 28))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addComponent(jSeparator2)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Terug_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Terug_ButtonActionPerformed
        this.dispose();
        Main Main = new Main();
        Main.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_Terug_ButtonActionPerformed

    private void masterclassListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_masterclassListValueChanged
        gegevensLijst();
    }//GEN-LAST:event_masterclassListValueChanged

    private void masterclassListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_masterclassListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_masterclassListMouseClicked

    private void Voegtoe_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Voegtoe_ButtonActionPerformed
        if (checkFields()) {
            nieuwMasterclass();
            this.dispose();
            Masterclass_beheren Masterclass_beheren = new Masterclass_beheren();
            Masterclass_beheren.setVisible(rootPaneCheckingEnabled);
        }
    }//GEN-LAST:event_Voegtoe_ButtonActionPerformed

    private void Verwijder_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Verwijder_ButtonActionPerformed
        verwijderenMasterclass();
    }//GEN-LAST:event_Verwijder_ButtonActionPerformed

    private void Wijzigen_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Wijzigen_ButtonActionPerformed
        if (checkFields()) {
            wijzigenMasterclass();
        }
    }//GEN-LAST:event_Wijzigen_ButtonActionPerformed

    private void Leegvelden_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Leegvelden_ButtonActionPerformed
        nieuwMasterclassId();
        vulLijst();
        leegVelden();
    }//GEN-LAST:event_Leegvelden_ButtonActionPerformed

    private void zoekTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zoekTxtKeyReleased
        vulLijst();
    }//GEN-LAST:event_zoekTxtKeyReleased

    private void locatieListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_locatieListValueChanged
        setLocatie();
    }//GEN-LAST:event_locatieListValueChanged

    private void zoekTxt2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zoekTxt2KeyReleased
        vulLijst2();
    }//GEN-LAST:event_zoekTxt2KeyReleased

    /**
     * @param args the command line arguments
     */
    
    private void ePopup(Exception e) {
        final String eMessage = "Er is iets fout gegaan, neem contact op met de aplicatiebouwer, geef deze foutmelding door: ";
        String error = eMessage + e;
        JOptionPane.showMessageDialog(rootPane, error);
    }
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Masterclass_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Masterclass_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Masterclass_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Masterclass_beheren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Masterclass_beheren().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Leegvelden_Button;
    private javax.swing.JLabel MELDINGFIELD;
    private javax.swing.JButton Terug_Button;
    private javax.swing.JButton Verwijder_Button;
    private javax.swing.JButton Voegtoe_Button;
    private javax.swing.JButton Wijzigen_Button;
    private javax.swing.JTextField codeLocatieTxt;
    private javax.swing.JFormattedTextField datumTxt;
    private javax.swing.JTextField idMasterclassTxt;
    private javax.swing.JTextField inschrijfKostenTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JList locatieList;
    private javax.swing.JList masterclassList;
    private javax.swing.JTextField maxInschrijfTxt;
    private javax.swing.JTextField minRatingTxt;
    private javax.swing.JTextField naamMasterclassTxt;
    private javax.swing.JTextField zoekTxt;
    private javax.swing.JTextField zoekTxt2;
    // End of variables declaration//GEN-END:variables
}
