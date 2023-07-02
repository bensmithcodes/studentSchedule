package entity;

public class PatientEntity extends CustomerEntity {

    private String medicalDepartment;
    private String medicalHistory;

    public PatientEntity(int cusId, String cusName, String cusAddress, String city, String postalCode, String phoneNo, String country) {
        super(cusId, cusName, cusAddress, city, postalCode, phoneNo, country, "P");
    }

    public PatientEntity(String cusName, String cusAddress, String city, String postalCode, String phoneNo, String country) {
        super(cusName, cusAddress, city, postalCode, phoneNo, country, "P");
    }

    public String getMedicalDepartment() {
        return medicalDepartment;
    }

    public void setMedicalDepartment(String medicalDepartment) {
        this.medicalDepartment = medicalDepartment;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}
