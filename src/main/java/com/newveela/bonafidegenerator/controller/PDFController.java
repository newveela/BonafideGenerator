package com.newveela.bonafidegenerator.controller;

import com.newveela.bonafidegenerator.service.PDFGeneratorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("students")
public class PDFController {

    private final PDFGeneratorService pdfGeneratorService;

    public PDFController(PDFGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    // Endpoint to generate PDF for a specific student by their student ID
    @GetMapping("pdf/{studentId}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String studentId) {
        try {
            byte[] pdfBytes = pdfGeneratorService.generatePdfForStudent(studentId);

            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.notFound().build(); // Student not found
            }

            // Fetch the student's name to dynamically set the filename
            String studentName = pdfGeneratorService.getStudentNameById(studentId);

            // Sanitize the name to avoid invalid filename characters
            String filename = studentName.replaceAll("[^a-zA-Z0-9]", "_") + "_bonafide_certificate.pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=" + filename);
            headers.add("Content-Type", "application/pdf");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // Internal server error
        }
    }
}
