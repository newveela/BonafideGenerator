package com.newveela.bonafidegenerator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Use Long for auto-generated identity values (not String)

    private String studentId;
    private String studentName;
    private String department;
    private String courseName;
    private final String institutionName = "CBIT";
    private final int currentYear = LocalDate.now().getYear();
    private LocalDate issueDate;

    // This method is called before persisting the entity, to set the issueDate and generate the studentId
    @PrePersist
    public void prePersist() {
        this.issueDate = LocalDate.now();
        if (this.department != null && !this.department.isEmpty()) {
            this.studentId = generateStudentIdBasedOnDepartment(this.department); // Generate student ID based on department
        }
    }

    // Method to generate student ID based on the department
    private String generateStudentIdBasedOnDepartment(String department) {
        String deptPrefix = "";

        switch (department.trim().toUpperCase()) {
            case "CS":
                deptPrefix = "CS";
                break;
            case "ME":
                deptPrefix = "ME";
                break;
            case "EC":
                deptPrefix = "EC";
                break;
            case "IT":
                deptPrefix = "IT";
                break;
            default:
                throw new IllegalArgumentException("Invalid department: " + department);
        }


        String randomDigits = String.format("%02d", (int)(Math.random() * 10000));
        return deptPrefix + randomDigits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
