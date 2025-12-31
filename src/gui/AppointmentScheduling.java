package gui;

import connection.MySQL;
import dialogs.SelectPatient;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import permission.RoleRepository;
import raven.toast.Notifications;

interface AppointmentMediator {

    boolean bookAppointment(String patientMobile, String doctorId, String date, String time, String branchId, String typeId);

    boolean bookConsultation(String patientMobile, String doctorId, String date, String time, String branchId);

    boolean bookDiagnostic(String patientMobile, String doctorId, String date, String time, String branchId);

    boolean bookSurgery(String patientMobile, String doctorId, String date, String time, String branchId);
}

class AppointmentMediatorService implements AppointmentMediator {

    @Override
    public boolean bookConsultation(String patientMobile, String doctorId, String date, String time, String branchId) {
        return bookAppointment(patientMobile, doctorId, date, time, branchId, "1");
    }

    @Override
    public boolean bookDiagnostic(String patientMobile, String doctorId, String date, String time, String branchId) {
        return bookAppointment(patientMobile, doctorId, date, time, branchId, "2");
    }

    @Override
    public boolean bookSurgery(String patientMobile, String doctorId, String date, String time, String branchId) {
        return bookAppointment(patientMobile, doctorId, date, time, branchId, "3");
    }

    @Override
    public boolean bookAppointment(String patientMobile, String doctorId, String date, String time, String branchId, String typeId) {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM appointment "
                    + "WHERE doctor_id='" + doctorId + "' "
                    + "AND appointment_date='" + date + "' "
                    + "AND appointment_time='" + time + "' "
                    + "AND branch_id='" + branchId + "' "
                    + "AND type_type_id='" + typeId + "'");

            if (rs.next()) {
                return false;
            }

            ResultSet lastID = MySQL.execute("SELECT appointment_number FROM appointment ORDER BY appointment_number DESC LIMIT 1");
            String newAppointmentId = "AP0001";
            if (lastID.next()) {
                int num = Integer.parseInt(lastID.getString("appointment_number").substring(2));
                newAppointmentId = String.format("AP%04d", ++num);
            }

            String addedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            MySQL.execute("INSERT INTO appointment (appointment_number, added_date, patient, doctor_id, appointment_date, appointment_time, branch_id, type_type_id) "
                    + "VALUES('" + newAppointmentId + "','" + addedDate + "','" + patientMobile + "', '" + doctorId + "', '" + date + "', '" + time + "', '" + branchId + "', '" + typeId + "')");

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

public class AppointmentScheduling extends javax.swing.JPanel {

    public String patientMobile = "";
    HashMap<String, String> doctorHashMap;
    HashMap<String, String> branchHashMap;
    HashMap<String, String> typeHashMap;

    private AppointmentMediator mediator;

    public AppointmentScheduling(AppointmentMediator mediator) {
        this.mediator = mediator;
        initComponents();
        defaultPermission();
        setupPermissions();
        loadCombo();
        Notifications.getInstance().setJFrame(Main.getMain());
        buttonActionChange(false);
        appointmentTimeTextField.setText("");
        loadAppointmentDetails();
    }

    private void defaultPermission() {
        selectPatientButton.setVisible(false);
        bookAppointmentButton.setVisible(false);
        confirmAppointmentButton.setVisible(false);
        updateAppointmentButton.setVisible(false);
        cancelAppointmentButton.setVisible(false);
        visibelFiltersLableName(false);
        visibelFilters(false);
    }

    private void visibelFiltersLableName(boolean isVisible) {
        selectDoctorLabel.setVisible(isVisible);
        appointmentDateLabel.setVisible(isVisible);
        appointmentTimeLabel.setVisible(isVisible);
        selectBranchLabel.setVisible(isVisible);
        selectTypeLabel.setVisible(isVisible);
    }

    private void visibelFilters(boolean isVisible) {
        doctorComboBox.setVisible(isVisible);
        appointmentDateChooser.setVisible(isVisible);
        appointmentTimeTextField.setVisible(isVisible);
        appointmentTimeSelectorButton.setVisible(isVisible);
        branchComboBox.setVisible(isVisible);
        typeComboBox.setVisible(isVisible);
    }

