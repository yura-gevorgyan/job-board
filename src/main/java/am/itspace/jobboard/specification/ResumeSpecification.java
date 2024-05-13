package am.itspace.jobboard.specification;

import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.enums.WorkExperience;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ResumeSpecification {

    public static Specification<Resume> searchResumes(String profession, List<WorkExperience> workExperiences, String gender, Category category, double fromSalary, double toSalary, Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (profession != null && !profession.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("profession"), "%" + profession + "%"));
            }

            if (workExperiences != null && !workExperiences.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("workExperience").in(workExperiences));
            }

            if (gender != null && !gender.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("gender").in(gender));
            }

            if (category != null) {
                Join<Job, Category> categoryJoin = root.join("category", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(categoryJoin, category));
            }

            if (fromSalary >= 0 && toSalary >= fromSalary) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.between(root.get("expectedSalary"), fromSalary, toSalary)
                );
            }
            if (isActive != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isActive"), isActive));
            }
            return predicate;
        };
    }
}