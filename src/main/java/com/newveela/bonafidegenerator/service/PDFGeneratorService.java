package com.newveela.bonafidegenerator.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
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

    public String getStudentNameById(String studentId) {
        Optional<Student> studentOpt = studentRepository.findByStudentId(studentId);
        return studentOpt.map(Student::getStudentId).orElse("Unknown_Student");
    }

    public byte[] generatePdfForStudent(String studentId) throws DocumentException, IOException {
        Optional<Student> studentOpt = studentRepository.findByStudentId(studentId);
        if (!studentOpt.isPresent()) {
            return null;
        }

        Student student = studentOpt.get();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK);
        Font contentFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);

        String logoPath = "src/main/resources/static/cbit.png";
        Image logo = Image.getInstance(logoPath);
        logo.scaleToFit(500, 100);
        logo.setAlignment(Paragraph.ALIGN_CENTER);
        logo.setSpacingAfter(20);
        document.add(logo);

        Paragraph title = new Paragraph("BONAFIDE AND CONDUCT CERTIFICATE", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        String certText = String.format(
                "This is to certify that %s, bearing Roll Number %s, is a bonafide student " +
                        "of %s. He/She is currently enrolled in the %s program under the %s department for " +
                        "the academic year %d-%d.\n\n" +
                        "This certificate is issued upon request for official purposes.\n\n" +
                        "Place: Gandipet\n" +
                        "Issued Date: %s\n\n" +
                        "%s\n\n\nThis is a system-generated document and does not require a signature.",
                student.getStudentName().toUpperCase(),
                student.getStudentId().toUpperCase(),
                student.getInstitutionName(),
                student.getCourseName().toUpperCase(),
                student.getDepartment().toUpperCase(),
                student.getCurrentYear(),
                student.getCurrentYear() + 1,
                student.getIssueDate().toString(),
                student.getInstitutionName()
        );

        Paragraph content = new Paragraph(certText, contentFont);
        content.setAlignment(Paragraph.ALIGN_LEFT);
        content.setSpacingAfter(30);
        document.add(content);


        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
                new Phrase("CBIT, \t" +
                        "Gandipet, Hyderabad, Telangana, India - 500075, \t" +
                        "www.cbit.ac.in", contentFont),
                document.getPageSize().getWidth() / 2, document.bottomMargin() - 10, 0);

        document.close();
        return baos.toByteArray();
    }
}