    private void setupPermissions() {
        if (RoleRepository.hasPermission(Login.userRole, "appointment_select_patient")) {
            selectPatientButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "appointment_filters")) {
            visibelFilters(true);
            visibelFiltersLableName(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "appointment_book_appointment_button")) {
            bookAppointmentButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "appointment_update_button")) {
            updateAppointmentButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "appointment_confirm_button")) {
            confirmAppointmentButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "appointment_cancel_button")) {
            cancelAppointmentButton.setVisible(true);
        }
    }

    private void loadCombo() {
        doctorHashMap = new HashMap<>();
        branchHashMap = new HashMap<>();
        typeHashMap = new HashMap<>();
        loadDoctor();
        loadBranch();
        loadType();
    }

    public void setPatientName(String patientName) {
        this.patientNameLabel.setText(patientName);
    }

    public void setPatientMobile(String patientMobile) {
        this.patientMobileLabel.setText(patientMobile);
    }

    private void loadDoctor() {

        try {

            ResultSet doctorResult = MySQL.execute("SELECT `staff_id`,`full_name` FROM `staff` "
                    + "INNER JOIN `role` ON `staff`.`role_role_id`=`role`.`role_id` WHERE `role`.`name`='Doctor'");

            Vector<String> doctorVector = new Vector<>();
            doctorVector.add("Select Doctor");

            while (doctorResult.next()) {

                doctorVector.add(doctorResult.getString("full_name"));
                doctorHashMap.put(doctorResult.getString("full_name"), doctorResult.getString("staff_id"));

            }

            doctorComboBox.setModel(new DefaultComboBoxModel<>(doctorVector));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadBranch() {

        try {

            ResultSet classResult = MySQL.execute("SELECT * FROM `branch`");

            Vector<String> branchVector = new Vector<>();
            branchVector.add("Select Branch");

            while (classResult.next()) {

                branchVector.add(classResult.getString("name"));
                branchHashMap.put(classResult.getString("name"), classResult.getString("branch_id"));

            }

            branchComboBox.setModel(new DefaultComboBoxModel<>(branchVector));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadType() {

        try {

            ResultSet classResult = MySQL.execute("SELECT * FROM `type`");

            Vector<String> typeVector = new Vector<>();
            typeVector.add("Select Type");

            while (classResult.next()) {

                typeVector.add(classResult.getString("name"));
                typeHashMap.put(classResult.getString("name"), classResult.getString("type_id"));

            }

            typeComboBox.setModel(new DefaultComboBoxModel<>(typeVector));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadAppointmentDetails() {

        try {

            String patientMobile = patientMobileLabel.getText();
            String selectedDoctor = String.valueOf(doctorComboBox.getSelectedItem());
            Date appointmentDate = appointmentDateChooser.getDate();
            String selectedBranch = String.valueOf(branchComboBox.getSelectedItem());
            String selectedType = String.valueOf(typeComboBox.getSelectedItem());

            String query = "SELECT `appointment_number`,`added_date`,`patient`.`mobile` AS `patient_mobile`,`patient`.`full_name` AS `patient_name`,`staff`.`staff_id`,`staff`.`full_name`,"
                    + "`appointment_date`,`appointment_time`,`branch`.`branch_id`,`branch`.`name`,`type`.`type_id`,`type`.`name`,`confirmation` FROM `appointment` "
                    + "INNER JOIN `patient` ON `appointment`.`patient`=`patient`.`mobile` "
                    + "INNER JOIN `staff` ON `appointment`.`doctor_id`=`staff`.`staff_id` "
                    + "INNER JOIN `branch` ON `appointment`.`branch_id`=`branch`.`branch_id` "
                    + "INNER JOIn `type` ON `appointment`.`type_type_id`=`type`.`type_id` "
                    + "WHERE (`patient`.`mobile` LIKE '%" + patientMobile + "%') ";

            if (!selectedDoctor.equals("Select Doctor")) {
                query += " AND `staff`.`staff_id` ='" + doctorHashMap.get(selectedDoctor) + "'";
            }

            if (appointmentDate != null) {
                query += " AND `appointment_date`='" + new SimpleDateFormat("yyyy-MM-dd").format(appointmentDate) + "'";
            }

            if (!selectedBranch.equals("Select Branch")) {
                query += " AND `branch`.`branch_id`='" + branchHashMap.get(selectedBranch) + "'";
            }

            if (!selectedType.equals("Select Type")) {
                query += " AND `type`.`type_id`='" + typeHashMap.get(selectedType) + "'";
            }

            query += " ORDER BY `added_date` DESC";

            ResultSet appointmentResult = MySQL.execute(query);
            DefaultTableModel tableModel = (DefaultTableModel) appointmentTable.getModel();
            tableModel.setRowCount(0);

            while (appointmentResult.next()) {

                Vector<String> row = new Vector<>();
                row.add(appointmentResult.getString("appointment_number"));
                row.add(appointmentResult.getString("added_date"));
                row.add(appointmentResult.getString("patient_name"));
                row.add(appointmentResult.getString("patient_mobile"));
                row.add(appointmentResult.getString("staff.full_name"));
                row.add(appointmentResult.getString("appointment_date"));
                row.add(appointmentResult.getString("appointment_time"));
                row.add(appointmentResult.getString("branch.name"));
                row.add(appointmentResult.getString("type.name"));

                String confirmation = appointmentResult.getString("confirmation");
                if ("1".equals(confirmation)) {
                    row.add("Pending");
                } else if ("2".equals(confirmation)) {
                    row.add("Confirmed");
                } else if ("3".equals(confirmation)) {
                    row.add("Cancelled");
                }
                tableModel.addRow(row);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clear() {

        patientNameLabel.setText("None");
        patientMobileLabel.setText("");
        doctorComboBox.setSelectedIndex(0);
        appointmentDateChooser.setDate(null);
        appointmentTimeTextField.setText("");
        branchComboBox.setSelectedIndex(0);
        typeComboBox.setSelectedIndex(0);
        appointmentIDLabel.setText("None");
        buttonActionChange(false);
        loadAppointmentDetails();
        selectPatientButton.setEnabled(true);
        bookAppointmentButton.setEnabled(true);

    }

    private void buttonActionChange(boolean enabled) {
        updateAppointmentButton.setEnabled(enabled);
        cancelAppointmentButton.setEnabled(enabled);
        confirmAppointmentButton.setEnabled(enabled);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AppointmentTimePicker = new com.raven.swing.TimePicker();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        selectPatientButton = new javax.swing.JButton();
        selectDoctorLabel = new javax.swing.JLabel();
        doctorComboBox = new javax.swing.JComboBox<>();
        appointmentDateLabel = new javax.swing.JLabel();
        appointmentDateChooser = new com.toedter.calendar.JDateChooser();
        selectBranchLabel = new javax.swing.JLabel();
        branchComboBox = new javax.swing.JComboBox<>();
        bookAppointmentButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        appointmentTable = new javax.swing.JTable();
        updateAppointmentButton = new javax.swing.JButton();
        cancelAppointmentButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        appointmentIDLabel = new javax.swing.JLabel();
        patientNameLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        patientMobileLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        confirmAppointmentButton = new javax.swing.JButton();
        selectTypeLabel = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        appointmentTimeSelectorButton = new javax.swing.JButton();
        appointmentTimeTextField = new javax.swing.JTextField();
        appointmentTimeLabel = new javax.swing.JLabel();
        searchButton = new javax.swing.JButton();

        AppointmentTimePicker.setDisplayText(appointmentTimeTextField);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Appointment Scheduling");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Patient Name");

        selectPatientButton.setBackground(new java.awt.Color(89, 89, 89));
        selectPatientButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        selectPatientButton.setText("Select Patient");
        selectPatientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPatientButtonActionPerformed(evt);
            }
        });

        selectDoctorLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        selectDoctorLabel.setText("Select Doctor");

        doctorComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        doctorComboBox.setMinimumSize(new java.awt.Dimension(76, 30));
        doctorComboBox.setPreferredSize(new java.awt.Dimension(76, 30));

        appointmentDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        appointmentDateLabel.setText(" Appointment Date");

        selectBranchLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        selectBranchLabel.setText(" Select Branch");

        branchComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        bookAppointmentButton.setBackground(java.awt.SystemColor.textHighlight);
        bookAppointmentButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bookAppointmentButton.setText("Book Appointment");
        bookAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookAppointmentButtonActionPerformed(evt);
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

        appointmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Appointment Number", "Added Date ", "Patient Name", "Patient Mobile", "Doctor's Name", "Appointment Date", "Appointment Time", "Branch", "Type", "Confirmation "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        appointmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                appointmentTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(appointmentTable);

        updateAppointmentButton.setBackground(new java.awt.Color(67, 144, 77));
        updateAppointmentButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        updateAppointmentButton.setText("Update Appointment");
        updateAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateAppointmentButtonActionPerformed(evt);
            }
        });

        cancelAppointmentButton.setBackground(new java.awt.Color(188, 80, 75));
        cancelAppointmentButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cancelAppointmentButton.setText("Cancel Appointment");
        cancelAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelAppointmentButtonActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Appointment No :");

        appointmentIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        appointmentIDLabel.setText("None");

        patientNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        patientNameLabel.setText("None");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Mobile Number");

        patientMobileLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText(" Appoiment History");

        confirmAppointmentButton.setBackground(new java.awt.Color(67, 144, 77));
        confirmAppointmentButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        confirmAppointmentButton.setText("Confirm Appointment");
        confirmAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmAppointmentButtonActionPerformed(evt);
            }
        });

        selectTypeLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        selectTypeLabel.setText(" Select Type");

        typeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel13.setText(":");

        jLabel14.setText(":");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Info.png"))); // NOI18N
        jLabel8.setText("Double click the table row to update the appointment.");

        appointmentTimeSelectorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/clock.png"))); // NOI18N
        appointmentTimeSelectorButton.setToolTipText("Add new  grade");
        appointmentTimeSelectorButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        appointmentTimeSelectorButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        appointmentTimeSelectorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appointmentTimeSelectorButtonActionPerformed(evt);
            }
        });

        appointmentTimeTextField.setEnabled(false);

        appointmentTimeLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        appointmentTimeLabel.setText(" Appointment Time");

        searchButton.setBackground(new java.awt.Color(51, 51, 51));
        searchButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        searchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        searchButton.setToolTipText("Search");
        searchButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator1))
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(selectPatientButton)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(patientNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                            .addComponent(patientMobileLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(selectDoctorLabel)
                                    .addComponent(doctorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(appointmentDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(appointmentDateLabel))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(appointmentTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(appointmentTimeSelectorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(appointmentTimeLabel))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(branchComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(selectBranchLabel))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(selectTypeLabel)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                                .addComponent(bookAppointmentButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(appointmentIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(confirmAppointmentButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(updateAppointmentButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelAppointmentButton))
                            .addComponent(jScrollPane1))
                        .addGap(18, 18, 18))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(selectPatientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(patientNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(patientMobileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(appointmentDateLabel)
                                        .addComponent(selectBranchLabel)
                                        .addComponent(appointmentTimeLabel))
                                    .addComponent(selectDoctorLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(appointmentDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(branchComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                    .addComponent(doctorComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(appointmentTimeTextField)
                                    .addComponent(appointmentTimeSelectorButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bookAppointmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(selectTypeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                    .addComponent(typeComboBox))
                                .addGap(3, 3, 3)))))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(appointmentIDLabel))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelAppointmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(updateAppointmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(confirmAppointmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectPatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPatientButtonActionPerformed

        JDialog medicalDialog = new SelectPatient(Main.getMain(), true, "A", new BillingAndInsuranceClaims(), this, new Reports());
        medicalDialog.setLocationRelativeTo(this);
        medicalDialog.setVisible(true);


    }//GEN-LAST:event_selectPatientButtonActionPerformed

    private void appointmentTimeSelectorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appointmentTimeSelectorButtonActionPerformed
        AppointmentTimePicker.showPopup(this, WIDTH, WIDTH);
    }//GEN-LAST:event_appointmentTimeSelectorButtonActionPerformed

    private void bookAppointmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookAppointmentButtonActionPerformed

        String patientMobile = patientMobileLabel.getText();
        String selectedDoctor = String.valueOf(doctorComboBox.getSelectedItem());
        Date date = appointmentDateChooser.getDate();
        String appointmentTime = appointmentTimeTextField.getText();
        String selectedBranch = String.valueOf(branchComboBox.getSelectedItem());
        String selectedType = String.valueOf(typeComboBox.getSelectedItem());

        Date today = new Date();

        if (patientMobile.equals("")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select patient");
        } else if (selectedDoctor.equals("Select Doctor")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select doctor");
        } else if (date == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select appointment date");
        } else if (date.equals(today) || date.before(today)) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Choose Valid Appointment Date");
        } else if (appointmentTime.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select appointment time");
        } else if (selectedBranch.equals("Select Branch")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select branch");
        } else if (selectedType.equals("Select Type")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select type");
        } else {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String appointmentDate = dateFormat.format(date);

            boolean success = mediator.bookAppointment(patientMobile,
                    doctorHashMap.get(selectedDoctor),
                    appointmentDate,
                    appointmentTime,
                    branchHashMap.get(selectedBranch),
                    typeHashMap.get(selectedType));

            switch (selectedType) {
                case "Consultation":
                    success = mediator.bookConsultation(patientMobile, doctorHashMap.get(selectedDoctor), appointmentDate, appointmentTime, branchHashMap.get(selectedBranch));
                    break;
                case "Diagnostic":
                    success = mediator.bookDiagnostic(patientMobile, doctorHashMap.get(selectedDoctor), appointmentDate, appointmentTime, branchHashMap.get(selectedBranch));
                    break;
                case "Surgery":
                    success = mediator.bookSurgery(patientMobile, doctorHashMap.get(selectedDoctor), appointmentDate, appointmentTime, branchHashMap.get(selectedBranch));
                    break;
            }

            if (success) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER,
                        patientNameLabel.getText() + "'s appointment has been successfully scheduled.");
                clear();

            } else {
                JOptionPane.showMessageDialog(this, "This time slot is already booked !", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        }


    }//GEN-LAST:event_bookAppointmentButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void appointmentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_appointmentTableMouseClicked

        int selectedRow = appointmentTable.getSelectedRow();

        if (evt.getClickCount() == 2 && selectedRow != -1) {

            appointmentIDLabel.setText(String.valueOf(appointmentTable.getValueAt(selectedRow, 0)));
            patientNameLabel.setText(String.valueOf(appointmentTable.getValueAt(selectedRow, 2)));
            patientMobileLabel.setText(String.valueOf(appointmentTable.getValueAt(selectedRow, 3)));
            doctorComboBox.setSelectedItem(String.valueOf(appointmentTable.getValueAt(selectedRow, 4)));

            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                appointmentDateChooser.setDate(simpleDateFormat.parse(String.valueOf(appointmentTable.getValueAt(selectedRow, 5))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            appointmentTimeTextField.setText(String.valueOf(appointmentTable.getValueAt(selectedRow, 6)));
            branchComboBox.setSelectedItem(String.valueOf(appointmentTable.getValueAt(selectedRow, 7)));
            typeComboBox.setSelectedItem(String.valueOf(appointmentTable.getValueAt(selectedRow, 8)));

            bookAppointmentButton.setEnabled(false);
            selectPatientButton.setEnabled(false);

            String confirmed = String.valueOf(appointmentTable.getValueAt(selectedRow, 9));

            if ("Cancelled".equals(confirmed)) {
                confirmAppointmentButton.setEnabled(false);
                updateAppointmentButton.setEnabled(false);
                cancelAppointmentButton.setEnabled(false);
            } else if ("Confirmed".equals(confirmed)) {
                confirmAppointmentButton.setEnabled(false);
                updateAppointmentButton.setEnabled(true);
                cancelAppointmentButton.setEnabled(true);
            } else if ("Pending".equals(confirmed)) {
                buttonActionChange(true);
            }

        }

    }//GEN-LAST:event_appointmentTableMouseClicked

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        loadAppointmentDetails();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void cancelAppointmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelAppointmentButtonActionPerformed

        String appintmentID = appointmentIDLabel.getText();

        if (appintmentID.equals("None")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select appointment");
        } else {

            try {
                ResultSet appointmentResult = MySQL.execute("SELECT `appointment_number` FROM `appointment` WHERE `appointment_number`='" + appintmentID + "'");

                if (appointmentResult.next()) {

                    int confimation = JOptionPane.showConfirmDialog(this, "Do you want to cancel the appointment ?", "Confirmation", JOptionPane.YES_OPTION);

                    if (confimation == JOptionPane.YES_OPTION) {
                        MySQL.execute("UPDATE `appointment` SET `confirmation`='3' WHERE `appointment_number`='" + appintmentID + "'");
                        clear();
                        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, appintmentID + " Appointment was Canceled.");
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "No appointment results were found", "An Error Occurred !", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_cancelAppointmentButtonActionPerformed

    private void confirmAppointmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmAppointmentButtonActionPerformed
        String appintmentID = appointmentIDLabel.getText();

        if (appintmentID.equals("None")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select appointment");
        } else {

            try {
                ResultSet appointmentResult = MySQL.execute("SELECT `appointment_number` FROM `appointment` WHERE `appointment_number`='" + appintmentID + "'");

                if (appointmentResult.next()) {

                    MySQL.execute("UPDATE `appointment` SET `confirmation`='2' WHERE `appointment_number`='" + appintmentID + "'");
                    clear();
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, appintmentID + " Appointment Confirmed.");

                } else {
                    JOptionPane.showMessageDialog(this, "No appointment results were found", "An Error Occurred !", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_confirmAppointmentButtonActionPerformed

    private void updateAppointmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateAppointmentButtonActionPerformed

        String patientMobile = patientMobileLabel.getText();
        String selectedDoctor = String.valueOf(doctorComboBox.getSelectedItem());
        Date date = appointmentDateChooser.getDate();
        String appointmentTime = appointmentTimeTextField.getText();
        String selectedBranch = String.valueOf(branchComboBox.getSelectedItem());
        String selectedType = String.valueOf(typeComboBox.getSelectedItem());
        String appointmentNumber = appointmentIDLabel.getText();

        Date today = new Date();

        if (appointmentNumber.equals("None")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select appointment");
        } else if (patientMobile.equals("")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select patient");
        } else if (selectedDoctor.equals("Select Doctor")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select doctor");
        } else if (date == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select appointment date");
        } else if (date.equals(today) || date.before(today)) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Choose Valid Appointment Date");
        } else if (appointmentTime.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select appointment time");
        } else if (selectedBranch.equals("Select Branch")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select branch");
        } else if (selectedType.equals("Select Type")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please select type");
        } else {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String appointmentDate = dateFormat.format(date);

            try {
                ResultSet appointmentResult = MySQL.execute("SELECT `appointment_number`,`patient` FROM `appointment` "
                        + "WHERE (`appointment_number`='" + appointmentNumber + "' AND `patient`='" + patientMobile + "')");

                if (appointmentResult.next()) {

                    MySQL.execute("UPDATE `appointment` SET "
                            + "`doctor_id`='" + doctorHashMap.get(selectedDoctor) + "',"
                            + "`appointment_date`='" + appointmentDate + "',"
                            + "`appointment_time`='" + appointmentTime + "',"
                            + "`branch_id`='" + branchHashMap.get(selectedBranch) + "',"
                            + "`type_type_id`='" + typeHashMap.get(selectedType) + "' WHERE `appointment_number`='" + appointmentResult.getString("appointment_number") + "'");
                    clear();
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, appointmentResult.getString("appointment_number") + " Appointment Updated.");

                } else {
                    JOptionPane.showMessageDialog(this, "Update Not Complete, No appointment results found", "An Error Occurred !", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }//GEN-LAST:event_updateAppointmentButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.swing.TimePicker AppointmentTimePicker;
    private com.toedter.calendar.JDateChooser appointmentDateChooser;
    private javax.swing.JLabel appointmentDateLabel;
    private javax.swing.JLabel appointmentIDLabel;
    private javax.swing.JTable appointmentTable;
    private javax.swing.JLabel appointmentTimeLabel;
    private javax.swing.JButton appointmentTimeSelectorButton;
    private javax.swing.JTextField appointmentTimeTextField;
    private javax.swing.JButton bookAppointmentButton;
    private javax.swing.JComboBox<String> branchComboBox;
    private javax.swing.JButton cancelAppointmentButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton confirmAppointmentButton;
    private javax.swing.JComboBox<String> doctorComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel patientMobileLabel;
    private javax.swing.JLabel patientNameLabel;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel selectBranchLabel;
    private javax.swing.JLabel selectDoctorLabel;
    private javax.swing.JButton selectPatientButton;
    private javax.swing.JLabel selectTypeLabel;
    private javax.swing.JComboBox<String> typeComboBox;
    private javax.swing.JButton updateAppointmentButton;
    // End of variables declaration//GEN-END:variables
}
