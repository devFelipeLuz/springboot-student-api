package com.example.backend.service;

import com.example.backend.entity.Enrollment;
import com.example.backend.entity.Grade;
import com.example.backend.entity.Student;
import com.example.backend.exception.BusinessException;

public class EnrollmentService {

    public Enrollment enroll(Student student, Grade grade) {
        grade.validateCapacity();
        student.validateCanEnroll();

        return new Enrollment(student, grade);
    }
}
