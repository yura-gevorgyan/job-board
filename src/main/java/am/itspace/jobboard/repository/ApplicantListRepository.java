package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicantListRepository extends JpaRepository<ApplicantList, Integer> {

    List<ApplicantList> findAllByToEmployer(User user);

    Optional<ApplicantList> findByToEmployerIdAndResumeIdAndIsActiveTrue(int employerId, int resumeId);

}

