package dialogs;

import connection.MySQL;
import gui.BillingAndInsuranceClaims;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;

public class SelectMedicines extends javax.swing.JDialog {
    
    private final BillingAndInsuranceClaims billingAndInsuranceClaims;

    public SelectMedicines(java.awt.Frame parent, boolean modal,BillingAndInsuranceClaims billingAndInsuranceClaims) {
        super(parent, modal);
        initComponents();
        this.billingAndInsuranceClaims = billingAndInsuranceClaims;
        loadMedicines("");
        selectButton.setEnabled(false);
        Notifications.getInstance().setJFrame((JFrame) parent);
    }

    private void loadMedicines(String medicineName) {

        try {

            ResultSet medicinesResult = MySQL.execute("SELECT * FROM `medicine` WHERE `name` LIKE '%" + medicineName + "%' ORDER BY `name` ASC");

            DefaultTableModel tableModel = (DefaultTableModel) medicinesTable.getModel();
            tableModel.setRowCount(0);

            while (medicinesResult.next()) {

                Vector<String> medicinesVector = new Vector<String>();

                medicinesVector.add(medicinesResult.getString("medicine_id"));
                medicinesVector.add(medicinesResult.getString("name"));
                medicinesVector.add(medicinesResult.getString("unit price"));

                tableModel.addRow(medicinesVector);

            }

        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    private void clear() {

        medicineNameTextField.setText("");
        loadMedicines("");
        quantityTextField.setText("");
        medicineIDLabel.setText("None");
        selectButton.setEnabled(false);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clearButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        medicinesTable = new javax.swing.JTable();
        selectButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        medicineNameTextField = new javax.swing.JTextField();
        quantityTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        medicineIDLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        clearButton.setBackground(new java.awt.Color(51, 51, 51));
        clearButton.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        medicinesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Medicine ID", "Item Name", "Unit Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        medicinesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                medicinesTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(medicinesTable);

        selectButton.setBackground(java.awt.SystemColor.textHighlight);
        selectButton.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        selectButton.setText("Select");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Select Medicine");

        jLabel3.setText("Search by Item Name");

        medicineNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                medicineNameTextFieldKeyReleased(evt);
            }
        });

        quantityTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                quantityTextFieldKeyReleased(evt);
            }
        });

        jLabel4.setText("Qty :");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Info.png"))); // NOI18N
        jLabel1.setText("Double click the table row to select the medication.");

        jLabel5.setText(" Medicine ID :");

        medicineIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        medicineIDLabel.setText("None");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel2))
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(medicineNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(medicineIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(medicineNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(medicineIDLabel)))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void medicineNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_medicineNameTextFieldKeyReleased
        loadMedicines(medicineNameTextField.getText());
    }//GEN-LAST:event_medicineNameTextFieldKeyReleased

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void medicinesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_medicinesTableMouseClicked

        int selectedRow = medicinesTable.getSelectedRow();

        if (evt.getClickCount() == 2 && selectedRow != -1) {
            medicineIDLabel.setText(String.valueOf(medicinesTable.getValueAt(selectedRow, 0)));
        }

    }//GEN-LAST:event_medicinesTableMouseClicked

    private void quantityTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantityTextFieldKeyReleased

        if (!quantityTextField.getText().isBlank()) {
            selectButton.setEnabled(true);
        } else {
            selectButton.setEnabled(false);
        }

    }//GEN-LAST:event_quantityTextFieldKeyReleased

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed

        String medicineID = medicineIDLabel.getText();
        String quantity = quantityTextField.getText();

        if (!quantity.matches("^[1-9][0-9]*$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Invalid Quantity");
        } else {

            try {

                MySQL.execute("INSERT INTO `patient_has_medicines` (`patient_mobile`,`medicine_medicine_id`,`quantity`,`added_date`) "
                        + "VALUES('" + billingAndInsuranceClaims.getPatientMobile() + "','" + medicineID + "','" + quantity + "','" + billingAndInsuranceClaims.getAddedDate() + "')");

                Date addedDate = new SimpleDateFormat("yyyy-MM-dd").parse(billingAndInsuranceClaims.getAddedDate());

                billingAndInsuranceClaims.loadMedicalDetails(addedDate);
                this.dispose();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_selectButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel medicineIDLabel;
    private javax.swing.JTextField medicineNameTextField;
    private javax.swing.JTable medicinesTable;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JButton selectButton;
    // End of variables declaration//GEN-END:variables
}
