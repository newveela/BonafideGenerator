package com.newveela.bonafidegenerator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newveela.bonafidegenerator.model.Student;
import com.newveela.bonafidegenerator.service.PDFGeneratorService;
import com.newveela.bonafidegenerator.service.StudentService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("list")
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "allstudents";
    }


    @GetMapping("/create")
    public String showCreateForm() {

        return "createStudent";
    }


    @PostMapping
    public String createStudent(@ModelAttribute Student student, Model model) {
        try {
            studentService.createStudent(student);
            model.addAttribute("message", "Student created successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Error creating student: " + e.getMessage());
        }
        return "createStudent";
    }


    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam("studentId") String studentId) {
        try {
            byte[] pdfBytes = pdfGeneratorService.generatePdfForStudent(studentId);
            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.notFound().build();
            }

            String studentName = pdfGeneratorService.getStudentNameById(studentId);
            String filename = studentName.replaceAll("[^a-zA-Z0-9]", "_") + "_bonafide_certificate.pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=" + filename);
            headers.add("Content-Type", "application/pdf");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/pdf/{studentId}")
    public ResponseEntity<byte[]> generatePdfPathVar(@PathVariable String studentId) {
        try {
            byte[] pdfBytes = pdfGeneratorService.generatePdfForStudent(studentId);
            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.notFound().build();
            }

            String studentName = pdfGeneratorService.getStudentNameById(studentId);
            String filename = studentName.replaceAll("[^a-zA-Z0-9]", "_") + "_bonafide_certificate.pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=" + filename);
            headers.add("Content-Type", "application/pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/load")
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
