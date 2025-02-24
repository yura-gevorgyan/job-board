package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicantListRepository extends JpaRepository<ApplicantList, Integer> {

    List<ApplicantList> findAllByToEmployer(User user);

    Page<ApplicantList> findAllByResumeIdAndIsActiveTrue(int resumeId, Pageable pageable);

    Page<ApplicantList> findAllByToEmployerId(int toEmployerId, Pageable pageable);

    Page<ApplicantList> findAll(Specification<ApplicantList> specification, Pageable pageable);

    Optional<ApplicantList> findByToEmployerIdAndResumeIdAndIsActiveTrue(int employerId, int resumeId);

    Optional<ApplicantList> findByJobIdAndResumeIdAndIsActiveTrue(int jobId, int resumeId);

    void deleteAllByResumeId(int id);

    void deleteAllByJobId(int id);

    List<ApplicantList> findAllByResume(Resume resume);
}