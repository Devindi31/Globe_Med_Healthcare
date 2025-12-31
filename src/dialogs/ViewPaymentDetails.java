package dialogs;

import connection.MySQL;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class ViewPaymentDetails extends javax.swing.JDialog {
    
    private String billNumber;
    
    public ViewPaymentDetails(java.awt.Frame parent, boolean modal, String newBillNumber) {
        super(parent, modal);
        this.billNumber = newBillNumber;
        initComponents();
        loadBasicInfo(billNumber);
    }
    
    private void loadBasicInfo(String billNumber) {
        
        try {
            
            ResultSet basicResult = MySQL.execute("SELECT `bill_number`,`patient`.`full_name`,`patient`.`mobile`,`added_date`,`total`,`paid_amount`,`balance`,`payment_type`.`type` "
                    + "FROM `bill` "
                    + "INNER JOIN `patient` ON `bill`.`patient_mobile`=`patient`.`mobile` "
                    + "INNER JOIN `payment_type` ON `bill`.`payment_type_id`=`payment_type`.`payment_type_id` "
                    + "WHERE `bill_number`='" + billNumber + "'");
            
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            
            if (basicResult.next()) {
                
                dateTimeLabel.setText(new SimpleDateFormat("yyyy-MMMM-dd hh:mm:ss a").format(new Date()));
                billNumberLabel.setText(billNumber);
                patientNameLabel.setText(basicResult.getString("patient.full_name"));
                patientMobileLabel.setText(basicResult.getString("patient.mobile"));
                appointmentDateLabel.setText(basicResult.getString("added_date"));
                totalLabel.setText(decimalFormat.format(basicResult.getDouble("total")));
                paidAmountLabel.setText(decimalFormat.format(basicResult.getDouble("paid_amount")));
                balanceLabel.setText(decimalFormat.format(basicResult.getDouble("balance")));
                paymentMethodLabel.setText(basicResult.getString("payment_type.type"));
                
                loadBillTable(billNumber);
                
            } else {
                JOptionPane.showMessageDialog(this, "Payment Details Not Found.", "View Details Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void loadBillTable(String billNumber) {
        
        try {
            
            ResultSet basicResult = MySQL.execute("SELECT `item_name`,`unit_price`,`quantity` FROM `bill` WHERE `bill_number`='" + billNumber + "'");
            DefaultTableModel tableModel = (DefaultTableModel) billDetailsTable.getModel();
            tableModel.setRowCount(0);
            
            while (basicResult.next()) {
                
                Vector<String> billResultVector = new Vector<String>();
                
                billResultVector.add(basicResult.getString("item_name"));
                billResultVector.add(basicResult.getString("unit_price"));
                billResultVector.add(basicResult.getString("quantity"));
                
                tableModel.addRow(billResultVector);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        dateTimeLabel = new javax.swing.JLabel();
        billNumberLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        patientNameLabel = new javax.swing.JLabel();
        patientMobileLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        billDetailsTable = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        appointmentDateLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        balanceLabel = new javax.swing.JLabel();
        paidAmountLabel = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        paymentMethodLabel = new javax.swing.JLabel();
        printInvoiceButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Invoice");

        dateTimeLabel.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        dateTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dateTimeLabel.setText("Date Time");

        billNumberLabel.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        billNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        billNumberLabel.setText("Bill No :");

        jLabel4.setText("Patient Name :");

        jLabel5.setText("Patient Mobile  :");

        patientNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        patientNameLabel.setText("None");

        patientMobileLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        patientMobileLabel.setText("None");

        billDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Unit Price", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        billDetailsTable.setEnabled(false);
        billDetailsTable.setFocusable(false);
        jScrollPane1.setViewportView(billDetailsTable);
        if (billDetailsTable.getColumnModel().getColumnCount() > 0) {
            billDetailsTable.getColumnModel().getColumn(1).setMinWidth(150);
            billDetailsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            billDetailsTable.getColumnModel().getColumn(1).setMaxWidth(150);
            billDetailsTable.getColumnModel().getColumn(2).setMinWidth(75);
            billDetailsTable.getColumnModel().getColumn(2).setPreferredWidth(75);
            billDetailsTable.getColumnModel().getColumn(2).setMaxWidth(75);
        }

        jLabel8.setText("Appointment Date :");

        appointmentDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        appointmentDateLabel.setText("None");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Total :");

        jLabel11.setText("Paid Amount :");

        jLabel12.setText("Balance :");

        balanceLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        balanceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        balanceLabel.setText("None");

        paidAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        paidAmountLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paidAmountLabel.setText("None");

        totalLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalLabel.setText("None");

        jLabel16.setText("Payment Method :");

        paymentMethodLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        paymentMethodLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paymentMethodLabel.setText("None");

        printInvoiceButton.setBackground(java.awt.SystemColor.textHighlight);
        printInvoiceButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        printInvoiceButton.setText("Print Invoice");
        printInvoiceButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        printInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printInvoiceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1152, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateTimeLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(billNumberLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(patientMobileLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(patientNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(appointmentDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(totalLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                                    .addComponent(paidAmountLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(balanceLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(paymentMethodLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(printInvoiceButton, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dateTimeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(billNumberLabel))
                    .addComponent(jLabel1))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(patientNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(appointmentDateLabel)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(patientMobileLabel)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalLabel)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paidAmountLabel)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(balanceLabel)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymentMethodLabel)
                    .addComponent(jLabel16))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(printInvoiceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void printInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printInvoiceButtonActionPerformed
        
        try {
            
            HashMap<String, Object> parameters = new HashMap<>();
            String filePath = "src//reports/bill_report.jasper";
            
            JRDataSource dataSource = new JRTableModelDataSource(billDetailsTable.getModel());
            
            parameters.put("patient_name", patientNameLabel.getText());
            parameters.put("patient_mobile", patientMobileLabel.getText());
            parameters.put("appointment_date", appointmentDateLabel.getText());
            parameters.put("total_amount", totalLabel.getText());
            parameters.put("paid_amount", paidAmountLabel.getText());
            parameters.put("balance", balanceLabel.getText());
            parameters.put("payment_method", paymentMethodLabel.getText());
            
            JasperPrint report = JasperFillManager.fillReport(filePath, parameters, dataSource);
            
            JasperViewer viewer = new JasperViewer(report, false);
            viewer.setAlwaysOnTop(true);
            viewer.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_printInvoiceButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel appointmentDateLabel;
    private javax.swing.JLabel balanceLabel;
    private javax.swing.JTable billDetailsTable;
    private javax.swing.JLabel billNumberLabel;
    private javax.swing.JLabel dateTimeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel paidAmountLabel;
    private javax.swing.JLabel patientMobileLabel;
    private javax.swing.JLabel patientNameLabel;
    private javax.swing.JLabel paymentMethodLabel;
    private javax.swing.JButton printInvoiceButton;
    private javax.swing.JLabel totalLabel;
    // End of variables declaration//GEN-END:variables
}
