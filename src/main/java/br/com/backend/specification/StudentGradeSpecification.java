package br.com.backend.specification;

import br.com.backend.dto.request.StudentGradeFilter;
import br.com.backend.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentGradeSpecification {

    public static Specification<StudentGrade> withFilters(StudentGradeFilter filter) {
        return (root, query, criteriaBuilder) -> {

            if (StudentGrade.class.equals(query.getResultType())) {
                Fetch<StudentGrade, Assessment> assessmentFetch =
                        root.fetch("assessment", JoinType.LEFT);

                Fetch<Assessment, TeachingAssignment> assignmentFetch =
                        assessmentFetch.fetch("teachingAssignment", JoinType.LEFT);

                assignmentFetch.fetch("professor", JoinType.LEFT);
                assignmentFetch.fetch("subject", JoinType.LEFT);
                assignmentFetch.fetch("classroom", JoinType.LEFT);

                Fetch<StudentGrade, Enrollment> enrollmentFetch =
                        root.fetch("enrollment", JoinType.LEFT);

                enrollmentFetch.fetch("student", JoinType.LEFT);
            }

            Join<StudentGrade, Assessment> assessment =
                    root.join("assessment", JoinType.LEFT);

            Join<Assessment, TeachingAssignment> assignment =
                    root.join("assignment", JoinType.LEFT);

            Join<TeachingAssignment, Professor> professor =
                    assignment.join("professor", JoinType.LEFT);

            Join<TeachingAssignment, Subject> subject =
                    assignment.join("subject", JoinType.LEFT);

            Join<TeachingAssignment, Classroom> classroom =
                    assignment.join("classroom", JoinType.LEFT);

            Join<Enrollment, Student> student =
                    root.join("enrollment", JoinType.LEFT)
                            .join("student", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (filter.assessmentType() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                assessment.get("type"),
                                filter.assessmentType())
                );
            }

            addLikePredicate(predicates, criteriaBuilder, student.get("name"), filter.studentName());
            addLikePredicate(predicates, criteriaBuilder, professor.get("name"), filter.professorName());
            addLikePredicate(predicates, criteriaBuilder, subject.get("name"), filter.subjectName());
            addLikePredicate(predicates, criteriaBuilder, classroom.get("name"), filter.classroomName());

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void addLikePredicate(
            List<Predicate> predicates,
            CriteriaBuilder cb,
            Path<String> field,
            String value
    ) {
        if (value != null && !value.isBlank()) {
            String[] terms = value.toLowerCase().split("\\s+");

            for (String term : terms) {
                predicates.add(
                        cb.like(
                                cb.lower(field),
                                "%" + term + "%"
                        )
                );
            }
        }
    }
}
