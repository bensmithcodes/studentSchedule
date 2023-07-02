package entity;

public class StudentEntity extends CustomerEntity {

    private String academicLevel;
    private String course;

    public StudentEntity(int cusId, String cusName, String cusAddress, String city, String postalCode, String phoneNo, String country) {
        super(cusId, cusName, cusAddress, city, postalCode, phoneNo, country, "S");

    }

    public StudentEntity(String cusName, String cusAddress, String city, String postalCode, String phoneNo, String country) {
        super(cusName, cusAddress, city, postalCode, phoneNo, country, "S");

    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }


}
