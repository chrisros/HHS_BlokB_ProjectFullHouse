/*
 *Chris Ros Services
 */
package lidselecter;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * @author chris
 */
public class Ledeneditor extends javax.swing.JFrame {

    //general variables
    private boolean fieldsOk;
    /**
     * Creates new form Ledeneditor
     */
    DefaultListModel jListModel = new DefaultListModel();

    public Ledeneditor() {
        //String rnaam = roepnaam.getText();
        //String anaam = achternaam.getText();
        initComponents();
        jList.setModel(jListModel);
    }

    private void getPerson() {
        try {
            int l_code;
            try {
                l_code = Integer.parseInt(id.getText());
            } catch (Exception e) {
                l_code = 0;
            }

            boolean hasRun = false;
            if (l_code > 0) {
                Sql_connect.doConnect();
                String prepSqlStatement = "SELECT roepnaam, achternaam FROM lid WHERE l_code = ? LIMIT 1";
                PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
                stat.setInt(1, l_code);
                ResultSet result = stat.executeQuery();

                while (result.next()) {
                    roepnaam.setText(result.getString("roepnaam"));
                    achternaam.setText(result.getString("achternaam"));
                    hasRun = true;
                    feedback.setText("Opvraag lid gelukt!");
                }
                if (!hasRun) {
                    feedback.setText("code bestaat niet");
                }
            } else {
                feedback.setText("Geen code ingevoerd");
            }
        } catch (Exception e) {
            ePopup(e);
            Logger.getLogger(Ledeneditor.class.getName()).log(Level.SEVERE, null, e);
            feedback.setText("Opvraag lid mislukt!");
        }
    }

    private void setPerson() {
        try {
            //collect variables from textfields
            int code = Integer.parseInt(id.getText());
            String rnaam = roepnaam.getText();
            String anaam = achternaam.getText();

            //parse fields to prepstat
            Sql_connect.doConnect();
            String prepSqlStatement = "UPDATE lid SET roepnaam=?, achternaam=? WHERE l_code = ?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setString(1, rnaam);
            stat.setString(2, anaam);
            stat.setInt(3, code);
            stat.executeUpdate();
            feedback.setText("Wijziging lid gelukt!");
            getLijst();
        } catch (Exception e) {
            ePopup(e);
            Logger.getLogger(Ledeneditor.class.getName()).log(Level.SEVERE, null, e);
            feedback.setText("Wijziging lid mislukt!");
            getLijst();
        }
    }

