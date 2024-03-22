package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

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

    Optional<Resume> findByUserId(int userId);

}

