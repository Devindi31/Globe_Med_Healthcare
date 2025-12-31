package permission;

public class RoleRepository {

    private static RoleGroup rootGroup = new RoleGroup("All Staff");

    static {

        RoleGroup medicalStaff = new RoleGroup("Medical Staff");
        RoleGroup adminStaff = new RoleGroup("Admin Staff");

        RoleLeaf doctor = new RoleLeaf("Doctor");

        //Home Permissions
        doctor.addPermission("patient");
        doctor.addPermission("appointment");
        doctor.addPermission("report");

        //Patient Management Permissions
        doctor.addPermission("patient_click_personaldetails_table");
        doctor.addPermission("patient_click_medicalhistory_table");
        doctor.addPermission("patient_add_treatmentplan_button");
        doctor.addPermission("patient_add_surgery_button");
        doctor.addPermission("patient_add_laboratorytest_button");

        //Appointment Management Permissions
        doctor.addPermission("appointment_confirm_button");
        doctor.addPermission("appointment_cancel_button");

        //Reports Management Permissions
        doctor.addPermission("report_treatments_panel");
        doctor.addPermission("report_diangostic_panel");

        RoleLeaf nurse = new RoleLeaf("Nurse");

        //Home Permissions
        nurse.addPermission("patient");
        nurse.addPermission("appointment");
        nurse.addPermission("report");
        nurse.addPermission("billing");

        //Patient Management Permissions
        nurse.addPermission("patient_register_button");
        nurse.addPermission("patient_click_personaldetails_table");
        nurse.addPermission("patient_click_medicalhistory_table");
        nurse.addPermission("patient_add_treatmentplan_button");
        nurse.addPermission("patient_add_surgery_button");
        nurse.addPermission("patient_add_laboratorytest_button");

        //Appointment Management Permissions
        nurse.addPermission("appointment_select_patient");
        nurse.addPermission("appointment_filters");
        nurse.addPermission("appointment_book_appointment_button");
        nurse.addPermission("appointment_update_button");

        //Reports Management Permissions
        nurse.addPermission("report_treatments_panel");
        nurse.addPermission("report_treatments_print_button");
        nurse.addPermission("report_diangostic_panel");
        nurse.addPermission("report_diangostic_print_button");

        //Billing and Insurance Claims Management Permissions
        nurse.addPermission("bill_pay_consultation_charges_button");

        RoleLeaf pharmacist = new RoleLeaf("Pharmacist");

        //Home Permissions
        pharmacist.addPermission("patient");
        pharmacist.addPermission("billing");

        //Billing and Insurance Claims Management Permissions
        pharmacist.addPermission("bill_proceed_payment_button");
        pharmacist.addPermission("bill_select_medicines_button");
        pharmacist.addPermission("bill_insurance_claim_radio_buttton");

        RoleLeaf admin = new RoleLeaf("Admin");

        //Home Permissions
        admin.addPermission("patient");
        admin.addPermission("appointment");
        admin.addPermission("report");
        admin.addPermission("billing");
        admin.addPermission("staff_management");

        //Patient Management Permissions
        admin.addPermission("patient_register_button");
        admin.addPermission("patient_click_personaldetails_table");
        admin.addPermission("patient_click_medicalhistory_table");
        admin.addPermission("patient_add_treatmentplan_button");
        admin.addPermission("patient_add_surgery_button");
        admin.addPermission("patient_add_laboratorytest_button");

        //Appointment Management Permissions
        admin.addPermission("appointment_select_patient");
        admin.addPermission("appointment_filters");
        admin.addPermission("appointment_book_appointment_button");
        admin.addPermission("appointment_update_button");
        admin.addPermission("appointment_confirm_button");
        admin.addPermission("appointment_cancel_button");

        //Reports Management Permissions
        admin.addPermission("report_treatments_panel");
        admin.addPermission("report_treatments_print_button");
        admin.addPermission("report_diangostic_panel");
        admin.addPermission("report_diangostic_print_button");
        admin.addPermission("report_financial_panel");

        //Billing and Insurance Claims Management Permissions
        admin.addPermission("bill_proceed_payment_button");
        admin.addPermission("bill_select_medicines_button");
        admin.addPermission("bill_pay_consultation_charges_button");
        admin.addPermission("bill_insurance_claim_radio_buttton");

        medicalStaff.add(doctor);
        medicalStaff.add(nurse);
        medicalStaff.add(pharmacist);
        adminStaff.add(admin);

        rootGroup.add(medicalStaff);
        rootGroup.add(adminStaff);
    }

    public static RoleComponent getRoles() {
        return rootGroup;
    }

    public static boolean hasPermission(String roleName, String permission) {
        for (RoleComponent group : rootGroup.getChildren()) {
            for (RoleComponent role : group.getChildren()) {
                if (role.getName().equalsIgnoreCase(roleName)) {
                    return role.hasPermission(permission);
                }
            }
        }
        return false;
    }
}
