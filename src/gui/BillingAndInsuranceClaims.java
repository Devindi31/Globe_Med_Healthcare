package gui;

import com.formdev.flatlaf.FlatLaf;
import connection.MySQL;
import dialogs.SelectMedicines;
import dialogs.SelectPatient;
import dialogs.ViewConsultationPayment;
import dialogs.ViewPaymentDetails;
import java.awt.Color;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.ButtonModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import permission.RoleRepository;
import raven.toast.Notifications;

interface BillService {

    void processPayment(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String total, String paid, String balance, String PaymentMethod);
}

interface PaymentProcessor {

    void pay(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String total, String paid, String balance, String PaymentMethod);
}

class ConsultationService implements BillService {

    private PaymentProcessor processor;

    public ConsultationService(PaymentProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void processPayment(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String consultationFee, String paid, String balance, String PaymentMethod) {
        processor.pay(billNumber, patientMobile, addedDate, itemName + " (Consultation)", unitPrice, quantity, consultationFee, paid, balance, PaymentMethod);
    }
}

class LaboratoryTestService implements BillService {

    private PaymentProcessor processor;

    public LaboratoryTestService(PaymentProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void processPayment(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String total, String paid, String balance, String PaymentMethod) {
        processor.pay(billNumber, patientMobile, addedDate, itemName + " (Laboratory Test)", unitPrice, quantity, total, paid, balance, PaymentMethod);
    }
}

class SurgeryService implements BillService {

    private PaymentProcessor processor;

    public SurgeryService(PaymentProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void processPayment(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String total, String paid, String balance, String PaymentMethod) {
        processor.pay(billNumber, patientMobile, addedDate, itemName + " (Surgery)", unitPrice, quantity, total, paid, balance, PaymentMethod);
    }
}

class MedicationService implements BillService {

    private PaymentProcessor processor;

    public MedicationService(PaymentProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void processPayment(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String total, String paid, String balance, String PaymentMethod) {
        processor.pay(billNumber, patientMobile, addedDate, itemName + " (Medicines)", unitPrice, quantity, total, paid, balance, PaymentMethod);
    }
}

class DirectPaymentProcessor implements PaymentProcessor {

    @Override
    public void pay(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String total, String paid, String balance, String paymentMethod) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
            MySQL.execute("INSERT INTO `bill` "
                    + "(`bill_number`,`patient_mobile`,`added_date`,`item_name`,`unit_price`,`quantity`,`total`,`paid_amount`,`balance`,`payment_type_id`,`payment_date`) "
                    + "VALUES ('" + billNumber + "','" + patientMobile + "','" + addedDate + "','" + itemName + "','" + unitPrice + "','" + quantity + "','" + total + "','" + paid + "','" + balance + "','" + paymentMethod + "','" + date + "')"
            );

            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, "Direct payment processed for " + itemName + " : LKR " + total);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class CardPaymentProcessor implements PaymentProcessor {

    @Override
    public void pay(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String total, String paid, String balance, String paymentMethod) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
            MySQL.execute("INSERT INTO `bill` "
                    + "(`bill_number`,`patient_mobile`,`added_date`,`item_name`,`unit_price`,`quantity`,`total`,`paid_amount`,`balance`,`payment_type_id`,`payment_date`) "
                    + "VALUES ('" + billNumber + "','" + patientMobile + "','" + addedDate + "','" + itemName + "','" + unitPrice + "','" + quantity + "','" + total + "','" + paid + "','" + balance + "','" + paymentMethod + "','" + date + "')"
            );

            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, "Credit/Debit Card payment processed for " + itemName + " : LKR " + total);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class InsuranceClaimProcessor implements PaymentProcessor {

    @Override
    public void pay(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String total, String paid, String balance, String paymentMethod) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
            MySQL.execute("INSERT INTO `bill` "
                    + "(`bill_number`,`patient_mobile`,`added_date`,`item_name`,`unit_price`,`quantity`,`total`,`paid_amount`,`balance`,`payment_type_id`,`payment_date`) "
                    + "VALUES ('" + billNumber + "','" + patientMobile + "','" + addedDate + "','" + itemName + "','" + unitPrice + "','" + quantity + "','" + total + "','" + paid + "','" + balance + "','" + paymentMethod + "','" + date + "')"
            );
            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, "Insurance claim processed for " + itemName + " : LKR " + total);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class BillingAndInsuranceClaims extends javax.swing.JPanel {

    private String patientMobile;
    private String addedDate;
    private double totalAmount;
    private double paidAmount = 0.0;
    private double consultationFee;
    private int consultationCount;
    private boolean alreadypaid;
    private String newBillNumber = "BL0001";

    public BillingAndInsuranceClaims() {
        initComponents();
        defaultPermission();
        setupPermissions();
        Notifications.getInstance().setJFrame(Main.getMain());
        medicalDateChooser.setEnabled(false);
        setupPaidAmountField();
        proceedPaymentButton.setEnabled(false);
        payConsultationButton.setEnabled(false);
    }

    private void defaultPermission() {
        insuranceRadioButton.setVisible(false);
        selectMedicineButton.setVisible(false);
        payConsultationButton.setVisible(false);
        proceedPaymentButton.setVisible(false);
    }

    private void setupPermissions() {
        if (RoleRepository.hasPermission(Login.userRole, "bill_proceed_payment_button")) {
            proceedPaymentButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "bill_select_medicines_button")) {
            selectMedicineButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "bill_pay_consultation_charges_button")) {
            payConsultationButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "bill_insurance_claim_radio_buttton")) {
            insuranceRadioButton.setVisible(true);
        }
    }

