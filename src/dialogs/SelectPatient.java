package dialogs;

import connection.MySQL;
import gui.AppointmentScheduling;
import gui.BillingAndInsuranceClaims;
import gui.Reports;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class SelectPatient extends javax.swing.JDialog {
    
    private String patientName;
    private String ABR;
    private final BillingAndInsuranceClaims billingAndInsuranceClaims;
    private final AppointmentScheduling appointmentScheduling;
    private final Reports reports;
    
    public SelectPatient(java.awt.Frame parent, boolean modal, String ABR, BillingAndInsuranceClaims billingAndInsuranceClaims, AppointmentScheduling appointmentScheduling, Reports reports) {
        super(parent, modal);
        initComponents();
        this.billingAndInsuranceClaims = billingAndInsuranceClaims;
        this.appointmentScheduling = appointmentScheduling;
        this.reports = reports;
        this.ABR = ABR;
        loadPatient("");
        selectButton.setEnabled(false);
    }
    
    private void loadPatient(String patientName) {
        
        try {
            
            ResultSet patientResult = MySQL.execute("SELECT * FROM `patient` WHERE `full_name` LIKE '%" + patientName + "%' ORDER BY `registered_date` DESC");
            
            DefaultTableModel tableModel = (DefaultTableModel) patientDetailsTable.getModel();
            tableModel.setRowCount(0);
            
            while (patientResult.next()) {
                
                Vector<String> patientVector = new Vector<String>();
                
                patientVector.add(patientResult.getString("full_name"));
                patientVector.add(patientResult.getString("age"));
                patientVector.add(patientResult.getString("mobile"));
                patientVector.add(patientResult.getString("home_town"));
                patientVector.add(patientResult.getString("registered_date"));
                
                tableModel.addRow(patientVector);
                
            }
            
        } catch (Exception e) {
            throw new RuntimeException();
        }
        
    }
    
    private void clear() {
        
        patientMobileLabel.setText("None");
        patientNameTextField.setText("");
        loadPatient("");
        selectButton.setEnabled(false);
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        patientNameTextField = new javax.swing.JTextField();
        clearButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        patientDetailsTable = new javax.swing.JTable();
        selectButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        patientMobileLabel = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Register New Patient");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Select Patient");

        jLabel3.setText("Search by Name");

        patientNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                patientNameTextFieldKeyReleased(evt);
            }
        });

        clearButton.setBackground(new java.awt.Color(51, 51, 51));
        clearButton.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        patientDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Full Name", "Age", "Mobile Number", "Home Town"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        patientDetailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                patientDetailsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(patientDetailsTable);

        selectButton.setBackground(java.awt.SystemColor.textHighlight);
        selectButton.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        selectButton.setText("Select");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Patient Mobile :");

        patientMobileLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        patientMobileLabel.setText("None");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel2))
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(patientNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearButton)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(patientMobileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(patientNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(patientMobileLabel))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void patientNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_patientNameTextFieldKeyReleased
        loadPatient(patientNameTextField.getText());
    }//GEN-LAST:event_patientNameTextFieldKeyReleased

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void patientDetailsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_patientDetailsTableMouseClicked
        
        int selectedRow = patientDetailsTable.getSelectedRow();
        
        if (evt.getClickCount() == 2 && selectedRow != -1) {
            
            patientMobileLabel.setText(String.valueOf(patientDetailsTable.getValueAt(selectedRow, 2)));
            patientName = String.valueOf(patientDetailsTable.getValueAt(selectedRow, 0));
            selectButton.setEnabled(true);
            
        }

    }//GEN-LAST:event_patientDetailsTableMouseClicked

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        
        if ("A".equals(ABR)) {
            appointmentScheduling.setPatientName(patientName);
            appointmentScheduling.setPatientMobile(patientMobileLabel.getText());
            this.dispose();
        } else if ("B".equals(ABR)) {
            billingAndInsuranceClaims.setPatientMobile(patientMobileLabel.getText());
            billingAndInsuranceClaims.setPatientName(patientName);
            billingAndInsuranceClaims.loadMedicalDetails(new Date());
            this.dispose();
        } else if ("RT".equals(ABR)) {
            reports.setPatientName(patientName);
            reports.setPatientMobile(patientMobileLabel.getText());
            reports.loadMedicalHistory(patientMobileLabel.getText());
            this.dispose();
        } else if ("RD".equals(ABR)) {
            reports.setdPatientName(patientName);
            reports.setdPatientMobile(patientMobileLabel.getText());
            reports.loadLabTestSummary(patientMobileLabel.getText());
            this.dispose();
        }
        

    }//GEN-LAST:event_selectButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable patientDetailsTable;
    private javax.swing.JLabel patientMobileLabel;
    private javax.swing.JTextField patientNameTextField;
    private javax.swing.JButton selectButton;
    // End of variables declaration//GEN-END:variables
}
