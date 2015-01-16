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

    DefaultListModel toernooi = new DefaultListModel();
    DefaultListModel locatie = new DefaultListModel();
    private boolean spelersOk;
    /**
     * Creates new form Toevoegen_toernooi
     */
    public Toernooi_beheren() {
        initComponents();
        nieuwToernooiId();
        setLocationRelativeTo(null);
        //locatieLabel.setText("<html>Loctie code:<br>(0 voor onbekende locatie)</html>");
        toernooiList.setModel(toernooi);
        locatieList.setModel(locatie);
        vulLijst();
        vulLijst2();       
       
    }
    
      //check max haalbare spelers
    private boolean maxSpelers()
    {
        
        Sql_connect.doConnect();
        if ((codeLocatieTxt.getText()).equals(""))
        {
                        
            MELDINGFIELD.setForeground(Color.black);
            maxInschrijfTxt.setBackground(Color.white);
            spelersOk = true;
        } else{
            try {
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement("SELECT Max_tafels FROM locatie WHERE Naam_locatie LIKE ? LIMIT 1");
                stat.setString(1, codeLocatieTxt.getText());
                ResultSet result = stat.executeQuery();
                while (result.next()) {
                    int tafels = result.getInt("Max_tafels");
                    int spelersPerTafel = Integer.parseInt(maxSpelersTafelTxt.getText());
                    int capaciteit = spelersPerTafel*tafels;
                    int maxSpelers = Integer.parseInt(maxInschrijfTxt.getText());
                    if(capaciteit<maxSpelers)
                    {
                        spelersOk = false;
                        MELDINGFIELD.setText("Maximum spelers voor deze locatie is: "+capaciteit);
                        MELDINGFIELD.setForeground(Color.red);
                        maxInschrijfTxt.setBackground(Color.red);
                    } else
                    {
                        MELDINGFIELD.setForeground(Color.black);
                        maxInschrijfTxt.setBackground(Color.white);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Toernooi_beheren.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
       
        return spelersOk;
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
    // Hier vul je de lijst met de toernooi namen gevuld
    private void vulLijst() {
        try {
            
            //Sql_connect.doConnect();
            String zoekVeld = removeLastChar(zoekTxt.getText());
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
            
            toernooi.removeAllElements();     
            while (result.next()) {
                ModelItem item = new ModelItem();

                item.id = result.getInt("Id_toernooi");
                item.naam = result.getString("Naam");
                item.datum = result.getString("Datum");
                item.inschrijfKosten = result.getString("Inschrijfkosten");
                item.maxInschrijf = result.getString("Max_inschrijvingen_T");
                item.maxPTafel = result.getString("Max_speler_per_tafel");
                item.kaartCode = result.getString("Kaartspel_code");
                item.locatieCode = result.getInt("Id_locatie");
                item.kaartType = result.getString("Kaartspeltype");
                toernooi.addElement(item);

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
    
    //leegt de velden
    private void leegVelden()
    {
        naamToernooiTxt.setText("");
        datumTxt.setText("");
        inschrijfKostenTxt.setText("");
        maxInschrijfTxt.setText("");
        maxSpelersTafelTxt.setText("");
        //codeKaartspelTxt.setText("");
        codeLocatieTxt.setText("");
        kaartspelType.setSelectedIndex(0);
    }
    
    //haalt de locatie op van de huidig geselecteerd toernooi
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
    
    // hier krijg je het eerst volgende nummer voor het id
    private int nieuwToernooiId() {
        // standaart waarde nieuw id

        try {
            // maak connectie
            Sql_connect.doConnect();
            // statement
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement("select MAX(Id_toernooi) AS biggest from toernooi");
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
            int Kaartspel_code = kaartspelType.getSelectedIndex();
            int Id_locatie = getLocatie(codeLocatieTxt.getText());
            String Kaartspeltype = (String)kaartspelType.getSelectedItem();

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
            stat.setInt(7, Kaartspel_code);
            stat.setInt(8, Id_locatie);
            stat.setString(9, Kaartspeltype);
            stat.executeUpdate();
            // melding
            JOptionPane.showMessageDialog(rootPane, "Toevoegen nieuw toernooi gelukt");


        } catch (Exception e) {
            ePopup(e);
        }
    }
    
    private void setLocatie()
    {
        ModelItem selectedItem = (ModelItem) locatieList.getSelectedValue();
        codeLocatieTxt.setText(selectedItem.naam);
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
            int Kaartspel_code = kaartspelType.getSelectedIndex();
            int Id_locatie = getLocatie(codeLocatieTxt.getText());
            String Kaartspeltype = (String)kaartspelType.getSelectedItem();

            Sql_connect.doConnect();
            String prepSqlStatement = "UPDATE toernooi SET "
                    + "Naam = ?, "
                    + "Datum = ?, "
                    + "Inschrijfkosten = ?,"
                    + "Max_inschrijvingen_T = ?,"
                    + "Max_speler_per_tafel = ?,"
                    + "Kaartspel_code = ?,"
                    + "Id_locatie = ?,"
                    + "Kaartspeltype = ? "
                    + "WHERE Id_toernooi = ?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, naam);
            stat.setString(2, Datum);
            stat.setString(3, Inschrijfkosten);
            stat.setString(4, Max_inschrijvingen_T);
            stat.setString(5, Max_speler_per_tafel);
            stat.setInt(6, Kaartspel_code);
            stat.setInt(7, Id_locatie);
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
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement("DELETE FROM toernooi WHERE Id_toernooi = ?");
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
                codeLocatieTxt.setText(getLocatieNaam(selectedItem.locatieCode));
                kaartspelType.setSelectedItem(selectedItem.kaartType);

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
        locatieLabel = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        codeLocatieTxt = new javax.swing.JTextField();
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
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        locatieList = new javax.swing.JList();
        zoekTxt2 = new javax.swing.JTextField();
        kaartspelType = new javax.swing.JComboBox();

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
        idToernooiTxt.setBackground(new java.awt.Color(200, 200, 200));
        idToernooiTxt.setFocusCycleRoot(true);
        idToernooiTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                idToernooiTxtFocusLost(evt);
            }
        });
        idToernooiTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idToernooiTxtActionPerformed(evt);
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

        locatieLabel.setText("Locatie:");

        jLabel19.setText("Kaartspel type");

        codeLocatieTxt.setEditable(false);
        codeLocatieTxt.setBackground(new java.awt.Color(200, 200, 200));
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

        jLabel4.setText("Zoek toernooi:");

        backToMain.setText("Terug");
        backToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToMainActionPerformed(evt);
            }
        });

        jButton1.setText("Leeg velden");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Zoek locatie:");

        locatieList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                locatieListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(locatieList);

        zoekTxt2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zoekTxt2KeyReleased(evt);
            }
        });

        kaartspelType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Poker", "Klaverjassen", "Blackjack", "Patiance" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel12)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel14)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(idToernooiTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(maxInschrijfTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(maxSpelersTafelTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(inschrijfKostenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(naamToernooiTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                            .addComponent(datumTxt)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(wijzigenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(voegtoeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(1, 1, 1))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel19)
                                                    .addComponent(locatieLabel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(verwijderenButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(kaartspelType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(codeLocatieTxt))))))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                            .addComponent(MELDINGFIELD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(zoekTxt2))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(100, 100, 100)
                                .addComponent(backToMain))))
                    .addComponent(feedback2, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {codeLocatieTxt, datumTxt, idToernooiTxt, inschrijfKostenTxt, kaartspelType, maxInschrijfTxt, maxSpelersTafelTxt, naamToernooiTxt});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(idToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(naamToernooiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(datumTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inschrijfKostenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(maxInschrijfTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(maxSpelersTafelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(codeLocatieTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(locatieLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(kaartspelType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(verwijderenButton)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(voegtoeButton)
                                .addGap(4, 4, 4)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(wijzigenButton)
                                    .addComponent(jButton1)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zoekTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(zoekTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                            .addComponent(jScrollPane2))))
                .addGap(18, 18, 18)
                .addComponent(feedback2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(backToMain))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(MELDINGFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {codeLocatieTxt, datumTxt, idToernooiTxt, inschrijfKostenTxt, kaartspelType, maxInschrijfTxt, maxSpelersTafelTxt, naamToernooiTxt});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void terugButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terugButtonActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_terugButtonActionPerformed

    private void zoekTxt2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zoekTxt2KeyReleased
        vulLijst2();
    }//GEN-LAST:event_zoekTxt2KeyReleased

    private void locatieListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_locatieListValueChanged
        setLocatie();
    }//GEN-LAST:event_locatieListValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        nieuwToernooiId();
        vulLijst();
        leegVelden();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void backToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToMainActionPerformed
        this.dispose();
        Main Main = new Main();
        Main.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_backToMainActionPerformed

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

    private void verwijderenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verwijderenButtonActionPerformed
        // TODO add your handling code here:
        verwijderenToernooi();
    }//GEN-LAST:event_verwijderenButtonActionPerformed

    private void toernooiListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_toernooiListValueChanged
        gegevensLijst();
    }//GEN-LAST:event_toernooiListValueChanged

    private void toernooiListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_toernooiListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_toernooiListMouseClicked

    private void wijzigenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wijzigenButtonActionPerformed
        // TODO add your handling code here:
        if (checkFields()&&maxSpelers()) {
            wijzigenToernooi();
        }
    }//GEN-LAST:event_wijzigenButtonActionPerformed

    private void datumTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datumTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_datumTxtActionPerformed

    private void codeLocatieTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codeLocatieTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codeLocatieTxtActionPerformed

    private void maxInschrijfTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxInschrijfTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maxInschrijfTxtActionPerformed

    private void inschrijfKostenTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inschrijfKostenTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inschrijfKostenTxtActionPerformed

    private void idToernooiTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idToernooiTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idToernooiTxtActionPerformed

    private void idToernooiTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idToernooiTxtFocusLost

    }//GEN-LAST:event_idToernooiTxtFocusLost

    private void naamToernooiTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_naamToernooiTxtFocusLost

    }//GEN-LAST:event_naamToernooiTxtFocusLost

    private void voegtoeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voegtoeButtonActionPerformed
        if (checkFields()&&maxSpelers()) {
            nieuwToernooi();
            this.dispose();
            Toernooi_main Toernooien_main = new Toernooi_main();
            Toernooien_main.setVisible(rootPaneCheckingEnabled);
        }
    }//GEN-LAST:event_voegtoeButtonActionPerformed

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
    private javax.swing.JTextField codeLocatieTxt;
    private javax.swing.JFormattedTextField datumTxt;
    private javax.swing.JLabel feedback2;
    private javax.swing.JTextField idToernooiTxt;
    private javax.swing.JTextField inschrijfKostenTxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox kaartspelType;
    private javax.swing.JLabel locatieLabel;
    private javax.swing.JList locatieList;
    private javax.swing.JTextField maxInschrijfTxt;
    private javax.swing.JTextField maxSpelersTafelTxt;
    private javax.swing.JTextField naamToernooiTxt;
    private javax.swing.JButton terugButton;
    private javax.swing.JList toernooiList;
    private javax.swing.JButton verwijderenButton;
    private javax.swing.JButton voegtoeButton;
    private javax.swing.JButton wijzigenButton;
    private javax.swing.JTextField zoekTxt;
    private javax.swing.JTextField zoekTxt2;
    // End of variables declaration//GEN-END:variables

}
