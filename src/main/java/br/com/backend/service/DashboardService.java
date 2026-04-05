package br.com.backend.service;

import br.com.backend.dto.response.DashboardResponse;
import br.com.backend.entity.enums.EnrollmentStatus;
import br.com.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DashboardService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final ClassroomRepository classroomRepository;
    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssessmentRepository assessmentRepository;

    public DashboardService(
            StudentRepository studentRepository,
            ProfessorRepository professorRepository,
            ClassroomRepository classroomRepository,
            SubjectRepository subjectRepository,
            EnrollmentRepository enrollmentRepository,
            AssessmentRepository assessmentRepository) {

        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.classroomRepository = classroomRepository;
        this.subjectRepository = subjectRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.assessmentRepository = assessmentRepository;
    }

    public DashboardResponse getDashboard() {
        return new DashboardResponse(
                studentRepository.countByActiveTrue(),
                professorRepository.countByActiveTrue(),
                classroomRepository.countByActiveTrue(),
                subjectRepository.countByActiveTrue(),
                enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE),
                assessmentRepository.countByActiveTrue()
        );
    }
}
