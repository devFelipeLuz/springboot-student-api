package br.com.backend.specification;

import br.com.backend.entity.AttendanceRecord;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.Student;
import br.com.backend.entity.User;
import br.com.backend.entity.enums.AttendanceStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRecordSpecification {

    public static Specification<AttendanceRecord> withFilters(
            String studentName, String studentEmail, AttendanceStatus status) {

        return (root, query, criteriaBuilder) -> {

            if (AttendanceRecord.class.equals(query.getResultType())) {
                root.fetch("session", JoinType.LEFT);
                root.fetch("enrollment",  JoinType.LEFT)
                        .fetch("student", JoinType.LEFT)
                        .fetch("user", JoinType.LEFT);

                query.distinct(true);
            }

            Join<AttendanceRecord, Enrollment> enrollment =
                    root.join("enrollment", JoinType.LEFT);

            Join<Enrollment, Student> student =
                    enrollment.join("student", JoinType.LEFT);

            Join<Student, User> user =
                    student.join("user", JoinType.LEFT);


            List<Predicate> predicates = new ArrayList<>();

            if (studentName != null && !studentName.isBlank()) {
                String[] terms = studentName.toLowerCase().split("\\s+");

                for (String term : terms) {
                    predicates.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(student.get("name")),
                                    "%" + term + "%"
                            )
                    );
                }
            }

            if (studentEmail != null && !studentEmail.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(user.get("email")),
                                "%" + studentEmail.toLowerCase() + "%"
                        )
                );
            }

            if (status != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("status"), status)
                );
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
