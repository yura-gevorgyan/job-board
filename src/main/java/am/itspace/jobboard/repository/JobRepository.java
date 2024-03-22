package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer> {

    int countBy();

    List<Job> findTop4ByOrderByPublishedDateDesc();

    int countByTitleContaining(String title);

    Page<Job> findAllByTitleContaining(PageRequest pageRequest, String title);

    int countByCompanyId(int companyId);

    List<Job> getAllByCompanyId(int companyId);

    List<Job> getAllByUser(User user);

    List<Job> findTop6ByOrderByPublishedDateDesc();

    Page<Job> findByIsDeletedFalse(Pageable pageable);

    Page<Job> findAll(Specification<Job> specification, Pageable pageable);

    Optional<Job> findByUserId(int userId);
}