    public void setPatientName(String patientName) {
        this.patientNameLabel.setText(patientName);;
    }

    public void setPatientMobile(String patientMobile) {
        this.patientMobile = patientMobile;
    }

    public String getPatientMobile() {
        return patientMobile;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void loadMedicalDetails(Date date) {

        try {
            medicalDateChooser.setEnabled(true);
            String searchDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            resetAmount();
            addedDate = searchDate;
            consultationCount = 0;
            loadConsultation(patientMobile, searchDate);

            ResultSet medicalHistoryResult = MySQL.execute("SELECT `medical_history_id`,`patient_mobile`,`added_date`,`illnesses`,`surgeries`,`laboratory_test`,`medicines` FROM `medical_history` "
                    + "WHERE `patient_mobile` = '" + patientMobile + "' AND `added_date`='" + searchDate + "'");

            DefaultTableModel tableModel = (DefaultTableModel) medicalDetailsTable.getModel();
            tableModel.setRowCount(0);

            ((DefaultTableModel) surgeryDetailsTable.getModel()).setRowCount(0);
            ((DefaultTableModel) laboratoryTestDetailsTable.getModel()).setRowCount(0);

            while (medicalHistoryResult.next()) {

                Vector<String> medicalHistoryVector = new Vector<String>();

                medicalHistoryVector.add(medicalHistoryResult.getString("added_date"));
                medicalHistoryVector.add(medicalHistoryResult.getString("illnesses"));
                medicalHistoryVector.add(medicalHistoryResult.getString("surgeries"));
                medicalHistoryVector.add(medicalHistoryResult.getString("laboratory_test"));
                medicalHistoryVector.add(medicalHistoryResult.getString("medicines"));

                loadSurgeryDetails(medicalHistoryResult.getString("medical_history_id"));
                loadLaboratoryTestDetails(medicalHistoryResult.getString("medical_history_id"));
                addedDate = medicalHistoryResult.getString("added_date");

                tableModel.addRow(medicalHistoryVector);

            }
            loadPatientMedicines(searchDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadConsultation(String patientMobile, String searchDate) {
        

        try {

            ResultSet appointmentResult = MySQL.execute("SELECT `appointment_number`,`patient`,`appointment_date`,`type_type_id` FROM `appointment` "
                    + "WHERE `patient`='" + patientMobile + "' AND `appointment_date`='" + searchDate + "' AND `type_type_id`='1'");

            while (appointmentResult.next()) {
                consultationCount++;
                if (Login.userRole.equals("Nurse") || Login.userRole.equals("Admin")) {
                    consultationFee += 2000;
                }
                calculateTotal(consultationFee);
            }

            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            consultationFeeLabel.setText(decimalFormat.format(consultationFee));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void loadSurgeryDetails(String medicalID) {

        try {

            ResultSet surgeryDetailsResult = MySQL.execute("SELECT * FROM `surgery` WHERE `medical_history_id`='" + medicalID + "'");

            DefaultTableModel tableModel = (DefaultTableModel) surgeryDetailsTable.getModel();

            while (surgeryDetailsResult.next()) {

                Vector<String> surgeryDetailsVector = new Vector<String>();

                surgeryDetailsVector.add(surgeryDetailsResult.getString("surgery_id"));
                surgeryDetailsVector.add(surgeryDetailsResult.getString("name"));
                surgeryDetailsVector.add(surgeryDetailsResult.getString("description"));
                surgeryDetailsVector.add(surgeryDetailsResult.getString("amount"));

                if (Login.userRole.equals("Admin") || Login.userRole.equals("Pharmacist")) {
                    calculateTotal(surgeryDetailsResult.getDouble("amount"));
                }

                tableModel.addRow(surgeryDetailsVector);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadLaboratoryTestDetails(String medicalID) {

        try {

            ResultSet laboratoryTestResult = MySQL.execute("SELECT * FROM `laboratory_test` WHERE `medical_history_id`='" + medicalID + "'");

            DefaultTableModel tableModel = (DefaultTableModel) laboratoryTestDetailsTable.getModel();

            while (laboratoryTestResult.next()) {

                Vector<String> laboratoryTestVector = new Vector<String>();

                laboratoryTestVector.add(laboratoryTestResult.getString("laboratory_test_id"));
                laboratoryTestVector.add(laboratoryTestResult.getString("name"));
                laboratoryTestVector.add(laboratoryTestResult.getString("description"));
                laboratoryTestVector.add(laboratoryTestResult.getString("amount"));

                if (Login.userRole.equals("Admin") || Login.userRole.equals("Pharmacist")) {
                    calculateTotal(laboratoryTestResult.getDouble("amount"));
                }

                tableModel.addRow(laboratoryTestVector);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadPatientMedicines(String addedDate) {

        try {

            ResultSet patientMedicinesResult = MySQL.execute("SELECT * FROM `patient_has_medicines` "
                    + "INNER JOIN `medicine` ON `patient_has_medicines`.`medicine_medicine_id`=`medicine`.`medicine_id` "
                    + "WHERE `patient_mobile`='" + patientMobile + "' AND `added_date`='" + addedDate + "'");

            DefaultTableModel tableModel = (DefaultTableModel) medicinesTable.getModel();
            tableModel.setRowCount(0);

            while (patientMedicinesResult.next()) {

                Vector<String> patientMedicinesVector = new Vector<String>();

                patientMedicinesVector.add(patientMedicinesResult.getString("medicine.name"));
                patientMedicinesVector.add(patientMedicinesResult.getString("unit price"));
                patientMedicinesVector.add(patientMedicinesResult.getString("quantity"));

                if (Login.userRole.equals("Admin") || Login.userRole.equals("Pharmacist")) {
                    calculateTotal(patientMedicinesResult.getDouble("unit price") * patientMedicinesResult.getInt("quantity"));
                }

                tableModel.addRow(patientMedicinesVector);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupPaidAmountField() {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(true);

        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(0);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setCommitsOnValidEdit(true);

        paidAmountTextField.setFormatterFactory(
                new javax.swing.text.DefaultFormatterFactory(numberFormatter)
        );
    }

    private void calculateTotal(double amount) {
        totalAmount += amount;

        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        totalLabel.setText(decimalFormat.format(totalAmount));
        proceedPaymentButton.setEnabled(true);
        payConsultationButton.setEnabled(true);

    }

    private void calculateBalance() {

        try {
            String paidAmountText = paidAmountTextField.getText().replace(",", "").trim();
            if (!paidAmountText.isEmpty()) {
                paidAmount = Double.parseDouble(paidAmountText);
            }
        } catch (NumberFormatException e) {
            paidAmount = 0.0;
        }

        double balance = paidAmount - totalAmount;

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(true);
        balanceLabel.setText(numberFormat.format(balance));

        if (paidAmount < totalAmount) {
            balanceLabel.setForeground(new Color(252, 89, 83));
        } else {
            balanceLabel.setForeground(new Color(0, 255, 0));
        }
    }

    private void resetAmount() {

        consultationFee = 0;
        totalAmount = 0;
        calculateTotal(totalAmount);
        proceedPaymentButton.setEnabled(false);
        payConsultationButton.setEnabled(false);

    }

    public void clear() {

        DefaultTableModel medicalDetailsTableModel = (DefaultTableModel) medicalDetailsTable.getModel();
        medicalDetailsTableModel.setRowCount(0);

        DefaultTableModel surgeryDetailsTableModel = (DefaultTableModel) surgeryDetailsTable.getModel();
        surgeryDetailsTableModel.setRowCount(0);

        DefaultTableModel laboratoryTestDetailsTableModel = (DefaultTableModel) laboratoryTestDetailsTable.getModel();
        laboratoryTestDetailsTableModel.setRowCount(0);

        DefaultTableModel medicinesTableModel = (DefaultTableModel) medicinesTable.getModel();
        medicinesTableModel.setRowCount(0);

        resetAmount();

        medicalDateChooser.setEnabled(false);
        patientNameLabel.setText("None");

        patientMobile = null;
        addedDate = null;
        paidAmount = 0.0;
        paidAmountTextField.setValue(null);
        balanceLabel.setText("0");
        consultationFeeLabel.setText("0.0");
        consultationCount = 0;
        FlatLaf.updateUI();

    }

    private PaymentProcessor getSelectedProcessor() {
        ButtonModel selected = paymentButtonGroup.getSelection();
        if (selected == null) {
            return new DirectPaymentProcessor();
        }
        String method = selected.getActionCommand();
        if ("3".equals(method)) {
            return new InsuranceClaimProcessor();
        }
        if ("2".equals(method)) {
            return new CardPaymentProcessor();
        }
        return new DirectPaymentProcessor();
    }

    private void handleConsultationPayment(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String consultationFee, String paid, String balance, String PaymentMethod) {
        PaymentProcessor processor = getSelectedProcessor();
        BillService consultation = new ConsultationService(processor);
        consultation.processPayment(billNumber, patientMobile, addedDate, itemName, unitPrice, quantity, consultationFee, paid, balance, PaymentMethod);
    }

    private void handleSurgeryPayment(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String total, String paid, String balance, String PaymentMethod) {
        PaymentProcessor processor = getSelectedProcessor();
        BillService treatment = new SurgeryService(processor);
        treatment.processPayment(billNumber, patientMobile, addedDate, itemName, unitPrice, quantity, total, paid, balance, PaymentMethod);
    }

    private void handleLaboratoryTestPayment(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String total, String paid, String balance, String PaymentMethod) {
        PaymentProcessor processor = getSelectedProcessor();
        BillService treatment = new LaboratoryTestService(processor);
        treatment.processPayment(billNumber, patientMobile, addedDate, itemName, unitPrice, quantity, total, paid, balance, PaymentMethod);
    }

    private void handleMedicationPayment(String billNumber, String patientMobile, String addedDate, String itemName, String unitPrice, String quantity, String total, String paid, String balance, String PaymentMethod) {
        PaymentProcessor processor = getSelectedProcessor();
        BillService meds = new MedicationService(processor);
        meds.processPayment(billNumber, patientMobile, addedDate, itemName, unitPrice, quantity, total, paid, balance, PaymentMethod);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paymentButtonGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        selectPatientButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        medicinesTable = new javax.swing.JTable();
        selectMedicineButton = new javax.swing.JButton();
        proceedPaymentButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        balanceLabel = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        surgeryDetailsTable = new javax.swing.JTable();
        patientNameLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        medicalDetailsTable = new javax.swing.JTable();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        insuranceRadioButton = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        laboratoryTestDetailsTable = new javax.swing.JTable();
        medicalDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        paidAmountTextField = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        consultationFeeLabel = new javax.swing.JLabel();
        payConsultationButton = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Billing and Insurance Claims");

        selectPatientButton.setBackground(java.awt.SystemColor.textHighlight);
        selectPatientButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        selectPatientButton.setText("Select Patient");
        selectPatientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPatientButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel2.setText(" Patient Name :");

        medicinesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Unit Price", "Quantity "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(medicinesTable);

        selectMedicineButton.setBackground(java.awt.SystemColor.textHighlight);
        selectMedicineButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        selectMedicineButton.setText("Select Medicines");
        selectMedicineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectMedicineButtonActionPerformed(evt);
            }
        });

        proceedPaymentButton.setBackground(new java.awt.Color(67, 144, 77));
        proceedPaymentButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        proceedPaymentButton.setText("Proceed Payment");
        proceedPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proceedPaymentButtonActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Total (LKR) :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel6.setText("Paid Amount (LKR) :");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel7.setText("Balance (LKR) :");

        balanceLabel.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        balanceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        balanceLabel.setText("0");

        totalLabel.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        totalLabel.setForeground(new java.awt.Color(102, 102, 102));
        totalLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalLabel.setText("0");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText(" Medicines");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText(" Medical Details");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText(" Payment Methods");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText(" Surgery Details");

        surgeryDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Description", "Amount (LKR)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(surgeryDetailsTable);
        if (surgeryDetailsTable.getColumnModel().getColumnCount() > 0) {
            surgeryDetailsTable.getColumnModel().getColumn(0).setMinWidth(100);
            surgeryDetailsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            surgeryDetailsTable.getColumnModel().getColumn(0).setMaxWidth(100);
            surgeryDetailsTable.getColumnModel().getColumn(1).setMinWidth(300);
            surgeryDetailsTable.getColumnModel().getColumn(1).setPreferredWidth(300);
            surgeryDetailsTable.getColumnModel().getColumn(1).setMaxWidth(300);
            surgeryDetailsTable.getColumnModel().getColumn(3).setMinWidth(250);
            surgeryDetailsTable.getColumnModel().getColumn(3).setPreferredWidth(250);
            surgeryDetailsTable.getColumnModel().getColumn(3).setMaxWidth(250);
        }

        patientNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        patientNameLabel.setText("None");

        medicalDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Illnesses", "Surgeries ", "Laboratory Tests", "Medicines"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(medicalDetailsTable);
        if (medicalDetailsTable.getColumnModel().getColumnCount() > 0) {
            medicalDetailsTable.getColumnModel().getColumn(2).setHeaderValue("Surgeries ");
            medicalDetailsTable.getColumnModel().getColumn(3).setHeaderValue("Laboratory Tests");
            medicalDetailsTable.getColumnModel().getColumn(4).setHeaderValue("Medicines");
        }

        paymentButtonGroup.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Cash");
        jRadioButton1.setActionCommand("1");

        paymentButtonGroup.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jRadioButton2.setText("Credit/Debit Card");
        jRadioButton2.setActionCommand("2");

        paymentButtonGroup.add(insuranceRadioButton);
        insuranceRadioButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        insuranceRadioButton.setText("Insurance Claims");
        insuranceRadioButton.setActionCommand("3");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText(" Laboratory Tests Details");

        laboratoryTestDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Description", "Amount (LKR)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(laboratoryTestDetailsTable);
        if (laboratoryTestDetailsTable.getColumnModel().getColumnCount() > 0) {
            laboratoryTestDetailsTable.getColumnModel().getColumn(0).setMinWidth(100);
            laboratoryTestDetailsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            laboratoryTestDetailsTable.getColumnModel().getColumn(0).setMaxWidth(100);
            laboratoryTestDetailsTable.getColumnModel().getColumn(1).setMinWidth(300);
            laboratoryTestDetailsTable.getColumnModel().getColumn(1).setPreferredWidth(300);
            laboratoryTestDetailsTable.getColumnModel().getColumn(1).setMaxWidth(300);
            laboratoryTestDetailsTable.getColumnModel().getColumn(3).setMinWidth(250);
            laboratoryTestDetailsTable.getColumnModel().getColumn(3).setPreferredWidth(250);
            laboratoryTestDetailsTable.getColumnModel().getColumn(3).setMaxWidth(250);
        }

        medicalDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                medicalDateChooserPropertyChange(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setText("  Date :");

        paidAmountTextField.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        paidAmountTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paidAmountTextFieldKeyReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText(" Consultation Charges");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText(": LKR");

        consultationFeeLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        consultationFeeLabel.setText("0.0");

        payConsultationButton.setBackground(new java.awt.Color(67, 144, 77));
        payConsultationButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        payConsultationButton.setText("Pay Consultation Charges");
        payConsultationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payConsultationButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel11)
                            .addComponent(jLabel1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(patientNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(selectPatientButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(medicalDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jRadioButton1)
                                .addGap(27, 27, 27)
                                .addComponent(jRadioButton2)
                                .addGap(27, 27, 27)
                                .addComponent(insuranceRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 265, Short.MAX_VALUE)
                                .addComponent(payConsultationButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(proceedPaymentButton))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(selectMedicineButton))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(consultationFeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel5))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jLabel7)))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(balanceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(totalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                    .addComponent(paidAmountTextField)))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(15, 15, 15))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(patientNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(medicalDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(selectPatientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))))
                .addGap(24, 24, 24)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(selectMedicineButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel5)
                                            .addComponent(totalLabel))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(paidAmountTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(balanceLabel))
                                        .addGap(15, 15, 15))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(proceedPaymentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jRadioButton1)
                                        .addComponent(jRadioButton2)
                                        .addComponent(insuranceRadioButton))
                                    .addComponent(payConsultationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(13, 13, 13))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(consultationFeeLabel)
                                .addGap(123, 123, 123))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16)
                        .addGap(123, 123, 123))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectPatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPatientButtonActionPerformed

        JDialog medicalDialog = new SelectPatient(Main.getMain(), true, "B", this, new AppointmentScheduling(new AppointmentMediatorService()), new Reports());
        medicalDialog.setLocationRelativeTo(this);
        medicalDialog.setVisible(true);

    }//GEN-LAST:event_selectPatientButtonActionPerformed

    private void selectMedicineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectMedicineButtonActionPerformed

        SelectMedicines selectMedicines = new SelectMedicines(Main.getMain(), true, this);
        selectMedicines.setLocationRelativeTo(this);
        selectMedicines.setVisible(true);

    }//GEN-LAST:event_selectMedicineButtonActionPerformed

    private void medicalDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_medicalDateChooserPropertyChange

        Date date = medicalDateChooser.getDate();

        if (date != null) {
            loadMedicalDetails(date);
        }


    }//GEN-LAST:event_medicalDateChooserPropertyChange

