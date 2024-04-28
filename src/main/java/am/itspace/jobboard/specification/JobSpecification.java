package am.itspace.jobboard.specification;

import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.enums.Status;
import am.itspace.jobboard.entity.enums.WorkExperience;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class JobSpecification {

    public static Specification<Job> searchJobs(String title, List<WorkExperience> workExperiences, List<Status> statuses, Category category, double fromSalary, double toSalary, Boolean isDeleted) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (title != null && !title.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }

            if (workExperiences != null && !workExperiences.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("workExperience").in(workExperiences));
            }

            if (statuses != null && !statuses.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("status").in(statuses));
            }

            if (category != null) {
                Join<Job, Category> categoryJoin = root.join("category", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(categoryJoin, category));
            }

            if (fromSalary >= 0 && toSalary >= fromSalary) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.between(root.get("salary"), fromSalary, toSalary)
                );
            }
            if (isDeleted != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
            }
            return predicate;
        };
    }
}