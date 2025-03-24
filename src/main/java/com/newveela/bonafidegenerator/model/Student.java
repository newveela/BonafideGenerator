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
}