    private void paidAmountTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidAmountTextFieldKeyReleased
        calculateBalance();
    }//GEN-LAST:event_paidAmountTextFieldKeyReleased

    private void proceedPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proceedPaymentButtonActionPerformed

        if (totalAmount == 0) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_CENTER, "This payment cannot be processed..");
        } else if (paidAmount == 0.0) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_CENTER, "The paid amount cannot be empty");
        } else if (paidAmount < totalAmount) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_CENTER, "The paid amount cannot be less than the total amount.");
        } else {

            try {
                ResultSet lastID = MySQL.execute("SELECT `bill_number` FROM `bill` ORDER BY `bill_number` DESC LIMIT 1;");

                if (lastID.next()) {
                    String lastId = lastID.getString("bill_number");
                    int num = Integer.parseInt(lastId.substring(2));
                    num++;
                    newBillNumber = String.format("BL%04d", num);
                }

                DecimalFormat decimalFormat = new DecimalFormat("0");

                String total = decimalFormat.format(totalAmount);
                String paid = decimalFormat.format(paidAmount);
                String balance = balanceLabel.getText().replace(",", "");
                String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                ButtonModel selectePaymentMethod = paymentButtonGroup.getSelection();
                String paymentMethod = selectePaymentMethod.getActionCommand();

                DefaultTableModel surgeryDetailsTableModel = (DefaultTableModel) surgeryDetailsTable.getModel();
                if (surgeryDetailsTableModel.getRowCount() != 0) {
                    for (int row = 0; row < surgeryDetailsTableModel.getRowCount(); row++) {

                        ResultSet billResultSet = MySQL.execute("SELECT `patient_mobile`,`added_date`,`item_name`FROM `bill` "
                                + "WHERE `patient_mobile`='" + patientMobile + "' AND `added_date`='" + addedDate + "' "
                                + "AND `item_name`='" + String.valueOf(surgeryDetailsTableModel.getValueAt(row, 1)) + " (Surgery)'");

                        if (billResultSet.next() && billResultSet.getInt(1) > 0) {
                            alreadypaid = true;
                        } else {
                            alreadypaid = false;

                            String Name = String.valueOf(surgeryDetailsTableModel.getValueAt(row, 1));
                            String unitPrice = String.valueOf(surgeryDetailsTableModel.getValueAt(row, 3));

                            if (unitPrice.endsWith(".0")) {
                                unitPrice = unitPrice.substring(0, unitPrice.length() - 2);
                            }

                            handleSurgeryPayment(newBillNumber, patientMobile, addedDate, Name, unitPrice, "1", total, paid, balance, paymentMethod);
                        }

                    }
                }

                DefaultTableModel laboratoryTestDetailsTableModel = (DefaultTableModel) laboratoryTestDetailsTable.getModel();
                if (laboratoryTestDetailsTableModel.getRowCount() != 0) {
                    for (int row = 0; row < laboratoryTestDetailsTableModel.getRowCount(); row++) {

                        ResultSet billResultSet = MySQL.execute("SELECT `patient_mobile`,`added_date`,`item_name` FROM `bill` "
                                + "WHERE `patient_mobile`='" + patientMobile + "' AND `added_date`='" + addedDate + "' "
                                + "AND `item_name`='" + String.valueOf(laboratoryTestDetailsTableModel.getValueAt(row, 1)) + " (Laboratory Test)'");

                        if (billResultSet.next() && billResultSet.getInt(1) > 0) {
                            alreadypaid = true;
                        } else {
                            alreadypaid = false;

                            String Name = String.valueOf(laboratoryTestDetailsTableModel.getValueAt(row, 1));
                            String unitPrice = String.valueOf(laboratoryTestDetailsTableModel.getValueAt(row, 3));

                            if (unitPrice.endsWith(".0")) {
                                unitPrice = unitPrice.substring(0, unitPrice.length() - 2);
                            }

                            handleLaboratoryTestPayment(newBillNumber, patientMobile, addedDate, Name, unitPrice, "1", total, paid, balance, paymentMethod);

                        }

                    }
                }

                DefaultTableModel medicinesTableModel = (DefaultTableModel) medicinesTable.getModel();
                if (medicinesTableModel.getRowCount() != 0) {
                    for (int row = 0; row < medicinesTableModel.getRowCount(); row++) {
                        ResultSet billResultSet = MySQL.execute("SELECT `patient_mobile`,`added_date`,`item_name` FROM `bill` "
                                + "WHERE `patient_mobile`='" + patientMobile + "' AND `added_date`='" + addedDate + "' "
                                + "AND `item_name`='" + String.valueOf(medicinesTableModel.getValueAt(row, 0)) + " (Medicines)'");

                        if (billResultSet.next() && billResultSet.getInt(1) > 0) {
                            alreadypaid = true;
                        } else {
                            alreadypaid = false;

                            String Name = String.valueOf(medicinesTableModel.getValueAt(row, 0));
                            String unitPrice = String.valueOf(medicinesTableModel.getValueAt(row, 1));

                            if (unitPrice.endsWith(".0")) {
                                unitPrice = unitPrice.substring(0, unitPrice.length() - 2);
                            }

                            String quantity = String.valueOf(medicinesTableModel.getValueAt(row, 2));

                            handleMedicationPayment(newBillNumber, patientMobile, addedDate, Name, unitPrice, quantity, total, paid, balance, paymentMethod);

                        }
                    }
                }

                if (alreadypaid) {
                    JOptionPane.showMessageDialog(this, "This payment has already been paid.", "Payment Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    ViewPaymentDetails viewPaymentDetails = new ViewPaymentDetails(Main.getMain(), false, newBillNumber);
                    viewPaymentDetails.setVisible(true);
                }

                clear();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_proceedPaymentButtonActionPerformed

    private void payConsultationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payConsultationButtonActionPerformed

        if (totalAmount == 0) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_CENTER, "This payment cannot be processed..");
        } else if (paidAmount == 0.0) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_CENTER, "The paid amount cannot be empty");
        } else if (paidAmount < totalAmount) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_CENTER, "The paid amount cannot be less than the total amount.");
        } else {

            try {
                ResultSet lastID = MySQL.execute("SELECT `bill_number` FROM `bill` ORDER BY `bill_number` DESC LIMIT 1;");

                if (lastID.next()) {
                    String lastId = lastID.getString("bill_number");
                    int num = Integer.parseInt(lastId.substring(2));
                    num++;
                    newBillNumber = String.format("BL%04d", num);
                }

                String addedDate = this.addedDate;

                if (medicalDateChooser.getDate() != null) {
                    addedDate = new SimpleDateFormat("yyyy-MM-dd").format(medicalDateChooser.getDate());
                }

                DecimalFormat decimalFormat = new DecimalFormat("0");

                String total = decimalFormat.format(totalAmount);
                String paid = decimalFormat.format(paidAmount);
                String balance = balanceLabel.getText().replace(",", "");
                String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                ButtonModel selectePaymentMethod = paymentButtonGroup.getSelection();
                String paymentMethod = selectePaymentMethod.getActionCommand();

                handleConsultationPayment(newBillNumber, patientMobile, addedDate, "Charges", "2000", String.valueOf(consultationCount), String.valueOf(consultationFee), paid, balance, paymentMethod);

//                MySQL.execute("INSERT INTO `bill` "
//                        + "(`bill_number`,`patient_mobile`,`added_date`,`item_name`,`unit_price`,`quantity`,`total`,`paid_amount`,`balance`,`payment_type_id`,`payment_date`) "
//                        + "VALUES ('" + newBillNumber + "','" + patientMobile + "','" + addedDate + "','Charges (Consultation)','2000','" + consultationCount + "','" + total + "','" + paid + "','" + balance + "','" + paymentMethod + "','" + date + "')"
//                );

                ViewConsultationPayment consultationPayment = new ViewConsultationPayment(Main.getMain(), true, newBillNumber, consultationCount);
                consultationPayment.setVisible(true);
                clear();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_payConsultationButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel balanceLabel;
    private javax.swing.JLabel consultationFeeLabel;
    private javax.swing.JRadioButton insuranceRadioButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable laboratoryTestDetailsTable;
    private com.toedter.calendar.JDateChooser medicalDateChooser;
    private javax.swing.JTable medicalDetailsTable;
    private javax.swing.JTable medicinesTable;
    private javax.swing.JFormattedTextField paidAmountTextField;
    private javax.swing.JLabel patientNameLabel;
    private javax.swing.JButton payConsultationButton;
    private javax.swing.ButtonGroup paymentButtonGroup;
    private javax.swing.JButton proceedPaymentButton;
    private javax.swing.JButton selectMedicineButton;
    private javax.swing.JButton selectPatientButton;
    private javax.swing.JTable surgeryDetailsTable;
    private javax.swing.JLabel totalLabel;
    // End of variables declaration//GEN-END:variables
}
