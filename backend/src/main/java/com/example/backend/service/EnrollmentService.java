package com.example.backend.service;

import com.example.backend.entity.Enrollment;
import com.example.backend.entity.Grade;
import com.example.backend.entity.Student;

public class EnrollmentService {

    public Enrollment enroll(Student student, Grade grade) {
        grade.validateCapacity();
        student.validateCanEnroll();

        Enrollment enrollment = new Enrollment(student, grade);

        grade.increaseActiveEnrollmentsCount();
        student.addEnrollment(enrollment);
        grade.addEnrollment(enrollment);

        return enrollment;
    }

    public void cancel(Enrollment enrollment) {
        enrollment.getGrade().cancelEnrollment(enrollment);
    }
}
