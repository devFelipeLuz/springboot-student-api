package br.com.backend.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenericSpecification {

    public static <T> Specification<T> withFilters(Map<String, Object> filters) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            filters.forEach((field, value) -> {
                if (value == null) return;

                if (value instanceof String str && !str.isBlank()) {
                    String[] terms = str.toLowerCase().split("\\s+");

                    for (String term : terms) {
                        predicates.add(
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get(field)),
                                        "%" + term.toLowerCase() + "%"
                                )
                        );
                    }

                }

                predicates.add(
                        criteriaBuilder.equal(root.get(field), value)
                );

            });

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
