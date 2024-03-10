package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    int countBy();

    int countByEmailContaining(String email);

    int countByEmailContainingAndRole(String email, Role role);

    int countByRole(Role role);

    Page<User> findAllByEmailContainingAndRole(PageRequest pageRequest, String email, Role role);

    Page<User> findAllByEmailContaining(PageRequest pageRequest, String email);

    Page<User> findAllByRole(PageRequest pageRequest, Role role);

    Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token);
    List<User> findUserByActivated(boolean isActive);
}
