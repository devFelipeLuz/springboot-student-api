package br.com.backend.specification;

import br.com.backend.entity.Student;
import br.com.backend.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {

    public static Specification<Student> withFilters(String name, String email, Boolean active){
        return (root, query, criteriaBuilder) -> {

            if (Student.class.equals(query.getResultType())) {
                root.fetch("user", JoinType.LEFT);
            }

            Join<Student, User> user = root.join("user", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                String[] terms = name.toLowerCase().split("\\s+");

                for (String term : terms) {
                    predicates.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("name")),
                                    "%" + term + "%"
                            )
                    );
                }
            }

            if (email != null && !email.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(user.get("email")),
                                "%" + email.toLowerCase() + "%"
                        )
                );
            }

            if (active != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("active"), active)
                );
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
