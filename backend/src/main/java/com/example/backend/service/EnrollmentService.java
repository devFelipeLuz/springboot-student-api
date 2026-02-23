package com.example.backend.service;

import com.example.backend.entity.Enrollment;
import com.example.backend.entity.Grade;
import com.example.backend.entity.Student;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.repository.EnorllmentRepository;
import com.example.backend.repository.GradeRepository;
import com.example.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class EnrollmentService {

    private EnorllmentRepository enrollmentRepo;
    private StudentRepository studentRepo;
    private GradeRepository gradeRepo;

    public EnrollmentService(
            EnorllmentRepository repository,
            StudentRepository studentRepo,
            GradeRepository gradeRepo) {

        this.enrollmentRepo = repository;
        this.studentRepo = studentRepo;
        this.gradeRepo = gradeRepo;
    }

    public Enrollment enroll(UUID studentId, UUID gradeId) {
        Student student = studentRepo.findById(studentId)
                        .orElseThrow(() -> new EntityNotFoundException("Student não encontrado"));

        Grade grade = gradeRepo.findById(gradeId)
                        .orElseThrow(() -> new EntityNotFoundException("Grade não encontrada"));


        grade.validateCapacity();
        student.validateCanEnroll();

        Enrollment enrollment = new Enrollment(student, grade);

        grade.increaseActiveEnrollmentsCount();
        student.addEnrollment(enrollment);
        grade.addEnrollment(enrollment);

        enrollmentRepo.save(enrollment);

        return enrollment;
    }

    public void cancel(Enrollment enrollment) {
        enrollment.getGrade().cancelEnrollment(enrollment);

        enrollmentRepo.save(enrollment);
    }
}
