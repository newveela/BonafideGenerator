package com.newveela.bonafidegenerator.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.newveela.bonafidegenerator.model.Student;
import com.newveela.bonafidegenerator.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
public class PDFGeneratorService {

    @Autowired
    private StudentRepository studentRepository;

    // Method to get the student's name by ID
    public String getStudentNameById(String studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return studentOpt.get().getStudentId();
        }
        return "Unknown_Student"; // Default name if not found
    }

    // Method to generate PDF for a specific student based on student ID
    public byte[] generatePdfForStudent(String studentId) throws DocumentException, IOException {
        // Fetch the student by their ID
        Optional<Student> studentOpt = studentRepository.findById(studentId);

        if (!studentOpt.isPresent()) {
            return null;
        }

        Student student = studentOpt.get();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();

        // Set up the fonts for the certificate
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK);
        Font contentFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);

        // Add the logo from the static folder
        String logoPath = "src/main/resources/static/cbit.png"; // Update the path if necessary
        Image logo = Image.getInstance(logoPath);
        logo.scaleToFit(320, 180);
        logo.setAlignment(Paragraph.ALIGN_CENTER);
        logo.setSpacingAfter(20);
        document.add(logo);

        // Add Title
        Paragraph title = new Paragraph("BONAFIDE CERTIFICATE", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Add Body Text for the specific student
        String certText = String.format(
                "BONAFIDE CERTIFICATE\n\n" +
                        "This is to certify that \033[1m%s\033[0m, bearing Roll Number \033[1m%s\033[0m, is a bonafide student " +
                        "of %s. He/She is currently enrolled in the %s program under the %s department in %d year " +
                        "for the academic session %d - %d.\n\n" +
                        "This certificate is issued upon request for official purposes.\n\n" +
                        "Place: Gandipet\n" +
                        "Issued Date: %s\n\n" +
                        "This is a system-generated document and does not require a signature.\n\n" +
                        "%s\n(Official Seal/Stamp)",
                student.getStudentName(),
                student.getStudentId(),
                student.getInstitutionName(),
                student.getCourseName(),
                student.getDepartment(),
                student.getCurrentYear(),
                student.getCurrentYear(),
                student.getCurrentYear() + 1,
                student.getIssueDate().toString(),
                student.getInstitutionName()
        );



        Paragraph content = new Paragraph(certText, contentFont);
        content.setAlignment(Paragraph.ALIGN_LEFT);
        content.setSpacingAfter(30);
        document.add(content);

        document.close();
        return baos.toByteArray();
    }
}
