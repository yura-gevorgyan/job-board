package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Integer> {

    int countBy();

    List<Resume> findTop6ByOrderByCreatedDateDesc();

    int countByUserEmailContaining(String email);

    int countByCategoryId(int id);

    int countByUserEmailContainingAndCategoryId(String email, int id);

    Page<Resume> findAllByUserEmailContaining(PageRequest pageRequest, String email);

    Page<Resume> findAllByCategoryId(PageRequest pageRequest, int id);

    Page<Resume> findAllByUserEmailContainingAndCategoryId(PageRequest pageRequest, String email, int id);

    Optional<Resume> findByUser(User user);

    Optional<Resume> findByUserIdAndIsActiveTrue(int userId);

    Page<Resume> findAllByIsActiveTrue(Pageable pageable);

    Page<Resume> findAll(Specification<Resume> resumeSpecification,Pageable pageable);

    @Query(value = "SELECT * FROM resume where is_active = true ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Resume> findRandomResumes(int limit);

}

