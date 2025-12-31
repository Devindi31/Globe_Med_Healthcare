package gui;

import com.formdev.flatlaf.FlatLaf;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import permission.RoleRepository;

public class Home extends javax.swing.JPanel {

    private final Main mainPanel;

    public Home() {
        initComponents();
        defaultPermissions();
        setupPermissions();
        this.mainPanel = Main.getMain();

    }

    private void defaultPermissions() {
        patientManagementButton.setVisible(false);
        appointmentSchedulingButton.setVisible(false);
        reportsButton.setVisible(false);
        billingAndInsuranceClaimsButton.setVisible(false);
        staffRolesAndPermissionsButton.setVisible(false);
    }

    private void setupPermissions() {

        if (RoleRepository.hasPermission(Login.userRole, "patient")) {
            patientManagementButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "appointment")) {
            appointmentSchedulingButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "report")) {
            reportsButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "billing")) {
            billingAndInsuranceClaimsButton.setVisible(true);
        }
        if (RoleRepository.hasPermission(Login.userRole, "staff_management")) {
            staffRolesAndPermissionsButton.setVisible(true);
        }

        FlatLaf.updateUI();

    }

    private void loadSectionPanel(JPanel panel) {

        if (!panel.isShowing()) {
            loadPanels.removeAll();
            loadPanels.add(panel, BorderLayout.CENTER);
            FlatLaf.updateUI();
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuPanel = new javax.swing.JPanel();
        patientManagementButton = new javax.swing.JButton();
        appointmentSchedulingButton = new javax.swing.JButton();
        reportsButton = new javax.swing.JButton();
        staffRolesAndPermissionsButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        billingAndInsuranceClaimsButton = new javax.swing.JButton();
        logOutButton = new javax.swing.JButton();
        loadPanels = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 51, 51));
        setLayout(new java.awt.BorderLayout());

        menuPanel.setBackground(new java.awt.Color(51, 51, 51));
        menuPanel.setPreferredSize(new java.awt.Dimension(70, 653));

        patientManagementButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Patient.png"))); // NOI18N
        patientManagementButton.setToolTipText("Patient Management");
        patientManagementButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        patientManagementButton.setPreferredSize(new java.awt.Dimension(45, 45));
        patientManagementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientManagementButtonActionPerformed(evt);
            }
        });

        appointmentSchedulingButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Scheduling.png"))); // NOI18N
        appointmentSchedulingButton.setToolTipText("Appointment Scheduling");
        appointmentSchedulingButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        appointmentSchedulingButton.setPreferredSize(new java.awt.Dimension(45, 45));
        appointmentSchedulingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appointmentSchedulingButtonActionPerformed(evt);
            }
        });

        reportsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Report.png"))); // NOI18N
        reportsButton.setToolTipText("Reports");
        reportsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reportsButton.setPreferredSize(new java.awt.Dimension(45, 45));
        reportsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportsButtonActionPerformed(evt);
            }
        });

        staffRolesAndPermissionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Role.png"))); // NOI18N
        staffRolesAndPermissionsButton.setToolTipText("Managing Medical Staff Roles and Permissions ");
        staffRolesAndPermissionsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        staffRolesAndPermissionsButton.setPreferredSize(new java.awt.Dimension(45, 45));
        staffRolesAndPermissionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffRolesAndPermissionsButtonActionPerformed(evt);
            }
        });

        billingAndInsuranceClaimsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Payment.png"))); // NOI18N
        billingAndInsuranceClaimsButton.setToolTipText("Billing and Insurance Claims");
        billingAndInsuranceClaimsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        billingAndInsuranceClaimsButton.setPreferredSize(new java.awt.Dimension(45, 45));
        billingAndInsuranceClaimsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billingAndInsuranceClaimsButtonActionPerformed(evt);
            }
        });

        logOutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Logout.png"))); // NOI18N
        logOutButton.setToolTipText("Log Out");
        logOutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logOutButton.setPreferredSize(new java.awt.Dimension(45, 45));
        logOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reportsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(patientManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(appointmentSchedulingButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(billingAndInsuranceClaimsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(menuPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(staffRolesAndPermissionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(logOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(patientManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(appointmentSchedulingButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billingAndInsuranceClaimsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(staffRolesAndPermissionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 203, Short.MAX_VALUE)
                .addComponent(logOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(menuPanel, java.awt.BorderLayout.LINE_START);

        loadPanels.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Icon.png"))); // NOI18N
        jLabel1.setText("GlobeMed Health Care");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setIconTextGap(10);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        loadPanels.add(jLabel1, java.awt.BorderLayout.CENTER);

        add(loadPanels, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void patientManagementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientManagementButtonActionPerformed
        loadSectionPanel(new PatientManagement());
    }//GEN-LAST:event_patientManagementButtonActionPerformed

    private void appointmentSchedulingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appointmentSchedulingButtonActionPerformed
        loadSectionPanel(new AppointmentScheduling(new AppointmentMediatorService()));
    }//GEN-LAST:event_appointmentSchedulingButtonActionPerformed

    private void reportsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportsButtonActionPerformed
        loadSectionPanel(new Reports());
    }//GEN-LAST:event_reportsButtonActionPerformed

    private void staffRolesAndPermissionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffRolesAndPermissionsButtonActionPerformed
        loadSectionPanel(new StaffRolesManagement());
    }//GEN-LAST:event_staffRolesAndPermissionsButtonActionPerformed

    private void billingAndInsuranceClaimsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billingAndInsuranceClaimsButtonActionPerformed
        loadSectionPanel(new BillingAndInsuranceClaims());
    }//GEN-LAST:event_billingAndInsuranceClaimsButtonActionPerformed

    private void logOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutButtonActionPerformed
        mainPanel.loadPanel(new Login());
        mainPanel.setExtendedState(JFrame.NORMAL);
    }//GEN-LAST:event_logOutButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton appointmentSchedulingButton;
    private javax.swing.JButton billingAndInsuranceClaimsButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel loadPanels;
    private javax.swing.JButton logOutButton;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JButton patientManagementButton;
    private javax.swing.JButton reportsButton;
    private javax.swing.JButton staffRolesAndPermissionsButton;
    // End of variables declaration//GEN-END:variables
}
