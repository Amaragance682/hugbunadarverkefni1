package com.hugbo.clock_in;

import java.lang.reflect.Field;
import java.time.Instant;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

public class SpecificationUtils {
    public static <T, F> Specification<T> fromFilter(F filter) {
        return (root, query, cb) -> {
            cb = (CriteriaBuilder) cb;
            Predicate predicate = cb.conjunction();

            for (Field field : filter.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    Object value = field.get(filter);
                    if (value == null) continue;

                    String fieldName = field.getName();
                    String pathExpr = field.isAnnotationPresent(FilterPath.class)
                        ? field.getAnnotation(FilterPath.class).value()
                        : inferFromPathName(fieldName);

                    Path<?> path = resolvePath(root, pathExpr);

                    if (fieldName.toLowerCase().endsWith("from")) {
                        Expression<? extends Comparable> expr = (Expression<? extends Comparable>) path;
                        predicate = cb.and(predicate, cb.greaterThanOrEqualTo(expr, (Comparable) value));
                    } else if (fieldName.toLowerCase().endsWith("to")) {
                        Expression<? extends Comparable> expr = (Expression<? extends Comparable>) path;
                        predicate = cb.and(predicate, cb.lessThanOrEqualTo(expr, (Comparable) value));
                    } else if (value instanceof String str && !str.isBlank()) {
                        predicate = cb.and(predicate,
                                cb.like(cb.lower(path.as(String.class)), "%" + str.toLowerCase() + "%"));
                    } else {
                        predicate = cb.and(predicate, cb.equal(path, value));
                    }
                } catch (Exception ignored) {}
            }
            return predicate;
        };
    }

    private static String inferFromPathName(String pathName) {
        return "";
    }

    private static Path<?> resolvePath(From<?, ?> root, String pathExpr) {
        From<?, ?> from = root;
        Path<?> path = root;

        String[] parts = pathExpr.split("\\.");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            if (i < parts.length - 1) {
                Join<?, ?> join = from.getJoins()
                    .stream()
                    .filter(j -> j.getAttribute().getName().equals(part))
                    .findFirst()
                    .orElse(null);

                if (join == null) {
                    join = from.join(part, JoinType.LEFT);
                }
                from = join;
                path = join;
            } else {
                path = from.get(part);
            }
        }

        return path;
    }
}
