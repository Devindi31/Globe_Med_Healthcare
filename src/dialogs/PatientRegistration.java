package dialogs;

import gui.PatientManagement;
import javax.swing.JFrame;
import raven.toast.Notifications;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class PatientRegistration extends javax.swing.JDialog {

    private PatientManagement patientManagement;

    public PatientRegistration(java.awt.Frame parent, boolean modal, PatientManagement patientManagement) {
        super(parent, modal);
        initComponents();
        this.patientManagement = patientManagement;
        Notifications.getInstance().setJFrame((JFrame) parent);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        fullNameTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        ageTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        mobileTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        homeTownTextField = new javax.swing.JTextField();
        patientRegisterButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Register New Patient");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText(" Full Name");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText(" Age");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 153));
        jLabel4.setText(" Mobile Number");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 153, 153));
        jLabel5.setText(" Home Town");

        patientRegisterButton.setBackground(java.awt.SystemColor.textHighlight);
        patientRegisterButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        patientRegisterButton.setText("Register");
        patientRegisterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientRegisterButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(patientRegisterButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fullNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(mobileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(ageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(homeTownTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(32, 32, 32))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fullNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mobileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(homeTownTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addComponent(patientRegisterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void patientRegisterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientRegisterButtonActionPerformed
        String fullName = fullNameTextField.getText();
        String age = ageTextField.getText();
        String mobile = mobileTextField.getText();
        String homeTown = homeTownTextField.getText();

        if (fullName == null || fullName.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Enter Full Name");
            return;
        }

        if (fullName.length() > 50) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Full name must be less than 50 characters.");
            return;

        }
        if (age.isBlank() && age.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Enter Your Age");
            return;

        }
        if (!age.matches("^[0-9]{1,3}$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Invalid Age");
            return;

        }
        if (Integer.parseInt(age) <= 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Age cannot be less than 0");
            return;

        }
        if (mobile == null || mobile.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Enter a Mobile Number");
            return;

        }
        if (!mobile.matches("^07[1,2,4,5,6,7,8,0]{1}[0-9]{7}$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Invalid Mobile Number");
            return;

        }
        if (homeTown == null || homeTown.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Please Enter Home Town");
            return;

        }
        if (homeTown.length() > 60) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_CENTER, "Home Town less than 60 Characters");
            return;

        }

        String registeredDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        try {
            patientManagement.registerNewPatient(mobile, fullName, age, homeTown, registeredDate);
            this.dispose();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_CENTER, "Patient Registration Successful !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while registering: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_patientRegisterButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ageTextField;
    private javax.swing.JTextField fullNameTextField;
    private javax.swing.JTextField homeTownTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField mobileTextField;
    private javax.swing.JButton patientRegisterButton;
    // End of variables declaration//GEN-END:variables
}
