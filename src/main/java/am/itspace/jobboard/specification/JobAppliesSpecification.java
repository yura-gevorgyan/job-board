package am.itspace.jobboard.specification;

import am.itspace.jobboard.entity.JobApplies;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Calendar;
import java.util.Date;

public class JobAppliesSpecification {

    public static Specification<JobApplies> filterByStatusAndLastDate(String status, String date, int jobSeekerId, boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (status != null && !status.isEmpty() && !status.equals("0")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("jobAppliesStatus"), status));
            }

            if (date != null && !date.isEmpty()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());

                switch (date) {
                    case "day" -> calendar.add(Calendar.DAY_OF_MONTH, -1);
                    case "week" -> calendar.add(Calendar.DAY_OF_MONTH, -7);
                    case "month" -> calendar.add(Calendar.MONTH, -1);
                    default -> throw new IllegalArgumentException("Incorrect Date Format!");
                }

                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("sendDate"), calendar.getTime()));
            }

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("toJobSeeker").get("id"), jobSeekerId));

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isActive"), isActive));
            return predicate;
        };
    }
}
