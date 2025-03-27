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
    private Long id;  // Primary key

    private String studentId;   // Generated roll number
    private String studentName;
    private String department;
    private String courseName;

    private final String institutionName = "CBIT";
    private final int currentYear = LocalDate.now().getYear();

    private LocalDate issueDate;

    @PrePersist
    public void prePersist() {
        this.issueDate = LocalDate.now();
        if (this.department != null && !this.department.isEmpty()) {
            this.studentId = generateStudentIdBasedOnDepartment(this.department);
        }
    }

    private String generateStudentIdBasedOnDepartment(String department) {
        String deptPrefix;
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
        // Random digits for illustration; you could do more robust logic
        String randomDigits = String.format("%3d", (int) (Math.random() * 1000)).replace(" ", "0");
        return deptPrefix + randomDigits;
    }
}
