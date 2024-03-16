package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobAppliesRepository extends JpaRepository<JobApplies, Integer> {

    List<JobApplies> findAllByToJobSeeker(User user);

}
