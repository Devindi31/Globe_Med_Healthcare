package bean;

public class TreatmentReportDetails {

    private String addedDate;
    private String illness;
    private String medications;
    private String therapies;
    private String nextSchedule;

    public TreatmentReportDetails(String addedDate, String Illness, String medications, String therapies, String nextSchedule) {
        this.addedDate = addedDate;
        this.illness = Illness;
        this.medications = medications;
        this.therapies = therapies;
        this.nextSchedule = nextSchedule;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getIllness() {
        return illness;
    }

    public String getMedications() {
        return medications;
    }

    public String getTherapies() {
        return therapies;
    }

    public String getNextSchedule() {
        return nextSchedule;
    }

}
