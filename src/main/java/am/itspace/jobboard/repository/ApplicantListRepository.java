package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicantListRepository extends JpaRepository<ApplicantList, Integer> {

    List<ApplicantList> findAllByToEmployer(User user);

    Page<ApplicantList> findAllByResumeIdAndIsActiveTrue(int resumeId, Pageable pageable);

    Page<ApplicantList> findAll(Specification<ApplicantList> specification, Pageable pageable);

    Page<ApplicantList> findAllByResumeIdAndIsActiveTrue(Specification<ApplicantList> specification, Pageable pageable, int resumeId);

    Optional<ApplicantList> findByToEmployerIdAndResumeIdAndIsActiveTrue(int employerId, int resumeId);

    Optional<ApplicantList> findByJobIdAndResumeIdAndIsActiveTrue(int jobId, int resumeId);
}
