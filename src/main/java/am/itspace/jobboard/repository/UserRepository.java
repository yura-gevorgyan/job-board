package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    int countBy();

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndActivatedTrueAndIsDeletedFalse(int id);

    Optional<User> findByToken(String token);

    List<User> findUserByActivated(boolean isActive);

    List<User> findUserByPasswordAndActivatedFalse(String password);

    List<User> findTop4ByOrderByRegisterDateDesc();

    Optional<User> findByIdAndIsDeletedFalse(int id);
}
