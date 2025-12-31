package gui;

import connection.MySQL;
import dialogs.AddLaboratoryTests;
import dialogs.AddMedicalDetails;
import dialogs.AddSurgeryDetails;
import dialogs.AddTreatmentPlans;
import dialogs.PatientRegistration;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import permission.RoleRepository;
import raven.toast.Notifications;

public class PatientManagement extends javax.swing.JPanel {

    private String illness;
    private String surgery;
    private String laboratoryTestName;

    public PatientManagement() {
        initComponents();
        defaultPermission();
        setupPermission();
        loadPatients("");
        updateActionButtonsState(false);
        Notifications.getInstance().setJFrame(Main.getMain());
    }

    private void defaultPermission() {
        registerPatientButton.setVisible(false);
        addTreatmentPlanButton.setVisible(false);
        addSurgeryDetailsButton.setVisible(false);
        addLaboratoryTestButton.setVisible(false);
    }

    private void setupPermission() {
        if (RoleRepository.hasPermission(Login.userRole, "patient_register_button")) {
            registerPatientButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "patient_click_personaldetails_table")) {
            personalDetailsTable.setEnabled(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "patient_click_medicalhistory_table")) {
            medicalHistoryTable.setEnabled(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "patient_add_treatmentplan_button")) {
            addTreatmentPlanButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "patient_add_surgery_button")) {
            addSurgeryDetailsButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "patient_add_laboratorytest_button")) {
            addLaboratoryTestButton.setVisible(true);
        }
    }

    interface PatientDetails {

        void loadData(String patientIdentifier);

        Vector<String> getData();
    }

    class BasicPatientDetails implements PatientDetails {

        protected Vector<String> data = new Vector<>();

        @Override
        public void loadData(String patientName) {
            data.clear();
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `patient` WHERE `full_name` LIKE '%" + patientName + "%' ORDER BY `registered_date` DESC");
                if (rs.next()) {
                    data.add(rs.getString("full_name"));
                    data.add(rs.getString("age"));
                    data.add(rs.getString("mobile"));
                    data.add(rs.getString("home_town"));
                    data.add(rs.getString("registered_date"));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Vector<String> getData() {
            return data;
        }
    }

    abstract class PatientDetailsDecorator implements PatientDetails {

        protected PatientDetails patientDetails;

        public PatientDetailsDecorator(PatientDetails patientDetails) {
            this.patientDetails = patientDetails;
        }

        @Override
        public void loadData(String patientIdentifier) {
            patientDetails.loadData(patientIdentifier);
        }

        @Override
        public Vector<String> getData() {
            return patientDetails.getData();
        }
    }

    class MedicalHistoryDecorator extends PatientDetailsDecorator {

        public MedicalHistoryDecorator(PatientDetails patientDetails) {
            super(patientDetails);
        }

        @Override
        public void loadData(String patientMobile) {
            super.loadData(patientMobile);
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `medical_history` WHERE `patient_mobile`='" + patientMobile + "'");
                while (rs.next()) {
                    patientDetails.getData().add(rs.getString("illnesses"));
                    patientDetails.getData().add(rs.getString("surgeries"));
                    patientDetails.getData().add(rs.getString("allergies"));
                    patientDetails.getData().add(rs.getString("laboratory_test"));
                    patientDetails.getData().add(rs.getString("medicines"));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class TreatmentPlanDecorator extends PatientDetailsDecorator {

        public TreatmentPlanDecorator(PatientDetails patientDetails) {
            super(patientDetails);
        }

        @Override
        public void loadData(String patientMobile) {
            super.loadData(patientMobile);
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `treatment_plans` "
                        + "INNER JOIN `medical_history` ON `treatment_plans`.`medical_history_medical_history_id`=`medical_history`.`medical_history_id` "
                        + "WHERE `medical_history`.`patient_mobile`='" + patientMobile + "'");
                while (rs.next()) {
                    patientDetails.getData().add(rs.getString("added_date"));
                    patientDetails.getData().add(rs.getString("procedures_or_therapies"));
                    patientDetails.getData().add(rs.getString("medications"));
                    patientDetails.getData().add(rs.getString("next_schedule"));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class SurgeryDecorator extends PatientDetailsDecorator {

        public SurgeryDecorator(PatientDetails patientDetails) {
            super(patientDetails);
        }

        @Override
        public void loadData(String patientMobile) {
            super.loadData(patientMobile);
            try {
                ResultSet rs = MySQL.execute("SELECT `surgeries` FROM `medical_history` WHERE `patient_mobile`='" + patientMobile + "'");
                while (rs.next()) {
                    patientDetails.getData().add(rs.getString("surgeries"));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class LaboratoryDecorator extends PatientDetailsDecorator {

        public LaboratoryDecorator(PatientDetails patientDetails) {
            super(patientDetails);
        }

        @Override
        public void loadData(String patientMobile) {
            super.loadData(patientMobile);
            try {
                ResultSet rs = MySQL.execute("SELECT `laboratory_test` FROM `medical_history` WHERE `patient_mobile`='" + patientMobile + "'");
                while (rs.next()) {
                    patientDetails.getData().add(rs.getString("laboratory_test"));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void registerNewPatient(String mobile, String fullName, String age, String homeTown, String registeredDate) throws Exception {
        ResultSet rs = MySQL.execute("SELECT `mobile` FROM `patient` WHERE `mobile`='" + mobile + "'");
        if (rs.next()) {
            throw new Exception("This patient already exists.");
        }

        MySQL.execute("INSERT INTO `patient` (`mobile`,`full_name`,`age`,`home_town`,`registered_date`) "
                + "VALUES('" + mobile + "','" + fullName + "','" + age + "','" + homeTown + "','" + registeredDate + "')");

        loadPatients("");
    }

    public void addMedicalHistory(String patientMobile, String illness, String surgery, String allergy, String laboratoryTest, String medicine, String patientName) {

        try {

            String addedDate = new SimpleDateFormat("yyyy-MM-d").format(new Date());

            ResultSet medicalHistoryResult = MySQL.execute("SELECT `medical_history_id` FROM `medical_history` "
                    + "WHERE (`patient_mobile`='" + patientMobile + "' && `added_date`='" + addedDate + "' && `illnesses`='" + illness + "')");

            if (medicalHistoryResult.next()) {
                JOptionPane.showMessageDialog(this, "This Medical Report is Already Exists", "Duplicate Entry !", JOptionPane.ERROR_MESSAGE);
            } else {

                ResultSet lastID = MySQL.execute("SELECT `medical_history_id` FROM `medical_history` ORDER BY `medical_history_id` DESC LIMIT 1;");

                String newMedicalId = "MH0001";

                if (lastID.next()) {
                    String lastId = lastID.getString("medical_history_id");
                    int num = Integer.parseInt(lastId.substring(2));
                    num++;
                    newMedicalId = String.format("MH%04d", num);
                }

                MySQL.execute("INSERT INTO `medical_history` (`medical_history_id`,`patient_mobile`,`added_date`,`illnesses`,`surgeries`,`allergies`,`laboratory_test`,`medicines`) "
                        + "VALUES ('" + newMedicalId + "','" + patientMobile + "','" + addedDate + "','" + illness + "','" + surgery + "','" + allergy + "','" + laboratoryTest + "','" + medicine + "')");
                loadmedicalHistory(patientName, patientMobile);
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, patientName + "'s Medical Details Added Successfull !");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addTreatmentPlan(String addedDate, String medicines, String therapies, java.util.Date nextScheduleDate, String medicalID) {

        try {
            ResultSet treatmentResult = MySQL.execute("SELECT `treatment_plans_id` FROM `treatment_plans` WHERE `medical_history_medical_history_id`='" + medicalID + "'");
            if (treatmentResult.next()) {
                JOptionPane.showMessageDialog(this, "This Treatment Plan is Already Exists", "Duplicate Entry !", JOptionPane.ERROR_MESSAGE);
            } else {
                ResultSet lastID = MySQL.execute("SELECT `treatment_plans_id` FROM `treatment_plans` ORDER BY `treatment_plans_id` DESC LIMIT 1;");
                String newTreatmentPlanId = "TP0001";
                if (lastID.next()) {
                    String lastId = lastID.getString("treatment_plans_id");
                    int num = Integer.parseInt(lastId.substring(2));
                    num++;
                    newTreatmentPlanId = String.format("TP%04d", num);
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formatScheduleDate = dateFormat.format(nextScheduleDate);
                MySQL.execute("INSERT INTO `treatment_plans` (`treatment_plans_id`,`added_date`,`medications`,`procedures_or_therapies`,`next_schedule`,`medical_history_medical_history_id`) "
                        + "VALUES ('" + newTreatmentPlanId + "','" + addedDate + "','" + medicines + "','" + therapies + "','" + formatScheduleDate + "','" + medicalID + "')");

                loadTreatmentPlans("", medicalID);
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, "The Treatment Plan was Successfully Added.");
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    public void showPatientDetails(String patientMobile) {
        PatientDetails patientDetails = new BasicPatientDetails();
        patientDetails = new MedicalHistoryDecorator(patientDetails);
        patientDetails = new TreatmentPlanDecorator(patientDetails);
        patientDetails = new SurgeryDecorator(patientDetails);
        patientDetails = new LaboratoryDecorator(patientDetails);

        patientDetails.loadData(patientMobile);

        Vector<String> fullData = patientDetails.getData();
        DefaultTableModel model = (DefaultTableModel) personalDetailsTable.getModel();
        model.setRowCount(0);
        model.addRow(fullData);
    }

    public void loadPatients(String searchName) {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `patient` WHERE `full_name` LIKE '%" + searchName + "%' ORDER BY `registered_date` DESC");
            DefaultTableModel model = (DefaultTableModel) personalDetailsTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("full_name"));
                row.add(rs.getString("age"));
                row.add(rs.getString("mobile"));
                row.add(rs.getString("home_town"));
                row.add(rs.getString("registered_date"));
                model.addRow(row);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void loadmedicalHistory(String patientName, String patientMobile) {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `medical_history`"
                    + "INNER JOIN `patient` ON `patient`.`mobile`=`medical_history`.`patient_mobile`"
                    + "WHERE `full_name` LIKE '%" + patientName + "%' && `patient_mobile` LIKE '%" + patientMobile + "%' ORDER BY `added_date` DESC");
            DefaultTableModel model = (DefaultTableModel) medicalHistoryTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                Vector<String> row = new Vector<>();

                row.add(rs.getString("medical_history_id"));
                row.add(rs.getString("added_date"));
                row.add(rs.getString("illnesses"));
                row.add(rs.getString("surgeries"));
                row.add(rs.getString("allergies"));
                row.add(rs.getString("laboratory_test"));
                row.add(rs.getString("medicines"));

                model.addRow(row);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTreatmentPlans(String patientName, String medicalID) {
        try {

            ResultSet treatmentPlanResult = MySQL.execute("SELECT `treatment_plans`.`added_date`, `medical_history`.`illnesses`,`treatment_plans`.`procedures_or_therapies`,`medications`,`next_schedule` FROM `treatment_plans` "
                    + "INNER JOIN `medical_history` ON `treatment_plans`.`medical_history_medical_history_id`=`medical_history`.`medical_history_id`"
                    + "INNER JOIN `patient` ON `medical_history`.`patient_mobile`=`patient`.`mobile` "
                    + "WHERE (`patient`.`full_name` LIKE '%" + patientName + "%' && `medical_history_medical_history_id` LIKE '%" + medicalID + "%') ORDER BY `treatment_plans`.`added_date` DESC");

            DefaultTableModel tableModel = (DefaultTableModel) treatmentPlanTable.getModel();
            tableModel.setRowCount(0);

            while (treatmentPlanResult.next()) {

                Vector<String> treatmentPlanVector = new Vector<String>();

                treatmentPlanVector.add(treatmentPlanResult.getString("treatment_plans.added_date"));
                treatmentPlanVector.add(treatmentPlanResult.getString("medical_history.illnesses"));
                treatmentPlanVector.add(treatmentPlanResult.getString("medications"));
                treatmentPlanVector.add(treatmentPlanResult.getString("treatment_plans.procedures_or_therapies"));
                treatmentPlanVector.add(treatmentPlanResult.getString("treatment_plans.next_schedule"));

                tableModel.addRow(treatmentPlanVector);

            }

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private void updateActionButtonsState(boolean enabled) {
        addTreatmentPlanButton.setEnabled(enabled);
        addSurgeryDetailsButton.setEnabled(enabled);
        addLaboratoryTestButton.setEnabled(enabled);
    }

    private void clear() {

        patientNameTextField.setText("");
        medicalIdLabel.setText("None");
        loadPatients("");

        DefaultTableModel medicalModel = (DefaultTableModel) medicalHistoryTable.getModel();
        medicalModel.setRowCount(0);

        DefaultTableModel treatmentPlanModel = (DefaultTableModel) treatmentPlanTable.getModel();
        treatmentPlanModel.setRowCount(0);

        updateActionButtonsState(false);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        addLaboratoryTestButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        patientNameTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        personalDetailsTable = new javax.swing.JTable();
        clearButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        medicalHistoryTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        treatmentPlanTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        registerPatientButton = new javax.swing.JButton();
        addSurgeryDetailsButton = new javax.swing.JButton();
        addTreatmentPlanButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        medicalIdLabel = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Patient Management");

        addLaboratoryTestButton.setBackground(new java.awt.Color(102, 102, 102));
        addLaboratoryTestButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addLaboratoryTestButton.setText("Add Laboratory Tests");
        addLaboratoryTestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLaboratoryTestButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Search by Name");

        patientNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                patientNameTextFieldKeyReleased(evt);
            }
        });

        personalDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Full Name", "Age", "Mobile Number", "Home Town", "Registered Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        personalDetailsTable.setEnabled(false);
        personalDetailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                personalDetailsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(personalDetailsTable);

        clearButton.setBackground(new java.awt.Color(51, 51, 51));
        clearButton.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Personal Details");

        medicalHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Medical ID", "Added Date", "Illnesses", "Surgeries ", "Allergies", "Laboratory Tests", "Medicines"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        medicalHistoryTable.setEnabled(false);
        medicalHistoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                medicalHistoryTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(medicalHistoryTable);
        if (medicalHistoryTable.getColumnModel().getColumnCount() > 0) {
            medicalHistoryTable.getColumnModel().getColumn(0).setMinWidth(100);
            medicalHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            medicalHistoryTable.getColumnModel().getColumn(0).setMaxWidth(100);
        }

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
        jScrollPane3.setViewportView(treatmentPlanTable);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Info.png"))); // NOI18N
        jLabel4.setText("Double click the row to add medical details");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText(" Medical History ");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Treatment Plans");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Info.png"))); // NOI18N
        jLabel7.setText("Double-click the row to add treatment plans, surgery details, laboratory test");

        registerPatientButton.setBackground(java.awt.SystemColor.textHighlight);
        registerPatientButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        registerPatientButton.setText("Register Patient");
        registerPatientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerPatientButtonActionPerformed(evt);
            }
        });

        addSurgeryDetailsButton.setBackground(new java.awt.Color(102, 102, 102));
        addSurgeryDetailsButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addSurgeryDetailsButton.setText("Add Surgery Details");
        addSurgeryDetailsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSurgeryDetailsButtonActionPerformed(evt);
            }
        });

        addTreatmentPlanButton.setBackground(new java.awt.Color(102, 102, 102));
        addTreatmentPlanButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addTreatmentPlanButton.setText("Add Treatment Plans");
        addTreatmentPlanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTreatmentPlanButtonActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel8.setText("Medical ID  :");

        medicalIdLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        medicalIdLabel.setText("None");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(patientNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel3)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(registerPatientButton))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1235, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(medicalIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addTreatmentPlanButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addSurgeryDetailsButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addLaboratoryTestButton)))
                        .addGap(15, 15, 15))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(registerPatientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(patientNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addLaboratoryTestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addSurgeryDetailsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTreatmentPlanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(medicalIdLabel))
                .addGap(39, 39, 39)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addGap(23, 23, 23))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addLaboratoryTestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLaboratoryTestButtonActionPerformed
        AddLaboratoryTests laboratoryTestDialog = new AddLaboratoryTests(Main.getMain(), true);
        laboratoryTestDialog.setMedicalID(medicalIdLabel.getText());
        laboratoryTestDialog.setLaboratoryTestName(laboratoryTestName);
        laboratoryTestDialog.loadLaboratoryTestDetails(medicalIdLabel.getText());
        laboratoryTestDialog.setLocationRelativeTo(this);
        laboratoryTestDialog.setVisible(true);
    }//GEN-LAST:event_addLaboratoryTestButtonActionPerformed

    private void personalDetailsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_personalDetailsTableMouseClicked

        int row = personalDetailsTable.getSelectedRow();
        if (evt.getClickCount() == 2 && row != -1) {
            AddMedicalDetails medicalDialog = new AddMedicalDetails(Main.getMain(), true, this);
            medicalDialog.setPatientMobile(String.valueOf(personalDetailsTable.getValueAt(row, 2)));
            medicalDialog.setPatientName(String.valueOf(personalDetailsTable.getValueAt(row, 0)));
            loadmedicalHistory(String.valueOf(personalDetailsTable.getValueAt(row, 0)), String.valueOf(personalDetailsTable.getValueAt(row, 2)));
            medicalDialog.setLocationRelativeTo(this);
            medicalDialog.setVisible(true);

        }

    }//GEN-LAST:event_personalDetailsTableMouseClicked

    private void medicalHistoryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_medicalHistoryTableMouseClicked

        int selectedRow = medicalHistoryTable.getSelectedRow();

        if (evt.getClickCount() == 2 && selectedRow != -1) {
            updateActionButtonsState(true);
            medicalIdLabel.setText(String.valueOf(medicalHistoryTable.getValueAt(selectedRow, 0)));
            illness = String.valueOf(medicalHistoryTable.getValueAt(selectedRow, 2));
            surgery = String.valueOf(medicalHistoryTable.getValueAt(selectedRow, 3));
            laboratoryTestName = String.valueOf(medicalHistoryTable.getValueAt(selectedRow, 5));

            loadTreatmentPlans("", medicalIdLabel.getText());

        }
    }//GEN-LAST:event_medicalHistoryTableMouseClicked

    private void addSurgeryDetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSurgeryDetailsButtonActionPerformed
        AddSurgeryDetails surgeryDialog = new AddSurgeryDetails(Main.getMain(), true);
        surgeryDialog.setSurgeryName(surgery);
        surgeryDialog.setMedicalID(medicalIdLabel.getText());
        surgeryDialog.loadSurgeryDetails(medicalIdLabel.getText());
        surgeryDialog.setLocationRelativeTo(this);
        surgeryDialog.setVisible(true);
    }//GEN-LAST:event_addSurgeryDetailsButtonActionPerformed

    private void addTreatmentPlanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTreatmentPlanButtonActionPerformed

        AddTreatmentPlans treatmentDialog = new AddTreatmentPlans(Main.getMain(), true, this);
        treatmentDialog.setMedicalID(medicalIdLabel.getText());
        treatmentDialog.setIllness(illness);
        treatmentDialog.setLocationRelativeTo(this);
        treatmentDialog.setVisible(true);
    }//GEN-LAST:event_addTreatmentPlanButtonActionPerformed

    private void patientNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_patientNameTextFieldKeyReleased

        String patientName = patientNameTextField.getText();

        loadPatients(patientName);
        updateActionButtonsState(false);

        if (patientName.isBlank()) {
            clear();
        } else {
            loadmedicalHistory(patientName, "");
            loadTreatmentPlans(patientName, "");
        }
    }//GEN-LAST:event_patientNameTextFieldKeyReleased

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void registerPatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerPatientButtonActionPerformed
        PatientRegistration registerDialog = new PatientRegistration(Main.getMain(), true, this);
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setVisible(true);
    }//GEN-LAST:event_registerPatientButtonActionPerformed

    public void openAddMedicalDetailsDialog(String patientMobile, String patientName) {
        AddMedicalDetails dialog = new AddMedicalDetails(null, true, this);
        dialog.setPatientMobile(patientMobile);
        dialog.setPatientName(patientName);
        dialog.setVisible(true);
    }

    public void openAddTreatmentPlansDialog(String medicalID, String illness, String patientMobile) {
        AddTreatmentPlans dialog = new AddTreatmentPlans(null, true, this);
        dialog.setMedicalID(medicalID);
        dialog.setIllness(illness);
        dialog.setVisible(true);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLaboratoryTestButton;
    private javax.swing.JButton addSurgeryDetailsButton;
    private javax.swing.JButton addTreatmentPlanButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable medicalHistoryTable;
    private javax.swing.JLabel medicalIdLabel;
    private javax.swing.JTextField patientNameTextField;
    private javax.swing.JTable personalDetailsTable;
    private javax.swing.JButton registerPatientButton;
    private javax.swing.JTable treatmentPlanTable;
    // End of variables declaration//GEN-END:variables
}
