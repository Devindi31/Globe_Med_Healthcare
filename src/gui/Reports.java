package gui;

import bean.MedicalHistoryReportDetails;
import bean.TreatmentReportDetails;
import com.formdev.flatlaf.FlatLaf;
import connection.MySQL;
import dialogs.SelectPatient;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import permission.RoleRepository;

public class Reports extends javax.swing.JPanel {

    private final HashMap<String, String> paymentMethodMap;
    private double totalIncome = 0.0;
    private double labtestAmount = 0.0;
    DecimalFormat decimalFormat;

    public Reports() {
        initComponents();
        defaultPermission();
        setupPermissions();
        this.paymentMethodMap = new HashMap<>();
        decimalFormat = new DecimalFormat("#,##0.00");
        loadType();
        loadPaymentMethod();
        ((JTextField) fromDateChooser.getDateEditor().getUiComponent()).setEditable(false);
        ((JTextField) toDateChooser.getDateEditor().getUiComponent()).setEditable(false);
        loadFinancialReport();

    }

    private void defaultPermission() {
        jTabbedPane1.removeAll();
        printTreatmentSummariesButton.setVisible(false);
        printDiagnosticButton.setVisible(false);
    }

    private void setupPermissions() {
        if (RoleRepository.hasPermission(Login.userRole, "report_treatments_panel")) {
            jTabbedPane1.add("Patient Treatment Summaries", treatmentPanel);
        }
        if (RoleRepository.hasPermission(Login.userRole, "report_diangostic_panel")) {
            jTabbedPane1.add("Diagnostic Reports", diagnosticPanel);
        }
        if (RoleRepository.hasPermission(Login.userRole, "report_financial_panel")) {
            jTabbedPane1.add("Financial Reports", financialPanel);
        }
        if (RoleRepository.hasPermission(Login.userRole, "report_treatments_print_button")) {
            printTreatmentSummariesButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "report_diangostic_print_button")) {
            printDiagnosticButton.setVisible(true);
        }
    }

    interface ReportElement {

        void accept(ReportVisitor visitor);
    }

    interface ReportVisitor {

        void visit(TreatmentSummaryReport report);

        void visit(DiagnosticReport report);

        void visit(FinancialReport report);
    }

    class TreatmentSummaryReport implements ReportElement {

        @Override
        public void accept(ReportVisitor visitor) {
            visitor.visit(this);
        }
    }

    class DiagnosticReport implements ReportElement {

        @Override
        public void accept(ReportVisitor visitor) {
            visitor.visit(this);
        }
    }

    class FinancialReport implements ReportElement {

        @Override
        public void accept(ReportVisitor visitor) {
            visitor.visit(this);
        }
    }

    class TreatmentSummaryVisitor implements ReportVisitor {

        private final Reports reports;

        public TreatmentSummaryVisitor(Reports reports) {
            this.reports = reports;
        }

        @Override
        public void visit(TreatmentSummaryReport report) {
            reports.printTreatmentSummaries();
        }

        @Override
        public void visit(DiagnosticReport report) {
        }

        @Override
        public void visit(FinancialReport report) {
        }
    }

    class DiagnosticVisitor implements ReportVisitor {

        private final Reports reports;

        public DiagnosticVisitor(Reports reports) {
            this.reports = reports;
        }

        @Override
        public void visit(DiagnosticReport report) {
            reports.printDiagnostic();
        }

        @Override
        public void visit(TreatmentSummaryReport report) {
        }

        @Override
        public void visit(FinancialReport report) {
        }
    }

    class FinancialVisitor implements ReportVisitor {

        private final Reports reports;

        public FinancialVisitor(Reports reports) {
            this.reports = reports;
        }

        @Override
        public void visit(FinancialReport report) {
            reports.printFinancial();
        }

        @Override
        public void visit(TreatmentSummaryReport report) {
        }

        @Override
        public void visit(DiagnosticReport report) {
        }
    }

    public void printTreatmentSummaries() {
        try {

            HashMap<String, Object> parameters = new HashMap<>();
            String filePath = "src//reports/treatment_summaries.jasper";

            List<MedicalHistoryReportDetails> medicalHistoryList = new ArrayList<>();
            List<TreatmentReportDetails> treatmentReportList = new ArrayList<>();

            DefaultTableModel medicalHistoryTableModel = (DefaultTableModel) medicalHistoryTable.getModel();
            DefaultTableModel treatmentTableModel = (DefaultTableModel) treatmentPlanTable.getModel();

            for (int i = 0; i < medicalHistoryTableModel.getRowCount(); i++) {

                String medicalID = String.valueOf(medicalHistoryTable.getValueAt(i, 0));
                String medicalDate = String.valueOf(medicalHistoryTable.getValueAt(i, 1));
                String Illness = String.valueOf(medicalHistoryTable.getValueAt(i, 2));
                String surgery = String.valueOf(medicalHistoryTable.getValueAt(i, 3));
                String allergy = String.valueOf(medicalHistoryTable.getValueAt(i, 4));
                String labTest = String.valueOf(medicalHistoryTable.getValueAt(i, 5));
                String medicines = String.valueOf(medicalHistoryTable.getValueAt(i, 6));

                medicalHistoryList.add(new MedicalHistoryReportDetails(medicalID, medicalDate, Illness, surgery, allergy, labTest, medicines));

            }

            for (int t = 0; t < treatmentTableModel.getRowCount(); t++) {

                String addedDate = String.valueOf(treatmentPlanTable.getValueAt(t, 0));
                String Illness = String.valueOf(treatmentPlanTable.getValueAt(t, 1));
                String medications = String.valueOf(treatmentPlanTable.getValueAt(t, 2));
                String therapies = String.valueOf(treatmentPlanTable.getValueAt(t, 3));
                String nextSchedule = String.valueOf(treatmentPlanTable.getValueAt(t, 4));

                treatmentReportList.add(new TreatmentReportDetails(addedDate, Illness, medications, therapies, nextSchedule));

            }

            JRBeanCollectionDataSource medicalDS = new JRBeanCollectionDataSource(medicalHistoryList);
            JRBeanCollectionDataSource treatmentDS = new JRBeanCollectionDataSource(treatmentReportList);

            parameters.put("patient_name", patientNameLabel.getText());
            parameters.put("patient_mobile", patientMobileLabel.getText());
            parameters.put("TreatmentPlanDS", treatmentDS);
            parameters.put("MedicalHistoryDS", medicalDS);

            JasperPrint report = JasperFillManager.fillReport(filePath, parameters, new JREmptyDataSource());
            JasperViewer.viewReport(report, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printDiagnostic() {
        try {

            HashMap<String, Object> parameters = new HashMap<>();
            String filePath = "src//reports/diagnostic_report.jasper";

            JRDataSource dataSource = new JRTableModelDataSource(labTestSummaryTable.getModel());

            parameters.put("patient_name", dPatientNameLabel.getText());
            parameters.put("patient_mobile", dPatientMobileLabel.getText());
            parameters.put("total_amount", decimalFormat.format(labtestAmount));

            JasperPrint report = JasperFillManager.fillReport(filePath, parameters, dataSource);
            JasperViewer.viewReport(report, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFinancial() {
        try {

            HashMap<String, Object> parameters = new HashMap<>();
            String filePath = "src//reports/financial_report.jasper";

            JRDataSource dataSource = new JRTableModelDataSource(incomeSummaryTable.getModel());

            String incomeType = String.valueOf(typeComboBox.getSelectedItem());
            String paymentMethod = String.valueOf(paymentMethodComboBox.getSelectedItem());
            String fromDate = "-";
            String toDate = "-";

            Date fD = fromDateChooser.getDate();
            Date tD = toDateChooser.getDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd");

            if (fD != null) {
                fromDate = simpleDateFormat.format(fD);
            }
            if (tD != null) {
                toDate = simpleDateFormat.format(tD);
            }

            if (incomeType.equals("Select Type")) {
                parameters.put("income_type", "All Types");
            } else {
                parameters.put("income_type", incomeType);
            }

            if (paymentMethod.equals("Select Payment Method")) {
                parameters.put("payment_method", "All Methods");
            } else {
                parameters.put("payment_method", paymentMethod);
            }

            parameters.put("from_date", fromDate);
            parameters.put("to_date", toDate);
            parameters.put("total_amount", totalIncomeLabel.getText());

            JasperPrint report = JasperFillManager.fillReport(filePath, parameters, dataSource);
            JasperViewer.viewReport(report, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPatientName(String patientName) {
        this.patientNameLabel.setText(patientName);
    }

    public void setPatientMobile(String patientMobile) {
        this.patientMobileLabel.setText(patientMobile);
    }

    public void setdPatientName(String patientName) {
        this.dPatientNameLabel.setText(patientName);
    }

    public void setdPatientMobile(String patientMobile) {
        this.dPatientMobileLabel.setText(patientMobile);
    }

    public void loadMedicalHistory(String patientMobile) {

        try {

            ResultSet medicalHistoryResult = MySQL.execute("SELECT * FROM `medical_history`"
                    + " INNER JOIN `patient` ON `patient`.`mobile`=`medical_history`.`patient_mobile` "
                    + "WHERE  `patient_mobile`= '" + patientMobile + "' ORDER BY `added_date` DESC");

            DefaultTableModel tableModel = (DefaultTableModel) medicalHistoryTable.getModel();
            tableModel.setRowCount(0);

            DefaultTableModel treatmentPlanTableModel = (DefaultTableModel) treatmentPlanTable.getModel();
            tableModel.setRowCount(0);

            while (medicalHistoryResult.next()) {

                Vector<String> medicalHistoryVector = new Vector<String>();

                medicalHistoryVector.add(medicalHistoryResult.getString("medical_history_id"));
                medicalHistoryVector.add(medicalHistoryResult.getString("added_date"));
                medicalHistoryVector.add(medicalHistoryResult.getString("illnesses"));
                medicalHistoryVector.add(medicalHistoryResult.getString("surgeries"));
                medicalHistoryVector.add(medicalHistoryResult.getString("allergies"));
                medicalHistoryVector.add(medicalHistoryResult.getString("laboratory_test"));
                medicalHistoryVector.add(medicalHistoryResult.getString("medicines"));

                loadTreatmentPlan(medicalHistoryResult.getString("medical_history_id"), treatmentPlanTableModel);
                tableModel.addRow(medicalHistoryVector);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadTreatmentPlan(String medicalID, DefaultTableModel tableModel) {

        try {

            ResultSet treatmentPlanResult = MySQL.execute("SELECT `treatment_plans`.`added_date`, `medical_history`.`illnesses`,`treatment_plans`.`procedures_or_therapies`,`treatment_plans`.`medications`,`next_schedule` FROM `treatment_plans` "
                    + "INNER JOIN `medical_history` ON `medical_history`.`medical_history_id`=`treatment_plans`.`medical_history_medical_history_id` "
                    + "WHERE `treatment_plans`.`medical_history_medical_history_id`='" + medicalID + "' ORDER BY `treatment_plans`.`added_date` DESC");

            while (treatmentPlanResult.next()) {

                Vector<String> treatmentPlanVector = new Vector<String>();

                treatmentPlanVector.add(treatmentPlanResult.getString("added_date"));
                treatmentPlanVector.add(treatmentPlanResult.getString("medical_history.illnesses"));
                treatmentPlanVector.add(treatmentPlanResult.getString("treatment_plans.medications"));
                treatmentPlanVector.add(treatmentPlanResult.getString("treatment_plans.procedures_or_therapies"));
                treatmentPlanVector.add(treatmentPlanResult.getString("next_schedule"));

                tableModel.addRow(treatmentPlanVector);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clearTreatmentSummary() {

        DefaultTableModel treatmentPlanTableModel = (DefaultTableModel) treatmentPlanTable.getModel();
        treatmentPlanTableModel.setRowCount(0);

        DefaultTableModel medicalHistoryTableModel = (DefaultTableModel) medicalHistoryTable.getModel();
        medicalHistoryTableModel.setRowCount(0);

        patientNameLabel.setText("None");
        patientMobileLabel.setText("None");

        FlatLaf.updateUI();

    }

    public void loadLabTestSummary(String patientMobile) {

        try {

            ResultSet medicalHistoryResult = MySQL.execute("SELECT `medical_history_id` FROM `medical_history`"
                    + " INNER JOIN `patient` ON `patient`.`mobile`=`medical_history`.`patient_mobile` "
                    + "WHERE  `patient_mobile`= '" + patientMobile + "' ORDER BY `added_date` DESC");

            DefaultTableModel tableModel = (DefaultTableModel) labTestSummaryTable.getModel();
            tableModel.setRowCount(0);
            labtestAmount = 0.0;

            while (medicalHistoryResult.next()) {

                ResultSet labTestResult = MySQL.execute("SELECT * FROM `laboratory_test` WHERE `medical_history_id`='" + medicalHistoryResult.getString("medical_history_id") + "'");
                while (labTestResult.next()) {

                    Vector<String> labTestVector = new Vector<String>();

                    labTestVector.add(labTestResult.getString("medical_history_id"));
                    labTestVector.add(labTestResult.getString("laboratory_test_id"));
                    labTestVector.add(labTestResult.getString("name"));
                    labTestVector.add(labTestResult.getString("description"));
                    labTestVector.add("Normal");
                    labTestVector.add(labTestResult.getString("amount"));

                    labtestAmount += labTestResult.getDouble("amount");

                    tableModel.addRow(labTestVector);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clearLabTestSummary() {

        DefaultTableModel tableModel = (DefaultTableModel) labTestSummaryTable.getModel();
        tableModel.setRowCount(0);

        dPatientNameLabel.setText("None");
        dPatientMobileLabel.setText("None");

        FlatLaf.updateUI();

    }

    private void loadType() {
        String[] types = new String[]{"Select Type", "Consultation", "Surgery", "Laboratory Test", "Medicines"};
        typeComboBox.setModel(new DefaultComboBoxModel<>(types));
    }

    private void loadPaymentMethod() {

        try {

            ResultSet paymentMethodResult = MySQL.execute("SELECT * FROM `payment_type`");

            Vector<String> doctorVector = new Vector<>();
            doctorVector.add("Select Payment Method");

            while (paymentMethodResult.next()) {

                doctorVector.add(paymentMethodResult.getString("type"));
                paymentMethodMap.put(paymentMethodResult.getString("type"), paymentMethodResult.getString("payment_type_id"));

            }

            paymentMethodComboBox.setModel(new DefaultComboBoxModel<>(doctorVector));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadFinancialReport() {

        String type = String.valueOf(typeComboBox.getSelectedItem());
        String paymentMethod = String.valueOf(paymentMethodComboBox.getSelectedItem());
        Date fromDate = fromDateChooser.getDate();
        Date toDate = toDateChooser.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String whereOrAnd = null;
        String query = "SELECT `bill_number`,`item_name`,`unit_price`,`quantity` FROM `bill`";

        if (!type.equals("Select Type")) {
            if (whereOrAnd == null) {
                whereOrAnd = " WHERE ";
            } else {
                whereOrAnd = " AND ";
            }

            query += whereOrAnd + "`item_name` LIKE '%(" + type + ")'";

        }

        if (!paymentMethod.equals("Select Payment Method")) {
            if (whereOrAnd == null) {
                whereOrAnd = " WHERE ";
            } else {
                whereOrAnd = " AND ";
            }
            query += whereOrAnd + "`payment_type_id`='" + paymentMethodMap.get(paymentMethod) + "'";
        }

        if (fromDate != null || toDate != null) {
            if (whereOrAnd == null) {
                whereOrAnd = " WHERE ";
            } else {
                whereOrAnd = " AND ";
            }

            if (fromDate != null && toDate != null) {
                query += whereOrAnd + "`payment_date` BETWEEN '" + simpleDateFormat.format(fromDate) + "' AND '" + simpleDateFormat.format(toDate) + "'";
            } else if (fromDate != null) {
                query += whereOrAnd + "`payment_date` >= '" + simpleDateFormat.format(fromDate) + "'";
            } else {
                query += whereOrAnd + "`payment_date` <= '" + simpleDateFormat.format(toDate) + "'";
            }

        }

        query += " ORDER BY `payment_date` DESC";

        try {
            ResultSet billResult = MySQL.execute(query);

            DefaultTableModel tableModel = (DefaultTableModel) incomeSummaryTable.getModel();
            tableModel.setRowCount(0);
            totalIncome = 0.0;

            while (billResult.next()) {

                Vector<String> billVector = new Vector<>();

                billVector.add(billResult.getString("bill_number"));
                billVector.add(billResult.getString("item_name"));
                billVector.add(billResult.getString("unit_price"));
                billVector.add(billResult.getString("quantity"));

                tableModel.addRow(billVector);
                calculateTotalIncome(billResult.getDouble("unit_price"), billResult.getInt("quantity"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void calculateTotalIncome(double unitPrice, int quantity) {

        totalIncome += unitPrice * quantity;
        totalIncomeLabel.setText(decimalFormat.format(totalIncome));

    }

    public void clearFinancialReport() {

        typeComboBox.setSelectedIndex(0);
        paymentMethodComboBox.setSelectedIndex(0);
        fromDateChooser.setDate(null);
        toDateChooser.setDate(null);

        loadFinancialReport();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        treatmentPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        medicalHistoryTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        treatmentPlanTable = new javax.swing.JTable();
        clearTreatmentSummaryButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        patientNameLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        patientMobileLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        printTreatmentSummariesButton = new javax.swing.JButton();
        selectPatientButton = new javax.swing.JButton();
        diagnosticPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        labTestSummaryTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        dPatientNameLabel = new javax.swing.JLabel();
        dPatientMobileLabel = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dSelectPatientButton = new javax.swing.JButton();
        clearDiagnosticButton = new javax.swing.JButton();
        printDiagnosticButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        financialPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        incomeSummaryTable = new javax.swing.JTable();
        printFinancialButton = new javax.swing.JButton();
        toDateChooser = new com.toedter.calendar.JDateChooser();
        fromDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        paymentMethodComboBox = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox<>();
        clearFinancialButton = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        totalIncomeLabel = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Reports");

        medicalHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Medical ID", "Medical Date", "Illness", "Surgeries", "Allergies", "Lab Test", "Medicines"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(medicalHistoryTable);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel2.setText(" Treatment Plan");

        treatmentPlanTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Added Date", "Illnesses", "Medications", "Procedures or Therapies", "Next Schedule"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(treatmentPlanTable);

        clearTreatmentSummaryButton.setBackground(new java.awt.Color(51, 51, 51));
        clearTreatmentSummaryButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        clearTreatmentSummaryButton.setText("Clear");
        clearTreatmentSummaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearTreatmentSummaryButtonActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText(" Patient Name");

        patientNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        patientNameLabel.setText("None");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText(" Mobile Number");

        patientMobileLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        patientMobileLabel.setText("None");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText(":");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText(":");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setText(" Medical History");

        printTreatmentSummariesButton.setBackground(java.awt.SystemColor.textHighlight);
        printTreatmentSummariesButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        printTreatmentSummariesButton.setText("Print Report");
        printTreatmentSummariesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printTreatmentSummariesButtonActionPerformed(evt);
            }
        });

        selectPatientButton.setBackground(new java.awt.Color(89, 89, 89));
        selectPatientButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        selectPatientButton.setText("Select Patient");
        selectPatientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPatientButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout treatmentPanelLayout = new javax.swing.GroupLayout(treatmentPanel);
        treatmentPanel.setLayout(treatmentPanelLayout);
        treatmentPanelLayout.setHorizontalGroup(
            treatmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(treatmentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(treatmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5)
                    .addGroup(treatmentPanelLayout.createSequentialGroup()
                        .addGroup(treatmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 620, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, treatmentPanelLayout.createSequentialGroup()
                        .addGroup(treatmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(treatmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(treatmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(patientNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(patientMobileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectPatientButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearTreatmentSummaryButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, treatmentPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(printTreatmentSummariesButton)))
                .addContainerGap())
        );
        treatmentPanelLayout.setVerticalGroup(
            treatmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(treatmentPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(treatmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(treatmentPanelLayout.createSequentialGroup()
                        .addGroup(treatmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(patientNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(treatmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(patientMobileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(treatmentPanelLayout.createSequentialGroup()
                        .addGroup(treatmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(clearTreatmentSummaryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(selectPatientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)))
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(printTreatmentSummariesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Patient Treatment Summaries", treatmentPanel);

        diagnosticPanel.setEnabled(false);

        labTestSummaryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Medical ID", "Lab Test ID", "Name", "Description", "Result", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(labTestSummaryTable);
        if (labTestSummaryTable.getColumnModel().getColumnCount() > 0) {
            labTestSummaryTable.getColumnModel().getColumn(0).setMinWidth(150);
            labTestSummaryTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            labTestSummaryTable.getColumnModel().getColumn(0).setMaxWidth(150);
            labTestSummaryTable.getColumnModel().getColumn(1).setMinWidth(150);
            labTestSummaryTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            labTestSummaryTable.getColumnModel().getColumn(1).setMaxWidth(150);
            labTestSummaryTable.getColumnModel().getColumn(2).setMinWidth(350);
            labTestSummaryTable.getColumnModel().getColumn(2).setPreferredWidth(350);
            labTestSummaryTable.getColumnModel().getColumn(2).setMaxWidth(350);
            labTestSummaryTable.getColumnModel().getColumn(4).setMinWidth(200);
            labTestSummaryTable.getColumnModel().getColumn(4).setPreferredWidth(200);
            labTestSummaryTable.getColumnModel().getColumn(4).setMaxWidth(200);
            labTestSummaryTable.getColumnModel().getColumn(5).setMinWidth(150);
            labTestSummaryTable.getColumnModel().getColumn(5).setPreferredWidth(150);
            labTestSummaryTable.getColumnModel().getColumn(5).setMaxWidth(150);
        }

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText(" Patient Name");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText(" Mobile Number");

        dPatientNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        dPatientNameLabel.setText("None");

        dPatientMobileLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        dPatientMobileLabel.setText("None");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText(":");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText(":");

        dSelectPatientButton.setBackground(new java.awt.Color(89, 89, 89));
        dSelectPatientButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dSelectPatientButton.setText("Select Patient");
        dSelectPatientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dSelectPatientButtonActionPerformed(evt);
            }
        });

        clearDiagnosticButton.setBackground(new java.awt.Color(51, 51, 51));
        clearDiagnosticButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        clearDiagnosticButton.setText("Clear");
        clearDiagnosticButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearDiagnosticButtonActionPerformed(evt);
            }
        });

        printDiagnosticButton.setBackground(java.awt.SystemColor.textHighlight);
        printDiagnosticButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        printDiagnosticButton.setText("Print Report");
        printDiagnosticButton.setPreferredSize(new java.awt.Dimension(116, 38));
        printDiagnosticButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printDiagnosticButtonActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel8.setText(" Lab Test Summary");

        javax.swing.GroupLayout diagnosticPanelLayout = new javax.swing.GroupLayout(diagnosticPanel);
        diagnosticPanel.setLayout(diagnosticPanelLayout);
        diagnosticPanelLayout.setHorizontalGroup(
            diagnosticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(diagnosticPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(diagnosticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE)
                    .addGroup(diagnosticPanelLayout.createSequentialGroup()
                        .addGroup(diagnosticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(diagnosticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(diagnosticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dPatientNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dPatientMobileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dSelectPatientButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearDiagnosticButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, diagnosticPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(printDiagnosticButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(diagnosticPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        diagnosticPanelLayout.setVerticalGroup(
            diagnosticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, diagnosticPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(diagnosticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(diagnosticPanelLayout.createSequentialGroup()
                        .addGroup(diagnosticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dPatientNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(diagnosticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dPatientMobileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(diagnosticPanelLayout.createSequentialGroup()
                        .addGroup(diagnosticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(clearDiagnosticButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dSelectPatientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)))
                .addGap(39, 39, 39)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(printDiagnosticButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Diagnostic Reports", diagnosticPanel);

        incomeSummaryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Bill Number", "Item", "Unit Price", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(incomeSummaryTable);
        if (incomeSummaryTable.getColumnModel().getColumnCount() > 0) {
            incomeSummaryTable.getColumnModel().getColumn(0).setMinWidth(200);
            incomeSummaryTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            incomeSummaryTable.getColumnModel().getColumn(0).setMaxWidth(200);
            incomeSummaryTable.getColumnModel().getColumn(2).setMinWidth(200);
            incomeSummaryTable.getColumnModel().getColumn(2).setPreferredWidth(200);
            incomeSummaryTable.getColumnModel().getColumn(2).setMaxWidth(200);
            incomeSummaryTable.getColumnModel().getColumn(3).setMinWidth(200);
            incomeSummaryTable.getColumnModel().getColumn(3).setPreferredWidth(200);
            incomeSummaryTable.getColumnModel().getColumn(3).setMaxWidth(200);
        }

        printFinancialButton.setBackground(java.awt.SystemColor.textHighlight);
        printFinancialButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        printFinancialButton.setText("Print Report");
        printFinancialButton.setPreferredSize(new java.awt.Dimension(116, 38));
        printFinancialButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printFinancialButtonActionPerformed(evt);
            }
        });

        toDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toDateChooserPropertyChange(evt);
            }
        });

        fromDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromDateChooserPropertyChange(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel13.setText(" Payment Method");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel14.setText(" To");

        paymentMethodComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        paymentMethodComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentMethodComboBoxActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel15.setText(" From");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel16.setText(" Type");

        typeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        typeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboBoxActionPerformed(evt);
            }
        });

        clearFinancialButton.setBackground(new java.awt.Color(51, 51, 51));
        clearFinancialButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        clearFinancialButton.setText("Clear");
        clearFinancialButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearFinancialButtonActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel17.setText(" Income Summary");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel18.setText(" Total Income :");

        totalIncomeLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalIncomeLabel.setText("0.0");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setText("LKR");

        javax.swing.GroupLayout financialPanelLayout = new javax.swing.GroupLayout(financialPanel);
        financialPanel.setLayout(financialPanelLayout);
        financialPanelLayout.setHorizontalGroup(
            financialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(financialPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(financialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, financialPanelLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalIncomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(printFinancialButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, financialPanelLayout.createSequentialGroup()
                        .addGroup(financialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(financialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(paymentMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(financialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fromDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(financialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(toDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearFinancialButton))
                    .addGroup(financialPanelLayout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        financialPanelLayout.setVerticalGroup(
            financialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(financialPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(financialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(financialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(clearFinancialButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fromDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(paymentMethodComboBox)
                    .addComponent(typeComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addGap(39, 39, 39)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(financialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printFinancialButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalIncomeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Financial Reports", financialPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectPatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPatientButtonActionPerformed

        JDialog medicalDialog = new SelectPatient(Main.getMain(), true, "RT", new BillingAndInsuranceClaims(), new AppointmentScheduling(new AppointmentMediatorService()), this);
        medicalDialog.setLocationRelativeTo(this);
        medicalDialog.setVisible(true);

    }//GEN-LAST:event_selectPatientButtonActionPerformed

    private void clearTreatmentSummaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearTreatmentSummaryButtonActionPerformed
        clearTreatmentSummary();
    }//GEN-LAST:event_clearTreatmentSummaryButtonActionPerformed

    private void dSelectPatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dSelectPatientButtonActionPerformed

        JDialog medicalDialog = new SelectPatient(Main.getMain(), true, "RD", new BillingAndInsuranceClaims(), new AppointmentScheduling(new AppointmentMediatorService()), this);
        medicalDialog.setLocationRelativeTo(this);
        medicalDialog.setVisible(true);

    }//GEN-LAST:event_dSelectPatientButtonActionPerformed

    private void clearDiagnosticButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearDiagnosticButtonActionPerformed
        clearLabTestSummary();
    }//GEN-LAST:event_clearDiagnosticButtonActionPerformed

    private void typeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeComboBoxActionPerformed
        loadFinancialReport();
    }//GEN-LAST:event_typeComboBoxActionPerformed

    private void paymentMethodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentMethodComboBoxActionPerformed
        loadFinancialReport();
    }//GEN-LAST:event_paymentMethodComboBoxActionPerformed

    private void fromDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromDateChooserPropertyChange
        loadFinancialReport();
    }//GEN-LAST:event_fromDateChooserPropertyChange

    private void toDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_toDateChooserPropertyChange
        loadFinancialReport();
    }//GEN-LAST:event_toDateChooserPropertyChange

    private void clearFinancialButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearFinancialButtonActionPerformed
        clearFinancialReport();
    }//GEN-LAST:event_clearFinancialButtonActionPerformed

    private void printDiagnosticButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printDiagnosticButtonActionPerformed
        ReportElement report = new DiagnosticReport();
        ReportVisitor visitor = new DiagnosticVisitor(this);
        report.accept(visitor);
    }//GEN-LAST:event_printDiagnosticButtonActionPerformed

    private void printFinancialButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printFinancialButtonActionPerformed
        ReportElement report = new FinancialReport();
        ReportVisitor visitor = new FinancialVisitor(this);
        report.accept(visitor);
    }//GEN-LAST:event_printFinancialButtonActionPerformed


    private void printTreatmentSummariesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printTreatmentSummariesButtonActionPerformed
        ReportElement report = new TreatmentSummaryReport();
        ReportVisitor visitor = new TreatmentSummaryVisitor(this);
        report.accept(visitor);
    }//GEN-LAST:event_printTreatmentSummariesButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearDiagnosticButton;
    private javax.swing.JButton clearFinancialButton;
    private javax.swing.JButton clearTreatmentSummaryButton;
    private javax.swing.JLabel dPatientMobileLabel;
    private javax.swing.JLabel dPatientNameLabel;
    private javax.swing.JButton dSelectPatientButton;
    private javax.swing.JPanel diagnosticPanel;
    private javax.swing.JPanel financialPanel;
    private com.toedter.calendar.JDateChooser fromDateChooser;
    private javax.swing.JTable incomeSummaryTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable labTestSummaryTable;
    private javax.swing.JTable medicalHistoryTable;
    private javax.swing.JLabel patientMobileLabel;
    private javax.swing.JLabel patientNameLabel;
    private javax.swing.JComboBox<String> paymentMethodComboBox;
    private javax.swing.JButton printDiagnosticButton;
    private javax.swing.JButton printFinancialButton;
    private javax.swing.JButton printTreatmentSummariesButton;
    private javax.swing.JButton selectPatientButton;
    private com.toedter.calendar.JDateChooser toDateChooser;
    private javax.swing.JLabel totalIncomeLabel;
    private javax.swing.JPanel treatmentPanel;
    private javax.swing.JTable treatmentPlanTable;
    private javax.swing.JComboBox<String> typeComboBox;
    // End of variables declaration//GEN-END:variables
}
