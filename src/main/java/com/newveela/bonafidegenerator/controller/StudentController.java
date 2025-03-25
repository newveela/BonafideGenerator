package com.newveela.bonafidegenerator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newveela.bonafidegenerator.model.ResponseMessage;
import com.newveela.bonafidegenerator.model.Student;
import com.newveela.bonafidegenerator.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private ObjectMapper objectMapper;
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable String studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student != null) {
            return new ResponseEntity<>(student, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            studentService.createStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Student created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Error creating student: " + e.getMessage())); // Return a JSON response
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    @PostMapping("/loadData")
    public ResponseEntity<?> loadData() {
        try {
            String filePath = "src/main/resources/static/load.json";
            File jsonFile = new File(filePath);
            List<Student> students = objectMapper.readValue(jsonFile, objectMapper.getTypeFactory().constructCollectionType(List.class, Student.class));
            studentService.saveAllStudents(students);
            return ResponseEntity.status(HttpStatus.CREATED).body("Students loaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error loading data from JSON file: " + e.getMessage());
        }
    }
}
