package bean;

public class MedicalHistoryReportDetails {

    String medicalID;
    String medicalDate;
    String illness;
    String surgery;
    String allergy;
    String labTest;
    String medicines;

    public MedicalHistoryReportDetails(String medicalID, String medicalDate, String Illness, String surgery, String allergy, String labTest, String medicines) {
        this.medicalID = medicalID;
        this.medicalDate = medicalDate;
        this.illness = Illness;
        this.surgery = surgery;
        this.allergy = allergy;
        this.labTest = labTest;
        this.medicines = medicines;
    }

    public String getMedicalID() {
        return medicalID;
    }

    public String getMedicalDate() {
        return medicalDate;
    }

    public String getIllness() {
        return illness;
    }

    public String getSurgery() {
        return surgery;
    }

    public String getAllergy() {
        return allergy;
    }

    public String getLabTest() {
        return labTest;
    }

    public String getMedicines() {
        return medicines;
    }

}
