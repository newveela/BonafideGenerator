package com.newveela.bonafidegenerator.repository;

import com.newveela.bonafidegenerator.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {



    Optional<Student> findByStudentId(String studentId);

}
