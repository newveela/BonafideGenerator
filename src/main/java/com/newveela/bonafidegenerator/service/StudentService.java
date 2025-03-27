package com.newveela.bonafidegenerator.service;

import com.newveela.bonafidegenerator.model.Student;
import com.newveela.bonafidegenerator.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student getStudentById(String studentId) {
        Optional<Student> studentOpt = studentRepository.findByStudentId(studentId);
        return studentOpt.orElse(null);
    }

    public void createStudent(Student student) {
        studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void saveAllStudents(List<Student> students) {
        studentRepository.saveAll(students);
    }

    public Optional<Student> findByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }
}
