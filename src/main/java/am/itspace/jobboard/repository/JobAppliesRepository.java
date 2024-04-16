package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobAppliesRepository extends JpaRepository<JobApplies, Integer> {

    List<JobApplies> findAllByToJobSeeker(User user);

    List<JobApplies> findAllByToJobSeekerIdAndIsActiveTrue(int jobSeekerId);

    Page<JobApplies> findAllByToJobSeekerIdAndIsActiveTrue(Specification<JobApplies> specification, int jobSeekerId, Pageable pageable);

    Page<JobApplies> findAllByToJobSeekerIdAndIsActiveTrue(int jobSeekerId, Pageable pageable);

    Page<JobApplies> findAll(Specification<JobApplies> specification, Pageable pageable);

    Optional<JobApplies> findByJobIdAndToJobSeekerIdAndIsActiveTrue(int jobId,int UserId);

}