    private void getLijst() {
        try {
            Sql_connect.doConnect();
            String prepSqlStatement = "SELECT roepnaam, l_code FROM lid ";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();

            jListModel.removeAllElements();
            while (result.next()) {
                ModelItem item = new ModelItem();
                item.id = result.getInt("l_code");
                item.description = result.getString("roepnaam");
                jListModel.addElement(item);
                feedback.setText("Opvraag lijst gelukt!");
            }

        } catch (Exception e) {
            ePopup(e);
        }
    }
    /*
    * vraagt de hoogste ID op uit de DB en  maakt een nieuwe die 1 hoger is
    */
    private int getNewCode() {
        int newl_code = 0;
        try {
            Sql_connect.doConnect();
            String prepSqlStatement = "select MAX(l_code) AS biggest from lid";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            ResultSet result = stat.executeQuery();
            while (result.next()) {
                newl_code = result.getInt("biggest");
            }
            newl_code = newl_code+1;
            return newl_code;
            
        } catch (Exception ex) {
            Logger.getLogger(Ledeneditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newl_code;
    }

    private void addPerson() {
        try {
            int code = getNewCode();
            String rnaam = roepnaam.getText();
            String anaam = achternaam.getText();

            //parse fields to prepstat
            Sql_connect.doConnect();
            String prepSqlStatement = "INSERT INTO lid (l_code, roepnaam, achternaam) VALUES (?, ?, ?)";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setInt(1, code);
            stat.setString(2, rnaam);
            stat.setString(3, anaam);
            stat.executeUpdate();
            feedback.setText("Toevoegen lid gelukt!");
            getLijst();

        } catch (Exception e) {
            ePopup(e);
        }

    }

    private void getSelectedId() {
        try {
            if (jList.getSelectedValue() == null) {
                feedback.setText("Niets Geselecteerd.");
            } else {
                ModelItem selectedItem = (ModelItem) jList.getSelectedValue();
                id.setText(Integer.toString(selectedItem.id));
                roepnaam.setText("");
                achternaam.setText("");
                feedback.setText("Opvraag ID gelukt!");
            }
        } catch (Exception e) {
            feedback.setText("Geen naam geselecteerd!");
        }

    }

    private void deletePerson() {
        try {
            int code = Integer.parseInt(id.getText());
            String rnaam = roepnaam.getText();
            String anaam = achternaam.getText();

            //parse fields to prepstat
            Sql_connect.doConnect();
            String prepSqlStatement = "DELETE FROM lid WHERE l_code = ? AND roepnaam = ? AND achternaam = ?";
            PreparedStatement stat = Sql_connect.getConnection().prepareStatement(prepSqlStatement);
            stat.setInt(1, code);
            stat.setString(2, rnaam);
            stat.setString(3, anaam);
            stat.executeUpdate();
            getLijst();
            feedback.setText(rnaam + " " + anaam + " Succesvol verwijderd.");
        } catch (Exception ex) {
            getLijst();
            Logger.getLogger(Ledeneditor.class.getName()).log(Level.SEVERE, null, ex);
            feedback.setText("Geen persoon gevonden");
        }
    }
    /*
     *Controleert of de input van een numeriek veld daadwerkelijk een nummer is.
     *Geeft eventuele feedback op de foutive invoer.
     *ook kan er gekeken worden of het ingevoerde nummer lang genoeg is
     * indien deze functie niet gewenst is `1` meegeven als `length`
     */
    private void checkIntField(JTextField field, int minLength, int maxLength) {
        try {
                if (field.getText().equals("")) {
                    feedback.setForeground(Color.orange);
                    feedback.setText("Veld mag niet leeg zijn");
                    field.setBackground(Color.orange);
                    fieldsOk=false;
                } else if (field.getText().length() < minLength) {
                    feedback.setForeground(Color.red);
                    feedback.setText("Input te kort");
                    field.setBackground(Color.red);
                    fieldsOk=false;
                } else if (field.getText().length() > maxLength) {
                    feedback.setForeground(Color.red);
                    feedback.setText("Input te lang");
                    field.setBackground(Color.red);
                    fieldsOk=false;
                }else {
                    Integer.parseInt(field.getText());
                    feedback.setForeground(Color.black);
                    feedback.setText("");
                    field.setBackground(Color.white);
                }           
        } catch (Exception e) {
            feedback.setForeground(Color.red);
            feedback.setText("Alleen cijfers toegestaan");
            field.setBackground(Color.red);
            fieldsOk=false;
        }
    }
    /*
     *Controleert of de input van een text veld daadwerkelijk een text is
     *Geeft eventuele feedback op de foutive invoer.
     *ook kan er gekeken worden of de ingevoerde text lang genoeg is
     * indien deze functie niet gewenst is `1` meegeven als `length`
     */
    private void checkStringField(JTextField field, int minLength, int maxLength) {
        if (field.getText().equals("")) {
            feedback.setForeground(Color.orange);
            feedback.setText("veld mag niet leeg zijn");
            field.setBackground(Color.orange);
            fieldsOk=false;
        } else {
            try {
                if (field.getText().length() < minLength) {
                    feedback.setForeground(Color.red);
                    feedback.setText("Input te kort");
                    field.setBackground(Color.red);
                    fieldsOk=false;
                } else if (field.getText().length() > maxLength) {
                    feedback.setForeground(Color.red);
                    feedback.setText("Input te lang");
                    field.setBackground(Color.red);
                    fieldsOk=false;
                } else {
                    feedback.setForeground(Color.black);
                    feedback.setText("");
                    field.setBackground(Color.white);
                }
            } catch (Exception e) {
                ePopup(e);
                fieldsOk=false;
            }
        }
    }
    /*
    *Loopt alle velden na en controleerd de input
    *(veldaam, minimale waarde, maximale waarde)
    *returned true als alle velden volgens eis zijn ingevuld 
    */
    private boolean checkFields() {
        fieldsOk=true;
        checkIntField   (id,            3,  12);
        checkStringField(roepnaam,      2,  30);
        checkStringField(achternaam,    2,  30);
        checkStringField(adres,         2,  40);
        checkStringField(postcode,      2,  6);
        checkStringField(woonplaats,    2,  30);
        checkStringField(land,          2,  30);
        checkStringField(email,         2,  40);
        checkStringField(telefoon,      10, 12);
        return fieldsOk;
    }

    /*
     *methode voor het vullen van de progressbar
     *voor een grafische weergave van de bezettingsgraad van een toernooi
     */

    private void ePopup(Exception e) {
        final String eMessage = "Er is iets fout gegaan, neem contact op met de aplicatiebouwer, geef deze foutmelding door: ";
        String error = eMessage + e;
        JOptionPane.showMessageDialog(rootPane, error);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList = new javax.swing.JList();
        feedback = new javax.swing.JLabel();
        verwijder = new javax.swing.JButton();
        voegtoe = new javax.swing.JButton();
        wijzig = new javax.swing.JButton();
        toonId = new javax.swing.JButton();
        toon = new javax.swing.JButton();
        toonlijst = new javax.swing.JButton();
        achternaam = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        roepnaam = new javax.swing.JTextField();
        id = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        adres = new javax.swing.JTextField();
        postcode = new javax.swing.JTextField();
        woonplaats = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        land = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        telefoon = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        jFormattedTextField1.setText("DD");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 95, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 72, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Leden beheer");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Personen"));

        jScrollPane1.setViewportView(jList);

        feedback.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        verwijder.setText("Verwijder persoon");
        verwijder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verwijderActionPerformed(evt);
            }
        });

        voegtoe.setText("Voeg persoon toe");
        voegtoe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voegtoeActionPerformed(evt);
            }
        });

        wijzig.setText("Wijzig  persoon");
        wijzig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wijzigActionPerformed(evt);
            }
        });

        toonId.setText("Toon ID");
        toonId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toonIdActionPerformed(evt);
            }
        });

        toon.setText("Toon persoon");
        toon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toonActionPerformed(evt);
            }
        });

        toonlijst.setText("Toon lijst");
        toonlijst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toonlijstActionPerformed(evt);
            }
        });

        achternaam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                achternaamFocusLost(evt);
            }
        });

        jLabel3.setText("Achternaam:");

        jLabel2.setText("Roepnaam:");

        roepnaam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                roepnaamFocusLost(evt);
            }
        });

        id.setEditable(false);
        id.setFocusCycleRoot(true);
        id.setNextFocusableComponent(roepnaam);
        id.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                idFocusLost(evt);
            }
        });
        id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idActionPerformed(evt);
            }
        });

        jLabel1.setText("ID:");

        jLabel4.setText("Adres:");

        adres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adresActionPerformed(evt);
            }
        });

        jLabel6.setText("Postcode:");

        jLabel7.setText("Woonplaats:");

        jLabel8.setText("Land:");

        jLabel9.setText("Email:");

        jLabel10.setText("Telefoonnummer");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });

        jLabel11.setText("Alle spelers");

        jButton2.setText("Zoek persoon");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(roepnaam, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(postcode, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(woonplaats, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(achternaam)
                                            .addComponent(adres)
                                            .addComponent(id, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(land)
                                            .addComponent(email)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(telefoon)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(voegtoe, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(verwijder, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(wijzig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(feedback, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(toon, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(toonlijst, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toonId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(roepnaam))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(achternaam))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(adres)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(postcode)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(woonplaats)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(land, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(telefoon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(toonId)
                                .addGap(7, 7, 7)
                                .addComponent(toon)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toonlijst, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(voegtoe)
                                    .addComponent(wijzig))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(verwijder)
                                    .addComponent(jButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(feedback, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(34, 34, 34))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1)))
                .addContainerGap())
        );

        jButton3.setText("Terug");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton3)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void adresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_adresActionPerformed

    private void idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idActionPerformed

    private void idFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idFocusLost

    }//GEN-LAST:event_idFocusLost

    private void roepnaamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_roepnaamFocusLost

    }//GEN-LAST:event_roepnaamFocusLost

    private void achternaamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_achternaamFocusLost

    }//GEN-LAST:event_achternaamFocusLost

    private void toonlijstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toonlijstActionPerformed
        getLijst();
    }//GEN-LAST:event_toonlijstActionPerformed

    private void toonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toonActionPerformed
        getPerson();
    }//GEN-LAST:event_toonActionPerformed

    private void toonIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toonIdActionPerformed
        getSelectedId();
    }//GEN-LAST:event_toonIdActionPerformed

    private void wijzigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wijzigActionPerformed
        if(checkFields()==true)setPerson();
    }//GEN-LAST:event_wijzigActionPerformed

    private void voegtoeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voegtoeActionPerformed
        if(checkFields()==true)addPerson();
    }//GEN-LAST:event_voegtoeActionPerformed

    private void verwijderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verwijderActionPerformed
        if(checkFields()==true)deletePerson();
    }//GEN-LAST:event_verwijderActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.dispose();
        Main menu = new Main();
        menu.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(Ledeneditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ledeneditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ledeneditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ledeneditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ledeneditor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField achternaam;
    private javax.swing.JTextField adres;
    private javax.swing.JTextField email;
    private javax.swing.JLabel feedback;
    private javax.swing.JTextField id;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField land;
    private javax.swing.JTextField postcode;
    private javax.swing.JTextField roepnaam;
    private javax.swing.JTextField telefoon;
    private javax.swing.JButton toon;
    private javax.swing.JButton toonId;
    private javax.swing.JButton toonlijst;
    private javax.swing.JButton verwijder;
    private javax.swing.JButton voegtoe;
    private javax.swing.JButton wijzig;
    private javax.swing.JTextField woonplaats;
    // End of variables declaration//GEN-END:variables
}
