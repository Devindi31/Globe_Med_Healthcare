package gui;

import connection.MySQL;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFrame;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JPanel {

    public static String userRole;

    public Login() {
        initComponents();
    }

    abstract class LoginHandler {

        protected LoginHandler nextHandler;

        public LoginHandler setNext(LoginHandler nextHandler) {
            this.nextHandler = nextHandler;
            return nextHandler;
        }

        public void handle(String mobile, String password) {
            if (process(mobile, password) && nextHandler != null) {
                nextHandler.handle(mobile, password);
            }
        }

        protected abstract boolean process(String mobile, String password);
    }

    class NullOrBlankCheckHandler extends LoginHandler {

        @Override
        protected boolean process(String mobile, String password) {
            if (mobile == null || mobile.isBlank()) {
                JOptionPane.showMessageDialog(Login.this,
                        "Please Enter Mobile Number",
                        "Login Warning !",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (password == null || password.isBlank()) {
                JOptionPane.showMessageDialog(Login.this,
                        "Please Enter Password",
                        "Login Warning !",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return true;
        }
    }

    class CredentialCheckHandler extends LoginHandler {

        private ResultSet loginDetails;

        @Override
        protected boolean process(String mobile, String password) {
            try {
                String encryptPassword = PasswordDecryptor.encrypt(password);

                loginDetails = MySQL.execute(
                        "SELECT `name`,`mobile`,`password`,`role`.`role_id`,`role`.`name` AS roleName,`status` "
                        + "FROM `staff` "
                        + "INNER JOIN `role` ON `staff`.`role_role_id`=`role`.`role_id` "
                        + "WHERE `mobile`='" + mobile + "'"
                );

                if (loginDetails.next()) {
                    String dbPassword = loginDetails.getString("password");
                    if (dbPassword.equals(encryptPassword)) {
                        userRole = loginDetails.getString("role.name");
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(Login.this,
                                "Invalid Password!",
                                "Login Error !",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                } else {
                    JOptionPane.showMessageDialog(Login.this,
                            "User not found!",
                            "Login Error !",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    class StatusCheckHandler extends LoginHandler {

        @Override
        protected boolean process(String mobile, String password) {
            try {
                ResultSet rs = MySQL.execute(
                        "SELECT `status`,`full_name` FROM `staff` WHERE `mobile`='" + mobile + "'"
                );
                if (rs.next()) {
                    String status = rs.getString("status");
                    String name = rs.getString("full_name");
                    if (!"1".equalsIgnoreCase(status)) {
                        JOptionPane.showMessageDialog(Login.this,
                                "Your account is inactive. Please contact admin.",
                                "Login Error !",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    LoginComponent baseLogin = new BaseLoginComponent();
                    LoginComponent withLogging = new LoggingDecorator(baseLogin);
                    withLogging.login(name, mobile);

                    Home home = new Home();
                    Main.getMain().loadPanel(home);
                    Main.getMain().setExtendedState(JFrame.MAXIMIZED_BOTH);
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    interface LoginComponent {

        void login(String name, String mobile);
    }

    class BaseLoginComponent implements LoginComponent {

        @Override
        public void login(String name, String mobile) {
        }
    }

    class LoggingDecorator implements LoginComponent {

        private final LoginComponent wrappee;

        public LoggingDecorator(LoginComponent wrappee) {
            this.wrappee = wrappee;
        }

        @Override
        public void login(String name, String mobile) {
            wrappee.login(name, mobile);
            logToFile(name, mobile);
        }

        private void logToFile(String name, String mobile) {
            try (FileWriter fw = new FileWriter("login_logs.txt", true)) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                fw.write("User: " + name + " | Mobile: " + mobile + " | DateTime: " + timestamp + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class PasswordDecryptor {

        private static final String KEY = "1234567890123456";

        public static String encrypt(String password) throws Exception {
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        userNameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();

        loginButton.setBackground(new java.awt.Color(89, 89, 89));
        loginButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        loginButton.setText("Sign In");
        loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GlobeMed Health Care");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel2.setText(" Mobile Number");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel3.setText(" Password");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(123, 123, 123)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(userNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(138, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel1)
                .addGap(84, 84, 84)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed

        String mobile = userNameTextField.getText();
        String password = String.valueOf(passwordField.getPassword());

        LoginHandler handlerChain = new NullOrBlankCheckHandler();
        handlerChain.setNext(new CredentialCheckHandler()).setNext(new StatusCheckHandler());
        handlerChain.handle(mobile, password);

    }//GEN-LAST:event_loginButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField userNameTextField;
    // End of variables declaration//GEN-END:variables
}
