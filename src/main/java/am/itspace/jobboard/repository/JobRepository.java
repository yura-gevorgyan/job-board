package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    Page<Job> findByIsDeletedFalse(Pageable pageable);

    Page<Job> findAll(Specification<Job> specification, Pageable pageable);

    List<Job> findByUserId(int userId);

    int countByCompanyIdAndIsDeletedFalse(int companyId);

    @Query(value = "SELECT * FROM job where is_deleted = false ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Job> findRandomJobs(int limit);

    List<Job> findTop8ByCompanyIdAndIsDeletedFalse(int id);

    Optional<Job> findByIdAndIsDeletedFalse(int id);

    List<Job> findAllByUserIdAndIsDeletedFalse(int id);

    List<Job> findAllByCompanyIdAndIsDeletedFalse(int companyId);

    Page<Job> findByUserId(Pageable pageable, int userId);
}

