package dialogs;

import connection.MySQL;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;

public class AddLaboratoryTests extends javax.swing.JDialog {

    private String medicalID;

    public AddLaboratoryTests(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Notifications.getInstance().setJFrame((JFrame) parent);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        laboratoryTestDescriptionTextArea = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        amountTextField = new javax.swing.JTextField();
        addLaboratoryTestButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        laboratoryTestLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        laboratoryDetailsTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel5.setText(" Description :");

        laboratoryTestDescriptionTextArea.setColumns(20);
        laboratoryTestDescriptionTextArea.setRows(5);
        jScrollPane1.setViewportView(laboratoryTestDescriptionTextArea);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel6.setText(" Amount :");

        addLaboratoryTestButton.setBackground(java.awt.SystemColor.textHighlight);
        addLaboratoryTestButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addLaboratoryTestButton.setText("Add");
        addLaboratoryTestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLaboratoryTestButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Laboratory Tests Details");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel3.setText(" Name  :");

        laboratoryTestLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        laboratoryTestLabel.setText("None");

        laboratoryDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Laboratory Test ID", "Name", "Description", "Amount (LKR)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(laboratoryDetailsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(laboratoryTestLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel5)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(addLaboratoryTestButton))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2))))
                        .addGap(0, 6, Short.MAX_VALUE)))
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
                    .addComponent(laboratoryTestLabel))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addLaboratoryTestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void loadLaboratoryTestDetails(String medicalID) {

        try {

            ResultSet patientMobile = MySQL.execute("SELECT `patient`.`mobile` FROM `medical_history` "
                    + "INNER JOIN `patient` ON `medical_history`.`patient_mobile`=`patient`.`mobile` WHERE `medical_history_id`='" + medicalID + "'");

            if (patientMobile.next()) {

                ResultSet laboratoryTestResult = MySQL.execute("SELECT `laboratory_test_id`,`laboratory_test`.`name`,`description`,`amount`,`patient`.`mobile` FROM `laboratory_test` "
                        + "INNER JOIN `medical_history` ON `laboratory_test`.`medical_history_id`=`medical_history`.`medical_history_id`"
                        + "INNER JOIN `patient` ON `medical_history`.`patient_mobile`=`patient`.`mobile` "
                        + "WHERE `patient`.`mobile`='" + patientMobile.getString("patient.mobile") + "'");

                DefaultTableModel tableModel = (DefaultTableModel) laboratoryDetailsTable.getModel();
                tableModel.setRowCount(0);

                while (laboratoryTestResult.next()) {

                    Vector<String> laboratoryTestDetailsVector = new Vector<String>();

                    laboratoryTestDetailsVector.add(laboratoryTestResult.getString("laboratory_test_id"));
                    laboratoryTestDetailsVector.add(laboratoryTestResult.getString("laboratory_test.name"));
                    laboratoryTestDetailsVector.add(laboratoryTestResult.getString("description"));
                    laboratoryTestDetailsVector.add(laboratoryTestResult.getString("amount"));

                    tableModel.addRow(laboratoryTestDetailsVector);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setMedicalID(String medicalID) {
        this.medicalID = medicalID;
    }

    public void setLaboratoryTestName(String laboratoryTestName) {
        this.laboratoryTestLabel.setText(laboratoryTestName);
    }


    private void addLaboratoryTestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLaboratoryTestButtonActionPerformed

        String laboratoryTestName = laboratoryTestLabel.getText();
        String laboratoryTestDescription = laboratoryTestDescriptionTextArea.getText().trim();
        String laboratoryTestAmount = amountTextField.getText();

        if (laboratoryTestName.equals("None")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "No laboratory test OR Please select medical history table row");
        } else if (laboratoryTestDescription.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please enter laboratory test description");
        } else if (laboratoryTestAmount.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please enter amount");
        } else if (!laboratoryTestAmount.matches("^\\d+$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please enter a valid amount (numbers only, no decimals)");
        } else {

            try {

                if (Double.parseDouble(laboratoryTestAmount) <= 0) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Amount must be greater than 0");
                } else {

                    ResultSet lastID = MySQL.execute("SELECT `laboratory_test_id` FROM `laboratory_test` ORDER BY `laboratory_test_id` DESC LIMIT 1;");

                    String newLaboratoryTestId = "LB0001";

                    if (lastID.next()) {
                        String lastId = lastID.getString("laboratory_test_id");
                        int num = Integer.parseInt(lastId.substring(2));
                        num++;
                        newLaboratoryTestId = String.format("LB%04d", num);
                    }

                    MySQL.execute("INSERT INTO `laboratory_test` (`laboratory_test_id`,`name`,`description`,`amount`,`medical_history_id`) "
                            + "VALUES('" + newLaboratoryTestId + "','" + laboratoryTestName + "','" + laboratoryTestDescription + "','" + laboratoryTestAmount + "','" + medicalID + "')");
                    loadLaboratoryTestDetails(medicalID);
                    this.dispose();
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, "Laboratory test details were successfully added.");

                }

            } catch (Exception e) {
                throw new RuntimeException();
            }

        }

    }//GEN-LAST:event_addLaboratoryTestButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddLaboratoryTests dialog = new AddLaboratoryTests(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton addLaboratoryTestButton;
    private javax.swing.JTextField amountTextField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable laboratoryDetailsTable;
    private javax.swing.JTextArea laboratoryTestDescriptionTextArea;
    private javax.swing.JLabel laboratoryTestLabel;
    // End of variables declaration//GEN-END:variables
}
