package dialogs;

import connection.MySQL;
import javax.swing.JFrame;
import raven.toast.Notifications;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AddSurgeryDetails extends javax.swing.JDialog {

    private String medicalID;

    public AddSurgeryDetails(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Notifications.getInstance().setJFrame((JFrame) parent);
    }

    public void loadSurgeryDetails(String medicalID) {

        try {

            ResultSet patientMobile = MySQL.execute("SELECT `patient`.`mobile` FROM `medical_history` "
                    + "INNER JOIN `patient` ON `medical_history`.`patient_mobile`=`patient`.`mobile` WHERE `medical_history_id`='" + medicalID + "'");

            if (patientMobile.next()) {

                ResultSet surgeryResult = MySQL.execute("SELECT `surgery_id`,`surgery`.`name`,`description`,`amount`,`patient`.`mobile` FROM `surgery` "
                        + "INNER JOIN `medical_history` ON `surgery`.`medical_history_id`=`medical_history`.`medical_history_id`"
                        + "INNER JOIN `patient` ON `medical_history`.`patient_mobile`=`patient`.`mobile` "
                        + "WHERE `patient`.`mobile`='" + patientMobile.getString("patient.mobile") + "'");

                DefaultTableModel tableModel = (DefaultTableModel) surgeryDetailsTable.getModel();
                tableModel.setRowCount(0);

                while (surgeryResult.next()) {

                    Vector<String> surgeryDetailsVector = new Vector<String>();

                    surgeryDetailsVector.add(surgeryResult.getString("surgery_id"));
                    surgeryDetailsVector.add(surgeryResult.getString("surgery.name"));
                    surgeryDetailsVector.add(surgeryResult.getString("description"));
                    surgeryDetailsVector.add(surgeryResult.getString("amount"));

                    tableModel.addRow(surgeryDetailsVector);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setSurgeryName(String surgeryName) {
        this.surgeryNameLabel.setText(surgeryName);
    }

    public void setMedicalID(String medicalID) {
        this.medicalID = medicalID;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        surgeryNameLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        surgeryDescriptionTextArea = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        amountTextField = new javax.swing.JTextField();
        addSurgeryDetailsButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        surgeryDetailsTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Surgery Details");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel3.setText(" Name  :");

        surgeryNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        surgeryNameLabel.setText("None");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel5.setText(" Description :");

        surgeryDescriptionTextArea.setColumns(20);
        surgeryDescriptionTextArea.setRows(5);
        jScrollPane1.setViewportView(surgeryDescriptionTextArea);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel6.setText(" Amount :");

        addSurgeryDetailsButton.setBackground(java.awt.SystemColor.textHighlight);
        addSurgeryDetailsButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addSurgeryDetailsButton.setText("Add");
        addSurgeryDetailsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSurgeryDetailsButtonActionPerformed(evt);
            }
        });

        surgeryDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Surgery ID", "Name", "Description", "Amount (LKR)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(surgeryDetailsTable);
        if (surgeryDetailsTable.getColumnModel().getColumnCount() > 0) {
            surgeryDetailsTable.getColumnModel().getColumn(0).setMinWidth(100);
            surgeryDetailsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            surgeryDetailsTable.getColumnModel().getColumn(0).setMaxWidth(100);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(surgeryNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(addSurgeryDetailsButton))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2))
                                .addGap(0, 6, Short.MAX_VALUE)))))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(surgeryNameLabel))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addSurgeryDetailsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addSurgeryDetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSurgeryDetailsButtonActionPerformed
       
        String surgeryName = surgeryNameLabel.getText();
        String surgeryDescription = surgeryDescriptionTextArea.getText().trim();
        String amount = amountTextField.getText();

        if (surgeryName.equals("None")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "No surgery OR Please select medical history table row");
        } else if (surgeryDescription.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please enter surgery description");
        } else if (amount.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please enter surgery amount");
        } else if (!amount.matches("^\\d+$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please enter a valid amount (numbers only, no decimals)");
        } else {

            try {

                if (Double.parseDouble(amount) <= 0) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Surgery amount must be greater than 0");
                } else {          

                    ResultSet surgeryDetails = MySQL.execute("SELECT `medical_history_id` FROM `surgery` WHERE `medical_history_id`='" + medicalID + "'");

                    if (surgeryDetails.next()) {
                        JOptionPane.showMessageDialog(this, "This Surgery Detail is Already Exists", "Duplicate Entry !", JOptionPane.ERROR_MESSAGE);
                    } else {

                        ResultSet lastID = MySQL.execute("SELECT `surgery_id` FROM `surgery` ORDER BY `surgery_id` DESC LIMIT 1;");

                        String newSurgeryId = "SG0001";

                        if (lastID.next()) {
                            String lastId = lastID.getString("surgery_id");
                            int num = Integer.parseInt(lastId.substring(2));
                            num++;
                            newSurgeryId = String.format("SG%04d", num);
                        }

                        MySQL.execute("INSERT INTO `surgery` (`surgery_id`,`name`,`description`,`amount`,`medical_history_id`) "
                                + "VALUES ('" + newSurgeryId + "','" + surgeryName + "','" + surgeryDescription + "','" + amount + "','" + medicalID + "')");
                        loadSurgeryDetails(medicalID);
                        this.dispose();
                        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, "Laboratory test details were successfully collected.");
                    }

                }

            } catch (Exception e) {
                throw new RuntimeException();
            }

        }

    }//GEN-LAST:event_addSurgeryDetailsButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddSurgeryDetails dialog = new AddSurgeryDetails(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSurgeryDetailsButton;
    private javax.swing.JTextField amountTextField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea surgeryDescriptionTextArea;
    private javax.swing.JTable surgeryDetailsTable;
    private javax.swing.JLabel surgeryNameLabel;
    // End of variables declaration//GEN-END:variables
}
