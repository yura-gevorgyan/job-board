package am.itspace.jobboard.specification;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> searchUsers(String name, String surname, String email, Role role) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            if (surname != null && !surname.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("surname"), "%" + surname + "%"));
            }

            if (email != null && !email.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }

            if (role != null) {
                predicate = criteriaBuilder.and(predicate, root.get("role").in(role));
            }

            return predicate;
        };
    }
}