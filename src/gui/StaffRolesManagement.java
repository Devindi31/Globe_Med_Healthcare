package gui;

import connection.MySQL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Password;
import raven.toast.Notifications;

public class StaffRolesManagement extends javax.swing.JPanel {

    private HashMap<String, String> roleMap;

    public StaffRolesManagement() {
        initComponents();
        this.roleMap = new HashMap<>();
        loadType();
        loadStaff("");
        Notifications.getInstance().setJFrame(Main.getMain());

        passwprdTextField.setText(Password.generateUniquePassword(8));

    }

    private void loadType() {

        try {

            ResultSet roleResult = MySQL.execute("SELECT * FROM `role`");

            Vector<String> typeVector = new Vector<>();
            typeVector.add("Select Role");

            while (roleResult.next()) {

                if (!"Admin".equals(roleResult.getString("name"))) {
                    typeVector.add(roleResult.getString("name"));
                    roleMap.put(roleResult.getString("name"), roleResult.getString("role_id"));
                }

            }

            roleComboBox.setModel(new DefaultComboBoxModel<>(typeVector));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadStaff(String staffName) {

        try {

            ResultSet patientResult = MySQL.execute("SELECT * FROM `staff` "
                    + "INNER JOIN `role` ON `staff`.`role_role_id`=`role`.`role_id` "
                    + "WHERE `full_name` LIKE '%" + staffName + "%' ORDER BY `registered_date` DESC");

            DefaultTableModel tableModel = (DefaultTableModel) staffTable.getModel();
            tableModel.setRowCount(0);

            while (patientResult.next()) {

                Vector<String> staffVector = new Vector<String>();

                staffVector.add(patientResult.getString("staff_id"));
                staffVector.add(patientResult.getString("full_name"));
                staffVector.add(patientResult.getString("mobile"));
                staffVector.add(patientResult.getString("name"));
                staffVector.add(patientResult.getString("registered_date"));

                String status = patientResult.getString("status");

                if ("1".equals(status)) {
                    staffVector.add("Active");

                } else if ("2".equals(status)) {
                    staffVector.add("Deactive");

                }

                tableModel.addRow(staffVector);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clear() {

        nameTextField.setText("");
        mobileTextField.setText("");
        roleComboBox.setSelectedIndex(0);
        staffIDLabel.setText("None");
        loadStaff("");
        updateButton.setEnabled(false);
        activeButton.setEnabled(false);
        deactiveButton.setEnabled(false);
        registerButton.setEnabled(true);
        roleComboBox.setEnabled(true);
        passwprdTextField.setText(Password.generateUniquePassword(8));

    }

    private void updateStatus(boolean isActivate) {

        String status = "1";
        String statusName = "Active";

        if (!isActivate) {
            status = "2";
            statusName = "Deactive";
        }

        String staffID = staffIDLabel.getText();

        if (staffID.equals("None")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Select Staff");
        } else {

            try {
                ResultSet staffResult = MySQL.execute("SELECT `staff_id` FROM `staff` WHERE `staff_id`='" + staffID + "'");

                if (staffResult.next()) {

                    MySQL.execute("UPDATE `staff` SET `status`='" + status + "' WHERE `staff_id`='" + staffID + "'");
                    clear();
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, staffID + ", You are Now " + statusName + ".");

                } else {
                    JOptionPane.showMessageDialog(this, "Update Not Complete, No staff results found", "An Error Occurred !", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    static class PasswordEncryptor {

        private static final String KEY = "1234567890123456";

        public static String encrypt(String password) throws Exception {
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        staffIDLabel = new javax.swing.JLabel();
        updateButton = new javax.swing.JButton();
        activeButton = new javax.swing.JButton();
        deactiveButton = new javax.swing.JButton();
        registerButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        roleComboBox = new javax.swing.JComboBox<>();
        nameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        staffTable = new javax.swing.JTable();
        mobileTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        passwprdTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel1.setText(" Staff ID :");

        staffIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        staffIDLabel.setText("None");

        updateButton.setBackground(new java.awt.Color(102, 102, 102));
        updateButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        updateButton.setText("Update");
        updateButton.setEnabled(false);
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        activeButton.setBackground(new java.awt.Color(67, 144, 77));
        activeButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        activeButton.setText("Activate");
        activeButton.setEnabled(false);
        activeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activeButtonActionPerformed(evt);
            }
        });

        deactiveButton.setBackground(new java.awt.Color(188, 80, 75));
        deactiveButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deactiveButton.setText("Deactivate");
        deactiveButton.setEnabled(false);
        deactiveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deactiveButtonActionPerformed(evt);
            }
        });

        registerButton.setBackground(java.awt.SystemColor.textHighlight);
        registerButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        registerButton.setText("Register");
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        clearButton.setBackground(new java.awt.Color(51, 51, 51));
        clearButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        roleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        nameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameTextFieldKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel2.setText(" Name :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel3.setText(" Mobile No :");

        staffTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Staff ID", "Full Name", "Mobile Number", "Role", "Registered Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        staffTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                staffTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(staffTable);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setText(" Role :");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Info.png"))); // NOI18N
        jLabel6.setText("Double-click the table row to change the details.");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setText("Medical Staff Roles Management");

        passwprdTextField.setEditable(false);
        passwprdTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setText(" Password :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mobileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(roleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(passwprdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                                .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearButton))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel6))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(staffIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(activeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deactiveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwprdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(mobileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                                .addComponent(roleComboBox)
                                .addComponent(registerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(clearButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(50, 50, 50)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(staffIDLabel, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(523, 523, 523)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(deactiveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(activeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed

        String name = nameTextField.getText();
        String mobile = mobileTextField.getText();
        String role = String.valueOf(roleComboBox.getSelectedItem());
        String password = passwprdTextField.getText();

        if (name == null || name.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Enter Name");
        } else if (name.length() > 100) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Name must be less than 100 characters.");
        } else if (mobile == null || mobile.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Enter a Mobile Number");
        } else if (!mobile.matches("^07[1,2,4,5,6,7,8,0]{1}[0-9]{7}$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Invalid Mobile Number");
        } else if (role.equals("Select Role")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Select Role");
        } else if (password == null || password.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Password cannot be Empty");
        } else {

            try {
                ResultSet staffResult = MySQL.execute("SELECT `mobile` FROM `staff` WHERE `mobile`='" + mobile + "'");

                if (staffResult.next()) {
                    JOptionPane.showMessageDialog(this, "This Mobile is already exists", "Duplicate Entry !", JOptionPane.ERROR_MESSAGE);
                } else {
                    String registerdDate = new SimpleDateFormat("yyyy-MM-d").format(new Date());

                    if (role.equals("Doctor")) {
                        name = "Dr. " + name;
                    }

                    ResultSet lastID = MySQL.execute("SELECT `staff_id` FROM `staff` ORDER BY `staff_id` DESC LIMIT 1;");
                    String staffID = "ST0001";
                    if (lastID.next()) {
                        String lastId = lastID.getString("staff_id");
                        int num = Integer.parseInt(lastId.substring(2));
                        num++;
                        staffID = String.format("ST%04d", num);
                    }

                    MySQL.execute("INSERT INTO `staff` (`staff_id`,`full_name`,`mobile`,`role_role_id`,`registered_date`,`password`) "
                            + "VALUES('" + staffID + "','" + name + "','" + mobile + "','" + roleMap.get(role) + "','" + registerdDate + "','" + PasswordEncryptor.encrypt(password) + "')");
                    clear();
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, "Registration Successfully !");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_registerButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void nameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTextFieldKeyReleased
        loadStaff(nameTextField.getText());
    }//GEN-LAST:event_nameTextFieldKeyReleased

    private void staffTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_staffTableMouseClicked

        int selectedRow = staffTable.getSelectedRow();

        if (evt.getClickCount() == 2 && selectedRow != -1) {

            staffIDLabel.setText(String.valueOf(staffTable.getValueAt(selectedRow, 0)));

            String name = String.valueOf(staffTable.getValueAt(selectedRow, 1));
            if (name.startsWith("Dr. ")) {
                name = name.replaceFirst("Dr\\.\\s*", "");
            }
            nameTextField.setText(name);

            mobileTextField.setText(String.valueOf(staffTable.getValueAt(selectedRow, 2)));
            roleComboBox.setSelectedItem(String.valueOf(staffTable.getValueAt(selectedRow, 3)));

            String status = String.valueOf(staffTable.getValueAt(selectedRow, 5));

            if ("Active".equals(status)) {
                activeButton.setEnabled(false);
                updateButton.setEnabled(true);
                deactiveButton.setEnabled(true);
            } else if ("Deactive".equals(status)) {
                deactiveButton.setEnabled(false);
                updateButton.setEnabled(true);
                activeButton.setEnabled(true);
            }
            registerButton.setEnabled(false);
            roleComboBox.setEnabled(false);

        }

    }//GEN-LAST:event_staffTableMouseClicked

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed

        String name = nameTextField.getText();
        String mobile = mobileTextField.getText();
        String role = String.valueOf(roleComboBox.getSelectedItem());
        String staffID = staffIDLabel.getText();

        if (staffID.equals("None")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Select Staff");
        } else if (name == null || name.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Enter Name");
        } else if (name.length() > 100) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Name must be less than 100 characters.");
        } else if (mobile == null || mobile.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Enter a Mobile Number");
        } else if (!mobile.matches("^07[1,2,4,5,6,7,8,0]{1}[0-9]{7}$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Invalid Mobile Number");
        } else if (role.equals("Select Role")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Select Role");
        } else {

            try {
                ResultSet staffResult = MySQL.execute("SELECT `staff_id` FROM `staff` WHERE `staff_id`='" + staffID + "'");

                if (staffResult.next()) {

                    if (role.equals("Doctor")) {
                        name = "Dr. " + name;
                    }

                    ResultSet staffMobile = MySQL.execute("SELECT `mobile`,`staff_id` FROM `staff` WHERE `mobile`='" + mobile + "' AND `staff_id`!='" + staffID + "'");

                    if (staffMobile.next()) {
                        JOptionPane.showMessageDialog(this, "This mobile number cannot be updated: Mobile number already exists.", "Couldn't update. !", JOptionPane.ERROR_MESSAGE);
                    } else {
                        MySQL.execute("UPDATE `staff` SET `full_name`='" + name + "',`mobile`='" + mobile + "' WHERE `staff_id`='" + staffID + "'");
                        clear();
                        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, name + "'s details were successfully updated");

                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Update Not Complete, No staff results found", "An Error Occurred !", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_updateButtonActionPerformed

    private void activeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activeButtonActionPerformed
        updateStatus(true);
    }//GEN-LAST:event_activeButtonActionPerformed

    private void deactiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deactiveButtonActionPerformed
        updateStatus(false);
    }//GEN-LAST:event_deactiveButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activeButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton deactiveButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField mobileTextField;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField passwprdTextField;
    private javax.swing.JButton registerButton;
    private javax.swing.JComboBox<String> roleComboBox;
    private javax.swing.JLabel staffIDLabel;
    private javax.swing.JTable staffTable;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
