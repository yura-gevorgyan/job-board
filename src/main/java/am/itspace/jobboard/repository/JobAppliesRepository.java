package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobAppliesRepository extends JpaRepository<JobApplies, Integer> {

    List<JobApplies> findAllByToJobSeeker(User user);

    Page<JobApplies> findAllByToJobSeekerIdAndIsActiveTrue(int jobSeekerId, Pageable pageable);

    Page<JobApplies> findAll(Specification<JobApplies> specification, Pageable pageable);

    Optional<JobApplies> findByJobIdAndToJobSeekerIdAndIsActiveTrue(int jobId, int UserId);

    void deleteAllByJobId(int id);

    List<JobApplies> findAllByJob(Job job);
}
