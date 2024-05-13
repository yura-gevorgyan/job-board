package am.itspace.jobboard.specification;

import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.entity.Company;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CompanySpecification {
    public static Specification<Company> searchCompanies(String name, Category category, Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (!StringUtils.isBlank(name)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (category != null) {
                Join<Company, Category> categoryJoin = root.join("category", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(categoryJoin, category));
            }
            if (isActive != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isActive"), isActive));
            }
            return predicate;
        });
    }
}
