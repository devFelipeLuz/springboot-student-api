package br.com.backend.specification;

import br.com.backend.entity.Enrollment;
import br.com.backend.entity.Student;
import br.com.backend.entity.enums.EnrollmentStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentSpecification {

    public static Specification<Enrollment> withFilters(String studentName, EnrollmentStatus status) {

        return (root, query, criteriaBuilder) -> {

            if (Enrollment.class.equals(query.getResultType())) {
                root.fetch("student", JoinType.LEFT);
                query.distinct(true);
            }

            Join<Enrollment, Student> student =
                    root.join("student", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (studentName != null && !studentName.isBlank()) {
                String[] terms = studentName.toLowerCase().split("\\s+");

                for (String term : terms) {
                    predicates.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(student.get("name")),
                                    "%" + term.toLowerCase() + "%"
                            )
                    );
                }
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
